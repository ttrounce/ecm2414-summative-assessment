package ecm2414.cardgame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class CardGameUtil {
	
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
			System.err.println("Error writing to file");
		}
	}
	
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
			System.err.println("Error writing to file");
		}
	}
	
	public static String listToString(Collection<? extends Object> type)
	{
		StringBuilder sb = new StringBuilder();
		for(Object obj : type)
		{
			sb.append(obj);
			sb.append(" ");
		}
		return sb.substring(0, sb.length() - 1).toString();
	}
	
	public static void notifyLock(Object lock)
	{
		synchronized (lock)
		{
			lock.notify();
		}
	}

}
