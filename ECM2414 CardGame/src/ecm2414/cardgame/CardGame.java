package ecm2414.cardgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import ecm2414.cardgame.exceptions.HandFullException;
import ecm2414.cardgame.exceptions.InvalidCardInputException;
import ecm2414.cardgame.exceptions.NotEnoughCardsException;
import ecm2414.cardgame.exceptions.NotEnoughPlayersException;

public class CardGame
{

	public static void main(String[] args)
	{
		try
		{
			CardGame cardGame = new CardGame();
			cardGame.startGame(System.in);
		} catch (IOException e)
		{
			// Print that there was an error reading the input.
			System.err.println("Error reading input.");
		} catch (NotEnoughCardsException e)
		{
			System.err.println(e.getMessage());
		} catch (NotEnoughPlayersException e)
		{
			System.err.println(e.getMessage());
		}
	}

	/**
	 * An atomic boolean so that each thread can atomically identify each other when one has won.
	 */
	public volatile AtomicBoolean playerHasWon;
	/**
	 * An atomic reference to a player, set by a thread that has won.
	 */
	public volatile AtomicReference<Player> winningPlayer;

	private List<Card> cards;
	private int playerCount;

	private List<Player> players;
	private List<CardDeck> cardDecks;

	private List<Thread> threads;

	/*
	 * Starts by reading the user input and then runs the card game.
	 */
	public CardGame()
	{
		this.players = new ArrayList<Player>();
		this.cardDecks = new ArrayList<CardDeck>();
		this.cards = new ArrayList<Card>();

		this.threads = new ArrayList<Thread>();

		this.playerHasWon = new AtomicBoolean(false);
		this.winningPlayer = new AtomicReference<Player>();
	}

	/**
	 * Starts the game by requesting inputs from the user, distributing the cards
	 * and then creating and running the threads.
	 * 
	 * @param inputStream the input stream of which to read the user input.
	 * @throws IOException               thrown when the end of input is reached.
	 * @throws NotEnoughCardsException   thrown when there aren't enough cards in
	 *                                   the game to function, this should not
	 *                                   happen and would indicate a large error.
	 * @throws NotEnoughPlayersException thrown when there aren't enough players in
	 *                                   the game, this should not happen and would
	 *                                   indicate the user has bypassed the initial
	 *                                   request for 2 or more players.
	 */
	public void startGame(InputStream inputStream) throws IOException, NotEnoughCardsException, NotEnoughPlayersException
	{
		setupInput(inputStream);
		setupGame();
		setupThreads();

		// Wait for all threads to be in a WAITING state.
		boolean allThreadsWaiting = false;
		while (!allThreadsWaiting)
		{
			allThreadsWaiting = true;
			for (Thread th : threads)
			{
				allThreadsWaiting = allThreadsWaiting && th.getState() == State.WAITING;
			}
		}

		System.out.println("Beginning game.");

		// Unlock the first player waiting on the last player.
		synchronized (players.get(this.playerCount - 1).lock)
		{
			players.get(this.playerCount - 1).lock.notify();
		}
	}

	/**
	 * Requests input from the user, repeating the question is invalid input is
	 * given.
	 * 
	 * @param inputStream the input stream in which to read user input.
	 * @throws IOException               thrown when the end of input has reached.
	 * @throws NotEnoughPlayersException thrown when there aren't enough players in
	 *                                   the game, this should not happen and would
	 *                                   indicate the user has bypassed the initial
	 *                                   request for 2 or more players.
	 */
	public void setupInput(InputStream inputStream) throws IOException, NotEnoughPlayersException
	{
		// Create a BufferedReader to read user input.
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		/*
		 * Asks user for the number of players. Repeatedly asks until given valid input.
		 */
		boolean validIntegerFound = false;
		while (!validIntegerFound)
		{
			System.out.println("Please enter the number of players: ");
			try
			{
				String input = reader.readLine();

				if (input == null)
				{
					// ran out of input.
					throw new IOException("End of input reached");
				}
				this.playerCount = Integer.parseInt(input);
				// if there aren't enough players, repeat the question to the user.
				if (this.playerCount < 2)
				{
					System.out.println("Not enough players: there must be 2 or more.");
					continue;
				}
				// exit loop
				validIntegerFound = true;
			} catch (NumberFormatException e)
			{
				System.out.println("Invalid number: " + e.getMessage());
			}
		}

		/*
		 * Asks user for the path of the pack file. Repeatedly asks until valid pack
		 * file is given.
		 */
		boolean inputCardsFound = false;
		while (!inputCardsFound)
		{
			System.out.println("Please enter the location of the pack to load:");

			String packPath = reader.readLine();

			/*
			 * packPath is null if we run out of input. Used mostly for testing purposes.
			 */
			if (packPath == null)
			{
				throw new IOException("End of input reached");
			}

			// Create file and check if it exists, if not, ask user again.
			File file = new File(packPath);
			if (!file.exists())
			{
				System.out.println("File path cannot be found.");
				continue;
			}

			// Try to load in the pack, and throws exception for invalid cards or invalid
			// pack sizes and asks the user to provide a valid pack again.
			try
			{
				this.cards = loadPack(file, this.playerCount);
			} catch (InvalidCardInputException e)
			{
				System.err.println(e.getMessage() + " - card pack is invalid, please try again.");
				continue;
			}
			// exit loop
			inputCardsFound = true;
		}
		reader.close();
	}

