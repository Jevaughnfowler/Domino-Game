package com.pluralsight;

import java.util.*;

/**
 * Represents a player in the domino game
 * Manages the player's hand, score, and available moves
 */
public class Player {
    private String name;              // Player's name
    private List<Domino> hand;        // Dominoes in player's hand
    private int score;                // Current game score
    private int gamesWon;             // Total games won (for tournaments)

    /**
     * Constructor to create a new player
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
        this.gamesWon = 0;
    }

    /**
     * Get the player's name
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player's current score
     * @return current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Add points to player's score
     * @param points points to add (can be negative)
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Reset player's score to 0 (for new game)
     */
    public void resetScore() {
        this.score = 0;
    }

    /**
     * Get number of games won
     * @return games won count
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * Increment games won counter
     */
    public void addGameWin() {
        this.gamesWon++;
    }

    /**
     * Add a domino to the player's hand
     * @param domino domino to add
     */
    public void addDomino(Domino domino) {
        if (domino == null) {
            throw new IllegalArgumentException("Cannot add null domino to hand");
        }
        hand.add(domino);
    }

    /**
     * Add multiple dominoes to the player's hand
     * @param dominoes list of dominoes to add
     */
    public void addDominoes(List<Domino> dominoes) {
        if (dominoes == null) {
            throw new IllegalArgumentException("Cannot add null domino list to hand");
        }
        hand.addAll(dominoes);
    }

    /**
     * Remove a specific domino from the player's hand
     * @param domino domino to remove
     * @return true if domino was found and removed
     */
    public boolean removeDomino(Domino domino) {
        return hand.remove(domino);
    }

    /**
     * Get a copy of the player's hand (for viewing, not modifying)
     * @return unmodifiable list of dominoes in hand
     */
    public List<Domino> getHand() {
        return Collections.unmodifiableList(hand);
    }

    /**
     * Get number of dominoes in hand
     * @return hand size
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Check if player has any dominoes left
     * @return true if hand is empty
     */
    public boolean hasEmptyHand() {
        return hand.isEmpty();
    }

    /**
     * Clear all dominoes from hand (for new game)
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Check if player has a specific domino
     * @param domino domino to look for
     * @return true if player has this domino
     */
    public boolean hasDomino(Domino domino) {
        return hand.contains(domino);
    }

    /**
     * Find all dominoes that can connect to a specific pip value
     * This is crucial for determining valid moves
     * @param pipValue the pip value to connect to
     * @return list of dominoes that can connect
     */
    public List<Domino> getPlayableDominoes(int pipValue) {
        List<Domino> playable = new ArrayList<>();

        for (Domino domino : hand) {
            if (domino.canConnectTo(pipValue)) {
                playable.add(domino);
            }
        }

        return playable;
    }

    /**
     * Find all dominoes that can connect to either of two pip values
     * Used when the board has two open ends
     * @param leftPip pip value on left end of board
     * @param rightPip pip value on right end of board
     * @return list of dominoes that can connect to either end
     */
    public List<Domino> getPlayableDominoes(int leftPip, int rightPip) {
        List<Domino> playable = new ArrayList<>();

        for (Domino domino : hand) {
            if (domino.canConnectTo(leftPip) || domino.canConnectTo(rightPip)) {
                playable.add(domino);
            }
        }

        return playable;
    }

    /**
     * Check if player can make any moves with given pip values
     * @param leftPip left end of board (or -1 if not applicable)
     * @param rightPip right end of board (or -1 if not applicable)
     * @return true if player has at least one playable domino
     */
    public boolean canPlay(int leftPip, int rightPip) {
        if (leftPip == -1 && rightPip == -1) {
            return !hand.isEmpty();  // Can play any domino if board is empty
        }

        for (Domino domino : hand) {
            if ((leftPip != -1 && domino.canConnectTo(leftPip)) ||
                    (rightPip != -1 && domino.canConnectTo(rightPip))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get all doubles in the player's hand
     * @return list of double dominoes
     */
    public List<Domino> getDoubles() {
        List<Domino> doubles = new ArrayList<>();

        for (Domino domino : hand) {
            if (domino.isDouble()) {
                doubles.add(domino);
            }
        }

        return doubles;
    }

    /**
     * Find the highest double in player's hand
     * @return highest double domino, or null if no doubles
     */
    public Domino getHighestDouble() {
        return hand.stream()
                .filter(Domino::isDouble)
                .max((d1, d2) -> Integer.compare(d1.getLeftSide(), d2.getLeftSide()))
                .orElse(null);
    }

    /**
     * Calculate total pip count in hand (for scoring at end of round)
     * @return sum of all pips in hand
     */
    public int getTotalPipsInHand() {
        return hand.stream()
                .mapToInt(Domino::getTotalPips)
                .sum();
    }

    /**
     * Sort the player's hand for better display
     * Sorts by total pips, then by left side value
     */
    public void sortHand() {
        hand.sort((d1, d2) -> {
            int totalCompare = Integer.compare(d1.getTotalPips(), d2.getTotalPips());
            if (totalCompare != 0) {
                return totalCompare;
            }
            return Integer.compare(d1.getLeftSide(), d2.getLeftSide());
        });
    }

    /**
     * Get a formatted string showing the player's hand
     * @return formatted hand display
     */
    public String getHandDisplay() {
        if (hand.isEmpty()) {
            return getName() + "'s hand: (empty)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("'s hand (").append(hand.size()).append("): ");

        for (int i = 0; i < hand.size(); i++) {
            sb.append(hand.get(i));
            if (i < hand.size() - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    /**
     * String representation of the player
     */
    @Override
    public String toString() {
        return String.format("%s (Score: %d, Hand: %d dominoes, Games Won: %d)",
                name, score, hand.size(), gamesWon);
    }

    /**
     * Check if two players are equal (by name)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Player player = (Player) obj;
        return Objects.equals(name, player.name);
    }

    /**
     * Generate hash code based on player name
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}