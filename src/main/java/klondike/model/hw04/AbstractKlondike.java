package klondike.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.CascadePile;
import klondike.model.hw02.DrawPile;
import klondike.model.hw02.FoundationPile;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.PlayingCard;
import klondike.model.hw02.Suit;
import klondike.model.hw02.Value;

/**
 * An abstract class representing a basic version of Klondike.
 * Allows modification to rules for how cards are stacked on piles.
 */
public abstract class AbstractKlondike implements KlondikeModel<Card> {
  protected List<CascadePile> cascadePiles;
  protected List<FoundationPile> foundationPiles;
  protected DrawPile drawPile;
  protected boolean gameStarted;

  /**
   * Determines whether a card at a specific position should be visible when dealing.
   *
   * @param row  the row number (0-based)
   * @param pile the pile number (0-based) where the card is being placed
   * @return true if the card should be face-up, false if face-down
   */
  protected abstract boolean shouldCardBeVisible(int row, int pile);

  /**
   * Determines if a card can be legally stacked on top of another card in cascade piles.
   *
   * @param bottom the card currently at the bottom of the destination pile
   * @param top    the card attempting to be placed on top
   * @return true if the top card can be legally placed on the bottom card, false otherwise
   */
  protected abstract boolean canStackCards(Card bottom, Card top);

  /**
   * Determines if a card can be placed on an empty cascade pile.
   *
   * @param card the card attempting to be placed on an empty cascade pile
   * @return true if the card can be placed on an empty pile, false otherwise
   */
  protected abstract boolean canPlaceOnEmptyPile(Card card);

  /**
   * Determines whether a sequence of cards forms a valid moveable group.
   *
   * @param cards the list of cards to validate, in order from top to bottom of the pile
   * @return true if the cards form a valid sequence that can be moved together, false otherwise
   */
  protected abstract boolean isValidCardSequence(List<Card> cards);

  @Override
  public List<Card> createNewDeck() {
    List<Card> deck = new ArrayList<>();
    for (Suit suit : Suit.values()) {
      for (Value value : Value.values()) {
        deck.add(new PlayingCard(value, suit));
      }
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException {
    if (gameStarted) {
      throw new IllegalStateException("Game has already started");
    }
    validParameters(deck, numPiles, numDraw);
    if (!isValidDeck(deck)) {
      throw new IllegalArgumentException("Runs are not valid, so the deck is invalid");
    }

    int cardsNeeded = (numPiles * (numPiles + 1)) / 2;  // 1 + 2 + ... + n = n(n+1)/2
    if (deck.size() < cardsNeeded) {
      throw new IllegalArgumentException("Not enough cards to deal");
    }

    List<Card> checkedDeck = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(checkedDeck);
    }
    this.cascadePiles = new ArrayList<>();
    this.foundationPiles = new ArrayList<>();
    this.drawPile = new DrawPile(numDraw);

    dealCascadePiles(checkedDeck, numPiles);
    setupFoundationPiles(deck);

    // adds remaining cards to draw pile
    for (Card card : checkedDeck) {
      drawPile.addCard(card);
    }

    this.gameStarted = true;
  }

  /**
   * Throws an IllegalArgumentException if the deck is null, the deck is empty, the piles are
   * negative, or the draw cards are negative.
   *
   * @param deck     the deck of cards to be dealt
   * @param numPiles the number of cascade piles
   * @param numDraw  the maximum number of draw cards available at a time
   */
  private void validParameters(List<Card> deck, int numPiles, int numDraw) {
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null");
    }
    if (deck.isEmpty()) {
      throw new IllegalArgumentException("Deck cannot be empty");
    }
    if (numPiles <= 0) {
      throw new IllegalArgumentException("Piles cannot be negative or zero");
    }
    if (numDraw <= 0) {
      throw new IllegalArgumentException("Draw cards cannot be negative or zero");
    }
  }

