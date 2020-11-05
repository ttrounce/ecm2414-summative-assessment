package ecm2414.cardgame.exceptions;

/**
 * Represents an attempt to remove a card from an empty hand.
 */
// We are not serialising this class, hence tell the compiler to suppress.
@SuppressWarnings("serial")
public class HandEmptyException extends Exception
{
	public HandEmptyException(String message)
	{
		super(message);
	}
}
