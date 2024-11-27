package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ChatException;
import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.lobby.Chat;
import it.polimi.ingsw.message.ChatMessage;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.action.*;
import it.polimi.ingsw.message.error.*;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.server.Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static it.polimi.ingsw.exception.PlayerExceptions.ErrorCode.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Controller} class.
 * These tests validate the behavior of the Controller class, which manages interactions between players,
 * game actions, and communication with external systems via a {@link Connection}.
 */
public class ControllerTest {

    private Connection connectionMock;
    private Player playerMock;
    private Chat chatMock;
    private Controller controller;

    /**
     * Sets up the test environment before each test method.
     * Initializes the mocks for Connection, Player, and Chat, and creates an instance of Controller with the mocked Connection.
     *
     * @throws Exception if an error occurs during setup
     */
    @Before
    public void setUp() throws Exception {
        connectionMock = mock(Connection.class);
        playerMock = mock(Player.class);
        chatMock = mock(Chat.class);
        controller = new Controller(connectionMock);
    }

    /**
     * Tests the {@link Controller#setPlayer(Player)} method.
     * Verifies that the player is correctly set.
     */
    @Test
    public void setPlayer() {
        controller.setPlayer(playerMock);
        assertEquals(playerMock, controller.getPlayer());
    }

    /**
     * Tests the {@link Controller#getPlayer()} method.
     * Verifies that the correct player is returned.
     */
    @Test
    public void getPlayer() {
        controller.setPlayer(playerMock);
        assertEquals(playerMock, controller.getPlayer());
    }

