package klondike.model.hw04;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.PlayingCard;
import org.junit.Test;

/**
 * Comprehensive test suite for WhiteheadKlondike implementation.
 */
public class WhiteheadKlondikeTest {

  // INITIALIZE GAME TESTS

  @Test
  public void testGameNotStartedInitially() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    assertThrows(IllegalStateException.class, () -> game.getNumPiles());
    assertThrows(IllegalStateException.class, () -> game.getNumRows());
    assertThrows(IllegalStateException.class, () -> game.getScore());
    assertThrows(IllegalStateException.class, () -> game.isGameOver());
    assertThrows(IllegalStateException.class, () -> game.getDrawCards());
    assertThrows(IllegalStateException.class, () -> game.getNumFoundations());
  }

  @Test
  public void testStartGameValid() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();

    game.startGame(deck, false, 7, 3);

    assertEquals(7, game.getNumPiles());
    assertEquals(3, game.getNumDraw());
    assertEquals(4, game.getNumFoundations());
    assertEquals(0, game.getScore());
    assertFalse(game.isGameOver());
  }

  @Test
  public void testStartGameWithShuffle() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, true, 7, 3);

    assertEquals(7, game.getNumPiles());
    assertEquals(3, game.getNumDraw());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    game.startGame(null, false, 7, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameEmptyDeck() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    game.startGame(new java.util.ArrayList<>(), false, 7, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumPiles() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 0, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumDraw() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, -1);
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameTwice() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    game.startGame(deck, false, 7, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameTooManyPilesForDeck() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 10, 1);
  }

  // VISIBILITY TESTS (UNIQUE TO WHITEHEAD)

  @Test
  public void testAllCardsVisibleInWhitehead() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    for (int pile = 0; pile < 7; pile++) {
      for (int card = 0; card < game.getPileHeight(pile); card++) {
        assertTrue(game.isCardVisible(pile, card));
      }
    }
  }

  // STACKING SAME COLOR TESTS (UNIQUE TO WHITEHEAD)

  @Test
  public void testCanStackSameColorBlackOnBlack() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    boolean foundValidMove = false;
    for (int src = 0; src < 7 && !foundValidMove; src++) {
      if (game.getPileHeight(src) > 0) {
        Card srcCard = game.getCardAt(src, game.getPileHeight(src) - 1);
        if (srcCard instanceof PlayingCard sourceCard && sourceCard.isBlack()) {

          for (int dest = 0; dest < 7; dest++) {
            if (src == dest || game.getPileHeight(dest) == 0) {
              continue;
            }

            Card destCard = game.getCardAt(dest, game.getPileHeight(dest) - 1);
            if (destCard instanceof PlayingCard destinationCard) {
              if (destinationCard.isBlack()
                  && destinationCard.getValue() - sourceCard.getValue() == 1) {
                int destHeightBefore = game.getPileHeight(dest);
                game.movePile(src, 1, dest);
                assertEquals(destHeightBefore + 1, game.getPileHeight(dest));
                foundValidMove = true;
                break;
              }
            }
          }
        }
      }
    }
  }

  @Test
  public void testCanStackSameColorRedOnRed() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    boolean foundValidMove = false;
    for (int src = 0; src < 7 && !foundValidMove; src++) {
      if (game.getPileHeight(src) > 0) {
        Card srcCard = game.getCardAt(src, game.getPileHeight(src) - 1);
        if (srcCard instanceof PlayingCard sourceCard && !sourceCard.isBlack()) {

          for (int dest = 0; dest < 7; dest++) {
            if (src == dest || game.getPileHeight(dest) == 0) {
              continue;
            }

            Card destCard = game.getCardAt(dest, game.getPileHeight(dest) - 1);
            if (destCard instanceof PlayingCard destinationCard) {
              if (!destinationCard.isBlack()
                  && destinationCard.getValue() - sourceCard.getValue() == 1) {
                int destHeightBefore = game.getPileHeight(dest);
                game.movePile(src, 1, dest);
                assertEquals(destHeightBefore + 1, game.getPileHeight(dest));
                foundValidMove = true;
                break;
              }
            }
          }
        }
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testCannotStackDifferentColors() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    for (int src = 0; src < 7; src++) {
      if (game.getPileHeight(src) > 0) {
        Card srcCard = game.getCardAt(src, game.getPileHeight(src) - 1);
        if (srcCard instanceof PlayingCard sourceCard) {

          for (int dest = 0; dest < 7; dest++) {
            if (src == dest || game.getPileHeight(dest) == 0) {
              continue;
            }

            Card destCard = game.getCardAt(dest, game.getPileHeight(dest) - 1);
            if (destCard instanceof PlayingCard destinationCard) {
              if (destinationCard.isBlack() != sourceCard.isBlack()
                  && destinationCard.getValue() - sourceCard.getValue() == 1) {
                game.movePile(src, 1, dest);
                return;
              }
            }
          }
        }
      }
    }
  }

  // EMPTY PILE TESTS (UNIQUE TO WHITEHEAD)

  @Test
  public void testAnyCardCanGoOnEmptyPile() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    int emptyPile = -1;
    for (int pile = 0; pile < 7; pile++) {
      if (game.getPileHeight(pile) == 0) {
        emptyPile = pile;
        break;
      }
    }

    if (emptyPile == -1) {
      int shortestPile = 0;
      int minHeight = game.getPileHeight(0);
      for (int pile = 1; pile < 7; pile++) {
        if (game.getPileHeight(pile) < minHeight && game.getPileHeight(pile) > 0) {
          shortestPile = pile;
          minHeight = game.getPileHeight(pile);
        }
      }

      for (int dest = 0; dest < 7; dest++) {
        if (dest == shortestPile) {
          continue;
        }
        try {
          game.movePile(shortestPile, game.getPileHeight(shortestPile), dest);
          emptyPile = shortestPile;
          break;
        } catch (Exception e) {
          // try next
        }
      }
    }

    if (emptyPile >= 0) {
      for (int src = 0; src < 7; src++) {
        if (src == emptyPile || game.getPileHeight(src) == 0) {
          continue;
        }

        Card srcCard = game.getCardAt(src, game.getPileHeight(src) - 1);
        if (srcCard instanceof PlayingCard) {
          game.movePile(src, 1, emptyPile);
          assertEquals(1, game.getPileHeight(emptyPile));
          return;
        }
      }
    }
  }

  // MOVE PILE TESTS

  @Test
  public void testMoveMultipleCardsSameSuit() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    for (int pile = 0; pile < 7; pile++) {
      if (game.getPileHeight(pile) >= 2) {
        Card bottom = game.getCardAt(pile, game.getPileHeight(pile) - 1);
        Card nextUp = game.getCardAt(pile, game.getPileHeight(pile) - 2);

        if (bottom instanceof PlayingCard bottomCard && nextUp instanceof PlayingCard nextCard) {
          if (bottomCard.getSuit().equals(nextCard.getSuit())
              && nextCard.getValue() - bottomCard.getValue() == 1) {

            for (int dest = 0; dest < 7; dest++) {
              if (dest == pile) {
                continue;
              }
              try {
                game.movePile(pile, 2, dest);
                return;
              } catch (Exception e) {
                // try next
              }
            }
          }
        }
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testCannotMoveMultipleCardsDifferentSuits() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    for (int pile = 0; pile < 7; pile++) {
      if (game.getPileHeight(pile) >= 2) {
        Card bottom = game.getCardAt(pile, game.getPileHeight(pile) - 1);
        Card nextUp = game.getCardAt(pile, game.getPileHeight(pile) - 2);

        if (bottom instanceof PlayingCard bottomPlayingCard
            && nextUp instanceof PlayingCard nextPlayingCard) {
          if (!bottomPlayingCard.getSuit().equals(nextPlayingCard.getSuit())) {
            for (int dest = 0; dest < 7; dest++) {
              if (dest == pile) {
                continue;
              }
              game.movePile(pile, 2, dest);
            }
          }
        }
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidSourcePile() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    game.movePile(-1, 1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileSameSourceAndDest() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    game.movePile(0, 1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileTooManyCards() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    game.movePile(0, 100, 1);
  }

  // MOVE DRAW TESTS

  @Test
  public void testMoveDrawToEmptyPile() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 9, 3);

    int emptyPile = -1;
    for (int pile = 0; pile < 9; pile++) {
      if (game.getPileHeight(pile) == 0) {
        emptyPile = pile;
        break;
      }
    }

    if (emptyPile >= 0 && !game.getDrawCards().isEmpty()) {
      game.moveDraw(emptyPile);
      assertEquals(1, game.getPileHeight(emptyPile));
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawInvalidColor() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    while (!game.getDrawCards().isEmpty()) {
      Card drawCard = game.getDrawCards().getFirst();
      if (drawCard instanceof PlayingCard drawPlayingCard) {

        for (int pile = 0; pile < 7; pile++) {
          if (game.getPileHeight(pile) > 0) {
            Card pileCard = game.getCardAt(pile, game.getPileHeight(pile) - 1);
            if (pileCard instanceof PlayingCard pilePlayingCard) {
              if (pilePlayingCard.isBlack() != drawPlayingCard.isBlack()
                  && pilePlayingCard.getValue() - drawPlayingCard.getValue() == 1) {
                game.moveDraw(pile);
                return;
              }
            }
          }
        }
      }
      game.discardDraw();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawInvalidPile() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    game.moveDraw(-1);
  }

  // FOUNDATION TESTS

  @Test
  public void testMoveToFoundationAce() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    for (int pile = 0; pile < 7; pile++) {
      if (game.getPileHeight(pile) > 0) {
        Card card = game.getCardAt(pile, game.getPileHeight(pile) - 1);
        if (card instanceof PlayingCard && ((PlayingCard) card).getValue() == 1) {
          game.moveToFoundation(pile, 0);
          assertEquals(1, game.getScore());
          return;
        }
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveToFoundationNonAceToEmpty() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    for (int pile = 0; pile < 7; pile++) {
      if (game.getPileHeight(pile) > 0) {
        Card card = game.getCardAt(pile, game.getPileHeight(pile) - 1);
        if (card instanceof PlayingCard && ((PlayingCard) card).getValue() != 1) {
          game.moveToFoundation(pile, 0);
          return;
        }
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFoundationInvalidFoundation() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    game.moveToFoundation(0, 10);
  }

  // GAME STATE TESTS

  @Test
  public void testGetPileHeight() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    assertEquals(1, game.getPileHeight(0));
    assertEquals(2, game.getPileHeight(1));
    assertEquals(3, game.getPileHeight(2));
    assertEquals(4, game.getPileHeight(3));
    assertEquals(5, game.getPileHeight(4));
    assertEquals(6, game.getPileHeight(5));
    assertEquals(7, game.getPileHeight(6));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtInvalidCard() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    game.getCardAt(0, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsCardVisibleInvalidCard() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    game.isCardVisible(0, 100);
  }

  @Test
  public void testGetDrawCards() {
    KlondikeModel<Card> game = new WhiteheadKlondike();
    List<Card> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    List<Card> drawCards = game.getDrawCards();
    assertNotNull(drawCards);
    assertTrue(drawCards.size() <= 3);
  }
}