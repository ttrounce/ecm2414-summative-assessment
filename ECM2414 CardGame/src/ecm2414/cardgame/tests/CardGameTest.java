package ecm2414.cardgame.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ecm2414.cardgame.CardDeck;
import ecm2414.cardgame.CardGame;
import ecm2414.cardgame.Player;
import ecm2414.cardgame.exceptions.NotEnoughCardsException;
import ecm2414.cardgame.exceptions.NotEnoughPlayersException;

public class CardGameTest
{

	@Test
	public void testCardGame() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		CardGame cardGame = new CardGame();

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

		assertTrue(cardGame.shouldShutdown != null && cardGame.shouldShutdown.get() == false);
		assertTrue(cardGame.playerHasWon != null && cardGame.playerHasWon.get() == false);
		assertTrue(cardGame.winningPlayer != null);
	}

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
			assertEquals(players.size(), 4);

			// card deck assert

			List<CardDeck> cardDecks = new ArrayList<CardDeck>();
			try
			{
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
			assertEquals(cardDecks.size(), 4);
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
