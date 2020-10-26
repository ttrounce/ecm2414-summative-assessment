package ecm2414.cardgame.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import ecm2414.cardgame.CardGame;

public class CardInputTest
{
	@Test
	void validInputTest()
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
		}
	}
	
	//TODO: FINISH TESTS.

	@Test
	void invalidInputTypeTest()
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
		}
	}

	@Test
	void invalidPackSizeTest()
	{
		String testInput = "1\nTests/invalidSizeTestPack.txt";

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
		}
	}
}
