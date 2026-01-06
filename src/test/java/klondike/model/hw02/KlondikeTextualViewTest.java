package klondike.model.hw02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import klondike.view.KlondikeTextualView;
import org.junit.Test;

/**
 * Tests for the textual view of the Klondike Solitaire game.
 */
public class KlondikeTextualViewTest {

  @Test
  public void testToStringBeforeGameStart() {
    KlondikeModel<Card> model = new BasicKlondike();
    KlondikeTextualView view = new KlondikeTextualView(model);
    assertEquals("", view.toString());
  }

  @Test
  public void testToStringAfterGameStartIncludeDrawAndFoundation() {
    KlondikeModel<Card> model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 3, 1); // small game, not shuffled

    KlondikeTextualView view = new KlondikeTextualView(model);
    String out = view.toString();

    assertTrue(out.contains("Draw:"));
    assertTrue(out.contains("Foundation:"));
  }

  @Test
  public void testSmallGameExactOutput() {
    KlondikeModel<Card> model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();
    model.startGame(deck, false, 2, 1); // 2 piles, 1 draw, not shuffled

    KlondikeTextualView view = new KlondikeTextualView(model);
    String out = view.toString();

    // first line: draw
    assertTrue(out.startsWith("Draw:"));
    // second line: foundation with two <none>
    assertTrue(out.contains("Foundation: <none>, <none>"));
    // cascade rows should show up (pile 0 and pile 1)
    assertTrue(out.contains("A♣")); // ace of clubs is first card in deck
  }
}
