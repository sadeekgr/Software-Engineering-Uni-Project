package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.Gui;

/**
 * Abstract controller class for managing scenes in the GUI.
 * This class uses a static listener shared among all instances.
 */
public abstract class SceneController {
    private static Gui listener;

    /**
     * Sets the listener for all scene controllers.
     *
     * @param listener the GUI listener to be set
     * @throws IllegalArgumentException if the listener is null
     */
    public static void setListener(Gui listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        SceneController.listener = listener;
    }

    /**
     * Returns the current listener.
     *
     * @return the current GUI listener
     */
    public static Gui getListener() {
        return listener;
    }
}
