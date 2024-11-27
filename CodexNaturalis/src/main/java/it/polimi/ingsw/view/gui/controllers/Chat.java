package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.message.ChatMessage;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;

/**
 * Controller class for managing chat functionality in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 * Implements {@link javafx.fxml.Initializable} to handle initialization.
 */
public class Chat extends SceneController implements Initializable{

    @FXML
    private TabPane tabPane;

    @FXML
    private ListView<String> publicChatListView;

    @FXML
    private TextField publicChatTextField;

    @FXML
    private Button sendPublicMsgButton;

    @FXML
    private Button createPrivateChatButton;

    private ListView<CheckBox> recipientListView;

    private static final ObservableList<String> publicChat = FXCollections.observableArrayList();
    private static final ObservableMap<Set<String>, ObservableList<String>> privateChats = FXCollections.observableHashMap();
    private static final BooleanProperty triggerClearData = new SimpleBooleanProperty(false);

    /**
     * Clears all chat messages (public and private).
     */
    public static void clearChats(){
        publicChat.clear();
        privateChats.clear();
        triggerClearData.set(true);
    }

    /**
     * Initializes the controller.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources specific to the locale.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createPrivateChatButton.setOnAction(event -> toggleRecipientListView());
        sendPublicMsgButton.setOnAction(event -> handlePublicChatSend());
        publicChatListView.setItems(publicChat);
        privateChats.addListener(this::createPrivateChat);

        // Initialize existing private chats
        for (Map.Entry<Set<String>, ObservableList<String>> entry : privateChats.entrySet()) {
            openExistingPrivateChat(entry.getKey(), entry.getValue());
        }

        // Listen for changes in triggerClearData
        triggerClearData.addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                // Perform actions when triggerClearData changes to true
                clearData();
                // Reset triggerClearData to false after clearing data
                triggerClearData.set(false);
            }
        });
    }

    /**
     * Opens an existing private chat tab with the given recipients and messages.
     *
     * @param recipients The set of recipients for the private chat.
     * @param messages   The list of messages in the private chat.
     */
    private void openExistingPrivateChat(Set<String> recipients, ObservableList<String> messages) {
        String tabTitle = String.join(", ", recipients);
        Tab privateChatTab = new Tab(tabTitle);
        ListView<String> privateChatListView = new ListView<>();
        TextField privateChatTextField = new TextField();
        Button sendPrivateMsgButton = new Button("send");

        AnchorPane privateChatPane = createChatPane(privateChatListView, privateChatTextField, sendPrivateMsgButton, recipients);

        privateChatTab.setContent(privateChatPane);
        tabPane.getTabs().add(privateChatTab);

        privateChatListView.setItems(messages);

        Set<String> tmp = new HashSet<>(recipients);
        sendPrivateMsgButton.setOnAction(event -> handlePrivateChatSend(tmp, privateChatTextField));
    }

    /**
     * Handles sending a message in the public chat.
     * Clears the text field after sending.
     */
    private void handlePublicChatSend() {
        // Action to perform when the public chat send button is clicked
        String message = publicChatTextField.getText();
        if (!message.isEmpty()) {
            sendMessage(message);
            publicChatTextField.clear();
        }
    }

    /**
     * Toggles the visibility of the recipient list view and prompts the user to select recipients for a private chat.
     */
    @FXML
    private void toggleRecipientListView() {
        populateRecipientListView();

        // Create the dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Select Recipients");

        alert.getDialogPane().setContent(recipientListView);

        // Create the create chat button and add it to the dialog
        ButtonType createChatButton = new ButtonType("Create Chat");

        alert.getButtonTypes().add(createChatButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == createChatButton) {
            // Raccogli i destinatari selezionati
            Set<String> recipients = new HashSet<>();
            for (CheckBox checkBox : recipientListView.getItems()) {
                if (checkBox.isSelected()) {
                    recipients.add(checkBox.getText());
                }
            }
            //recipients.add(getListener().getGameState().getUser());

            if (!privateChats.containsKey(recipients) && !(recipients.size() == recipientListView.getItems().size())){
                privateChats.put(recipients, FXCollections.observableArrayList());
                for (Map.Entry<Set<String>, ObservableList<String>> entry : privateChats.entrySet()) {
                    Set<String> recipientsSet = entry.getKey();
                    //ListView<String> listView = (ListView<String>) entry.getValue();

                    //System.out.println("-----prima Recipients: " + recipientsSet);
                    //System.out.println("ListView: " + listView);
                }
            }
        }
    }

    /**
     * Populates the recipient list view with users from the lobby, excluding the current user.
     */
    private void populateRecipientListView() {
        if (recipientListView == null) {
            recipientListView = new ListView<>();
        }
        recipientListView.getItems().clear(); // Clear existing items if any
        for (String user : getUsers().getItems()) {
            if (!user.equals(getListener().getGameState().getUser())) {
                recipientListView.getItems().add(new CheckBox(user));
            }
        }
    }

    /**
     * Handles sending a message in a private chat with the specified recipients.
     * Clears the text field after sending.
     *
     * @param recipients The recipients of the private message.
     * @param textField  The text field containing the message to send.
     */
    private void handlePrivateChatSend(Set<String> recipients, TextField textField) {
        // Action to perform when the private chat send button is clicked
        String message = textField.getText();
        if (!message.isEmpty()) {
            recipients.remove(getListener().getGameState().getUser());
            sendMessage(message, recipients);
            textField.clear();
        }
    }

