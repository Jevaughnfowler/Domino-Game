package com.pluralsight;

import java.util.*;

/**
 * Implements Draw Dominoes game logic
 *
 * DRAW DOMINOES RULES:
 * - 2-4 players each get 7 dominoes (6 if 2 players)
 * - Player with highest double starts, or highest domino if no doubles
 * - Players take turns placing dominoes that match either end
 * - If can't play, draw from boneyard until you can play (or it's empty)
 * - First player to empty their hand wins the round
 * - If game is blocked, player with lowest pip total wins
 * - Winner scores the total pips in all other players' hands
 * - First to reach target score (usually 100) wins the game
 */
public class DrawDominoesGame {
    private final List<Player> players;
    private final DominoSet dominoSet;
    private final GameBoard board;
    private final Scanner scanner;

    private int currentPlayerIndex;
    private boolean gameActive;
    private final int targetScore;
    private final int dominoesPerPlayer;

    /**
     * Create a new Draw Dominoes game
     * @param playerNames names of players (2-4 players)
     * @param targetScore score needed to win (default 100)
     */
    public DrawDominoesGame(List<String> playerNames, int targetScore) {
        if (playerNames.size() < 2 || playerNames.size() > 4) {
            throw new IllegalArgumentException("Draw Dominoes requires 2-4 players");
        }

        this.players = new ArrayList<>();
        for (String name : playerNames) {
            this.players.add(new Player(name));
        }

        this.dominoSet = new DominoSet();
        this.board = new GameBoard();
        this.scanner = new Scanner(System.in);
        this.targetScore = targetScore;
        this.dominoesPerPlayer = (playerNames.size() == 2) ? 7 : 7; // 7 for all player counts in Draw
        this.gameActive = false;
    }

    /**
     * Start a complete game (multiple rounds until someone reaches target score)
     */
    public void playGame() {
        System.out.println("=== DRAW DOMINOES GAME ===");
        System.out.println("Players: " + getPlayerNames());
        System.out.println("Target Score: " + targetScore);
        System.out.println("Dominoes per player: " + dominoesPerPlayer);

        while (!hasWinner()) {
            playRound();
            showScores();

            if (!hasWinner()) {
                System.out.println("\nPress Enter to continue to next round...");
                scanner.nextLine();
            }
        }

        Player winner = getGameWinner();
        System.out.println("\n🎉 GAME OVER! " + winner.getName() + " wins with " + winner.getScore() + " points! 🎉");
    }

    /**
     * Play a single round of Draw Dominoes
     */
    public void playRound() {
        setupRound();

        Player startingPlayer = determineStartingPlayer();
        currentPlayerIndex = players.indexOf(startingPlayer);

        System.out.println("\n=== NEW ROUND ===");
        System.out.println(startingPlayer.getName() + " starts the round!");

        gameActive = true;

        // Starting player must play their starting domino
        playStartingDomino(startingPlayer);
        currentPlayerIndex = getNextPlayerIndex();

        // Continue turns until round ends
        while (gameActive) {
            playTurn(getCurrentPlayer());

            if (getCurrentPlayer().getHandSize() == 0) {
                // Current player won!
                handleRoundWin(getCurrentPlayer());
                break;
            }

            if (isGameBlocked()) {
                handleBlockedGame();
                break;
            }

            currentPlayerIndex = getNextPlayerIndex();
        }

        gameActive = false;
    }

    /**
     * Set up a new round: reset board, deal dominoes
     */
    private void setupRound() {
        // Reset everything
        board.reset();
        dominoSet.reset();
        dominoSet.shuffle();

        // Clear all player hands
        for (Player player : players) {
            player.clearHand();
        }

        // Deal dominoes to each player
        for (Player player : players) {
            player.addDominoes(dominoSet.deal(dominoesPerPlayer));
            player.sortHand();
        }

        System.out.println("\nDominoes dealt! Boneyard has " + dominoSet.size() + " dominoes remaining.");
    }

