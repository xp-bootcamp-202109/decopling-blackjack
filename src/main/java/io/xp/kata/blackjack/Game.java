package io.xp.kata.blackjack;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

public class Game {
    private static final String[] ALL_CARDS = {
            "A1","B1","C1","D1","A2","B2","C2","D2","A3","B3","C3","D3",
            "A4","B4","C4","D4","A5","B5","C5","D5","A6","B6","C6","D6",
            "A7","B7","C7","D7","A8","B8","C8","D8","A9","B9","C9","D9",
            "AA","BA","CA","DA","AB","BB","CB","DB","AD","BD","CD","DD",
            "AE","BE","CE","DE"};
    public static final int HOST_DEAL_THRESHOLD = 17;

    private Iterator<String> cardsIterator;
    private GameRule gameRule = new GameRule();
    private Player host;
    private Player player;

    public GameResult startGame() {
        shuffle();
        deliverCards();
        return generateGameResult(false, false);
    }

    private void deliverCards() {
        player = new Player();
        host = new Player();
        player.add(dealCard());
        host.add(dealCard());
        player.add(dealCard());
    }

    private String dealCard() {
        return cardsIterator.next();
    }

    private void shuffle() {
        List<String> cards = new ArrayList<>(asList(ALL_CARDS));
        Collections.shuffle(cards);
        this.cardsIterator = cards.iterator();
    }

    public GameResult closeDeal() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5740/blackjack");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from configuration where key = 'game_rule'");
            rs.next();
            int hostMinimumPoint = Integer.parseInt(rs.getString("host_deal_limit"));
            while (gameRule.sum(host.getCards()) < hostMinimumPoint) {
                host.add(dealCard());
            }
            boolean isHostWin = gameRule.isHostWin(host.getCards(), player.getCards());
            return generateGameResult(isHostWin, !isHostWin);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameResult deal() {
        player.add(dealCard());
        boolean playerLose = gameRule.isBust(player.getCards());

        return generateGameResult(playerLose, false);
    }

    private GameResult generateGameResult(final boolean isHostWin, final boolean isPlayerWin) {
        PlayerDto hostDto = new PlayerDto();
        hostDto.setCards(host.getCards());
        hostDto.setWinner(isHostWin);

        PlayerDto playerDto = new PlayerDto();
        playerDto.setCards(player.getCards());
        playerDto.setWinner(isPlayerWin);

        GameResult gameResult = new GameResult();
        gameResult.setHost(hostDto);
        gameResult.setPlayer(playerDto);
        return gameResult;
    }
}
