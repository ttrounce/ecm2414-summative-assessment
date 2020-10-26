package io.github.ttrounce.tests;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.github.ttrounce.CardGame;

public class CardInputTest
{
	@Test
	void validInputTest()
	{
		String testInput = "1\nTests/validTestPack.txt";
		
		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());
		try
		{
			CardGame cardGame = new CardGame(in);
		} catch (IOException e)
		{
			fail("IOException");
		}
	}
	
	@Test
	void invalidInputTypeTest()
	{
		String testInput = "1\nTests/invalidTestPack.txt";
		
		ByteArrayInputStream in = new ByteArrayInputStream(testInput.getBytes());
		try
		{
			CardGame cardGame = new CardGame(in);
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
			CardGame cardGame = new CardGame(in);
			fail("Didn't detect invalid pack size");
		} catch (IOException e)
		{
			// end of input is reached, hence program didn't take invalid input
			// test passed.
		}
	}
}
