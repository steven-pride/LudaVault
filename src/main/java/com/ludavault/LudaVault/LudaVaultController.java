package com.ludavault.LudaVault;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Controller
public class LudaVaultController {
    private GameManager gameManager;
    private BoardGame currentBoardGame;
    private double trsValue = 0;
    private HashMap<String, String> messages;

    public LudaVaultController() {
        gameManager = new GameManager();
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", gameManager.listGames());
        if(currentBoardGame == null) {
            currentBoardGame = new BoardGame();
            currentBoardGame.setGameId(gameManager.getMaxId() + 1);
        }
        model.addAttribute("boardGame", currentBoardGame);
        model.addAttribute("trsValue", trsValue);
        model.addAttribute("maxId", gameManager.getMaxId());
        model.addAttribute("messages", messages);
        return "index";
    }

    @GetMapping("/newGame")
    public String addGame(Model model) {
        currentBoardGame = null;
        trsValue = 0;
        messages = null;
        currentBoardGame = new BoardGame();
        currentBoardGame.setGameId(gameManager.getMaxId() + 1);
        return "redirect:/";
    }

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
            messages = gameManager.bulkImport(fileBytes);
        }
        catch (Exception e) {
            messages = new HashMap<String, String>();
            messages.put("error", "Error processing file: " + e.getMessage());
            return "redirect:/";
        }

        return "redirect:/";
    }

    @PostMapping("/createGame")
    public String createGame(@ModelAttribute("boardGame") BoardGame boardGame,
                             BindingResult bindingResult,
                             Model model) {
        currentBoardGame = null;
        trsValue = 0;
        messages = null;

        boolean result = gameManager.createGame(boardGame.getGameId(), boardGame.getTitle(), boardGame.getMaxPlayers(), boardGame.getPlayTime(), boardGame.getWeight(), boardGame.getIsExpansion());
        currentBoardGame = boardGame;
        if(!result) {
            messages = new HashMap<String, String>();
            messages.put("error", "Problem creating game with ID: " + boardGame.getGameId());
        }

        return "redirect:/";
    }

    @GetMapping("/retrieveGame")
    public String retrieveGame(@RequestParam int gameId, Model model) {
        currentBoardGame = null;
        trsValue = 0;
        messages = null;

        currentBoardGame = gameManager.retrieveGame(gameId);
        if(currentBoardGame == null) {
            messages = new HashMap<String, String>();
            messages.put("error", "Game with ID: " + gameId + " not found");
        }

        return "redirect:/";
    }

    @PostMapping("/updateGame")
    public String updateGame(@ModelAttribute("boardGame") BoardGame boardGame,
                             BindingResult bindingResult,
                             Model model) {
        trsValue = 0;
        messages = null;

        boolean result = gameManager.updateGame(boardGame.getGameId(), boardGame.getTitle(), boardGame.getMaxPlayers(), boardGame.getPlayTime(), boardGame.getWeight(), boardGame.getIsExpansion());
        if(!result) {
            messages = new HashMap<String, String>();
            messages.put("error", "Problem updating game with ID: " + boardGame.getGameId());
        }
        return "redirect:/";
    }

    @GetMapping("/deleteGame")
    public String deleteGame(@RequestParam int gameId, Model model) {
        currentBoardGame = null;
        trsValue = 0;
        messages = null;

        boolean result = gameManager.deleteGame(gameId);
        if(!result) {
            messages = new HashMap<String, String>();
            messages.put("error", "Problem deleting game with ID: " + gameId);
        }
        return "redirect:/";
    }

    @GetMapping("/calculateTRS")
    public String calculateTRS(@RequestParam int gameId, Model model) {
        trsValue = 0;
        messages = null;

        BoardGame game = gameManager.retrieveGame(gameId);
        if(game == null) {
            messages = new HashMap<String, String>();
            messages.put("error", "Game with ID: " + gameId + " not found");
        }
        trsValue = gameManager.calculateTRS(game);
        if(trsValue <= 0) {
            messages = new HashMap<String, String>();
            messages.put("error", "Problem calculating TRS for game with ID: " + gameId);
        }
        return "redirect:/";
    }
}
