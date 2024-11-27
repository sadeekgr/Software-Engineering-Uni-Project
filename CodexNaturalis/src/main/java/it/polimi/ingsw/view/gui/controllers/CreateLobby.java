package it.polimi.ingsw.view.gui.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * Controller class for managing the "Create Lobby" view in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class CreateLobby extends SceneController {
    @FXML
    private ComboBox<String> numberOfPlayersComboBox;

    @FXML
    private Button createButton;

    @FXML
    private Label messageLabel;

    /**
     * Initializes the controller. Sets up the number of players combo box.
     */
    @FXML
    public void initialize() {
        // Inizializzazione del controller, se necessario
        numberOfPlayersComboBox.setItems(FXCollections.observableArrayList("2", "3", "4"));
    }

    /**
     * Event handler for the create lobby button.
     * Creates a lobby with the selected number of players.
     *
     * @param actionEvent The action event that triggers the method (typically a button click).
     */
    @FXML
    public void createLobby(ActionEvent actionEvent) {
        String selectedNumberOfPlayers = numberOfPlayersComboBox.getValue();
        if (selectedNumberOfPlayers != null) {
            SceneController.getListener().createLobby(Integer.parseInt(selectedNumberOfPlayers));
        }
    }

    /**
     * Retrieves the create lobby button.
     *
     * @return The create lobby button instance.
     */
    public Button getCreateLobbyButton() {
        return createButton;
    }
}
