import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/19/2025
 * GameHashMapTest
 * Defines the GameHashMapTest class for testing the GameHashMap object.
 */
class GameHashMapTest {

    GameHashMap gameHashMap;

    /**
     * method: setUp
     * parameters: none
     * return: void
     * purpose: Initializes the GameHashMap object for testing
     */
    @BeforeEach
    void setUp() {
        gameHashMap = new GameHashMap();
    }

    /**
     * method: get
     * parameters: none
     * return: void
     * purpose: Tests the get method of the GameHashMap object
     */
    @Test
    void get() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertNotNull(gameHashMap.get(1));
        assertEquals("Azul", gameHashMap.get(1).getTitle());
    }

    /**
     * method: put
     * parameters: none
     * return: void
     * purpose: Tests the put method of the GameHashMap object
     */
    @Test
    void put() {
        BoardGame game = new BoardGame(1, "Azul", 4, 60, 2.2, false);
        assertTrue(gameHashMap.put(1, game));
        assertTrue(gameHashMap.contains(1));
        assertNotNull(gameHashMap.get(1));
        assertEquals("Azul", gameHashMap.get(1).getTitle());
    }

    /**
     * method: overridePut
     * parameters: none
     * return: void
     * purpose: Tests the overridden put method of the GameHashMap object
     */
    @Test
    void overridePut() {
        BoardGame game = new BoardGame(1, "Azul", 4, 60, 2.2, false);
        assertTrue(gameHashMap.put(game));
        assertTrue(gameHashMap.contains(1));
        assertNotNull(gameHashMap.get(1));
        assertEquals("Azul", gameHashMap.get(1).getTitle());
    }

    /**
     * method: remove
     * parameters: none
     * return: void
     * purpose: Tests the remove method of the GameHashMap object
     */
    @Test
    void remove() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertTrue(gameHashMap.contains(1));
        assertNotNull(gameHashMap.get(1));
        assertTrue(gameHashMap.remove(1));
        assertFalse(gameHashMap.contains(1));
        assertNull(gameHashMap.get(1));
    }

    /**
     * method: getAll
     * parameters: none
     * return: void
     * purpose: Tests the getAll method of the GameHashMap object
     */
    @Test
    void getAll() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertNotNull(gameHashMap.getAll());
        assertEquals(1, gameHashMap.getAll().size());
    }

    /**
     * method: isEmpty
     * parameters: none
     * return: void
     * purpose: Tests the isEmpty method of the GameHashMap object
     */
    @Test
    void isEmpty() {
        assertTrue(gameHashMap.isEmpty());
    }

    /**
     * method: isNotEmpty
     * parameters: none
     * return: void
     * purpose: Tests when the isEmpty method of the GameHashMap object is not empty (returns false)
     */
    @Test
    void isNotEmpty() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertFalse(gameHashMap.isEmpty());
    }

    /**
     * method: contains
     * parameters: none
     * return: void
     * purpose: Tests the contains method of the GameHashMap object
     */
    @Test
    void contains() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertTrue(gameHashMap.contains(1));
        assertFalse(gameHashMap.contains(2));
    }

    /**
     * method: doesNotContain
     * parameters: none
     * return: void
     * purpose: Tests when the contains method of the GameHashMap object returns false (key does not exist in collection)
     */
    @Test
    void doesNotContain() {
        assertTrue(gameHashMap.isEmpty());
        assertFalse(gameHashMap.contains(1));
    }
}