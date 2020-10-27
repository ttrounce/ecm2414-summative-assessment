package ecm2414.cardgame.exceptions;

/**
 * Represents when a win should've happened but it hasn't yet been triggered.
 */
public class WinConditionException extends Exception
{
	public WinConditionException(String message)
	{
		super(message);
	}
}
