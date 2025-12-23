# Bricker - Arkanoid Clone 🧱

**Bricker** is a modern implementation of the classic Breakout/Arkanoid arcade game, developed in **Java** using the **DanoGameLab** game engine.

This project was designed with a heavy focus on **Object-Oriented Programming (OOP)** principles, utilizing Design Patterns to create a flexible, scalable, and modular game architecture.

![Java](https://img.shields.io/badge/Language-Java_17-orange) ![OOP](https://img.shields.io/badge/Concepts-Design_Patterns-blue) ![GameDev](https://img.shields.io/badge/Focus-Game_Architecture-green)

## 🎮 Game Overview

The objective is simple: control the paddle to deflect the ball and destroy all bricks without running out of lives. However, **Bricker** introduces complex gameplay mechanics where bricks can possess unique, randomized behaviors.

### Special Brick Behaviors
Unlike standard implementations, bricks in this game are polymorphic. Upon collision, they can trigger various strategies:
* **Double Pucks:** Spawns two additional temporary balls (Pucks) to increase chaos.
* **Extra Paddle:** Creates a temporary secondary paddle in the center of the screen that mimics the user's movements.
* **Exploding Brick:** Triggers a chain reaction, destroying the brick and its immediate neighbors.
* **Extra Life:** Drops a heart power-up that the player must catch to regain a life.
* **Double Behavior:** A "wildcard" brick that executes **two** special behaviors simultaneously (e.g., exploding *and* spawning extra balls).

## 🏗️ Design & Architecture

The core strength of this project lies in its architecture. To handle the complexity of mixing and matching brick behaviors without creating a rigid inheritance tree, I utilized several Design Patterns.

### 1. Strategy Pattern (`CollisionStrategy`)
Instead of hardcoding collision logic inside the `Brick` class, I defined a `CollisionStrategy` interface.
* **Benefit:** This adheres to the **Open/Closed Principle**. I can add new brick behaviors (strategies) without modifying the `Brick` or `GameManager` code.
* **Implementation:** Classes like `BasicCollisionStrategy`, `ExplodingBrickStrategy`, and `ExtraPaddleStrategy` implement this interface.

### 2. Factory Pattern (`CollisionStrategyFactory`)
The generation of brick behaviors is encapsulated in a Factory class.
* **Logic:** The factory manages the probability distribution (1/10 chance for special traits) and recursively builds strategies.
* **Constraint Handling:** It enforces game balance by limiting a single brick to a maximum of 3 combined behaviors.

### 3. Decorator / Composite Pattern (`DoubleBehaviorStrategy`)
To support the "Double Behavior" feature where a brick has multiple effects, I implemented a `DoubleBehaviorStrategy`.
* **Mechanism:** This class holds references to two other `CollisionStrategy` objects. When the `onCollision` method is called, it iterates through its held strategies and executes them sequentially.
* **Result:** This allows for dynamic composition of behaviors at runtime (e.g., an Exploding Extra-Life Brick).

## 📂 Project Structure

```text
src/
├── bricker/
│   ├── main/
│   │   └── BrickerGameManager.java    # Main entry point and game loop
│   ├── gameobjects/
│   │   ├── Ball.java                  #
│   │   ├── Brick.java                 #
│   │   ├── Paddle.java                #
│   │   └── LivesManager.java          # Manages numeric and graphical life counters
│   └── brick_strategies/
│       ├── CollisionStrategy.java     # Strategy Interface
│       ├── BasicCollisionStrategy.java
│       ├── ExplodingBrickStrategy.java
│       ├── ExtraPaddleStrategy.java
│       ├── DoubleBehaviorStrategy.java
│       └── CollisionStrategyFactory.java
└── assets/                            # Images and Sound files
```

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK) 17** or higher.
* **DanoGameLab** library (Game Engine).

### Installation
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Amit-Yeffet/Bricker-Game.git
    ```
2.  **Open the project:**
    Open the root folder in your preferred IDE (IntelliJ IDEA is recommended).
3.  **Dependencies:**
    Ensure the `DanoGameLab` jar is added to your project's build path/libraries.

### Running the Game
Locate the `BrickerGameManager` class in `src/bricker/main/` and run the `main` method.

#### Customizing the Grid
You can run the game with specific grid dimensions by passing command-line arguments. The game accepts two integer arguments:
```bash
java BrickerGameManager <number_of_rows> <number_of_columns>
```

## 🕹️ Controls

| Key | Action |
| :--- | :--- |
| **⬅️ Left Arrow** | Move Paddle Left |
| **➡️ Right Arrow** | Move Paddle Right |
| **W** | **Instant Win Cheat** (Clears all bricks for testing purposes) |