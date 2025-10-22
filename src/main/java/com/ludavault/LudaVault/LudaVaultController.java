package com.ludavault.LudaVault;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class LudaVaultController {
    private GameManager gameManager;
    private BoardGame currentBoardGame;
    private double trsValue = 0;
    private ArrayList<Message> messages;

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

    @GetMapping("/calculateTRS")
    public String calculateTRS(@RequestParam int gameId, Model model) {
        trsValue = 0;
        messages = null;

        BoardGame game = gameManager.retrieveGame(gameId);
        if(game == null) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Game with ID: " + gameId + " not found"));
        }
        trsValue = gameManager.calculateTRS(game);
        if(trsValue <= 0) {
            messages = new ArrayList<Message>();
            messages.add(new Message("error", "Problem calculating TRS for game with ID: " + gameId));
        }
        return "redirect:/";
    }

    //Prepping for phase 4
    @PostMapping("/setSqlConnection")
    public String setSqlConnection(@RequestParam("sql-db-path") String sqldbpath,
                                   Model model) {
        //gameManager = new GameManager(sqldbpath);
        return "redirect:/";
    }

    private String parseBindingResultErrors(BindingResult bindingResult) {
        messages = new ArrayList<Message>();

        bindingResult.getAllErrors().forEach(error -> {
            messages.add(new Message("error", error.getDefaultMessage()));
        });
        return "redirect:/";
    }
}
