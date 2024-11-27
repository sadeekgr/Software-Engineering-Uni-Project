package it.polimi.ingsw.gamestate;

import it.polimi.ingsw.message.ChatMessage;
import it.polimi.ingsw.message.GameStateMessage;
import it.polimi.ingsw.message.notify.*;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.*;

/**
 * Represents the state of the game for the client side.
 * It holds information about players, cards, scores, etc.
 */
public class GameState {
    private final String user;
    private Map<String, PlayerColor> players;

    private List<PlayerColor> gameOrder;
    private Map<PlayerColor, PlayerField> fields;
    private Map<PlayerColor, Integer> scores;
    private Map<PlayerColor, Symbol[]> cardsBack;

    private Objective personalObjective;
    private PlayableCard[] hand;

    private final List<ChatMessage> publicChat;
    private final Map<Set<String>, List<ChatMessage>> privateChats; // only your chats

    private Objective[] commonObjectives;

    private Symbol topResourceDeck;
    private int numResourceCard;

    private Symbol topGoldDeck;
    private int numGoldCard;

    private PlayableCard[] market;

    private PlayerColor currentPlayer;

    /**
     * Constructor for PlayerGameView.
     *
     * @param user The username of the player associated with this game view.
     */
    public GameState(String user) {
        this.user = user;
        fields = new HashMap<>();
        scores = new HashMap<>();
        publicChat = new ArrayList<>();
        privateChats = new HashMap<>();
    }

    /**
     * Gets the username of the player associated with this game view.
     *
     * @return The username of the player.
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the mapping of player usernames to their colors.
     *
     * @return A map of player usernames to PlayerColor.
     */
    public Map<String, PlayerColor> getPlayers() {
        return players;
    }

    /**
     * Gets the order of players in the game.
     *
     * @return A list of PlayerColor representing the game order.
     */
    public List<PlayerColor> getGameOrder() {
        return gameOrder;
    }

    /**
     * Gets the fields of all players.
     *
     * @return A map of PlayerColor to PlayerField.
     */
    public Map<PlayerColor, PlayerField> getFields() {
        return fields;
    }

    /**
     * Gets the scores of all players.
     *
     * @return A map of PlayerColor to scores.
     */
    public Map<PlayerColor, Integer> getScores() {
        return scores;
    }

    /**
     * Gets the back of the cards for all players.
     *
     * @return A map of PlayerColor to an array of Symbol representing the backs of the cards.
     */
    public Map<PlayerColor, Symbol[]> getCardsBack() {
        return cardsBack;
    }

    /**
     * Gets the personal objective of the player.
     *
     * @return The personal Objective of the player.
     */
    public Objective getPersonalObjective() {
        return personalObjective;
    }

    /**
     * Gets the cards in the player's hand.
     *
     * @return An array of PlayableCard representing the player's hand.
     */
    public PlayableCard[] getHand() {
        return hand;
    }

    /**
     * Gets the public chat messages.
     *
     * @return A list of public ChatMessage.
     */
    public List<ChatMessage> getPublicChat() {
        return publicChat;
    }

    /**
     * Gets the private chat messages.
     *
     * @return A map of sets of player usernames to lists of private ChatMessage.
     */
    public Map<Set<String>, List<ChatMessage>> getPrivateChats() {
        return privateChats;
    }

    /**
     * Gets the common objectives of the game.
     *
     * @return An array of common Objective.
     */
    public Objective[] getCommonObjectives() {
        return commonObjectives;
    }

    /**
     * Gets the top symbol of the resource deck.
     *
     * @return The top Symbol of the resource deck.
     */
    public Symbol getTopResourceDeck() {
        return topResourceDeck;
    }

    /**
     * Gets the top symbol of the gold deck.
     *
     * @return The top Symbol of the gold deck.
     */
    public Symbol getTopGoldDeck() {
        return topGoldDeck;
    }

    /**
     * Retrieves the current market of playable cards.
     *
     * @return an array of PlayableCard representing the current market.
     */
    public PlayableCard[] getMarket() {
        return market;
    }

    /**
     * Gets the field of the player associated with this game view.
     *
     * @return The PlayerField of the associated player.
     */
    public PlayerField getMyField(){ return fields.get(players.get(user)); }

