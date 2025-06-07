# Software-Engineering-project-2-23739461-23318116-23318136

---
# 🧩 HexOust – Hexagonal Strategy Board Game

## 🎮 Overview

**HexOust** is a two-player turn-based strategy board game implemented in Java using JavaFX. The game challenges players to place stones on a hexagonal grid while attempting to trap and eliminate the opponent’s stones. Players alternate turns until one side either loses all their pieces or the board is full.

This project was developed as a final assignment for UCD module Software Engineering Project 2 (COMP20050) module by Group 42 – *Elite Three*.

The project was divided up into 4 sprints and for each sprint we had a plan of the work needed to be done and what features to implement. We had meetings at the beginning of each sprint to decide the tasks everyone was assigned to.

---
## 🔧 Features

- 🟥🟦 Two-player gameplay with color-coded turns (Red starts first)
- 🧠 Valid move validation and turn management
- 🧱 Hexagonal board rendering via JavaFX
- 💥 Capture logic to remove surrounded stones
- 🏆 Automatic win detection and game-over message
- 🔄 Restart and exit buttons integrated with game state reset
- 🧪 Comprehensive unit testing of game logic and board behavior

---

## 🗂️ Key Classes

| Class                         | Description                                                            |
|-------------------------------|------------------------------------------------------------------------|
| `Game`                        | Core controller managing board state, player turns, and win conditions |
| `Board`                       | Represents the hexagonal grid and cell placement logic                 |
| `Cell`                        | Encapsulates position (q, r, s) and occupancy of a board tile          |
| `Player`                      | Defines each player’s identity, color, and turn behavior               |
| `Point`                       | Holds cube coordinates used to manage the hex grid layout              |
| `UIHandler`                   | JavaFX class that handles rendering of board, stones, and UI elements  |
| `HelloControllerTest`         | JUnit test suite to validate board and game logic                      |

---

## 🚀 How to Run the Game

### 🧰 Prerequisites

- Java 11+ installed (check via `java -version`)
- JavaFX SDK (can be downloaded from [openjfx.io](https://openjfx.io/))

### ▶️ Launching the Game

1. Locate the `.jar` file:  
   `out/artifacts/HexagonalBoard_jar/HexagonalBoard.jar`

2. Replace the path below with your actual JavaFX SDK location:

```bash
java --module-path /path/to/javafx-sdk/lib \
--add-modules javafx.controls,javafx.fxml \
-jar out/artifacts/HexagonalBoard_jar/HexagonalBoard.jar
````

---

## 🎮 How to Play

* Red always goes first.
* Click on a valid hexagonal tile to place your stone.
* Captured opponent stones are automatically removed.
* The game ends when a player has no stones left or all tiles are filled.
* Use the restart button to play again or exit to close the game.

---

## 🎥 Demo Video
📽️ [Watch Gameplay Demo](https://github.com/user-attachments/assets/e23ee93e-8697-4e27-8d5a-38a0c18d18b5)

## 👥 Team & Contributions

**Group 42: Elite Three**

* **Spencer Kesse** (23739461):

* **Abdulwahid Adesanya** (23318116):

* **Declan Chukwu** (23318136):

All team members collaborated equally on planning, troubleshooting, and integration.

---

## 🧪 Testing

A comprehensive `HelloControllerTest` Unit test class is included to test:

* Cell placement validation
* Stone capture conditions
* Edge cases and invalid inputs
* Win detection logic

To run tests:

* Open in IntelliJ or your preferred Java IDE
* Run the `HelloControllerTest.java` file using JUnit

---

## 📌 Notes
* Uses cube coordinate system `(q, r, s)` for managing the hex grid
* Project built using MVC design pattern principles
---