  /**
   * Checks if a deck is valid by organizing cards into their suits.
   * Calls checkAllSuits for further validation.
   * Returns true if valid, false otherwise
   *
   * @param deck the deck of cards to be dealt
   * @return true if the deck is valid, false otherwise
   */
  private boolean isValidDeck(List<Card> deck) {
    for (Card card : deck) {
      if (card == null) {
        return false;  // if card is null, return false
      }
    }
    // group cards by suit
    List<List<Card>> suitGroups = new ArrayList<>();
    for (Suit suit : Suit.values()) {
      List<Card> suitCards = new ArrayList<>();
      for (Card card : deck) {
        if (card instanceof PlayingCard pc && pc.getSuitEnum() == suit) {
          suitCards.add(card);
        }
      }
      if (!suitCards.isEmpty()) {
        suitGroups.add(suitCards);
      }
    }
    if (suitGroups.isEmpty()) {
      return false;
    }
    // all suits have same number of cards
    int expectedSize = suitGroups.getFirst().size();
    for (List<Card> suitCards : suitGroups) {
      if (suitCards.size() != expectedSize) {
        return false;
      }
      if (!hasValidRunsForSuit(suitCards)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if cards of the same suit have a valid run. Confirms that every list of cards has
   * an ace, discovers how long the run ends, and if there are any gaps in the run. Returns
   * false if there is no ace in the run, not all values are appear the same amount of times,
   * or if there are gaps in the run. Returns true if all criteria met.
   *
   * @param suitCards a list of cards of the same suit
   * @return true if the run is valid, false otherwise
   */
  private boolean hasValidRunsForSuit(List<Card> suitCards) {
    // count number of each value we have
    int[] valueCounts = new int[14];  // index 0 unused, 1-13 for card values
    for (Card card : suitCards) {
      int value = ((KlondikeCard) card).getValue();
      valueCounts[value]++;
    }
    // should have at least one ace
    if (valueCounts[1] == 0) {
      return false;
    }
    // number of runs equals the number of aces
    int numRuns = valueCounts[1];
    // finds out when the run ends
    int runLength = 0;
    for (int value = 1; value <= 13; value++) {
      if (valueCounts[value] == numRuns) {
        runLength++;  // adds on to the run
      } else if (valueCounts[value] == 0) {
        break;  // run ended
      } else {
        return false;  // not all values appear the same amount of times
      }
    }
    // checks out if there are any gaps in the run
    for (int value = runLength + 1; value <= 13; value++) {
      if (valueCounts[value] != 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Deals the cascade piles by creating the number of piles specified as an argument.
   * Creates triangle formation by ensuring each pile gets cards equal to its position
   * (pile 1 gets 1 card, pile 2 gets 2...). Only the bottom card of each pile is face-up.
   *
   * @param deck     the deck to be dealt in cascade piles
   * @param numPiles the number of cascade piles
   */
  protected void dealCascadePiles(List<Card> deck, int numPiles) {
    for (int i = 0; i < numPiles; i++) {
      cascadePiles.add(new CascadePile());
    }

    int cardIndex = 0;

    // row determines where to put cards horizontally
    for (int row = 0; row < numPiles; row++) { // which row we are dealing
      for (int pile = row; pile < numPiles; pile++) { // which piles get cards this row
        if (cardIndex >= deck.size()) {
          break;
        }
        Card card = deck.get(cardIndex);
        boolean visible = shouldCardBeVisible(row, pile);
        cascadePiles.get(pile).addCard(card, visible);
        cardIndex++;
      }
    }

    // remove cards dealt from deck
    for (int i = 0; i < cardIndex; i++) {
      deck.removeFirst();
    }
  }

  /**
   * Creates the foundation piles by counting the number of aces existent in the deck, and creating
   * an empty foundation pile for each ace.
   *
   * @param deck the deck to be dealt into foundation piles
   */
  private void setupFoundationPiles(List<Card> deck) {
    int numAces = 0;
    for (Card card : deck) {
      if (card instanceof PlayingCard pc && pc.getValueEnum() == Value.ACE) {
        numAces++;
      }
    }

    for (int i = 0; i < numAces; i++) {
      foundationPiles.add(new FoundationPile());
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException {
    isGameNotStarted();
    if (srcPile < 0 || srcPile >= cascadePiles.size()) {
      throw new IllegalArgumentException("Source pile out of bounds");
    }
    if (destPile < 0 || destPile >= cascadePiles.size()) {
      throw new IllegalArgumentException("Destination pile out of bounds");
    }
    if (srcPile == destPile) {
      throw new IllegalArgumentException("Source pile cannot be the same as destination pile");
    }
    CascadePile source = cascadePiles.get(srcPile);
    if (source.size() < numCards) {
      throw new IllegalArgumentException("Source pile does not have enough cards to move");
    }
    int startPos = source.size() - numCards;
    for (int i = startPos; i < source.size(); i++) {
      if (!source.isCardVisible(i)) {
        throw new IllegalArgumentException("All cards to move must be visible");
      }
    }
    List<Card> cardsToCheck = new ArrayList<>();
    for (int i = startPos; i < source.size(); i++) {
      cardsToCheck.add(source.getCardAt(i));
    }
    if (!isValidCardSequence(cardsToCheck)) {
      throw new IllegalStateException("Cards do not form a valid sequence for this game variant");
    }

    // validate move
    CascadePile dest = cascadePiles.get(destPile);
    Card topCardToMove = source.getCardAt(startPos);
    validateCascadeMove(topCardToMove, dest);

    // Perform move
    List<Card> cardsToMove = new ArrayList<>();
    for (int i = 0; i < numCards; i++) {
      cardsToMove.addFirst(source.removeCard());
    }

    for (Card card : cardsToMove) {
      dest.addCard(card, true);
    }
  }

  /**
   * Validates the cascade pile for placing a card group.
   * Cascade piles are built down in alternating colors (red on black, black on red).
   * Empty cascade piles can only accept Kings.
   */
  protected void validateCascadeMove(Card cardToMove, CascadePile dest) {
    if (!(cardToMove instanceof PlayingCard movingCard)) {
      throw new IllegalStateException("Invalid card type");
    }

    if (dest.isEmpty()) {
      if (!canPlaceOnEmptyPile(movingCard)) {
        throw new IllegalStateException("This card cannot be placed on empty pile");
      }
    } else {
      Card destBottom = dest.getBottomVisibleCard();
      if (!(destBottom instanceof PlayingCard destCard)) {
        throw new IllegalStateException("Invalid card type");
      }
      if (!canStackCards(destBottom, movingCard)) {
        throw new IllegalStateException("This card cannot be placed here");
      }
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    isGameNotStarted();
    if (destPile < 0 || destPile >= cascadePiles.size()) {
      throw new IllegalArgumentException("Destination pile out of bounds");
    }
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }
    Card drawCard = drawPile.getTopCard();
    CascadePile dest = cascadePiles.get(destPile);

    validateCascadeMove(drawCard, dest);

    drawPile.removeCard();
    dest.addCard(drawCard, true);
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    isGameNotStarted();
    if (srcPile < 0 || srcPile >= cascadePiles.size()) {
      throw new IllegalArgumentException("Source pile out of bounds");
    }
    validateFoundationPileIndex(foundationPile);
    CascadePile pile = cascadePiles.get(srcPile);
    if (pile.isEmpty()) {
      throw new IllegalStateException("Source pile is empty");
    }
    if (!pile.isCardVisible(pile.size() - 1)) {
      throw new IllegalStateException("No visible cards in source pile");
    }
    Card bottomCard = pile.getCardAt(pile.size() - 1);
    FoundationPile foundation = foundationPiles.get(foundationPile);

    if (!foundation.canAddCard(bottomCard)) {
      if (foundation.isEmpty()) {
        throw new IllegalStateException("Only Aces can be placed on empty foundations");
      } else {
        throw new IllegalStateException("Card must be same suit and one value higher");
      }
    }

    pile.removeCard();
    foundation.addCard(bottomCard);
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    isGameNotStarted();
    validateFoundationPileIndex(foundationPile);
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty");
    }
    Card drawCard = drawPile.getTopCard();
    FoundationPile foundation = foundationPiles.get(foundationPile);
    if (!foundation.canAddCard(drawCard)) {
      if (foundation.isEmpty()) {
        throw new IllegalStateException("Only Aces can be placed on empty foundations");
      } else {
        throw new IllegalStateException("Card must be same suit and one value higher");
      }
    }

    drawPile.removeCard();
    foundation.addCard(drawCard);
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    isGameNotStarted();
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("No draw cards to discard");
    }
    drawPile.discardTopCard();
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    isGameNotStarted();
    int maxHeight = 0;
    for (CascadePile pile : cascadePiles) {
      maxHeight = Math.max(maxHeight, pile.size());
    }
    return maxHeight;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    isGameNotStarted();
    return cascadePiles.size();
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    isGameNotStarted();
    return drawPile.getNumDraw();
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    isGameNotStarted();
    if (!drawPile.isEmpty()) {
      return false;
    }
    for (int cascadePile = 0; cascadePile < cascadePiles.size(); cascadePile++) {
      if (canMoveToAnyFoundation(cascadePile)) {
        return false;
      }
    }
    for (int cascadePile = 0; cascadePile < cascadePiles.size(); cascadePile++) {
      if (canMoveBetweenCascades(cascadePile)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the bottom card of a cascade pile can move to any foundation.
   *
   * @param pileNum the index of the cascade pile to check
   * @return true if the pile's bottom card can move to a foundation, false otherwise
   */
  private boolean canMoveToAnyFoundation(int pileNum) {
    CascadePile pile = cascadePiles.get(pileNum);
    if (pile.isEmpty()) {
      return false;
    }

    // check if bottom card is visible
    int lastIndex = pile.size() - 1;
    if (!pile.isCardVisible(lastIndex)) {
      return false;
    }

    Card bottomCard = pile.getCardAt(lastIndex);

    // for every foundation...
    for (FoundationPile foundation : foundationPiles) {
      if (foundation.canAddCard(bottomCard)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if any visible cards from a cascade pile can move to another cascade pile.
   *
   * @param pileNum the index of a source cascade pile to check
   * @return true if any cards can move to another cascade pile, false otherwise
   */
  private boolean canMoveBetweenCascades(int pileNum) {
    CascadePile source = cascadePiles.get(pileNum);
    if (source.isEmpty()) {
      return false;
    }
    int firstVisible = -1;
    for (int i = 0; i < source.size(); i++) {
      if (source.isCardVisible(i)) {
        firstVisible = i; // finds first visible card in the source cascade pile
        break;
      }
    }
    if (firstVisible == -1) {
      return false; // no visible cards in the pile
    } // check each possible group of cards that could be moved
    for (int startPos = firstVisible; startPos < source.size(); startPos++) {
      Card topCard = source.getCardAt(startPos);  // get top card of the group desired to move
      if (!(topCard instanceof PlayingCard card)) {
        continue;
      }
      for (int dest = 0; dest < cascadePiles.size(); dest++) {
        if (pileNum == dest) {  // cannot move cards to same pile
          continue;
        } // check for every cascade pile as a potential destination
        CascadePile destPile = cascadePiles.get(dest);
        if (destPile.isEmpty()) { // case 1: destination pile is empty
          return canPlaceOnEmptyPile(card);
        } else {  // case 2: destination pile has cards
          int destLastIndex = destPile.size() - 1; // gets bottom card of destination pile
          Card destBottom = destPile.getCardAt(destLastIndex);
          // cards must be opposite colors, source card must be one value lower than destination
          if (destBottom instanceof PlayingCard destCard) {
            // Cards must alternate colors and be one value lower
            if (canStackCards(destCard, card)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    isGameNotStarted();
    int score = 0;
    for (FoundationPile foundation : foundationPiles) {
      Card topCard = foundation.getTopCard();
      if (topCard instanceof PlayingCard pc) {
        score += pc.getValue();
      }
    }
    return score;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    isGameNotStarted();

    validateCascadePileIndex(pileNum);

    return cascadePiles.get(pileNum).size();
  }

  @Override
  public Card getCardAt(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    isGameNotStarted();
    validateCascadePileIndex(pileNum);
    CascadePile pile = cascadePiles.get(pileNum);
    if (card < 0 || card >= pile.size()) {
      throw new IllegalArgumentException("Invalid card index: " + card);
    }
    if (!pile.isCardVisible(card)) {
      throw new IllegalArgumentException("Card at position " + card + " is not visible");
    }
    return pile.getCardAt(card);
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    isGameNotStarted();
    validateFoundationPileIndex(foundationPile);
    return foundationPiles.get(foundationPile).getTopCard();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    isGameNotStarted();
    validateCascadePileIndex(pileNum);
    CascadePile pile = cascadePiles.get(pileNum);
    if (card < 0 || card >= pile.size()) {
      throw new IllegalArgumentException("Invalid card index: " + card);
    }
    return pile.isCardVisible(card);
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    isGameNotStarted();
    return drawPile.getVisibleCards();
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    isGameNotStarted();
    return foundationPiles.size();
  }

  /**
   * Validates that the game has been started.
   *
   * @throws IllegalStateException if the game has not been started
   */
  private void isGameNotStarted() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
  }

  /**
   * Validates that a cascade pile index is within bounds.
   *
   * @param pileNum the cascade pile index to validate
   * @throws IllegalArgumentException if the cascade pile index is out of bounds
   */
  private void validateCascadePileIndex(int pileNum) throws IllegalArgumentException {
    if (pileNum < 0 || pileNum >= cascadePiles.size()) {
      throw new IllegalArgumentException("Invalid pile number: " + pileNum);
    }
  }

  /**
   * Validates that a foundation pile index is within bounds.
   *
   * @param foundationPile the index of the foundation pile index to validate
   * @throws IllegalArgumentException if the foundation pile index is out of bounds
   */
  private void validateFoundationPileIndex(int foundationPile) throws IllegalArgumentException {
    if (foundationPile < 0 || foundationPile >= foundationPiles.size()) {
      throw new IllegalArgumentException("Foundation pile out of bounds");
    }
  }

}
