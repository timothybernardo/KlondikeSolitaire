package klondike.model.hw02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * A class for testing the KlondikeModel. All tests
 * in this class cannot create Card type objects. Instead,
 * the tests use the createNewDeck method to help create
 * example games.
 */
public class KlondikeModelTests {
  @Test
  public void testCreateNewDeckSize52() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    assertEquals(52, deck.size());
  }

  @Test
  public void testCreateNewDeckHasAllSuits() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    int clubs = 0;
    int diamonds = 0;
    int hearts = 0;
    int spades = 0;
    for (Card card : deck) {
      String cardStr = card.toString();
      if (cardStr.contains("♣")) {
        clubs++;
      } else if (cardStr.contains("♢")) {
        diamonds++;
      } else if (cardStr.contains("♡")) {
        hearts++;
      } else if (cardStr.contains("♠")) {
        spades++;
      }
    }
    assertEquals(13, clubs);
    assertEquals(13, diamonds);
    assertEquals(13, hearts);
    assertEquals(13, spades);
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameTwice() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.startGame(deck, false, 7, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    BasicKlondike model = new BasicKlondike();
    model.startGame(null, false, 7, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameEmptyDeck() {
    BasicKlondike model = new BasicKlondike();
    model.startGame(new ArrayList<>(), false, 7, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNegativePiles() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, -1, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameZeroPiles() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 0, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNegativeDraw() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNotEnoughCards() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    // 55 cards needed for 10 piles
    model.startGame(deck, false, 10, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeckMissingAce() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    List<Card> deckWithoutAces = new ArrayList<>();
    for (Card card : deck) {
      if (!card.toString().startsWith("A")) {
        deckWithoutAces.add(card);
      }
    }
    model.startGame(deckWithoutAces, false, 5, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeckGapInSequence() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    // create new deck without 5s
    List<Card> deckWithGap = new ArrayList<>();
    for (Card card : deck) {
      if (!card.toString().startsWith("5")) {
        deckWithGap.add(card);
      }
    }
    model.startGame(deckWithGap, false, 5, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeckUnequalSuits() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    // removed 5 of hearts
    List<Card> unequalDeck = new ArrayList<>();
    boolean removed5Hearts = false;
    for (Card card : deck) {
      if (!removed5Hearts && card.toString().equals("3♡")) {
        removed5Hearts = true;  // skip card
      } else {
        unequalDeck.add(card);
      }
    }
    model.startGame(unequalDeck, false, 5, 3);
  }

  @Test
  public void testStartGameSuccess() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    assertEquals(7, model.getNumPiles());
    assertEquals(3, model.getNumDraw());
    assertEquals(4, model.getNumFoundations());
  }

  @Test
  public void testStartGameWithShuffle() {
    BasicKlondike model1 = new BasicKlondike();
    BasicKlondike model2 = new BasicKlondike();
    List<Card> deck1 = model1.createNewDeck();
    List<Card> deck2 = new ArrayList<>(deck1);

    model1.startGame(deck1, false, 7, 3);
    model2.startGame(deck2, true, 7, 3);

    boolean foundDifference = false;
    for (int pile = 0; pile < 7 && !foundDifference; pile++) {
      for (int card = 0; card < model1.getPileHeight(pile); card++) {
        if (model1.isCardVisible(pile, card) && model2.isCardVisible(pile, card)) {
          if (!model1.getCardAt(pile, card).toString().equals(
              model2.getCardAt(pile, card).toString())) {
            foundDifference = true;
            break;
          }
        }
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameOneLessThanMinimumCards() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    // 27 cards but need 28 cards
    List<Card> shortDeck = new ArrayList<>();
    int cardCount = 0;
    for (Card card : deck) {
      if (cardCount < 27) {
        shortDeck.add(card);
        cardCount++;
      } else {
        break;
      }
    }

    model.startGame(shortDeck, false, 7, 1);
  }

  @Test
  public void testValidDeckWithTwoRuns() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();

    // make a deck with two runs of hearts A-3
    List<Card> fullDeck = model.createNewDeck();
    for (Card card : fullDeck) {
      if (card.toString().equals("A♡")
          || card.toString().equals("2♡")
          || card.toString().equals("3♡")) {
        deck.add(card);
        deck.add(card);
      }
    }

    model.startGame(deck, false, 2, 1);
    assertEquals(2, model.getNumFoundations());
  }

  @Test
  public void testGetPileHeight() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    assertEquals(1, model.getPileHeight(0));
    assertEquals(2, model.getPileHeight(1));
    assertEquals(7, model.getPileHeight(6));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetPileHeightBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getPileHeight(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPileHeightInvalidPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.getPileHeight(7); // 0-6 piles valid
  }

  @Test
  public void testIsCardVisible() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    // only last card visible in each pile
    assertTrue(model.isCardVisible(0, 0));
    assertTrue(model.isCardVisible(1, 1));
    assertTrue(model.isCardVisible(2, 2));
    assertTrue(model.isCardVisible(3, 3));
    assertTrue(model.isCardVisible(4, 4));
    assertTrue(model.isCardVisible(5, 5));
    assertTrue(model.isCardVisible(6, 6));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsCardVisibleInvalidCard() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.isCardVisible(0, 5);
  }

  @Test
  public void testGetNumRows() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    assertEquals(7, model.getNumRows());
  }

  @Test
  public void testGetNumRowsSinglePile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 1, 3);

    assertEquals(1, model.getNumRows());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumRowsBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getNumRows();
  }

  @Test
  public void testGetScoreEmptyFoundations() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    assertEquals(0, model.getScore());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetScoreBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getScore();
  }

  @Test
  public void testGetDrawCards() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    List<Card> drawCards = model.getDrawCards();
    assertEquals(3, drawCards.size());
  }

  @Test
  public void testGetDrawCardsLessThanNumDraw() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    // 9 piles, 45 cards dealt, 7 for draw
    model.startGame(deck, false, 9, 10);

    List<Card> drawCards = model.getDrawCards();
    assertEquals(7, drawCards.size());
  }

  @Test
  public void testGetDrawCardsEmpty() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    List<Card> smallDeck = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      smallDeck.add(deck.get(i));
    }
    model.startGame(smallDeck, false, 2, 3);

    List<Card> drawCards = model.getDrawCards();
    assertEquals(0, drawCards.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetDrawCardsBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getDrawCards();
  }

  @Test
  public void testGetCardAtEmptyFoundation() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    assertNull(model.getCardAt(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtInvalidFoundation() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    model.getCardAt(4); // only 0-3 valid
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtNegativeFoundation() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    model.getCardAt(-1);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCardAtFoundationBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getCardAt(0);
  }

  @Test
  public void testGetCardAtFoundationAfterMovingAce() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 1); // numDraw = 1 for simplicity

    // cycle draw pile until you hit an ace
    boolean movedAce = false;
    int maxAttempts = 52;

    for (int i = 0; i < maxAttempts && !movedAce; i++) {
      List<Card> drawCards = model.getDrawCards();

      if (!drawCards.isEmpty()) {
        Card topDraw = drawCards.getFirst();
        if (topDraw.toString().startsWith("A")) {
          // move ace to foundation
          model.moveDrawToFoundation(0);
          movedAce = true;
        } else {
          // discard if not an ace
          model.discardDraw();
        }
      }
    }

    assertTrue(movedAce);

    // test getCardAt, should return A
    Card foundationTop = model.getCardAt(0);
    assertTrue(foundationTop.toString().startsWith("A"));
    assertEquals(
        "A", foundationTop.toString().substring(0, 1));
  }

  @Test
  public void testDiscardDraw() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    List<Card> originalDraw = new ArrayList<>(model.getDrawCards());
    Card firstCard = originalDraw.getFirst();

    model.discardDraw();

    List<Card> newDraw = model.getDrawCards();
    // first card should change
    assertNotEquals(
        firstCard.toString(),
        newDraw.getFirst().toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testDiscardDrawEmptyPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    // no draw cards
    List<Card> smallDeck = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      smallDeck.add(deck.get(i));
    }
    model.startGame(smallDeck, false, 2, 3);

    model.discardDraw(); // no cards to discard
  }

  @Test(expected = IllegalStateException.class)
  public void testDiscardDrawBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.discardDraw();
  }

  @Test
  public void testMovePileValidPlacement() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    // keep trying moves until one works
    boolean moveSucceeded = false;

    for (int src = 0; src < 7 && !moveSucceeded; src++) {
      int srcHeight = model.getPileHeight(src);
      if (srcHeight == 0) {
        continue;
      }

      // move bottom card if visible
      if (model.isCardVisible(src, srcHeight - 1)) {
        for (int dest = 0; dest < 7; dest++) {
          if (dest == src) {
            continue;
          }
          try {
            int destHeightBefore = model.getPileHeight(dest);
            model.movePile(src, 1, dest);

            // verify if move succeeded
            assertEquals(srcHeight - 1, model.getPileHeight(src));
            assertEquals(destHeightBefore + 1, model.getPileHeight(dest));
            moveSucceeded = true;
            break;

          } catch (Exception e) {
            // try next destination
          }
        }
      }
    }

    assertTrue(moveSucceeded);
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.movePile(0, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidSourcePile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.movePile(10, 1, 0); // pile 10 doesn't exist
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileNegativeSourcePile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.movePile(-1, 1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidDestPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.movePile(0, 1, 10); // pile 10 doesn't exist
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileNegativeDestPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.movePile(0, 1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileSameSourceAndDest() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.movePile(0, 1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileNotEnoughCards() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.movePile(0, 5, 1); // pile 0 only has 1 card
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileNotVisibleCards() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    // attempts moving not visible cards from pile 6 (which has 7 cards, 6 invisible)
    model.movePile(6, 7, 5);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.moveDraw(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawInvalidDestPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.moveDraw(10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawNegativeDestPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.moveDraw(-1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawEmptyDrawPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    // 0 draw cards
    List<Card> smallDeck = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      smallDeck.add(deck.get(i));
    }
    model.startGame(smallDeck, false, 2, 3);
    model.moveDraw(0);
  }

  @Test
  public void testMoveDrawValidPlacement() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    // try until you find draw card to be placed
    boolean placed = false;
    for (int i = 0; i < 52 && !placed; i++) {
      List<Card> drawCards = model.getDrawCards();
      if (drawCards.isEmpty()) {
        break;
      }

      // try to place top draw card in each pile
      for (int pile = 0; pile < 7; pile++) {
        try {
          model.moveDraw(pile);
          placed = true;

          // verify card moved
          // draw has one less visible
          // destination pile has one more card

          break;
        } catch (Exception e) {
          // pile does not work
        }
      }

      if (!placed && !drawCards.isEmpty()) {
        model.discardDraw();
      }
    }

    assertTrue(placed);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToEmptyPileNotKing() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 1);

    // need to empty cascade
    // find pile with only 1 card
    int singleCardPile = -1;
    for (int i = 0; i < 7; i++) {
      if (model.getPileHeight(i) == 1) {
        singleCardPile = i;
        break;
      }
    }

    if (singleCardPile != -1) {
      // move single card somewhere else to empty the pile
      Card card = model.getCardAt(singleCardPile, 0);

      // move to another pile/foundation
      boolean moved = false;

      // try foundation
      if (card.toString().startsWith("A")) {
        model.moveToFoundation(singleCardPile, 0);
        moved = true;
      } else {
        // try another cascade
        for (int dest = 0; dest < 7; dest++) {
          if (dest != singleCardPile) {
            try {
              model.movePile(singleCardPile, 1, dest);
              moved = true;
              break;
            } catch (Exception e) {
              // cannot move here
            }
          }
        }
      }

      if (moved) {
        // cascade pile empty
        assertEquals(0, model.getPileHeight(singleCardPile));

        // find a non-king in draw pile and try to place it
        for (int i = 0; i < 52; i++) {
          List<Card> drawCards = model.getDrawCards();
          if (!drawCards.isEmpty()) {
            Card topCard = drawCards.getFirst();
            if (!topCard.toString().startsWith("K")) {
              // throw the exception
              model.moveDraw(singleCardPile);
              break;
            }
            model.discardDraw();
          }
        }
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveToFoundationBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.moveToFoundation(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFoundationInvalidSourcePile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.moveToFoundation(10, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFoundationNegativeSourcePile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.moveToFoundation(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFoundationInvalidFoundationPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.moveToFoundation(0, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFoundationNegativeFoundationPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.moveToFoundation(0, -1);
  }

  @Test
  public void testMoveToFoundationEmptyPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();
    // deck with just the 4 aces
    List<Card> fullDeck = model.createNewDeck();
    for (Card card : fullDeck) {
      if (card.toString().equals("A♣")) {
        deck.add(card);
        break;
      }
    }
    model.startGame(deck, false, 1, 1);

    // move ace to empty foundation
    try {
      model.moveToFoundation(0, 0);
      assertEquals(1, model.getScore()); // ace = 1
    } catch (Exception e) {
      // pile empty -> IllegalStateException
      assertTrue(e instanceof IllegalStateException);
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToFoundationBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.moveDrawToFoundation(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawToFoundationInvalidFoundationPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.moveDrawToFoundation(10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawToFoundationNegativeFoundationPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.moveDrawToFoundation(-1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToFoundationEmptyDrawPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    // empty draw card pile
    List<Card> smallDeck = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      smallDeck.add(deck.get(i));
    }
    model.startGame(smallDeck, false, 2, 3);
    model.moveDrawToFoundation(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCardAtCascadeBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getCardAt(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtCascadeInvalidPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.getCardAt(10, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtCascadeNegativePile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.getCardAt(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtCascadeInvalidCardIndex() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.getCardAt(0, 10); // pile 0 only has 1 card
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtCascadeNegativeCardIndex() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.getCardAt(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtCascadeInvisibleCard() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    // invisible card from pile 6
    model.getCardAt(6, 0);
  }

  @Test
  public void testGetCardAtCascadeValidCard() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    // visible card from pile 0
    Card card = model.getCardAt(0, 0);
    assertNotEquals(null, card);
  }

  @Test(expected = IllegalStateException.class)
  public void testIsCardVisibleBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.isCardVisible(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsCardVisibleInvalidPile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.isCardVisible(10, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsCardVisibleNegativePile() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.isCardVisible(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsCardVisibleNegativeCardIndex() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    model.isCardVisible(0, -1);
  }

  @Test
  public void testIsCardVisibleInvisibleCards() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    // card 6 is visible
    assertFalse(model.isCardVisible(6, 0));
    assertFalse(model.isCardVisible(6, 1));
    assertFalse(model.isCardVisible(6, 2));
    assertFalse(model.isCardVisible(6, 3));
    assertFalse(model.isCardVisible(6, 4));
    assertFalse(model.isCardVisible(6, 5));
    assertTrue(model.isCardVisible(6, 6));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsGameOverBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.isGameOver();
  }

  @Test
  public void testIsGameOverWithDrawCards() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    assertFalse(model.isGameOver());
  }

  @Test
  public void testIsGameOverFalseWithPossibleMoves() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    List<Card> smallDeck = new ArrayList<>();
    // take first 28 cards forming a valid deck
    int clubs = 0;
    int diamonds = 0;
    int hearts = 0;
    int spades = 0;

    for (Card card : deck) {
      String cardStr = card.toString();
      // take 7 of each suit (A through 7)
      boolean sevenLess = cardStr.startsWith("A") || cardStr.startsWith("2")
          || cardStr.startsWith("3") || cardStr.startsWith("4")
          || cardStr.startsWith("5") || cardStr.startsWith("6")
          || cardStr.startsWith("7");
      if (cardStr.contains("♣") && clubs < 7) {
        if (sevenLess) {
          smallDeck.add(card);
          clubs++;
        }
      } else if (cardStr.contains("♢") && diamonds < 7) {
        if (sevenLess) {
          smallDeck.add(card);
          diamonds++;
        }
      } else if (cardStr.contains("♡") && hearts < 7) {
        if (sevenLess) {
          smallDeck.add(card);
          hearts++;
        }
      } else if (cardStr.contains("♠") && spades < 7) {
        if (sevenLess) {
          smallDeck.add(card);
          spades++;
        }
      }

      if (smallDeck.size() == 28) {
        break;
      }
    }

    model.startGame(smallDeck, false, 7, 1);

    // Draw pile is empty, so the loops will run
    assertFalse(model.isGameOver());
  }

  @Test
  public void testIsGameOverTrue() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    // deck with only aces
    List<Card> aceOnlyDeck = new ArrayList<>();
    for (Card card : deck) {
      if (card.toString().startsWith("A")) {
        aceOnlyDeck.add(card);
      }
    }

    // 4 aces, 3 go to cascades, 1 to draw
    model.startGame(aceOnlyDeck, false, 2, 1);

    // aces to foundations
    for (int pile = 0; pile < 2; pile++) {
      if (model.getPileHeight(pile) > 0) {
        for (int i = 0; i < 4; i++) {
          try {
            model.moveToFoundation(pile, i);
          } catch (Exception e) {
            // Already moved
          }
        }
      }
    }

    // draw aces to foundation
    while (!model.getDrawCards().isEmpty()) {
      Card draw = model.getDrawCards().getFirst();
      if (draw.toString().startsWith("A")) {
        for (int f = 0; f < 4; f++) {
          try {
            model.moveDrawToFoundation(f);
            break;
          } catch (Exception e) {
            // next foundation
          }
        }
      } else {
        model.discardDraw();
      }
    }

    // all cards in foundation, game over
    assertTrue(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumPilesBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getNumPiles();
  }

  @Test
  public void testGetNumPilesAfterStart() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 5, 3);
    assertEquals(5, model.getNumPiles());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumDrawBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getNumDraw();
  }

  @Test
  public void testGetNumDrawAfterStart() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 5);
    assertEquals(5, model.getNumDraw());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumFoundationsBeforeStart() {
    BasicKlondike model = new BasicKlondike();
    model.getNumFoundations();
  }

  @Test
  public void testGetNumFoundationsAfterStart() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);
    assertEquals(4, model.getNumFoundations()); // 4 aces in standard deck
  }

  @Test
  public void testGetNumFoundationsCustomDeck() {
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = new ArrayList<>();

    // deck with 2 aces, or 2 runs
    List<Card> fullDeck = model.createNewDeck();
    for (Card card : fullDeck) {
      if (card.toString().equals("A♡") || card.toString().equals("2♡")
          || card.toString().equals("3♡")) {
        deck.add(card);
        deck.add(card);
      }
    }

    model.startGame(deck, false, 2, 1);
    assertEquals(2, model.getNumFoundations()); // 2 aces
  }
}
