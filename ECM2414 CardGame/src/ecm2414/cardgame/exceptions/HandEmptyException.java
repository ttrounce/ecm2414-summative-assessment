package ecm2414.cardgame.exceptions;

/**
 * Represents an attempt to remove a card from an empty hand.
 */
public class HandEmptyException extends Exception
{
	public HandEmptyException(String message)
	{
		super(message);
	}
}
