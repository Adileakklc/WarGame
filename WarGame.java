import java.util.ArrayList;
import java.util.Collections;

// Knight sýnýfý
class Knight implements Comparable<Knight> {
    private String rank;
    private int value;

    public Knight(String rank, int value) {
        this.rank = rank;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Knight other) {
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return rank + " (" + value + ")";
    }
}

// Player sýnýfý
class Player {
    private ArrayList<Knight> hand = new ArrayList<>();

    public void getKnight(Knight knight) {
        hand.add(knight);
    }

    public Knight playKnight() {
        if (!hand.isEmpty()) {
            return hand.remove(0);
        }
        return null;
    }

    public int knightCount() {
        return hand.size();
    }

    public Knight seeKnight(int index) {
        if (index >= 0 && index < hand.size()) {
            return hand.get(index);
        }
        return null;
    }
}

// Deck sýnýfý
class Deck {
    private ArrayList<Knight> deck = new ArrayList<>();

    public Deck(String[] ranks, int[] values) {
        for (int i = 0; i < ranks.length; i++) {
            deck.add(new Knight(ranks[i], values[i]));
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Knight deal() {
        if (!deck.isEmpty()) {
            return deck.remove(0);
        }
        return null;
    }
}

// WarGame sýnýfý
public class WarGame {

    Player p1, p2;
    Knight p1Knight, p2Knight;
    Deck theDeck;
    ArrayList<Knight> p1Pile, p2Pile; // Savaþ olduðunda þövalyeleri tutmak için liste
    Boolean justWarred; // Oyunda bir savaþ olup olmadýðýný bilmek için boolean kullanýyorum

    public WarGame() {
        justWarred = false;
        p1Pile = new ArrayList<>();
        p2Pile = new ArrayList<>();
        p1 = new Player();
        p2 = new Player();
        String[] ranks = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"};
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        theDeck = new Deck(ranks, values);
        theDeck.shuffle();
        for (int i = 0; i < 10; i++) { // Þövalyeler oyuncularýn eline daðýtýr
            p1.getKnight(theDeck.deal());
            p2.getKnight(theDeck.deal());
        }
        beginGame();
    }

    private void beginGame() {
        String winner = "";
        while (true) {
            pause(1000);
            showKnights();
            if (!justWarred) { // Masada þövalye yok, bu yüzden her oyun bir þövalye koyar
                if (p1.knightCount() > 0) p1Pile.add(p1.playKnight());
                else {
                    winner = "p2"; // 1. oyuncunun baþka þövalyesi yok
                    break;
                }

                if (p2.knightCount() > 0) p2Pile.add(p2.playKnight());
                else {
                    winner = "p1"; // 2. oyuncunun baþka þövalyesi yok
                    break;
                }
            }
            justWarred = false; // Oyuncularýn bir sonraki turda þövalyelerini býrakmak için
            p1Knight = p1Pile.get(p1Pile.size() - 1);
            p2Knight = p2Pile.get(p2Pile.size() - 1);

            if (p1Knight != null && p2Knight != null) {
                if (p1Knight.compareTo(p2Knight) > 0) winner = "p1";
                else if (p1Knight.compareTo(p2Knight) < 0) winner = "p2";
                else winner = "none";
            }

            if (winner.equals("p1")) { // Masadaki tüm þövalyeleri P1'e taþý
                while (p1Pile.size() > 0) p1.getKnight(p1Pile.remove(0));
                while (p2Pile.size() > 0) p1.getKnight(p2Pile.remove(0));
            }

            if (winner.equals("p2")) { // Masadaki tüm þövalyeleri P2'ye taþý
                while (p1Pile.size() > 0) p2.getKnight(p1Pile.remove(0));
                while (p2Pile.size() > 0) p2.getKnight(p2Pile.remove(0));
            }

            if (winner.equals("none")) {
                justWarred = true;
                int count = 0;
                // Her oyuncudan en fazla 4 þövalye ekraný yazdýralým
                while (p1.knightCount() > 0 && count < 4) {
                    p1Pile.add(p1.playKnight());
                    count++;
                }
                count = 0;
                while (p2.knightCount() > 0 && count < 4) {
                    p2Pile.add(p2.playKnight());
                    count++;
                }
            }
        }
        System.out.println("The winner is " + winner + ".");
        System.out.println("P1 has " + p1.knightCount() + " knights. P2 has " + p2.knightCount() + " knights.");
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.print('\u000C'); // Terminali temizler
    }

    public void showKnights() {
        System.out.println("P1 has " + p1.knightCount() + " knights. P2 has " + p2.knightCount() + " knights.");
        if (p1Knight != null) System.out.println("Player 1 knight = " + p1Knight.toString());
        if (p2Knight != null) System.out.println("Player 2 knight = " + p2Knight.toString());
        System.out.print("P1's hand: ");
        for (int i = 0; i < p1.knightCount(); i++) {
            Knight knight = p1.seeKnight(i);
            if (knight != null) {
                System.out.print(knight.getValue() + " ");
            }
        }
        System.out.println();
        System.out.print("P2's hand: ");
        for (int i = 0; i < p2.knightCount(); i++) {
            Knight knight = p2.seeKnight(i);
            if (knight != null) {
                System.out.print(knight.getValue() + " ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new WarGame();
    }
}
