import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/12/2025
 * LudaVaultApp
 * Console application for the LudaVault Data Management System to allow collectors to manage their board games.
 * Users can import games from a text file, create games manually, display, search, update, delete, and compute
 * a custom TRS (Table Resistance Score). This class performs input validation to avoid crashes. Subclasses will
 * be called, with results displayed to the user by this class.
 */
public class LudaVaultApp {
    /**
     * Class attributes:
     *     gameManager: GameManager - an instance of the GameManager class to manage game data
     *     userInput: Scanner - an instance of the Scanner class to capture user inputs
     */
    private GameManager gameManager;
    private Scanner userInput;

    /**
     * constructor: LudaVaultApp
     * parameters: none
     * return: LudaVaultApp
     * purpose: Initializes the GameManager and Scanner
     */
    public LudaVaultApp()
    {
        gameManager = new GameManager();
        userInput = new Scanner(System.in);
    }

    /**
     * method: main
     * parameters: args (String[])
     * return: void
     * purpose: Standard Java entry point – launches the LudaVault app, and handles exit.
     */
    public static void main(String[] args)
    {
        //Launch new instance of LudaVault
        LudaVaultApp app = new LudaVaultApp();
        //Run LudaVault until an exit code is returned
        int exitCode = app.run();
        System.exit(exitCode);
    }

    /**
     * method: run
     * parameters: none
     * return: int
     * purpose: Runs the LudaVault application, handling user input and menu options.
     */
    public int run()
    {
        System.out.println("Welcome to the LudaVault");
        System.out.println("Let's level up your game night!");
        //Loop until the user selects the exit option
        while(true) {
            System.out.println(generateMenu());

            String response = "";

            int menuOption = 0;
            try {
                menuOption = Integer.parseInt(userInput.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid option selected. Please enter 1-8");
                continue;
            }

            //Execute the selected menu option
            switch(menuOption) {
                case 1:
                    response = bulkImport();
                    break;
                case 2:
                    response = createGame();
                    break;
                case 3:
                    response = retrieveGame();
                    break;
                case 4:
                    response = updateGame();
                    break;
                case 5:
                    response = deleteGame();
                    break;
                case 6:
                    response = listGames();
                    break;
                case 7:
                    response = calculateTRS();
                    break;
                case 8:
                    return 0;
                default:
                    response = "Invalid option selected. Please enter 1-8";
                    break;
            }

            //Display the results of the chosen menu action to the user
            System.out.println(response);
            System.out.println("----------------------------------------------------");
            System.out.println();
        }
    }

    /**
     * method: generateMenu
     * parameters: none
     * return: String
     * purpose: Generates a formatted menu string.
     */
    public String generateMenu()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Please select an option:\n");
        builder.append("1. Import Board Game File\n");
        builder.append("2. Create New Board Game\n");
        builder.append("3. Retrieve Board Game\n");
        builder.append("4. Update Board Game\n");
        builder.append("5. Remove Board Game\n");
        builder.append("6. List All Board Games\n");
        builder.append("7. Calculate Table Resistance Score\n");
        builder.append("8. Exit\n");
        return builder.toString();
    }

    /**
     * method: bulkImport
     * parameters: none
     * return: String
     * purpose: Handles bulk import of board games from a file.
     */
    public String bulkImport()
    {
        //Get file path from user
        System.out.println("Please enter the file path or exit to return to the main menu:");
        String filePath = userInput.nextLine();
        if(filePath.equalsIgnoreCase("exit"))
            return "";

        //Check if the file exists and is readable
        File file = new File(filePath);
        if (!file.exists())
            return "File does not exist";
        else if (!file.canRead())
            return "File is not readable";
        else if (file.isDirectory())
            return "File is a directory";
        else if (file.length() == 0)
            return "File is empty";

        byte[] fileBytes = null;

        //Read file and story in byte[]
        try {
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            return "Error reading file";
        }

        //Excute the gameManager bulkImport method and return the results in a formatted string
        HashMap<String, String> results = gameManager.bulkImport(fileBytes);
        return results.get("success") + "\n" + results.get("error");
    }

