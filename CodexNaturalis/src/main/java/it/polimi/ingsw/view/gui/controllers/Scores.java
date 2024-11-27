package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utilities.ImagePath;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for managing the scores display in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class Scores extends SceneController {
    @FXML
    private Label winner;

    @FXML
    private Circle redPawn;
    @FXML
    private Circle greenPawn;
    @FXML
    private Circle yellowPawn;
    @FXML
    private Circle bluePawn;

    @FXML
    private Circle color1;
    @FXML
    private Circle color2;
    @FXML
    private Circle color3;
    @FXML
    private Circle color4;

    @FXML
    private Text pos1;
    @FXML
    private Text pos2;
    @FXML
    private Text pos3;
    @FXML
    private Text pos4;

    @FXML
    private Text name1;
    @FXML
    private Text name2;
    @FXML
    private Text name3;
    @FXML
    private Text name4;

    @FXML
    private Text score1;
    @FXML
    private Text score2;
    @FXML
    private Text score3;
    @FXML
    private Text score4;

    @FXML
    private ImageView obj1;
    @FXML
    private ImageView obj2;
    @FXML
    private ImageView obj3;
    @FXML
    private ImageView obj4;

    // Map to store positions for displaying pawns
    private final Map<Integer, Position> scorePositions = new HashMap<>() {
        {
            put(0, new Position(57, 402));
            put(1, new Position(108, 402));
            put(2, new Position(159, 402));
            put(3, new Position(185, 356));
            put(4, new Position(134, 356));
            put(5, new Position(83, 356));
            put(6, new Position(32, 356));
            put(7, new Position(32, 309));
            put(8, new Position(83, 309));
            put(9, new Position(134, 309));
            put(10, new Position(185, 309));
            put(11, new Position(185, 262));
            put(12, new Position(134, 262));
            put(13, new Position(83, 262));
            put(14, new Position(32, 262));
            put(15, new Position(32, 216));
            put(16, new Position(83, 216));
            put(17, new Position(134, 216));
            put(18, new Position(185, 216));
            put(19, new Position(184, 169));
            put(20, new Position(108, 146));
            put(21, new Position(32, 169));
            put(22, new Position(32, 123));
            put(23, new Position(32, 76));
            put(24, new Position(61, 38));
            put(25, new Position(108, 30));
            put(26, new Position(155, 38));
            put(27, new Position(184, 76));
            put(28, new Position(184, 123));
            put(29, new Position(108, 86));
        }
    };

    /**
     * Handles the action when returning to the main menu button is clicked.
     */
    @FXML
    private void goToMenu() { getListener().openMenuPage(); }

    /**
     * Sets the scores and display data for the players in the GUI.
     *
     * @param winners The number of winners.
     * @param players The list of PlayerColor representing the players.
     * @param names The list of names corresponding to the players.
     * @param scores The list of scores corresponding to the players.
     * @param objectiveId The list of objective IDs corresponding to the players.
     */
    public void setScores(int winners, List<PlayerColor> players, List<String> names, List<Integer> scores, List<String> objectiveId){
        if(players.size() < winners || players.size()!= names.size() || players.size()!=scores.size() || players.size()!=objectiveId.size()){ return;}

        if(winners == 0) { // ended due to disconnection
            winner.setText("Game Ended Due to Disconnection");
        }
        else {
            StringBuilder banner = new StringBuilder("Winner: ");
            for (int i = 0; i < winners; i++) {
                banner.append(names.get(i));
            }
            winner.setText(banner.toString());
        }

        if(players.size() < 4){
            pos4.setText("");
            color4.setVisible(false);
            name4.setVisible(false);
            score4.setVisible(false);

            if(players.size() < 3){
                pos3.setText("");
                color3.setVisible(false);
                name3.setVisible(false);
                score3.setVisible(false);
            }
        }

        for(PlayerColor color : PlayerColor.values()){
            if(!players.contains(color)){
                switch(color){
                    case RED:
                        redPawn.setVisible(false);
                        break;
                    case GREEN:
                        greenPawn.setVisible(false);
                        break;
                    case YELLOW:
                        yellowPawn.setVisible(false);
                        break;
                    case BLUE:
                        bluePawn.setVisible(false);
                        break;
                }
            }
        }

        for(int i = 0; i < players.size(); i++){
            PlayerColor color = players.get(i);
            String name = names.get(i);
            int score = scores.get(i);
            String objective = objectiveId.get(i);

            // set pawn in scoreboard
            Circle circle = switch (color){
                case RED -> redPawn;
                case GREEN -> greenPawn;
                case YELLOW -> yellowPawn;
                case BLUE -> bluePawn;
            };

            circle.setLayoutX(scorePositions.get(score).x());
            circle.setLayoutY(scorePositions.get(score).y());

            // set data in scoreboard
            Color color_i = switch(color){
                case RED -> Color.RED;
                case GREEN -> Color.GREEN;
                case YELLOW -> Color.YELLOW;
                case BLUE -> Color.BLUE;
            };

            switch (i){
                case 0:
                    if(winners != 0) { // winners == 0 -> ended due to disconnection
                        pos1.setText("1");
                    }
                    color1.setFill(color_i);
                    name1.setText(name);
                    score1.setText(String.valueOf(score));
                    obj1.setImage(ImagePath.generate(true, objective));
                    break;
                case 1:
                    if(winners != 0) { // winners == 0 -> ended due to disconnection
                        if (winners > 1) {
                            pos2.setText("1");
                        } else {
                            pos2.setText("2");
                        }
                    }
                    color2.setFill(color_i);
                    name2.setText(name);
                    score2.setText(String.valueOf(score));
                    obj2.setImage(ImagePath.generate(true, objective));
                    break;
                case 2:
                    if(winners != 0) { // winners == 0 -> ended due to disconnection
                        if (winners > 2) {
                            pos3.setText("1");
                        } else {
                            if (winners > 1) {
                                pos3.setText("2");
                            } else {
                                pos3.setText("3");
                            }
                        }
                    }
                    color3.setFill(color_i);
                    name3.setText(name);
                    score3.setText(String.valueOf(score));
                    obj3.setImage(ImagePath.generate(true, objective));
                    break;
                case 3:
                    if(winners != 0) { // winners == 0 -> ended due to disconnection
                        if (winners == 4) {
                            pos4.setText("1");
                        } else if (winners == 3) {
                            pos4.setText("2");
                        } else if (winners == 2) {
                            pos4.setText("3");
                        } else {
                            pos4.setText("4");
                        }
                    }

                    color4.setFill(color_i);
                    name4.setText(name);
                    score4.setText(String.valueOf(score));
                    obj4.setImage(ImagePath.generate(true, objective));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }
        }
    }

}
