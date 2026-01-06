package klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a foundation pile in Klondike Solitaire.
 * Foundation piles accept cards starting with Ace and building up to King in the same suit.
 */
public class FoundationPile implements Pile {
  private final List<Card> cards;
  private Suit suit;

  /**
   * Creates an empty foundation pile.
   */
  public FoundationPile() {
    this.cards = new ArrayList<>();
    this.suit = null;
  }

  /**
   * Adds card to the foundation pile.
   * Cards are added only if they are the same suit and one value higher than the top card.
   * Aces can only be placed on empty foundation piles.
   *
   * @param card the card to add
   * @throws IllegalArgumentException if card is null or invalid
   * @throws IllegalStateException    if card is not an Ace and placed on empty foundation, card is
   *                                  not the same suit, or card is not one value higher than the
   *                                  top card
   */
  @Override
  public void addCard(Card card) throws IllegalArgumentException, IllegalStateException {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }

    if (!(card instanceof PlayingCard playingCard)) {
      throw new IllegalArgumentException("Invalid card type");
    }

    if (isEmpty()) {
      if (playingCard.getValueEnum() != Value.ACE) {
        throw new IllegalStateException("Only Aces can be placed on empty foundations");
      }
      this.suit = playingCard.getSuitEnum();
    } else {

      PlayingCard topCard = (PlayingCard) cards.getLast();

      if (playingCard.getSuitEnum() != this.suit) {
        throw new IllegalStateException("Card must be same suit as foundation pile");
      }

      if (playingCard.getValue() != topCard.getValue() + 1) {
        throw new IllegalStateException("Card must be one value higher than top card");
      }
    }

    cards.add(card);
  }

  @Override
  public Card removeCard() throws IllegalStateException {
    if (isEmpty()) {
      throw new IllegalStateException("Cannot remove from empty foundation pile");
    }

    Card removed = cards.removeLast();

    // if pile empty, reset the suit
    if (isEmpty()) {
      this.suit = null;
    }

    return removed;
  }

  @Override
  public Card getCardAt(int index) throws IllegalArgumentException {
    if (index < 0 || index >= cards.size()) {
      throw new IllegalArgumentException("Invalid index: " + index);
    }
    return cards.get(index);
  }

  /**
   * Gets the top card of the foundation pile without removing it.
   *
   * @return the top card, or null if pile is empty
   */
  public Card getTopCard() {
    if (isEmpty()) {
      return null;
    }
    return cards.getLast();
  }

  @Override
  public int size() {
    return cards.size();
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  @Override
  public List<Card> getCards() {
    return new ArrayList<>(cards);
  }

  /**
   * Gets the suit this foundation pile accepts.
   *
   * @return the suit, or null if pile is empty
   */
  public Suit getSuit() {
    return suit;
  }

  /**
   * Checks if a card can be legally added to this foundation pile.
   *
   * @param card the card to check
   * @return true if the card can be added, false otherwise
   */
  public boolean canAddCard(Card card) {
    if (card == null || !(card instanceof PlayingCard playingCard)) {
      return false;
    }

    if (isEmpty()) {
      return playingCard.getValueEnum() == Value.ACE;
    }

    PlayingCard topCard = (PlayingCard) getTopCard();
    return playingCard.getSuitEnum() == this.suit
        && playingCard.getValue() == topCard.getValue() + 1;
  }
}