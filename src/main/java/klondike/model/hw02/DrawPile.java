package klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a draw pile in the Klondike game.
 * The draw pile holds cards that can be drawn and moved to cascade or foundation piles.
 */
public class DrawPile implements Pile {
  private final List<Card> cards;
  private final int numDraw;  // max cards visible

  /**
   * Creates an empty draw pile with specified draw count.
   *
   * @param numDraw the maximum number of cards that can be drawn at once
   * @throws IllegalArgumentException if numDraw is less than or equal to 0
   */
  public DrawPile(int numDraw) {
    if (numDraw <= 0) {
      throw new IllegalArgumentException("Number of draw cards must be positive");
    }
    this.cards = new ArrayList<>();
    this.numDraw = numDraw;
  }

  @Override
  public void addCard(Card card) throws IllegalArgumentException {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    cards.add(card);
  }

  /**
   * Adds multiple cards to the draw pile.
   *
   * @param cardsToAdd the list of cards to add
   * @throws IllegalArgumentException if the list is null or contains null cards
   */
  public void addCards(List<Card> cardsToAdd) throws IllegalArgumentException {
    if (cardsToAdd == null) {
      throw new IllegalArgumentException("Card list cannot be null");
    }
    for (Card card : cardsToAdd) {
      addCard(card);
    }
  }

  /**
   * Removes the first card from the draw pile.
   *
   * @return the removed card from the draw pile
   * @throws IllegalStateException if the pile is empty
   */
  @Override
  public Card removeCard() throws IllegalStateException {
    if (isEmpty()) {
      throw new IllegalStateException("Cannot remove from empty draw pile");
    }
    return cards.removeFirst();  // Remove from top of draw pile
  }

  /**
   * Discards the top card by moving it to the bottom of the draw pile.
   *
   * @throws IllegalStateException if the pile is empty
   */
  public void discardTopCard() throws IllegalStateException {
    if (isEmpty()) {
      throw new IllegalStateException("Cannot discard from empty draw pile");
    }
    Card topCard = cards.removeFirst();
    cards.add(topCard);
  }

  @Override
  public Card getCardAt(int index) throws IllegalArgumentException {
    if (index < 0 || index >= cards.size()) {
      throw new IllegalArgumentException("Invalid index: " + index);
    }
    return cards.get(index);
  }

  /**
   * Gets the top card without removing it.
   *
   * @return the top card
   * @throws IllegalStateException if the pile is empty
   */
  public Card getTopCard() throws IllegalStateException {
    if (isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }
    return cards.getFirst();
  }

  /**
   * Gets the currently visible draw cards.
   * The number of visible cards is limited by numDraw.
   *
   * @return a list of visible draw cards
   */
  public List<Card> getVisibleCards() {
    List<Card> visible = new ArrayList<>();
    int count = Math.min(numDraw, cards.size());
    for (int i = 0; i < count; i++) {
      visible.add(cards.get(i));
    }
    return visible;
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

  /**
   * Gets the maximum number of cards that can be drawn at once.
   *
   * @return the number of draw cards
   */
  public int getNumDraw() {
    return numDraw;
  }
}