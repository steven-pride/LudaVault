import java.util.ArrayList;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/12/2025
 * Interface: GameDataStore
 * Defines the contract for a data store that manages board games.
 */
public interface GameDataStore {
    /**
     * method: get
     * parameters: int in - the ID of the board game to retrieve
     * return: BoardGame
     * purpose: Retrieves a board game by its ID.
     */
    public BoardGame get(int in);

    /**
     * method: put
     * parameters: int in - the ID of the board game to add
     *             BoardGame game - the board game to add
     * return: boolean
     * purpose: Adds a board game to the data store.
     */
    public boolean put(int in, BoardGame game);

    /**
     * method: put
     * parameters: BoardGame game - the board game to add
     * return: boolean
     * purpose: Adds a board game to the data store.
     */
    public boolean put(BoardGame game);

    /**
     * method: remove
     * parameters: int in - the ID of the board game to remove
     * return: boolean
     * purpose: Removes a board game from the data store.
     */
    public boolean remove(int in);

    /**
     * method: getAll
     * parameters: none
     * return: ArrayList<BoardGame>
     * purpose: Retrieves all board games in the data store.
     */
    public ArrayList<BoardGame> getAll();

    /**
     * method: isEmpty
     * parameters: none
     * return: boolean
     * purpose: Checks if the data store is empty.
     */
    public boolean isEmpty();

    /**
     * method: contains
     * parameters: int in - the ID of the board game to check
     * return: boolean
     * purpose: Checks if a board game with the given ID exists in the data store.
     */
    public boolean contains(int in);
}
