package ecm2414.cardgame.exceptions;

/**
 * Represents an attempt to add a card to a full deck.
 */
public class HandFullException extends Exception
{
	public HandFullException(String message)
	{
		super(message);
	}
}
