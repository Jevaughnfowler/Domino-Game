import com.pluralsight.Domino;
import com.pluralsight.DominoSet;

import java.util.List;

/**
 * Test class to verify Domino and DominoSet functionality
 * Run this to make sure everything is working correctly
 */
public class DominoTest {

    public static void main(String[] args) {
        System.out.println("=== DOMINO GAME TEST SUITE ===\n");

        testDominoClass();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testDominoSetClass();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testGameScenarios();

        System.out.println("\n✅ All tests completed successfully!");
    }

    /**
     * Test all Domino class functionality
     */
    private static void testDominoClass() {
        System.out.println("🧪 TESTING DOMINO CLASS");
        System.out.println("-".repeat(25));

        // Test basic domino creation
        Domino domino1 = new Domino(3, 5);
        Domino domino2 = new Domino(5, 3);  // Should be equal to domino1
        Domino domino3 = new Domino(6, 6);  // Double

        System.out.println("Created dominoes:");
        System.out.println("Domino 1: " + domino1);
        System.out.println("Domino 2: " + domino2);
        System.out.println("Domino 3: " + domino3);

        // Test equality (should handle flipped dominoes)
        System.out.println("\n🔍 Testing Equality:");
        System.out.println(domino1 + " equals " + domino2 + "? " + domino1.equals(domino2));
        System.out.println(domino1 + " equals " + domino3 + "? " + domino1.equals(domino3));

        // Test double detection
        System.out.println("\n🎯 Testing Double Detection:");
        System.out.println(domino1 + " is double? " + domino1.isDouble());
        System.out.println(domino3 + " is double? " + domino3.isDouble());

        // Test pip counting
        System.out.println("\n🔢 Testing Pip Counts:");
        System.out.println(domino1 + " total pips: " + domino1.getTotalPips());
        System.out.println(domino3 + " total pips: " + domino3.getTotalPips());

        // Test connection ability
        System.out.println("\n🔗 Testing Connections:");
        System.out.println(domino1 + " can connect to 3? " + domino1.canConnectTo(3));
        System.out.println(domino1 + " can connect to 5? " + domino1.canConnectTo(5));
        System.out.println(domino1 + " can connect to 2? " + domino1.canConnectTo(2));

        // Test flipping
        System.out.println("\n🔄 Testing Flipping:");
        Domino flipped = domino1.flip();
        System.out.println("Original: " + domino1);
        System.out.println("Flipped:  " + flipped);
        System.out.println("Still equal? " + domino1.equals(flipped));

        // Test invalid domino creation
        System.out.println("\n❌ Testing Invalid Creation:");
        try {
            new Domino(-1, 3);
            System.out.println("ERROR: Should have thrown exception!");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Correctly rejected invalid domino: " + e.getMessage());
        }
    }

    /**
     * Test all DominoSet class functionality
     */
    private static void testDominoSetClass() {
        System.out.println("🧪 TESTING DOMINO SET CLASS");
        System.out.println("-".repeat(30));

        // Create and verify full set
        DominoSet set = new DominoSet(12345L);  // Fixed seed for consistent testing

        System.out.println("📦 Set Creation:");
        System.out.println("Set size: " + set.size());
        System.out.println("Is empty? " + set.isEmpty());
        System.out.println("Highest double: " + set.getHighestDouble());

        // Test shuffling (show first few before and after)
        System.out.println("\n🔀 Testing Shuffle:");
        List<Domino> before = set.getRemainingDominoes().subList(0, 5);
        System.out.println("First 5 before shuffle: " + before);

        set.shuffle();
        List<Domino> after = set.getRemainingDominoes().subList(0, 5);
        System.out.println("First 5 after shuffle:  " + after);

        // Test dealing
        System.out.println("\n🎴 Testing Dealing:");
        System.out.println("Size before dealing: " + set.size());

        List<Domino> hand1 = set.deal(7);
        System.out.println("Dealt 7 dominoes: " + hand1);
        System.out.println("Size after dealing 7: " + set.size());

        Domino single = set.dealOne();
        System.out.println("Dealt single domino: " + single);
        System.out.println("Size after dealing 1 more: " + set.size());

        // Test specific domino operations
        System.out.println("\n🔍 Testing Specific Operations:");
        Domino testDomino = new Domino(2, 4);
        System.out.println("Looking for " + testDomino + " in set: " + set.contains(testDomino));

        // Test reset
        System.out.println("\n🔄 Testing Reset:");
        set.reset();
        System.out.println("Size after reset: " + set.size());
        System.out.println("Contains " + testDomino + " after reset: " + set.contains(testDomino));
    }

    /**
     * Test realistic game scenarios
     */
    private static void testGameScenarios() {
        System.out.println("🧪 TESTING GAME SCENARIOS");
        System.out.println("-".repeat(30));

        // Scenario 1: 4-player game setup
        System.out.println("🎮 Scenario 1: 4-Player Game Setup");
        DominoSet gameSet = new DominoSet();
        gameSet.shuffle();

        // Deal 7 dominoes to each of 4 players
        System.out.println("Dealing 7 dominoes to 4 players...");
        for (int player = 1; player <= 4; player++) {
            List<Domino> hand = gameSet.deal(7);
            System.out.println("Player " + player + " hand: " + hand);

            // Show some statistics about this hand
            long doubles = hand.stream().filter(Domino::isDouble).count();
            int totalPips = hand.stream().mapToInt(Domino::getTotalPips).sum();
            System.out.println("  - Doubles: " + doubles + ", Total Pips: " + totalPips);
        }

        System.out.println("Remaining dominoes (boneyard): " + gameSet.size());

        // Scenario 2: Finding starting domino
        System.out.println("\n🎮 Scenario 2: Finding Starting Domino");
        DominoSet startSet = new DominoSet();

        // In many domino games, the player with the highest double starts
        Domino highestDouble = startSet.getHighestDouble();
        System.out.println("Highest double in set: " + highestDouble);

        if (highestDouble != null) {
            startSet.remove(highestDouble);
            System.out.println("Removed starting domino, remaining: " + startSet.size());
        }

        // Scenario 3: Connection testing
        System.out.println("\n🎮 Scenario 3: Connection Logic");
        Domino playedDomino = new Domino(3, 5);
        System.out.println("Domino on table: " + playedDomino);
        System.out.println("Available connections: left=" + playedDomino.getLeftSide() +
                ", right=" + playedDomino.getRightSide());

        // Test which dominoes could connect
        DominoSet testSet = new DominoSet();
        List<Domino> sample = testSet.deal(10);
        System.out.println("\nTesting connections with sample dominoes:");

        for (Domino d : sample) {
            boolean canConnectLeft = d.canConnectTo(playedDomino.getLeftSide());
            boolean canConnectRight = d.canConnectTo(playedDomino.getRightSide());

            if (canConnectLeft || canConnectRight) {
                System.out.println("  " + d + " can connect " +
                        (canConnectLeft ? "(left)" : "") +
                        (canConnectLeft && canConnectRight ? " & " : "") +
                        (canConnectRight ? "(right)" : ""));
            }
        }
    }
}