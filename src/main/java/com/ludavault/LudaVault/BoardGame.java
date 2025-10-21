package com.ludavault.LudaVault;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/12/2025
 * main.java.com.ludavault.LudaVault.BoardGame
 * Defines the main.java.com.ludavault.LudaVault.BoardGame object that stores information about a game.
 */
public class BoardGame {
    /**
     * Class attributes:
     *     gameId: int - the unique identifier for the game.
     *     title: String - the title of the game.
     *     maxPlayers: int - the maximum number of players for the game.
     *     playTime: int - the estimated playtime for the game.
     *     weight: double - the weight of the game.
     *     isExpansion: boolean - whether the game is an expansion.
     */
    private Integer gameId;
    private String title;
    private Integer maxPlayers;
    private Integer playTime;
    private Double weight;
    private Boolean isExpansion;

    /**
     * constructor: main.java.com.ludavault.LudaVault.BoardGame
     * parameters: int gameId - the unique identifier for the game.
     *             String title - the title of the game.
     *             int maxPlayers - the maximum number of players for the game.
     *             int playTime - the estimated playtime for the game.
     *             double weight - the weight of the game.
     *             boolean isExpansion - whether the game is an expansion.
     * return: main.java.com.ludavault.LudaVault.BoardGame
     * purpose: Initializes a new main.java.com.ludavault.LudaVault.BoardGame object with the provided attributes.
     */
    public BoardGame(int gameId, String title, int maxPlayers, int playTime, double weight, boolean isExpansion)
    {
        this.gameId = gameId;
        this.title = title;
        this.maxPlayers = maxPlayers;
        this.playTime = playTime;
        this.weight = weight;
        this.isExpansion = isExpansion;
    }

    public BoardGame()
    {
        this.gameId = null;
        this.title = null;
        this.maxPlayers = null;
        this.playTime = null;
        this.weight = null;
        this.isExpansion = null;
    }

    //Getters
    public Integer getGameId() {
        return gameId;
    }
    public String getTitle() {
        return title;
    }
    public Integer getMaxPlayers() {
        return maxPlayers;
    }
    public Integer getPlayTime() {
        return playTime;
    }
    public Double getWeight() {
        return weight;
    }
    public Boolean getIsExpansion() {
        return isExpansion;
    }

    //Setters
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    public void setPlayTime(Integer playTime) {
        this.playTime = playTime;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public void setIsExpansion(Boolean isExpansion) {
        this.isExpansion = isExpansion;
    }

    /**
     * method: calculateTRS
     * parameters: none
     * return: double
     * purpose: Calculates the Total Rating Score (TRS) for a board game based on playtime in hours (30%),
     * max players (20%), and weight (50%)
     */
    public double calculateTRS()
    {
        double TRS = 0;
        double playTimeFactor = ((double) playTime /60) * .3;
        double maxPlayersFactor = maxPlayers * .2;
        double weightFactor = weight * .5;

        TRS = playTimeFactor + maxPlayersFactor + weightFactor;

        return TRS;
    }

    /**
     * method: toString
     * parameters: none
     * return: String
     * purpose: Returns a string representation of the board game's information,
     * formatting the weight to two decimal places.
     */
    @Override
    public String toString()
    {
        return "BoardGame [gameId=" + gameId + ", title=" + title + ", maxPlayers=" + maxPlayers + ", playTime="
                + playTime + ", weight=" + weight + ", isExpansion=" + isExpansion + "]";
    }
}
