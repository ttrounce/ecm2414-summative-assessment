package ecm2414.cardgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ecm2414.cardgame.exceptions.NotEnoughCardsException;
import ecm2414.cardgame.exceptions.NotEnoughPlayersException;

public class CardGameTest
{

	/**
	 * Tests the CardGame constructor, and tests that each field is initialised.
	 */
	@Test
	public void testCardGame()
	{
		CardGame cardGame = new CardGame();

		try
		{
			Field playersField = CardGame.class.getDeclaredField("players");
			playersField.setAccessible(true);
			assertTrue(playersField.get(cardGame) != null);

			Field cardDecksField = CardGame.class.getDeclaredField("cardDecks");
			cardDecksField.setAccessible(true);
			assertTrue(cardDecksField.get(cardGame) != null);

			Field cardsField = CardGame.class.getDeclaredField("cards");
			cardsField.setAccessible(true);
			assertTrue(cardsField.get(cardGame) != null);

			Field threadsField = CardGame.class.getDeclaredField("threads");
			threadsField.setAccessible(true);
			assertTrue(threadsField.get(cardGame) != null);
		} catch (IllegalAccessException e)
		{
			fail();
		} catch (NoSuchFieldException e)
		{
			fail();
		} catch (SecurityException e)
		{
			fail();
		}

		assertTrue(cardGame.shouldShutdown != null && cardGame.shouldShutdown.get() == false);
		assertTrue(cardGame.playerHasWon != null && cardGame.playerHasWon.get() == false);
		assertTrue(cardGame.winningPlayer != null);
	}

	/**
	 * Tests that the CardGame.waitForShutdown() works by rejoining all of the
	 * CardGame Player threads together.
	 */
	@Test
	public void testWaitForShutdown()
	{
		String testInput = "4\nTests/winningTestPack.txt";
		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());

		CardGame cardGame = new CardGame();
		try
		{
			cardGame.startGame(in);
		} catch (IOException e)
		{
			fail();
		} catch (NotEnoughCardsException e)
		{
			fail();
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
		try
		{
			cardGame.waitForShutdown();
		} catch (InterruptedException e)
		{
			fail();
		}
	}

	/**
	 * Tests the CardGame.setupGame() function, by making sure that each player and
	 * card deck has been initialised, and that the cards have been distributed
	 * among them.
	 */
	@Test
	public void testSetupGame()
	{
		String testInput = "4\nTests/winningTestPack.txt";
		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());

		CardGame cardGame = new CardGame();
		try
		{
			cardGame.setupInput(in);
			cardGame.setupGame();

			// players assert

			List<Player> players = new ArrayList<Player>();
			try
			{
				/*
				 * Using reflection to get the List of players, then loading each one into the
				 * new list.
				 */
				Field playersField = CardGame.class.getDeclaredField("players");
				playersField.setAccessible(true);
				Object fieldResult = playersField.get(cardGame);
				if (fieldResult instanceof List<?>)
				{
					List<?> list = (List<?>) fieldResult;
					for (Object o : list)
					{
						if (o instanceof Player)
							players.add((Player) o);
					}
				}
			} catch (IllegalArgumentException e)
			{
				fail("Internal error");
			} catch (IllegalAccessException e)
			{
				fail("Internal error");
			} catch (NoSuchFieldException e)
			{
				fail("Internal error");
			} catch (SecurityException e)
			{
				fail("Internal error");
			}
			/*
			 * Check that there are 4 players as expected and that their hand is not empty.
			 */
			assertEquals(players.size(), 4);
			for (Player player : players)
			{
				assertTrue(!player.getHand().isEmpty());
			}

			// card deck assert

			List<CardDeck> cardDecks = new ArrayList<CardDeck>();
			try
			{
				/*
				 * Using reflection to get the CardDecks, and add each one to the local list of
				 * CardDecks to test.
				 */
				Field cardDecksField = CardGame.class.getDeclaredField("cardDecks");
				cardDecksField.setAccessible(true);
				Object fieldResult = cardDecksField.get(cardGame);
				if (fieldResult instanceof List<?>)
				{
					List<?> list = (List<?>) fieldResult;
					for (Object o : list)
					{
						if (o instanceof CardDeck)
							cardDecks.add((CardDeck) o);
					}
				}
			} catch (IllegalArgumentException e)
			{
				fail("Internal error");
			} catch (IllegalAccessException e)
			{
				fail("Internal error");
			} catch (NoSuchFieldException e)
			{
				fail("Internal error");
			} catch (SecurityException e)
			{
				fail("Internal error");
			}
			/*
			 * Check that there are 4 card decks and that they're not empty.
			 */
			assertEquals(cardDecks.size(), 4);
			for (CardDeck deck : cardDecks)
			{
				assertTrue(!deck.getDeck().isEmpty());
			}
		} catch (IOException e)
		{
			fail();
		} catch (NotEnoughCardsException e)
		{
			fail();
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
}
