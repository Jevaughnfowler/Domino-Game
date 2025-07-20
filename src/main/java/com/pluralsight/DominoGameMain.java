package com.pluralsight;

import java.util.*;

/**
 * Main class to run the domino game
 */
public class DominoGameMain {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🎲 WELCOME TO DOMINO GAMES! 🎲");

        while (true) {
            showMainMenu();
            int choice = getMenuChoice(1, 4);

            switch (choice) {
                case 1:
                    playDrawDominoes();
                    break;
                case 2:
                    System.out.println("Block Dominoes coming soon!");
                    break;
                case 3:
                    System.out.println("Mexican Train coming soon!");
                    break;
                case 4:
                    System.out.println("Thanks for playing! Goodbye!");
                    return;
            }
        }
    }

    /**
     * Show the main menu
     */
    private static void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Play Draw Dominoes");
        System.out.println("2. Play Block Dominoes (Coming Soon)");
        System.out.println("3. Play Mexican Train (Coming Soon)");
        System.out.println("4. Exit");
    }

    /**
     * Play Draw Dominoes game
     */
    private static void playDrawDominoes() {
        System.out.println("\n=== DRAW DOMINOES SETUP ===");

        // Get number of players
        System.out.print("How many players (2-4)? ");
        int numPlayers = getMenuChoice(2, 4);

        // Get player names
        List<String> playerNames = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = "Player " + i;
            }
            playerNames.add(name);
        }

        // Get target score
        System.out.print("Target score to win (default 100): ");
        String scoreInput = scanner.nextLine().trim();
        int targetScore = 100;
        try {
            if (!scoreInput.isEmpty()) {
                targetScore = Integer.parseInt(scoreInput);
                if (targetScore < 50 || targetScore > 500) {
                    System.out.println("Using default score of 100");
                    targetScore = 100;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid score, using default of 100");
        }

        // Create and start the game
        DrawDominoesGame game = new DrawDominoesGame(playerNames, targetScore);
        game.playGame();

        // Ask if they want to play again
        System.out.print("\nPlay another game? (y/n): ");
        String again = scanner.nextLine().trim().toLowerCase();
        if (again.equals("y") || again.equals("yes")) {
            playDrawDominoes();
        }
    }

    /**
     * Get menu choice with validation
     */
    private static int getMenuChoice(int min, int max) {
        while (true) {
            System.out.print("Choose (" + min + "-" + max + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (NumberFormatException e) {
                // Invalid input
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }
}