    /**
     * Tests the action handling for choosing a starter card.
     * Verifies that the {@link Player#chooseStarterCardSide(boolean)} method is called with the correct parameter.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionChooseStarter() throws PlayerExceptions {
        controller.setPlayer(playerMock);
        ChooseStarter message = mock(ChooseStarter.class);
        when(message.getAction()).thenReturn(GameAction.CHOOSE_STARTER);
        when(message.isFront()).thenReturn(true);

        controller.action(message);

        verify(playerMock, times(1)).chooseStarterCardSide(true);
    }

    /**
     * Tests the action handling for choosing a starter card with an error.
     * Verifies that an appropriate error message is sent when a {@link PlayerExceptions} is thrown.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionChooseStarterError() throws PlayerExceptions {
        controller.setPlayer(playerMock);
        ChooseStarter message = mock(ChooseStarter.class);
        when(message.getAction()).thenReturn(GameAction.CHOOSE_STARTER);
        when(message.isFront()).thenReturn(true);
        doThrow(new PlayerExceptions(CARD_ALREADY_CHOSEN, null)).when(playerMock).chooseStarterCardSide(anyBoolean());

        controller.action(message);

        verify(connectionMock, times(1)).send(any(StarterChooseError.class));
    }

    /**
     * Tests the action handling for choosing an objective card.
     * Verifies that the {@link Player#chooseObjective(int)} method is called with the correct parameter.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionChooseObjective() throws PlayerExceptions {
        controller.setPlayer(playerMock);
        ChooseObjective message = mock(ChooseObjective.class);
        when(message.getAction()).thenReturn(GameAction.CHOOSE_OBJECTIVE);
        when(message.getNumObj()).thenReturn(1);

        controller.action(message);

        verify(playerMock, times(1)).chooseObjective(1);
    }

    /**
     * Tests the action handling for choosing an objective card with an error.
     * Verifies that an appropriate error message is sent when a {@link PlayerExceptions} is thrown.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionChooseObjectiveError() throws PlayerExceptions {
        controller.setPlayer(playerMock);
        ChooseObjective message = mock(ChooseObjective.class);
        when(message.getAction()).thenReturn(GameAction.CHOOSE_OBJECTIVE);
        when(message.getNumObj()).thenReturn(1);
        doThrow(new PlayerExceptions(CARD_ALREADY_CHOSEN, null)).when(playerMock).chooseObjective(anyInt());

        controller.action(message);

        verify(connectionMock, times(1)).send(any(ObjectiveChooseError.class));
    }

    /**
     * Tests the action handling for playing a card.
     * Verifies that the {@link Player#playCard(int, Position, boolean)} method is called with the correct parameters.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionPlayCard() throws PlayerExceptions {
        controller.setPlayer(playerMock);
        PlayCard message = mock(PlayCard.class);
        when(message.getAction()).thenReturn(GameAction.PLAY_CARD);
        when(message.getCardIndex()).thenReturn(1);
        Position position = new Position(1, 1);
        when(message.getPosition()).thenReturn(position);
        when(message.getSide()).thenReturn(true);

        controller.action(message);

        verify(playerMock, times(1)).playCard(1, position, true);
    }

    /**
     * Tests the action handling for playing a card with an error.
     * Verifies that an appropriate error message is sent when a {@link PlayerExceptions} is thrown.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionPlayCardError() throws PlayerExceptions {
        controller.setPlayer(playerMock);
        PlayCard message = mock(PlayCard.class);
        when(message.getAction()).thenReturn(GameAction.PLAY_CARD);
        when(message.getCardIndex()).thenReturn(1);
        Position position = new Position(1,1);
        when(message.getPosition()).thenReturn(position);
        when(message.getSide()).thenReturn(true);
        doThrow(new PlayerExceptions(CARD_ALREADY_PLAYED, null)).when(playerMock).playCard(anyInt(), any(Position.class), anyBoolean());

        controller.action(message);

        verify(connectionMock, times(1)).send(any(PlayCardError.class));
    }

    /**
     * Tests the action handling for drawing gold.
     * Verifies that the {@link Player#drawGold()} method is called.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionDrawGold() throws PlayerExceptions {
        controller.setPlayer(playerMock);

        GameMessage message = mock(GameMessage.class);
        when(message.getAction()).thenReturn(GameAction.DRAW_GOLD);

        controller.action(message);

        verify(playerMock, times(1)).drawGold();
    }

    /**
     * Tests the action handling for drawing gold with an error.
     * Verifies that an appropriate error message is sent when a {@link PlayerExceptions} is thrown.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionDrawGoldError() throws PlayerExceptions {
        controller.setPlayer(playerMock);

        GameMessage message = mock(GameMessage.class);
        when(message.getAction()).thenReturn(GameAction.DRAW_GOLD);
        doThrow(new PlayerExceptions(EMPTY_DECK, null)).when(playerMock).drawGold();

        controller.action(message);

        verify(connectionMock, times(1)).send(any(DrawError.class));
    }

    /**
     * Tests the action handling for drawing a resource.
     * Verifies that the {@link Player#drawResource()} method is called.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionDrawResource() throws PlayerExceptions {
        controller.setPlayer(playerMock);

        GameMessage message = mock(GameMessage.class);
        when(message.getAction()).thenReturn(GameAction.DRAW_RESOURCE);

        controller.action(message);

        verify(playerMock, times(1)).drawResource();
    }

    /**
     * Tests the action handling for drawing a resource with an error.
     * Verifies that an appropriate error message is sent when a {@link PlayerExceptions} is thrown.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionDrawResourceError() throws PlayerExceptions {
        controller.setPlayer(playerMock);

        GameMessage message = mock(GameMessage.class);
        when(message.getAction()).thenReturn(GameAction.DRAW_RESOURCE);
        doThrow(new PlayerExceptions(EMPTY_DECK, null)).when(playerMock).drawResource();

        controller.action(message);

        verify(connectionMock, times(1)).send(any(DrawError.class));
    }

    /**
     * Tests the action handling for drawing from the market.
     * Verifies that the {@link Player#drawMarket(int)} method is called with the correct parameter.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionDrawMarket() throws PlayerExceptions {
        controller.setPlayer(playerMock);
        DrawMarket message = mock(DrawMarket.class);
        when(message.getAction()).thenReturn(GameAction.DRAW_MARKET);
        when(message.getMarketIndex()).thenReturn(2);

        controller.action(message);

        verify(playerMock, times(1)).drawMarket(2);
    }

    /**
     * Tests the action handling for drawing from the market with an error.
     * Verifies that an appropriate error message is sent when a {@link PlayerExceptions} is thrown.
     *
     * @throws PlayerExceptions if an error occurs during the action
     */
    @Test
    public void testActionDrawMarketError() throws PlayerExceptions {
        controller.setPlayer(playerMock);
        DrawMarket message = mock(DrawMarket.class);
        when(message.getAction()).thenReturn(GameAction.DRAW_MARKET);
        when(message.getMarketIndex()).thenReturn(2);
        doThrow(new PlayerExceptions(INVALID_MARKET_CHOICE, null)).when(playerMock).drawMarket(anyInt());

        controller.action(message);

        verify(connectionMock, times(1)).send(any(DrawError.class));
    }

