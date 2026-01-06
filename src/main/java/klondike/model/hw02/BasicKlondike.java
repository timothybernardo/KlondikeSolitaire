package klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import klondike.model.hw04.AbstractKlondike;

/**
 * A basic implementation of Klondike Solitaire.
 * Manages the game state such as foundation piles, cascade piles, and draw pile.
 */
public class BasicKlondike extends AbstractKlondike {

  /**
   * Constructs a new BasicKlondike game in an unstarted state.
   * The game must be started by calling startGame().
   */
  public BasicKlondike() {
    this.gameStarted = false;
  }

  @Override
  protected boolean shouldCardBeVisible(int row, int pile) {
    return row == pile; // last card in each pile is visible
  }

  @Override
  protected boolean canStackCards(Card bottom, Card top) {
    if (!(bottom instanceof PlayingCard bottomCard)
        || !(top instanceof PlayingCard topCard)) {
      return false;
    }
    // check opposite colors
    if (bottomCard.isBlack() == topCard.isBlack()) {
      return false;  // same color, not allowed
    }
    // check if its 1 less than bottom
    return bottomCard.getValue() - topCard.getValue() == 1;
  }

  @Override
  protected boolean canPlaceOnEmptyPile(Card card) {
    if (card instanceof PlayingCard) {
      return ((PlayingCard) card).getValue() == 13;
    }
    return false;
  }

  @Override
  protected boolean isValidCardSequence(List<Card> cards) {
    if (cards.size() <= 1) {
      return true;  // single card or empty is valid!
    }
    for (int i = 1; i < cards.size(); i++) {
      Card prev = cards.get(i - 1);
      Card curr = cards.get(i);

      if (!(prev instanceof PlayingCard prevCard)
          || !(curr instanceof PlayingCard currCard)) {
        return false;
      }
      // descending by 1
      if (prevCard.getValue() - currCard.getValue() != 1) {
        return false;
      }

      // alternating colors
      if (prevCard.isBlack() == currCard.isBlack()) {
        return false;
      }
    }
    return true;
  }
}
