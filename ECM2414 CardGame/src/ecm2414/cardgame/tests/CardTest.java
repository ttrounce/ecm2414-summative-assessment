package ecm2414.cardgame.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ecm2414.cardgame.Card;

public class CardTest {
		
	@Test
	void testCard()
	{
		int denom = 1;
		Card card = new Card(denom);
		
		assertEquals(card.denomination, denom);
	}
}
