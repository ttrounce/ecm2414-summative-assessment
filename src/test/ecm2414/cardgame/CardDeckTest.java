package ecm2414.cardgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Test;

import ecm2414.cardgame.Card;
import ecm2414.cardgame.CardDeck;
import ecm2414.cardgame.exceptions.DeckEmptyException;

public class CardDeckTest
{

	/**
	 * Testing that the CardDeck constructor is correctly setting the instance's
	 * deck number.
	 */
	@Test
	public void testCardDeck()
	{
		int deckNum = 45;
		CardDeck cardDeck = new CardDeck(deckNum);

		int resultDeckNumber = 0;
		try
		{
			// Retrieving the deckNumber, a private field.
			Field deckNumberField = CardDeck.class.getDeclaredField("deckNumber");
			deckNumberField.setAccessible(true);
			resultDeckNumber = (int) deckNumberField.get(cardDeck);
		} catch (NoSuchFieldException e)
		{
			fail();
		} catch (SecurityException e)
		{
			fail();
		} catch (IllegalArgumentException e)
		{
			fail();
		} catch (IllegalAccessException e)
		{
			fail();
		}

		assertEquals(deckNum, resultDeckNumber);
	}

	/**
	 * Testing that CardDeck.getDeckNumber() returns the correct value.
	 */
	@Test
	public void testGetDeckNumber()
	{
		int deckNum = 45;
		CardDeck cardDeck = new CardDeck(deckNum);

		assertEquals(deckNum, cardDeck.getDeckNumber());
	}

	/**
	 * Testing that CardDeck.getDeck() returns a queue of cards.
	 */
	@Test
	public void testGetDeck()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		cardDeck.addCard(new Card(1));

		assertTrue(cardDeck.getDeck() != null);
		assertEquals(cardDeck.getDeck().size(), 1);
	}

	/**
	 * Testing that CardDeck.addCard(card) successfully adds a card to a deck.
	 */
	@Test
	public void testAddCard()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		Card card = new Card(deckNum);

		cardDeck.addCard(card);
		assertEquals(card, cardDeck.getDeck().peek());
		assertEquals(cardDeck.getDeck().size(), 1);
	}

	/**
	 * Testing that CardDeck.removeCard(card) successfully removes a card from a
	 * deck.
	 */
	@Test
	public void testRemoveCard()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		Card card = new Card(deckNum);

		cardDeck.addCard(card);
		try
		{
			cardDeck.removeCard(card);
		} catch (DeckEmptyException e)
		{
			fail();
		}

		assertEquals(cardDeck.getDeck().size(), 0);
	}

	/**
	 * Tests that CardDeck.takeCard() successfully takes the first card out of the
	 * deck and returns it.
	 */
	@Test
	public void testTakeCard()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		Card card = new Card(deckNum);
		Card card2 = new Card(deckNum + 1);
		Card card3 = new Card(deckNum + 2);
		Card card4 = new Card(deckNum + 3);
		Card takenCard = null;

		cardDeck.addCard(card);
		cardDeck.addCard(card2);
		cardDeck.addCard(card3);
		cardDeck.addCard(card4);
		try
		{
			takenCard = cardDeck.takeCard();
		} catch (DeckEmptyException e)
		{
			fail();
		}

		assertEquals(takenCard, card);
		assertEquals(cardDeck.getDeck().size(), 3);
		assertNotEquals(takenCard, card2);
		assertNotEquals(takenCard, card3);
		assertNotEquals(takenCard, card4);
	}

	/**
	 * Tests that the deck successfully throws a DeckEmptyException when a card is
	 * taken/removed when it is already empty.
	 */
	@Test
	public void testDeckEmptyException()
	{
		int deckNum = 1;
		CardDeck cardDeck = new CardDeck(deckNum);
		try
		{
			cardDeck.takeCard();
			fail();
		} catch (DeckEmptyException e)
		{
			// Pass - throws Exception successfully
		}
	}
}
