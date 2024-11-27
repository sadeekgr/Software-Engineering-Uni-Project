package it.polimi.ingsw.model.game;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exception.*;

import it.polimi.ingsw.gamestate.ServerGameState;
import it.polimi.ingsw.message.GameStateMessage;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.notify.*;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

import it.polimi.ingsw.utilities.GsonSingleton;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a match in the game.
 */
public class Match {
    private final int lobbyId;

    private final HashMap<Player, Controller> clients;
    private final ArrayList<Player> players;
    private GoldDeck goldDeck;
    private ResourceDeck resourceDeck;
    private PlayableCard[] market;
    private  Objective[] objectives;
    private ScoreTrack scoreTrack;
    private boolean started;
    private volatile Player currentPlayer;
    private boolean lastRound;
    private final Map<Player, PlayerInitialConfig> playersInitialConfig;
    private boolean gameEnded;

    /**
     * Constructs a new Match object.
     *
     */
    public Match(int lobbyId) {
        this.lobbyId = lobbyId;
        players = new ArrayList<>();

        market = new PlayableCard[4];
        objectives = new Objective[2];
        currentPlayer = null;
        scoreTrack = null;
        started = false;
        lastRound = false;
        playersInitialConfig = new HashMap<>();
        clients = new HashMap<>();
        gameEnded = false;
    }

    /**
     * Loads the match with the provided controllers and initializes game state.
     *
     * @param controllers List of controllers associated with players in the match.
     * @throws IOException If there's an error in loading game state.
     */
    public void load(List<Controller> controllers) throws IOException {
        started = true;
        gameEnded = false;

        ServerGameState serverGameState = new ServerGameState(lobbyId);
        List<PlayerColor> gameOrder = serverGameState.getGameOrder();
        List<PlayerField> fields = serverGameState.getFields();
        List<Integer> scores = serverGameState.getScores();
        List<Objective> personalObjectives = serverGameState.getPersonalObjectives();
        List<PlayableCard[]> hands = serverGameState.getHands();

        objectives = serverGameState.getCommonObjectives();
        market = serverGameState.getMarket();
        goldDeck = serverGameState.getGoldDeck();
        resourceDeck = serverGameState.getResourceDeck();

        for(int i = 0; i <  gameOrder.size(); i++){
            players.add(new Player(
                    this,
                    hands.get(i),
                    fields.get(i),
                    personalObjectives.get(i),
                    gameOrder.get(i)
            ));
        }

        lastRound = serverGameState.isLastLap();

        for(Player p : players){
            if(p.getColor() == serverGameState.getCurrentPlayer()){
                currentPlayer = p;
                break;
            }
        }

        scoreTrack = new ScoreTrack(players);
        for(Player p : players){
            scoreTrack.addScore(p, scores.get(gameOrder.indexOf(p.getColor())));
            for(Controller controller : controllers){
                if(Objects.equals(controller.getUsername(), serverGameState.getPlayers().get(gameOrder.indexOf(p.getColor()))))
                {
                    clients.put(p, controller);
                    controller.setPlayer(p);
                    break;
                }
            }
        }
    }

    /**
     * Resumes the game from a paused state, updating players and clients with current game state.
     *
     * @throws InterruptedException If thread is interrupted while waiting.
     */
    public void resumeGame() throws InterruptedException {
        Map<String, PlayerColor> playersColor = new HashMap<>();
        Map<PlayerColor, PlayerField> fields = new HashMap<>();
        Map<PlayerColor, Integer> scores = new HashMap<>();
        Map<PlayerColor, Symbol[]> cardsBack = new HashMap<>();
        for(Player p : players){
            playersColor.put(clients.get(p).getUsername(), p.getColor());
            fields.put(p.getColor(), p.getPlayerField());
            scores.put(p.getColor(), scoreTrack.getPlayerScore(p));
            Symbol[] back = new Symbol[3];
            for(int i = 0; i < 3; i++){
                back[i] = p.getHand()[i].getKingdom();
            }
            cardsBack.put(p.getColor(), back);
        }

        Symbol topResource = null;
        try {
            topResource = resourceDeck.topCardKingdom();
        } catch (Exception ignored){}


        Symbol topGold = null;
        try {
            topGold = goldDeck.topCardKingdom();
        } catch (Exception ignored){}

        for(Player p : players) {
            PlayerInitialConfig config = new PlayerInitialConfig();
            try {
                config.setStarterCard((StarterCard) p.getPlayerField().getCards().getFirst().getCard());
                config.setChosenStarterSide(p.getPlayerField().getCards().getFirst().isFront());
                Objective[] objectives = new Objective[]{ p.getObjective(), null };
                config.setObjectives(objectives);
                config.setChosenObjective(0);
            } catch (Exception ignored){}

            playersInitialConfig.put(p, config);
        }

        for(Player p : players){
            clients.get(p).update(new GameStateMessage(
                    playersColor,
                    players.stream().map(Player::getColor).toList(),
                    fields,
                    scores,
                    cardsBack,
                    p.getObjective(),
                    p.getHand(),
                    objectives,
                    topResource,
                    resourceDeck.cards.size(),
                    topGold,
                    goldDeck.cards.size(),
                    market,
                    currentPlayer.getColor()
            ));
        }

        broadcast(new NotifyTurn(currentPlayer.getColor()));

        synchronized (this){
            wait();
        }
    }

