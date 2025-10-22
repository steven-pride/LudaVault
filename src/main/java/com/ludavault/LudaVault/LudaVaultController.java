package com.ludavault.LudaVault;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/22/2025
 * LudaVaultController
 * Provides the controller for the LudaVault application.
 * Handles HTTP requests and responses for the application.
 */
@Controller
public class LudaVaultController {
    /**
     * Class attributes:
     *     gameManager: GameManager - an instance of the GameManager class to manage the board game collection
     *     currentBoardGame: BoardGame - the current board game being edited
     *     trsValue: double - the current Table Resistance Score (TRS) value for the current board game
     *     messages: ArrayList<Message> - a list of messages to be displayed to the user
     */
    private GameManager gameManager;
    private BoardGame currentBoardGame;
    private double trsValue = 0;
    private ArrayList<Message> messages;

    /**
     * constructor: LudaVaultController
     * parameters: none
     * return: LudaVaultController
     * purpose: Initializes the gameManager with a new GameManager instance.
     */
    public LudaVaultController() {
        gameManager = new GameManager();
    }

    /**
     * method: index
     * parameters: Model model - the model used to pass data to the view
     * return: String
     * purpose: Sets the model attributes for the index page. If currentBoardGame is null,
     * creates a new BoardGame object with the next available ID. Returns the index view.
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", gameManager.listGames());
        if(currentBoardGame == null) {
            currentBoardGame = new BoardGame();
        }
        model.addAttribute("boardGame", currentBoardGame);
        model.addAttribute("trsValue", trsValue);
        model.addAttribute("maxId", gameManager.getMaxId());
        model.addAttribute("messages", messages);
        return "index";
    }

    /**
     * method: newGame
     * parameters: Model model - the model used to pass data to the view
     * return: String
     * purpose: Sets the currentBoardGame to a new BoardGame object with the next available ID.
     * Clears messages and TRS value. Redirects to the index page.
     */
    @GetMapping("/newGame")
    public String newGame(Model model) {
        currentBoardGame = null;
        trsValue = 0;
        messages = null;
        currentBoardGame = new BoardGame();
        currentBoardGame.setGameId(gameManager.getMaxId() + 1);
        return "redirect:/";
    }

    /**
     * method: bulkImport
     * parameters: MultipartFile file - the file submitted from the view, a multipart form response
     *             Model model - the model used to pass data to the view
     * return: String
     * purpose: Imports board games from a file submitted from the view. Checks the file has content,
     * gets the file bytes, passes them to the gameManager to import the games, parses the results, and
     * redirects to the index page.
     */
    @PostMapping("/bulkImport")
    public String bulkImport(@RequestParam("file") MultipartFile file,
                             Model model) {
        currentBoardGame = null;
        trsValue = 0;
        messages = null;
        try {
            if(file.isEmpty() || file.getSize() == 0 || file.getContentType() == null)
                throw new Exception("File is empty");
            byte[] fileBytes = file.getBytes();
            HashMap<String, String> results = gameManager.bulkImport(fileBytes);
            messages = new ArrayList<Message>();
            for(String key : results.keySet()) {
                messages.add(new Message(key, results.get(key)));
            }
        }
        catch (Exception e) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Error processing file: " + e.getMessage()));
            return "redirect:/";
        }

