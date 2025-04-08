package com.example.advgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


import java.util.Random;

public class HelloController {

    @FXML
    private Label playerStatsLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label npcStatsLabel;

    @FXML
    private Label goldLabel;

    @FXML
    private TextArea gameTextArea;

    // Navigation Buttons
    @FXML private Button handleMoveNorthButtonClick;
    @FXML private Button handleMoveWestButtonClick;
    @FXML private Button handleMoveEastButtonClick;
    @FXML private Button handleMoveSouthButtonClick;


    // Action Buttons
    @FXML private Button handleAttackButton;
    @FXML private Button handleRunButton;
    @FXML private Button handleSearchButton;
    @FXML private Button handleSleepButton;

    // Start and Quit Buttons
    @FXML private Button handleStartButton;
    @FXML private Button handleQuitButton;



    private PlayerCharacter player;
    private Room[][] rooms;
    private Random random;
    private Room currentRoom;

    public void initialize() {
        player = new PlayerCharacter();
        rooms = new Room[10][10];
        random = new Random();

        createRooms();
        setCurrentRoom(rooms[0][0]); // Set starting room
        updateUI();

        // Disable all buttons except the Start button
        handleMoveNorthButtonClick.setDisable(true);
        handleMoveWestButtonClick.setDisable(true);
        handleMoveEastButtonClick.setDisable(true);
        handleMoveSouthButtonClick.setDisable(true);
        handleAttackButton.setDisable(true);
        handleRunButton.setDisable(true);
        handleSearchButton.setDisable(true);
        handleSleepButton.setDisable(true);

        // Enable the Start button
        handleStartButton.setDisable(false);

        // Disable Quit button initially (you can enable it if needed)
        handleQuitButton.setDisable(false);

        // Update UI after setting the initial currentRoom
        updateUI();

    }

    private void createRooms() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                boolean blocked = Math.random() < 0.15; // 15% chance of being blocked
                int gold = random.nextInt(100) + 1; // random gold amount
                rooms[row][col] = new Room(row, col, blocked, gold);


