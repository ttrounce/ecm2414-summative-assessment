package ecm2414.cardgame.exceptions;

/**
 * Represents invalid card input being provided to the system.
 */
// We are not serialising this class, hence tell the compiler to suppress.
@SuppressWarnings("serial")
public class InvalidCardInputException extends Exception
{

	public InvalidCardInputException(String message)
	{
		super(message);
	}
}
