package ecm2414.cardgame;

import java.util.ArrayList;
import java.util.List;

import ecm2414.cardgame.exceptions.DeckEmptyException;
import ecm2414.cardgame.exceptions.HandEmptyException;
import ecm2414.cardgame.exceptions.HandFullException;

public class Player implements Runnable
{
	public static final int MAX_CARDS = 4;

	public final int playerNumber;

	public Object lock;
	public CardGame cardGame;

	private final String playerLogPath;
	private final String deckLogPath;

	private List<Card> hand;
	private int numberOfCards;

	public Player(CardGame cardGame, int playerNumber)
	{
		this.lock = new Object();
		this.cardGame = cardGame;
		this.playerNumber = playerNumber;
		this.hand = new ArrayList<Card>();

		this.playerLogPath = "Output/player" + this.playerNumber + "_output.txt";
		this.deckLogPath = "Output/deck" + this.playerNumber + "_output.txt";
	}

	/**
	 * @return the reference to the current player's hand.
	 */
	public List<Card> getHand()
	{
		return hand;
	}

	/**
	 * @return the number of cards in the player's hand.
	 */
	public int getNumberOfCards()
	{
		return this.numberOfCards;
	}

	/**
	 * Adds a card to the player's hand, non-synchronised as it is only used on a singular thread.
	 * @param card the card which the player wants to add.
	 * @throws HandFullException when the hand is already at a full capacity of {@value #MAX_CARDS}.
	 */
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

	/**
	 * Removes a card from the player's hand, non-synchronised as it is only used on a singular thread.
	 * @param card the card which the player wants to remove.
	 * @throws HandEmptyException when the hand is already empty.
	 */
	public void remFromHand(Card card) throws HandEmptyException
	{
		if (this.numberOfCards <= 0)
		{
			throw new HandEmptyException("No cards in hand");
		}

		hand.remove(card);
		this.numberOfCards--;
	}

	/**
	 * Checks a card to see if it's value is preferred by the player.
	 * @param card the card to check
	 * @return whether the card is preferred by the player.
	 */
	private boolean isPreferred(Card card)
	{
		return card.denomination == this.playerNumber;
	}

