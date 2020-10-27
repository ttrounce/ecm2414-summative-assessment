package ecm2414.cardgame.exceptions;

/**
 * Represents invalid card input being provided to the system.
 */
public class InvalidCardInputException extends Exception
{

	public InvalidCardInputException(String message)
	{
		super(message);
	}
}
