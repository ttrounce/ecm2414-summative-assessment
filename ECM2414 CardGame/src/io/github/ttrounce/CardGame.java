package io.github.ttrounce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.github.ttrounce.exceptions.InvalidCardInputException;

public class CardGame
{

	public static void main(String[] args)
	{
		try
		{
			new CardGame(System.in);
		} catch (IOException e)
		{
			System.out.println("Error reading input.");
		}
	}

	private List<Card> cards;
	private int players;

	/*
	 * Starts by reading the user input and then runs the card game.
	 */
	public CardGame(InputStream inputStream) throws IOException
	{
		this.cards = new ArrayList<Card>();
		
		// Create a BufferedReader to read user input.
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		/*
		 * Asks user for the number of players.
		 * Repeatedly asks until given valid input.
		 */
		boolean validIntegerFound = false;
		while(!validIntegerFound)
		{
			System.out.println("Please enter the number of players: ");
			try
			{	
				this.players = Integer.parseInt(reader.readLine());	
				// exit loop
				validIntegerFound = true;
			} catch(NumberFormatException e)
			{
				System.out.println("Not a valid number.");
			}
		}
		
		/*
		 * Asks user for the path of the pack file.
		 * Repeatedly asks until valid pack file is given.
		 */
		boolean inputCardsFound = false;
		while(!inputCardsFound)
		{
			System.out.println("Please enter the location of the pack to load:");
			
			String packPath = reader.readLine();
			
			/*
			 * packPath is null if we run out of input.
			 * Used mostly for testing purposes.
			 */
			if(packPath == null)
			{
				throw new IOException("End of input reached");
			}
			
			// Create file and check if it exists, if not, ask user again.
			File file = new File(packPath);
			if(!file.exists())
			{
				System.out.println("File path cannot be found.");
				continue;
			}
			
			// Try to load in the pack, and throws exception for invalid cards or invalid pack sizes and asks the user to provide a valid pack again.
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
		
		cards.forEach(card -> {
			System.out.println("Card: " + card.denomination);
		});
		
		reader.close();
	}
	
	public void loadPack(File packFile) throws IOException, InvalidCardInputException
	{
		// BufferedReader to read pack file.
		BufferedReader reader = new BufferedReader(new FileReader(packFile));
		
		int validCards = 0;
		
		// Read every line from the pack file.
		String line = "";
		while((line = reader.readLine()) != null)
		{
			// Checks if the card is a valid integer, if not throws an Exception caught above in the call chain.
			try
			{
				int cardDenom = Integer.parseInt(line);
				
				// add card to list of cards.
				cards.add(new Card(cardDenom));
				
				// increment the number of valid cards found.
				validCards++;
			} catch(NumberFormatException e)
			{
				reader.close();
				throw new InvalidCardInputException("Cards must be non-negative integers");
			}
		}
		
		/*
		 * If the number of valid cards does not equal 8*#players, throws an exception caught above in the call chain.
		 */
		if(validCards != 8*players)
		{
			reader.close();
			throw new InvalidCardInputException("Card packs must be 8x the number of players");
		}
		
		System.out.println("Valid");
		
		reader.close();
	}
}