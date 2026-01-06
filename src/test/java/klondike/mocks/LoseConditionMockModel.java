package klondike.mocks;

/**
 * Mock for testing non-win game over scenarios.
 */
public class LoseConditionMockModel extends LoggingMockModel {

  @Override
  public int getPileHeight(int pileNum) {
    return 1; //  has cards in piles
  }

  @Override
  public boolean isGameOver() {
    return true;
  }

  @Override
  public int getScore() {
    return 15; // partial score
  }
}