    /**
     * Creates a new private chat tab when a change is detected in the private chats map.
     *
     * @param change The change event containing the recipients and messages for the new private chat.
     */
    private void createPrivateChat(javafx.collections.MapChangeListener.Change<? extends Set<String>, ? extends ObservableList<String>> change){
        Tab privateChatTab = new Tab("Private Chat");
        ListView<String> privateChatListView = new ListView<>();
        TextField privateChatTextField = new TextField();
        Button sendPrivateMsgButton = new Button("send");

        AnchorPane privateChatPane = createChatPane(privateChatListView, privateChatTextField, sendPrivateMsgButton, change.getKey());

        privateChatTab.setContent(privateChatPane);
        tabPane.getTabs().add(privateChatTab);

        privateChatListView.setItems(change.getValueAdded());

        Set<String> tmp = new HashSet<>(change.getKey());

        sendPrivateMsgButton.setOnAction(event -> handlePrivateChatSend(tmp, privateChatTextField));
    }

    /**
     * Sends a public chat message to all players.
     *
     * @param m The message to send.
     */
    private void sendMessage(String m){
        ChatMessage message = new ChatMessage(m, new Date());
        getListener().sendChatMessage(message);
    }

    /**
     * Sends a private chat message to specific recipients.
     *
     * @param m    The message to send.
     * @param recipients The recipients of the message.
     */
    private void sendMessage(String m, Set<String> recipients){
        ChatMessage message = new ChatMessage(m, new Date(), recipients);
        getListener().sendChatMessage(message);
    }

    /**
     * Receives a chat message from another user.
     * Updates the public chat or the appropriate private chat tab.
     *
     * @param m The received chat message.
     */
    public void receiveMessage(ChatMessage m){
        Platform.runLater(() -> {
            String sender = m.getSender();
            String message = m.getMessage();

            if(m.getChatType() == ChatMessage.ChatType.PUBLIC){
                publicChat.add(sender + ": " + message);
            }
            else {
                Set<String> rec = new HashSet<>(m.getRecipients());
                rec.add(sender);
                rec.remove(getListener().getGameState().getUser());

                for (Map.Entry<Set<String>, ObservableList<String>> entry : privateChats.entrySet()) {
                    Set<String> recipientsSet = entry.getKey();
                    //ListView<String> listView = (ListView<String>) entry.getValue();

                    //System.out.println("Recipients: " + recipientsSet);
                    // System.out.println("ListView: " + listView);
               }

                //System.out.println("-----dopo" + rec);

                if (privateChats.containsKey(rec)){
                    privateChats.get(rec).add(sender + ": " + message);
                } else {
                    privateChats.put(rec, FXCollections.observableArrayList());
                    privateChats.get(rec).add(sender + ": " + message);
                }

            }
        });
    }

    /**
     * Creates a chat pane with a list view of messages, a text field for typing messages,
     * a button for sending messages, and a list view of recipients.
     *
     * @param chatListView     The list view displaying chat messages.
     * @param chatTextField    The text field for typing messages.
     * @param sendMsgButton    The button for sending messages.
     * @param recipients       The recipients of the private chat.
     * @return The created anchor pane containing the chat components.
     */
    private AnchorPane createChatPane(ListView<String> chatListView, TextField chatTextField, Button sendMsgButton, Set<String> recipients) {
        AnchorPane chatPane = new AnchorPane();
        chatPane.setPrefSize(600, 400);

        chatListView.setLayoutX(14.0);
        chatListView.setLayoutY(14.0);
        chatListView.setPrefSize(572, 336);

        Set<String> tmpRec = new HashSet<>(recipients);
        tmpRec.add(getListener().getGameState().getUser());

        ObservableList<String> observableList = FXCollections.observableArrayList(tmpRec);
        ListView<String> listView = new ListView<>(observableList);

        listView.setLayoutX(14.0);
        listView.setLayoutY(220.0);
        listView.setPrefSize(572, 70);

        chatTextField.setLayoutX(20.0);
        chatTextField.setLayoutY(293.0);
        chatTextField.setPrefSize(450, 38);

        sendMsgButton.setLayoutX(488.0);
        sendMsgButton.setLayoutY(303.0);
        sendMsgButton.setPrefSize(77, 30);

        chatPane.getChildren().addAll(chatListView, chatTextField, sendMsgButton, listView);

        return chatPane;
    }

    /**
     * Retrieves the list view of users from the lobby.
     *
     * @return The list view of users.
     */
    public ListView<String> getUsers(){
        Lobby lobbyController = (Lobby)getListener().getController("Lobby");
        return lobbyController.getPlayerListView();
    }

    /**
     * Clears all chat data, including public messages and private chat tabs.
     */
    private void clearData(){
        // remove public messages
        publicChatListView.getItems().clear();

        ObservableList<javafx. scene. control. Tab> tabs = tabPane.getTabs();

        //remove all the private tabs
        if (tabs.size() > 1) {
            tabs.subList(1, tabs.size()).clear();
        }
    }
}
