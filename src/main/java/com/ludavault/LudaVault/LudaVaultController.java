package com.ludavault.LudaVault;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LudaVaultController {
    private GameManager gameManager;
    public LudaVaultController() {
        //if(gameManager == null)
            gameManager = new GameManager();
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", gameManager.listGames());
        model.addAttribute("newGame", new BoardGame());
        return "index";
    }

    @PostMapping("/bulkImport")
    public String bulkImport(@RequestParam String games, Model model) {
        return "redirect:/";
    }

    @PostMapping("/createGame")
    public String createGame(@ModelAttribute("newGame") BoardGame newGame,
                             BindingResult bindingResult,
                             Model model) {
        gameManager.createGame(newGame.getGameId(), newGame.getTitle(), newGame.getMaxPlayers(), newGame.getPlayTime(), newGame.getWeight(), newGame.getIsExpansion());
        model.addAttribute("newGame", newGame);

        return "redirect:/";
    }

    @GetMapping("/retrieveGame")
    public String retrieveGame(@RequestParam int gameId, Model model) {
        BoardGame game = gameManager.retrieveGame(gameId);
        if(game != null)
            model.addAttribute("game", game);
        return "index";
    }

    @PostMapping("/updateGame")
    public String updateGame(@ModelAttribute("newGame") BoardGame newGame,
                             BindingResult bindingResult,
                             Model model) {
        gameManager.updateGame(newGame.getGameId(), newGame.getTitle(), newGame.getMaxPlayers(), newGame.getPlayTime(), newGame.getWeight(), newGame.getIsExpansion());
        return "redirect:/";
    }

    @PostMapping("/deleteGame")
    public String deleteGame(@RequestParam int gameId, Model model) {
        gameManager.deleteGame(gameId);
        return "redirect:/";
    }

    @GetMapping("/calculateTRS")
    public String calculateTRS(@RequestParam int gameId, Model model) {
        BoardGame game = gameManager.retrieveGame(gameId);
        double trs = gameManager.calculateTRS(game);
        if(game != null)
            model.addAttribute("trsValue", trs);
        return "index";
    }


}
