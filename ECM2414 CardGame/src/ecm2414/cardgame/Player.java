package ecm2414.cardgame;

import java.util.ArrayList;
import java.util.List;

import ecm2414.cardgame.exceptions.HandEmptyException;
import ecm2414.cardgame.exceptions.HandFullException;
import ecm2414.cardgame.exceptions.WinConditionException;

public class Player implements Runnable
{
	public static final int MAX_CARDS = 4;
	private List<Card> hand;
	public final int playerNumber;
	private int numberOfCards;

	public Object lock;

	public CardGame cardGame;

	public Player(CardGame cardGame, int playerNumber)
	{
		this.lock = new Object();
		this.cardGame = cardGame;
		this.playerNumber = playerNumber;
		this.hand = new ArrayList<Card>();
	}

	public List<Card> getHand()
	{
		return hand;
	}

	public int getNumberOfCards()
	{
		return this.numberOfCards;
	}

	public void addToHand(Card card) throws HandFullException
	{
		if (this.getNumberOfCards() < MAX_CARDS)
		{
			hand.add(card);
			this.numberOfCards++;
		} else
		{
			throw new HandFullException("Hand is full");
		}
	}

	public void discardCard(Card card) throws HandEmptyException
	{
		if (this.numberOfCards <= 0)
		{
			throw new HandEmptyException("No cards in hand");
		}

		hand.remove(card);
		this.numberOfCards--;
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

	public void takeTurn(CardDeck deckLeft, CardDeck deckRight) throws HandEmptyException, HandFullException, WinConditionException
	{
		Card takenCard = deckLeft.takeCard();
		System.out.println("    " + this + " has taken a " + takenCard + " from " + deckLeft);

		if (this.isPreferred(takenCard))
		{
			boolean removedCard = false;
			for (Card card : this.hand)
			{
				if (!this.isPreferred(card))
				{
					this.discardCard(card);
					deckRight.addCard(card);
					System.out.println("    " + this + " has discarded " + card + " from their hand to " + deckRight);
					removedCard = true;
					break;
				}
			}

			if (removedCard)
			{
				this.addToHand(takenCard);
				System.out.println("[!] " + this + " has added " + takenCard + " to their hand.");
			} else
			{
				if (this.hasWon())
				{
					// TODO notify threads that player has won
					System.out.println("Player " + playerNumber + " has won!");
				} else
				{
					throw new WinConditionException("Player should have won, but hasn't");
				}
			}
		} else
		{
			deckRight.addCard(takenCard);
			System.out.println("    " + this + " picked up a non-favourable card and has placed it in " + deckRight);
		}
	}

	@Override
	public void run()
	{
		while (!cardGame.playerHasWon)
		{

			int previousPlayerNumber = (playerNumber - 1) - 1 % cardGame.getPlayerCount();
			if (previousPlayerNumber < 0)
				previousPlayerNumber += cardGame.getPlayerCount();
			Player previousPlayer = cardGame.getPlayers().get(previousPlayerNumber);
			try
			{
				synchronized (previousPlayer.lock)
				{
//					System.out.println(playerNumber + " is waiting on previous lock...");
					previousPlayer.lock.wait();
				}
//				try
//				{
//					//TODO: Not ideal
//					Thread.sleep(1000);
//				} catch (InterruptedException e)
//				{
//					e.printStackTrace();
//				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
//			System.out.println(playerNumber + " is no longer waiting on previous lock.");

			// Check if it has won before.
			if (!cardGame.playerHasWon && this.hasWon())
			{
				//TODO: Implement proper win state.
				System.out.println("Player " + playerNumber + " has won!");
				cardGame.playerHasWon = true;
				cardGame.winningPlayer = this;
				break;
			}
			
			try
			{
				takeTurn(cardGame.getCardDecks().get(playerNumber - 1), cardGame.getCardDecks().get((playerNumber) % cardGame.getPlayerCount()));
			} catch (HandEmptyException e)
			{
				System.err.println("Fatal error: no player should have an empty hand.");
			} catch (HandFullException e)
			{
				System.err.println("Fatal error: no player should have full hand.");
			} catch (WinConditionException e)
			{
				System.err.println("Fatal error: " + e.getMessage());
			}
			
			// Check if it has won after.
			if (!cardGame.playerHasWon && this.hasWon())
			{
				//TODO: Implement proper win state.
				System.out.println("Player " + playerNumber + " has won!");
				cardGame.playerHasWon = true;
				cardGame.winningPlayer = this;
				break;
			}

			synchronized (this.lock)
			{
//				System.out.println(playerNumber + " is locking itself...");
				this.lock.notify();
			}
		}
	}

	public String toString()
	{
		return "Player[" + this.playerNumber + "]";
	}
}
