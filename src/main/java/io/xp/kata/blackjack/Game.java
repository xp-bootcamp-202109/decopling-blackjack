package io.xp.kata.blackjack;

public class Game {

    public static final int HOST_DEAL_THRESHOLD = 17;
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
        while (gameRule.sum(host.getCards()) < HOST_DEAL_THRESHOLD) {
            host.add(deck.deal());
        }
        boolean isHostWin = gameRule.isHostWin(host.getCards(), player.getCards());

        return generateGameResult(isHostWin, !isHostWin);

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
