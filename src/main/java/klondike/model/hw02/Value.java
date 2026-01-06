package klondike.model.hw02;

/**
 * Enum representing the thirteen values in a standard deck of playing cards.
 */
public enum Value {
  ACE("A", 1),
  TWO("2", 2),
  THREE("3", 3),
  FOUR("4", 4),
  FIVE("5", 5),
  SIX("6", 6),
  SEVEN("7", 7),
  EIGHT("8", 8),
  NINE("9", 9),
  TEN("10", 10),
  JACK("J", 11),
  QUEEN("Q", 12),
  KING("K", 13);

  private final String display;
  private final int numericValue;

  /**
   * Constructs a Value with its display string and numeric value.
   *
   * @param display      the display representation of the value
   * @param numericValue the numeric value (1-13)
   */
  Value(String display, int numericValue) {
    this.display = display;
    this.numericValue = numericValue;
  }

  /**
   * Gets the display representation of the value.
   *
   * @return the value display string
   */
  public String getDisplay() {
    return display;
  }

  /**
   * Gets the numeric value.
   *
   * @return the numeric value (1 for Ace, 2-10 for numbers, 11-13 for face cards)
   */
  public int getNumericValue() {
    return numericValue;
  }

  @Override
  public String toString() {
    return display;
  }
}