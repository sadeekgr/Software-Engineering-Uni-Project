package it.polimi.ingsw.model.game;

import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.objective.Objective;

import java.security.InvalidParameterException;

/**
 * Represents the initial configuration of a player in the game, including their starter card, objectives, and choices made during setup.
 */
public class PlayerInitialConfig {
    private StarterCard starterCard;
    private Objective[] objectives;
    private Boolean chosenStarterSide;
    private int chosenObjective;

    /**
     * Constructs a new PlayerInitialConfig object with default values.
     */
    public PlayerInitialConfig() {
        this.starterCard = null;
        this.objectives = null;
        this.chosenStarterSide = null;
        this.chosenObjective = -1;
    }

    /**
     * Gets the starter card chosen by the player.
     *
     * @return The chosen starter card.
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }

    /**
     * Gets the objectives available for the player to choose from.
     *
     * @return An array of available objectives.
     */
    public Objective[] getObjectives() { return objectives; }

    /**
     * Gets the objective card chosen by the player.
     *
     * @return The chosen objective card.
     */
    public Objective getChosenObjective() {
        if (chosenObjective == -1){
            return null;
        }
        return objectives[chosenObjective]; }

    /**
     * Sets the chosen side of the starter card.
     *
     * @param choice The chosen side of the starter card.
     * @throws PlayerExceptions If it's not the appropriate time to choose the starter card or if the starter card side has already been chosen.
     */
    public void setChosenStarterSide(boolean choice) throws PlayerExceptions {
        if (starterCard == null){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.CONFIGURATION_CHOICE_NOT_PERMITTED, "You can't choose the starter card now.");
        }

        if (chosenStarterSide != null){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.CARD_ALREADY_CHOSEN, "Card already chosen.");
        }

        chosenStarterSide = choice;
    }

    /**
     * Sets the chosen objective card.
     *
     * @param choice The index of the chosen objective card.
     * @throws InvalidParameterException   If the choice is not valid (0 or 1).
     * @throws PlayerExceptions If it's not the appropriate time to choose the objective card or if the objective card has already been chosen.
     */
    public void setChosenObjective(int choice) throws PlayerExceptions {
        if (objectives == null){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.CONFIGURATION_CHOICE_NOT_PERMITTED,"You can't choose the objective now.");
        }

        if (chosenObjective != -1){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.CARD_ALREADY_CHOSEN, "Card already chosen.");
        }

        if (choice != 0 && choice != 1){
            throw new InvalidParameterException("Invalid choice, choose either 0 or 1.");
        }

        chosenObjective = choice;
    }

    /**
     * Sets the starter card for the player.
     *
     * @param s The starter card to be set.
     */
    public void setStarterCard(StarterCard s){
        starterCard = s;
    }

    /**
     * Sets the objectives available for the player to choose from.
     *
     * @param objs An array of objectives to be set.
     */
    public void setObjectives(Objective[] objs){
        objectives = objs;
    }

    /**
     * Gets the chosen side of the starter card.
     *
     * @return The chosen side of the starter card (true for front, false for back).
     */
    public Boolean getStarterSide(){
        return chosenStarterSide;
    }
}
