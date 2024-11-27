package it.polimi.ingsw.network;

import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Application;

/**
 * Client class for connecting to the game server.
 */
public class Client {

    /**
     * Main method to start the client application.
     * Launches either a GUI or CLI interface based on command-line arguments.
     *
     * @param args Command line arguments. Use "-cli" to run in CLI mode; GUI mode is default.
     */
    public static void main(String[] args) {
        boolean runGUI = true;

        // Check command line arguments
        for (String arg : args) {
            if (arg.equals("-cli")) {
                runGUI = false;
            } else {
                System.out.println("Unknown argument: " + arg);
            }
        }
        if (runGUI) {
            Application.launch(Gui.class);
        } else {
            new Cli().run();
        }
    }
}