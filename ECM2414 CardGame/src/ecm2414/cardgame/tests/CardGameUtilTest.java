package ecm2414.cardgame.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.State;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ecm2414.cardgame.Card;
import ecm2414.cardgame.CardGameUtil;

public class CardGameUtilTest
{
	public static final String APPEND_TEST_PATH = "Tests/append_test.txt";
	
	@Test
	public void testClearFile()
	{
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(APPEND_TEST_PATH)))
		{
			writer.write("Junk String");
			writer.newLine();
			writer.write("Testing Junk");
			writer.newLine();
			writer.write("Random Message");
			writer.newLine();
		} catch (IOException e)
		{
			fail();
		}

		CardGameUtil.clearFile(APPEND_TEST_PATH);
		
		File file = new File(APPEND_TEST_PATH);
		if(file.length() != 0)
		{
			fail();
		}
	}
	
	@Test
	public void testAppendToFileOnce()
	{
		// Clean file before test.
		CardGameUtil.clearFile(APPEND_TEST_PATH);
		
		String testMessage = "test";		
		CardGameUtil.appendToFile(APPEND_TEST_PATH, testMessage);
		
		try(BufferedReader reader = new BufferedReader(new FileReader(APPEND_TEST_PATH)))
		{
			String line = reader.readLine();
			assertTrue(line.equals(testMessage));
		} catch (FileNotFoundException e)
		{
			fail();
		} catch (IOException e)
		{
			fail();
		}
		
		// Clean again.
		CardGameUtil.clearFile(APPEND_TEST_PATH);
	}
	
	@Test
	public void testAppendToFileTwice()
	{
		// Clean file before test.
		CardGameUtil.clearFile(APPEND_TEST_PATH);
		
		String testMessage = "test";	
		String testMessage2 = "test2";
		CardGameUtil.appendToFile(APPEND_TEST_PATH, testMessage);
		CardGameUtil.appendToFile(APPEND_TEST_PATH, testMessage2);

		try(BufferedReader reader = new BufferedReader(new FileReader(APPEND_TEST_PATH)))
		{
			String line = reader.readLine();
			String line2 = reader.readLine();
			assertTrue(line.equals(testMessage));
			assertTrue(line2.equals(testMessage2));
		} catch (FileNotFoundException e)
		{
			fail();
		} catch (IOException e)
		{
			fail();
		}
		
		// Clean again.
		CardGameUtil.clearFile(APPEND_TEST_PATH);
	}
	
	@Test
	public void testCollectionToString()
	{
		List<Card> testCards = Arrays.asList(new Card[] {new Card(2), new Card(5), new Card(0), new Card(1), new Card(3)}); 
		String expectedString = "2 5 0 1 3";
		String resultString = CardGameUtil.collectionToString(testCards);
		
		assertTrue(expectedString.equals(resultString));
	}
	
	@Test
	public void testCollectionToStringEmpty()
	{
		List<Card> testCards = Arrays.asList(new Card[] {}); 
		String expectedString = "";
		String resultString = CardGameUtil.collectionToString(testCards);
		
		assertTrue(expectedString.equals(resultString));
	}
	
	@Test
	public void testNotifyLock()
	{
		Object lock = new Object();
				
		// The time that the test will wait for threads to reach WAITING/TERMINATED states.
		final long timeout = 3000;
		
		Thread thread = new Thread(() -> {
			synchronized(lock)
			{
				try
				{
					lock.wait();
				} catch (InterruptedException e)
				{
					fail();
				}				
			}
		});
		
		thread.start();
		
		long time0 = System.currentTimeMillis();
		while(thread.getState() != State.WAITING)
		{
			/*
			 * If the thread takes too long to become waiting, fail the test.
			 */
			if(System.currentTimeMillis() - time0 >= timeout)
			{
				fail();
				return;
			}
		}
		
		CardGameUtil.notifyLock(lock);
		
		time0 = System.currentTimeMillis();
		while(thread.getState() != State.TERMINATED)
		{
			/*
			 * If the thread takes too long to become waiting, fail the test.
			 */
			if(System.currentTimeMillis() - time0 >= timeout)
			{
				fail();
			}
		}
		// pass
	}
}
