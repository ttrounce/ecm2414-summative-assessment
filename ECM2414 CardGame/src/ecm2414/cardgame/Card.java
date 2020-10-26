package ecm2414.cardgame;

import java.util.Arrays;

public class Card
{

	public final int denomination;

	public Card(int denom)
	{
		this.denomination = denom;
	}

	public String toString()
	{
		return "Card(" + this.denomination + ")";
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