    // Methods for managing players

    /**
     * Allows a player to take a seat in the match.
     *
     * @param controller The controller associated with the player taking the seat.
     * @throws MatchExceptions If the match is already full or if the match is already started.
     */
    public void takeSeat(Controller controller) throws MatchExceptions {
        synchronized (players) {
            if(started) {
                throw new MatchExceptions(MatchExceptions.ErrorCode.MATCH_ALREADY_STARTED, "The match is already started!");
            }

            if(players.size() == 4) {
                throw new MatchExceptions(MatchExceptions.ErrorCode.MATCH_FULL, "The match is full!");
            }

            Player p = new Player(this);
            players.add(p);
            clients.put(p, controller);
            controller.setPlayer(p);
        }
    }

    /**
     * Allows a player to leave their seat in the match.
     *
     * @param controller The player leaving the match.
     * @throws MatchExceptions If the match has already started or if the player is not found.
     */
    public void leaveSeat(Controller controller) throws MatchExceptions{
        synchronized (players){
            if(!started) {
                Player p = controller.getPlayer();
                if (!clients.containsKey(p)) {
                    throw new MatchExceptions(MatchExceptions.ErrorCode.PLAYER_NOT_FOUND, "Player not found!");
                }
                clients.remove(p);
                players.remove(p);
            } else {
                throw new MatchExceptions(MatchExceptions.ErrorCode.MATCH_ALREADY_STARTED, "The match is already started!");
            }
        }
    }

    /**
     * Retrieves the available player colors that have not been assigned to any player.
     *
     * @return The list of available player colors.
     */
    private ArrayList<PlayerColor> getAvailableColor(){
        ArrayList<PlayerColor> color = new ArrayList<>();

        boolean used;
        for(PlayerColor c : PlayerColor.values()){
            used = false;
            for(Player p : players){
                if(p.getColor() == c){
                    used = true;
                    break;
                }
            }
            if(!used){
                color.add(c);
            }
        }

        return color;
    }

    /**
     * Allows a player to choose the side of the starter card.
     *
     * @param p     The player making the choice.
     * @param side  The chosen side of the starter card.
     * @throws PlayerExceptions If it's not the time to choose or if the card side has already been chosen
     */
    public void chooseStarterCardSide(Player p, boolean side) throws PlayerExceptions {
        synchronized (playersInitialConfig){
            if(!playersInitialConfig.containsKey(p)){
                throw new PlayerExceptions(PlayerExceptions.ErrorCode.CONFIGURATION_CHOICE_NOT_PERMITTED, "Wait game to start!");
            }

            PlayerInitialConfig config = playersInitialConfig.get(p);
            config.setChosenStarterSide(side);
            p.placeStarterCard(config.getStarterCard(), side);

            //NOTIFY PLAYER
            Controller c = clients.get(p);
            c.update(new NotifyChosenStarter(config.getStarterCard(), side));

            for(Player player : players){
                if(playersInitialConfig.get(player).getStarterSide() == null){
                    return;
                }
            }

            playersInitialConfig.notify();
        }
    }

