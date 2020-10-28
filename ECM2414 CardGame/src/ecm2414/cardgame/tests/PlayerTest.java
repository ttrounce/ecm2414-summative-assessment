package ecm2414.cardgame.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.Thread.State;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ecm2414.cardgame.Card;
import ecm2414.cardgame.CardDeck;
import ecm2414.cardgame.CardGame;
import ecm2414.cardgame.Player;
import ecm2414.cardgame.exceptions.DeckEmptyException;
import ecm2414.cardgame.exceptions.HandEmptyException;
import ecm2414.cardgame.exceptions.HandFullException;
import ecm2414.cardgame.exceptions.NotEnoughCardsException;
import ecm2414.cardgame.exceptions.NotEnoughPlayersException;

public class PlayerTest
{
	public static final long DEADLOCK_TEST_TIMEOUT = 5000;
	
	@Test
	public void testPlayer()
	{
		CardGame cardGame = new CardGame();
		int playerNum = 1;

		Player player = new Player(cardGame, playerNum);
		assertEquals(player.playerNumber, playerNum);
		assertEquals(player.cardGame, cardGame);
	}

	@Test
	public void testAddToAndGetHand()
	{
		CardGame cardGame = new CardGame();
		int playerNum = 1;
		Card card = new Card(playerNum);
		Card card2 = new Card(playerNum + 1);
		Card card3 = new Card(playerNum + 2);
		Card card4 = new Card(playerNum + 3);

		List<Card> hand = Arrays.asList(new Card[] { card, card2, card3, card4 });
		Player player = new Player(cardGame, playerNum);

		try
		{
			player.addToHand(card);
			player.addToHand(card2);
			player.addToHand(card3);
			player.addToHand(card4);
		} catch (HandFullException e)
		{
			fail();
		}

		assertEquals(player.getHand(), hand);
		assertEquals(player.getHand().size(), 4);
	}

	@Test
	public void testGetNumberOfCards()
	{
		CardGame cardGame = new CardGame();
		int playerNum = 1;
		Card card = new Card(playerNum);
		Card card2 = new Card(playerNum + 1);
		Card card3 = new Card(playerNum + 2);
		Card card4 = new Card(playerNum + 3);

		Player player = new Player(cardGame, playerNum);
		try
		{
			player.addToHand(card);
			player.addToHand(card2);
			player.addToHand(card3);
			player.addToHand(card4);
		} catch (HandFullException e)
		{
		}

		assertEquals(player.getNumberOfCards(), 4);
	}

	@Test
	public void testRemFromHand()
	{
		CardGame cardGame = new CardGame();
		int playerNum = 1;
		Card card = new Card(playerNum);
		Card card2 = new Card(playerNum + 1);
		Card card3 = new Card(playerNum + 2);
		Card card4 = new Card(playerNum + 3);

		Player player = new Player(cardGame, playerNum);
		try
		{
			player.addToHand(card);
			player.addToHand(card2);
			player.addToHand(card3);
			player.addToHand(card4);
		} catch (HandFullException e)
		{
		}

		try
		{
			player.remFromHand(card);
			player.remFromHand(card2);
			player.remFromHand(card3);
			player.remFromHand(card4);
		} catch (HandEmptyException e)
		{
			fail();
		}

		assertEquals(player.getHand().size(), 0);
	}

	@Test
	public void testIsPreferred() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		CardGame cardGame = new CardGame();
		int playerNum = 1;
		Player player = new Player(cardGame, playerNum);
		Card card = new Card(playerNum);

		Method method = Player.class.getDeclaredMethod("isPreferred", Card.class);
		method.setAccessible(true);
		Object obj = method.invoke(player, card);

