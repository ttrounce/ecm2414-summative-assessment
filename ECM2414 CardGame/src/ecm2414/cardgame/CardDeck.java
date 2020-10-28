package ecm2414.cardgame;

import java.util.ArrayDeque;
import java.util.Queue;

import ecm2414.cardgame.exceptions.DeckEmptyException;

/**
 * A class representation of a deck, with a number and a queue of cards.
 */
public class CardDeck
{
	private Queue<Card> deck;
	private final int deckNumber;
	private int numberOfCards;

	public CardDeck(int deckNumber)
	{
		this.deckNumber = deckNumber;
		this.deck = new ArrayDeque<Card>();
	}

	/**
	 * @return the queue of cards.
	 */
	public Queue<Card> getDeck()
	{
		return deck;
	}

	/**
	 * @return the deck number (id).
	 */
	public int getDeckNumber()
	{
		return this.deckNumber;
	}

	/**
	 * @return the number of cards in the deck.
	 */
	public int getNumberOfCards()
	{
		return this.numberOfCards;
	}

	/**
	 * Adds a card to the end of the queue deck.
	 * @param card card to add.
	 */
	public void addCard(Card card)
	{
		deck.offer(card);
		this.numberOfCards++;
	}

	/**
	 * Removes a specified card from the deck.
	 * @param card card to remove.
	 * @throws DeckEmptyException thrown when the deck is empty.
	 */
	public void removeCard(Card card) throws DeckEmptyException
	{
		if (this.getNumberOfCards() <= 0)
		{
			throw new DeckEmptyException("Deck is empty - cannot take card");
		}

		deck.remove(card);
		this.numberOfCards--;
	}

	/**
	 * Removes the first card in the queue (deck).
	 * @return the card at the front of the deck.
	 * @throws DeckEmptyException thrown when the deck is empty.
	 */
	public Card takeCard() throws DeckEmptyException
	{
		if (this.getNumberOfCards() <= 0)
		{
			throw new DeckEmptyException("Deck is empty - cannot take card");
		}
		Card card = deck.remove();
		this.numberOfCards--;
		return card;
	}

	@Override
	public String toString()
	{
		return "deck " + deckNumber;
	}
}
