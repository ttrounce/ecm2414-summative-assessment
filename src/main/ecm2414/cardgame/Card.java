package ecm2414.cardgame;

import java.util.Arrays;

/**
 * A class representing a card with a value.
 */
public class Card
{

	public final int denomination;

	public Card(int denom)
	{
		this.denomination = denom;
	}

	@Override
	public String toString()
	{
		return "" + this.denomination;
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(new int[] { denomination });
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Card && ((Card) obj).denomination == this.denomination;
	}
}
