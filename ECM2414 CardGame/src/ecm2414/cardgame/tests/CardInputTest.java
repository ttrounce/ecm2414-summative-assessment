package ecm2414.cardgame.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import ecm2414.cardgame.Card;
import ecm2414.cardgame.CardGame;
import ecm2414.cardgame.exceptions.InvalidCardInputException;
import ecm2414.cardgame.exceptions.NotEnoughPlayersException;

public class CardInputTest
{
	/*
	 * TEST: CardGame.setupInput -> 2 players, valid pack.							 ==> Expected: No errors, cards should match test input.
	 * TEST: CardGame.setupInput -> 2 players, invalid pack (non-integer text card). ==> Expected: IOException due to running out of input.
	 * TEST: CardGame.setupInput -> 2 players, invalid pack (not enough cards). 	 ==> Expected: IOException due to running out of input.
	 * TEST: CardGame.setupInput -> 2 players, invalid pack (too many cards).    	 ==> Expected: IOException due to running out of input.
	 * TEST: CardGame.setupInput -> 1 player, valid pack.						     ==> Expected: IOException due to running out of input.
	 * 
	 * TEST: CardGame.loadPack   -> 2 players, valid pack.							 ==> Expected: No errors, cards should match test input.
	 * TEST: CardGame.loadPack   -> 2 players, invalid pack (non-integer text card). ==> Expected: InvalidCardInputException due to non-integer text card.
	 * TEST: CardGame.loadPack   -> 2 players, invalid pack (not enough cards). 	 ==> Expected: InvalidCardInputException due to not enough text cards.
	 * TEST: CardGame.loadPack   -> 2 players, invalid pack (too many cards). 		 ==> Expected: InvalidCardInputException due to too many text cards.
	 * TEST: CardGame.loadPack   -> 1 player, valid pack					 	 	 ==> Expected: NotEnoughPlayersException due to not enough players.
	 */
	
	@Test
	public void testSetupInputValid()
	{
		String testInput = "2\nTests/validTestPack.txt";
		int[] packValidTest = new int[] {5, 3, 2, 7, 1, 7, 4, 8, 65, 43, 3, 385, 34, 3, 1, 2};

		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());
		try
		{
			CardGame cardGame = new CardGame();
			cardGame.setupInput(in);
			
			assertEquals(cardGame.getPlayerCount(), 2);
			assertEquals(cardGame.getAllCards().size(), packValidTest.length);
			for(int i = 0; i < cardGame.getAllCards().size(); i++)
			{
				assertEquals(cardGame.getAllCards().get(i).denomination, packValidTest[i]);
			}
		} catch (IOException e)
		{
			fail("IOException");
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
	
	//TODO: FINISH TESTS.

	@Test
	public void testSetupInputInvalid()
	{
		String testInput = "2\nTests/invalidTestPack.txt";

		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());
		try
		{
			CardGame cardGame = new CardGame();
			cardGame.setupInput(in);
			fail("Didn't detect invalid input");
		} catch (IOException e)
		{
			// end of input is reached, hence program didn't take invalid input
			// test passed.
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}

	
	@Test
	public void testSetupInputInvalidSize()
	{
		String testInput = "2\nTests/invalidSizeTestPack.txt";
		
		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());
		try
		{
			CardGame cardGame = new CardGame();
			cardGame.setupInput(in);
			fail("Didn't detect invalid pack size");
		} catch (IOException e)
		{
			// end of input is reached, hence program didn't take invalid input
			// test passed.
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
	
	@Test
	public void testSetupInputInvalidSize2()
	{
		String testInput = "2\nTests/invalidSizeTestPack2.txt";
		
		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());
		try
		{
			CardGame cardGame = new CardGame();
			cardGame.setupInput(in);
			fail("Didn't detect invalid pack size");
		} catch (IOException e)
		{
			// end of input is reached, hence program didn't take invalid input
			// test passed.
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
	
	@Test
	public void testSetupInputValidOnePlayer()
	{
		String testInput = "1\nTests/validTestPackOnePlayer.txt";

		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());
		try
		{
			CardGame cardGame = new CardGame();
			cardGame.setupInput(in);
			
			fail();
		} catch (IOException e)
		{
			// pass
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
	
	@Test
	public void testLoadPackValid()
	{
		CardGame cardGame = new CardGame();
		int[] packValidTest = new int[] {5, 3, 2, 7, 1, 7, 4, 8, 65, 43, 3, 385, 34, 3, 1, 2};
		int testPlayers = 2;
		
		try
		{
			List<Card> cards = cardGame.loadPack(new File("Tests/validTestPack.txt"), testPlayers);
			assertEquals(cards.size(), packValidTest.length);
			for(int i = 0; i < cards.size(); i++)
			{
				assertEquals(cards.get(i).denomination, packValidTest[i]);
			}
		} catch (IOException e)
		{
			fail();
		} catch (InvalidCardInputException e)
		{
			fail("Cards should be valid - " + e.getMessage());
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
	
	@Test
	public void testLoadPackInvalid()
	{
		CardGame cardGame = new CardGame();
		int testPlayers = 2;
		try
		{
			cardGame.loadPack(new File("Tests/invalidTestPack.txt"), testPlayers);
			fail();
		} catch (IOException e)
		{
			fail();
		} catch (InvalidCardInputException e)
		{
			// pass
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
	
	@Test
	public void testLoadPackInvalidSize()
	{
		CardGame cardGame = new CardGame();
		int testPlayers = 2;
		try
		{
			cardGame.loadPack(new File("Tests/invalidSizeTestPack.txt"), testPlayers);
			fail();
		} catch (IOException e)
		{
			fail();
		} catch (InvalidCardInputException e)
		{
			// pass
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
	
	@Test
	public void testLoadPackInvalidSize2()
	{
		CardGame cardGame = new CardGame();
		int testPlayers = 2;
		try
		{
			cardGame.loadPack(new File("Tests/invalidSizeTestPack2.txt"), testPlayers);
			fail();
		} catch (IOException e)
		{
			fail();
		} catch (InvalidCardInputException e)
		{
			// pass
		} catch (NotEnoughPlayersException e)
		{
			fail();
		}
	}
	
	@Test
	public void testLoadPackValidOnePlayer()
	{
		CardGame cardGame = new CardGame();
		int testPlayers = 1;
		
		try
		{
			cardGame.loadPack(new File("Tests/validTestPackOnePlayer.txt"), testPlayers);
			fail();
		} catch (IOException e)
		{
			fail();
		} catch (InvalidCardInputException e)
		{
			fail("Cards should be valid - " + e.getMessage());
		} catch (NotEnoughPlayersException e)
		{
			// pass
		}
	}
}
