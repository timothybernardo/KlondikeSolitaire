package klondike.mocks;

/**
 * Mock that checks for invalid indices provided by the user.
 */
public class InvalidNumbersMockModel extends LoggingMockModel {
  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    super.movePile(srcPile, numCards, destPile);
    if (srcPile < 0 || srcPile >= 7 || destPile < 0 || destPile >= 7) {
      throw new IllegalArgumentException("Invalid pile index");
    }
    if (numCards < 1) {
      throw new IllegalArgumentException("Invalid number of cards");
    }
  }

  @Override
  public void moveDraw(int destPile) {
    super.moveDraw(destPile);
    if (destPile < 0 || destPile >= 7) {
      throw new IllegalArgumentException("Invalid pile index");
    }
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    super.moveToFoundation(srcPile, foundationPile);
    if (srcPile < 0 || srcPile >= 7 || foundationPile < 0 || foundationPile >= 4) {
      throw new IllegalArgumentException("Invalid index");
    }
  }
}