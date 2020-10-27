package ecm2414.cardgame.exceptions;

/**
 * Represents a lack of cards in the game, too little to facilitate any gameplay.
 */
public class NotEnoughCardsException extends Exception
{
	public NotEnoughCardsException(String message)
	{
		super(message);
	}
}
