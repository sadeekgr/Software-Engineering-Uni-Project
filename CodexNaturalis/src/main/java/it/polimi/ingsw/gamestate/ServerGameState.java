package it.polimi.ingsw.gamestate;

import com.google.gson.Gson;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.game.GoldDeck;
import it.polimi.ingsw.model.game.ResourceDeck;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utilities.GsonSingleton;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents the server-side state of the game, including player information, card placements,
 * objectives, decks, market state, and current game progress.
 * This class is serializable to JSON format for saving and loading game states.
 */
public class ServerGameState implements Serializable {
    private static final Logger logger = Logger.getLogger(ServerGameState.class.getName());

    private List<PlayerColor> gameOrder;

    private List<String> players;

    private List<StarterCard> starters;
    private List<Boolean> starterSides;
    private List<List<PlayableCard>> playableCards;
    private List<List<Position>> positions;
    private List<List<Boolean>> sides;

    private List<Integer> scores;

    private List<Objective> personalObjectives;
    private List<PlayableCard[]> hands;

    private Objective[] commonObjectives;

    private GoldDeck goldDeck;
    private ResourceDeck resourceDeck;
    private PlayableCard[] market;

    private PlayerColor currentPlayer;
    private boolean lastLap;

    /**
     * Constructs a ServerGameState by loading the game state from a JSON file identified by the provided lobby ID.
     *
     * @param lobbyId The ID of the lobby from which to load the game state.
     * @throws IOException If an I/O error occurs during loading.
     */
    public ServerGameState(int lobbyId) throws IOException {
        load(lobbyId);
    }

    /**
     * Constructs a ServerGameState with the provided game state parameters and saves it to a JSON file identified by the lobby ID.
     *
     * @param lobbyId           The ID of the lobby to which this game state belongs.
     * @param gameOrder         The list of player colors representing the order of turns in the game.
     * @param players           The list of player usernames.
     * @param fields            The list of player fields, containing card placements and their positions.
     * @param scores            The list of player scores.
     * @param personalObjectives The list of personal objectives for each player.
     * @param hands             The list of hands (arrays of playable cards) for each player.
     * @param commonObjectives  The common objectives shared among all players.
     * @param goldDeck          The deck of gold cards.
     * @param resourceDeck      The deck of resource cards.
     * @param market            The array of cards available in the market.
     * @param currentPlayer     The current player taking their turn.
     * @param lastLap           Indicates if it is the last lap of the game.
     */
    public ServerGameState(int lobbyId, List<PlayerColor> gameOrder, List<String> players, List<PlayerField> fields, List<Integer> scores, List<Objective> personalObjectives, List<PlayableCard[]> hands, Objective[] commonObjectives, GoldDeck goldDeck, ResourceDeck resourceDeck, PlayableCard[] market, PlayerColor currentPlayer, boolean lastLap) {
        this.gameOrder = gameOrder;
        this.players = players;

        starters = new ArrayList<>();
        starterSides = new ArrayList<>();
        playableCards = new ArrayList<>();
        positions = new ArrayList<>();
        sides = new ArrayList<>();
        for(PlayerField field : fields) {
            List<CardPlacement> cardPlacements = field.getCards();
            starters.add((StarterCard) cardPlacements.getFirst().getCard()); //there is always, the first dave is after the setup
            starterSides.add(cardPlacements.getFirst().isFront());

            List<PlayableCard> cards = new ArrayList<>();
            List<Position> pos = new ArrayList<>();
            List<Boolean> side = new ArrayList<>();
            for(int i = 1; i < cardPlacements.size(); i++){
                cards.add((PlayableCard) cardPlacements.get(i).getCard());
                pos.add(cardPlacements.get(i).getPosition());
                side.add(cardPlacements.get(i).isFront());
            }
            playableCards.add(cards);
            positions.add(pos);
            sides.add(side);
        }

        this.scores = scores;
        this.personalObjectives = personalObjectives;
        this.hands = hands;
        this.commonObjectives = commonObjectives;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.market = market;
        this.currentPlayer = currentPlayer;
        this.lastLap = lastLap;

        save(lobbyId);
    }

