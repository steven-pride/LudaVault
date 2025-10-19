import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameTest {
    BoardGame boardGame;

    @BeforeEach
    void setUp() {
        boardGame = new BoardGame(1, "Azul", 4, 60, 2.2, false);
    }

    @Test
    void calculateTRS() {
        double trs = boardGame.calculateTRS();
        assertEquals(2.2, trs);
    }

    @Test
    void testToString() {
        String expected = "BoardGame [gameId=1, title=Azul, maxPlayers=4, playTime=60, weight=2.2, isExpansion=false]";
        String actual = boardGame.toString();
        assertEquals(expected, actual);
    }
}