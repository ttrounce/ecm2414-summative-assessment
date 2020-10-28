package ecm2414.cardgame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Utilities used by the card game, such as a basic file output and conversion functions.
 */
public class CardGameUtil {
	
	/**
	 * Opens a file at a path and writes a line.
	 * This function creates the file and any directories should they not exist.
	 * @param path the path of the file to write to.
	 * @param message the line to write to the file.
	 */
	public static void appendToFile(String path, String message)
	{
		File file = new File(path);
		file.getParentFile().mkdirs();
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(message);
			writer.newLine();
			writer.close();
		}
		catch (IOException e)
		{
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}
	
	/**
	 * Clears a file at a path.
	 * This function creates the file and any directories should they not exist.
	 * @param path the path of the file to clear.
	 */
	public static void clearFile(String path)
	{
		File file = new File(path);
		file.getParentFile().mkdirs();
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.close();
		} catch (IOException e)
		{
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}
	
	/**
	 * Converts a collection of Objects to a string where elements are separated by a space (" ").
	 * @param collection the collection to turn into a string.
	 * @return the string conversion of the collection.
	 */
	public static String collectionToString(Collection<? extends Object> collection)
	{
		StringBuilder sb = new StringBuilder();
		for(Object obj : collection)
		{
			sb.append(obj);
			sb.append(" ");
		}
		if(sb.toString().isEmpty())
			return "";
		return sb.substring(0, sb.length() - 1).toString();
	}

	/**
	 * Notifies any thread waiting on a lock.
	 * @param lock the object lock which threads might be waiting on.
	 */
	public static void notifyLock(Object lock)
	{
		synchronized (lock)
		{
			lock.notify();
		}
	}

}
