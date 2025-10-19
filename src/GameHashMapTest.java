import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameHashMapTest {

    GameHashMap gameHashMap;
    @BeforeEach
    void setUp() {
        gameHashMap = new GameHashMap();
    }

    @Test
    void get() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertNotNull(gameHashMap.get(1));
        assertEquals("Azul", gameHashMap.get(1).getTitle());
    }

    @Test
    void put() {
        BoardGame game = new BoardGame(1, "Azul", 4, 60, 2.2, false);
        assertTrue(gameHashMap.put(1, game));
        assertTrue(gameHashMap.contains(1));
        assertNotNull(gameHashMap.get(1));
        assertEquals("Azul", gameHashMap.get(1).getTitle());
    }

    @Test
    void overridePut() {
        BoardGame game = new BoardGame(1, "Azul", 4, 60, 2.2, false);
        assertTrue(gameHashMap.put(game));
        assertTrue(gameHashMap.contains(1));
        assertNotNull(gameHashMap.get(1));
        assertEquals("Azul", gameHashMap.get(1).getTitle());
    }

    @Test
    void remove() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertTrue(gameHashMap.contains(1));
        assertNotNull(gameHashMap.get(1));
        assertTrue(gameHashMap.remove(1));
        assertFalse(gameHashMap.contains(1));
        assertNull(gameHashMap.get(1));
    }

    @Test
    void getAll() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertNotNull(gameHashMap.getAll());
        assertEquals(1, gameHashMap.getAll().size());
    }

    @Test
    void isEmpty() {
        assertTrue(gameHashMap.isEmpty());
    }

    @Test
    void isNotEmpty() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertFalse(gameHashMap.isEmpty());
    }

    @Test
    void contains() {
        gameHashMap.put(1, new BoardGame(1, "Azul", 4, 60, 2.2, false));
        assertTrue(gameHashMap.contains(1));
        assertFalse(gameHashMap.contains(2));
    }

    @Test
    void doesNotContain() {
        assertTrue(gameHashMap.isEmpty());
        assertFalse(gameHashMap.contains(1));
    }
}