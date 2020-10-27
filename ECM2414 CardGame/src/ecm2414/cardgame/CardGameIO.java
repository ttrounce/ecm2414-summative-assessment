package ecm2414.cardgame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CardGameIO {
	public static void appendToFile(String file, String message)
	{
		File f = new File(file);
		f.getParentFile().mkdirs();
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
			writer.write(message);
			writer.newLine();
			writer.close();
		}
		catch (IOException e)
		{
			System.err.println("Error writing to file");
		}
	}
	
	public static void clearFile(String file)
	{
		File f = new File(file);
		f.getParentFile().mkdirs();
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.close();
		} catch (IOException e)
		{
			System.err.println("Error writing to file");
		}
	}
}
