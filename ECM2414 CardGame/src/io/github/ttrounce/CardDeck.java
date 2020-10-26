package io.github.ttrounce;

import java.util.Stack;

public class CardDeck
{
	private Stack<Card> deck;
	private final int deckNumber;
	private int numberOfCards;
	
	public CardDeck(int deckNumber)
	{
		this.deckNumber = deckNumber;
		this.deck = new Stack<Card>();
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
		deck.add(card);
	}
	
	public void removeCard(Card card) throws DeckEmptyException
	{
		if (this.getNumberOfCards() <= 0)
		{
			throw new DeckEmptyException("Deck is empty - cannot take card");
		}
		
		deck.remove(card);
	}
	
	public Card takeCard()
	{
		Card card = deck.lastElement();
		return card;
	}
}
