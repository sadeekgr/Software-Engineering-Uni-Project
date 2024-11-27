package it.polimi.ingsw.view.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * Controller class for managing the lobby view in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class Lobby extends SceneController {

    @FXML
    private Label lobbyIdLabel;

    @FXML
    private Label playerCountLabel;

    @FXML
    private ListView<String> playerListView;

    int max;

    /**
     * Sets the lobby ID label to display the specified lobby ID.
     *
     * @param lobbyId The ID of the lobby to display.
     */
    public void setLobbyId(String lobbyId) {
        lobbyIdLabel.setText("Lobby ID: " + lobbyId);
    }

    /**
     * Updates the player count label to show the current number of players and the maximum allowed.
     *
     * @param currentPlayers The current number of players in the lobby.
     * @param maxPlayers     The maximum number of players allowed in the lobby.
     */
    public void setPlayerCount(int currentPlayers, int maxPlayers) {
        max = maxPlayers;
        playerCountLabel.setText(currentPlayers + "/" + maxPlayers + " Players");
    }

    /**
     * Updates the player list with the provided array of player names.
     *
     * @param players An array of player names to display in the player list.
     */
    public void updatePlayerList(String[] players) {
        playerListView.getItems().clear();
        playerListView.getItems().addAll(players);
    }

    /**
     * Updates the player list when a new player joins the lobby.
     *
     * @param username The username of the player who joined.
     */
    public void joinedLobby(String username){
        Platform.runLater(() -> {
            playerListView.getItems().add(username);
            setPlayerCount(playerListView.getItems().size(), max);
        });
    }

    /**
     * Updates the player list when a player leaves the lobby.
     *
     * @param username The username of the player who left.
     */
    public void leftLobby(String username){
        Platform.runLater(() -> {
            playerListView.getItems().remove(username);
            setPlayerCount(playerListView.getItems().size(), max);
        });
    }

    /**
     * Retrieves the player list view.
     *
     * @return The {@link javafx.scene.control.ListView} containing the list of players.
     */
    public ListView<String> getPlayerListView() {
        return playerListView;
    }

}
