package klondike.controller;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Interface for the Klondike's controller.
 */
public interface KlondikeController {
  /**
   * Plays a new game of Klondike using the provided model.
   *
   * @param model   the Klondike model provided
   * @param deck    the list of Cards provided
   * @param shuffle true if cards need to be shuffled, false otherwise
   * @param numRows the number of rows
   * @param numDraw the number of cards in the Draw pile
   * @param <C>     the type of Card
   * @throws IllegalArgumentException if the provided model is null
   * @throws IllegalStateException    if the controller cannot receive input, transmit output, or
   *                                  game cannot be started
   */
  <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck, boolean shuffle, int numRows,
                                 int numDraw)
      throws IllegalArgumentException, IllegalStateException;

}
