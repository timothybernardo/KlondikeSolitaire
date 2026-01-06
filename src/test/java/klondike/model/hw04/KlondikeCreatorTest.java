package klondike.model.hw04;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import org.junit.Test;

/**
 * Test class for KlondikeCreator factory.
 */
public class KlondikeCreatorTest {

  @Test
  public void testCreateBasicKlondike() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    assertNotNull(model);
    assertTrue(model instanceof BasicKlondike);
  }

  @Test
  public void testCreateWhiteheadKlondike() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    assertNotNull(model);
    assertTrue(model instanceof WhiteheadKlondike);
  }

  @Test
  public void testBasicKlondikeFromFactory() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    // test that bottom cards should be visible in BasicKlondike
    for (int pile = 0; pile < 7; pile++) {
      int height = model.getPileHeight(pile);
      for (int card = 0; card < height - 1; card++) {
        assertTrue(!model.isCardVisible(pile, card));
      }
      if (height > 0) {
        assertTrue(model.isCardVisible(pile, height - 1));
      }
    }
  }

  @Test
  public void testWhiteheadKlondikeFromFactory() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 7, 3);

    // all cards should be visible in Whitehead
    for (int pile = 0; pile < 7; pile++) {
      int height = model.getPileHeight(pile);
      for (int card = 0; card < height; card++) {
        assertTrue(model.isCardVisible(pile, card));
      }
    }
  }

  @Test(expected = NullPointerException.class)
  public void testCreateWithNull() {
    KlondikeCreator.create(null);
  }

  @Test
  public void testFactoryModelsAreIndependent() {
    // create two models, check if independent!
    KlondikeModel<Card> basic = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    KlondikeModel<Card> whitehead = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);

    List<Card> deck1 = basic.createNewDeck();
    List<Card> deck2 = whitehead.createNewDeck();

    basic.startGame(deck1, false, 7, 3);
    whitehead.startGame(deck2, false, 7, 3);

    // make a move should not modify other
    int basicInitialScore = basic.getScore();
    int whiteheadInitialScore = whitehead.getScore();

    // find and move an ace in basic
    for (int pile = 0; pile < 7; pile++) {
      if (basic.getPileHeight(pile) > 0) {
        Card card = basic.getCardAt(pile, basic.getPileHeight(pile) - 1);
        if (card.toString().startsWith("A")) {
          basic.moveToFoundation(pile, 0);
          break;
        }
      }
    }
    // whitehead should be unchanged
    assertEquals(whitehead.getScore(), whiteheadInitialScore);
  }

  @Test
  public void testMultipleCallsCreateNewInstances() {
    KlondikeModel<Card> basic1 = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    KlondikeModel<Card> basic2 = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);

    assertNotSame(basic1, basic2);

    KlondikeModel<Card> whitehead1 = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    KlondikeModel<Card> whitehead2 = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);

    assertNotSame(whitehead1, whitehead2);
  }
}