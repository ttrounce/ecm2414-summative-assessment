package ecm2414.cardgame.exceptions;

/**
 * Represents a lack of cards in the game, too little to facilitate any
 * gameplay.
 */
// We are not serialising this class, hence tell the compiler to suppress.
@SuppressWarnings("serial")
public class NotEnoughCardsException extends Exception
{
	public NotEnoughCardsException(String message)
	{
		super(message);
	}
}
