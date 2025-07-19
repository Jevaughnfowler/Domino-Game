import com.pluralsight.Domino;
import com.pluralsight.DominoSet;
import com.pluralsight.Player;

import java.util.List;

/**
 * Quick test for Player class functionality
 */
public class PlayerTest {

    public static void main(String[] args) {
        System.out.println("=== PLAYER CLASS TEST ===\n");

        // Create players
        Player alice = new Player("Alice");
        Player bob = new Player("Bob");

        System.out.println("Created players:");
        System.out.println(alice);
        System.out.println(bob);

        // Deal dominoes to players
        DominoSet set = new DominoSet();
        set.shuffle();

        alice.addDominoes(set.deal(7));
        bob.addDominoes(set.deal(7));

        System.out.println("\nAfter dealing 7 dominoes each:");
        System.out.println(alice.getHandDisplay());
        System.out.println(bob.getHandDisplay());

        // Test finding playable dominoes
        System.out.println("\nTesting playable dominoes:");
        List<Domino> aliceCanPlay3 = alice.getPlayableDominoes(3);
        System.out.println("Alice can play with pip 3: " + aliceCanPlay3);

        List<Domino> aliceCanPlay5 = alice.getPlayableDominoes(5);
        System.out.println("Alice can play with pip 5: " + aliceCanPlay5);

        // Test if players can play
        System.out.println("\nTesting can play:");
        System.out.println("Alice can play (3, 5)? " + alice.canPlay(3, 5));
        System.out.println("Bob can play (2, 6)? " + bob.canPlay(2, 6));

        // Test doubles
        System.out.println("\nTesting doubles:");
        List<Domino> aliceDoubles = alice.getDoubles();
        System.out.println("Alice's doubles: " + aliceDoubles);

        Domino aliceHighestDouble = alice.getHighestDouble();
        System.out.println("Alice's highest double: " + aliceHighestDouble);

        // Test scoring
        System.out.println("\nTesting scoring:");
        System.out.println("Alice's hand total pips: " + alice.getTotalPipsInHand());

        alice.addScore(25);
        bob.addScore(15);

        System.out.println("After adding scores:");
        System.out.println(alice);
        System.out.println(bob);

        // Test sorting hand
        System.out.println("\nTesting hand sorting:");
        alice.sortHand();
        System.out.println("Alice's sorted hand: " + alice.getHandDisplay());

        System.out.println("\n✅ Player class test completed!");
    }
}