    /**
     * method: createGame
     * parameters: none
     * return: String
     * purpose: Handles creation of a new board game.
     */
    public String createGame()
    {
        //Get the game ID from the user
        int gameId = getGameId(true);
        if(gameId == -1)
            return "";


        String title = "";
        while(title.isBlank()) {
            System.out.println("Please enter the title of the game:");
            title = userInput.nextLine();
            if(title.isBlank()) {
                System.out.println("Title cannot be empty or blank");
                continue;
            }
        }

        //Request the maximum number of players until the user enters a valid number greater than zero
        int maxPlayers = 0;
        while(!gameManager.validateGreaterThanZero(maxPlayers)) {
            System.out.println("Please enter the maximum number of players:");
            try {
                maxPlayers = Integer.parseInt(userInput.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number");
                continue;
            }
            if(!gameManager.validateGreaterThanZero(maxPlayers)) {
                System.out.println("Maximum number of players must be greater than 0");
            }
        }

        //Request the playtime until the user enters a valid number greater than zero
        int playTime = 0;
        while(!gameManager.validateGreaterThanZero(playTime)) {
            System.out.println("Please enter the play time in minutes:");
            try {
                playTime = Integer.parseInt(userInput.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number");
                continue;
            }
            if(!gameManager.validateGreaterThanZero(playTime)) {
                System.out.println("Play time must be greater than 0");
            }
        }

        //Request the weight until the user enters a valid number between 1 and 5
        double weight = 0;
        while(!gameManager.validateRange(1, 5, weight)) {
            System.out.println("Please enter the weight of the game:");
            try {
                weight = Double.parseDouble(userInput.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number");
                continue;
            }
            if(!gameManager.validateRange(1, 5, weight)) {
                System.out.println("Weight must be between 0 and 5");
            }
        }

        //Request if the game is an expansion until the user enters 'yes' or 'no'
        boolean isExpansion = false;
        String expansion = "";
        while (expansion.isBlank()) {
            System.out.println("Is this game an expansion (yes/no)?");
            String response = userInput.nextLine();
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                isExpansion = true;
                expansion = "Yes";
            } else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
                expansion = "No";
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'");
                continue;
            }
        }

        //Execute the gameManager createGame method
        boolean createResponse = gameManager.createGame(gameId, title, playTime, maxPlayers, weight, isExpansion);
        if(createResponse)
            return title + " added to your collection";
        else
            return "Error creating game";
    }

    /**
     * method: retrieveGame
     * parameters: none
     * return: String
     * purpose: Handles retrieval of a board game by ID.
     */
    public String retrieveGame()
    {
        //Get the game ID from the user
        int gameId = getGameId(false);
        if(gameId == -1)
            return "";

        BoardGame retrieveResponse = gameManager.retrieveGame(gameId);
        if(retrieveResponse == null)
            return "Game not found";
        return retrieveResponse.toString();
    }

    /**
     * method: updateGame
     * parameters: none
     * return: String
     * purpose: Handles updating of a board game by ID.
     */
    public String updateGame()
    {
        //Get the game ID from the user
        int gameId = getGameId(false);
        if(gameId == -1)
            return "";

        //Retrieve the game from the collection
        BoardGame game = gameManager.retrieveGame(gameId);
        if(game == null)
            return "Game not found";

        //Request the title if blank uses the current tile
        System.out.println("Please enter the title of the game (Leave blank to not update):");
        String title = userInput.nextLine();
        if(!title.isBlank()) {
            game.setTitle(title);
        }
        else {
            title = game.getTitle();
        }

        //Request the maximum number of players until the user enters a valid number greater than zero
        //Uses the current value if blank
        int maxPlayers = 0;
        while(!gameManager.validateGreaterThanZero(maxPlayers)) {
            System.out.println("Please enter the maximum number of players (Leave blank to not update):");
            try {
                String sMaxPlayers = userInput.nextLine();
                if(sMaxPlayers.isBlank()) {
                    maxPlayers = game.getMaxPlayers();
                }
                else {
                    maxPlayers = Integer.parseInt(sMaxPlayers);
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number");
                continue;
            }

            if(!gameManager.validateGreaterThanZero(maxPlayers)) {
                System.out.println("Maximum number of players must be greater than 0");
            }
        }

        //Request the playtime until the user enters a valid number greater than zero
        //Uses the current value if blank
        int playTime = 0;
        while(!gameManager.validateGreaterThanZero(playTime)) {
            System.out.println("Please enter the play time in minutes (Leave blank to not update):");
            try {
                String sPlayTime = userInput.nextLine();
                if(sPlayTime.isBlank()) {
                    playTime = game.getPlayTime();
                }
                else {
                    playTime = Integer.parseInt(sPlayTime);
                }

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number");
            }

            if(!gameManager.validateGreaterThanZero(playTime)) {
                System.out.println("Play time must be greater than 0");
            }
        }

        //Request the weight until the user enters a valid number between 1 and 5
        //Uses the current value if blank
        double weight = 0;
        while(!gameManager.validateRange(1, 5, weight)) {
            System.out.println("Please enter the weight of the game (Leave blank to not update):");
            try {
                String sWeight = userInput.nextLine();
                if(sWeight.isBlank()) {
                    weight = game.getWeight();
                }
                else {
                    weight = Double.parseDouble(sWeight);
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number");
            }

            if(!gameManager.validateRange(1, 5, weight)) {
                System.out.println("Weight must be between 0 and 5");
            }
        }

        //Request if the game is an expansion until the user enters 'yes' or 'no'
        //Uses the current value if blank
        boolean isExpansion = false;
        String expansion = "";
        while (expansion.isBlank()) {
            System.out.println("Is this game an expansion (yes/no)? (Leave blank to not update)");
            String response = userInput.nextLine();
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                isExpansion = true;
                expansion = "Yes";
            } else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
                isExpansion = false;
                expansion = "No";
            } else if (response.isBlank()){
                isExpansion = game.getIsExpansion();
                expansion = "Unchanged";
            }
            else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'");
            }
        }

        //Execute the gameManager updateGame method
        boolean updateResponse = gameManager.updateGame(gameId, title, maxPlayers, playTime, weight, isExpansion);
        if(updateResponse) {
            return "Game updated";
        }
        else {
            return "Error updating game";
        }
    }