    /**
     * Determine which player starts (highest double, or highest domino)
     */
    private Player determineStartingPlayer() {
        // First, look for highest double
        Player playerWithHighestDouble = null;
        Domino highestDouble = null;

        for (Player player : players) {
            Domino playerHighest = player.getHighestDouble();
            if (playerHighest != null) {
                if (highestDouble == null || playerHighest.getTotalPips() > highestDouble.getTotalPips()) {
                    highestDouble = playerHighest;
                    playerWithHighestDouble = player;
                }
            }
        }

        if (playerWithHighestDouble != null) {
            System.out.println(playerWithHighestDouble.getName() + " has the highest double: " + highestDouble);
            return playerWithHighestDouble;
        }

        // No doubles found, find player with highest domino
        Player playerWithHighestDomino = players.get(0);
        Domino highestDomino = playerWithHighestDomino.getHand().get(0);

        for (Player player : players) {
            for (Domino domino : player.getHand()) {
                if (domino.getTotalPips() > highestDomino.getTotalPips()) {
                    highestDomino = domino;
                    playerWithHighestDomino = player;
                }
            }
        }

        System.out.println(playerWithHighestDomino.getName() + " has the highest domino: " + highestDomino);
        return playerWithHighestDomino;
    }

    /**
     * Handle the first domino being played
     */
    private void playStartingDomino(Player startingPlayer) {
        Domino startingDomino;

        // Find the domino they should start with
        Domino highestDouble = startingPlayer.getHighestDouble();
        if (highestDouble != null) {
            startingDomino = highestDouble;
        } else {
            // Find their highest domino
            startingDomino = startingPlayer.getHand().get(0);
            for (Domino domino : startingPlayer.getHand()) {
                if (domino.getTotalPips() > startingDomino.getTotalPips()) {
                    startingDomino = domino;
                }
            }
        }

        // Remove from hand and play
        startingPlayer.removeDomino(startingDomino);
        board.playFirstDomino(startingDomino);

        System.out.println(startingPlayer.getName() + " plays starting domino: " + startingDomino);
        showBoardState();
    }

    /**
     * Handle a single player's turn
     */
    private void playTurn(Player player) {
        System.out.println("\n--- " + player.getName() + "'s Turn ---");
        showBoardState();
        System.out.println("Your hand: " + player.getHandDisplay());

        // Check if player can play
        List<Domino> playableDominoes = player.getPlayableDominoes(board.getLeftEnd(), board.getRightEnd());

        if (playableDominoes.isEmpty()) {
            // Must draw from boneyard
            handleDrawing(player);
            return;
        }

        // Player can play - show options and get choice
        Domino chosenDomino = getPlayerChoice(player, playableDominoes);
        playDomino(player, chosenDomino);
    }

    /**
     * Handle player drawing from boneyard when they can't play
     */
    private void handleDrawing(Player player) {
        System.out.println(player.getName() + " cannot play and must draw from boneyard.");

        while (dominoSet.size() > 0) {
            Domino drawnDomino = dominoSet.deal(1).get(0);
            player.addDomino(drawnDomino);

            System.out.println("Drew: " + drawnDomino);

            // Check if drawn domino can be played
            if (board.canPlay(drawnDomino)) {
                System.out.println("The drawn domino can be played!");
                playDomino(player, drawnDomino);
                return;
            }
        }

        System.out.println("Boneyard is empty! " + player.getName() + " must pass.");
    }

