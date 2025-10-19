import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    GameManager gameManager;
    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
    }

    @Test
    void validateCollectionIsEmpty() {
        assertTrue(gameManager.validateCollectionIsEmpty());
    }

    @Test
    void validateGreaterThanZeroValid() {
        assertTrue(gameManager.validateGreaterThanZero(1));
    }

    @Test
    void validateGreaterThanZeroInvalid() {
        assertFalse(gameManager.validateGreaterThanZero(0));
    }

    @Test
    void validateRangeValid() {
        assertTrue(gameManager.validateRange(1,5,2.2));
    }

    @Test
    void validateRangeInvalid() {
        assertFalse(gameManager.validateRange(1,5,6.2));
    }

    @Test
    void bulkImportNoErrors() {
        String sampleData = "1,Azul,4,60,2.2,false\n2,Gloomhaven,4,240,3.8,false\n3,Twilight Imperium,6,480,4.5,false";
        HashMap<String, String> results = gameManager.bulkImport(sampleData.getBytes());
        assertEquals("3 games imported.", results.get("success"));
        assertNotNull(gameManager.retrieveGame(1));
    }

    @Test
    void bulkImportHasErrors() {
        String sampleData = "4,Feast for Oden,4,160,3.2,false\n5,Catan,4,120,2.8,false\n6,Twilight Struggle,2,120,7.5,false";
        HashMap<String, String> results = gameManager.bulkImport(sampleData.getBytes());
        assertTrue(results.containsKey("error"));
        assertEquals("2 games imported.", results.get("success"));
        assertNull(gameManager.retrieveGame(3));
    }

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
    @Test
    void retrieveGameHasErrors() {
        BoardGame game = gameManager.retrieveGame(10);
        assertNull(game);
    }

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

    @Test
    void deleteGameNoErrors() {
        gameManager.createGame(1,"Azul", 4, 60, 2.2, false);
        boolean isDeleted = gameManager.deleteGame(1);
        assertTrue(isDeleted);
        assertNull(gameManager.retrieveGame(1));
    }

    @Test
    void deleteGameHasErrors() {
        boolean isDeleted = gameManager.deleteGame(10);
        assertFalse(isDeleted);
        assertNull(gameManager.retrieveGame(10));
    }

    @Test
    void deleteGameTestFails() {
        boolean isDeleted = gameManager.deleteGame(10);
        assertTrue(isDeleted);
        assertNull(gameManager.retrieveGame(10));
    }

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

    @Test
    void calculateTRSNoErrors() {
        gameManager.createGame(2,"Gloomhaven", 4, 240, 3.8, false);
        BoardGame game = gameManager.retrieveGame(2);
        assertNotNull(game);
        double trs = gameManager.calculateTRS(game);
        assertEquals(3.9, trs);
    }

    @Test
    void calculateTRSHasErrors() {
        BoardGame game = gameManager.retrieveGame(10);
        assertNull(game);
    }

    @Test
    void validateCollectionIsNotEmpty() {
        String sampleData = "1,Azul,4,60,2.2,false\n2,Gloomhaven,4,240,3.8,false\n3,Twilight Imperium,6,480,4.5,false";
        HashMap<String, String> results = gameManager.bulkImport(sampleData.getBytes());
        assertFalse(gameManager.validateCollectionIsEmpty());
    }

}