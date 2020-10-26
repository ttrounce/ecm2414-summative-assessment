package ecm2414.cardgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ecm2414.cardgame.exceptions.HandFullException;
import ecm2414.cardgame.exceptions.InvalidCardInputException;
import ecm2414.cardgame.exceptions.NotEnoughCardsException;

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
			System.out.println("Error reading input.");
		} catch (NotEnoughCardsException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public boolean playerHasWon;

	private List<Card> cards;
	private int playerCount;

	private List<Player> players;
	private List<CardDeck> cardDecks;

	/*
	 * Starts by reading the user input and then runs the card game.
	 */
	public CardGame()
	{
		this.players = new ArrayList<Player>();
		this.cardDecks = new ArrayList<CardDeck>();
		this.cards = new ArrayList<Card>();
	}
	
	public void startGame(InputStream inputStream) throws IOException, NotEnoughCardsException
	{
		setupInput(inputStream);
		setupGame();
		setupThreads();

		try
		{
			Thread.sleep(5000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println();

		synchronized (players.get(this.playerCount - 1).lock)
		{
			players.get(this.playerCount - 1).lock.notify();
		}
	}
	
	public void setupInput(InputStream inputStream) throws IOException
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
				
				if(input == null)
				{
					//ran out of input.
					throw new IOException("End of input reached");
				}
				this.playerCount = Integer.parseInt(input);
				if (this.playerCount < 2)
				{
					throw new NumberFormatException("Player count cannot be below 2");
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
				loadPack(file);
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

	public void setupGame() throws NotEnoughCardsException
	{
		for (int i = 0; i < this.playerCount; i++)
		{
			Player player = new Player(this, i + 1);
			players.add(player);

			CardDeck deck = new CardDeck(i + 1);
			cardDecks.add(deck);
		}

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

		players.forEach(player ->
		{
			System.out.println(player + " " + Arrays.deepToString(player.getHand().toArray(new Card[1])));
		});
		cardDecks.forEach(deck ->
		{
			System.out.println(deck + " " + deck.getDeck());
		});
	}

	public void setupThreads()
	{
		List<Thread> threads = new ArrayList<Thread>();
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

	public void loadPack(File packFile) throws IOException, InvalidCardInputException
	{
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
				cards.add(new Card(cardDenom));

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
		if (validCards != 8 * playerCount)
		{
			reader.close();
			throw new InvalidCardInputException("Card packs must be 8x the number of players");
		}

		System.out.println("Valid");

		reader.close();
	}

	public List<Card> getAllCards()
	{
		return cards;
	}
	
	public List<Player> getPlayers()
	{
		return players;
	}

	public int getPlayerCount()
	{
		return playerCount;
	}

	public List<CardDeck> getCardDecks()
	{
		return cardDecks;
	}
}