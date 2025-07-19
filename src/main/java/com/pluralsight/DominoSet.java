package com.pluralsight;

import java.util.*;

/**
 * Represents a complete set of dominoes (28 tiles in standard double-six set)
 * Handles creation, shuffling, and dealing of dominoes
 */
public class DominoSet {
    private List<Domino> dominoes;  // All dominoes in the set
    private Random random;          // For shuffling

    /**
     * Constructor creates a full double-six domino set (28 tiles)
     * Standard set includes: [0|0] through [6|6] with no duplicates
     */
    public DominoSet() {
        this.dominoes = new ArrayList<>();
        this.random = new Random();
        generateStandardSet();
    }

    /**
     * Constructor with custom random seed (useful for testing)
     * @param seed random seed for shuffling
     */
    public DominoSet(long seed) {
        this.dominoes = new ArrayList<>();
        this.random = new Random(seed);
        generateStandardSet();
    }

    /**
     * Generate all 28 dominoes in a standard double-six set
     * Each unique combination appears only once: [0|1] but not [1|0]
     */
    private void generateStandardSet() {
        dominoes.clear();

        // Generate all unique combinations
        for (int left = 0; left <= 6; left++) {
            for (int right = left; right <= 6; right++) {  // right >= left to avoid duplicates
                dominoes.add(new Domino(left, right));
            }
        }

        // Verify we have exactly 28 dominoes
        if (dominoes.size() != 28) {
            throw new IllegalStateException("Standard domino set should have 28 tiles, got " + dominoes.size());
        }
    }

    /**
     * Shuffle the domino set randomly
     */
    public void shuffle() {
        Collections.shuffle(dominoes, random);
    }

    /**
     * Deal a specified number of dominoes from the set
     * @param count number of dominoes to deal
     * @return list of dealt dominoes (removed from the set)
     * @throws IllegalArgumentException if trying to deal more dominoes than available
     */
    public List<Domino> deal(int count) {
        if (count > dominoes.size()) {
            throw new IllegalArgumentException("Cannot deal " + count + " dominoes, only " + dominoes.size() + " remaining");
        }

        List<Domino> dealt = new ArrayList<>();

        // Remove dominoes from the end of the list (more efficient than removing from start)
        for (int i = 0; i < count; i++) {
            dealt.add(dominoes.remove(dominoes.size() - 1));
        }

        return dealt;
    }

    /**
     * Deal a single domino
     * @return the dealt domino
     * @throws IllegalStateException if no dominoes left
     */
    public Domino dealOne() {
        if (isEmpty()) {
            throw new IllegalStateException("No dominoes left to deal");
        }
        return dominoes.remove(dominoes.size() - 1);
    }

    /**
     * Check if there are dominoes left in the set
     * @return true if set is empty
     */
    public boolean isEmpty() {
        return dominoes.isEmpty();
    }

    /**
     * Get number of remaining dominoes
     * @return count of dominoes left
     */
    public int size() {
        return dominoes.size();
    }

    /**
     * Get a copy of all remaining dominoes (for viewing, not modifying)
     * @return unmodifiable list of remaining dominoes
     */
    public List<Domino> getRemainingDominoes() {
        return Collections.unmodifiableList(dominoes);
    }

    /**
     * Reset the set to contain all 28 dominoes
     * Useful for starting a new game
     */
    public void reset() {
        generateStandardSet();
    }

    /**
     * Find and return the highest double in the set (for game starting)
     * @return the highest double domino, or null if no doubles exist
     */
    public Domino getHighestDouble() {
        return dominoes.stream()
                .filter(Domino::isDouble)  // Only doubles
                .max((d1, d2) -> Integer.compare(d1.getLeftSide(), d2.getLeftSide()))  // Highest value
                .orElse(null);
    }

    /**
     * Remove a specific domino from the set
     * @param domino the domino to remove
     * @return true if domino was found and removed
     */
    public boolean remove(Domino domino) {
        return dominoes.remove(domino);
    }

    /**
     * Check if the set contains a specific domino
     * @param domino domino to search for
     * @return true if domino exists in set
     */
    public boolean contains(Domino domino) {
        return dominoes.contains(domino);
    }

    /**
     * String representation showing all dominoes in the set
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Empty domino set";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Domino Set (").append(size()).append(" dominoes):\n");

        int count = 0;
        for (Domino domino : dominoes) {
            sb.append(domino.toString()).append(" ");
            count++;
            if (count % 7 == 0) {  // New line every 7 dominoes for readability
                sb.append("\n");
            }
        }

        return sb.toString().trim();
    }
}