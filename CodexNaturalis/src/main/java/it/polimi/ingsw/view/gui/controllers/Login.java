package it.polimi.ingsw.view.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Controller class for managing the login view in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class Login extends SceneController {
    @FXML
    private TextField usernameTextField;

    @FXML
    private ComboBox connectionType;

    @FXML
    private TextField serverIP;

    /**
     * Handles the login action when the login button is clicked.
     * Validates server IP, establishes connection with the server, and logs in with the provided username.
     *
     * @param actionEvent The action event that triggers the method (typically a button click).
     */
    @FXML
    public void login(ActionEvent actionEvent) {
        //Set up server IP
        String IP = serverIP.getText();
        if(!getListener().setUpServerIp(IP)){
            getListener().showAlert("Invalid Server IP");
            return;
        }

        // Establish Connection with the server
        if(!getListener().setUpConnection((String) connectionType.getValue())){
            getListener().showAlert("Failed to connect with the Server");
            return;
        }

        // login
        String username = usernameTextField.getText();
        if (username.isEmpty()) {
            getListener().showAlert("username Required");
        } else {
            if (SceneController.getListener() != null) {
                SceneController.getListener().login(username);
            }
        }
    }
}