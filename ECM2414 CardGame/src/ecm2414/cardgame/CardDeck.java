package ecm2414.cardgame;

import java.util.ArrayDeque;
import java.util.Queue;

import ecm2414.cardgame.exceptions.DeckEmptyException;

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

	public Queue<Card> getDeck()
	{
		return deck;
	}

	public int getDeckNumber()
	{
		return this.deckNumber;
	}

	public int getNumberOfCards()
	{
		return this.numberOfCards;
	}

	public void addCard(Card card)
	{
		deck.offer(card);
		this.numberOfCards++;
	}

	public void removeCard(Card card) throws DeckEmptyException
	{
		if (this.getNumberOfCards() <= 0)
		{
			throw new DeckEmptyException("Deck is empty - cannot take card");
		}

		deck.remove(card);
		this.numberOfCards--;
	}

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
