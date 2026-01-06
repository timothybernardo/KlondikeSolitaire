package klondike;

import java.io.InputStreamReader;
import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw04.KlondikeCreator;

/**
 * Constructs a Klondike game based on command-line arguments specifying the type of game and
 * number of cascade piles and visible draw cards.
 */
public final class Klondike {
  /**
   * Entry point for the Klondike game. Parses through command-line arguments to determine
   * game variant and configuration, then initializes and starts the appropriate game.
   *
   * @param args command-line arguments specifying game configuration
   *             args[0]: required, game type (basic or whitehead)
   *             args[1]: optional, number of cascade piles (default is 7)
   *             args[2]: optional, number of visible draw cards (default is 3)
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("Must specify the Klondike game type: basic or whitehead");
    }
    KlondikeCreator.GameType gameType;
    String gameTypeString = args[0].toLowerCase();

    if (gameTypeString.equals("basic")) {
      gameType = KlondikeCreator.GameType.BASIC;
    } else if (gameTypeString.equals("whitehead")) {
      gameType = KlondikeCreator.GameType.WHITEHEAD;
    } else {
      throw new IllegalArgumentException("Invalid game type: " + args[0] + ". Must be basic "
          + "or whitehead");
    }

    int numPiles = 7;
    int numDraw = 3;
    if (args.length > 1) {
      try {
        numPiles = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        numPiles = 7;
      }
    }
    if (args.length > 2) {
      try {
        numDraw = Integer.parseInt(args[2]);
      } catch (NumberFormatException e) {
        numDraw = 3;
      }
    }
    KlondikeModel<Card> model = KlondikeCreator.create(gameType);
    KlondikeController controller = new KlondikeTextualController(new InputStreamReader(System.in),
        System.out);
    try {
      controller.playGame(model, model.createNewDeck(), true, numPiles, numDraw);
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println("Invalid game configuration: " + e.getMessage());
    }
  }
}
