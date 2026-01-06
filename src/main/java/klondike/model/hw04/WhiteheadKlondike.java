package klondike.model.hw04;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.PlayingCard;

/**
 * A basic implementation of Whitehead Solitaire.
 * Manages the game state such as foundation piles, cascade piles, and draw pile.
 */
public class WhiteheadKlondike extends AbstractKlondike {

  /**
   * Constructs a new WhiteheadSolitaire game in an unstarted state.
   * The game must be started by calling startGame().
   */
  public WhiteheadKlondike() {
    this.gameStarted = false;
  }

  /**
   * Returns true as all cards should be visible in Whitehead except for draw.
   *
   * @param row  the row number (0-based)
   * @param pile the pile number (0-based) where the card is being placed
   * @return true always
   */
  @Override
  protected boolean shouldCardBeVisible(int row, int pile) {
    return true; // all cards should be visible in whitehead!
  }

  /**
   * Checks if cards can be placed by comparing if they are the same color and 1-less than
   * the bottom card.
   *
   * @param bottom the card currently at the bottom of the destination pile
   * @param top    the card attempting to be placed on top
   * @return true if the card can be placed, false otherwise
   */
  @Override
  protected boolean canStackCards(Card bottom, Card top) {
    if (!(bottom instanceof PlayingCard bottomCard)
        || !(top instanceof PlayingCard topCard)) {
      return false;
    }
    // check SAME colors
    if (bottomCard.isBlack() != topCard.isBlack()) {
      return false;  // different color, not allowed
    }
    // check if its 1 less than bottom
    return bottomCard.getValue() - topCard.getValue() == 1;
  }

  /**
   * Returns true as all cards can be placed on an empty pile in Whitehead.
   *
   * @param card the card attempting to be placed on an empty cascade pile
   * @return true always
   */
  @Override
  protected boolean canPlaceOnEmptyPile(Card card) {
    return true; // ANY card can go on an empty pile in Whitehead
  }

  /**
   * Returns true if sequence is valid.
   * This is if a single card or empty is placed, or if a card is descending by 1, same suit,
   * and same color.
   *
   * @param cards the list of cards to validate, in order from top to bottom of the pile
   * @return true if valid card sequence, false otherwise
   */
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

      // same suit (also ensures same color)
      if (!(prevCard.getSuit().equals(currCard.getSuit()))) {
        return false;
      }
    }
    return true;
  }
}
