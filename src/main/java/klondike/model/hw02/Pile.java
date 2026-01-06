package klondike.model.hw02;

import java.util.List;

/**
 * Interface representing a pile of cards in Klondike Solitaire.
 */
public interface Pile {
  /**
   * Adds a card to the pile.
   *
   * @param card the card to add
   * @throws IllegalArgumentException if the card is null
   * @throws IllegalStateException    if the card cannot be legally added to this pile
   */
  void addCard(Card card) throws IllegalArgumentException, IllegalStateException;

  /**
   * Removes and returns the top card from the pile.
   *
   * @return the removed card
   * @throws IllegalStateException if the pile is empty
   */
  Card removeCard() throws IllegalStateException;

  /**
   * Gets the card at the specified index without removing it.
   *
   * @param index the index of the card
   * @return the card at the index
   * @throws IllegalArgumentException if the index is invalid
   */
  Card getCardAt(int index) throws IllegalArgumentException;

  /**
   * Returns the number of cards in the pile.
   *
   * @return the size of the pile
   */
  int size();

  /**
   * Checks if the pile is empty.
   *
   * @return true if the pile is empty, false otherwise
   */
  boolean isEmpty();

  /**
   * Gets a list of all cards in the pile.
   *
   * @return a list of cards in the pile
   */
  List<Card> getCards();
}