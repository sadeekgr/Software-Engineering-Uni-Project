module it.polimi.ingsw.view.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;
    requires java.logging;
    requires java.desktop;
    requires junit;

    opens it.polimi.ingsw.view.gui to javafx.fxml;
    opens it.polimi.ingsw.view.gui.controllers to javafx.fxml;
    opens it.polimi.ingsw.model.card to com.google.gson;
    opens it.polimi.ingsw.model.objective to com.google.gson;
    opens it.polimi.ingsw.model.field to com.google.gson;
    opens it.polimi.ingsw.utilities to com.google.gson;
    opens it.polimi.ingsw.gamestate to com.google.gson;
    opens it.polimi.ingsw.model.game to com.google.gson;

    exports it.polimi.ingsw.model.player;
    exports it.polimi.ingsw.model.card;
    exports it.polimi.ingsw.model.objective;
    exports it.polimi.ingsw.model.field;
    exports it.polimi.ingsw.model.game;

    exports it.polimi.ingsw.network.client;
    exports it.polimi.ingsw.network.server;

    exports it.polimi.ingsw.message;
    exports it.polimi.ingsw.message.notify;
    exports it.polimi.ingsw.message.lobby;
    exports it.polimi.ingsw.message.error;
    exports it.polimi.ingsw.message.action;

    exports it.polimi.ingsw.gamestate;
    exports it.polimi.ingsw.utilities;

    exports it.polimi.ingsw.controller;

    exports it.polimi.ingsw.exception;

    exports it.polimi.ingsw.lobby;

    exports it.polimi.ingsw.view;
    exports it.polimi.ingsw.view.gui;
    exports it.polimi.ingsw.view.cli;
    exports it.polimi.ingsw.view.gui.controllers;
}
