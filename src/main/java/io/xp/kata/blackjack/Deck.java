package io.xp.kata.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

public class Deck {
    private static final String[] ALL_CARDS = {
            "A1","B1","C1","D1","A2","B2","C2","D2","A3","B3","C3","D3",
            "A4","B4","C4","D4","A5","B5","C5","D5","A6","B6","C6","D6",
            "A7","B7","C7","D7","A8","B8","C8","D8","A9","B9","C9","D9",
            "AA","BA","CA","DA","AB","BB","CB","DB","AD","BD","CD","DD",
            "AE","BE","CE","DE"};

    private Iterator<String> cardsIterator;

    public Deck() {
        shuffle();
    }

    public String deal() {
        return cardsIterator.next();
    }

    public void shuffle() {
        List<String> cards = new ArrayList<>(asList(ALL_CARDS));
        Collections.shuffle(cards);
        this.cardsIterator = cards.iterator();
    }
}
