package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utilities.ImagePath;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Controller that manages the players' fields in the GUI
 * Handles placement of cards and resizing of the pane
 */
public class FieldController extends SceneController {
    @FXML
    private Pane pane;

    private static final double INITIAL_PANE_SIZE = 1000;
    private static final double EXTENSION_SIZE = 500;
    private static final double SCROLL_THRESHOLD = 100;
    private static final double IMAGE_WIDTH = 106;
    private static final double IMAGE_HEIGHT = 71;
    private static final double UNIT_X = 82;
    private final static double UNIT_Y = 41;

    private double xOffset = 0;
    private double yOffset = 0;

    private PlayerColor fieldColor = null;
    private int lastAdded = 0;

    /**
     * Gets the X and Y offsets
     * @param event mouse event that called the function
     */
    @FXML
    private void onMousePressed(MouseEvent event) {
        xOffset = event.getX();
        yOffset = event.getY();
    }

    /**
     * Sets the new pane layouts based on how the pane was dragged
     * @param event mouse event that called the function
     */
    @FXML
    private void onMouseDragged(MouseEvent event) {
        Platform.runLater(() -> {
            double newX = event.getSceneX() - xOffset;
            double newY = event.getSceneY() - yOffset;

            pane.setLayoutX(newX);
            pane.setLayoutY(newY);
        });
    }

    /**
     * Sets the new pane layouts (and extends them) based on where the pane was released (after being dragged)
     * @param event mouse event that called the function
     */
    @FXML
    private void onMouseReleased(MouseEvent event) {
        final double newX = event.getSceneX() - xOffset;
        final double newY = event.getSceneY() - yOffset;

        if (
            newX > -SCROLL_THRESHOLD ||
            newY > -SCROLL_THRESHOLD ||
            pane.getPrefWidth() + newX - getListener().getFieldStageWidth() < SCROLL_THRESHOLD ||
            pane.getPrefHeight() + newY - getListener().getFieldStageHeight() < SCROLL_THRESHOLD
        ) {
            extendPane();

            Platform.runLater(() -> {
                pane.setLayoutX(newX - EXTENSION_SIZE / 2);
                pane.setLayoutY(newY - EXTENSION_SIZE / 2);
            });
        }

    }

    /**
     * Extends the width and height of the pane and moves all the placed cards to the center of the pane
     */
    private void extendPane() {
        Platform.runLater(() -> {
            double newWidth = pane.getWidth() + EXTENSION_SIZE;
            double newHeight = pane.getHeight() + EXTENSION_SIZE;
            pane.setPrefSize(newWidth, newHeight);

            // Move all the placed cards to the center of the pane
            for(Node card : pane.getChildren()){
                card.setLayoutX(card.getLayoutX() + EXTENSION_SIZE / 2);
                card.setLayoutY(card.getLayoutY() + EXTENSION_SIZE / 2);
            }
        });
    }

    /**
     * Centers the position of the layout within the pane
     */
    private void centerPane(){
        Platform.runLater(() -> {
            pane.setLayoutX(-pane.getPrefWidth() / 2 + getListener().getFieldStageWidth() / 2);
            pane.setLayoutY(-pane.getPrefHeight() / 2 + getListener().getFieldStageHeight() / 2);
        });
    }

    /**
     * Clears  the pane and resets its size
     */
    private void clearPane(){
        Platform.runLater(() -> {
            pane.getChildren().clear();
            pane.setPrefSize(
                Math.max(INITIAL_PANE_SIZE, getListener().getFieldStageWidth() + 3 * SCROLL_THRESHOLD),
                Math.max(INITIAL_PANE_SIZE, getListener().getFieldStageHeight() + 3 * SCROLL_THRESHOLD)
            );

            lastAdded = 0;
            centerPane();
        });
    }

    /**
     * Sets the cards on the field pane for the specified PlayerColor color by clearing the pane and updating it
     * @param color color of the player whose cards need to be set
     */
    public void setCards(PlayerColor color){
        if(fieldColor != color && color != null){
            clearPane();
            fieldColor = color;
            update();
        }
    }

    /**
     * Updates the field pane for the current fieldColor (checks current card placements from the game state)
     */
    public void update(){
        if(fieldColor != null){
            Platform.runLater(() -> {
                List<CardPlacement> cards = getListener().getGameState().getFields().get(fieldColor).getCards();
                for (int i = lastAdded; i < cards.size(); i++) {
                    CardPlacement placement = cards.get(i);
                    Image image = ImagePath.generate(placement.isFront(), placement.getCard().getId());
                    placeImageAtCartesian(image, placement.getPosition());
                }
                lastAdded = cards.size();
            });
        }
    }

    /**
     * Places an image on the pane using Cartesian coordinates
     * @param image The image to place
     * @param p     The coordinate in the Cartesian plane
     */
    private void placeImageAtCartesian(Image image, Position p) {
        Platform.runLater(() -> {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(IMAGE_WIDTH);
            imageView.setFitHeight(IMAGE_HEIGHT);

            // Convert Cartesian coordinates to Pane coordinates
            double paneWidth = pane.getPrefWidth();
            double paneHeight = pane.getPrefHeight();

            double paneX = (paneWidth / 2) + p.x() * UNIT_X;
            double paneY = (paneHeight / 2) - p.y() * UNIT_Y;


            if (paneX < 0 || paneY < 0 || paneX > paneWidth || paneY > paneHeight) {
                extendPane();
                placeImageAtCartesian(image, p); // Recalculate after extension
                return;
            }

            // Set the position of the ImageView
            imageView.setLayoutX(paneX - imageView.getFitWidth() / 2);
            imageView.setLayoutY(paneY - imageView.getFitHeight() / 2);

            pane.getChildren().add(imageView);
        });
    }
}
