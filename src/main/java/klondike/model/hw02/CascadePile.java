package klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a cascade pile in the Klondike game.
 * Manages cards and if visible in the cascade pile.
 */
public class CascadePile implements Pile {
  private final List<Card> cards;
  private final List<Boolean> isVisible;

  /**
   * Creates an empty cascade pile.
   */
  public CascadePile() {
    this.cards = new ArrayList<>();
    this.isVisible = new ArrayList<>();
  }

  /**
   * Adds a card to the bottom of the cascade pile.
   * Adds whether the card is visible or not to a visibility array that is parallel
   * to the cascade pile.
   *
   * @param card the card that is added to the cascade pile
   */
  @Override
  public void addCard(Card card) throws IllegalArgumentException {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    addCard(card, true);
  }

  /**
   * Adds a card to the cascade pile with specified visibility.
   *
   * @param card    the card to add
   * @param visible true if the card should be face-up, false if face-down
   * @throws IllegalArgumentException if the card is null
   */
  public void addCard(Card card, boolean visible) throws IllegalArgumentException {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    cards.add(card);
    isVisible.add(visible);
  }

  /**
   * Removes card from the bottom of the pile. If removed the last visible card and there are still
   * cards left, make the new bottom card visible. Returns removed card from pile.
   *
   * @return removed card
   * @throws IllegalStateException if the pile is empty
   */
  @Override
  public Card removeCard() throws IllegalStateException {
    if (isEmpty()) {
      throw new IllegalStateException("Cannot remove from empty pile");
    }
    Card removed = cards.removeLast();
    isVisible.removeLast();

    if (!isEmpty() && !hasVisibleCards()) {
      isVisible.set(isVisible.size() - 1, true);
    }

    return removed;
  }

  /**
   * Removes the specified number of cards from the bottom of the pile
   * Flips the new bottom card, if exists, if all visible cards are removed.
   *
   * @param numCards the number of cards to remove
   * @throws IllegalArgumentException if numCards exceeds pile size
   */
  public List<Card> removeCards(int numCards) throws IllegalArgumentException {
    if (numCards < 0) {
      throw new IllegalArgumentException("Number of cards cannot be negative");
    }
    if (numCards > size()) {
      throw new IllegalArgumentException("Cannot remove " + numCards
          + " cards from pile with only " + size() + " cards");
    }

    List<Card> removed = new ArrayList<>();
    for (int i = 0; i < numCards; i++) {
      removed.addFirst(removeCard());
    }
    return removed;
  }

  @Override
  public Card getCardAt(int index) throws IllegalArgumentException {
    if (index < 0 || index >= cards.size()) {
      throw new IllegalArgumentException("Invalid index: " + index);
    }
    return cards.get(index);
  }

  /**
   * Checks if the card at the specified index is visible.
   *
   * @param index the index of the card
   * @return true if the card is visible, false otherwise
   * @throws IllegalArgumentException if the index is invalid
   */
  public boolean isCardVisible(int index) throws IllegalArgumentException {
    if (index < 0 || index >= isVisible.size()) {
      throw new IllegalArgumentException("Invalid index: " + index);
    }
    return isVisible.get(index);
  }

  /**
   * Gets the bottommost visible card in the pile.
   *
   * @return the bottommost visible card
   * @throws IllegalStateException if there are no visible cards
   */
  public Card getBottomVisibleCard() throws IllegalStateException {
    for (int i = cards.size() - 1; i >= 0; i--) {
      if (isVisible.get(i)) {
        return cards.get(i);
      }
    }
    throw new IllegalStateException("No visible cards in pile");
  }

  /**
   * Checks if the pile has any visible cards.
   *
   * @return true if there are visible cards, false otherwise
   */
  private boolean hasVisibleCards() {
    for (Boolean isVisible : isVisible) {
      if (isVisible) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets a list of visible cards in the pile.
   *
   * @return a list of visible cards
   */
  public List<Card> getVisibleCards() {
    List<Card> visibleCards = new ArrayList<>();
    for (int i = 0; i < cards.size(); i++) {
      if (isVisible.get(i)) {
        visibleCards.add(cards.get(i));
      }
    }
    return visibleCards;
  }

  @Override
  public int size() {
    return cards.size();
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  @Override
  public List<Card> getCards() {
    return new ArrayList<>(cards);
  }
}