    /**
     * Allows a player to choose an objective.
     *
     * @param p     The player making the choice.
     * @param num   The index of the chosen objective.
     * @throws PlayerExceptions If it's not the time to choose or if the objective has already been chosen.
     */
    public void chooseObjective(Player p, int num) throws PlayerExceptions {
        synchronized (playersInitialConfig){
            if(!playersInitialConfig.containsKey(p)){
                throw new PlayerExceptions(PlayerExceptions.ErrorCode.CONFIGURATION_CHOICE_NOT_PERMITTED, "Wait game to start!");
            }

            PlayerInitialConfig config = playersInitialConfig.get(p);
            config.setChosenObjective(num);
            p.setObjective(config.getChosenObjective());

            //NOTIFY PLAYER
            Controller c = clients.get(p);
            c.update(new NotifyChosenObjective(config.getChosenObjective()));

            for(Player player : players){

                if(playersInitialConfig.get(player).getChosenObjective() == null){
                    return;
                }
            }

            playersInitialConfig.notify();
        }
    }

    /**
     * Starts the match.
     *
     * @throws MatchExceptions   If the match has already started or if the number of players is invalid.
     * @throws InterruptedException if a thread is interrupted while waiting
     * @throws JsonLoadException              If there is an error loading JSON data.
     */
    public void startMatch() throws JsonLoadException, InterruptedException, MatchExceptions {
        synchronized (players) {
            if(!started) {
                if (players.size() > 1 && players.size() < 5) {
                    started = true;
                } else {
                    throw new MatchExceptions(MatchExceptions.ErrorCode.INVALID_NUMBER_OF_PLAYERS, "Invalid number of players!");
                }
            } else {
                throw new MatchExceptions(MatchExceptions.ErrorCode.MATCH_ALREADY_STARTED, "The match is already started!");
            }
        }

        goldDeck = new GoldDeck();
        resourceDeck = new ResourceDeck();

        broadcast(new NotifyMatchStarted());

        for(Player p : players){
            playersInitialConfig.put(p, new PlayerInitialConfig());
        }

        // Color Assignment
        Random rand = new Random();
        for(Player p : players){
            List<PlayerColor> availableColors = getAvailableColor();
            int randomIndex = rand.nextInt(availableColors.size());
            p.setPlayerColor(availableColors.get(randomIndex));
        }

        Map<String, PlayerColor> colors = new HashMap<>();
        for (Player p : players){
            String username = clients.get(p).getUsername();
            colors.put(username, p.getColor());
        }

        broadcast(new NotifyColorsAssignment(colors));

        // Rulebook: Game setup
        // 1 : score track

        scoreTrack = new ScoreTrack(players);

        // 2 : decks, market
        resourceDeck.shuffle();
        goldDeck.shuffle();

        try {
            market[0] = resourceDeck.draw();
            market[1] = resourceDeck.draw();
            market[2] = goldDeck.draw();
            market[3] = goldDeck.draw();

            broadcast(new NotifyCardState(market, resourceDeck.topCardKingdom(), goldDeck.topCardKingdom()));
        } catch (PlayerExceptions e){
            // can't happen
        }

        // 3 : starter card, color, hand
        ArrayList<StarterCard> starterCards = GsonSingleton.loadJson("/starterCards.json", new TypeToken<List<StarterCard>>(){}.getType());
        Collections.shuffle(starterCards);

        synchronized (playersInitialConfig) {
            for (Player p : players) {
                StarterCard c = starterCards.removeFirst();
                playersInitialConfig.get(p).setStarterCard(c);

                // NOTIFY PLAYER
                Controller controller = clients.get(p);
                controller.update(new NotifyToChooseStarter(c));
            }
            playersInitialConfig.wait();
        }

        if(gameEnded){
            return;
        }

        broadcast(new NotifyStarterCards(
                players.stream()
                        .map(Player::getColor)
                        .collect(Collectors.toList()),
                players.stream()
                        .map(p -> playersInitialConfig.get(p).getStarterCard())
                        .collect(Collectors.toList()),
                players.stream()
                        .map(p -> playersInitialConfig.get(p).getStarterSide())
                        .collect(Collectors.toList())
        ));

        Map<PlayerColor, Symbol[]> backHands = new HashMap<>();
        for(Player p : players){
            // draw hand
            PlayableCard[] playerHand = new PlayableCard[3];
            try{
                playerHand[0] = resourceDeck.draw();
                playerHand[1] = resourceDeck.draw();
                playerHand[2] = goldDeck.draw();
            } catch (PlayerExceptions e){
                //
            }
            p.setHand(playerHand);

            backHands.put(p.getColor(), new Symbol[]{playerHand[0].getKingdom(), playerHand[1].getKingdom(), playerHand[2].getKingdom()});
        }

        // NOTIFY PLAYERS
        for(Player p : players) {
            Controller controller = clients.get(p);
            controller.update(new NotifyPlayerHand(p.getHand(), backHands));
        }

        // 4 : common objectives
        ArrayList<Objective> objectiveCards = GsonSingleton.loadJson("/objectiveCards.json", new TypeToken<List<Objective>>(){}.getType());
        Collections.shuffle(objectiveCards);

        objectives[0] = objectiveCards.removeFirst();
        objectives[1] = objectiveCards.removeFirst();

        broadcast(new NotifyGlobalObjectives(objectives));

        // 5 : personal objectives
        synchronized (playersInitialConfig) {
            for(Player p : players) {
                Objective[] objectives = new Objective[]{
                        objectiveCards.removeFirst(),
                        objectiveCards.removeFirst()
                };
                playersInitialConfig.get(p).setObjectives(objectives);

                // NOTIFY PLAYER
                Controller controller = clients.get(p);
                controller.update(new NotifyToChooseObjective(objectives[0], objectives[1]));
            }
            playersInitialConfig.wait();
        }

        if(gameEnded){
            return;
        }

        // 6 : choose first player
        Collections.shuffle(players); //starting player is the first of the list

        // start the game
        currentPlayer = getStartingPlayer();

        try { // maybe split the function in 2 parts
            broadcast(new NotifyCardState(market, resourceDeck.topCardKingdom(), goldDeck.topCardKingdom()));
        } catch (PlayerExceptions e){
            e.printStackTrace();
        }

        broadcast(new NotifySetUpFinished(
                players.stream()
                        .map(Player::getColor)
                        .collect(Collectors.toList())
        ));

        // NOTIFY PLAYER ITS TURN TO PLAY
        broadcast(new NotifyTurn(currentPlayer.getColor()));

        // save ServerGameState
        new ServerGameState(
            lobbyId,
            players.stream().map(Player::getColor).toList(),
            players.stream().map(p -> clients.get(p).getUsername()).toList(),
            players.stream().map(Player::getPlayerField).toList(),
            players.stream().map(p -> scoreTrack.getPlayerScore(p)).toList(),
            players.stream().map(Player::getObjective).toList(),
            players.stream().map(Player::getHand).toList(),
            objectives,
            goldDeck,
            resourceDeck,
            market,
            currentPlayer.getColor(),
            lastRound
        );

        synchronized (this){
            wait();
        }
    }

