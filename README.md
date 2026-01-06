# Changes made to model
* BasicKlondike now extends AbstractKlondike,
where all instance variables and a majority of these methods 
moved to this abstract class.

* All public interface methods, foundation-related methods, draw pile method,
getter methods, and validation helper methods moved to this abstract class.

* The abstract class has four new helper methods:
* shouldCardBeVisible(): Determines card visibility during dealing (returns row == pile)
* canStackCards(): Validates cascade stacking rules (opposite colors, descending by 1)
* canPlaceOnEmptyPile(): Checks empty pile placement (only Kings allowed)
* isValidCardSequence(): Validates multi-card move sequences (alternating colors, descending)

* Modified methods include dealCascadePiles(), where it uses abstract shouldBeCardVisible().
validateCascadeMove() delegates to abstract methods canPlaceOnEmptyPile() and canStackCards().
movePile() uses isValidCardSequence() for validation. canMoveBetweenCascades() can be used to
update abstract helper methods.

* I changed dealCascadePiles() and validateCascadeMove() to protected because it allows
the BasicKlondike model and the WhiteheadKlondike model to override these methods.