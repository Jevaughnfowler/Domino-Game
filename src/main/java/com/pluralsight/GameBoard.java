package com.pluralsight;

import java.util.*;

/**
 * Represents the game board where dominoes are played
 * Manages the layout and tracks available connection points
 */
public class GameBoard {
    private List<Domino> playedDominoes;  // Dominoes on the board in play order
    private List<Integer> layout;         // The actual pip sequence on the board
    private int leftEnd;                  // Pip value available on left end
    private int rightEnd;                 // Pip value available on right end
    private boolean isEmpty;              // True if no dominoes played yet

    /**
     * Constructor creates an empty game board
     */
    public GameBoard() {
        this.playedDominoes = new ArrayList<>();
        this.layout = new ArrayList<>();
        this.leftEnd = -1;  // -1 indicates no end available
        this.rightEnd = -1;
        this.isEmpty = true;
    }

    /**
     * Check if the board is empty (no dominoes played)
     * @return true if board is empty
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Get the pip value available on the left end
     * @return left end pip value, or -1 if not available
     */
    public int getLeftEnd() {
        return leftEnd;
    }

    /**
     * Get the pip value available on the right end
     * @return right end pip value, or -1 if not available
     */
    public int getRightEnd() {
        return rightEnd;
    }

    /**
     * Get both end values as an array [left, right]
     * @return array with left and right end values
     */
    public int[] getBothEnds() {
        return new int[]{leftEnd, rightEnd};
    }

    /**
     * Check if a domino can be played on the board
     * @param domino domino to check
     * @return true if domino can be legally played
     */
    public boolean canPlay(Domino domino) {
        if (isEmpty()) {
            return true;  // Any domino can start the game
        }

        // Check if domino can connect to either end
        return domino.canConnectTo(leftEnd) || domino.canConnectTo(rightEnd);
    }

    /**
     * Play the first domino on an empty board
     * @param domino the starting domino
     * @throws IllegalStateException if board is not empty
     * @throws IllegalArgumentException if domino is null
     */
    public void playFirstDomino(Domino domino) {
        if (!isEmpty()) {
            throw new IllegalStateException("Cannot play first domino on non-empty board");
        }
        if (domino == null) {
            throw new IllegalArgumentException("Cannot play null domino");
        }

        playedDominoes.add(domino);

        // Set up the layout - for first domino, both values go in sequence
        layout.add(domino.getLeftSide());
        layout.add(domino.getRightSide());

        // Set the available ends
        leftEnd = domino.getLeftSide();
        rightEnd = domino.getRightSide();

        isEmpty = false;
    }

    /**
     * Play a domino on the left end of the board
     * @param domino domino to play
     * @return true if successfully played
     */
    public boolean playLeft(Domino domino) {
        if (isEmpty()) {
            playFirstDomino(domino);
            return true;
        }

        if (!domino.canConnectTo(leftEnd)) {
            return false;  // Cannot connect to left end
        }

        playedDominoes.add(domino);

        // Determine which side of the domino connects to the left end
        if (domino.getLeftSide() == leftEnd) {
            // Domino's left side connects, so right side becomes new left end
            layout.add(0, domino.getRightSide());
            leftEnd = domino.getRightSide();
        } else {
            // Domino's right side connects, so left side becomes new left end
            layout.add(0, domino.getLeftSide());
            leftEnd = domino.getLeftSide();
        }

        return true;
    }

    /**
     * Play a domino on the right end of the board
     * @param domino domino to play
     * @return true if successfully played
     */
    public boolean playRight(Domino domino) {
        if (isEmpty()) {
            playFirstDomino(domino);
            return true;
        }

        if (!domino.canConnectTo(rightEnd)) {
            return false;  // Cannot connect to right end
        }

        playedDominoes.add(domino);

        // Determine which side of the domino connects to the right end
        if (domino.getLeftSide() == rightEnd) {
            // Domino's left side connects, so right side becomes new right end
            layout.add(domino.getRightSide());
            rightEnd = domino.getRightSide();
        } else {
            // Domino's right side connects, so left side becomes new right end
            layout.add(domino.getLeftSide());
            rightEnd = domino.getLeftSide();
        }

        return true;
    }

    /**
     * Attempt to play a domino on either end (automatically chooses)
     * @param domino domino to play
     * @return true if successfully played, false if cannot be played
     */
    public boolean playDomino(Domino domino) {
        if (isEmpty()) {
            playFirstDomino(domino);
            return true;
        }

        // Try left end first, then right end
        if (domino.canConnectTo(leftEnd)) {
            return playLeft(domino);
        } else if (domino.canConnectTo(rightEnd)) {
            return playRight(domino);
        }

        return false;  // Cannot play on either end
    }

    /**
     * Attempt to play a domino on a specific end
     * @param domino domino to play
     * @param onLeftEnd true to play on left end, false for right end
     * @return true if successfully played
     */
    public boolean playDominoOnEnd(Domino domino, boolean onLeftEnd) {
        if (onLeftEnd) {
            return playLeft(domino);
        } else {
            return playRight(domino);
        }
    }

    /**
     * Get a copy of all played dominoes in order
     * @return unmodifiable list of played dominoes
     */
    public List<Domino> getPlayedDominoes() {
        return Collections.unmodifiableList(playedDominoes);
    }

    /**
     * Get the current layout as a sequence of pip values
     * @return unmodifiable list of pip values in layout order
     */
    public List<Integer> getLayout() {
        return Collections.unmodifiableList(layout);
    }

    /**
     * Get the number of dominoes played
     * @return count of dominoes on board
     */
    public int getDominoCount() {
        return playedDominoes.size();
    }

    /**
     * Calculate total pips on the board (for certain scoring rules)
     * @return sum of all pip values on board
     */
    public int getTotalPips() {
        return layout.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Check if the board ends are "blocked" (same value on both ends)
     * This can be important in some domino variants
     * @return true if both ends have the same pip value
     */
    public boolean isBlocked() {
        return !isEmpty() && leftEnd == rightEnd;
    }

    /**
     * Reset the board to empty state (for new game)
     */
    public void reset() {
        playedDominoes.clear();
        layout.clear();
        leftEnd = -1;
        rightEnd = -1;
        isEmpty = true;
    }

    /**
     * Get a visual representation of the board
     * @return formatted string showing the domino layout
     */
    public String getBoardDisplay() {
        if (isEmpty()) {
            return "Empty board - no dominoes played yet";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Board Layout: ");

        // Show the pip sequence
        for (int i = 0; i < layout.size(); i++) {
            sb.append(layout.get(i));
            if (i < layout.size() - 1) {
                sb.append("-");
            }
        }

        sb.append("\nOpen ends: Left=").append(leftEnd)
                .append(", Right=").append(rightEnd);

        return sb.toString();
    }

    /**
     * Get detailed board display showing actual dominoes played
     * @return string showing dominoes in play order
     */
    public String getDetailedDisplay() {
        if (isEmpty()) {
            return "Empty board";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Dominoes played (").append(playedDominoes.size()).append("):\n");

        for (int i = 0; i < playedDominoes.size(); i++) {
            sb.append((i + 1)).append(". ").append(playedDominoes.get(i));
            if (i < playedDominoes.size() - 1) {
                sb.append("\n");
            }
        }

        sb.append("\n\n").append(getBoardDisplay());

        return sb.toString();
    }

    /**
     * String representation of the board
     */
    @Override
    public String toString() {
        return getBoardDisplay();
    }
}