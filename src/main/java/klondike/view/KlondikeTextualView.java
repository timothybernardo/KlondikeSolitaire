package klondike.view;

import java.io.IOException;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * A textual view for displaying the state of a Klondike Solitaire game.
 * Shows a game board as a string showing draw cards, foundation piles, and cascade piles.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel<?> model;
  private final Appendable log;

  /**
   * Constructs a textual view for the Klondike Model given.
   *
   * @param model the game model to display
   */
  public KlondikeTextualView(KlondikeModel<?> model) {
    this.model = model;
    this.log = null; // no appendable provided
  }

  /**
   * Constructs a textual view with a specified output destination.
   *
   * @param model the game model to display
   * @param log the Appendable to write the view to
   */
  public KlondikeTextualView(KlondikeModel<?> model, Appendable log) {
    this.model = model;
    this.log = log;
  }

  /**
   * Returns the current game state as a string.
   * Returns an empty string if the game has not been started.
   *
   * @return a string representation of the game board
   */
  @Override
  public String toString() {
    try {
      model.getNumPiles();
    } catch (IllegalStateException e) {
      return "";
    }
    StringBuilder gameBoard = new StringBuilder();
    drawLine(gameBoard);
    foundationLine(gameBoard);
    cascadePiles(gameBoard);
    return gameBoard.toString();
  }

  /**
   * Creates a draw pile line to the string builder.
   * Shows visible draw cards, all separated by commas.
   *
   * @param board the string builder to append to, representing the game board
   */
  private void drawLine(StringBuilder board) {
    board.append("Draw: ");
    List<?> drawCards = model.getDrawCards();

    if (!drawCards.isEmpty()) {
      for (int i = 0; i < drawCards.size(); i++) {
        if (i > 0) {
          board.append(", ");
        }
        board.append(drawCards.get(i).toString());
      }
    }
    board.append("\n");
  }

  /**
   * Creates the foundation piles line to the string builder
   * Shows foundation piles, all separated by commas.
   *
   * @param board the string builder to append to, representing the game board
   */
  private void foundationLine(StringBuilder board) {
    board.append("Foundation: ");
    for (int i = 0; i < model.getNumFoundations(); i++) {
      if (i > 0) {
        board.append(", ");
      }
      Card topCard = model.getCardAt(i);
      if (topCard == null) {
        board.append("<none>");
      } else {
        board.append(topCard.toString());
      }
    }
    board.append("\n");
  }

  /**
   * Creates the cascade piles lien to the string builder.
   * Each card position is 3 characters wide with right-alignment.
   *
   * @param board the string builder to append to, representing the game board
   */
  private void cascadePiles(StringBuilder board) {
    int numRows = model.getNumRows();
    int numPiles = model.getNumPiles();

    for (int row = 0; row < numRows; row++) {
      for (int pile = 0; pile < numPiles; pile++) {
        String cardStr = getCardAtPosition(pile, row);
        if (cardStr.length() == 1) {
          board.append("  ").append(cardStr); // 2 spaces + card
        } else if (cardStr.length() == 2) {
          board.append(" ").append(cardStr);  // 1 space + card
        } else {
          board.append(cardStr); // 3 chars
        }
      }
      // newline except for last line
      if (row < numRows - 1) {
        board.append("\n");
      }
    }
  }

  /**
   * Gets the string representation of a card at a specific position
   * Returns "?" for face-down cards, "X" for empty piles, or the card's string value.
   *
   * @param pile the pile index
   * @param row  the row index
   * @return a string representation of the card at the position
   */
  private String getCardAtPosition(int pile, int row) {
    int pileHeight = model.getPileHeight(pile);
    if (pileHeight == 0 && row == 0) {
      return " X"; // show x at row 0 for empty piles!
    }
    if (row >= pileHeight) { // check if position is beyond pile height!
      return "   "; // 3 spaces for empty position
    }
    if (model.isCardVisible(pile, row)) {
      return model.getCardAt(pile, row).toString();
    } else {
      return "?";
    }
  }

  @Override
  public void render() throws IOException {
    if (log != null) {
      log.append(this.toString());
    }
  }
}
