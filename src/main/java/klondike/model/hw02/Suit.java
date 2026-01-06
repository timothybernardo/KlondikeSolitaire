package klondike.model.hw02;

/**
 * Enum representing the four suits in a standard deck of playing cards.
 */
public enum Suit {
  CLUBS("♣", false),
  DIAMONDS("♢", true),
  HEARTS("♡", true),
  SPADES("♠", false);

  private final String symbol;
  private final boolean isRed;

  /**
   * Constructs a Suit with its symbol and color.
   *
   * @param symbol the symbol for the suit
   * @param isRed  true if the suit is red, false if black
   */
  Suit(String symbol, boolean isRed) {
    this.symbol = symbol;
    this.isRed = isRed;
  }

  /**
   * Gets the symbol representation of the suit.
   *
   * @return the suit symbol
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Checks if the suit is black.
   *
   * @return true if the suit is black (clubs or spades), false otherwise (hearts or diamonds)
   */
  public boolean isBlack() {
    return !isRed;
  }

  @Override
  public String toString() {
    return symbol;
  }
}