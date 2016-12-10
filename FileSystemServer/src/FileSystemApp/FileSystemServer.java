package FileSystemApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

class FileSystemImpl extends FileSystemPOA
{
	private ORB orb;
	private ArrayList<String[]> openedReadFiles = new ArrayList<String[]>();
	private ArrayList<String[]> openedWriteFiles = new ArrayList<String[]>();
	private ArrayList<String[]> openedDirtyFiles = new ArrayList<String[]>();

	private final String FILEPATH = "directory";
	private final int RECORDSIZE = 60;

	private int userNum = 0;

	public void setORB(ORB orb_val)
	{
		orb = orb_val;
	}

	// implement sayHello() method
	public String sayHello()
	{
		userNum++;
		return "User:" + userNum;
	}

	// implement shutdown() method
	public void shutdown()
	{
		orb.shutdown(false);
	}

	/**
	 * Returns if a given file exists on the server
	 */
	@Override
	public boolean hasFile(String title)
	{
		if(checkTitle(openedWriteFiles,title).size() != 0 || checkTitle(openedDirtyFiles,title).size()!= 0)
		{
			return false;
		}
		File file = new File(FILEPATH + '/' + title);
		return file.exists();
	}


	@Override
	public boolean openRead(String title , String userNum)
	{
		if(hasFile(title))
		{
			String[] stringarray = {title, userNum};
			openedReadFiles.add(stringarray);
			return true;
		}
		else
		{
			Scanner sc = null;
			try
			{
				sc = new Scanner(new File("config.txt"));
			}
			catch (FileNotFoundException e1)
			{
			}

			while (sc.hasNext())
			{
				String[] array = sc.nextLine().split(" ");
				FileSystem serverConnection;
				try {
					serverConnection = createConnection(array);

					if(serverConnection.hasFile(title))
					{
						FileWriter fileWriter = new FileWriter(new File(FILEPATH + '/' + title));
						fileWriter.append(serverConnection.readFile(title));
						fileWriter.close();
						String[] stringarray = {title, userNum};
						openedReadFiles.add(stringarray);
						sc.close();
						return true;
					}
				}
				catch (Exception e)
				{
					System.out.println("Could not find server: " + array[1]);
				}
			}
			sc.close();
		}
		return false;
	}

	@Override
	public boolean checkWrite(String title)
	{
		boolean booleanValue = false;
		if(checkTitle(openedWriteFiles, title).size() == 0)
		{
			booleanValue = true;
		}
		return booleanValue;
	}

	@Override
	public boolean openWrite(String title, String userNum)
	{
		if(hasFile(title))
		{
			Scanner sc = null;
			try
			{
				sc = new Scanner(new File("config.txt"));
			}
			catch (FileNotFoundException e)
			{
			}

			while (sc.hasNext())
			{
				String[] array = sc.nextLine().split(" ");
				FileSystem serverConnection = null;
				try {
					serverConnection = createConnection(array);

					if(!serverConnection.checkWrite(title))
					{
						return false;
					}
				}
				catch (Exception e)
				{
				}
			}
			sc.close();
			String[] stringarray = {title, userNum};
			openedWriteFiles.add(stringarray);
			Scanner sc2 = null;
			try
			{
				sc2 = new Scanner(new File("config.txt"));
			}
			catch (FileNotFoundException e)
			{
			}
			while (sc2.hasNext())
			{
				String[] array = sc2.nextLine().split(" ");
				try {
					FileSystem serverConnection = createConnection(array);
					serverConnection.markDirty(title);
				} catch (Exception e)
				{
				}
			}
			sc2.close();
		}
		else
		{

			Scanner sc = null;
			try
			{
				sc = new Scanner(new File("config.txt"));
			}
			catch (FileNotFoundException e)
			{
			}
			while (sc.hasNext())
			{
				String[] array = sc.nextLine().split(" ");
				FileSystem serverConnection = null;
				try
				{
					serverConnection = createConnection(array);
					if(serverConnection.hasFile(title))
					{
						FileWriter fileWriter = new FileWriter(new File(FILEPATH + '/' + title));
						fileWriter.append(serverConnection.readFile(title));
						fileWriter.close();
						openWrite(title, userNum);
						return true;
					}
				}
				catch (Exception e)
				{
				}
			}
			sc.close();
		}
		return true;
	}

	@Override
	public boolean markDirty(String title)
	{
		ArrayList<String> results = checkTitle(openedReadFiles,title);
		if(results.size() != 0)
		{
			for(int i =0; i < results.size(); i++)
			{
				System.out.println("Marking" + title + "dirty");

				String[] stringarray = {title, results.get(i)};
				for(int j = 0; j < openedReadFiles.size(); j++)
				{
					if(openedReadFiles.get(j)[0].equals(title) && openedReadFiles.get(j)[1].equals(stringarray[1]))
					{
					openedReadFiles.remove(j);
					}
				}
				openedDirtyFiles.add(stringarray);
			}
		}
		else if(checkTitle(openedWriteFiles, title).size() != 0)
		{

		}
		else if(hasFile(title))
		{
			File file = new File(FILEPATH + '/' + title);
			file.delete();
		}
		return true;
	}

