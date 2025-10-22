package com.ludavault.LudaVault;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/22/2025
 * GameManager
 * Provides the core business logic for LudaVault (Business Logic Layer).
 * Provides methods for importing board game data from a file, creating new board games,
 * retrieving board games, updating board games, deleting board games, listing all board games,
 * and calculating the Table Resistance Score (TRS).
 */
public class GameManager {
    /**
     * Class attributes:
     *     gameDataStore: GameDataStore - an instance of the GameDataStore interface to manage the board game collection
     */
    private GameDataStore gameDataStore;

    /**
     * constructor: GameManager
     * parameters: none
     * return: GameManager
     * purpose: Initializes the gameDataStore with a new GameHashMap instance.
     */
    public GameManager()
    {
        gameDataStore = new GameHashMap();
    }

    /**
     * method: bulkImport
     * parameters: byte[] fileBytes
     * return: HashMap<String, String>
     * purpose: Imports board game data from a byte array, splitting each line into an array of strings,
     *          creating a new BoardGame object, and putting the new BoardGame object into the gameDataStore.
     *          Returns a HashMap with success count and error messages.
     */
    public HashMap<String, String> bulkImport(byte[] fileBytes)
    {
        HashMap<String, String> results = new HashMap<>();
        StringBuilder errorMessage = new StringBuilder();
        int SuccessCount = 0;

        //Create a BufferedReader to read the file bytes passed in
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileBytes))))
        {
            String line;

            //iterate over each line in fileBytes
            while ((line = bufferedReader.readLine()) != null) {
                try
                {
                    //split each line into an array of strings
                    String[] gameData = line.split(",");

                    //Validate the gameData
                    if(gameData.length != 6) {
                        throw new IllegalArgumentException("Data was not in the correct format. Expected 6 comma separated values.");
                    }
                    if (!validateGreaterThanZero(Integer.parseInt(gameData[0]))) {
                        throw new IllegalArgumentException("Game ID must be greater than 0.");
                    }
                    if(gameDataStore.contains(Integer.parseInt(gameData[0])))
                    {
                        throw new IllegalArgumentException("Game ID: " + Integer.parseInt(gameData[0]) + " already exists.");
                    }
                    if (String.valueOf(gameData[1]).isBlank()) {
                        throw new IllegalArgumentException("Title cannot be empty or blank.");
                    }
                    if (!validateGreaterThanZero(Integer.parseInt(gameData[2]))) {
                        throw new IllegalArgumentException("Maximum number of players must be greater than 0.");
                    }
                    if (!validateGreaterThanZero(Integer.parseInt(gameData[3]))) {
                        throw new IllegalArgumentException("Play time must be greater than 0.");
                    }
                    if (!validateRange(1, 5, Double.parseDouble(gameData[4]))) {
                        throw new IllegalArgumentException("Weight must be between 1 and 5.");
                    }
                    if (!(gameData[5]).equalsIgnoreCase("true") && !(gameData[5]).equalsIgnoreCase("false")) {
                        throw new IllegalArgumentException("Expansion must be true or false.");
                    }

                    //create a new BoardGame object
                    BoardGame game = new BoardGame(Integer.parseInt(gameData[0]), gameData[1], Integer.parseInt(gameData[2]), Integer.parseInt(gameData[3]), Double.parseDouble(gameData[4]), Boolean.parseBoolean(gameData[5]));

                    //put the new BoardGame object into the gameDataStore
                    gameDataStore.put(game);
                    SuccessCount++;
                } catch (Exception e) {
                    //capture any errors for a line
                    errorMessage.append("Invalid game data: ").append(line).append(" | ").append(e.getMessage()).append("\n");
                }
            }
        } catch (IOException e) {
            //capture any errors reading the file bytes
            errorMessage.append("Error reading file: ").append(e.getMessage()).append("\n");
        }

        //return successes and errors
        results.put("error", errorMessage.toString());
        results.put("success", SuccessCount + " games imported.");
        return results;

    }

    /**
     * method: createGame
     * parameters: int gameId - the ID of the new board game
     *             String title - the title of the new board game
     *             int maxPlayers - the maximum number of players for the new board game
     *             int playTime - the playtime for the new board game
     *             double weight - the weight of the new board game
     *             boolean isExpansion - whether the new board game is an expansion
     * return: boolean
     * purpose: Creates a new BoardGame object and puts it into the gameDataStore.
     *          Returns true if successful, false otherwise.
     */
    public boolean createGame(int gameId, String title, int maxPlayers, int playTime, double weight, boolean isExpansion)
    {
        BoardGame game = new BoardGame(gameId, title, maxPlayers, playTime, weight, isExpansion);
        return gameDataStore.put(game);
    }

    /**
     * method: retrieveGame
     * parameters: int gameId - the ID of the board game to retrieve
     * return: BoardGame
     * purpose: Retrieves a BoardGame object from the gameDataStore based on the provided gameId.
     *          Returns the BoardGame object if found, null otherwise.
     */
    public BoardGame retrieveGame(int gameId)
    {
        if(gameDataStore.contains(gameId))
        {
            return gameDataStore.get(gameId);
        }
        else
        {
            return null;
        }
    }

    /**
     * method: updateGame
     * parameters: int gameId - the ID of the board game to update
     *             String title - the title of the board game
     *             int maxPlayers - the maximum number of players for the board game
     *             int playTime - the playtime for the board game
     *             double weight - the weight of the board game
     *             boolean isExpansion - whether the board game is an expansion
     * return: boolean
     * purpose: Updates an existing BoardGame object in the gameDataStore based on the provided gameId.
     *          Returns true if successful, false otherwise.
     */
    public boolean updateGame(int gameId, String title, int maxPlayers, int playTime, double weight, boolean isExpansion)
    {
        if(gameDataStore.contains(gameId))
        {
            BoardGame game = gameDataStore.get(gameId);
            game.setTitle(title);
            game.setMaxPlayers(maxPlayers);
            game.setPlayTime(playTime);
            game.setWeight(weight);
            game.setIsExpansion(isExpansion);
            return gameDataStore.put(gameId, game);
        }
        else
        {
            return false;
        }
    }

    /**
     * method: deleteGame
     * parameters: int gameId - the ID of the board game to delete
     * return: boolean
     * purpose: Deletes a BoardGame object from the gameDataStore based on the provided gameId.
     *          Returns true if successful, false otherwise.
     */
    public boolean deleteGame(int gameId)
    {
        if(gameDataStore.contains(gameId))
        {
            return gameDataStore.remove(gameId);
        }
        else
        {
            return false;
        }
    }

    /**
     * method: listGames
     * return: ArrayList<BoardGame>
     * purpose: Retrieves all BoardGame objects from the gameDataStore.
     *          Returns an ArrayList containing all BoardGame objects.
     */
    public ArrayList<BoardGame> listGames()
    {
        return gameDataStore.getAll();
    }

    /**
     * method: calculateTRS
     * parameters: BoardGame game - the board game to calculate TRS for
     * return: double
     * purpose: Calculates the TRS (Total Rating Score) for a given BoardGame object.
     *          Returns the calculated TRS value.
     */
    public double calculateTRS(BoardGame game)
    {
        if(game != null)
        {
            return game.calculateTRS();
        }
        else
        {
            return 0;
        }
    }

    /**
     * method: validateGreaterThanZero
     * parameters: int value - the value to validate
     * return: boolean
     * purpose: Validates if a given integer value is greater than zero.
     *          Returns true if the value is greater than zero, false otherwise.
     */
    public boolean validateGreaterThanZero(int value)
    {
        return value > 0;
    }

    /**
     * method: validateRange
     * parameters: double min - the minimum value
     *             double max - the maximum value
     *             double value - the value to validate
     * return: boolean
     * purpose: Validates if a given double value is within a specified range.
     *          Returns true if the value is within the range, false otherwise.
     */
    public boolean validateRange(double min, double max, double value)
    {
        return value >= min && value <= max;
    }

    /**
     * method: validateCollectionIsEmpty
     * return: boolean
     * purpose: Validates if the gameDataStore collection is empty.
     *          Returns true if the collection is empty, false otherwise.
     */
    public boolean validateCollectionIsEmpty()
    {
        return gameDataStore.isEmpty();
    }

    /**
     * method: getMaxId
     * return: int
     * purpose: Retrieves the maximum game ID from the gameDataStore collection.
     *          Returns 0 if the collection is empty, or the maximum game ID otherwise.
     */
    public int getMaxId() {
        if (validateCollectionIsEmpty()) {
            return 0;
        }
        else
        {
            BoardGame[] gamesArray = gameDataStore.getAll().toArray(new BoardGame[0]);
            return gamesArray[gamesArray.length - 1].getGameId();
        }
    }
}
