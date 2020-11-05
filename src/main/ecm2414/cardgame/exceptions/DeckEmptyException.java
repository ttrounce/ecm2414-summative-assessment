package ecm2414.cardgame.exceptions;

/**
 * Represents an attempt to remove a card from an empty deck.
 */
// We are not serialising this class, hence tell the compiler to suppress.
@SuppressWarnings("serial")
public class DeckEmptyException extends Exception
{
	public DeckEmptyException(String message)
	{
		super(message);
	}
}
