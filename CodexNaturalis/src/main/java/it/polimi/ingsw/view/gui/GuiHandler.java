package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.lobby.LobbyInfo;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.message.error.ErrorMessage;
import it.polimi.ingsw.message.lobby.GetLobbiesResponseMessage;
import it.polimi.ingsw.message.lobby.PlayerJoinedLobbyMessage;
import it.polimi.ingsw.message.lobby.PlayerLeftLobbyMessage;
import it.polimi.ingsw.message.notify.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles incoming messages from the server and updates the GUI accordingly.
 * It extends the abstract class `View` and implements various callback methods to process
 * specific types of messages received from the server.
 */
public class GuiHandler extends View {
    private final Gui gui;

    /**
     * Constructor for GuiHandler.
     *
     * @param gui The GUI instance associated with this handler.
     */
    public GuiHandler(Gui gui) {
        super();
        this.gui = gui;
    }

    /**
     * This method is empty as the logic is handled in the application.
     */
    @Override
    public void run() {
    } // does nothing, the logic is in the application

    /**
     * Override method to handle general messages received from the server.
     *
     * @param m The Message received.
     */
    @Override
    protected void onMessage(Message m) {
        System.out.println("Message " + m.getType());
        if (m.getType() == MessageType.INFO) {
            System.out.println("INFO: " + ((NotifyMessage) m).getNotifyType());
        }
    }

    /**
     * Override method to handle success messages received from the server.
     *
     * @param m The SuccessMessage received.
     */
    @Override
    protected void onSuccessMessage(SuccessMessage m) {
        switch (m.successType()) {
            case CREATE, JOIN:
                SuccessLobby lobby = (SuccessLobby) m;
                gui.openLobby(lobby.getId(), lobby.getPlayers(), lobby.getMaxNumPlayers());
                break;
            case LOGIN:
                gui.openMenuPage();
                break;
            case GAME:
            case LEAVE:
        }
    }

    /**
     * Override method to handle error messages received from the server.
     *
     * @param m The ErrorMessage received.
     */
    @Override
    protected void onErrorMessage(ErrorMessage m) {
        gui.showAlert("Error: " + m.getErrorMessage() + "\nCode: " + m.getErrorCode());
    }

    /**
     * Override method to handle GetLobbiesResponseMessage received from the server.
     *
     * @param m The GetLobbiesResponseMessage received.
     */
    @Override
    protected void onGetLobbiesMessage(GetLobbiesResponseMessage m) {
        ArrayList<String> lobbies = new ArrayList<>();
        for (LobbyInfo lobbyInfo : m.getLobbies()) {
            String s = "ID : " + lobbyInfo.getId() + "     " + lobbyInfo.getNumCurrentPlayers() + "/" + lobbyInfo.getMaxPlayers();
            lobbies.add(s);
        }
        ((Menu) gui.getController("Menu")).update(lobbies);
    }

    /**
     * Override method to handle PlayerJoinedLobbyMessage received from the server.
     *
     * @param m The PlayerJoinedLobbyMessage received.
     */
    @Override
    protected void onPlayerJoinedMessage(PlayerJoinedLobbyMessage m) {
        Lobby lobbyController = (Lobby) gui.getController("Lobby");
        lobbyController.joinedLobby(m.getUsername());
    }

    /**
     * Override method to handle PlayerLeftLobbyMessage received from the server.
     *
     * @param m The PlayerLeftLobbyMessage received.
     */
    @Override
    protected void onPlayerLeftMessage(PlayerLeftLobbyMessage m){
        Lobby lobbyController = (Lobby) gui.getController("Lobby");
        lobbyController.leftLobby(m.getUsername());
    }

    /**
     * Override method to handle GameStateMessage received from the server.
     *
     * @param m The GameStateMessage received.
     */
    @Override
    protected void onGameStateMessage(GameStateMessage m) {
        getGameState().load(m);

        List<String> players = m.players().keySet().stream().toList();
        String[] playerList = new String[players.size()];
        for(int i = 0; i < playerList.length; i++){
            playerList[i] = players.get(i);
        }
        ((Lobby) gui.getController("Lobby")).updatePlayerList(playerList);
        ((Table) gui.getController("Table")).loadInterruptedGame(m);
        gui.openTablePage();
    }

    /**
     * Override method to handle GameReconnectionMessage received from the server.
     *
     * @param m The GameReconnectionMessage received.
     */
    @Override
    protected void onReconnectionMessage(GameReconnectionMessage m) {
        Platform.runLater(() -> gui.switchScene("Reconnection"));
    }

    /**
     * Override method to handle FailedGameReconnectionMessage received from the server.
     *
     * @param m The FailedGameReconnectionMessage received.
     */
    @Override
    protected void onReconnectionFailedMessage(FailedGameReconnectionMessage m) {
        gui.showAlert("Reconnection Failed", gui::openMenuPage);
    }

    /**
     * Override method to handle FailedToStartMatchMessage received from the server.
     *
     * @param m The FailedToStartMatchMessage received.
     */
    @Override
    protected void onFailedToStartMatchMessage(FailedToStartMatchMessage m){
        gui.showAlert("Failed To Start Match\nServer Internal Error", gui::close);
    }

