package io.xp.kata.blackjack;

public class GameResult {
    private PlayerDto host;
    private PlayerDto player;

    public void setPlayer(PlayerDto player) {
        this.player = player;
    }

    public PlayerDto getHost() {
        return host;
    }

    public void setHost(PlayerDto host) {
        this.host = host;
    }

    public PlayerDto getPlayer() {
        return player;
    }

    public GameResult() {
    }

    public GameResult(PlayerDto host, PlayerDto player) {
        this.host = host;
        this.player = player;
    }
}
