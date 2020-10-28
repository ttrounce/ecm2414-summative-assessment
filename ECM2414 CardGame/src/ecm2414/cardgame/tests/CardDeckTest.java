package ecm2414.cardgame.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import ecm2414.cardgame.Card;
import ecm2414.cardgame.CardDeck;
import ecm2414.cardgame.exceptions.DeckEmptyException;

public class CardDeckTest {
		
	@Test
	void testCardDeck() 
	{
		int deckNum = 45;
		CardDeck cardDeck = new CardDeck(deckNum);
		
		assertEquals(deckNum, cardDeck.getDeckNumber());
	}
	
	@Test
	void testGetDeck()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		
		assertTrue (cardDeck.getDeck() != null && cardDeck.getDeck().isEmpty());
	}
	
	@Test
	void testAddCard()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		Card card = new Card(deckNum);
		
		cardDeck.addCard(card);
		assertEquals (card, cardDeck.getDeck().peek());
		assertEquals(cardDeck.getDeck().size(), 1);
	}
	
	@Test
	void testRemoveCard()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		Card card = new Card(deckNum);
		
		cardDeck.addCard(card);
		try {
			cardDeck.removeCard(card);
		} catch (DeckEmptyException e) {
			fail();
		}
		
		assertEquals(cardDeck.getDeck().size(), 0);
	}
	
	@Test
	void testTakeCard()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		Card card = new Card(deckNum);
		Card card2 = new Card(deckNum+1);
		Card card3 = new Card(deckNum+2);
		Card card4 = new Card(deckNum+3);
		Card takenCard = null;
		
		cardDeck.addCard(card);
		cardDeck.addCard(card2);
		cardDeck.addCard(card3);
		cardDeck.addCard(card4);
		try {
			takenCard = cardDeck.takeCard();
		} catch (DeckEmptyException e) {
			fail();
		}
		
		assertEquals(card, takenCard);
		assertEquals(cardDeck.getDeck().size(), 3);
		assertNotEquals(takenCard, card2);
		assertNotEquals(takenCard, card3);
		assertNotEquals(takenCard, card4);
	}
	
	@Test
	void testDeckEmptyException()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		try {
			cardDeck.takeCard();
			fail();
		} catch (DeckEmptyException e) {
			// Pass - throws Exception successfully
		}
	}
}
