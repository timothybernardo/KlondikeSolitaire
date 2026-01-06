package klondike.mocks;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Base mock that only logs commands - used for verifying correct method calls.
 */
public class LoggingMockModel implements KlondikeModel<Card> {
  protected StringBuilder log = new StringBuilder();
  protected boolean gameOver = false;

  @Override
  public List<Card> createNewDeck() {
    return List.of();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    log.append("startGame ").append(shuffle).append(" ")
        .append(numPiles).append(" ").append(numDraw).append("\n");
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    log.append("movePile ").append(srcPile).append(" ")
        .append(numCards).append(" ").append(destPile).append("\n");
  }

  @Override
  public void moveDraw(int destPile) {
    log.append("moveDraw ").append(destPile).append("\n");
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    log.append("moveToFoundation ").append(srcPile).append(" ")
        .append(foundationPile).append("\n");
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    log.append("moveDrawToFoundation ").append(foundationPile).append("\n");
  }

  @Override
  public void discardDraw() {
    log.append("discardDraw\n");
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  public void setGameOver(boolean over) {
    this.gameOver = over;
  }

  public String getLog() {
    return log.toString();
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum) {
    return 5;
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
  public int getNumPiles() {
    return 7;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumFoundations() {
    return 4;
  }

  @Override
  public List<Card> getDrawCards() {
    return new ArrayList<>();
  }
}