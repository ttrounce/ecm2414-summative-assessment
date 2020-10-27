package ecm2414.cardgame.exceptions;

/**
 * Represents an attempt to remove a card from an empty deck.
 */
public class DeckEmptyException extends Exception
{
	public DeckEmptyException(String message)
	{
		super(message);
	}
}
