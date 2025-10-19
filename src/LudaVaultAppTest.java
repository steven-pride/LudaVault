import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class LudaVaultAppTest {

    @BeforeEach
    void setUp() {
        System.setIn(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    void run() {
        System.setIn(new ByteArrayInputStream("8\n".getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        int exitVal = app.run();
        assertEquals(0, exitVal);
    }

    @Test
    void generateMenu() {
        LudaVaultApp app = new LudaVaultApp();
        String menu = app.generateMenu();
        assertNotNull(menu);
        assertFalse(menu.isEmpty());
        assertTrue(menu.contains("1. Import Board Game File"));
        assertTrue(menu.contains("4. Update Board Game"));
        assertTrue(menu.contains("8. Exit"));
    }

    @Test
    void bulkImportNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        System.setIn(new ByteArrayInputStream(sampleFilePath.getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String result = app.bulkImport();
        assertTrue(result.startsWith("20 games imported."));
        assertTrue(result.contains("Weight must be between 1 and 5"));
    }

    @Test
    void bulkImportHasErrors() {
        System.setIn(new ByteArrayInputStream("C:\\invalid_file.txt\n".getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String result = app.bulkImport();
        assertEquals("File does not exist", result);
    }

    @Test
    void createGameNoErrors() {
        System.setIn(new ByteArrayInputStream("1\nAzul\n4\n45\n2.2\nNo\n".getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String result = app.createGame();
        assertTrue(result.startsWith("Azul added to your collection"));
    }

    @Test
    void createGameHasErrors() {
        System.setIn(new ByteArrayInputStream("1\nAzul\n4\n45\n7.2\n2.2\nNo\n".getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String result = app.createGame();
        assertTrue(result.startsWith("Azul added to your collection"));
    }

    @Test
    void retrieveGameNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n1\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.retrieveGame();
        assertTrue(result.contains("Catan"));
    }

    @Test
    void retrieveGameNotFound() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n30\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.retrieveGame();
        assertEquals("Game not found", result);
    }

    @Test
    void retrieveGameNoGames() {
        LudaVaultApp app = new LudaVaultApp();
        String result = app.retrieveGame();
        assertEquals("", result);
    }

    @Test
    void updateGameNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n1\nSettlers of Catan\n\n\n\n\n1\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String updateResult = app.updateGame();
        assumeTrue(updateResult.equals("Game updated"));
        String result = app.retrieveGame();
        assertTrue(result.contains("Settlers of Catan"));
    }

    @Test
    void updateGameNotFound() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n30\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.updateGame();
        assertEquals("Game not found", result);
    }

    @Test
    void updateGameNoGames() {
        LudaVaultApp app = new LudaVaultApp();
        String result = app.updateGame();
        assertEquals("", result);
    }

    @Test
    void deleteGameNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n1\n1\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String deleteResult = app.deleteGame();
        assertEquals("Game deleted", deleteResult);
        String result = app.retrieveGame();
        assertEquals("Game not found", result);
    }

    @Test
    void deleteGameNotFound() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n30\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.deleteGame();
        assertEquals("Game not found", result);
    }

    @Test
    void deleteGameNoGames() {
        LudaVaultApp app = new LudaVaultApp();
        String result = app.deleteGame();
        assertEquals("", result);
    }

    @Test
    void listGamesNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.listGames();
        assertTrue(result.contains("Catan"));
        assertTrue(result.contains("Gloomhaven"));
        assertTrue(result.contains("Root"));
        assertFalse(result.contains("Clans of Caledonia"));
    }

    @Test
    void listGamesNoGames() {
        LudaVaultApp app = new LudaVaultApp();
        String result = app.listGames();
        assertEquals("No games found", result);
    }

    @Test
    void calculateTRSNoErrors() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n2\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.calculateTRS();
        assertTrue(result.contains("2.18"));
    }

    @Test
    void calculateTRSGameNotFound() {
        String sampleFilePath = new File("datasample.txt").getAbsolutePath();
        String userInputs = "\n30\n";
        System.setIn(new ByteArrayInputStream((sampleFilePath+userInputs).getBytes()));
        LudaVaultApp app = new LudaVaultApp();
        String importResult = app.bulkImport();
        assumeTrue(importResult.startsWith("20 games imported."));
        String result = app.calculateTRS();
        assertEquals("Game not found", result);
    }

    @Test
    void calculateTRSNoGames() {
        LudaVaultApp app = new LudaVaultApp();
        String result = app.calculateTRS();
        assertEquals("", result);
    }
}