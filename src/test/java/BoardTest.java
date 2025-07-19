import com.pluralsight.Domino;
import com.pluralsight.GameBoard;

/**
 * Test the GameBoard functionality
 */
public class BoardTest {

    public static void main(String[] args) {
        System.out.println("=== GAME BOARD TEST ===\n");

        GameBoard board = new GameBoard();

        // Test empty board
        System.out.println("Empty board:");
        System.out.println(board);
        System.out.println("Is empty? " + board.isEmpty());

        // Play first domino
        Domino first = new Domino(3, 5);
        System.out.println("\nPlaying first domino: " + first);
        board.playFirstDomino(first);
        System.out.println(board);

        // Play on right end
        Domino second = new Domino(5, 2);
        System.out.println("\nPlaying " + second + " on right end:");
        boolean played = board.playRight(second);
        System.out.println("Success? " + played);
        System.out.println(board);

        // Play on left end
        Domino third = new Domino(6, 3);
        System.out.println("\nPlaying " + third + " on left end:");
        played = board.playLeft(third);
        System.out.println("Success? " + played);
        System.out.println(board);

        // Try to play invalid domino
        Domino invalid = new Domino(1, 4);
        System.out.println("\nTrying to play invalid domino: " + invalid);
        System.out.println("Can play? " + board.canPlay(invalid));
        played = board.playDomino(invalid);
        System.out.println("Played? " + played);

        // Play valid domino using auto-placement
        Domino valid = new Domino(2, 1);
        System.out.println("\nPlaying valid domino: " + valid);
        played = board.playDomino(valid);
        System.out.println("Success? " + played);
        System.out.println(board);

        // Show detailed display
        System.out.println("\nDetailed board display:");
        System.out.println(board.getDetailedDisplay());

        // Test board properties
        System.out.println("\nBoard properties:");
        System.out.println("Domino count: " + board.getDominoCount());
        System.out.println("Total pips: " + board.getTotalPips());
        System.out.println("Is blocked? " + board.isBlocked());

        System.out.println("\n✅ Board test completed!");
    }
}