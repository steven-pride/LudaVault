import java.util.ArrayList;
import java.util.HashMap;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/12/2025
 * GameHashMap
 * Implements the GameDataStore interface using a HashMap<Integer, BoardGame> for in memory storage of
 * a board game collection.
 */
public class GameHashMap implements GameDataStore {
    /**
     * Class attributes:
     *     boardGameCollection: HashMap<Integer, BoardGame> - the collection of board games.
     */
    private static HashMap<Integer, BoardGame> boardGameCollection;

    /**
     * constructor: GameHashMap
     * parameters: none
     * return: GameHashMap
     * purpose: Initializes the boardGameCollection as a HashMap<Integer, BoardGame>.
     */
    public GameHashMap() {
        boardGameCollection = new HashMap<>();
    }

    /**
     * method: get
     * parameters: int in - the ID of the board game to retrieve
     * return: BoardGame
     * purpose: Retrieves a board game by its ID.
     */
    @Override
    public BoardGame get(int in) {
        return boardGameCollection.getOrDefault(in, null);
    }

    /**
     * method: put
     * parameters: int in - the ID of the board game to add
     *             BoardGame game - the board game to add
     * return: boolean
     * purpose: Adds a board game to the data store.
     */
    @Override
    public boolean put(int in, BoardGame game) {

        return boardGameCollection.put(in, game) != null;
    }

    /**
     * method: put
     * parameters: BoardGame game - the board game to add
     * return: boolean
     * purpose: Adds a board game to the data store.
     */
    @Override
    public boolean put(BoardGame game) {
        return boardGameCollection.put(game.getGameId(), game) != null;
    }

    /**
     * method: remove
     * parameters: int in - the ID of the board game to remove
     * return: boolean
     * purpose: Removes a board game from the data store.
     */
    @Override
    public boolean remove(int in) {

        return boardGameCollection.remove(in) != null;
    }

    /**
     * method: getAll
     * parameters: none
     * return: ArrayList<BoardGame>
     * purpose: Retrieves all board games in the data store.
     */
    @Override
    public ArrayList<BoardGame> getAll() {
        return new ArrayList<BoardGame>(boardGameCollection.values());
    }

    /**
     * method: isEmpty
     * parameters: none
     * return: boolean
     * purpose: Checks if the data store is empty.
     */
    @Override
    public boolean isEmpty() {
        return boardGameCollection.isEmpty();
    }

    /**
     * method: contains
     * parameters: int in - the ID of the board game to check
     * return: boolean
     * purpose: Checks if a board game with the given ID exists in the data store.
     */
    @Override
    public boolean contains(int in) {
        return boardGameCollection.containsKey(in);
    }
}
