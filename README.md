# 🃏 Klondike Solitaire

A Java implementation of Klondike Solitaire with multiple game variants, built using the Model-View-Controller (MVC) architecture as part of Fundies 2 coursework at Northeastern University.

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)

## 🎮 Game Variants

### Basic Klondike
The classic solitaire rules:
- Cards in cascade piles are dealt face-down (only top card visible)
- Builds must alternate colors (red on black, black on red)
- Only Kings can be placed on empty cascade piles
- Multi-card moves must be valid alternating-color builds

### Whitehead Klondike
A variant with modified rules:
- All cards in cascade piles are dealt face-up
- Builds must be single-colored (red on red, black on black)
- Any card can be placed on empty cascade piles
- Multi-card moves must be same suit (not just same color)

## ✨ Features

- **Multiple Game Variants** — Play Basic or Whitehead Klondike
- **Command-Line Interface** — Configure games via command-line arguments
- **Textual View** — ASCII representation of the game board
- **MVC Architecture** — Clean separation of model, view, and controller
- **Factory Pattern** — Easy instantiation of game variants via KlondikeCreator

## 🛠 Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java |
| Architecture | Model-View-Controller (MVC) |
| Testing | JUnit |
| Build | Gradle |

## 🚀 Usage

### Running in IntelliJ IDEA

1. Open the project in IntelliJ
2. Navigate to `Klondike.java` in the `klondike` package
3. Right-click on the file → **Run 'Klondike.main()'**
4. The first run will fail (no arguments) — this is expected

**To add command-line arguments:**

1. Go to **Run → Edit Configurations**
2. Select the `Klondike` configuration
3. In the **Program arguments** field, enter your arguments:
   - `basic` — Basic Klondike with defaults
   - `basic 7 3` — Basic with 7 piles, 3 draw cards
   - `whitehead` — Whitehead Klondike with defaults
   - `whitehead 8 5` — Whitehead with 8 piles, 5 draw cards
4. Click **Apply** then **Run**

### Command-Line Arguments

| Argument | Description | Default |
|----------|-------------|---------|
| `arg[0]` | Game type: `basic` or `whitehead` | Required |
| `arg[1]` | Number of cascade piles | 7 |
| `arg[2]` | Number of visible draw cards | 3 |

## 🎯 Game Commands

| Command | Description |
|---------|-------------|
| `mpp <src> <numCards> <dest>` | Move cards between cascade piles |
| `md <dest>` | Move draw card to cascade pile |
| `mpf <src> <foundation>` | Move from cascade to foundation |
| `mdf <foundation>` | Move draw card to foundation |
| `dd` | Discard draw card (cycle to next) |
| `q` | Quit game |

## 📁 Project Structure

```
klondike/
├── model/
│   ├── hw02/
│   │   ├── KlondikeModel.java      # Model interface
│   │   ├── BasicKlondike.java      # Basic game implementation
│   │   ├── Card.java               # Card interface
│   │   └── PlayingCard.java        # Card implementation
│   └── hw04/
│       ├── AbstractKlondike.java   # Shared game logic
│       ├── WhiteheadKlondike.java  # Whitehead variant
│       └── KlondikeCreator.java    # Factory class
├── view/
│   ├── TextualView.java            # View interface
│   └── KlondikeTextualView.java    # ASCII game display
├── controller/
│   ├── KlondikeController.java     # Controller interface
│   └── KlondikeTextualController.java  # Handles user input
└── Klondike.java                   # Main entry point
```

## 🏗 Design Highlights

### Abstract Class for Code Reuse
`AbstractKlondike` contains shared logic, with abstract methods for variant-specific rules:
- `shouldCardBeVisible()` — Card visibility rules
- `canStackCards()` — Valid card stacking rules
- `canPlaceOnEmptyPile()` — Empty pile placement rules
- `isValidCardSequence()` — Multi-card move validation

### Factory Pattern
`KlondikeCreator` provides clean instantiation:
```java
KlondikeModel model = KlondikeCreator.create(GameType.BASIC);
KlondikeModel model = KlondikeCreator.create(GameType.WHITEHEAD);
```

## 📊 Scoring

- Each card moved to a foundation pile scores 1 point
- Game is won when all cards are in foundation piles (score = 52)

## 🤝 Contact

**Timothy Bernardo**  
- GitHub: [@timothybernardo](https://github.com/timothybernardo)
- LinkedIn: [timothybernardo](https://www.linkedin.com/in/timothybernardo)
- Email: bernardo.t@northeastern.edu