    /**
     * Override method to handle ChatMessage received from the server.
     *
     * @param m The ChatMessage received.
     */
    @Override
    protected void onChatMessage(ChatMessage m) {
        Chat chatController = (Chat) gui.getController("Chat");
        chatController.receiveMessage(m);
    }

    /**
     * Override method to handle NotifyToChooseStarter received from the server.
     *
     * @param notifyMsg The NotifyToChooseStarter received.
     */
    @Override
    protected void onChooseStarterMessage(NotifyToChooseStarter notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.chooseStarter(notifyMsg); // open pop up with starter card sides
    }

    /**
     * Override method to handle NotifyToChooseObjective received from the server.
     *
     * @param notifyMsg The NotifyToChooseObjective received.
     */
    @Override
    protected void onChooseObjectiveMessage(NotifyToChooseObjective notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.chooseObjective(notifyMsg); // open pop up with two objective card
    }

    /**
     * Override method to handle NotifyMatchStarted received from the server.
     *
     * @param m The NotifyMatchStarted received.
     */
    @Override
    protected void onMatchStartedMessage(NotifyMatchStarted m) {
        System.out.println("Match Started!!");
        gui.openTablePage();
    }

    /**
     * Override method to handle NotifyCardState received from the server.
     *
     * @param notifyMsg The NotifyCardState received.
     */
    @Override
    protected void onCardStateMessage(NotifyCardState notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.updateCardState();
    }

    /**
     * Override method to handle NotifyDraw received from the server.
     *
     * @param notifyMsg The NotifyDraw received.
     */
    @Override
    protected void onDrawMessage(NotifyDraw notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.updateDraw(notifyMsg);
    }

    /**
     * Override method to handle NotifyTurn received from the server.
     *
     * @param notifyMsg The NotifyTurn received.
     */
    @Override
    protected void onNotifyTurnMessage(NotifyTurn notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.notifyTurn(notifyMsg);
    }

    /**
     * Override method to handle NotifySetUpFinished received from the server.
     *
     * @param notifyMsg The NotifySetUpFinished received.
     */
    @Override
    protected void onSetUpEndedMessage(NotifySetUpFinished notifyMsg) {
        //Bo forse non serve
        Table tableController = (Table) gui.getController("Table");
        tableController.setUpEnd(notifyMsg);
    }

    /**
     * Override method to handle NotifyCardPlayed received from the server.
     *
     * @param notifyMsg The NotifyCardPlayed received.
     */
    @Override
    protected void onCardPlayedMessage(NotifyCardPlayed notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.cardPlayed(notifyMsg);
        ((FieldController) gui.getController("Field")).update();
    }

    /**
     * Override method to handle NotifyPlayerHand received from the server.
     *
     * @param notifyMsg The NotifyPlayerHand received.
     */
    @Override
    protected void onPlayerHandMessage(NotifyPlayerHand notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.setUpHands();
    }

    /**
     * Override method to handle NotifyColorsAssignment received from the server.
     *
     * @param notifyMsg The NotifyColorsAssignment received.
     */
    @Override
    protected void onColorAssignmentMessage(NotifyColorsAssignment notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.setColors();
    }

    @Override
    protected void onStarterCardsMessage(NotifyStarterCards notifyMsg) {} // forse non serve

    /**
     * Override method to handle NotifyEndMatch received from the server.
     *
     * @param notifyMsg The NotifyEndMatch received.
     */
    @Override
    protected void onEndMatchMessage(NotifyEndMatch notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.notifyEndMatch(notifyMsg);
    }

    /**
     * Override method to handle NotifyLastRound received from the server.
     *
     * @param notifyMsg The NotifyLastRound received.
     */
    @Override
    protected void onLastRoundMessage(NotifyLastRound notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.notifyLastTurn();
    }

    /**
     * Override method to handle NotifyChosenObjective received from the server.
     *
     * @param notifyMsg The NotifyChosenObjective received.
     */
    @Override
    protected void onObjectiveConfirmationMessage(NotifyChosenObjective notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.objectiveConfirmation(notifyMsg);
    }

    /**
     * Override method to handle NotifyChosenStarter received from the server.
     *
     * @param notifyMsg The NotifyChosenStarter received.
     */
    @Override
    protected void onStarterConfirmationMessage(NotifyChosenStarter notifyMsg) {} // forse non serve

    /**
     * Override method to handle NotifyGlobalObjectives received from the server.
     *
     * @param notifyMsg The NotifyGlobalObjectives received.
     */
    @Override
    protected void onGlobalObjectiveMessage(NotifyGlobalObjectives notifyMsg) {
        Table tableController = (Table) gui.getController("Table");
        tableController.setUpCommonObjective();
    }

    /**
     * Override method to handle unknown NotifyMessage received from the server.
     *
     * @param m The unknown NotifyMessage received.
     */
    @Override
    protected void onUnknownNotifyMessage(NotifyMessage m) {
        System.out.println("Unknown notify message.");
    }

    /**
     * Override method to handle unknown Message received from the server.
     *
     * @param m The unknown Message received.
     */
    @Override
    protected void onUnknownMessage(Message m) {
        System.out.println("Unknown action: " + m.getType());
    }

    /**
     * Method to handle disconnection from the server.
     */
    @Override
    public void disconnection(){
        gui.showAlert("Lost Connection with the Server!!", gui::close);
    }

}