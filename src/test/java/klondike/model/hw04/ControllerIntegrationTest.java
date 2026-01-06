package klondike.model.hw04;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;
import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration tests for controller with WhiteheadKlondike and KlondikeCreator.
 */
public class ControllerIntegrationTest {

  private Appendable output;
  private List<Card> deck;

  /**
   * Creates an Appendable for the controller, a temporary model that tests WhiteHead, as well as
   * a fresh card deck.
   */
  @Before
  public void setUp() {
    output = new StringBuilder();
    KlondikeModel<Card> tempModel = new WhiteheadKlondike();
    deck = tempModel.createNewDeck();
  }

  // FACTORY TESTS

  @Test
  public void testKlondikeCreatorBasic() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    assertNotNull(model);
    assertTrue(model instanceof BasicKlondike);
  }

  @Test
  public void testKlondikeCreatorWhitehead() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    assertNotNull(model);
    assertTrue(model instanceof WhiteheadKlondike);
  }

  @Test(expected = NullPointerException.class)
  public void testKlondikeCreatorNullType() {
    KlondikeCreator.create(null);
  }

  @Test
  public void testKlondikeCreatorBasicBehavior() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    model.startGame(deck, false, 7, 3);

    // only bottom cards are visible
    assertFalse(model.isCardVisible(6, 0)); // top card of pile 6
    assertTrue(model.isCardVisible(6, 6)); // bottom card of pile 6
  }

  @Test
  public void testKlondikeCreatorWhiteheadBehavior() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    model.startGame(deck, false, 7, 3);

    // all cards are visible
    for (int pile = 0; pile < 7; pile++) {
      for (int card = 0; card < model.getPileHeight(pile); card++) {
        assertTrue(model.isCardVisible(pile, card));
      }
    }
  }

  // CONTROLLER WITH WHITEHEAD TESTS

  @Test
  public void testControllerStartsWhiteheadGame() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Draw:"));
    assertTrue(result.contains("Foundation:"));

    // check that no '?' appears in cascade
    String cascadeSection = result.substring(result.indexOf("Foundation"));
    cascadeSection = cascadeSection.substring(0, cascadeSection.indexOf("Score:"));
    assertFalse(cascadeSection.contains("?"));

    assertTrue(result.contains("Game quit!"));
    assertTrue(result.contains("Score: 0"));
  }

  @Test
  public void testControllerStartsBasicGame() {
    KlondikeModel<Card> model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    // basic should have some hidden cards shown as '?'
    String cascadeSection = result.substring(result.indexOf("Foundation"));
    cascadeSection = cascadeSection.substring(0, cascadeSection.indexOf("Score:"));
    assertTrue(cascadeSection.contains("?"));
  }

  @Test
  public void testControllerMovePileWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("mpp 1 1 2 q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Foundation"));
    assertTrue(result.contains("Game quit!"));
  }

  @Test
  public void testControllerMoveDrawWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("md 1 q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    // move succeeds or fails with error message
    assertTrue(result.contains("Game quit!"));
  }

  @Test
  public void testControllerDiscardDrawWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("dd dd dd q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Draw:"));
    assertTrue(result.contains("Game quit!"));
  }

  @Test
  public void testControllerMoveToFoundationWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("mpf 1 1 q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Foundation"));
    assertTrue(result.contains("Game quit!"));
  }

  @Test
  public void testControllerMoveDrawToFoundationWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("mdf 1 q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Foundation"));
    assertTrue(result.contains("Game quit!"));
  }

  // CONTROLLER ERROR WITH WHITEHEAD

  @Test
  public void testControllerInvalidMoveWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("mpp 1 100 2 q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Invalid move"));
    assertTrue(result.contains("Game quit!"));
  }

  @Test
  public void testControllerInvalidCommandWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("badcommand dd q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Unknown command"));
    assertTrue(result.contains("Draw:"));
    assertTrue(result.contains("Game quit!"));
  }

  @Test
  public void testControllerInvalidParameterWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("mpp abc 1 2 3 q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Re-enter value"));
    assertTrue(result.contains("Game quit!"));
  }

  // GAME STATE TESTS

  @Test
  public void testControllerQuitShowsScoreWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Score: 0"));
  }

  @Test
  public void testControllerMultipleCommandsWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    String commands = "dd dd mpp 1 1 2 dd q";
    KlondikeController controller = new KlondikeTextualController(
        new StringReader(commands), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    // process all commands before quitting
    assertTrue(result.contains("Draw:"));
    assertTrue(result.contains("Game quit!"));
  }

  // CONTROLLER COMPATIBLE TESTS

  @Test
  public void testControllerWorksWithBothModels() {
    // test with basic
    KlondikeModel<Card> basicModel = KlondikeCreator.create(
        KlondikeCreator.GameType.BASIC);
    StringBuilder basicOutput = new StringBuilder();
    KlondikeController basicController = new KlondikeTextualController(
        new StringReader("dd q"), basicOutput);

    basicController.playGame(basicModel, deck, false, 7, 3);
    assertTrue(basicOutput.toString().contains("Game quit!"));

    // test with whitehead
    KlondikeModel<Card> whiteheadModel = KlondikeCreator.create(
        KlondikeCreator.GameType.WHITEHEAD);
    StringBuilder whiteheadOutput = new StringBuilder();
    KlondikeController whiteheadController = new KlondikeTextualController(
        new StringReader("dd q"), whiteheadOutput);

    whiteheadController.playGame(whiteheadModel, deck, false, 7, 3);
    assertTrue(whiteheadOutput.toString().contains("Game quit!"));
  }

  @Test
  public void testControllerWithDifferentPileConfigurations() {
    // test with 5 piles
    KlondikeModel<Card> model5 = new WhiteheadKlondike();
    StringBuilder output5 = new StringBuilder();
    KlondikeController controller5 = new KlondikeTextualController(
        new StringReader("q"), output5);

    controller5.playGame(model5, deck, false, 5, 3);
    assertTrue(output5.toString().contains("Game quit!"));

    // test with 9 piles
    KlondikeModel<Card> model9 = new WhiteheadKlondike();
    StringBuilder output9 = new StringBuilder();
    KlondikeController controller9 = new KlondikeTextualController(
        new StringReader("q"), output9);

    controller9.playGame(model9, deck, false, 9, 1);
    assertTrue(output9.toString().contains("Game quit!"));
  }

  @Test
  public void testControllerWithShuffledDeckWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("q"), output);

    controller.playGame(model, deck, true, 7, 3); // shuffle = true

    String result = output.toString();
    assertTrue(result.contains("Foundation"));
    assertTrue(result.contains("Game quit!"));
  }

  // NULL AND EXCEPTION TESTS

  @Test(expected = IllegalArgumentException.class)
  public void testControllerNullModelWhitehead() {
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("q"), output);
    controller.playGame(null, deck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testControllerInvalidDeckWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("q"), output);

    List<Card> invalidDeck = List.of(); // empty deck
    controller.playGame(model, invalidDeck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testControllerNoInputWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader(""), output); // no input

    controller.playGame(model, deck, false, 7, 3);
  }

  @Test
  public void testControllerCaseInsensitiveCommandsWhitehead() {
    KlondikeModel<Card> model = new WhiteheadKlondike();
    KlondikeController controller = new KlondikeTextualController(
        new StringReader("DD MPP 1 1 2 Q"), output);

    controller.playGame(model, deck, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Draw:"));
    assertTrue(result.contains("Game quit!"));
  }
}