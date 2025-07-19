package com.pluralsight;

/**
 * Represents a single domino tile with two sides (left and right)
 * In domino games, tiles are typically represented as [left|right] like [6|4]
 */
public class Domino {
    private final int leftSide;   // Left pip count (0-6 for standard set)
    private final int rightSide;  // Right pip count (0-6 for standard set)

    /**
     * Constructor to create a domino tile
     * @param leftSide number of pips on left side (0-6)
     * @param rightSide number of pips on right side (0-6)
     */
    public Domino(int leftSide, int rightSide) {
        // Validate that pip counts are within standard range
        if (leftSide < 0 || leftSide > 6 || rightSide < 0 || rightSide > 6) {
            throw new IllegalArgumentException("Domino sides must be between 0 and 6");
        }
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    /**
     * Get the left side pip count
     * @return left side pips
     */
    public int getLeftSide() {
        return leftSide;
    }

    /**
     * Get the right side pip count
     * @return right side pips
     */
    public int getRightSide() {
        return rightSide;
    }

    /**
     * Check if this domino is a double (same pips on both sides)
     * @return true if both sides have same pip count
     */
    public boolean isDouble() {
        return leftSide == rightSide;
    }

    /**
     * Get the total pip count for scoring
     * @return sum of both sides
     */
    public int getTotalPips() {
        return leftSide + rightSide;
    }

    /**
     * Check if this domino can connect to a given pip value
     * @param pipValue the pip count to match
     * @return true if either side matches the pip value
     */
    public boolean canConnectTo(int pipValue) {
        return leftSide == pipValue || rightSide == pipValue;
    }

    /**
     * Flip the domino (swap left and right sides)
     * Since dominoes are immutable, this returns a new Domino
     * @return new Domino with sides flipped
     */
    public Domino flip() {
        return new Domino(rightSide, leftSide);
    }

    /**
     * Check if two dominoes are equal
     * Note: [3|5] equals [5|3] because dominoes can be flipped
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Domino domino = (Domino) obj;

        // Check both orientations: [a|b] == [a|b] OR [a|b] == [b|a]
        return (leftSide == domino.leftSide && rightSide == domino.rightSide) ||
                (leftSide == domino.rightSide && rightSide == domino.leftSide);
    }

    /**
     * Generate hash code for the domino
     * Since [3|5] should equal [5|3], we need consistent hashing
     */
    @Override
    public int hashCode() {
        // Use sum and product to ensure [a|b] and [b|a] have same hash
        int sum = leftSide + rightSide;
        int product = leftSide * rightSide;
        return sum * 31 + product;
    }

    /**
     * String representation of the domino
     * @return domino in format [left|right]
     */
    @Override
    public String toString() {
        return "[" + leftSide + "|" + rightSide + "]";
    }
}