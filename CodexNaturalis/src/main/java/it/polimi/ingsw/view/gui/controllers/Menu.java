package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.message.lobby.GetLobbiesMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for managing the main menu view in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class Menu extends SceneController {
    @FXML
    private ListView<String> lobbyListView;
    @FXML
    private Button refreshButton;
    @FXML
    private Button joinLobbyButton;
    @FXML
    private Button createLobbyButton;

    /**
     * Refreshes the list of lobbies displayed in the lobbyListView.
     * Sends a {@link it.polimi.ingsw.message.lobby.GetLobbiesMessage} to retrieve updated lobby data.
     */
    @FXML
    private void refreshLobbies() {
        // Update lobbyListView with refreshed lobby data
        getListener().sendMessage(new GetLobbiesMessage());
    }

    /**
     * Updates the lobbyListView with the provided list of lobby names.
     *
     * @param lobbies A list of lobby names to display in the lobbyListView.
     */
    public void update(List<String> lobbies){
        Platform.runLater(()->{
        lobbyListView.getItems().clear();
        lobbyListView.getItems().addAll(lobbies);
        });
    }

    /**
     * Handles the action when the create lobby button is clicked.
     * Opens a new window to create a new lobby.
     */
    @FXML
    public void createLobby(ActionEvent actionEvent) {
        // Code to create a new lobby goes here
        // Update lobbyListView with new lobby data
        SceneController.getListener().openCreatePage();
    }

    /**
     * Handles the action when the join lobby button is clicked.
     * Retrieves the selected lobby from the lobbyListView and sends a request to join that lobby.
     */
    @FXML
    public void joinLobby(ActionEvent actionEvent) {
        String selectedLobby = lobbyListView.getSelectionModel().getSelectedItem();
        if (selectedLobby != null) {
            // Code to join the selected lobby goes here
            //c.send(new JoinLobbyMessage(Integer.parseInt(selectedLobby)));
            String[] parts = selectedLobby.split(" ");
            String id = parts[2];
            SceneController.getListener().joinLobby(Integer.parseInt(id));
        } else {
            System.out.println("Selected lobby is null");
        }
    }

    public Button getJoinLobbyButton() {
        return joinLobbyButton;
    }

    public Button getCreateLobbyButton() {
        return createLobbyButton;
    }

    private void openCreateLobbyPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createLobby.fxml"));
            Parent root = loader.load();
            CreateLobby createLobbyController = loader.getController();
            createLobbyController.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
