package klondike.model.hw02;

import java.util.Objects;

/**
 * A class representing a standard playing card. A card consists of a value (A, 2-10, J, Q, K)
 * and a suit (♣, ♢, ♡, ♠).
 */
public class PlayingCard implements KlondikeCard {
  private final Value value;
  private final Suit suit;

  /**
   * Constructs a playing card with the given value and suit.
   *
   * @param value the value of the card (A, 2-10, J, Q, or K)
   * @param suit  the suit of the card (♣, ♢, ♡, ♠)
   * @throws IllegalArgumentException if value or suit is null
   */
  public PlayingCard(Value value, Suit suit) {
    if (value == null) {
      throw new IllegalArgumentException("Card value cannot be null");
    }
    if (suit == null) {
      throw new IllegalArgumentException("Card suit cannot be null");
    }
    this.value = value;
    this.suit = suit;
  }

  /**
   * Determines whether this playing card is equal to another object.
   * Two cards are equal if they have the same value and suit.
   *
   * @param o the object to compare this object with
   * @return true if the cards are equal to each other, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayingCard)) {
      return false;
    }
    PlayingCard that = (PlayingCard) o;
    return this.value == that.value && this.suit == that.suit;
  }

  /**
   * Returns a hash code value for this card.
   *
   * @return a hash code for this card
   */
  @Override
  public int hashCode() {
    return Objects.hash(value, suit);
  }

  /**
   * Returns a string representation of this card.
   *
   * @return the card formatted as value followed by suit
   */
  @Override
  public String toString() {
    return value.getDisplay() + suit.getSymbol();
  }

  @Override
  public String getSuit() {
    return suit.getSymbol();
  }

  @Override
  public int getValue() {
    return value.getNumericValue();
  }

  @Override
  public boolean isBlack() {
    return suit.isBlack();
  }

  /**
   * Gets the Suit enum of this card.
   *
   * @return the suit enum
   */
  public Suit getSuitEnum() {
    return suit;
  }

  /**
   * Gets the Value enum of this card.
   *
   * @return the value enum
   */
  public Value getValueEnum() {
    return value;
  }

}
