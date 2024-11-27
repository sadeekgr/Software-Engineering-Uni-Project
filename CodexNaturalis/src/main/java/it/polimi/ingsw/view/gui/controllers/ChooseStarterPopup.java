package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.message.action.ChooseStarter;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.utilities.ImagePath;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controller class for managing the "Choose Starter Card" popup window in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class ChooseStarterPopup extends SceneController {
    @FXML
    private ImageView front;
    @FXML
    private ImageView back;

    /**
     * Sets the starter card to display in the popup window.
     *
     * @param card The {@link it.polimi.ingsw.model.card.StarterCard} object representing the starter card.
     */
    public void setCard(StarterCard card) {
        front.setImage(ImagePath.generate(true, card.getId()));
        back.setImage(ImagePath.generate(false, card.getId()));
    }

    /**
     * Handles the event when the front side of the starter card is chosen.
     * Sends a {@link it.polimi.ingsw.message.action.ChooseStarter} message with the front side chosen as true.
     * Closes the popup window after sending the message.
     */
    @FXML
    private void chooseFront(){
        getListener().sendMessage(new ChooseStarter(true));
        close();
    }

    /**
     * Handles the event when the back side of the starter card is chosen.
     * Sends a {@link it.polimi.ingsw.message.action.ChooseStarter} message with the front side chosen as false.
     * Closes the popup window after sending the message.
     */
    @FXML
    private void chooseBack(){
        getListener().sendMessage(new ChooseStarter(false));
        close();
    }

    /**
     * Closes the popup window.
     * Retrieves the stage associated with the ImageView front and closes it.
     */
    private void close(){
        // Close the popup window
        Stage stage = (Stage) front.getScene().getWindow();
        stage.close();
    }
}