    /**
     * Update the state of the game based on the received notify message.
     *
     * @param m The notify message to process.
     */
    public void updateState(NotifyMessage m){
        switch(m.getNotifyType()){
            case YOUR_TURN:
                currentPlayer = ((NotifyTurn) m).getColor();
                break;
            case GLOBAL_OBJECTIVES:
                commonObjectives = ((NotifyGlobalObjectives) m).getObjectives();
                break;
            case STARTER_CARDS:
                NotifyStarterCards starterMessage = (NotifyStarterCards) m;
                for(int i = 0; i < starterMessage.getColors().size(); i++){
                    PlayerColor color = starterMessage.getColors().get(i);

                    if(color != players.get(user)) {
                        StarterCard card = starterMessage.getStarterCards().get(i);
                        boolean side = starterMessage.getSides().get(i);

                        fields.get(color).placeStarterCard(card, side);
                    }
                }
                break;
            case COLOR_ASSIGNMENT:
                players = ((NotifyColorsAssignment) m).getColors();
                for(PlayerColor p : players.values()){
                    fields.put(p, new PlayerField());
                    scores.put(p, 0);
                }
                break;
            case CARD_STATE:
                NotifyCardState cardState = (NotifyCardState) m;
                topGoldDeck = cardState.getGoldKingdom();
                topResourceDeck = cardState.getResourceKingdom();
                market = cardState.getMarket();
                break;
            case PLAYER_HAND:
                NotifyPlayerHand hands = (NotifyPlayerHand) m;
                hand = hands.getHand();
                cardsBack = hands.getBackHands();
                break;
            case DRAW:
                NotifyDraw draw = (NotifyDraw) m;
                PlayerColor who = draw.getWho();
                Symbol topKingdom = draw.getKingdom();

                switch(draw.getWhere()){
                    case MARKET:
                        int index = draw.getIndex();
                        if(index < 2){
                            topResourceDeck = topKingdom;
                            if (numResourceCard > 0){
                                numResourceCard -= 1;
                            }
                        }
                        else{
                            topGoldDeck = topKingdom;
                            if (numGoldCard > 0) {
                                numGoldCard -= 1;
                            }
                        }

                        if (who == players.get(user)){
                            addToHand(market[index]);
                        }
                        addToBackCardsHand(cardsBack.get(who), market[index].getKingdom());

                        market[index] = draw.getCard();
                        break;
                    case GOLD:
                        if (who == players.get(user)) {
                            addToHand(draw.getCard());
                        }
                        addToBackCardsHand(cardsBack.get(who), topGoldDeck);

                        topGoldDeck = topKingdom;
                        numGoldCard -= 1;
                        break;
                    case RESOURCE:
                        if (who == players.get(user)) {
                            addToHand(draw.getCard());
                        }
                        addToBackCardsHand(cardsBack.get(who), topResourceDeck);

                        topResourceDeck = topKingdom;
                        numResourceCard -= 1;
                }
                break;

            case PLAY_CARD:
                NotifyCardPlayed play = (NotifyCardPlayed) m;
                PlayerColor p = play.getWho();
                fields.get(p).placeCard(play.getCard(), play.getSide(), play.getPosition());
                int updatedScore = scores.get(p) + play.getScore();
                scores.put(p, updatedScore);
                if(p == players.get(user)){
                    hand[play.getIndex()] = null;
                } else {
                    cardsBack.get(p)[play.getIndex()] = null;
                }
                break;
            case SET_UP_END:
                NotifySetUpFinished setUpEnd = (NotifySetUpFinished) m;
                gameOrder = setUpEnd.getColors();
                break;
            case STARTER_CHOSEN:
                NotifyChosenStarter starter = (NotifyChosenStarter) m;
                fields.get(players.get(user)).placeStarterCard(starter.getCard(), starter.getSide());
                break;
            case OBJECTIVE_CHOSEN:
                personalObjective = ((NotifyChosenObjective) m).getObjective();
                break;
        }
    }

    /**
     * Updates the back of the player's hand with the symbol associated with the drew card.
     *
     * @param playerHand The player's hand.
     * @param back       The symbol to add.
     */
    private void addToBackCardsHand(Symbol[] playerHand, Symbol back){
        for(int i = 0; i < playerHand.length; i++){
            if(playerHand[i] == null){
                playerHand[i] = back;
                break;
            }
        }
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to add.
     */
    private void addToHand(PlayableCard card){
        for(int i = 0; i < hand.length; i++){
            if(hand[i] == null){
                hand[i] = card;
                break;
            }
        }
    }

    /**
     * Update the chat with a new message.
     *
     * @param m The chat message to update.
     */
    public void updateChat(ChatMessage m){
        if(m.getChatType() == ChatMessage.ChatType.PUBLIC){
            publicChat.add(m);
        }
        else {
            Set<String> chatId = m.getRecipients();
            chatId.add(m.getSender());
            chatId.remove(user);

            if(!privateChats.containsKey(chatId)){
                privateChats.put(chatId, new ArrayList<>());
            }

            privateChats.get(chatId).add(m);
        }
    }

    /**
     * Creates a new private chat with the specified set of player usernames.
     *
     * @param id The set of player usernames to include in the chat.
     */
    public void createChat(Set<String> id){
        if(id == null){
            return;
        }

        id.remove(user); // remove user from id

        if(!players.keySet().containsAll(id) || players.size() - 1 == id.size()){
            return;
        }

        if(!privateChats.containsKey(id)){
            privateChats.put(id, new ArrayList<>());
        }
    }

    public String getName(PlayerColor color){
        for(String name : players.keySet()){
            if(players.get(name) == color){
                return name;
            }
        }
        return "";
    }

    /**
     * Loads the game state from a provided message.
     *
     * @param m The message containing the game state to load.
     */
    public void load(GameStateMessage m) {
        players = m.players();
        gameOrder = m.gameOrder();
        fields = m.fields();
        scores = m.scores();
        cardsBack = m.cardsBack();

        personalObjective = m.personalObjective();
        hand = m.hand();

        commonObjectives = m.commonObjectives();

        topResourceDeck = m.topResourceDeck();
        numResourceCard = m.numResourceCard();

        topGoldDeck = m.topGoldDeck();
        numGoldCard = m.numGoldCard();
        market = m.market();
        currentPlayer = m.currentPlayer();
    }
}
