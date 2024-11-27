package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.gamestate.GameState;
import it.polimi.ingsw.message.GameStateMessage;
import it.polimi.ingsw.message.action.DrawGold;
import it.polimi.ingsw.message.action.DrawMarket;
import it.polimi.ingsw.message.action.DrawResource;
import it.polimi.ingsw.message.notify.*;

import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utilities.ImagePath;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.*;

/**
 * Controller class representing the game table in the GUI.
 * Manages UI components and interactions for displaying game state.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class Table extends SceneController {

    @FXML
    private ImageView imgHand0;
    @FXML
    private ImageView imgHand1;
    @FXML
    private ImageView imgHand2;
    @FXML
    private ImageView imgHand2Player1;
    @FXML
    private ImageView imgHand0Player1;
    @FXML
    private ImageView imgHand1Player1;
    @FXML
    private ImageView imgHand2Player3;
    @FXML
    private ImageView imgHand0Player3;
    @FXML
    private ImageView imgHand1Player3;
    @FXML
    private ImageView imgHand0Player2;
    @FXML
    private ImageView imgHand1Player2;
    @FXML
    private ImageView imgHand2Player2;
    @FXML
    private ImageView imgGoldDeck;
    @FXML
    private ImageView imgObjective0;
    @FXML
    private ImageView imgMarket1;
    @FXML
    private ImageView imgMarket3;
    @FXML
    private ImageView imgResourceDeck;
    @FXML
    private ImageView imgObjective1;
    @FXML
    private ImageView imgMarket0;
    @FXML
    private ImageView imgMarket2;

    @FXML
    private ImageView obj;
    @FXML
    private ImageView objPlayer1;
    @FXML
    private ImageView objPlayer2;
    @FXML
    private ImageView objPlayer3;

    @FXML
    private Button button3;
    @FXML
    private Button button4;

    @FXML
    private Circle color;
    @FXML
    private Circle colorPlayer1;
    @FXML
    private Circle colorPlayer2;
    @FXML
    private Circle colorPlayer3;

    @FXML
    private Circle redPawn;
    @FXML
    private Circle greenPawn;
    @FXML
    private Circle yellowPawn;
    @FXML
    private Circle bluePawn;

    private GameState gameState;
    private List<PlayerColor> order;

    private Stage popupStage = null;
    private Stage playCardPopup = null;
    private Stage chatStage = null;

    private final Map<PlayerColor, Color> colors = new HashMap<>(){
        {
            put(PlayerColor.BLUE, Color.BLUE);
            put(PlayerColor.RED, Color.RED);
            put(PlayerColor.YELLOW, Color.YELLOW);
            put(PlayerColor.GREEN, Color.GREEN);
        }
    };

    // Positions for pawn markers based on score
    private final Map<Integer, Position> scorePositions = new HashMap<>() {
        {
            put(0, new Position(287, 433));
            put(1, new Position(323, 434));
            put(2, new Position(359, 434));
            put(3, new Position(377, 401));
            put(4, new Position(341, 400));
            put(5, new Position(305, 400));
            put(6, new Position(269, 400));
            put(7, new Position(269, 367));
            put(8, new Position(305, 367));
            put(9, new Position(341, 367));
            put(10, new Position(377, 367));
            put(11, new Position(377, 334));
            put(12, new Position(341, 334));
            put(13, new Position(305, 334));
            put(14, new Position(269, 334));
            put(15, new Position(269, 301));
            put(16, new Position(305, 301));
            put(17, new Position(341, 301));
            put(18, new Position(377, 301));
            put(19, new Position(377, 268));
            put(20, new Position(323, 252));
            put(21, new Position(269, 268));
            put(22, new Position(269, 235));
            put(23, new Position(269, 202));
            put(24, new Position(290, 174));
            put(25, new Position(323, 168));
            put(26, new Position(356, 174));
            put(27, new Position(377, 201));
            put(28, new Position(377, 235));
            put(29, new Position(323, 208));
        }
    };

    /**
     * Initializes the pawn marker for the specified player color.
     * @param color The color of the player.
     * @return The corresponding circle (pawn) for the player color.
     */
    private Circle getPawn(PlayerColor color){
        return switch (color) {
            case RED -> redPawn;
            case GREEN -> greenPawn;
            case YELLOW -> yellowPawn;
            case BLUE -> bluePawn;
        };
    }

    /**
     * Updates the position of the pawn marker for a given player color based on their score.
     * @param color The color of the player whose pawn is to be updated.
     */
    private void updatePawn(PlayerColor color) {
        Circle circle = getPawn(color);

        if(circle != null) {
            Platform.runLater(()-> {
                int score = gameState.getScores().get(color);
                circle.setLayoutX(scorePositions.get(score).x());
                circle.setLayoutY(scorePositions.get(score).y());
            });
        }
    }

    /**
     * Loads the current game state from the listener.
     */
    public void loadGameState(){
        gameState = getListener().getGameState();
    }

    /**
     * Handler for clicking on the first hand card.
     * Opens a popup if the card is valid for playing.
     */
    @FXML
    private void handCard0(){
        PlayableCard card = gameState.getHand()[0];
        if (card != null) {
            showPlayCardPopup(0, card.getId());
        }
    }

    /**
     * Handler for clicking on the second hand card.
     * Opens a popup if the card is valid for playing.
     */
    @FXML
    private void handCard1(){
        PlayableCard card = gameState.getHand()[1];
        if (card != null) {
            showPlayCardPopup(1, card.getId());
        }

    }

    /**
     * Handler for clicking on the third hand card.
     * Opens a popup if the card is valid for playing.
     */
    @FXML
    private void handCard2(){
        PlayableCard card = gameState.getHand()[2];
        if (card != null) {
            showPlayCardPopup(2, card.getId());
        }
    }

    /**
     * Displays a popup window for playing a selected card.
     * @param index The index of the card in the hand.
     * @param id The ID of the card to be played.
     */
    private void showPlayCardPopup(int index, String id) {
        Platform.runLater(()->{
            try{
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/playCardPopup.fxml"));
                Parent root = loader.load();

                // Set up the controller
                PlayCardPopup controller = loader.getController();
                controller.setCard(index, id);

                if(playCardPopup == null){
                    // Create the stage
                    playCardPopup = new Stage();
                    playCardPopup.setTitle("Play Card");
                }

                playCardPopup.setScene(new Scene(root));

                if (!playCardPopup.isShowing()) {
                    playCardPopup.show();
                } else {
                    playCardPopup.toFront();
                    playCardPopup.requestFocus();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Event handler for drawing gold from the GUI.
     * @param event The mouse event triggering the action.
     */
    @FXML
    void drawGold(MouseEvent event) {
        getListener().sendMessage(new DrawGold());
    }

    /**
     * Handles the event of clicking on one of the market images.
     * Sends a DrawMarket message to the server based on the clicked image.
     *
     * @param event MouseEvent representing the click event
     */
    @FXML
    void drawMarket(MouseEvent event) {
        Object o = event.getSource();
        if(o == imgMarket0){
            getListener().sendMessage(new DrawMarket(0));
        } else if(o == imgMarket1){
            getListener().sendMessage(new DrawMarket(1));
        } else if(o == imgMarket2){
            getListener().sendMessage(new DrawMarket(2));
        } else if(o == imgMarket3){
            getListener().sendMessage(new DrawMarket(3));
        } else {
            System.out.println("Qualcosa non va");
        }
    }

    /**
     * Handles the event of drawing a resource card.
     *
     * @param event MouseEvent triggering the draw action
     */
    @FXML
    void drawResource(MouseEvent event) {
        getListener().sendMessage(new DrawResource());
    }

    /**
     * Displays the field for the current user.
     *
     * @param actionEvent ActionEvent triggering the display action
     */
    @FXML
    private void showMyField(ActionEvent actionEvent) {
        showField(gameState.getPlayers().get(gameState.getUser()));
    }

    /**
     * Displays the field for Player 3 if available.
     *
     * @param actionEvent ActionEvent triggering the display action
     */
    @FXML
    private void showFieldPlayer3(ActionEvent actionEvent) {
        if(order.size() > 3) {
            showField(order.get(3));
        }
    }

    /**
     * Displays the field for Player 2 if available.
     *
     * @param actionEvent ActionEvent triggering the display action
     */
    @FXML
    private void showFieldPlayer2(ActionEvent actionEvent) {
        if(order.size() > 2) {
            showField(order.get(2));
        }
    }

    /**
     * Displays the field for Player 1.
     *
     * @param actionEvent ActionEvent triggering the display action
     */
    @FXML
    private void showFieldPlayer1(ActionEvent actionEvent) {
        showField(order.get(1));
    }

    /**
     * Sets up the field view for a specific player color.
     *
     * @param color PlayerColor representing the player whose field is to be shown
     */
    private void showField(PlayerColor color){
        ((FieldController) getListener().getController("Field")).setCards(color);
        getListener().openField();
    }

    /**
     * Updates the view after a draw action.
     *
     * @param m NotifyDraw message containing draw details
     */
    public void updateDraw(NotifyDraw m){
        switch (order.indexOf(m.getWho())){
            case 0:
                updateHand();
                break;
            case 1:
                Symbol[] hand1 = gameState.getCardsBack().get(order.get(1));
                Platform.runLater(() -> {
                    imgHand0Player1.setImage(ImagePath.generateBackResource(hand1[0]));
                    imgHand1Player1.setImage(ImagePath.generateBackResource(hand1[1]));
                    imgHand2Player1.setImage(ImagePath.generateBackResource(hand1[2]));
                });
                break;
            case 2:
                Symbol[] hand2 = gameState.getCardsBack().get(order.get(2));
                Platform.runLater(() -> {
                    imgHand0Player2.setImage(ImagePath.generateBackResource(hand2[0]));
                    imgHand1Player2.setImage(ImagePath.generateBackResource(hand2[1]));
                    imgHand2Player2.setImage(ImagePath.generateBackResource(hand2[2]));
                });
                break;
            case 3:
                Symbol[] hand3 = gameState.getCardsBack().get(order.get(3));
                Platform.runLater(() -> {
                    imgHand0Player3.setImage(ImagePath.generateBackResource(hand3[0]));
                    imgHand1Player3.setImage(ImagePath.generateBackResource(hand3[1]));
                    imgHand2Player3.setImage(ImagePath.generateBackResource(hand3[2]));
                });
                break;
        }

        updateCardState();
    }

    /**
     * Handles the event of choosing a starter card.
     *
     * @param m NotifyToChooseStarter message containing starter card details
     */
    public void chooseStarter(NotifyToChooseStarter m) { // open pop up with starter card sides
        Platform.runLater(() -> {
            try {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseStarterPopup.fxml"));
                Parent root = loader.load();

                // Set up the controller
                ChooseStarterPopup controller = loader.getController();
                controller.setCard(m.getStarter());

                // Create the stage
                popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initStyle(StageStyle.UNDECORATED);

                // Set the owner to the main stage
                Stage ownerStage = getListener().getPrimaryStage();
                popupStage.initOwner(ownerStage);

                popupStage.setTitle("Choose Starter");
                popupStage.setScene(new Scene(root));

                // Position the popup in the center of the owner window
                centerPopup(popupStage, ownerStage);

                // Show the stage
                popupStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Centers a popup stage relative to its owner stage.
     *
     * @param popupStage Stage to be centered
     * @param ownerStage Owner stage to center relative to
     */
    private void centerPopup(Stage popupStage, Stage ownerStage) {
        popupStage.setOnShown(event -> {
            double ownerX = ownerStage.getX();
            double ownerY = ownerStage.getY();
            double ownerWidth = ownerStage.getWidth();
            double ownerHeight = ownerStage.getHeight();

            double popupWidth = popupStage.getWidth();
            double popupHeight = popupStage.getHeight();

            popupStage.setX(ownerX + (ownerWidth - popupWidth) / 2);
            popupStage.setY(ownerY + (ownerHeight - popupHeight) / 2);
        });
    }

    /**
     * Handles the event of choosing an objective card.
     *
     * @param m NotifyToChooseObjective message containing objective card details
     */
    public void chooseObjective(NotifyToChooseObjective m) { // open pop up with two objective card
        Platform.runLater(() -> {
            try {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseObjectivePopup.fxml"));
                Parent root = loader.load();

                // Set up the controller
                ChooseObjectivePopup controller = loader.getController();
                controller.setObjective(m.getObjective());

                // Create the stage
                popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initStyle(StageStyle.TRANSPARENT);

                // Set the owner to the main stage
                Stage ownerStage = getListener().getPrimaryStage();
                popupStage.initOwner(ownerStage);

                popupStage.setTitle("Choose Objective");
                popupStage.setScene(new Scene(root));

                // Position the popup in the center of the owner window
                centerPopup(popupStage, ownerStage);

                // Show the stage
                popupStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Updates the UI state after a card play action.
     */
    public void updateCardState(){
        Platform.runLater(() -> {
            // UI-related code here
            imgResourceDeck.setImage(ImagePath.generateBackResource(gameState.getTopResourceDeck()));
            imgGoldDeck.setImage(ImagePath.generateBackGold(gameState.getTopGoldDeck()));
            PlayableCard[] market = gameState.getMarket();
            imgMarket0.setImage(ImagePath.generate(true, market[0].getId()));
            imgMarket1.setImage(ImagePath.generate(true, market[1].getId()));
            imgMarket2.setImage(ImagePath.generate(true, market[2].getId()));
            imgMarket3.setImage(ImagePath.generate(true, market[3].getId()));
        });
    }

    /**
     * Handles the event of notifying the turn change.
     *
     * @param m NotifyTurn message containing turn details
     */
    public void notifyTurn(NotifyTurn m) { // pop up something
        if(m.getColor() == gameState.getPlayers().get(gameState.getUser())) {
            getListener().showAlert("It's your Turn!!");
        } else {
            getListener().showAlert(m.getColor() + "'s Turn!!");
        }
    }

    /**
     * Updates the UI after a card play action.
     *
     * @param m NotifyCardPlayed message containing played card details
     */
    public void cardPlayed(NotifyCardPlayed m){
        Platform.runLater(() -> {
            if (m.getScore() > 0) {
                updatePawn(m.getWho());
            }
            switch (order.indexOf(m.getWho())) {
                case 0: //Me
                    switch (m.getIndex()) {
                        case 0:
                            imgHand0.setImage(null);
                            break;
                        case 1:
                            imgHand1.setImage(null);
                            break;
                        case 2:
                            imgHand2.setImage(null);
                            break;
                    }
                    break;
                case 1: //Player 1
                    switch (m.getIndex()) {
                        case 0:
                            imgHand0Player1.setImage(null);
                            break;
                        case 1:
                            imgHand1Player1.setImage(null);
                            break;
                        case 2:
                            imgHand2Player1.setImage(null);
                            break;
                    }
                    break;
                case 2: //Player 2
                    switch (m.getIndex()) {
                        case 0:
                            imgHand0Player2.setImage(null);
                            break;
                        case 1:
                            imgHand1Player2.setImage(null);
                            break;
                        case 2:
                            imgHand2Player2.setImage(null);
                            break;
                    }
                    break;
                case 3: //Player 3
                    switch (m.getIndex()) {
                        case 0:
                            imgHand0Player3.setImage(null);
                            break;
                        case 1:
                            imgHand1Player3.setImage(null);
                            break;
                        case 2:
                            imgHand2Player3.setImage(null);
                            break;
                    }
                    break;
                default:
                    System.out.println("Error");
            }
        });
    }

    /**
     * Updates the UI after a hand update action.
     */
    public void updateHand(){
        PlayableCard[] hand = gameState.getHand();

        Platform.runLater(() -> {
            imgHand0.setImage(ImagePath.generate(true, hand[0].getId()));
            imgHand1.setImage(ImagePath.generate(true, hand[1].getId()));
            imgHand2.setImage(ImagePath.generate(true, hand[2].getId()));
        });
    }

    /**
     * Sets up the UI for all players' hands.
     */
    public void setUpHands(){
        updateHand();
        Map<PlayerColor, Symbol[]> backCards = gameState.getCardsBack();

        Platform.runLater(() -> {
            Symbol[] hand1 = backCards.get(order.get(1));
            imgHand0Player1.setImage(ImagePath.generateBackResource(hand1[0]));
            imgHand1Player1.setImage(ImagePath.generateBackResource(hand1[1]));
            imgHand2Player1.setImage(ImagePath.generateBackResource(hand1[2]));

            if(order.size() > 2) {
                Symbol[] hand2 = backCards.get(order.get(2));
                imgHand0Player2.setImage(ImagePath.generateBackResource(hand2[0]));
                imgHand1Player2.setImage(ImagePath.generateBackResource(hand2[1]));
                imgHand2Player2.setImage(ImagePath.generateBackResource(hand2[2]));
            }

            if(order.size() > 3) {
                Symbol[] hand3 = backCards.get(order.get(3));
                imgHand0Player3.setImage(ImagePath.generateBackResource(hand3[0]));
                imgHand1Player3.setImage(ImagePath.generateBackResource(hand3[1]));
                imgHand2Player3.setImage(ImagePath.generateBackResource(hand3[2]));
            }
        });
    }

    /**
     * Sets up the visual representation of player pawns, colors, and initial game state.
     */
    public void setColors(){
        Platform.runLater(() -> {
            Map<String, PlayerColor> players = gameState.getPlayers();
            setOrderAndFillMarkers(players.values().stream().toList());

            color.setFill(colors.get(players.get(gameState.getUser())));

            if (players.size() < 4) {
                colorPlayer3.setVisible(false);
                button4.setVisible(false);
                button4.setDisable(true);

                if (players.size() < 3) {
                    colorPlayer2.setVisible(false);
                    button3.setVisible(false);
                    button3.setDisable(true);
                }
            }

            for (PlayerColor color : PlayerColor.values()) {
                if (order.contains(color)) {
                    updatePawn(color);
                } else {
                    getPawn(color).setVisible(false);
                }
            }
        });
    }

    /**
     * Notifies the end of the match and displays appropriate alerts or results.
     *
     * @param m NotifyEndMatch message containing end match details
     */
    public void notifyEndMatch(NotifyEndMatch m) {
        //Close stage
        Platform.runLater(() -> {
            if(popupStage != null)
                popupStage.close();
            if(playCardPopup != null)
                playCardPopup.close();
            if(chatStage != null)
                chatStage.close();
        });

        int winners = m.getWinners();

        if (winners == -1) { // match ended in setup phase, return to menu
            getListener().showAlert("Match Ended Due to Disconnection", () -> getListener().openMenuPage());
        }
        else {
            List<PlayerColor> players = m.getPlayers();
            List<Integer> scores = m.getScores();
            List<String> objs = m.getObjs().stream().map(Objective::getId).toList();

            Map<String, PlayerColor> playersMap = gameState.getPlayers();
            List<String> names = new ArrayList<>();
            for (PlayerColor color : players) {
                for (String name : playersMap.keySet()) {
                    if (playersMap.get(name) == color) {
                        names.add(name);
                        break;
                    }
                }
            }

            getListener().showAlert(winners == 0 ? "Match Ended Due to Disconnection" : "Match Ended!!", () -> {
                getListener().openScores(winners, players, names, scores, objs);
                // clear hands images from table scene for next games
                Platform.runLater(() -> {
                    imgHand0.setImage(null);
                    imgHand1.setImage(null);
                    imgHand2.setImage(null);
                    imgHand2Player1.setImage(null);
                    imgHand0Player1.setImage(null);
                    imgHand1Player1.setImage(null);
                    imgHand2Player3.setImage(null);
                    imgHand0Player3.setImage(null);
                    imgHand1Player3.setImage(null);
                    imgHand0Player2.setImage(null);
                    imgHand1Player2.setImage(null);
                    imgHand2Player2.setImage(null);
                    imgGoldDeck.setImage(null);
                    imgObjective0.setImage(null);
                    imgMarket1.setImage(null);
                    imgMarket3.setImage(null);
                    imgResourceDeck.setImage(null);
                    imgObjective1.setImage(null);
                    imgMarket0.setImage(null);
                    imgMarket2.setImage(null);
                    obj.setImage(null);
                    objPlayer1.setImage(null);
                    objPlayer2.setImage(null);
                    objPlayer3.setImage(null);
                });
            });
        }
    }

    /**
     * Notifies the start of the last turn.
     */
    public void notifyLastTurn() { // pop up something
        getListener().showAlert("Last Turn!!");
    }

    /**
     * Updates the UI after an objective card is confirmed.
     *
     * @param m NotifyChosenObjective message containing chosen objective details
     */
    public void objectiveConfirmation(NotifyChosenObjective m){
        Platform.runLater(() -> {
            obj.setImage(ImagePath.generate(true, m.getObjective().getId()));
            objPlayer1.setImage(ImagePath.generate(false, m.getObjective().getId()));
            if(order.size() > 2)
                objPlayer2.setImage(ImagePath.generate(false, m.getObjective().getId()));
            if(order.size() > 3)
                objPlayer3.setImage(ImagePath.generate(false, m.getObjective().getId()));
        });
    }

    /**
     * Sets up the visual representation of common objectives.
     */
    public void setUpCommonObjective(){
        Objective[] objs = gameState.getCommonObjectives();
        Platform.runLater(() -> {
            imgObjective0.setImage(ImagePath.generate(true, objs[0].getId()));
            imgObjective1.setImage(ImagePath.generate(true, objs[1].getId()));
        });
    }

    /**
     * Sets up the end game view after the setup phase is finished.
     *
     * @param m NotifySetUpFinished message containing setup finished details
     */
    public void setUpEnd(NotifySetUpFinished m){
        Platform.runLater(() -> {
            List<PlayerColor> players = m.getColors();
            setOrderAndFillMarkers(players);
            updateHand();
        });
    }

    /**
     * Sets the order of players and updates UI markers with their respective colors.
     *
     * @param players List of PlayerColor objects representing all players in the game.
     */
    private void setOrderAndFillMarkers(List<PlayerColor> players){
        int me = 0;
        for(int i = 0; i < players.size(); i++){
            if(players.get(i) == gameState.getPlayers().get(gameState.getUser())){
                me = i;
                break;
            }
        }

        List<PlayerColor> newOrder = new ArrayList<>();
        newOrder.add(players.get(me));
        int index = 0;
        for(int i = me + 1; i < players.size(); i++){
            newOrder.add(players.get(i));
            Color color = colors.get(players.get(i));
            if(index == 0){
                colorPlayer1.setFill(color);
            } else if(index == 1 && players.size() > 2){
                colorPlayer2.setFill(color);
            } else if(index == 2 && players.size() > 3){
                colorPlayer3.setFill(color);
            }
            index++;
        }
        for(int i = 0; i < me; i++){
            newOrder.add(players.get(i));
            Color color = colors.get(players.get(i));
            if(index == 0){
                colorPlayer1.setFill(color);
            } else if(index == 1 && players.size() > 2){
                colorPlayer2.setFill(color);
            } else if(index == 2 && players.size() > 3){
                colorPlayer3.setFill(color);
            }
            index++;
        }

        order = newOrder;
    }

    /**
     * Handles the event of opening the chat window.
     *
     * @param event ActionEvent triggering the chat window opening
     */
    @FXML
    private void handleOpenChat(ActionEvent event) {
        try {
            // Load the chat window FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/chat.fxml"));
            Parent chatRoot = fxmlLoader.load();

            if(chatStage == null) {
                // Create a new stage (window)
                chatStage = new Stage();
                chatStage.setTitle("Chat");
            }

            chatStage.setScene(new Scene(chatRoot));

            if (!chatStage.isShowing()) {
                chatStage.show();
            } else {
                chatStage.toFront();
                chatStage.requestFocus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes and sets up the game table view for an interrupted game.
     *
     * @param m GameStateMessage containing the interrupted game state
     */
    public void loadInterruptedGame(GameStateMessage m) {
        loadGameState();
        setColors();
        updateCardState();
        setUpHands();
        setUpCommonObjective();

        objectiveConfirmation(new NotifyChosenObjective(m.personalObjective()));
        setOrderAndFillMarkers(m.gameOrder());
    }
}
