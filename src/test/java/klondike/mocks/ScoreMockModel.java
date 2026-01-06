package klondike.mocks;

/**
 * Mock focused on score tracking for quit scenarios.
 */
public class ScoreMockModel extends LoggingMockModel {
  private int score;

  /**
   * Creates a score mock model with a user-inputted score.
   *
   * @param score the inputted score
   */
  public ScoreMockModel(int score) {
    this.score = score;
  }

  @Override
  public int getScore() {
    return score;
  }

}