	@Override
	public String readRecord(String fileName, int recordNumber, String userNum)
	{
		String returnText = "File not open to read";
		ArrayList<String> a = checkTitle(openedReadFiles, fileName);
		ArrayList<String> b = checkTitle(openedDirtyFiles, fileName);
		ArrayList<String> c = checkTitle(openedWriteFiles, fileName);
		if(a.size() != 0||b.size() != 0 || c.size() != 0)
		{
			a.addAll(b);
			a.addAll(c);
			for(int i = 0; i < a.size(); i++)
			{
				if(a.get(i).equals(userNum))
				{
					try
					{
						BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(FILEPATH + '/' + fileName)));

						for(int j = 0; j < recordNumber; j++)
						{
							bufferedReader.readLine();
						}
						returnText = bufferedReader.readLine().substring(0, RECORDSIZE);

						bufferedReader.close();
					}
					catch (Exception e)
					{
					}
				}
			}
		}
		return returnText;
	}

	@Override
	public String writeRecord(String fileName, int recordNumber, String record, String userNum)
	{
		String outputText = "Record not written to file";
		ArrayList<String> a = checkTitle(openedWriteFiles, fileName);
		if(a.size()!= 0)
		{
			for(int i = 0; i < a.size(); i++)
			{
				if(a.get(i).equals(userNum))
				{
					try
					{
						Scanner sc = new Scanner(new File(FILEPATH + '/' + fileName));
						BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(FILEPATH + '/' +"newFile")));

						for(int j = 0; j < recordNumber; j++)
						{
							bufferedWriter.write(sc.nextLine()+ '\n');
						}
						if(record.length() > RECORDSIZE)
						{
							record = record.substring(0, RECORDSIZE);
						}
						else
						{
							while(record.length() < RECORDSIZE)
							{
								record = record + "~";
							}
						}

						bufferedWriter.write(record+ '\n');
						sc.nextLine();
						while(sc.hasNext())
						{
							bufferedWriter.write(sc.nextLine()+ '\n');
						}
						outputText = "Record Written to File";
						sc.close();
						bufferedWriter.close();

						File f1 = new File(FILEPATH + '/' +"newFile");
						f1.renameTo(new File(FILEPATH + '/' + fileName));
						f1.delete();
					}
					catch (Exception e)
					{

					}
				}
			}
		}
		return outputText;
	}

	/**
	 * Closes the file if open
	 */
	@Override
	public boolean closeFile(String fileName, String userNum)
	{
		boolean sendValue = false;
		for(int i = 0; i < openedReadFiles.size(); i++)
		{
			if(openedReadFiles.get(i)[0].equals(fileName) && openedReadFiles.get(i)[1].equals(userNum))
			{
				openedReadFiles.remove(i);
				sendValue = true;
			}
		}
		for(int i = 0; i < openedWriteFiles.size(); i++)
		{
			if(openedWriteFiles.get(i)[0].equals(fileName) && openedWriteFiles.get(i)[1].equals(userNum))
			{
				openedWriteFiles.remove(i);
				sendValue = true;
			}
		}
		for(int i = 0; i < openedDirtyFiles.size(); i++)
		{
			if(openedDirtyFiles.get(i)[0].equals(fileName) && openedDirtyFiles.get(i)[1].equals(userNum))
			{
				openedDirtyFiles.remove(i);
				sendValue = true;
			}
		}
		return sendValue;
	}

	@Override
	public String listFiles()
	{
		File directory = new File(FILEPATH);
		StringBuffer contents = new StringBuffer("");

		for(File file : directory.listFiles())
		{
			if(!file.getName().equals("config.txt"))
			{
				contents.append(file.getName() + "\n");
			}
		}
		return contents.toString();
	}

	@Override
	public String listOpenFiles()
	{
		StringBuffer contents = new StringBuffer("");
		for(String[] fileName : openedReadFiles)
		{
			contents.append(fileName[0] + " is opened to read by: " + fileName[1] +"\n");
		}
		for(String[] fileName : openedWriteFiles)
		{
			contents.append(fileName[0] + " is opened to write by: " + fileName[1] +"\n");
		}
		for(String[] fileName : openedDirtyFiles)
		{
			contents.append(fileName[0] + " is opened to dirty read by: " + fileName[1] +"\n");
		}
		return contents.toString();
	}

	@Override
	public String readFile(String title)
	{
		try
		{
			Scanner s = new Scanner(new File(title));
			StringBuffer contents = new StringBuffer("");
			while (s.hasNext())
			{
				contents.append(s.nextLine() + "\n");
			}

			s.close();
			return contents.toString();
		} catch (FileNotFoundException e)
		{
			System.out.println("Could not find file '"+title + "'");
		}
		return null;
	}


	private FileSystem createConnection(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName
	{
	ORB orb = ORB.init(args, null);

	// get the root naming context
	org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	// Use NamingContextExt instead of NamingContext. This is
	// part of the Interoperable naming Service.
	NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

	// resolve the Object Reference in Naming
	String name = "FileSystem";
	return FileSystemHelper.narrow(ncRef.resolve_str(name));
	}

	/**
	 * Method for checking if a array list contains a filename
	 * @param list
	 * @param as
	 * @return list of users using the file name
	 */
	private ArrayList<String> checkTitle(ArrayList<String[]> list, String as)
	{
		ArrayList<String> array = new ArrayList<String>();
		for(String[] stringArray : list)
		{
			if(stringArray[0].equals(as))
			{
				array.add(stringArray[1]);
			}
		}
		return array;
	}
}

/**
 * This is the class that runs on the server
 * @author merlin
 *
 */
public class FileSystemServer
{

	/**
	 * @param args ignored
	 */
	public static void main(String args[])
	{
		try
		{

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			FileSystemImpl fileSystemImpl = new FileSystemImpl();
			fileSystemImpl.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(fileSystemImpl);
			FileSystem href = FileSystemHelper.narrow(ref);

			// get the root naming context
			// NameService invokes the name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			String name = "FileSystem";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);

			System.out.println("FileSystemServer ready and waiting ...");

			// wait for invocations from clients
			orb.run();
		}

		catch (Exception e)
		{
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

		System.out.println("FileSystemServer Exiting ...");

	}
}