    /**
     * Gets the list of players in the match.
     *
     * @return The list of players.
     */
    public ArrayList<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    /**
     * Gets the score track of the match.
     *
     * @return The score track.
     */
    public ScoreTrack getScoreTrack() {
        return new ScoreTrack(scoreTrack);
    }

    /**
     * Gets the starting player of the match.
     *
     * @return The starting player.
     */
    public Player getStartingPlayer(){
        return players.getFirst();
    }

    /**
     * Gets the current player in the match.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Gets the symbol of the top gold card in the gold deck.
     *
     * @return The symbol of the top gold card.
     * @throws PlayerExceptions If the gold deck is empty.
     */
    public Symbol getTopGoldKingdom() throws PlayerExceptions{
        return goldDeck.topCardKingdom();
    }

    /**
     * Gets the symbol of the top resource card in the resource deck.
     *
     * @return The symbol of the top resource card.
     * @throws PlayerExceptions If the resource deck is empty.
     */
    public Symbol getTopResourceKingdom() throws PlayerExceptions{
        return resourceDeck.topCardKingdom();
    }

    /**
     * Draws a gold card from the gold deck.
     *
     * @return The drawn gold card.
     * @throws PlayerExceptions If the gold deck is empty.
     */
    public PlayableCard drawGold() throws PlayerExceptions {
        PlayableCard card = goldDeck.draw();

        clients.get(currentPlayer).update(new NotifyDraw(currentPlayer.getColor(), NotifyDraw.DrawType.GOLD, goldDeck.topCardKingdom(), card));
        broadcast(new NotifyDraw(currentPlayer.getColor(), NotifyDraw.DrawType.GOLD, goldDeck.topCardKingdom()), currentPlayer);

        return  card;
    }

