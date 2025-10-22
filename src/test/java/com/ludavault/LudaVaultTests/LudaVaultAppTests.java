package com.ludavault.LudaVaultTests;

import com.ludavault.LudaVault.LudaVaultController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 10/22/2025
 * LudaVaultAppTests
 * Defines the LudaVaultAppTests class for testing the LudaVaultApp and LudaVaultController object
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = com.ludavault.LudaVault.LudaVaultApp.class)
class LudaVaultAppTests {

    @Autowired
    private LudaVaultController ludaVaultController;

    @Autowired
    private MockMvc mockMvc;

    /**
     * method: contextLoads
     * parameters: none
     * return: void
     * purpose: Tests that the ludaVaultController object is not null
     */
    @Test
    void contextLoads() {
        assertNotNull(ludaVaultController);
    }

    /**
     * method: testIndex
     * parameters: none
     * return: void
     * purpose: Tests that the index page returns a successful response and contains the Logo
     */
    @Test
    void testIndex() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("/LudaVault_Logo.svg")));
    }

    /**
     * method: testBulkImportNoError
     * parameters: none
     * return: void
     * purpose: Tests the bulkImport method of the LudaVaultController object with a valid file
     */
    @Test
    void testBulkImportNoError() throws Exception {
        String sampleData = "1,Catan,4,90,2.3,false\n2,Carcassonne,5,45,1.9,false\n3,Pandemic,4,45,2.4,false";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", sampleData.getBytes());
        this.mockMvc.perform(multipart("/bulkImport").file(file))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("3 games imported.")))
                .andExpect(content().string(containsString("Catan")))
                .andExpect(content().string(containsString("Carcassonne")))
                .andExpect(content().string(containsString("Pandemic")));
    }

    /**
     * method: testBulkImportEmptyFile
     * parameters: none
     * return: void
     * purpose: Tests the bulkImport method of the LudaVaultController object with an invalid file
     */
    @Test
    void testBulkImportEmptyFile() throws Exception {
        String sampleData = "";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", sampleData.getBytes());
        this.mockMvc.perform(multipart("/bulkImport").file(file))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Error processing file: File is empty")));
    }

    /**
     * method: testNewGame
     * parameters: none
     * return: void
     * purpose: Tests the newGame method of the LudaVaultController object resets the game form
     */
    @Test
    void testNewGame() throws Exception {
        this.mockMvc.perform(get("/newGame")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create Game")));
    }

    /**
     * method: testCreateGameNoErrors
     * parameters: none
     * return: void
     * purpose: Tests the createGame method of the LudaVaultController object with valid parameters
     */
    @Test
    void testCreateGameNoErrors() throws Exception {
        this.mockMvc.perform(post("/createGame")
                        .param("gameId", "4")
                        .param("title", "Gloomhaven")
                        .param("maxPlayers", "4")
                        .param("playTime", "120")
                        .param("weight", "3.8")
                        .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Gloomhaven")))
                .andExpect(content().string(containsString("120 Minutes")));
    }

    /**
     * method: testCreateGameInvalidEntries
     * parameters: none
     * return: void
     * purpose: Tests the createGame method of the LudaVaultController object with invalid parameters
     */
    @Test
    void testCreateGameInvalidEntries() throws Exception {
        this.mockMvc.perform(post("/createGame")
                .param("gameId", "0")
                .param("title", "")
                .param("maxPlayers", "0")
                .param("playTime", "0")
                .param("weight", "7.2")
                .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        this.mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Game ID must be greater than or equal to 1.")))
                .andExpect(content().string(containsString("Title cannot be empty.")))
                .andExpect(content().string(containsString("Maximum players must be greater than or equal to 1.")))
                .andExpect(content().string(containsString("Play time must be greater than or equal to 1.")))
                .andExpect(content().string(containsString("Weight must be between 1 and 5.")));
    }

    /**
     * method: testCreateGameDuplicateId
     * parameters: none
     * return: void
     * purpose: Tests the createGame method of the LudaVaultController object with an already existing game ID
     */
    @Test
    void testCreateGameDuplicateId() throws Exception {
        this.mockMvc.perform(post("/createGame")
                .param("gameId", "5")
                .param("title", "Ticket to Ride")
                .param("maxPlayers", "5")
                .param("playTime", "45")
                .param("weight", "1.9")
                .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(post("/createGame")
                        .param("gameId", "5")
                        .param("title", "Catan")
                        .param("maxPlayers", "4")
                        .param("playTime", "120")
                        .param("weight", "2.1")
                        .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));


        this.mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Game with ID: 5 already exists")));
    }

    /**
     * method: testRetrieveGameNoError
     * parameters: none
     * return: void
     * purpose: Tests the retrieveGame method of the LudaVaultController object with a valid game ID
     */
    @Test
    void testRetrieveGameNoError() throws Exception {
        this.mockMvc.perform(post("/createGame")
                        .param("gameId", "6")
                        .param("title", "7 Wonders")
                        .param("maxPlayers", "7")
                        .param("playTime", "30")
                        .param("weight", "2.3")
                        .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/retrieveGame").param("gameId", "6"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Update Game")))
                .andExpect(content().string(containsString("7 Wonders")))
                .andExpect(content().string(containsString("30 Minutes")));
    }

    /**
     * method: testRetrieveGameNoError
     * parameters: none
     * return: void
     * purpose: Tests the retrieveGame method of the LudaVaultController object with an invalid game ID
     */
    @Test
    void testRetrieveGameNotFound() throws Exception {
        this.mockMvc.perform(get("/retrieveGame").param("gameId", "99"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Game with ID: 99 not found")));
    }

    /**
     * method: testUpdateGameNoError
     * parameters: none
     * return: void
     * purpose: Tests the updateGame method of the LudaVaultController object with valid parameters
     */
    @Test
    void testUpdateGameNoError() throws Exception {
        this.mockMvc.perform(post("/createGame")
                        .param("gameId", "7")
                        .param("title", "Terraforming")
                        .param("maxPlayers", "5")
                        .param("playTime", "120")
                        .param("weight", "3.2")
                        .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(post("/updateGame")
                        .param("gameId", "7")
                        .param("title", "Terraforming Mars")
                        .param("maxPlayers", "5")
                        .param("playTime", "120")
                        .param("weight", "3.2")
                        .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Update Game")))
                .andExpect(content().string(containsString("Terraforming Mars")))
                .andExpect(content().string(containsString("120 Minutes")));
    }

    /**
     * method: testUpdateGameInvalidEntries
     * parameters: none
     * return: void
     * purpose: Tests the updateGame method of the LudaVaultController object with invalid parameters
     */
    @Test
    void testUpdateGameInvalidEntries() throws Exception {
        this.mockMvc.perform(post("/createGame")
                        .param("gameId", "8")
                        .param("title", "Wingspan")
                        .param("maxPlayers", "0")
                        .param("playTime", "0")
                        .param("weight", "0"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Maximum players must be greater than or equal to 1.")))
                .andExpect(content().string(containsString("Play time must be greater than or equal to 1.")))
                .andExpect(content().string(containsString("Weight must be between 1 and 5.")))
                .andExpect(content().string(containsString("Expansion status must be provided.")));
    }

    /**
     * method: testUpdateGameNotFound
     * parameters: none
     * return: void
     * purpose: Tests the updateGame method of the LudaVaultController object with an invalid game ID
     */
    @Test
    void testUpdateGameNotFound() throws Exception {
        this.mockMvc.perform(post("/updateGame")
                        .param("gameId", "99")
                        .param("title", "Catan")
                        .param("maxPlayers", "4")
                        .param("playTime", "120")
                        .param("weight", "2.1")
                        .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Game with ID: 99 not found")));
    }

    /**
     * method: testDeleteGameNoError
     * parameters: none
     * return: void
     * purpose: Tests the deleteGame method of the LudaVaultController object with a valid game ID
     */
    @Test
    void testDeleteGameNoError() throws Exception {
        this.mockMvc.perform(post("/createGame")
                        .param("gameId", "9")
                        .param("title", "Azul")
                        .param("maxPlayers", "4")
                        .param("playTime", "45")
                        .param("weight", "1.8")
                        .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/deleteGame").param("gameId", "9"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Azul"))));
    }

    /**
     * method: testDeleteGameNotFound
     * parameters: none
     * return: void
     * purpose: Tests the deleteGame method of the LudaVaultController object with an invalid game ID
     */
    @Test
    void testDeleteGameNotFound() throws Exception {
        this.mockMvc.perform(get("/deleteGame").param("gameId", "99"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Game with ID: 99 not found")));
    }

    /**
     * method: testCalculateTRSNoError
     * parameters: none
     * return: void
     * purpose: Tests the calculateTRS method of the LudaVaultController object with a valid game ID
     */
    @Test
    void testCalculateTRSNoError() throws Exception {
        this.mockMvc.perform(post("/createGame")
                        .param("gameId", "10")
                        .param("title", "Gloomhaven")
                        .param("maxPlayers", "4")
                        .param("playTime", "240")
                        .param("weight", "3.8")
                        .param("isExpansion", "false"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/calculateTRS").param("gameId", "10"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<label>3.9</label>")));
    }

    /**
     * method: testCalculateTRSNotFound
     * parameters: none
     * return: void
     * purpose: Tests the calculateTRS method of the LudaVaultController object with an invalid game ID
     */
    @Test
    void testCalculateTRSNotFound() throws Exception {
        this.mockMvc.perform(get("/calculateTRS").param("gameId", "99"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Game with ID: 99 not found")));
    }
}
