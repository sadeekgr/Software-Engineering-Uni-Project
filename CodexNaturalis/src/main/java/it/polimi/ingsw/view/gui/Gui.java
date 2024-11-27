package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.gamestate.GameState;
import it.polimi.ingsw.message.ChatMessage;
import it.polimi.ingsw.message.LoginMessage;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.lobby.CreateLobbyMessage;
import it.polimi.ingsw.message.lobby.GetLobbiesMessage;
import it.polimi.ingsw.message.lobby.JoinLobbyMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.*;

/**
 * Gui class represents the main GUI application for the game Codex Naturalis.
 * It handles the initialization of scenes, controllers, and the primary stage.
 * It also provides methods to switch scenes, send messages, and handle game states.
 */
public class Gui extends Application {
    GuiHandler guiHandler;

    private Stage primaryStage;
    private Stage fieldWindow = null;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();

    /**
     * The main entry point for the JavaFX application.
     *
     * @param stage the primary stage for this application
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        // Initialize GUI handler
        guiHandler = new GuiHandler(this);

        // Load all scenes and their controllers
        loadControllerSceneMap();

        // Set unique listener for scenes
        SceneController.setListener(this);

        // Set initial stage properties
        stage.setTitle("Codex Naturalis");

        // Set the initial scene to login
        stage.setScene(scenes.get("Login"));
        //stage.setFullScreen(true);
        //stage.setMaximized(true);

        // Set the default close operation for the primary stage
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        stage.show();
    }

    /**
     * Loads multiple FXML files and stores their Scenes and controllers.
     *
     */
    private void loadControllerSceneMap() {
        String[] fxmlFiles = {"/fxml/login.fxml", "/fxml/menu.fxml", "/fxml/createLobby.fxml", "/fxml/lobby.fxml", "/fxml/chat.fxml", "/fxml/table.fxml", "/fxml/field.fxml", "/fxml/scores.fxml"};
        String[] sceneNames = {"Login", "Menu", "Create", "Lobby", "Chat", "Table", "Field", "Scores"};

        for (int i = 0; i < fxmlFiles.length; i++) {
            loadFxml(sceneNames[i], fxmlFiles[i]);
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reconnection.fxml"));
            scenes.put("Reconnection", new Scene(fxmlLoader.load()));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads an FXML file into a Scene and associates it with a controller.
     *
     * @param sceneName The name to associate with the loaded Scene.
     * @param fxmlPath  The path to the FXML file to load.
     */
    private void loadFxml(String sceneName, String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            scenes.put(sceneName, new Scene(fxmlLoader.load()));
            controllers.put(sceneName, fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches the current scene to the specified scene.
     *
     * @param sceneName the name of the scene to switch to
     */
    public void switchScene(String sceneName) {
        primaryStage.setScene(scenes.get(sceneName));
    }

    /**
     * Opens the menu page.
     */
    public void openMenuPage(){
        guiHandler.setGameState(guiHandler.getGameState().getUser()); // clear data
        Chat.clearChats();

        Platform.runLater(() -> switchScene("Menu"));

        Message m1 = new GetLobbiesMessage();
        sendMessage(m1);
    }

    /**
     * Opens the create lobby page.
     */
    public void openCreatePage(){
        Platform.runLater(()-> {
            try {
                // Retrieve the preloaded Menu controller and scene
                CreateLobby createController = (CreateLobby) controllers.get("Create");
                createController.initialize();

                switchScene("Create");
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    /**
     * Opens the lobby page with the specified lobby information.
     *
     * @param lobbyId the ID of the lobby
     * @param players the list of players in the lobby
     * @param numMax the maximum number of players in the lobby
     */
    public void openLobby(int lobbyId, List<String> players, int numMax){
        Platform.runLater(()-> {
            try {
                Lobby lobbyController = (Lobby) controllers.get("Lobby");
                lobbyController.setLobbyId(Integer.toString(lobbyId));
                String joinedString = String.join(",", players);
                String[] stringArray = joinedString.split(",");
                lobbyController.updatePlayerList(stringArray);
                lobbyController.setPlayerCount(players.size(), numMax);

                switchScene("Lobby");
            } catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    /**
     * Opens the field window.
     */
    public void openField(){
        Platform.runLater(()-> {
            if (fieldWindow == null) {
                fieldWindow = new Stage();
                fieldWindow.setTitle("Player Field");
                fieldWindow.setScene(scenes.get("Field"));
                fieldWindow.show();
            }

            if (!fieldWindow.isShowing()) {
                fieldWindow.show();
            } else {
                fieldWindow.toFront();
                fieldWindow.requestFocus();
            }
        });
    }

    /**
     * Gets the width of the field stage.
     *
     * @return the width of the field stage.
     */
    public double getFieldStageWidth(){
        if(fieldWindow == null){
            return 0;
        }
        return fieldWindow.getScene().getWidth();
    }

    /**
     * Gets the height of the field stage.
     *
     * @return the height of the field stage.
     */
    public double getFieldStageHeight(){
        if(fieldWindow == null){
            return 0;
        }
        return fieldWindow.getScene().getHeight();
    }

    /**
     * Opens the table page.
     */
    public void openTablePage(){
        Platform.runLater(()-> {
            try {
                ((Table) getController("Table")).loadGameState();
                switchScene("Table");
            } catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    /**
     * Opens the scores page.
     *
     * @param winners the number of winners.
     * @param players the list of players.
     * @param names the list of player names.
     * @param scores the list of player scores.
     * @param objs the list of objectives.
     */
    public void openScores(int winners, List<PlayerColor> players, List<String> names, List<Integer> scores, List<String> objs){
        Platform.runLater(()-> {
            try {
                // Close field window
                if(fieldWindow != null)
                    fieldWindow.close();

                Scores scoresController = (Scores) controllers.get("Scores");
                scoresController.setScores(winners, players, names, scores, objs);
                switchScene("Scores");
            } catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    /**
     * Logs in the user with the specified username.
     *
     * @param username the username to log in with.
     */
    public void login(String username){
        guiHandler.setGameState(username);
        Message m = new LoginMessage(username);
        guiHandler.sendMessage(m);
    }

    /**
     * Creates a new lobby with the specified number of players.
     *
     * @param numPlayers the number of players for the lobby.
     */
    public void createLobby(int numPlayers){
        Message m1 = new CreateLobbyMessage(numPlayers);
        guiHandler.sendMessage(m1);
    }

    /**
     * Joins the lobby with the specified ID.
     *
     * @param lobbyId the ID of the lobby to join.
     */
    public void joinLobby(int lobbyId){
        Message m2 = new JoinLobbyMessage(lobbyId);
        guiHandler.sendMessage(m2);
    }

    /**
     * Gets the controller for the specified scene.
     *
     * @param s the name of the scene.
     * @return the controller for the specified scene.
     */
    public Object getController(String s){
        return controllers.get(s);
    }

    /**
     * Sends a chat message.
     *
     * @param m the chat message to send.
     */
    public void sendChatMessage(ChatMessage m){
        guiHandler.sendMessage(m);
    }

    /**
     * Sends a message.
     *
     * @param m the message to send.
     */
    public void sendMessage(Message m) { guiHandler.sendMessage(m); }

    /**
     * Gets the current game state.
     *
     * @return the current game state.
     */
    public GameState getGameState(){
        return guiHandler.getGameState();
    }

    /**
     * Gets the primary stage.
     *
     * @return the primary stage.
     */
    public Stage getPrimaryStage(){ return primaryStage; }

    /**
     * Shows an alert with the specified message.
     *
     * @param message the message to display in the alert.
     */
    public void showAlert(String message) {
        showAlert(message, null);
    }

    /**
     * Shows an alert with the specified message and an action to run on close.
     *
     * @param message the message to display in the alert.
     * @param onClose the action to run when the alert is closed.
     */
    public void showAlert(String message, Runnable onClose){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(message);

            alert.initOwner(primaryStage);

            alert.setOnShown(event -> {
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();

                double ownerX = primaryStage.getX();
                double ownerY = primaryStage.getY();
                double ownerWidth = primaryStage.getWidth();
                double ownerHeight = primaryStage.getHeight();

                double alertWidth = alertStage.getWidth();
                double alertHeight = alertStage.getHeight();

                alertStage.setX(ownerX + (ownerWidth - alertWidth) / 2);
                alertStage.setY(ownerY + (ownerHeight - alertHeight) / 2);
            });

            if(onClose != null) {
                // Set the action to be taken when the alert is closed
                alert.setOnHidden(event -> onClose.run());
            }

            alert.showAndWait();
        });
    }

    /**
     * Sets up the connection to the server.
     *
     * @param type the type of connection ("SOCKET" or "RMI").
     * @return true if the connection was set up successfully, false otherwise.
     */
    public boolean setUpConnection(String type){
        if (guiHandler.getConnection() == null) {
            if (Objects.equals(type, "SOCKET")) {
                try {
                    guiHandler.setUpSocketConnection();
                } catch (IOException e) {
                    return false;
                }
            } else if (Objects.equals(type, "RMI")) {
                try {
                    guiHandler.setUpRMIConnection();
                } catch (IOException | NotBoundException e) {
                    return false;
                }
            } else {
                return false;
            }

            return guiHandler.getConnection() != null;
        }

        return true;
    }

    /**
     * Sets the server IP address.
     *
     * @param serverIP the IP address of the server.
     * @return true if the server IP was set successfully, false otherwise.
     */
    public boolean setUpServerIp(String serverIP){
        return guiHandler.setServerAddress(serverIP);
    }

    /**
     * Closes the application and releases any resources.
     */
    public void close(){
        // Ensure all JavaFX operations are run on the JavaFX application thread
        Platform.runLater(() -> {
            try {
                // If there is a field window open, close it
                if (fieldWindow != null) {
                    fieldWindow.close();
                }

                // Close any other resources or connections here
                if (guiHandler.getConnection() != null) {
                    guiHandler.getConnection().close(); // Assuming there is a close method for the connection
                }

                // Stop the primary stage, which should close the GUI
                if (primaryStage != null) {
                    primaryStage.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Exit the application
            Platform.exit();
            System.exit(0);
        });
    }
}