    /**
     * Tests the action handling for an invalid message.
     * Verifies that an {@link InvalidMessage} error message is sent.
     */
    @Test
    public void testActionInvalidMessage() {
        controller.setPlayer(playerMock);

        GameMessage message = mock(GameMessage.class);
        when(message.getAction()).thenReturn(GameAction.INVALID);

        controller.action(message);

        verify(connectionMock, times(1)).send(any(InvalidMessage.class));
    }

    /**
     * Tests handling of a chat message when the controller is disconnected.
     * Verifies that no message is sent to the chat.
     *
     * @throws ChatException if an error occurs during chat message handling
     */
    @Test
    public void testChatMessageWhenDisconnected() throws ChatException {
        // Simulate disconnected state
        controller.setDisconnected(true);

        ChatMessage mockMessage = mock(ChatMessage.class);
        controller.chatMessage(mockMessage);

        // Verify that no message is sent when disconnected
        verify(chatMock, never()).sendMessage(any());
    }

    /**
     * Tests handling of a chat message when the player is null.
     * Verifies that a {@link MatchDoesNotExistError} is sent and no message is forwarded to the chat.
     *
     * @throws ChatException if an error occurs during chat message handling
     */
    @Test
    public void testChatMessageWhenPlayerIsNull() throws ChatException {
        // Simulate player is null
        controller.setPlayer(null);

        ChatMessage mockMessage = mock(ChatMessage.class);
        controller.chatMessage(mockMessage);

        // Verify that MatchDoesNotExistError is sent
        verify(connectionMock).send(any(MatchDoesNotExistError.class));
        verify(chatMock, never()).sendMessage(any());
    }

    /**
     * Tests handling of a chat message when everything is fine.
     * Verifies that the message is sent to the chat with the correct sender name.
     *
     * @throws ChatException if an error occurs during chat message handling
     */
    @Test
    public void testChatMessageWhenEverythingIsFine() throws ChatException {
        // Simulate normal connected state with a valid player
        controller.setDisconnected(false);
        controller.setPlayer(playerMock);
        controller.setChat(chatMock);
        when(connectionMock.getUsername()).thenReturn("testUsername");

        ChatMessage mockMessage = new ChatMessage("ciao", null);
        controller.chatMessage(mockMessage);


        // Verify that sender is set and message is sent
        assertEquals("testUsername", mockMessage.getSender());
        verify(chatMock).sendMessage(mockMessage);
    }

    /**
     * Tests handling of a chat message when a {@link ChatException} is thrown.
     * Verifies that a {@link ChatError} is sent via the connection.
     *
     * @throws ChatException if an error occurs during chat message handling
     */
    @Test
    public void testChatMessageWhenChatExceptionThrown() throws ChatException {
        // Simulate ChatException being thrown
        controller.setDisconnected(false);
        controller.setPlayer(playerMock);
        controller.setChat(chatMock);
        when(connectionMock.getUsername()).thenReturn("testUsername");
        doThrow(new ChatException()).when(chatMock).sendMessage(any(ChatMessage.class));

        ChatMessage mockMessage = mock(ChatMessage.class);
        controller.chatMessage(mockMessage);

        // Verify that connection.send() was called with an instance of ChatError
        ArgumentCaptor<ChatError> captor = ArgumentCaptor.forClass(ChatError.class);
        verify(connectionMock).send(captor.capture());

        // Assert the properties of the captured ChatError instance if needed
        ChatError capturedError = captor.getValue();
        assertNotNull(capturedError); // Ensure a ChatError instance was captured
    }

