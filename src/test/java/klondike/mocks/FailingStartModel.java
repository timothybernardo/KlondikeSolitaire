package klondike.mocks;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Mock of a KlondikeModel that fails to start.
 */
public class FailingStartModel implements KlondikeModel<Card> {
  private boolean shouldFailWithIllegalArgument;

  /**
   * Creates a KlondikeModel with a boolean.
   *
   * @param failWithIllegalArgument a boolean that tracks failure when illegal arguments are passed
   */
  public FailingStartModel(boolean failWithIllegalArgument) {
    this.shouldFailWithIllegalArgument = failWithIllegalArgument;
  }

  

  @Override
  public List<Card> createNewDeck() {
    return List.of();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    if (shouldFailWithIllegalArgument) {
      throw new IllegalArgumentException("Invalid game parameters");
    } else {
      throw new IllegalStateException("Cannot initialize game");
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException {

  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {

  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
      throws IllegalArgumentException, IllegalStateException {

  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {

  }

  @Override
  public void discardDraw() throws IllegalStateException {

  }

  @Override
  public int getNumRows() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return true;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public Card getCardAt(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    return false;
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    return List.of();
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    return 0;
  }
}