package ecm2414.cardgame.exceptions;

/**
 * Represents an attempt to add a card to a full deck.
 */
// We are not serialising this class, hence tell the compiler to suppress.
@SuppressWarnings("serial")
public class HandFullException extends Exception
{
	public HandFullException(String message)
	{
		super(message);
	}
}
