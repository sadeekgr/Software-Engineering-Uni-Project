package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.message.action.ChooseObjective;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.utilities.ImagePath;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controller class for managing the "Choose Objective" popup window in the GUI.
 * Extends {@link it.polimi.ingsw.view.gui.controllers.SceneController}.
 */
public class ChooseObjectivePopup extends SceneController {
    @FXML
    private ImageView obj0;
    @FXML
    private ImageView obj1;

    /**
     * Sets the objectives to display in the popup window.
     *
     * @param objs An array containing two {@link it.polimi.ingsw.model.objective.Objective} objects to display.
     */
    public void setObjective(Objective[] objs) {
        obj0.setImage(ImagePath.generate(true, objs[0].getId()));
        obj1.setImage(ImagePath.generate(true, objs[1].getId()));
    }

    /**
     * Handles the event when the first objective (obj0) is chosen.
     * Sends a {@link it.polimi.ingsw.message.action.ChooseObjective} message with index 0.
     * Closes the popup window after sending the message.
     */
    @FXML
    private void chooseObj0(){
        getListener().sendMessage(new ChooseObjective(0));
        close();
    }

    /**
     * Handles the event when the second objective (obj1) is chosen.
     * Sends a {@link it.polimi.ingsw.message.action.ChooseObjective} message with index 1.
     * Closes the popup window after sending the message.
     */
    @FXML
    private void chooseObj1(){
        getListener().sendMessage(new ChooseObjective(1));
        close();
    }

    /**
     * Closes the popup window.
     * Retrieves the stage associated with the ImageView obj0 and closes it.
     */
    private void close(){
        // Close the popup window
        Stage stage = (Stage) obj0.getScene().getWindow();
        stage.close();
    }
}

