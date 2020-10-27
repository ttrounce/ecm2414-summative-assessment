package ecm2414.cardgame.exceptions;

/**
 * Represents when there are not enough players to start the game (2 min).
 */
public class NotEnoughPlayersException extends Exception
{
	
	public NotEnoughPlayersException(String message)
	{
		super(message);
	}
}