    /**
     * Tests handling of update messages when the controller is disconnected.
     * Verifies that no message is forwarded to the connection.
     */
    @Test
    public void testUpdateWhenDisconnected() {
        // Simulate disconnected state
        controller.setDisconnected(true);

        // Create a mock ChatMessage
        Message mockMessage = mock(Message.class);

        // Call sendChatMessage with the mock message
        controller.update(mockMessage);

        // Verify that connectionMock.send() is never called when disconnected
        verify(connectionMock, never()).send(mockMessage);
    }

    /**
     * Tests handling of update messages when the controller is connected.
     * Verifies that the message is forwarded to the connection exactly once.
     */
    @Test
    public void testUpdateWhenConnected() {
        // Simulate connected state
        controller.setDisconnected(false);

        // Create a mock ChatMessage
        Message mockMessage = mock(Message.class);

        // Call sendChatMessage with the mock message
        controller.update(mockMessage);

        // Verify that connectionMock.send() is called exactly once with the mock message
        verify(connectionMock, times(1)).send(mockMessage);
    }

    /**
     * Tests the {@link Controller#getUsername()} method.
     * Verifies that the correct username is retrieved from the connection.
     */
    @Test
    public void getUsername() {
        when(connectionMock.getUsername()).thenReturn("testUsername");
        String username = controller.getUsername();
        assertEquals("testUsername", username);
    }

    /**
     * Tests the {@link Controller#setChat(Chat)} method.
     * Verifies that the chat instance is correctly set.
     */
    @Test
    public void setChat() {
        controller.setChat(chatMock);
        assertEquals(chatMock, controller.getChat());
    }

    /**
     * Tests the {@link Controller#getChat()} method.
     * Verifies that the correct chat instance is retrieved.
     */
    @Test
    public void getChat() {
        controller.setChat(chatMock);
        assertEquals(chatMock, controller.getChat());
    }

    /**
     * Tests the {@link Controller#removeChat()} method.
     * Verifies that the chat instance is set to null after removal.
     */
    @Test
    public void removeChat() {
        controller.setChat(chatMock);
        controller.removeChat();
        assertNull(controller.getChat());
    }

    /**
     * Tests handling of sending a chat message when the controller is disconnected.
     * Verifies that no message is forwarded to the connection.
     */
    @Test
    public void testSendChatMessageWhenDisconnected() {
        // Simulate disconnected state
        controller.setDisconnected(true);

        // Create a mock ChatMessage
        ChatMessage mockMessage = mock(ChatMessage.class);

        // Call sendChatMessage with the mock message
        controller.sendChatMessage(mockMessage);

        // Verify that connectionMock.send() is never called when disconnected
        verify(connectionMock, never()).send(mockMessage);
    }

    /**
     * Tests handling of sending a chat message when the controller is connected.
     * Verifies that the message is forwarded to the connection exactly once.
     */
    @Test
    public void testSendChatMessageWhenConnected() {
        // Simulate connected state
        controller.setDisconnected(false);

        // Create a mock ChatMessage
        ChatMessage mockMessage = mock(ChatMessage.class);

        // Call sendChatMessage with the mock message
        controller.sendChatMessage(mockMessage);

        // Verify that connectionMock.send() is called exactly once with the mock message
        verify(connectionMock, times(1)).send(mockMessage);
    }

    /**
     * Tests the {@link Controller#disconnected()} method.
     * Verifies that the disconnected state is correctly set to true.
     */
    @Test
    public void disconnected() {
        controller.disconnected();
        boolean x = controller.isDisconnected();
        assertTrue(x);
    }

    /**
     * Tests the {@link Controller#isDisconnected()} method.
     * Verifies that the disconnected state is correctly retrieved.
     */
    @Test
    public void isDisconnected() {
       controller.setDisconnected(true);
       assertTrue(controller.isDisconnected());
       controller.setDisconnected(false);
       assertFalse(controller.isDisconnected());
    }

    /**
     * Tests the {@link Controller#setDisconnected(boolean)} method.
     * Verifies that the disconnected state is correctly set.
     */
    @Test
    public void setDisconnected() {
        controller.setDisconnected(true);
        assertTrue(controller.isDisconnected());
        controller.setDisconnected(false);
        assertFalse(controller.isDisconnected());
    }

}