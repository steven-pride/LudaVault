package com.ludavault.LudaVaultTests;

import java.util.ArrayList;
import java.util.HashMap;

import com.ludavault.LudaVault.BoardGame;
import com.ludavault.LudaVault.GameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/19/2025
 * GameManagerTest
 * Defines the GameManagerTest class for testing the GameManager object.
 */
class GameManagerTest {

    GameManager gameManager;

    /**
     * method: setUp
     * parameters: none
     * return: void
     * purpose: Initializes the GameManager object for testing
     */
    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
    }

    /**
     * method: validateCollectionIsEmpty
     * parameters: none
     * return: void
     * purpose: Tests when the validateCollectionIsEmpty method of the GameManager object is empty (returns true)
     */
    @Test
    void validateCollectionIsEmpty() {
        assertTrue(gameManager.validateCollectionIsEmpty());
    }

    /**
     * method: validateGreaterThanZero
     * parameters: none
     * return: void
     * purpose: Tests when the validateGreaterThanZero method of the GameManager object is greater than 0 (returns true)
     */
    @Test
    void validateGreaterThanZeroValid() {
        assertTrue(gameManager.validateGreaterThanZero(1));
    }

    /**
     * method: validateGreaterThanZeroInvalid
     * parameters: none
     * return: void
     * purpose: Tests when the validateGreaterThanZero method of the GameManager object is not greater than 0 (returns true)
     */
    @Test
    void validateGreaterThanZeroInvalid() {
        assertFalse(gameManager.validateGreaterThanZero(0));
    }

    /**
     * method: validateRangeValid
     * parameters: none
     * return: void
     * purpose: Tests when the validateRange method of the GameManager object is between the min and max values (returns true)
     */
    @Test
    void validateRangeValid() {
        assertTrue(gameManager.validateRange(1,5,2.2));
    }

    /**
     * method: validateRangeInvalid
     * parameters: none
     * return: void
     * purpose: Tests when the validateRange method of the GameManager object is not between the min and max values (returns true)
     */
    @Test
    void validateRangeInvalid() {
        assertFalse(gameManager.validateRange(1,5,6.2));
    }

    /**
     * method: bulkImportNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the bulkImportNoErrors method of the GameManager object, when no intentional errors occur
     */
    @Test
    void bulkImportNoErrors() {
        String sampleData = "1,Azul,4,60,2.2,false\n2,Gloomhaven,4,240,3.8,false\n3,Twilight Imperium,6,480,4.5,false";
        HashMap<String, String> results = gameManager.bulkImport(sampleData.getBytes());
        assertEquals("3 games imported.", results.get("success"));
        assertNotNull(gameManager.retrieveGame(1));
    }

    /**
     * method: bulkImportHasErrors
     * parameters: none
     * return: void
     * purpose: Tests the bulkImportHasErrors method of the GameManager object, when intentional errors occur
     */
    @Test
    void bulkImportHasErrors() {
        String sampleData = "4,Feast for Oden,4,160,3.2,false\n5,Catan,4,120,2.8,false\n6,Twilight Struggle,2,120,7.5,false";
        HashMap<String, String> results = gameManager.bulkImport(sampleData.getBytes());
        assertTrue(results.containsKey("error"));
        assertEquals("2 games imported.", results.get("success"));
        assertNull(gameManager.retrieveGame(3));
    }

    /**
     * method: createGame
     * parameters: none
     * return: void
     * purpose: Tests the createGame method of the GameManager object
     */
    @Test
    void createGame() {
        boolean isCreated = gameManager.createGame(6,"Sagrada", 4, 90, 2.2, false);
        assertTrue(isCreated);
        assertNotNull(gameManager.retrieveGame(6));
        BoardGame game = gameManager.retrieveGame(6);
        assertEquals("Sagrada", game.getTitle());
        assertEquals(4, game.getMaxPlayers());
        assertEquals(90, game.getPlayTime());
        assertEquals(2.2, game.getWeight());
        assertFalse(game.getIsExpansion());
    }

    /**
     * method: retrieveGameNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the retrieveGame method of the GameManager object, when no intentional errors occur
     */
    @Test
    void retrieveGameNoErrors() {
        gameManager.createGame(1,"Azul", 4, 60, 2.2, false);
        BoardGame game = gameManager.retrieveGame(1);
        assertNotNull(game);
        assertEquals(1, game.getGameId());
        assertEquals("Azul", game.getTitle());
        assertEquals(4, game.getMaxPlayers());
        assertEquals(60, game.getPlayTime());
        assertEquals(2.2, game.getWeight());
        assertFalse(game.getIsExpansion());
    }

    /**
     * method: retrieveGameHasErrors
     * parameters: none
     * return: void
     * purpose: Tests the retrieveGame method of the GameManager object, when the game does not exist
     */
    @Test
    void retrieveGameHasErrors() {
        BoardGame game = gameManager.retrieveGame(10);
        assertNull(game);
    }

    /**
     * method: updateGame
     * parameters: none
     * return: void
     * purpose: Tests the updateGame method of the GameManager object
     */
    @Test
    void updateGame() {
        gameManager.createGame(1,"Azul", 4, 60, 2.2, false);
        boolean isUpdated = gameManager.updateGame(1, "Azul", 4, 45, 2.2, false);
        assertTrue(isUpdated);
        BoardGame game = gameManager.retrieveGame(1);
        assertNotNull(game);
        assertEquals(1, game.getGameId());
        assertEquals("Azul", game.getTitle());
        assertEquals(4, game.getMaxPlayers());
        assertEquals(45, game.getPlayTime());
        assertEquals(2.2, game.getWeight());
        assertFalse(game.getIsExpansion());
    }

    /**
     * method: deleteGameNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the deleteGame method of the GameManager object, when no intentional errors occur
     */
    @Test
    void deleteGameNoErrors() {
        gameManager.createGame(1,"Azul", 4, 60, 2.2, false);
        boolean isDeleted = gameManager.deleteGame(1);
        assertTrue(isDeleted);
        assertNull(gameManager.retrieveGame(1));
    }

    /**
     * method: deleteGameHasErrors
     * parameters: none
     * return: void
     * purpose: Tests the deleteGame method of the GameManager object, when the game does not exist
     */
    @Test
    void deleteGameHasErrors() {
        boolean isDeleted = gameManager.deleteGame(10);
        assertFalse(isDeleted);
        assertNull(gameManager.retrieveGame(10));
    }

    /**
     * method: deleteGameTestFails
     * parameters: none
     * return: void
     * purpose: Tests the deleteGame method of the GameManager object, intentionally errors.
     */
    @Test
    void deleteGameTestFails() {
        boolean isDeleted = gameManager.deleteGame(10);
        assertTrue(isDeleted);
        assertNull(gameManager.retrieveGame(10));
    }

    /**
     * method: listGames
     * parameters: none
     * return: void
     * purpose: Tests the listGames method of the GameManager object, when no intentional errors occur
     */
    @Test
    void listGames() {
        String sampleData = "1,Azul,4,60,2.2,false\n2,Gloomhaven,4,240,3.8,false\n3,Twilight Imperium,6,480,4.5,false";
        HashMap<String, String> results = gameManager.bulkImport(sampleData.getBytes());
        ArrayList<BoardGame> games = gameManager.listGames();
        assertNotNull(games);
        assertFalse(games.isEmpty());
        assertEquals(3, games.size());
        assertNotNull(gameManager.retrieveGame(2));
    }

    /**
     * method: calculateTRSNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the calculateTRS method of the GameManager object, when no intentional errors occur
     */
    @Test
    void calculateTRSNoErrors() {
        gameManager.createGame(2,"Gloomhaven", 4, 240, 3.8, false);
        BoardGame game = gameManager.retrieveGame(2);
        assertNotNull(game);
        double trs = gameManager.calculateTRS(game);
        assertEquals(3.9, trs);
    }

    /**
     * method: calculateTRSHasErrors
     * parameters: none
     * return: void
     * purpose: Tests the calculateTRS method of the GameManager object, when the game does not exist
     */
    @Test
    void calculateTRSHasErrors() {
        BoardGame game = gameManager.retrieveGame(10);
        assumeTrue(game != null);
        double result = gameManager.calculateTRS(game);
        assertTrue(result > 0);
    }

    /**
     * method: validateCollectionIsNotEmpty
     * parameters: none
     * return: void
     * purpose: Tests when the validateCollectionIsEmpty method of the GameManager object is not empty (returns false)
     */
    @Test
    void validateCollectionIsNotEmpty() {
        String sampleData = "1,Azul,4,60,2.2,false\n2,Gloomhaven,4,240,3.8,false\n3,Twilight Imperium,6,480,4.5,false";
        HashMap<String, String> results = gameManager.bulkImport(sampleData.getBytes());
        assertFalse(gameManager.validateCollectionIsEmpty());
    }
}