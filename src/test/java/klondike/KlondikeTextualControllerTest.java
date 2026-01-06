package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.mocks.AlwaysFailingMoveMock;
import klondike.mocks.FailingStartModel;
import klondike.mocks.InvalidNumbersMockModel;
import klondike.mocks.LoggingMockModel;
import klondike.mocks.LoseConditionMockModel;
import klondike.mocks.NoInputTestMockModel;
import klondike.mocks.ScoreMockModel;
import klondike.mocks.WinConditionMockModel;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import org.junit.Test;

/**
 * A class for testing the KlondikeTextualController.
 */
public class KlondikeTextualControllerTest {

  // CONSTRUCTOR VALIDATION TESTS

  @Test(expected = IllegalArgumentException.class)
  public void testNullReadable() {
    new KlondikeTextualController(null, new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    new KlondikeTextualController(new StringReader(""), null);
  }

  @Test
  public void testValidConstructor() {
    KlondikeController controller =
        new KlondikeTextualController(new StringReader(""), new StringBuilder());
    assertNotNull(controller);
  }

  // PLAY GAME VALIDATION TESTS

  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameNullModel() {
    Readable input = new StringReader("");
    Appendable output = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(input, output);

    List<Card> deck = new ArrayList<>();
    controller.playGame(null, deck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameInvalidStartParameters() {
    Readable input = new StringReader("");
    Appendable output = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(input, output);

    KlondikeModel<Card> model = new FailingStartModel(true);
    List<Card> deck = new ArrayList<>();
    controller.playGame(model, deck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameStartThrowsIllegalState() {
    Readable input = new StringReader("q");
    Appendable output = new StringBuilder();
    KlondikeController controller = new KlondikeTextualController(input, output);

    KlondikeModel<Card> model = new FailingStartModel(false);
    List<Card> deck = new ArrayList<>();

    controller.playGame(model, deck, false, 7, 3);
  }

  // BASIC COMMAND TESTS
  @Test
  public void testMovePileToPileCommand() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("mpp 2 3 5 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("movePile 1 3 4"));
    assertTrue(output.toString().contains("Game quit!"));
  }

  @Test
  public void testMoveDrawCommand() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("md 4 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("moveDraw 3"));
  }

  @Test
  public void testMovePileToFoundationCommand() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("mpf 3 2 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("moveToFoundation 2 1"));
  }

  @Test
  public void testMoveDrawToFoundationCommand() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("mdf 1 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("moveDrawToFoundation 0"));
  }

  @Test
  public void testDiscardDrawCommand() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("dd q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("discardDraw"));
  }

  @Test
  public void testMppInvalidFirstParameter() {
    LoggingMockModel model = new LoggingMockModel();
    // invalid first parameter, then valid
    Readable input = new StringReader("mpp bad 1 2 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(model.getLog().contains("movePile 0 2 2"));
  }

  @Test
  public void testMppInvalidSecondParameter() {
    LoggingMockModel model = new LoggingMockModel();
    // invalid second parameter, then valid
    Readable input = new StringReader("mpp 1 invalid 2 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(model.getLog().contains("movePile 0 2 2"));
  }

  @Test
  public void testMppInvalidThirdParameter() {
    LoggingMockModel model = new LoggingMockModel();
    // invalid third parameter, then valid
    Readable input = new StringReader("mpp 1 2 wrong 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(model.getLog().contains("movePile 0 2 2"));
  }

  @Test
  public void testMppAllParametersInvalid() {
    LoggingMockModel model = new LoggingMockModel();
    // all parameters invalid
    Readable input = new StringReader("mpp a 1 b 2 c 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    String outputStr = output.toString();
    int count = outputStr.split("Re-enter value: ").length - 1;
    assertEquals(3, count);
    assertTrue(model.getLog().contains("movePile 0 2 2"));
  }

  @Test
  public void testMdInvalidParameter() {
    LoggingMockModel model = new LoggingMockModel();
    // invalid parameter for md
    Readable input = new StringReader("md notanumber 5 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(model.getLog().contains("moveDraw 4"));
  }

  @Test
  public void testMdMultipleInvalidInputs() {
    LoggingMockModel model = new LoggingMockModel();
    // multiple invalid inputs
    Readable input = new StringReader("md xxx yyy zzz 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    String outputStr = output.toString();
    int count = outputStr.split("Re-enter value: ").length - 1;
    assertEquals(3, count);
    assertTrue(model.getLog().contains("moveDraw 2"));
  }

  @Test
  public void testMpfInvalidFirstParameter() {
    LoggingMockModel model = new LoggingMockModel();
    // invalid first parameter, then valid
    Readable input = new StringReader("mpf asdfsfa 2 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(model.getLog().contains("moveToFoundation 1 2"));
  }

  @Test
  public void testMpfInvalidSecondParameter() {
    LoggingMockModel model = new LoggingMockModel();
    // invalid second parameter, then valid
    Readable input = new StringReader("mpf 2 asdfaf 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(model.getLog().contains("moveToFoundation 1 2"));
  }

  @Test
  public void testMpfBothParametersInvalid() {
    LoggingMockModel model = new LoggingMockModel();
    // both parameters invalid
    Readable input = new StringReader("mpf bad1 4 bad2 2 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    String outputStr = output.toString();
    int count = outputStr.split("Re-enter value: ").length - 1;
    assertEquals(2, count);
    assertTrue(model.getLog().contains("moveToFoundation 3 1"));
  }

  @Test
  public void testMdfInvalidParameter() {
    LoggingMockModel model = new LoggingMockModel();
    // invalid parameter
    Readable input = new StringReader("mdf junk 2 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(model.getLog().contains("moveDrawToFoundation 1"));
  }

  @Test
  public void testMdfMultipleInvalidBeforeValid() {
    LoggingMockModel model = new LoggingMockModel();
    // multiple invalid inputs
    Readable input = new StringReader("mdf fadsf asdf asd asd 4 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    String outputStr = output.toString();
    int count = outputStr.split("Re-enter value: ").length - 1;
    assertEquals(4, count);
    assertTrue(model.getLog().contains("moveDrawToFoundation 3"));
  }


  @Test
  public void testDdFollowedByInvalidCommand() {
    LoggingMockModel model = new LoggingMockModel();
    // dd is valid, then invalid command
    Readable input = new StringReader("dd badcommand mpp 1 2 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("discardDraw"));
    assertTrue(output.toString().contains("Invalid move. Play again. Unknown command."));
    assertTrue(model.getLog().contains("movePile 0 2 2"));
  }

  @Test
  public void testMixedCommandsWithInvalidInputs() {
    LoggingMockModel model = new LoggingMockModel();
    // commands with various invalid inputs
    Readable input = new StringReader(
        "mpp bad 1 2 wrong 3 "
            +  // mpp with 2 invalid inputs
            "dd " // valid dd
            + "md nope 7 " // md with 1 invalid input
            + "mpf 2 invalid 1 " // mpf with 1 invalid input
            + "mdf text 4 " // mdf with 1 invalid input
            + "q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    String log = model.getLog();
    assertTrue(log.contains("movePile 0 2 2"));
    assertTrue(log.contains("discardDraw"));
    assertTrue(log.contains("moveDraw 6"));
    assertTrue(log.contains("moveToFoundation 1 0"));
    assertTrue(log.contains("moveDrawToFoundation 3"));

    // prompted for re-entry 5 times total
    String outputStr = output.toString();
    int count = outputStr.split("Re-enter value: ").length - 1;
    assertEquals(5, count);
  }

  @Test
  public void testUppercaseQuitDuringInvalidInput() {
    LoggingMockModel model = new LoggingMockModel();
    // uppercase Q during invalid input recovery
    Readable input = new StringReader("mpf bad Q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(output.toString().contains("Game quit!"));
    assertFalse(model.getLog().contains("moveToFoundation"));
  }

  @Test
  public void testUppercaseCommands() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("DD MPF 1 2 MDF 3 MD 4 MPP 1 2 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("discardDraw"));
    assertTrue(model.getLog().contains("moveToFoundation 0 1"));
    assertTrue(model.getLog().contains("moveDrawToFoundation 2"));
    assertTrue(model.getLog().contains("moveDraw 3"));
    assertTrue(model.getLog().contains("movePile 0 2 2"));
  }

  @Test
  public void testMixedCaseCommands() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("Dd MpF 1 2 mDf 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("discardDraw"));
    assertTrue(model.getLog().contains("moveToFoundation 0 1"));
    assertTrue(model.getLog().contains("moveDrawToFoundation 2"));
  }

  // QUIT SCENARIOS TESTS

  @Test
  public void testMppQuitAtFirstParameter() {
    LoggingMockModel model = new ScoreMockModel(10);
    Readable input = new StringReader("mpp q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("movePile"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 10"));
  }

  @Test
  public void testMppQuitAtSecondParameter() {
    LoggingMockModel model = new ScoreMockModel(15);
    Readable input = new StringReader("mpp 2 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("movePile"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 15"));
  }

  @Test
  public void testMppQuitAtThirdParameter() {
    LoggingMockModel model = new ScoreMockModel(20);
    Readable input = new StringReader("mpp 2 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("movePile"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 20"));
  }

  @Test
  public void testMppUppercaseQuitAtEachPosition() {
    // first position
    LoggingMockModel model1 = new LoggingMockModel();
    Readable input1 = new StringReader("mpp Q");
    StringBuilder output1 = new StringBuilder();

    KlondikeController controller1 = new KlondikeTextualController(input1, output1);
    controller1.playGame(model1, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model1.getLog().contains("movePile"));
    assertTrue(output1.toString().contains("Game quit!"));
    assertTrue(output1.toString().contains("State of game when quit:"));

    // second position
    LoggingMockModel model2 = new LoggingMockModel();
    Readable input2 = new StringReader("mpp 1 Q");
    StringBuilder output2 = new StringBuilder();

    KlondikeController controller2 = new KlondikeTextualController(input2, output2);
    controller2.playGame(model2, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model2.getLog().contains("movePile"));
    assertTrue(output2.toString().contains("Game quit!"));
    assertTrue(output2.toString().contains("State of game when quit:"));

    // third position
    LoggingMockModel model3 = new LoggingMockModel();
    Readable input3 = new StringReader("mpp 1 2 Q");
    StringBuilder output3 = new StringBuilder();

    KlondikeController controller3 = new KlondikeTextualController(input3, output3);
    controller3.playGame(model3, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model3.getLog().contains("movePile"));
    assertTrue(output3.toString().contains("Game quit!"));
    assertTrue(output3.toString().contains("State of game when quit:"));
  }


  @Test
  public void testMdQuitAtParameter() {
    LoggingMockModel model = new ScoreMockModel(25);
    Readable input = new StringReader("md q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("moveDraw"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("Score: 25"));
  }

  @Test
  public void testMdUppercaseQuit() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("md Q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("moveDraw"));
    assertTrue(output.toString().contains("Game quit!"));
  }

  @Test
  public void testMpfQuitAtFirstParameter() {
    LoggingMockModel model = new ScoreMockModel(30);
    Readable input = new StringReader("mpf q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("moveToFoundation"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 30"));
  }

  @Test
  public void testMpfQuitAtSecondParameter() {
    LoggingMockModel model = new ScoreMockModel(35);
    Readable input = new StringReader("mpf 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("moveToFoundation"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 35"));
  }

  @Test
  public void testMpfUppercaseQuitBothPositions() {
    // first position
    LoggingMockModel model1 = new LoggingMockModel();
    Readable input1 = new StringReader("mpf Q");
    StringBuilder output1 = new StringBuilder();

    KlondikeController controller1 = new KlondikeTextualController(input1, output1);
    controller1.playGame(model1, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model1.getLog().contains("moveToFoundation"));
    assertTrue(output1.toString().contains("Game quit!"));

    // second position
    LoggingMockModel model2 = new LoggingMockModel();
    Readable input2 = new StringReader("mpf 2 Q");
    StringBuilder output2 = new StringBuilder();

    KlondikeController controller2 = new KlondikeTextualController(input2, output2);
    controller2.playGame(model2, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model2.getLog().contains("moveToFoundation"));
    assertTrue(output2.toString().contains("Game quit!"));
    assertTrue(output2.toString().contains("State of game when quit:"));
  }

  @Test
  public void testMdfQuitAtParameter() {
    LoggingMockModel model = new ScoreMockModel(40);
    Readable input = new StringReader("mdf q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("moveDrawToFoundation"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 40"));
  }

  @Test
  public void testMdfUppercaseQuit() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("mdf Q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("moveDrawToFoundation"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
  }


  @Test
  public void testDdFollowedByQuit() {
    LoggingMockModel model = new ScoreMockModel(45);
    Readable input = new StringReader("dd q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("discardDraw"));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 45"));
  }

  @Test
  public void testQuitAsDirectCommand() {
    LoggingMockModel model = new ScoreMockModel(50);
    Readable input = new StringReader("q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 50"));
  }

  @Test
  public void testUppercaseQuitAsDirectCommand() {
    LoggingMockModel model = new ScoreMockModel(55);
    Readable input = new StringReader("Q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 55"));
  }


  @Test
  public void testQuitAfterInvalidInput() {
    LoggingMockModel model = new LoggingMockModel();
    // invalid input, then quit
    Readable input = new StringReader("mpp bad q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Re-enter value: "));
    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertFalse(model.getLog().contains("movePile"));
  }

  @Test
  public void testQuitAfterSuccessfulCommands() {
    LoggingMockModel model = new ScoreMockModel(60);
    // execute commands successfully, then quit
    Readable input = new StringReader("dd md 3 mpf 1 2 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    String log = model.getLog();
    assertTrue(log.contains("discardDraw"));
    assertTrue(log.contains("moveDraw 2"));
    assertTrue(log.contains("moveToFoundation 0 1"));

    assertTrue(output.toString().contains("Game quit!"));
    assertTrue(output.toString().contains("State of game when quit:"));
    assertTrue(output.toString().contains("Score: 60"));
  }

  // TESTS USING FAILING APPENDABLE
  @Test(expected = IllegalStateException.class)
  public void testFailingAppendableThrowsIllegalState() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("q");
    Appendable failingOutput = new FailingAppendable();

    KlondikeController controller = new KlondikeTextualController(input, failingOutput);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
  }

  @Test
  public void testFailingAppendableExceptionMessage() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("q");
    Appendable failingOutput = new FailingAppendable();

    KlondikeController controller = new KlondikeTextualController(input, failingOutput);

    try {
      controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
    } catch (IllegalStateException e) {
      assertEquals("Failed to transmit output", e.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testIoExceptionCommandProcessing() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("dd q");
    Appendable failingOutput = new FailingAppendable();

    KlondikeController controller = new KlondikeTextualController(input, failingOutput);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
  }

  // GAME OVER TESTS
  @Test
  public void testGameContinuesUntilQuit() {
    LoggingMockModel model = new LoggingMockModel();
    model.setGameOver(false);

    Readable input = new StringReader("dd dd dd q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    // all 3 commands executed before quit
    assertEquals(3, model.getLog().split("discardDraw").length - 1);
    assertTrue(output.toString().contains("Game quit!"));
  }

  @Test
  public void testImmediateGameOver() {
    LoggingMockModel model = new LoggingMockModel();
    model.setGameOver(true);  // game already over

    Readable input = new StringReader("");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(model.getLog().contains("move"));
    assertFalse(model.getLog().contains("discard"));
    assertTrue(output.toString().contains("Game over. Score:"));
  }

  // WIN CONDITIONS TESTS
  @Test
  public void testWinConditionAfterDiscardDraw() {
    LoggingMockModel model = new WinConditionMockModel();
    Readable input = new StringReader("dd");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("You win!"));
    assertFalse(output.toString().contains("Game over. Score:"));
  }

  @Test
  public void testWinConditionAfterMovePile() {
    LoggingMockModel model = new WinConditionMockModel();
    Readable input = new StringReader("mpp 1 1 2");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
    assertTrue(output.toString().contains("You win!"));
  }

  @Test
  public void testWinConditionAfterMoveToFoundation() {
    LoggingMockModel model = new WinConditionMockModel();
    Readable input = new StringReader("mpf 1 1");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
    assertTrue(output.toString().contains("You win!"));
  }

  // LOSE CONDITION TEST
  @Test
  public void testLoseWithCardsInPiles() {
    LoggingMockModel model = new LoseConditionMockModel();
    Readable input = new StringReader("dd");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertFalse(output.toString().contains("You win!"));
    assertTrue(output.toString().contains("Game over. Score:"));
  }

  // NO INPUT TESTS
  @Test(expected = IllegalStateException.class)
  public void testNoInputProvided() {
    NoInputTestMockModel model = new NoInputTestMockModel();
    Readable input = new StringReader("");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
  }

  @Test
  public void testNoInputExceptionMessage() {
    NoInputTestMockModel model = new NoInputTestMockModel();
    Readable input = new StringReader("");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);

    try {
      controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
    } catch (IllegalStateException e) {
      assertEquals("No input provided", e.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testOnlyWhitespaceInput() {
    NoInputTestMockModel model = new NoInputTestMockModel();
    Readable input = new StringReader("   \n  \t  "); // whitespace
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
  }

  // INPUT RUNS OUT TESTS
  @Test(expected = IllegalStateException.class)
  public void testInputRunsOutMidGame() {
    LoggingMockModel model = new LoggingMockModel();
    model.setGameOver(false);

    Readable input = new StringReader("dd mpp 1 2 3");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
  }

  @Test(expected = NoSuchElementException.class)
  public void testInputRunsOutMidCommand() {
    LoggingMockModel model = new LoggingMockModel();
    model.setGameOver(false);

    Readable input = new StringReader("mpp 1 2");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);
  }

  @Test
  public void testSpecialCharactersAsCommands() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("!@# dd $%^ q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("discardDraw"));
  }

  // CONTROLLER REUSE TEST
  @Test
  public void testControllerReuse() {
    Readable input1 = new StringReader("dd q");
    Readable input2 = new StringReader("mpp 1 1 2 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input1, output);

    // game 1
    LoggingMockModel model1 = new LoggingMockModel();
    controller.playGame(model1, new ArrayList<Card>(), false, 7, 3);
    assertTrue(model1.getLog().contains("discardDraw"));

    // reset, new input
    controller = new KlondikeTextualController(input2, output);

    // game 2
    LoggingMockModel model2 = new LoggingMockModel();
    controller.playGame(model2, new ArrayList<Card>(), false, 7, 3);
    assertTrue(model2.getLog().contains("movePile 0 1 1"));
  }

  // INVALID COMMAND RECOVERY
  @Test
  public void testUnknownCommand() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("badcommand q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Invalid move. Play again. Unknown command."));
  }

  @Test
  public void testInvalidCommandRecovery() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("xyz dd abc mpp 1 1 2 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    // recover from bad commands
    assertTrue(model.getLog().contains("discardDraw"));
    assertTrue(model.getLog().contains("movePile 0 1 1"));
    assertEquals(2, output.toString().split("Unknown command").length - 1);
  }

  // INVALID NUMBERS TEST
  @Test
  public void testValidControllerInputInvalidModelNumbers() {
    LoggingMockModel model = new InvalidNumbersMockModel();
    Readable input = new StringReader("mpp 0 1 1 mpp 8 1 1 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(output.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testInvalidMoveHandling() {
    LoggingMockModel model = new AlwaysFailingMoveMock();

    Readable input = new StringReader("mpp 1 2 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("movePile 0 2 2"));
    assertTrue(output.toString().contains("Invalid move. Play again. Invalid move"));
  }

  @Test
  public void testMultipleInvalidMovesHandling() {
    LoggingMockModel model = new AlwaysFailingMoveMock();

    Readable input = new StringReader("dd mpp 1 2 3 md 4 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertTrue(model.getLog().contains("discardDraw"));
    assertTrue(model.getLog().contains("movePile 0 2 2"));
    assertTrue(model.getLog().contains("moveDraw 3"));

    assertEquals(3, output.toString().split("Invalid move. Play again.").length - 1);
  }

  @Test
  public void testInvalidTokensAsNumbers() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("mpp abc 1 def 2 ghi 3 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    assertEquals(3, output.toString().split("Re-enter value: ").length - 1);
    assertTrue(model.getLog().contains("movePile 0 2 2"));
  }

  // CASE SENSITIVE TEST
  @Test
  public void testCommandCaseSensitivity() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("DD MPP 1 2 3 MD 4 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    if (model.getLog().contains("discardDraw")) {
      assertTrue(model.getLog().contains("movePile 0 2 2"));
      assertTrue(model.getLog().contains("moveDraw 3"));
    } else {
      assertTrue(output.toString().contains("Unknown command"));
    }
  }

  // TEST MULTIPLE COMMANDS SEQUENCED
  @Test
  public void testMultipleCommandsInSequence() {
    LoggingMockModel model = new LoggingMockModel();
    Readable input = new StringReader("mpp 1 2 3 dd md 7 mpf 2 1 mdf 4 q");
    StringBuilder output = new StringBuilder();

    KlondikeController controller = new KlondikeTextualController(input, output);
    controller.playGame(model, new ArrayList<Card>(), false, 7, 3);

    String log = model.getLog();
    assertTrue(log.contains("movePile 0 2 2"));
    assertTrue(log.contains("discardDraw"));
    assertTrue(log.contains("moveDraw 6"));
    assertTrue(log.contains("moveToFoundation 1 0"));
    assertTrue(log.contains("moveDrawToFoundation 3"));

    assertTrue(log.indexOf("movePile") < log.indexOf("discardDraw"));
    assertTrue(log.indexOf("discardDraw") < log.indexOf("moveDraw"));
  }

}