        return "redirect:/";
    }

    /**
     * method: createGame
     * parameters: BoardGame boardGame - the board game submitted from the view
     *             BindingResult bindingResult - the binding result for the submitted form
     *             Model model - the model used to pass data to the view
     * return: String
     * purpose: Creates a new board game from the submitted form data. Validates the form data,
     * creates a new BoardGame object, and puts it into the gameDataStore. Sets messages if errors occur.
     * Redirects to the index page.
     */
    @PostMapping("/createGame")
    public String createGame(@Valid @ModelAttribute("boardGame") BoardGame boardGame,
                             BindingResult bindingResult,
                             Model model) {
        if(bindingResult.hasErrors()) {
            return parseBindingResultErrors(bindingResult);
        }
        currentBoardGame = null;
        trsValue = 0;
        messages = null;
        if(gameManager.retrieveGame(boardGame.getGameId()) != null) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Game with ID: " + boardGame.getGameId() + " already exists"));
            return "redirect:/";
        }
            boolean result = gameManager.createGame(boardGame.getGameId(), boardGame.getTitle(), boardGame.getMaxPlayers(), boardGame.getPlayTime(), boardGame.getWeight(), boardGame.getIsExpansion());
            currentBoardGame = boardGame;

            if(!result) {
                messages = new ArrayList<Message>();
                messages.add(new Message("error", "Problem creating game with ID: " + boardGame.getGameId()));
            }
        return "redirect:/";
    }

    /**
     * method: retrieveGame
     * parameters: int gameId - the ID of the board game to retrieve
     *             Model model - the model used to pass data to the view
     * return: String
     * purpose: Retrieves a BoardGame from the gameManager based on the provided gameId.
     * Checks if the game exists, and if so, sets the currentBoardGame to the retrieved game.
     * Sets messages if no game is found. Redirects to the index page.
     */
    @GetMapping("/retrieveGame")
    public String retrieveGame(@RequestParam int gameId, Model model) {
        currentBoardGame = null;
        trsValue = 0;
        messages = null;

        currentBoardGame = gameManager.retrieveGame(gameId);
        if(currentBoardGame == null) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Game with ID: " + gameId + " not found"));
        }

        return "redirect:/";
    }

    /**
     * method: updateGame
     * parameters: BoardGame boardGame - the board game submitted from the view
     *             BindingResult bindingResult - the binding result for the submitted form
     *             Model model - the model used to pass data to the view
     * return: String
     * purpose: Updates an existing board game from the submitted form data. Validates the form data,
     * updates the existing BoardGame object, and puts it into the gameDataStore. Sets messages if errors occur.
     * Redirects to the index page.
     */
    @PostMapping("/updateGame")
    public String updateGame(@Valid @ModelAttribute("boardGame") BoardGame boardGame,
                             BindingResult bindingResult,
                             Model model) {
        if(bindingResult.hasErrors()) {
            return parseBindingResultErrors(bindingResult);
        }

        if(gameManager.retrieveGame(boardGame.getGameId()) == null) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Game with ID: " + boardGame.getGameId() + " not found"));
            return "redirect:/";
        }

        messages = null;
        trsValue = 0;

        boolean result = gameManager.updateGame(boardGame.getGameId(), boardGame.getTitle(), boardGame.getMaxPlayers(), boardGame.getPlayTime(), boardGame.getWeight(), boardGame.getIsExpansion());
        if(!result) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Problem updating game with ID: " + boardGame.getGameId()));
        }
        return "redirect:/";
    }

    /**
     * method: deleteGame
     * parameters: int gameId - the ID of the board game to delete
     *             Model model - the model used to pass data to the view
     * return: String
     * purpose: Deletes a BoardGame from the gameManager based on the provided gameId.
     * Checks if the game exists, and if so, deletes the game. Sets messages if no game is found.
     * Redirects to the index page.
     */
    @GetMapping("/deleteGame")
    public String deleteGame(@RequestParam int gameId, Model model) {
        if(gameManager.retrieveGame(gameId) == null) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Game with ID: " + gameId + " not found"));
            return "redirect:/";
        }

        messages = null;
        currentBoardGame = null;
        trsValue = 0;

        boolean result = gameManager.deleteGame(gameId);
        if(!result) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Problem deleting game with ID: " + gameId));
        }
        return "redirect:/";
    }

    /**
     * method: calculateTRS
     * parameters: int gameId - the ID of the board game to calculate TRS for
     *             Model model - the model used to pass data to the view
     * return: String
     * purpose: Calculates the TRS value for a BoardGame based on the provided gameId.
     * Checks if the game exists, and if so, sets the trsValue to the calculated TRS.
     * Sets messages if no game is found. Redirects to the index page.
     */
    @GetMapping("/calculateTRS")
    public String calculateTRS(@RequestParam int gameId, Model model) {
        trsValue = 0;
        messages = null;

        BoardGame game = gameManager.retrieveGame(gameId);
        if(game == null) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Game with ID: " + gameId + " not found"));
            return "redirect:/";
        }
        trsValue = gameManager.calculateTRS(game);
        if(trsValue <= 0) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Problem calculating TRS for game with ID: " + gameId));
            return "redirect:/";
        }
        return "redirect:/";
    }

    //Prepping for phase 4
    /**
     * method: setSqlConnection
     * parameters: String sqldbpath - the path to the local SQL database file
     *             Model model - the model used to pass data to the view
     * return: String
     * purpose: Instantiates a new GameManager object with the provided SQL database file path.
     */
    @PostMapping("/setSqlConnection")
    public String setSqlConnection(@RequestParam("sql-db-path") String sqldbpath,
                                   Model model) {
        //gameManager = new GameManager(sqldbpath);
        return "redirect:/";
    }

    /**
     * method: parseBindingResultErrors
     * parameters: BindingResult bindingResult - the binding result from the submitted form
     * return: String
     * purpose: parses the binding result for errors, adds the errors to messages, and returns a redirect to the index page.
     */
    private String parseBindingResultErrors(BindingResult bindingResult) {
        messages = new ArrayList<Message>();

        bindingResult.getAllErrors().forEach(error -> {
            messages.add(new Message("error", error.getDefaultMessage()));
        });
        return "redirect:/";
    }
}