                if (!rooms[row][col].hasNPC() && Math.random() < 0.5) {
                    System.out.println("Adding NPC to room at (" + row + ", " + col + ")");
                    rooms[row][col].addNPC(new NPC());
                }

            }
        }
    }

    private void setCurrentRoom(Room room) {
        currentRoom = room;
        locationLabel.setText("Location: (" + room.getRow() + ", " + room.getCol() + ")");
        updateUI();
    }

    private void updateUI() {
        playerStatsLabel.setText("Player Stats: HP=" + player.getHitPoints() +
                ", Str=" + player.getStrength() +
                ", Dex=" + player.getDexterity() +
                ", Int=" + player.getIntelligence() +
                ", Gold=" + player.getTotalGold());

        goldLabel.setText("Gold in Room: " + currentRoom.getGold());

        if (currentRoom.hasNPC()) {
            NPC npc = currentRoom.getRandomNPC();
            npcStatsLabel.setText("NPC Stats: HP=" + npc.getHitPoints() +
                    ", Str=" + npc.getStrength() +
                    ", Dex=" + npc.getDexterity() +
                    ", Int=" + npc.getIntelligence());
            System.out.println("NPC stats updated");
        } else {
            npcStatsLabel.setText("No NPC in Room");
        }
    }

    @FXML
    private void handleMoveNorthButtonClick(ActionEvent event) {
        if (currentRoom.getRow() > 0 && !rooms[currentRoom.getRow() - 1][currentRoom.getCol()].isBlocked()) {
            setCurrentRoom(rooms[currentRoom.getRow() - 1][currentRoom.getCol()]);
            gameTextArea.appendText("You moved north.\n");
        } else {
            gameTextArea.appendText("You can't move in that direction.\n");
        }
        updateUI();
    }

    @FXML
    private void handleMoveWestButtonClick(ActionEvent event) {
        if (currentRoom.getCol() > 0 && !rooms[currentRoom.getRow()][currentRoom.getCol() - 1].isBlocked()) {
            setCurrentRoom(rooms[currentRoom.getRow()][currentRoom.getCol() - 1]);
            gameTextArea.appendText("You moved west.\n");
        } else {
            gameTextArea.appendText("You can't move in that direction.\n");
        }
        updateUI();
    }

    @FXML
    private void handleMoveEastButtonClick(ActionEvent event) {
        if (currentRoom.getCol() < 9 && !rooms[currentRoom.getRow()][currentRoom.getCol() + 1].isBlocked()) {
            setCurrentRoom(rooms[currentRoom.getRow()][currentRoom.getCol() + 1]);
            gameTextArea.appendText("You moved east.\n");
        } else {
            gameTextArea.appendText("You can't move in that direction.\n");
        }
        updateUI();
    }

    @FXML
    private void handleMoveSouthButtonClick(ActionEvent event) {
        if (currentRoom.getRow() < 9 && !rooms[currentRoom.getRow() + 1][currentRoom.getCol()].isBlocked()) {
            setCurrentRoom(rooms[currentRoom.getRow() + 1][currentRoom.getCol()]);
            gameTextArea.appendText("You moved south.\n");
        } else {
            gameTextArea.appendText("You can't move in that direction.\n");
        }
        updateUI();
    }


    @FXML
    private void handleSearchButtonClick(ActionEvent event) {
        if (currentRoom.hasNPC()) {
            gameTextArea.appendText("You can't search while an NPC is present.\n");
        } else {
            if (currentRoom.getGold() > 0) {
                int roll = random.nextInt(20) + 1;
                if (roll < player.getIntelligence()) {
                    int maxFoundGold = currentRoom.getGold(); // Ensure found gold doesn't exceed room gold
                    int foundGold = random.nextInt(Math.min(20, maxFoundGold)) + 1; // Limit found gold to room gold
                    player.increaseTotalGold(foundGold);
                    currentRoom.setGold(currentRoom.getGold() - foundGold);
                    gameTextArea.appendText("You found " + foundGold + " gold!\n");
                } else {
                    gameTextArea.appendText("You didn't find any gold.\n");
                }
                updateUI();
            } else {
                gameTextArea.appendText("There is no gold to be found in this room.\n");
            }
        }
    }

    @FXML
    private void handleSleepButtonClick(ActionEvent event) {
        if (currentRoom.hasNPC()) {
            gameTextArea.appendText("You can't sleep while an NPC is present.\n");
        } else if (player.getHitPoints() == 20) {
            gameTextArea.appendText("You slept but your hit points are already full.\n");
        } else {
            player.setHitPoints(20); // Set hit points to 20 when sleeping
            gameTextArea.appendText("You slept and regained your hit points.\n");

            int roll = random.nextInt(6) + 1;
            if (roll == 1) {
                NPC newNpc = new NPC();
                currentRoom.addNPC(newNpc);
                gameTextArea.appendText("While sleeping, an NPC found you!\n");
                System.out.println("NPC added to room while sleeping");

                // NPC attacks the player
                int npcDamage = Math.max(newNpc.attack(player), 1); // Ensure minimum damage of 1
                gameTextArea.appendText("The NPC attacked you and dealt " + npcDamage + " damage while you were sleeping!\n");

                // Update player's hit points
                player.takeDamage(npcDamage);

                if (player.getHitPoints() <= 0) {
                    gameTextArea.appendText("You were defeated by the NPC.\n");
                    handlePlayerDefeat(); // Handle game over state
                    return; // End the method to prevent further actions
                }
            }

            updateUI();
        }
    }


    @FXML
    private void handleAttackButtonClick(ActionEvent event) {
        if (currentRoom.hasNPC()) {
            NPC npc = currentRoom.getRandomNPC();
            int playerDamage = player.attack(npc);

            if (npc.getHitPoints() <= 0) {
                currentRoom.removeNPC(npc);
                player.increaseTotalGold(npc.getStrength() * 2); // Gold from defeated NPC
                gameTextArea.appendText("You defeated the NPC and gained " + (npc.getStrength() * 2) + " gold!\n");
            } else {
                int npcDamage = npc.attack(player);
                gameTextArea.appendText("You attacked the NPC and caused " + playerDamage + " damage.\n");

                if (player.getHitPoints() <= 0) {
                    gameTextArea.appendText("You were defeated by the NPC.\n");
                    handlePlayerDefeat(); // Handle game over state
                    return; // End the method to prevent further actions
                } else {
                    gameTextArea.appendText("The NPC attacked and caused " + npcDamage + " damage.\n");
                }
            }

            updateUI();
        } else {
            gameTextArea.appendText("There is no NPC to attack.\n");
        }
    }

    private void handlePlayerDefeat() {
        gameTextArea.appendText("Game Over - You were defeated by the NPC!\n");

        // Update player's hit points to zero
        player.setHitPoints(0);

        // Disable navigation buttons
        handleMoveNorthButtonClick.setDisable(true);
        handleMoveWestButtonClick.setDisable(true);
        handleMoveEastButtonClick.setDisable(true);
        handleMoveSouthButtonClick.setDisable(true);

        // Disable action buttons
        handleAttackButton.setDisable(true);
        handleRunButton.setDisable(true);
        handleSearchButton.setDisable(true);
        handleSleepButton.setDisable(true);

        // Disable start and quit buttons
        handleStartButton.setDisable(false);
        handleQuitButton.setDisable(true);
    }


    @FXML
    private void handleRunButtonClick(ActionEvent event) {
        if (currentRoom.hasNPC()) {
            NPC npc = currentRoom.getRandomNPC();
            boolean seen = npc.getIntelligence() > (int) (Math.random() * 20) + 1;

            if (seen) {
                int npcDamage = Math.max(npc.attack(player), 1);
                gameTextArea.appendText("The NPC saw you trying to run away and attacked you, dealing "
                        + npcDamage + " damage!\n");

                // Update player's hit points
                player.takeDamage(npcDamage);

                if (player.getHitPoints() <= 0) {
                    gameTextArea.appendText("You were defeated by the NPC.\n");
                    handlePlayerDefeat(); // Handle game over state
                    return; // End the method to prevent further actions
                }
            } else {
                gameTextArea.appendText("You successfully escaped from the NPC!\n");

                // Move the player to a different room (random direction)
                setCurrentRoom(getRandomAdjacentRoom());

                // Check if the new room has an NPC
                if (currentRoom.hasNPC()) {
                    NPC newNpc = currentRoom.getRandomNPC();
                    int npcDamage = newNpc.attack(player);
                    gameTextArea.appendText("An NPC in the new room attacked you and dealt " + npcDamage + " damage.\n");

                    // Update player's hit points
                    player.takeDamage(npcDamage);

                    if (player.getHitPoints() <= 0) {
                        gameTextArea.appendText("You were defeated by the NPC.\n");
                        handlePlayerDefeat(); // Handle game over state
                        return; // End the method to prevent further actions
                    }
                }
            }
            updateUI();
        } else {
            gameTextArea.appendText("There is no NPC to run away from.\n");
        }
    }


    private Room getRandomAdjacentRoom() {
        int newRow = currentRoom.getRow();
        int newCol = currentRoom.getCol();

        // Randomly choose a direction (up, down, left, or right)
        int direction = random.nextInt(4);
        switch (direction) {
            case 0: // Up
                newRow = Math.max(newRow - 1, 0);
                break;
            case 1: // Down
                newRow = Math.min(newRow + 1, 9);
                break;
            case 2: // Left
                newCol = Math.max(newCol - 1, 0);
                break;
            case 3: // Right
                newCol = Math.min(newCol + 1, 9);
                break;
        }

        return rooms[newRow][newCol];
    }

    @FXML
    private void handleStartButtonClick(ActionEvent event) {
        gameTextArea.clear();
        gameTextArea.appendText("Game started!\n");

        // Check if the current room has an NPC
        if (!currentRoom.hasNPC() && Math.random() < 0.5) {
            System.out.println("NPC generation condition met");
            currentRoom.addNPC(new NPC());
            gameTextArea.appendText("An NPC has appeared in the room!\n");
            // Update NPC stats label after adding NPC to the room
            updateUI();
        } else {
            System.out.println("NPC generation condition not met");
            // Update UI in case there is no NPC
            updateUI();
        }
        // Enable necessary buttons and disable the Start button
        handleMoveNorthButtonClick.setDisable(false);
        handleMoveWestButtonClick.setDisable(false);
        handleMoveEastButtonClick.setDisable(false);
        handleMoveSouthButtonClick.setDisable(false);
        handleAttackButton.setDisable(false);
        handleRunButton.setDisable(false);
        handleSearchButton.setDisable(false);
        handleSleepButton.setDisable(false);
        handleStartButton.setDisable(false);
        handleQuitButton.setDisable(false);
    }

    @FXML
    private void handleQuitButtonClick(ActionEvent event) {
        gameTextArea.clear();
        gameTextArea.appendText("Game quit.\n");
        updateUI();
        // Disable all buttons
        handleMoveNorthButtonClick.setDisable(true);
        handleMoveWestButtonClick.setDisable(true);
        handleMoveEastButtonClick.setDisable(true);
        handleMoveSouthButtonClick.setDisable(true);
        handleAttackButton.setDisable(true);
        handleRunButton.setDisable(true);
        handleSearchButton.setDisable(true);
        handleSleepButton.setDisable(true);
        handleStartButton.setDisable(true);
        handleQuitButton.setDisable(true);
    }



}