	/**
	 * Checks every card in the player's hand to see if they have won.
	 * @return whether the player has won (has a full deck of the same card).
	 */
	public boolean hasWon()
	{
		if (this.getNumberOfCards() == MAX_CARDS)
		{
			int lastCardValue = Integer.MIN_VALUE;
			
			for (Card card : this.hand)
			{
				if(lastCardValue == Integer.MIN_VALUE)
				{
					lastCardValue = card.denomination;
				}
				else
				{
					if(lastCardValue != card.denomination)
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Makes a player take their turn, picking up a card from the left deck, discarding a card in their hand and replacing
	 * it with the card they just picked up. The discarded card gets placed in the right deck.
	 * @param deckLeft the deck the player takes from.
	 * @param deckRight the deck the player discards to.
	 * @throws HandEmptyException thrown when their hand is empty so no cards can be taken from it.
	 * @throws HandFullException thrown when their hand is full so no more cards can be placed in it.
	 * @throws DeckEmptyException thrown when the deck to their left is empty so no card can be taken from it.
	 */
	public void takeTurn(CardDeck deckLeft, CardDeck deckRight) throws HandEmptyException, HandFullException, DeckEmptyException
	{
		// Take card from the left deck.
		Card takenCard = deckLeft.takeCard();
//		System.out.println("    " + this + " draws a " + takenCard + " from " + deckLeft);
		CardGameUtil.appendToFile(this.playerLogPath, this + " draws a " + takenCard + " from " + deckLeft);
		
		// Make a list of all non-preferred cards in the hand.
		List<Card> tempCards = new ArrayList<Card>();
		for (Card card : this.hand)
		{
			if (!this.isPreferred(card))
			{
				tempCards.add(card);
			}
		}

		// Choose a card index in random, then move it to the right deck, and take the card picked up from the left.
		int cardRemoveIndex = (int) Math.floor(Math.random() * tempCards.size());
		Card removedCard = tempCards.remove(cardRemoveIndex);
		if (removedCard != null)
		{
//			System.out.println("    " + this + " has discards a " + takenCard + " to " + deckRight);
			CardGameUtil.appendToFile(this.playerLogPath, this + " discards a " + removedCard + " to " + deckRight);
			remFromHand(removedCard);
			deckRight.addCard(removedCard);

//			System.out.println("[!] " + this + " puts " + takenCard + " into their hand.");
			CardGameUtil.appendToFile(this.playerLogPath, this + " puts " + takenCard + " into their hand");
			addToHand(takenCard);
		}
//		System.out.println("    " + this + " current hand is " + CardGameUtil.listToString(this.hand));
		CardGameUtil.appendToFile(this.playerLogPath, this + " current hand is " + CardGameUtil.collectionToString(this.hand));
	}
	
	@Override
	public void run()
	{
		// Clean the files.
		CardGameUtil.clearFile(this.deckLogPath);
		CardGameUtil.clearFile(this.playerLogPath);
		CardGameUtil.appendToFile(this.playerLogPath, this + " initial hand: " + this.hand);

		/*
		 * While there is no winners to the card game...
		 */
		while (!cardGame.playerHasWon.get())
		{
			int previousPlayerNumber = (playerNumber - 1) - 1 % cardGame.getPlayerCount();
			if (previousPlayerNumber < 0)
				previousPlayerNumber += cardGame.getPlayerCount();
			Player previousPlayer = cardGame.getPlayers().get(previousPlayerNumber);
			
			/*
			 * Wait upon the previous player's lock, the initial unlock will come from the main thread.
			 */
			try
			{
				synchronized (previousPlayer.lock)
				{
					previousPlayer.lock.wait();
				}

			} catch (InterruptedException e)
			{
				// Restart the while block, waiting again.
				continue;
			}

			/*
			 * If a player has won, break out of the while.
			 */
			if (cardGame.playerHasWon.get())
			{
				break;
			}

			/*
			 * If this player has won, aka - they have 4 of the same card, append it to file and tell set the flags.
			 * Then break out of the while loop.
			 */
			if (this.hasWon())
			{
				System.out.println("Player " + playerNumber + " has won!");
				CardGameUtil.appendToFile(this.playerLogPath, this + " wins");
				cardGame.playerHasWon.set(true);
				cardGame.winningPlayer.set(this);
				break;
			}

			/*
			 * Have a player take a turn, using the left-side deck and right-side deck.
			 * If there is any hand is empty, there is a fatal error with the program.
			 * If there is any hand that is full when trying to add the taken card, there is a fatal error.
			 * If an entire deck is empty, there is another fatal error and the program can't continue.
			 */
			try
			{
				takeTurn(cardGame.getCardDecks().get(playerNumber - 1), cardGame.getCardDecks().get((playerNumber) % cardGame.getPlayerCount()));
			} catch (HandEmptyException e)
			{
				System.err.println("Fatal error: no player should have an empty hand.");
			} catch (HandFullException e)
			{
				System.err.println("Fatal error: no player should have full hand.");
			} catch (DeckEmptyException e)
			{
				System.err.println("Fatal error: no deck should be empty.");
			}

			// Check if it has won after.
			if (!cardGame.playerHasWon.get() && this.hasWon())
			{
				System.out.println("Player " + playerNumber + " has won!");
				CardGameUtil.appendToFile(this.playerLogPath, this + " wins");
				cardGame.playerHasWon.set(true);
				cardGame.winningPlayer.set(this);
				;
				break;
			}
			// Notify any threads waiting on this player that it has finished.
			CardGameUtil.notifyLock(this.lock);
		}
		// Notify any threads that have just exited the while loop (either because they won or another player has won) that they can continue.
		CardGameUtil.notifyLock(this.lock);

		/*
		 * Log that this player has ended and display their final hand.
		 * Log the deck to the left of this player.
		 */
		CardGameUtil.appendToFile(this.playerLogPath, this + " exits");
		CardGameUtil.appendToFile(this.playerLogPath, this + " final hand: " + CardGameUtil.collectionToString(this.hand));
		CardGameUtil.appendToFile(this.deckLogPath, "deck" + this.playerNumber + " contents: " + CardGameUtil.collectionToString(cardGame.getCardDecks().get(this.playerNumber - 1).getDeck()));
	}

	/**
	 * Returns a String representation of this object, being that it is a "player" followed by its {@link #playerNumber}.
	 */
	public String toString()
	{
		return "player " + this.playerNumber;
	}
}
