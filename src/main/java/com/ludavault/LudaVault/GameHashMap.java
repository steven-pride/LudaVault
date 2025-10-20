package com.ludavault.LudaVault;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/12/2025
 * main.java.com.ludavault.LudaVault.GameHashMap
 * Implements the main.java.com.ludavault.LudaVault.GameDataStore interface using a HashMap<Integer, main.java.com.ludavault.LudaVault.BoardGame> for in memory storage of
 * a board game collection.
 */
public class GameHashMap implements GameDataStore {
    /**
     * Class attributes:
     *     boardGameCollection: HashMap<Integer, main.java.com.ludavault.LudaVault.BoardGame> - the collection of board games.
     */
    private static HashMap<Integer, BoardGame> boardGameCollection;

    /**
     * constructor: main.java.com.ludavault.LudaVault.GameHashMap
     * parameters: none
     * return: main.java.com.ludavault.LudaVault.GameHashMap
     * purpose: Initializes the boardGameCollection as a HashMap<Integer, main.java.com.ludavault.LudaVault.BoardGame>.
     */
    public GameHashMap() {
        boardGameCollection = new HashMap<>();
    }

    /**
     * method: get
     * parameters: int in - the ID of the board game to retrieve
     * return: main.java.com.ludavault.LudaVault.BoardGame
     * purpose: Retrieves a board game by its ID.
     */
    @Override
    public BoardGame get(int in) {
        return boardGameCollection.getOrDefault(in, null);
    }

    /**
     * method: put
     * parameters: int in - the ID of the board game to add
     *             main.java.com.ludavault.LudaVault.BoardGame game - the board game to add
     * return: boolean
     * purpose: Adds a board game to the data store.
     */
    @Override
    public boolean put(int in, BoardGame game)
    {
        try {
            boardGameCollection.put(in, game);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * method: put
     * parameters: main.java.com.ludavault.LudaVault.BoardGame game - the board game to add
     * return: boolean
     * purpose: Adds a board game to the data store.
     */
    @Override
    public boolean put(BoardGame game) {
        try {
            boardGameCollection.put(game.getGameId(), game);
            return true;
        }
        catch (Exception e) {
            return false;
        }
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
     * return: ArrayList<main.java.com.ludavault.LudaVault.BoardGame>
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
