package klondike.mocks;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;

/**
 * Mock configured specifically for win condition testing.
 */
public class WinConditionMockModel extends LoggingMockModel {

  @Override
  public int getPileHeight(int pileNum) {
    return 0; // all piles empty
  }

  @Override
  public List<Card> getDrawCards() {
    return new ArrayList<>(); // empty draw pile
  }

  @Override
  public boolean isGameOver() {
    return true; // game over immediately
  }

  @Override
  public int getScore() {
    return 52; // perfect score
  }
}
