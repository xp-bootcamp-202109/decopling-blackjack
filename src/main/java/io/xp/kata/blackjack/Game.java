package io.xp.kata.blackjack;

import java.sql.*;

public class Game {

    private Deck deck = new Deck();
    private GameRule gameRule = new GameRule();

    private Player host;
    private Player player;

    public GameResult startGame() {
        return deliverCards();
    }

    private GameResult deliverCards() {
        player = new Player();
        host = new Player();
        player.add(deck.deal());
        host.add(deck.deal());
        player.add(deck.deal());
        return generateGameResult(false, false);
    }

    public GameResult closeDeal() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5740/blackjack");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from configuration where key = 'game_rule'");
            rs.next();
            int hostMinimumPoint = Integer.parseInt(rs.getString("host_deal_limit"));
            while (gameRule.sum(host.getCards()) < hostMinimumPoint) {
                host.add(deck.deal());
            }
            boolean isHostWin = gameRule.isHostWin(host.getCards(), player.getCards());
            return generateGameResult(isHostWin, !isHostWin);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameResult deal() {
        player.add(deck.deal());
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