    /**
     * Draws a resource card from the resource deck.
     *
     * @return The drawn resource card.
     * @throws PlayerExceptions If the resource deck is empty.
     */
    public PlayableCard drawResource() throws PlayerExceptions {
        PlayableCard card = resourceDeck.draw();

        clients.get(currentPlayer).update(new NotifyDraw(currentPlayer.getColor(), NotifyDraw.DrawType.RESOURCE, resourceDeck.topCardKingdom(), card));
        broadcast(new NotifyDraw(currentPlayer.getColor(), NotifyDraw.DrawType.RESOURCE, resourceDeck.topCardKingdom()), currentPlayer);

        return card;
    }

    /**
     * Draws a card from the market at the specified index.
     *
     * @param index The index of the card to draw from the market.
     * @return The drawn card.
     * @throws ArrayIndexOutOfBoundsException If the index is out of bounds.
     * @throws PlayerExceptions If the specified index does not contain a card.
     */
    public PlayableCard drawMarket(int index) throws ArrayIndexOutOfBoundsException, PlayerExceptions {
        if (index<0 || index>3){
            throw new ArrayIndexOutOfBoundsException();
        }

        PlayableCard card = market[index];
        if(card == null){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.INVALID_MARKET_CHOICE, "Invalid market choice, card not present"); // eccezioni index non valido? eccezione elemento non presente?
        }

        Symbol topKingdom = null;

                // fissiamo che indici 0 e 1 sono risorse, 2 e 3 oro e cosi non uso istanceof ma checko indice
        PlayableCard replaceCard;
        try {
            if (index < 2) {
                replaceCard = resourceDeck.draw();
                topKingdom = resourceDeck.topCardKingdom();
            } else {
                replaceCard = goldDeck.draw();
                topKingdom = goldDeck.topCardKingdom();
            }
        }
        catch (PlayerExceptions e){
            // Bo fai qualcosa? Segnala che il mazzo è finito? magari la partita è finita
            replaceCard = null;
        }

        market[index] = replaceCard;


        broadcast(new NotifyDraw(currentPlayer.getColor(), NotifyDraw.DrawType.MARKET, topKingdom, index, replaceCard));

