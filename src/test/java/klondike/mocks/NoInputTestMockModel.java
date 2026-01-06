package klondike.mocks;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Mock that never ends game naturally, forcing controller to read input.
 * Used to test when no input is provided.
 */
public class NoInputTestMockModel implements KlondikeModel<Card> {
  private boolean startGameCalled = false;

  @Override
  public List<Card> createNewDeck() {
    return List.of();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    startGameCalled = true;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    throw new AssertionError("Should not reach move commands with no input");
  }

  @Override
  public void moveDraw(int destPile) {
    throw new AssertionError("Should not reach move commands with no input");
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    throw new AssertionError("Should not reach move commands with no input");
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    throw new AssertionError("Should not reach move commands with no input");
  }

  @Override
  public void discardDraw() {
    throw new AssertionError("Should not reach move commands with no input");
  }

  /**
   * Returns false if startGame is not called, true if called.
   *
   * @return false if startGame not set to true in startGame, false otherwise
   */
  public boolean wasStartGameCalled() {
    return startGameCalled;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum) {
    return 0; // empty piles
  }

  @Override
  public int getNumPiles() {
    return 7;
  }

  @Override
  public int getNumFoundations() {
    return 4;
  }

  @Override
  public List<Card> getDrawCards() {
    return new ArrayList<>();
  }

  @Override
  public int getNumDraw() {
    return 3;
  }

  @Override
  public int getNumRows() {
    return 7;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    return true;
  }

  @Override
  public Card getCardAt(int pileNum, int card) {
    return null;
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    return null;
  }

}