    /**
     * method: deleteGame
     * parameters: none
     * return: String
     * purpose: Handles deletion of a board game by ID.
     */
    public String deleteGame()
    {
        //Get the game ID from the user
        int gameId = getGameId(false);
        if(gameId == -1)
            return "";

        if (gameManager.deleteGame(gameId))
            return "Game deleted";
        else
            return "Game not found";
    }

    /**
     * method: listGames
     * parameters: none
     * return: String
     * purpose: Lists all board games in the system.
     */
    public String listGames()
    {
        //Execute the gameManager listGames method
        ArrayList<BoardGame> games = gameManager.listGames();
        if(games.isEmpty())
            return "No games found";

        //Format the results and return them to the user
        StringBuilder gamesString = new StringBuilder();
        for(BoardGame game : games) {
            gamesString.append(game.toString());
            gamesString.append("\n");
        }
        return gamesString.toString();
    }

    /**
     * method: calculateTRS
     * parameters: none
     * return: String
     * purpose: Displays the Table Resistance Score for a board game.
     */
    public String calculateTRS()
    {
        //Get the game ID from the user
        int gameId = getGameId(false);
        if(gameId == -1)
            return "";

        //Retrieve the game from the collection
        BoardGame game = gameManager.retrieveGame(gameId);
        if(gameId != 0)
            //Execute the gameManager calculateTRS method and return the results
            return String.format("The table resistance score for %s is %.2f", game.getTitle(), gameManager.calculateTRS(game));
        else
            return "Game not found";
    }

    /**
     * method: getGameId
     * parameters: boolean isCreate - true if the game ID is being requested for a new game
     * return: int
     * purpose: Gets a valid game ID from the user, or returns -1 if the user types exit.
     */
    private int getGameId(boolean isCreate)
    {
        int gameId = 0;

        //Check if the collection is empty and return -1 if it is
        if(gameManager.validateCollectionIsEmpty() && !isCreate)
            return -1;

        //Request the game ID from the user until a valid number is entered
        //Returns -1 if the user types exit
        while (gameId == 0) {
            System.out.println("Please enter a game ID or exit to return to the main menu: ");
            try {
                String sGameId = userInput.nextLine();
                if (sGameId.equalsIgnoreCase("exit")) {
                    return -1;
                }
                else {
                    gameId = Integer.parseInt(sGameId);
                    if (isCreate && gameId != 0 && gameManager.retrieveGame(gameId) != null)
                    {
                        gameId = 0;
                        System.out.println("Game ID already exists. Please enter a new ID");
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number");
                continue;
            }
        }
        return gameId;
    }
}
