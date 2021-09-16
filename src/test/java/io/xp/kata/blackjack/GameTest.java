package io.xp.kata.blackjack;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GameTest {
    Game game = new Game();

    @Test
    public void start_game_should_fapai() {
        GameResult gameDto = game.startGame();

        assertEquals(asList("B7"), gameDto.getHost().getCards());
        assertEquals(asList("A7","C7"), gameDto.getPlayer().getCards());
    }

    @Test
    public void close_deal_should_return_game_result() {
        game.startGame();

        GameResult result = game.closeDeal();

        assertEquals(true, result.getHost().isWinner());
        assertEquals(false, result.getPlayer().isWinner());
        assertEquals(asList("B7","BA"), result.getHost().getCards());
        assertEquals(asList("A7","C7"), result.getPlayer().getCards());
    }

    @Test
    public void host_should_deal_until_17() {
        game.startGame();
        GameResult result = game.closeDeal();

        assertEquals(asList("B7","B2","B3","B4","B5"), result.getHost().getCards());
        assertEquals(asList("A7","C7"), result.getPlayer().getCards());
    }

    @Test
    public void host_should_lose_with_bust() {
        game.startGame();
        GameResult result = game.closeDeal();

        assertEquals(asList("B7","B9","BA"), result.getHost().getCards());
        assertTrue(result.getPlayer().isWinner());
    }

    @Test
    public void player_should_lose_with_bust() {
        game.startGame();
        GameResult result = game.deal();

        assertEquals(asList("AA","CA", "B2"), result.getPlayer().getCards());
        assertTrue(result.getHost().isWinner());
    }

}