        return card;
    }

    /**
     * Updates the score track with the given score for the current player.
     *
     * @param score The score to be added to the score track.
     */
    public void updateScoreTrack(int score){
        scoreTrack.addScore(currentPlayer, score); // lancia eccezione che poi viene raccolta nel player se un player che non è il current cerca di settare punteggio?
    }

    /**
     * Checks if the end condition of the match is met.
     *
     * @return True if the end condition is met, otherwise false.
     */
    public boolean endConditionMet(){
        return (resourceDeck.isEmpty() && goldDeck.isEmpty()) || scoreTrack.isFinished();
    }

    /**
     * Advances the game to the next turn.
     */
    public void nextTurn(){
        Player nextPlayer = getNextPlayer();
        if(nextPlayer.equals(getStartingPlayer())){
            if(lastRound){
                endMatch();
                return;
            }
            else if(endConditionMet()){
                lastRound = true;
                broadcast(new NotifyLastRound());
            }
        }

        currentPlayer = nextPlayer;

        // NOTIFY PLAYER ITS TURN TO PLAY
        broadcast(new NotifyTurn(currentPlayer.getColor()));

        // save ServerGameState
        new ServerGameState(
                lobbyId,
                players.stream().map(Player::getColor).toList(),
                players.stream().map(p -> clients.get(p).getUsername()).toList(),
                players.stream().map(Player::getPlayerField).toList(),
                players.stream().map(p -> scoreTrack.getPlayerScore(p)).toList(),
                players.stream().map(Player::getObjective).toList(),
                players.stream().map(Player::getHand).toList(),
                objectives,
                goldDeck,
                resourceDeck,
                market,
                currentPlayer.getColor(),
                lastRound
        );
    }

    /**
     * Gets the player who plays next.
     *
     * @return The player who plays next.
     */
    public Player getNextPlayer(){
        boolean found = false;
        for(Player p : players){
            if(found){
                return p;
            }
            if(p.equals(currentPlayer)){
                found = true;
            }
        }
        return players.getFirst();
    }

    /**
     * Retrieves the common objectives for the match.
     *
     * @return An array containing the common objectives.
     */
    public Objective[] getObjectives() {
        return Arrays.copyOf(objectives, objectives.length);
    }

    /**
     * Retrieves the current market of the match.
     *
     * @return An array containing the cards in the market.
     */
    public PlayableCard[] getMarket() {
        return Arrays.copyOf(market, market.length);
    }

    /**
     * Ends the match and determines the winner(s).
     */
    synchronized public void endMatch(){
        currentPlayer = null;
        gameEnded = true;

        Map<Player, Integer> objScore = new HashMap<>();
        int score;
        for(Player p : players){
            // add personal objective
            score = p.calculatePersonalObjectiveScore();
            // add common objective
            score += p.calculateObjectiveScore(objectives[0]);
            score += p.calculateObjectiveScore(objectives[1]);

            objScore.put(p, score);
            scoreTrack.addScore(p, score);
        }

        ArrayList<Player> winners = new ArrayList<>();

        ArrayList<Player> bestScorers = scoreTrack.getBestScorers();
        if(bestScorers.size() == 1){
            winners.add(bestScorers.getFirst());
        }
        else{
            // equal score between two or more players
            int bestObjScore = 0;
            for(Player p : bestScorers){
                if(objScore.get(p) > bestObjScore){
                    bestObjScore = objScore.get(p);
                }
            }

            for(Player p : bestScorers){
                if(objScore.get(p) == bestObjScore){
                    winners.add(p);
                }
            }
        }

        int numWinners = winners.size();
        List<Player> orderedPlayers = scoreTrack.getPlayersOrderedByScore();

        broadcast(new NotifyEndMatch(
                numWinners,
                orderedPlayers.stream().map(Player::getColor).toList(),
                orderedPlayers.stream().map(p -> scoreTrack.getPlayerScore(p)).toList(),
                orderedPlayers.stream().map(Player::getObjective).toList(),
                orderedPlayers.stream().map(objScore::get).toList()
        ));

        this.notifyAll();
    }

    /**
     * Checks if it's the last round of the match.
     *
     * @return True if it's the last round, otherwise False.
     */
    public boolean lastRound(){ return lastRound; }

    /**
     * Checks if the match has started.
     *
     * @return True if the match has started, otherwise false.
     */
    public boolean isStarted(){ return started; }

    /**
     * Broadcasts a message to all players in the match.
     *
     * @param m The message to broadcast.
     */
    public void broadcast(Message m){
        for(Player p : players){
            Controller c = clients.get(p);
            c.update(m);
        }
    }

    /**
     * Sends a message to all players, excluding a specific player.
     *
     * @param m the message to send
     * @param exclude the player to exclude from receiving the message
     */
    public void broadcast(Message m, Player exclude){
        for(Player p : players){
            if (p != exclude) {
                Controller c = clients.get(p);
                c.update(m);
            }
        }
    }

    /**
     * Ends the match due to player disconnection, handling cleanup and notifying clients.
     */
    public void endForDisconnection(){
        gameEnded = true;

        boolean inSetUp = playersInitialConfig.size() != players.size();

        for(Player p : players){
            if(playersInitialConfig.get(p).getChosenObjective() == null){
                inSetUp = true;
            }
        }

        if(inSetUp){
            synchronized (playersInitialConfig) {
                playersInitialConfig.notifyAll();
                playersInitialConfig.notifyAll();
            }

            broadcast(new NotifyEndMatch(-1, null, null, null, null));
        }
        else {
            Map<Player, Integer> objScore = new HashMap<>();
            int score;
            for(Player p : players){
                // add personal objective
                score = p.calculatePersonalObjectiveScore();
                // add common objective
                score += p.calculateObjectiveScore(objectives[0]);
                score += p.calculateObjectiveScore(objectives[1]);
                objScore.put(p, score);
                scoreTrack.addScore(p, score);
            }

            List<Player> orderedPlayers = scoreTrack.getPlayersOrderedByScore();
            broadcast(new NotifyEndMatch(
                    0,
                    orderedPlayers.stream().map(Player::getColor).toList(),
                    orderedPlayers.stream().map(p -> scoreTrack.getPlayerScore(p)).toList(),
                    orderedPlayers.stream().map(Player::getObjective).toList(),
                    orderedPlayers.stream().map(objScore::get).toList()

            ));
        }

        synchronized (this) {
            this.notifyAll();
        }
    }
}