    /**
     * Get player's choice of which domino to play
     */
    private Domino getPlayerChoice(Player player, List<Domino> playableDominoes) {
        System.out.println("\nPlayable dominoes:");
        for (int i = 0; i < playableDominoes.size(); i++) {
            Domino domino = playableDominoes.get(i);
            String playInfo = getPlayInfo(domino);
            System.out.println((i + 1) + ". " + domino + " " + playInfo);
        }

        while (true) {
            System.out.print("Choose domino to play (1-" + playableDominoes.size() + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < playableDominoes.size()) {
                    return playableDominoes.get(choice);
                }
            } catch (NumberFormatException e) {
                // Invalid input
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Get information about where a domino can be played
     */
    private String getPlayInfo(Domino domino) {
        boolean canPlayLeft = domino.canConnectTo(board.getLeftEnd());
        boolean canPlayRight = domino.canConnectTo(board.getRightEnd());

        if (canPlayLeft && canPlayRight) {
            return "(can play on either end)";
        } else if (canPlayLeft) {
            return "(can play on left end: " + board.getLeftEnd() + ")";
        } else if (canPlayRight) {
            return "(can play on right end: " + board.getRightEnd() + ")";
        }
        return "";
    }

    /**
     * Play a domino on the board
     */
    private void playDomino(Player player, Domino domino) {
        player.removeDomino(domino);

        // Determine where to play
        boolean canPlayLeft = domino.canConnectTo(board.getLeftEnd());
        boolean canPlayRight = domino.canConnectTo(board.getRightEnd());

        if (canPlayLeft && canPlayRight) {
            // Player can choose which end
            System.out.print("Play on (L)eft or (R)ight? ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("L") || choice.equals("LEFT")) {
                board.playLeft(domino);
            } else {
                board.playRight(domino);
            }
        } else if (canPlayLeft) {
            board.playLeft(domino);
        } else {
            board.playRight(domino);
        }

        System.out.println(player.getName() + " plays: " + domino);
    }

    /**
     * Check if the game is blocked (nobody can play)
     */
    private boolean isGameBlocked() {
        if (dominoSet.size() > 0) {
            return false; // Can still draw
        }

        // Check if any player can play
        for (Player player : players) {
            if (player.canPlay(board.getLeftEnd(), board.getRightEnd())) {
                return false;
            }
        }

        return true; // Game is blocked
    }

    /**
     * Handle when a player wins a round
     */
    private void handleRoundWin(Player winner) {
        System.out.println("\n🎉 " + winner.getName() + " wins the round! 🎉");

        // Calculate points (sum of all other players' hands)
        int points = 0;
        for (Player player : players) {
            if (player != winner) {
                int handTotal = player.getTotalPipsInHand();
                points += handTotal;
                System.out.println(player.getName() + " has " + handTotal + " points in hand");
            }
        }

        winner.addScore(points);
        System.out.println(winner.getName() + " scores " + points + " points!");
    }

    /**
     * Handle when game is blocked
     */
    private void handleBlockedGame() {
        System.out.println("\n🚫 Game is blocked! Nobody can play.");

        // Find player with lowest pip total
        Player winner = players.get(0);
        int lowestTotal = winner.getTotalPipsInHand();

        for (Player player : players) {
            int total = player.getTotalPipsInHand();
            System.out.println(player.getName() + " has " + total + " points in hand");

            if (total < lowestTotal) {
                lowestTotal = total;
                winner = player;
            }
        }

        // Calculate points for winner
        int points = 0;
        for (Player player : players) {
            if (player != winner) {
                points += player.getTotalPipsInHand();
            }
        }
        points -= lowestTotal; // Subtract winner's hand

        winner.addScore(points);
        System.out.println("\n🎉 " + winner.getName() + " wins the blocked round with lowest hand (" + lowestTotal + " points)!");
        System.out.println(winner.getName() + " scores " + points + " points!");
    }

    /**
     * Display current board state
     */
    private void showBoardState() {
        System.out.println("Board: " + board);
        if (board.getPlayedDominoes().size() > 0) {
            System.out.println("Ends: " + board.getLeftEnd() + " <---> " + board.getRightEnd());
        }
    }

    /**
     * Show current scores
     */
    private void showScores() {
        System.out.println("\n=== SCORES ===");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }

    /**
     * Check if anyone has reached the target score
     */
    private boolean hasWinner() {
        return players.stream().anyMatch(p -> p.getScore() >= targetScore);
    }

    /**
     * Get the game winner
     */
    private Player getGameWinner() {
        return players.stream()
                .filter(p -> p.getScore() >= targetScore)
                .max(Comparator.comparingInt(Player::getScore))
                .orElse(null);
    }

    /**
     * Get current player
     */
    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Get next player index (wraps around)
     */
    private int getNextPlayerIndex() {
        return (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Get player names as a string
     */
    private String getPlayerNames() {
        return players.stream()
                .map(Player::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    // Getters for testing
    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }
    public GameBoard getBoard() { return board; }
    public DominoSet getDominoSet() { return dominoSet; }
    public boolean isGameActive() { return gameActive; }
}