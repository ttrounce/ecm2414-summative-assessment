package io.github.ttrounce;

import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable
{
	public static final int MAX_CARDS = 4;
	private List<Card> hand;
	public final int playerNumber;
	private int numberOfCards;
	
	public Player(int playerNumber)
	{
		this.playerNumber = playerNumber;
		this.hand = new ArrayList<Card>();
	}	
	
	public int getNumberOfCards()
	{
		return this.numberOfCards;
	}
	
	public void addToHand(Card card) throws FullHandException
	{
		if (this.getNumberOfCards() < MAX_CARDS)
		{
			hand.add(card);
			this.numberOfCards++;
		}
		else
		{
			throw new FullHandException("Hand is full");
		}
	}
	
	public void discardCard(Card card) throws EmptyHandException
	{
		if (this.numberOfCards <= 0)
		{
			throw new EmptyHandException("No cards in hand");
		}
		
		hand.remove(card);
	}
	
	private boolean isPreferred(Card card)
	{
		return card.denomination == this.playerNumber;
	}
	
	public boolean hasWon()
	{
		if (this.getNumberOfCards() == MAX_CARDS)
		{
			for (Card card : this.hand)
			{
				if (!this.isPreferred(card))
				{
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public void takeTurn(CardDeck deckLeft, CardDeck deckRight) throws EmptyHandException, FullHandException, WinConditionException
	{
		Card takenCard = deckLeft.takeCard();
		
		if (this.isPreferred(takenCard))
		{
			boolean removedCard = false;
			for (Card card : this.hand)
			{
				if (!this.isPreferred(card))
				{
					this.discardCard(card);
					deckRight.addCard(card);
					removedCard = true;
					break;
				}
			}
			
			if (removedCard)
			{
				this.addToHand(takenCard);
			}
			else
			{
				if (this.hasWon())
				{
					// TODO notify threads that player has won
				}
				else
				{
					throw new WinConditionException("Player should have won, but hasn't");
				}
			}
		}
		else
		{
			deckRight.addCard(takenCard);
		}
	}
	
	@Override
	public void run() 
	{	
		boolean isDone = false;
		while(!isDone)
		{
			
		}
	}
}
