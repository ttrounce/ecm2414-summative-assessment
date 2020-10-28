package ecm2414.cardgame.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ecm2414.cardgame.Card;

public class CardTest
{

	/**
	 * Tests that the constructor sets the denomination correctly.
	 */
	@Test
	public void testCard()
	{
		int denom = 1;
		Card card = new Card(denom);

		assertEquals(card.denomination, denom);
	}
}
