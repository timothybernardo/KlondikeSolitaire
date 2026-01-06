package klondike.model.hw02;

/**
 * An interface extending the Card class to provide new methods.
 */
public interface KlondikeCard extends Card {
  /**
   * Returns a string of the suit of the card.
   *
   * @return the suit of the card
   */
  String getSuit();

  /**
   * Returns a string of the value of the card.
   *
   * @return the value of the card
   */
  int getValue();

  /**
   * Returns whether a card is black ("♣" or "♠").
   *
   * @return true if the card is black, false otherwise
   */
  boolean isBlack();
}
