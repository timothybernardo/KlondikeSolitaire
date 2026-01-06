package klondike.model.hw04;

import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Allows a Klondike game to be created depending on which
 * version is inputted by the user.
 */
public class KlondikeCreator {
  /**
   * Defines the two types of Klondike implemented.
   */
  public enum GameType {
    BASIC, WHITEHEAD
  }

  /**
   * Creates an instance of a KlondikeModel.
   *
   * @param type the game type desired by the user
   * @return an instance of KlondikeModel, Basic or Whitehead depending on user input
   */
  public static KlondikeModel<Card> create(GameType type) {
    switch (type) {
      case BASIC: return new BasicKlondike();
      case WHITEHEAD: return new WhiteheadKlondike();
      default: throw new IllegalArgumentException("Unknown game type: " + type);
    }
  }
}
