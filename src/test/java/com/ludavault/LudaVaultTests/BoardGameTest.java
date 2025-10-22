package com.ludavault.LudaVaultTests;

import static org.junit.jupiter.api.Assertions.*;
import com.ludavault.LudaVault.BoardGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/22/2025
 * BoardGameTest
 * Defines the BoardGameTest class for testing the BoardGame object.
 */
class BoardGameTest {
    BoardGame boardGame;

    /**
     * method: setUp
     * parameters: none
     * return: void
     * purpose: Initializes the BoardGame object for testing
     */
    @BeforeEach
    void setUp() {
        boardGame = new BoardGame(1, "Azul", 4, 60, 2.2, false);
    }

    /**
     * method: calculateTRS
     * parameters: none
     * return: void
     * purpose: Tests the calculateTRS method of the BoardGame object
     */
    @Test
    void calculateTRS() {
        double trs = boardGame.calculateTRS();
        assertEquals(2.2, trs);
    }

    /**
     * method: testToString
     * parameters: none
     * return: void
     * purpose: Tests the toString method of the BoardGame object
     */
    @Test
    void testToString() {
        String expected = "BoardGame [gameId=1, title=Azul, maxPlayers=4, playTime=60, weight=2.2, isExpansion=false]";
        String actual = boardGame.toString();
        assertEquals(expected, actual);
    }
}