    /**
     * Returns the list of player colors representing the order of turns in the game.
     *
     * @return The list of player colors.
     */
    public List<PlayerColor> getGameOrder() {
        return gameOrder;
    }

    /**
     * Returns the list of player usernames.
     *
     * @return The list of player usernames.
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Retrieves and constructs a list of player fields based on the stored starter cards,
     * playable cards, positions, and sides.
     *
     * @return The list of constructed player fields.
     */
    public List<PlayerField> getFields() {
        List<PlayerField> fields = new ArrayList<>();
        for(int i = 0; i < players.size(); i++) {
            PlayerField field = new PlayerField();
            field.placeStarterCard(starters.get(i), starterSides.get(i));
            List<PlayableCard> cards = playableCards.get(i);
            List<Position> pos = positions.get(i);
            List<Boolean> side = sides.get(i);
            for(int j = 0; j < cards.size(); j++){
                field.placeCard(cards.get(j), side.get(j), pos.get(j));
            }
            fields.add(field);
        }
        return fields;
    }

    /**
     * Returns the list of player scores.
     *
     * @return The list of player scores.
     */
    public List<Integer> getScores() {
        return scores;
    }

    /**
     * Returns the list of personal objectives for each player.
     *
     * @return The list of personal objectives.
     */
    public List<Objective> getPersonalObjectives() {
        return personalObjectives;
    }

    /**
     * Returns the list of hands (arrays of playable cards) for each player.
     *
     * @return The list of hands.
     */
    public List<PlayableCard[]> getHands() {
        return hands;
    }

    /**
     * Returns the common objectives shared among all players.
     *
     * @return The array of common objectives.
     */
    public Objective[] getCommonObjectives() {
        return commonObjectives;
    }

    /**
     * Returns the deck of gold cards.
     *
     * @return The gold deck.
     */
    public GoldDeck getGoldDeck() {
        return goldDeck;
    }

    /**
     * Returns the deck of resource cards.
     *
     * @return The resource deck.
     */
    public ResourceDeck getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Returns the array of cards available in the market.
     *
     * @return The market cards.
     */
    public PlayableCard[] getMarket() {
        return market;
    }

    /**
     * Returns the current player taking their turn.
     *
     * @return The current player color.
     */
    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Checks if it is the last lap of the game.
     *
     * @return True if it is the last lap, false otherwise.
     */
    public boolean isLastLap() {
        return lastLap;
    }

    /**
     * Saves the current ServerGameState instance to a JSON file identified by the provided ID.
     *
     * @param id The ID used to identify the JSON file.
     */
    private void save(int id) {
        Gson gson = GsonSingleton.getGson();

        File directory = new File("interruptedGames");

        File file = new File(directory, id + ".json");
        // Create file if it doesn't exist
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    logger.info("Failed to create file: " + file.getPath());
                    return;
                }
            }
        } catch (IOException e) {
            logger.info("Failed to save file");
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this, writer);
        } catch (IOException e){
            logger.info("Failed to save file");
        }
    }

    /**
     * Loads the ServerGameState instance from a JSON file identified by the provided ID.
     *
     * @param id The ID used to identify the JSON file.
     * @throws IOException If an I/O error occurs during loading.
     */
    private void load(int id) throws IOException {
        Gson gson = GsonSingleton.getGson();
        try (FileReader reader = new FileReader("interruptedGames/" + id + ".json")) {
            ServerGameState loadedState = gson.fromJson(reader, ServerGameState.class);
            this.players = loadedState.players;
            this.gameOrder = loadedState.gameOrder;

            this.starters = loadedState.starters;
            this.starterSides = loadedState.starterSides;
            this.playableCards = loadedState.playableCards;
            this.positions = loadedState.positions;
            this.sides = loadedState.sides;

            this.scores = loadedState.scores;
            this.personalObjectives = loadedState.personalObjectives;
            this.hands = loadedState.hands;
            this.commonObjectives = loadedState.commonObjectives;
            this.goldDeck = loadedState.goldDeck;
            this.resourceDeck = loadedState.resourceDeck;
            this.market = loadedState.market;
            this.currentPlayer = loadedState.currentPlayer;
            this.lastLap = loadedState.lastLap;
        }
    }
}
