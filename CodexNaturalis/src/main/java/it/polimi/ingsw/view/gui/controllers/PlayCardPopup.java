package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.message.action.PlayCard;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.utilities.ImagePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for managing the popup window to play a card in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class PlayCardPopup extends SceneController {
    @FXML
    private ImageView cardImageView;

    @FXML
    private ToggleButton frontBackCheckBox;

    @FXML
    private TextField xPositionField;

    @FXML
    private TextField yPositionField;

    private int index;
    private boolean side;
    private Image imgFront;
    private Image imgBack;

    /**
     * Sets the card image to display based on the index and ID provided.
     *
     * @param index The index of the card.
     * @param id    The ID of the card to display.
     */
    public void setCard(int index, String id) {
        this.index = index;
        side = true;
        imgFront = ImagePath.generate(true, id);
        imgBack = ImagePath.generate(false, id);
        cardImageView.setImage(imgFront);
    }

    /**
     * Switches the displayed side of the card between front and back.
     *
     * @param event The action event that triggers the method (typically a button click).
     */
    @FXML
    private void switchFrontBack(ActionEvent event) {
        side = !side;
        if (side) {
            cardImageView.setImage(imgFront);
            frontBackCheckBox.setText("Front");
        } else {
            cardImageView.setImage(imgBack);
            frontBackCheckBox.setText("Back");
        }
    }

    /**
     * Handles the action when the play card button is clicked.
     * Retrieves the position from the text fields and sends a {@link it.polimi.ingsw.message.action.PlayCard} message.
     *
     * @param event The action event that triggers the method (typically a button click).
     */
    @FXML
    private void playCard(ActionEvent event) {
        int x, y;
        try {
            x = Integer.parseInt(xPositionField.getText());
            y = Integer.parseInt(yPositionField.getText());
        } catch (NumberFormatException e){
            getListener().showAlert("Invalid Positions");
            return;
        }

        getListener().sendMessage(new PlayCard(index, new Position(x, y), side));
        closePopup(null);
    }

    /**
     * Closes the popup window.
     *
     * @param event The action event that triggers the method (typically closing the window).
     */
    @FXML
    private void closePopup(ActionEvent event) {
        // Close the popup window
        Stage stage = (Stage) cardImageView.getScene().getWindow();
        stage.close();
    }
}