		assertTrue(obj instanceof Boolean && (boolean) obj == true);
	}

	@Test
	public void testHasWon()
	{
		CardGame cardGame = new CardGame();
		int playerNum = 1;
		Card card = new Card(playerNum);

		Player player = new Player(cardGame, playerNum);
		try
		{
			player.addToHand(card);
			player.addToHand(card);
			player.addToHand(card);
			player.addToHand(card);
		} catch (HandFullException e)
		{
		}

		assertTrue(player.hasWon());
	}

	@Test
	public void testTakeTurnThrowsHandEmptyException()
	{
		CardGame cardGame = new CardGame();
		int playerNum = 1;
		Card card = new Card(playerNum);
		CardDeck leftDeck = new CardDeck(playerNum);
		CardDeck rightDeck = new CardDeck(playerNum + 1);
		leftDeck.addCard(card);

		String playerLogPath = "Tests/Output/player" + playerNum + "_HandEmpty_output.txt";
		String leftDeckLogPath = "Tests/Output/deck" + playerNum + "_HandEmpty_output.txt";

		Player player = new Player(cardGame, playerNum, playerLogPath, leftDeckLogPath);
		try
		{
			player.takeTurn(leftDeck, rightDeck);
			fail();
		} catch (HandEmptyException e)
		{
			// pass - HandEmptyException thrown
		} catch (HandFullException e)
		{
			fail();
		} catch (DeckEmptyException e)
		{
			fail();
		}
	}

	@Test
	public void testTakeTurnThrowsDeckEmptyException()
	{
		// TODO: Fix hand empty
		CardGame cardGame = new CardGame();
		int playerNum = 1;
		CardDeck leftDeck = new CardDeck(playerNum);
		CardDeck rightDeck = new CardDeck(playerNum + 1);

		String playerLogPath = "Tests/Output/player" + playerNum + "_DeckEmpty_output.txt";
		String leftDeckLogPath = "Tests/Output/deck" + playerNum + "_DeckEmpty_output.txt";

		Player player = new Player(cardGame, playerNum, playerLogPath, leftDeckLogPath);
		try
		{
			player.takeTurn(leftDeck, rightDeck);
			fail();
		} catch (HandEmptyException e)
		{
			fail();
		} catch (HandFullException e)
		{
			fail();
		} catch (DeckEmptyException e)
		{
			// pass - DeckEmptyException thrown
		}
	}

	@Test
	public void testTakeTurn()
	{
		CardGame cardGame = new CardGame();
		int playerNum = 1;
		Card card = new Card(playerNum);
		Card card2 = new Card(playerNum + 1);
		Card card3 = new Card(playerNum + 2);
		Card card4 = new Card(playerNum + 3);
		CardDeck leftDeck = new CardDeck(playerNum);
		CardDeck rightDeck = new CardDeck(playerNum + 1);
		leftDeck.addCard(card);

		String playerLogPath = "Tests/Output/player" + playerNum + "_TakeTurn_output.txt";
		String leftDeckLogPath = "Tests/Output/deck" + playerNum + "_TakeTurn_output.txt";

		Player player = new Player(cardGame, playerNum, playerLogPath, leftDeckLogPath);
		try
		{
			player.addToHand(card);
			player.addToHand(card2);
			player.addToHand(card3);
			player.addToHand(card4);
			player.takeTurn(leftDeck, rightDeck);
		} catch (HandEmptyException e)
		{
			fail();
		} catch (HandFullException e)
		{
			fail();
		} catch (DeckEmptyException e)
		{
			fail();
		}
	}

	@Test
	public void testDeadLock()
	{
		String testInput = "2\nTests/validTestPack.txt";
		ByteArrayInputStream testInputStream = new ByteArrayInputStream(testInput.getBytes());

		CardGame cardGame = new CardGame();
		
		try
		{
			cardGame.startGame(testInputStream);

			List<Thread> threads = new ArrayList<Thread>();
			
			try
			{
				Field threadsField = CardGame.class.getDeclaredField("threads");
				threadsField.setAccessible(true);
				Object fieldResult = threadsField.get(cardGame);
				if(fieldResult instanceof List<?>)
				{
					List<?> list = (List<?>) fieldResult;
					for(Object o : list)
					{
						if(o instanceof Thread)
							threads.add((Thread) o);
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
					
			if(threads.isEmpty())
				fail();
			
			long time0 = System.currentTimeMillis();
			
			// while waiting for a deadlock up to 5s.
			boolean allThreadsWaiting = false;
			while((System.currentTimeMillis() - time0) < DEADLOCK_TEST_TIMEOUT && !cardGame.playerHasWon.get())
			{
				allThreadsWaiting = false;
				for (Thread th : threads)
				{
					allThreadsWaiting = allThreadsWaiting && th.getState() == State.WAITING;
				}
				
				if(allThreadsWaiting)
				{
					fail();
				}
			}				
			assertTrue(!allThreadsWaiting);
			cardGame.shutdown();
			
		} catch (IOException e)
		{
			fail();
		} catch (NotEnoughPlayersException e)
		{
			fail();
		} catch (NotEnoughCardsException e)
		{
			fail();
		}
	}
}
