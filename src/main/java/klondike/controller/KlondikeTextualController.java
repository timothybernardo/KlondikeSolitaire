package klondike.controller;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;

/**
 * Implements the KlondikeController interface with a Readable that reads from input and
 * an Appendable that appends the game's output.
 */
public class KlondikeTextualController implements KlondikeController {
  private final Readable rd;
  private final Appendable ap;

  /**
   * Custom exception to signal user wants to quit.
   */
  private class QuitException extends RuntimeException {
  }

  /**
   * A textual controller for the Klondike solitaire game.
   * This controller handles user input from a Readable source and outputs game state
   * to an Appendable destination.
   *
   * @param rd input coming from the user
   * @param ap output sent to the user
   * @throws IllegalArgumentException if Readable or Appendable objects are null.
   */
  public KlondikeTextualController(Readable rd, Appendable ap) throws IllegalArgumentException {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Readable and Appendable cannot be null.");
    }
    this.rd = rd;
    this.ap = ap;
  }

  /**
   * Reads the next integer from input, retrying on invalid input.
   * If the user enters 'q' or 'Q', throws a QuitException.
   * If the user enters non-integer input, prompts for re-entry.
   *
   * @param scan the Scanner to read from
   * @return the integer value
   * @throws QuitException if user enters 'q' or 'Q'
   * @throws IOException   if output fails
   */
  private int readNextInt(Scanner scan) throws IOException, QuitException {
    try {
      return scan.nextInt();
    } catch (InputMismatchException ex) {
      String input = scan.next();
      if (input.equalsIgnoreCase("q")) {
        throw new QuitException();
      }
      ap.append("Re-enter value: ");
      return readNextInt(scan);
    }
  }

  @Override
  public <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck, boolean shuffle,
                                        int numRows, int numDraw)
      throws IllegalArgumentException, IllegalStateException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }

    startGame(model, deck, shuffle, numRows, numDraw);
    KlondikeTextualView view = new KlondikeTextualView(model, ap);
    renderGameState(model, view);

    Scanner scan = new Scanner(rd);
    while (!model.isGameOver()) {
      try {
        processGameCommand(model, view, scan);
      } catch (QuitException e) {
        handleQuit(model, view);
        return;
      } catch (IOException e) {
        throw new IllegalStateException("Failed to transmit output");
      }
    }

    displayGameEnd(model, view);
  }

  /**
   * Processes a single game command from the user.
   * Reads the command and delegates to the appropriate execution method.
   *
   * @param <C>   the card type
   * @param model the game model
   * @param view  the textual view for rendering
   * @param scan  the scanner for reading user input
   * @throws IOException           if there is an error with I/O operations
   * @throws QuitException         if the user chooses to quit
   * @throws IllegalStateException if no input is available
   */
  private <C extends Card> void processGameCommand(KlondikeModel<C> model,
                                                   KlondikeTextualView view,
                                                   Scanner scan)
      throws IOException, QuitException {
    if (!scan.hasNext()) {
      throw new IllegalStateException("No input provided");
    }

    String command = scan.next();
    if (command.equalsIgnoreCase("q")) {
      throw new QuitException();
    }

    try {
      executeCommand(model, view, scan, command);
    } catch (IllegalArgumentException | IllegalStateException e) {
      ap.append("Invalid move. Play again. ").append(e.getMessage()).append("\n");
    }
  }

  /**
   * Executes a specific game command based on the command string.
   * Commands supported:
   * - mpp: Move cards from pile to pile
   * - md: Move card from draw pile to cascade pile
   * - mpf: Move card from cascade pile to foundation
   * - mdf: Move card from draw pile to foundation
   * - dd: Discard the current draw card
   *
   * @param <C>     the card type
   * @param model   the game model
   * @param view    the textual view for rendering
   * @param scan    the scanner for reading command parameters
   * @param command the command string to execute
   * @throws IOException              if there is an error with I/O operations
   * @throws QuitException            if the user chooses to quit while entering parameters
   * @throws IllegalArgumentException if the command is unknown
   */
  private <C extends Card> void executeCommand(KlondikeModel<C> model,
                                               KlondikeTextualView view,
                                               Scanner scan,
                                               String command)
      throws IOException, QuitException {
    switch (command.toLowerCase()) {
      case "mpp":
        int sourcePile = readNextInt(scan);
        int cardsToMove = readNextInt(scan);
        int cascadePile = readNextInt(scan);
        executePileToPile(model, sourcePile, cardsToMove, cascadePile, view);
        break;
      case "md":
        int cascadePileForDraw = readNextInt(scan);
        executeDrawToPile(model, cascadePileForDraw, view);
        break;
      case "mpf":
        int cascadePileIndex = readNextInt(scan);
        int foundationPileIndex = readNextInt(scan);
        executePileToFoundation(model, cascadePileIndex, foundationPileIndex, view);
        break;
      case "mdf":
        int foundationIndex = readNextInt(scan);
        executeDrawToFoundation(model, foundationIndex, view);
        break;
      case "dd":
        executeDiscardDraw(model, view);
        break;
      default:
        throw new IllegalArgumentException("Unknown command.");
    }
  }

  /**
   * Handles the quit action by displaying the final game state and score.
   *
   * @param <C>   the card type
   * @param model the game model
   * @param view  the textual view for rendering
   * @throws IllegalStateException if output transmission fails
   */
  private <C extends Card> void handleQuit(KlondikeModel<C> model,
                                           KlondikeTextualView view)
      throws IllegalStateException {
    try {
      ap.append("Game quit!\n");
      ap.append("State of game when quit:\n");
      view.render();
      ap.append("\n");
      ap.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to transmit output");
    }
  }

  /**
   * Displays the end game state, including the final score and win/loss message.
   *
   * @param <C>   the card type
   * @param model the game model
   * @param view  the textual view for rendering
   * @throws IllegalStateException if output transmission fails
   */
  private <C extends Card> void displayGameEnd(KlondikeModel<C> model,
                                               KlondikeTextualView view)
      throws IllegalStateException {
    try {
      if (isWinCondition(model)) {
        ap.append("You win!\n");
      } else {
        ap.append("Game over. Score: ").append(String.valueOf(model.getScore())).append("\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to transmit output");
    }
  }

  /**
   * Checks if the player has won the game.
   * A win occurs when all cascade piles are empty and the draw pile is empty.
   *
   * @param <C>   the card type
   * @param model the game model to check
   * @return true if the player has won, false otherwise
   */
  private <C extends Card> boolean isWinCondition(KlondikeModel<C> model) {
    boolean allCascadeEmpty = true;
    for (int i = 0; i < model.getNumPiles(); i++) {
      if (model.getPileHeight(i) > 0) {
        allCascadeEmpty = false;
        break;
      }
    }
    return allCascadeEmpty && model.getDrawCards().isEmpty();
  }

  /**
   * Renders the current game state, including the board layout and score.
   *
   * @param <C>   the card type
   * @param model the game model
   * @param view  the textual view for rendering
   * @throws IllegalStateException if output transmission fails
   */
  private <C extends Card> void renderGameState(KlondikeModel<C> model,
                                                KlondikeTextualView view)
      throws IllegalStateException {
    try {
      view.render();
      ap.append("\n");
      ap.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
    } catch (IOException e) {
      throw new IllegalStateException("Failed to transmit output");
    }
  }

  /**
   * Executes the discard draw command.
   * Discards the current draw card and renders the updated game state.
   *
   * @param <C>   the card type
   * @param model the game model
   * @param view  the textual view for rendering
   * @throws IOException if rendering fails
   */
  private <C extends Card> void executeDiscardDraw(KlondikeModel<C> model,
                                                   KlondikeTextualView view)
      throws IOException {
    model.discardDraw();
    renderGameState(model, view);
  }

  /**
   * Executes a move from the draw pile to a foundation pile.
   * Moves the top draw card to the specified foundation pile (1-indexed from user).
   *
   * @param <C>             the card type
   * @param model           the game model
   * @param foundationIndex the foundation pile index (1-indexed from user input)
   * @param view            the textual view for rendering
   * @throws IOException if rendering fails
   */
  private <C extends Card> void executeDrawToFoundation(KlondikeModel<C> model,
                                                        int foundationIndex,
                                                        KlondikeTextualView view)
      throws IOException {
    model.moveDrawToFoundation(foundationIndex - 1);
    renderGameState(model, view);
  }

  /**
   * Executes a move from a cascade pile to a foundation pile.
   * Moves the top card of the specified cascade pile to the specified foundation pile.
   *
   * @param <C>                 the card type
   * @param model               the game model
   * @param cascadePileIndex    the source cascade pile index (1-indexed from user input)
   * @param foundationPileIndex the destination foundation pile index (1-indexed from user input)
   * @param view                the textual view for rendering
   * @throws IOException if rendering fails
   */
  private <C extends Card> void executePileToFoundation(KlondikeModel<C> model,
                                                        int cascadePileIndex,
                                                        int foundationPileIndex,
                                                        KlondikeTextualView view)
      throws IOException {
    model.moveToFoundation(cascadePileIndex - 1, foundationPileIndex - 1);
    renderGameState(model, view);
  }

  /**
   * Executes a move from the draw pile to a cascade pile.
   * Moves the top draw card to the specified cascade pile.
   *
   * @param <C>                the card type
   * @param model              the game model
   * @param cascadePileForDraw the destination cascade pile index (1-indexed from user input)
   * @param view               the textual view for rendering
   * @throws IOException if rendering fails
   */
  private <C extends Card> void executeDrawToPile(KlondikeModel<C> model,
                                                  int cascadePileForDraw,
                                                  KlondikeTextualView view)
      throws IOException {
    model.moveDraw(cascadePileForDraw - 1);
    renderGameState(model, view);
  }

  /**
   * Executes a move of multiple cards from one cascade pile to another.
   * Moves the specified number of cards from the source pile to the destination pile.
   *
   * @param <C>         the card type
   * @param model       the game model
   * @param sourcePile  the source cascade pile index (1-indexed from user input)
   * @param cardsToMove the number of cards to move
   * @param cascadePile the destination cascade pile index (1-indexed from user input)
   * @param view        the textual view for rendering
   * @throws IOException if rendering fails
   */
  private <C extends Card> void executePileToPile(KlondikeModel<C> model,
                                                  int sourcePile,
                                                  int cardsToMove,
                                                  int cascadePile,
                                                  KlondikeTextualView view)
      throws IOException {
    model.movePile(sourcePile - 1, cardsToMove, cascadePile - 1);
    renderGameState(model, view);
  }

  /**
   * Starts a new game with the specified parameters.
   * Initializes the model with the given deck and game configuration.
   *
   * @param <C>     the card type
   * @param model   the game model to initialize
   * @param deck    the deck of cards to use
   * @param shuffle whether to shuffle the deck
   * @param numRows the number of cascade piles
   * @param numDraw the number of draw cards visible at once
   * @throws IllegalStateException if the game cannot be started with the given parameters
   */
  private <C extends Card> void startGame(KlondikeModel<C> model,
                                          List<C> deck,
                                          boolean shuffle,
                                          int numRows,
                                          int numDraw) {
    try {
      model.startGame(deck, shuffle, numRows, numDraw);
    } catch (IllegalArgumentException | IllegalStateException e) {
      throw new IllegalStateException("Cannot start game");
    }
  }
}