	/**
	 * Loads a pack file with a certain amount of players.
	 * 
	 * @param packFile the file in which to read the cards.
	 * @param players  the amount of players in the game (used to validate the pack
	 *                 file).
	 * @return a list of valid cards read from the file.
	 * @throws IOException               thrown if an I/O error occurs.
	 * @throws InvalidCardInputException thrown if the pack is invalid (either there
	 *                                   are invalid cards or there is a lack/excess
	 *                                   of cards in the pack).
	 * @throws NotEnoughPlayersException thrown if there are not enough players in
	 *                                   this game.
	 */
	public List<Card> loadPack(File packFile, int players) throws IOException, InvalidCardInputException, NotEnoughPlayersException
	{
		if (players < 2)
		{
			throw new NotEnoughPlayersException("There must be 2 or more players");
		}

		List<Card> cardList = new ArrayList<Card>();

		// BufferedReader to read pack file.
		BufferedReader reader = new BufferedReader(new FileReader(packFile));

		int validCards = 0;

		// Read every line from the pack file.
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			// Checks if the card is a valid integer, if not throws an Exception caught
			// above in the call chain.
			try
			{
				int cardDenom = Integer.parseInt(line);
				if (cardDenom < 0)
				{
					throw new NumberFormatException("Card values cannot be negative");
				}

				// add card to list of cards.
				cardList.add(new Card(cardDenom));

				// increment the number of valid cards found.
				validCards++;
			} catch (NumberFormatException e)
			{
				reader.close();
				throw new InvalidCardInputException("Cards must be non-negative integers");
			}
		}

		/*
		 * If the number of valid cards does not equal 8*#players, throws an exception
		 * caught above in the call chain.
		 */
		if (validCards != 8 * players)
		{
			reader.close();
			throw new InvalidCardInputException("Card packs must be 8x the number of players");
		}

		reader.close();
		return cardList;
	}

	/**
	 * Setups up the game, and distributes the cards between players and decks.
	 * 
	 * @throws NotEnoughCardsException thrown if there aren't enough cards in the
	 *                                 game to play, this shouldn't happen and would
	 *                                 suggest a large error has occurred.
	 */
	public void setupGame() throws NotEnoughCardsException
	{
		// Creates the players and decks.
		for (int i = 0; i < this.playerCount; i++)
		{
			Player player = new Player(this, i + 1);
			players.add(player);

			CardDeck deck = new CardDeck(i + 1);
			cardDecks.add(deck);
		}

		/*
		 * Start distributing cards until one of the player's hands are full.
		 */
		int index = 0;
		boolean distributingPlayerCards = true;
		while (distributingPlayerCards)
		{
			for (Player player : players)
			{
				try
				{
					if (index < cards.size())
					{
						player.addToHand(cards.get(index));
						index++;
					} else
					{
						throw new NotEnoughCardsException("There were not enough cards in the pack to allow the game to function.");
					}
				} catch (HandFullException e)
				{
					distributingPlayerCards = false;
					break;
				}
			}
		}

		/*
		 * Start distributing cards to the decks until there are no more cards left.
		 */
		boolean distributingDeckCards = true;
		while (distributingDeckCards)
		{

			for (CardDeck deck : cardDecks)
			{
				if (index < cards.size())
				{
					deck.addCard(cards.get(index++));
				} else
				{
					// ran out of cards..
					distributingDeckCards = false;
				}
			}
		}
	}

	/**
	 * Create the threads and run them.
	 */
	public void setupThreads()
	{
		for (Player player : players)
		{
			Thread thread = new Thread(player);
			threads.add(thread);
		}

		for (Thread thread : threads)
		{
			thread.start();
		}
	}

	/**
	 * @return a list of every card in this game, provided by the user.
	 */
	public List<Card> getAllCards()
	{
		return cards;
	}

	/**
	 * @return a list of players in this game.
	 */
	public List<Player> getPlayers()
	{
		return players;
	}

	/**
	 * @return the amount of players in this game.
	 */
	public int getPlayerCount()
	{
		return playerCount;
	}

	/**
	 * @return a list of decks in this game.
	 */
	public List<CardDeck> getCardDecks()
	{
		return cardDecks;
	}
}