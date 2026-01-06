package klondike.mocks;

/**
 * Mock that always fails on move operations.
 * Used to test controller's error handling when moves are invalid.
 */
public class AlwaysFailingMoveMock extends LoggingMockModel {

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    super.movePile(srcPile, numCards, destPile); // Log first
    throw new IllegalStateException("Invalid move");
  }

  @Override
  public void moveDraw(int destPile) {
    super.moveDraw(destPile); // Log first
    throw new IllegalStateException("Invalid move");
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    super.moveToFoundation(srcPile, foundationPile); // Log first
    throw new IllegalStateException("Invalid move");
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    super.moveDrawToFoundation(foundationPile); // Log first
    throw new IllegalStateException("Invalid move");
  }

  @Override
  public void discardDraw() {
    super.discardDraw(); // Log first
    throw new IllegalStateException("Invalid move");
  }
}