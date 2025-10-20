package com.ludavault.LudaVaultTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.ludavault.LudaVault.LudaVaultAppOld;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/19/2025
 * LudaVaultAppOldTest
 * Defines the LudaVaultAppOldTest class for testing the LudaVaultAppOld object.
 */
class LudaVaultAppOldTest {

    /**
     * method: setUp
     * parameters: none
     * return: void
     * purpose: Clears the input stream to a null value
     */
    @BeforeEach
    void setUp() {
        System.setIn(new ByteArrayInputStream(new byte[0]));
    }

    /**
     * method: run
     * parameters: none
     * return: void
     * purpose: Tests the run method of the LudaVaultAppOld object
     */
    @Test
    void run() {
        System.setIn(new ByteArrayInputStream("8\n".getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        int exitVal = app.run();
        assertEquals(0, exitVal);
    }

    /**
     * method: generateMenu
     * parameters: none
     * return: void
     * purpose: Tests the generateMenu method of the LudaVaultAppOld object
     */
    @Test
    void generateMenu() {
        LudaVaultAppOld app = new LudaVaultAppOld();
        String menu = app.generateMenu();
        assertNotNull(menu);
        assertFalse(menu.isEmpty());
        assertTrue(menu.contains("1. Import Board Game File"));
        assertTrue(menu.contains("4. Update Board Game"));
        assertTrue(menu.contains("8. Exit"));
    }

    /**
     * method: bulkImportNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the bulkImport method of the LudaVaultAppOld object, when no intentional errors occur
     */
    @Test
    void bulkImportNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        System.setIn(new ByteArrayInputStream(sampleFilePath.getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.bulkImport();
        assertTrue(result.startsWith("20 games imported."));
        assertTrue(result.contains("Weight must be between 1 and 5"));
    }

    /**
     * method: bulkImportHasErrors
     * parameters: none
     * return: void
     * purpose: Tests the bulkImport method of the LudaVaultAppOld object, when intentional errors occur
     */
    @Test
    void bulkImportHasErrors() {
        System.setIn(new ByteArrayInputStream("C:\\invalid_file.txt\n".getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.bulkImport();
        assertEquals("File does not exist", result);
    }

    /**
     * method: createGameNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the createGame method of the LudaVaultAppOld object, when no intentional errors occur
     */
    @Test
    void createGameNoErrors() {
        System.setIn(new ByteArrayInputStream("1\nAzul\n4\n45\n2.2\nNo\n".getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.createGame();
        assertTrue(result.startsWith("Azul added to your collection"));
    }

    /**
     * method: createGameHasErrors
     * parameters: none
     * return: void
     * purpose: Tests the createGame method of the LudaVaultAppOld object, when intentional errors occur
     */
    @Test
    void createGameHasErrors() {
        System.setIn(new ByteArrayInputStream("1\nAzul\n4\n45\n7.2\n2.2\nNo\n".getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.createGame();
        assertTrue(result.startsWith("Azul added to your collection"));
    }

    /**
     * method: retrieveGameNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the retrieveGame method of the LudaVaultAppOld object, when no intentional errors occur
     */
    @Test
    void retrieveGameNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n1\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.retrieveGame();
        assertTrue(result.contains("Catan"));
    }

    /**
     * method: retrieveGameNotFound
     * parameters: none
     * return: void
     * purpose: Tests the retrieveGame method of the LudaVaultAppOld object, when the game does not exist
     */
    @Test
    void retrieveGameNotFound() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n30\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.retrieveGame();
        assertEquals("Game not found", result);
    }

    /**
     * method: retrieveGameNoGames
     * parameters: none
     * return: void
     * purpose: Tests the retrieveGame method of the LudaVaultAppOld object, when the collection is empty
     */
    @Test
    void retrieveGameNoGames() {
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.retrieveGame();
        assertEquals("", result);
    }

    /**
     * method: updateGameNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the updateGame method of the LudaVaultAppOld object, when no intentional errors occur
     */
    @Test
    void updateGameNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n1\nSettlers of Catan\n\n\n\n\n1\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String updateResult = app.updateGame();
        assumeTrue(updateResult.equals("Game updated"));
        String result = app.retrieveGame();
        assertTrue(result.contains("Settlers of Catan"));
    }

    /**
     * method: updateGameNotFound
     * parameters: none
     * return: void
     * purpose: Tests the updateGame method of the LudaVaultAppOld object, when the game does not exist
     */
    @Test
    void updateGameNotFound() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n30\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.updateGame();
        assertEquals("Game not found", result);
    }

    /**
     * method: updateGameNoGames
     * parameters: none
     * return: void
     * purpose: Tests the updateGame method of the LudaVaultAppOld object, when the collection is empty
     */
    @Test
    void updateGameNoGames() {
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.updateGame();
        assertEquals("", result);
    }

    /**
     * method: deleteGameNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the deleteGame method of the LudaVaultAppOld object, when no intentional errors occur
     */
    @Test
    void deleteGameNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n1\n1\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String deleteResult = app.deleteGame();
        assertEquals("Game deleted", deleteResult);
        String result = app.retrieveGame();
        assertEquals("Game not found", result);
    }

    /**
     * method: deleteGameNotFound
     * parameters: none
     * return: void
     * purpose: Tests the deleteGame method of the LudaVaultAppOld object, when the game does not exist
     */
    @Test
    void deleteGameNotFound() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n30\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.deleteGame();
        assertEquals("Game not found", result);
    }

    /**
     * method: deleteGameNoGames
     * parameters: none
     * return: void
     * purpose: Tests the deleteGame method of the LudaVaultAppOld object, when the collection is empty
     */
    @Test
    void deleteGameNoGames() {
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.deleteGame();
        assertEquals("", result);
    }

    /**
     * method: listGamesNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the listGames method of the LudaVaultAppOld object, when no intentional errors occur
     */
    @Test
    void listGamesNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.listGames();
        assertTrue(result.contains("Catan"));
        assertTrue(result.contains("Gloomhaven"));
        assertTrue(result.contains("Root"));
        assertFalse(result.contains("Clans of Caledonia"));
    }

    /**
     * method: listGamesNoGames
     * parameters: none
     * return: void
     * purpose: Tests the listGames method of the LudaVaultAppOld object, when the collection is empty
     */
    @Test
    void listGamesNoGames() {
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.listGames();
        assertEquals("No games found", result);
    }

    /**
     * method: calculateTRSNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the calculateTRS method of the LudaVaultAppOld object, when no intentional errors occur
     */
    @Test
    void calculateTRSNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n2\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.calculateTRS();
        assertTrue(result.contains("2.18"));
    }

    /**
     * method: calculateTRSGameNotFound
     * parameters: none
     * return: void
     * purpose: Tests the calculateTRS method of the LudaVaultAppOld object, when the game does not exist
     */
    @Test
    void calculateTRSGameNotFound() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n30\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultAppOld app = new LudaVaultAppOld();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.calculateTRS();
        assertEquals("Game not found", result);
    }

    /**
     * method: calculateTRSNoGames
     * parameters: none
     * return: void
     * purpose: Tests the calculateTRS method of the LudaVaultAppOld object, when the collection is empty
     */
    @Test
    void calculateTRSNoGames() {
        LudaVaultAppOld app = new LudaVaultAppOld();
        String result = app.calculateTRS();
        assertEquals("", result);
    }
}