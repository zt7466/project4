package FileSystemApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

/**
 * FileSystemServer.java Server methods
 * 
 * @author Merlin, Scott, Ryan, Zach
 *
 */
class FileSystemImpl extends FileSystemPOA
{
	private ORB orb;
	// ArrayList of file names that are open
	private ArrayList<String[]> openedReadFiles = new ArrayList<String[]>();
	private ArrayList<String[]> openedWriteFiles = new ArrayList<String[]>();
	private ArrayList<String[]> openedDirtyFiles = new ArrayList<String[]>();

	/**
	 * Bonus Code
	 */
	private ArrayList<String[]> openedReadLatest = new ArrayList<String[]>();

	/**
	 * File paths save to be called
	 */
	private final String FILE_PATH = "directory";
	private final String LATEST_PATH = "latestRead/";
	private final String READ_DIRTY_PATH = "readDirty/";
	private final int RECORD_SIZE = 60;

	/**
	 * number of users that are logged on, used to assign unique user value
	 */
	private int userNum = 0;

	void setORB(ORB orb_val)
	{
		orb = orb_val;
	}

	/**
	 * Assigns a unique user key in a real life example this would have more
	 * security, but not need for the program
	 */
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
	 * Checks if a server has a certain file
	 * 
	 * @param title
	 *            of the file to find
	 * @return true or false weather the file exists on the server
	 */
	@Override
	public boolean hasFile(String title)
	{
		if (checkTitle(openedWriteFiles, title).size() != 0 || checkTitle(openedDirtyFiles, title).size() != 0)
		{
			return false;
		}
		File file = new File(FILE_PATH + '/' + title);
		return file.exists();
	}

	/**
	 * checks if the file should be deleted
	 * 
	 * @param title
	 *            of the file to find
	 * @return check if the server has a file open for write
	 */
	@Override
	public boolean checkWrite(String title)
	{
		boolean booleanValue = false;
		if (checkTitle(openedWriteFiles, title).size() == 0)
		{
			booleanValue = true;
		}
		return booleanValue;
	}

	/**
	 * opens a file to read
	 * 
	 * @param title
	 *            of file to open
	 * @param userNum
	 *            id of the user
	 * @return true or false if the file was opened
	 */
	@Override
	public boolean openRead(String title, String userNum)
	{
		boolean flag = false;
		if (hasFile(title))
		{
			String[] stringarray = { title, userNum };
			openedReadFiles.add(stringarray);
			flag = true;
		} else
		{
			Scanner sc = null;
			try
			{
				sc = new Scanner(new File("config.txt"));
			} catch (FileNotFoundException e1)
			{
			}

			while (sc.hasNext() && !flag)
			{
				String[] array = sc.nextLine().split(" ");
				FileSystem serverConnection;
				try
				{
					serverConnection = createConnection(array);

					if (serverConnection.hasFile(title))
					{
						FileWriter fileWriter = new FileWriter(new File(FILE_PATH + '/' + title));
						fileWriter.append(serverConnection.readFile(title));
						fileWriter.close();
						String[] stringarray = { title, userNum };
						openedReadFiles.add(stringarray);
						flag = true;
					}
				} catch (Exception e)
				{
					System.out.println("Could not find server: " + array[1]);
				}
			}
			sc.close();
		}
		return flag;
	}

	/**
	 * opens a file to write
	 * 
	 * @param title
	 *            of the file to open
	 * @param userNum
	 * @return t/f if the file was opened
	 */
	@Override
	public boolean openWrite(String title, String userNum)
	{
		boolean flag = false;
		if (hasFile(title))
		{
			Scanner sc = null;
			try
			{
				sc = new Scanner(new File("config.txt"));
			} catch (FileNotFoundException e)
			{
			}

			while (sc.hasNext())
			{
				String[] array = sc.nextLine().split(" ");
				FileSystem serverConnection = null;
				try
				{
					serverConnection = createConnection(array);

					if (!serverConnection.checkWrite(title))
					{
						return false; //Kill the method because its already open to write
					}
				} catch (Exception e)
				{
				}
			}
			sc.close();
			String[] stringarray = { title, userNum };
			openedWriteFiles.add(stringarray);
			flag = true;
			Scanner sc2 = null;
			try
			{
				sc2 = new Scanner(new File("config.txt"));
			} catch (FileNotFoundException e)
			{
			}
			while (sc2.hasNext())
			{
				String[] array = sc2.nextLine().split(" ");
				try
				{
					FileSystem serverConnection = createConnection(array);
					serverConnection.markDirty(title);
				} catch (Exception e)
				{
				}
			}
			sc2.close();
		} else
		{

			Scanner sc = null;
			try
			{
				sc = new Scanner(new File("config.txt"));
			} catch (FileNotFoundException e)
			{
			}
			while (sc.hasNext())
			{
				String[] array = sc.nextLine().split(" ");
				FileSystem serverConnection = null;
				try
				{
					serverConnection = createConnection(array);
					if (serverConnection.hasFile(title))
					{
						FileWriter fileWriter = new FileWriter(new File(FILE_PATH + '/' + title));
						fileWriter.append(serverConnection.readFile(title));
						fileWriter.close();
						openWrite(title, userNum);
						flag = true;
					}
				} catch (Exception e)
				{
				}
			}
			sc.close();
		}
		return flag;
	}

	/**
	 * Marks a file as dirty if it is opened to read when someone opens it to
	 * write
	 * 
	 * @param title
	 *            to make dirty
	 * @return if the file was marked dirty
	 */
	@Override
	public boolean markDirty(String title)
	{
		boolean flag = false;
		ArrayList<String> results = checkTitle(openedReadFiles, title);
		if (results.size() != 0)
		{
			try
			{
				File originalFile = new File(FILE_PATH + '/' + title);
				File newFile = new File(FILE_PATH + '/' + READ_DIRTY_PATH + title);

				InputStream inStream = new FileInputStream(originalFile);
				OutputStream outStream = new FileOutputStream(newFile);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = inStream.read(buffer)) > 0)
				{
					outStream.write(buffer, 0, length);
				}

				inStream.close();
				outStream.close();
				if (checkWrite(title))
				{
					originalFile.delete();
				}

			} catch (Exception e)
			{
				System.out.println("COPY FAILED");
			}

			for (int i = 0; i < results.size(); i++)
			{
				System.out.println("Marking " + title + " dirty");

				String[] stringarray = { title, results.get(i) };
				for (int j = 0; j < openedReadFiles.size(); j++)
				{
					if (openedReadFiles.get(j)[0].equals(title) && openedReadFiles.get(j)[1].equals(stringarray[1]))
					{
						openedReadFiles.remove(j);
					}
				}
				openedDirtyFiles.add(stringarray);
				flag = true;
			}
		} else if (hasFile(title))
		{
			File file = new File(FILE_PATH + '/' + title);
			file.delete();
		}
		return flag;
	}

	/**
	 * reads a given record from a given file
	 * 
	 * @param fileName
	 *            to pull the record from
	 * @param recordNumber
	 *            to pull from the file
	 * @param userNum
	 * @return the record pulled from the file
	 */
	@Override
	public String readRecord(String fileName, int recordNumber, String userNum)
	{
		String returnText = "File not open to read";
		String latestReadAddition = "";
		ArrayList<String> reader = checkTitle(openedReadFiles, fileName);
		ArrayList<String> dirtyReader = checkTitle(openedDirtyFiles, fileName);
		ArrayList<String> writer = checkTitle(openedWriteFiles, fileName);
		ArrayList<String> latestReader = checkTitle(openedReadLatest, fileName);

		if (dirtyReader.size() != 0)
		{
			for (int i = 0; i < dirtyReader.size(); i++)
			{
				if (dirtyReader.get(i).equals(userNum))
				{
					try
					{
						BufferedReader bufferedReader = new BufferedReader(
								new FileReader(new File(FILE_PATH + '/' + READ_DIRTY_PATH + fileName)));

						for (int j = 0; j < recordNumber; j++)
						{
							bufferedReader.readLine();
						}
						returnText = bufferedReader.readLine().substring(0, RECORD_SIZE);

						bufferedReader.close();
					} catch (Exception e)
					{
					}
				}
			}
		}

		if (latestReader.size() != 0)
		{
			if (latestReader.size() != 0)
			{
				latestReadAddition = LATEST_PATH;
				for (int i = 0; i < latestReader.size(); i++)
				{
					notifyReadLatest(fileName);
				}
			}

			for (int i = 0; i < latestReader.size(); i++)
			{
				if (latestReader.get(i).equals(userNum))
				{
					try
					{
						BufferedReader bufferedReader = new BufferedReader(
								new FileReader(new File(FILE_PATH + '/' + LATEST_PATH + fileName)));

						for (int j = 0; j < recordNumber; j++)
						{
							bufferedReader.readLine();
						}
						returnText = bufferedReader.readLine().substring(0, RECORD_SIZE);

						bufferedReader.close();
					} catch (Exception e)
					{
					}
				}
			}
		}

		if (reader.size() != 0 || writer.size() != 0)
		{
			reader.addAll(writer);
			for (int i = 0; i < reader.size(); i++)
			{
				if (reader.get(i).equals(userNum))
				{
					try
					{
						BufferedReader bufferedReader = new BufferedReader(
								new FileReader(new File(FILE_PATH + '/' + latestReadAddition + fileName)));

						for (int j = 0; j < recordNumber; j++)
						{
							bufferedReader.readLine();
						}
						returnText = bufferedReader.readLine().substring(0, RECORD_SIZE);

						bufferedReader.close();
					} catch (Exception e)
					{
					}
				}
			}
		}
		return returnText;
	}

	/**
	 * writes a given record to a line in the given file
	 * 
	 * @param fileName
	 *            the file to add the record to
	 * @param recordNumber
	 *            the place the record is to be placed
	 * @param record
	 *            the record to add
	 * @param userNum
	 *            the users unique key
	 * @return if the record was written to the server
	 */
	@Override
	public String writeRecord(String fileName, int recordNumber, String record, String userNum)
	{
		String outputText = "Record not written to file";
		ArrayList<String> a = checkTitle(openedWriteFiles, fileName);
		if (a.size() != 0)
		{
			for (int i = 0; i < a.size(); i++)
			{
				if (a.get(i).equals(userNum))
				{
					try
					{
						Scanner sc = new Scanner(new File(FILE_PATH + '/' + fileName));
						BufferedWriter bufferedWriter = new BufferedWriter(
								new FileWriter(new File(FILE_PATH + '/' + "newFile")));

						for (int j = 0; j < recordNumber; j++)
						{
							bufferedWriter.write(sc.nextLine() + '\n');
						}
						if (record.length() > RECORD_SIZE)
						{
							record = record.substring(0, RECORD_SIZE);
						} else
						{
							while (record.length() < RECORD_SIZE)
							{
								record = record + "~";
							}
						}

						bufferedWriter.write(record + '\n');
						sc.nextLine();
						while (sc.hasNext())
						{
							bufferedWriter.write(sc.nextLine() + '\n');
						}
						outputText = "Record Written to File";
						sc.close();
						bufferedWriter.close();

						File f1 = new File(FILE_PATH + '/' + "newFile");
						f1.renameTo(new File(FILE_PATH + '/' + fileName));
						f1.delete();
					} catch (Exception e)
					{

					}
				}
			}
		}
		return outputText;
	}

	/**
	 * close a file weather it is opened to read or write
	 * 
	 * @param fileName
	 *            file name to delete
	 * @param userNum
	 *            unique user value
	 * @return if the file was closed
	 */
	@Override
	public boolean closeFile(String fileName, String userNum)
	{
		boolean sendValue = false;
		boolean delete = true;
		for (int i = 0; i < openedReadFiles.size(); i++)
		{
			if (openedReadFiles.get(i)[0].equals(fileName) && openedReadFiles.get(i)[1].equals(userNum))
			{
				openedReadFiles.remove(i);
				sendValue = true;
			}
		}
		for (int i = 0; i < openedWriteFiles.size(); i++)
		{
			if (openedWriteFiles.get(i)[0].equals(fileName))
			{
				Scanner sc = null;
				try
				{
					sc = new Scanner(new File("config.txt"));
				} catch (FileNotFoundException e1)
				{
				}

				while (sc.hasNext())
				{
					String[] array = sc.nextLine().split(" ");
					FileSystem serverConnection;
					try
					{
						serverConnection = createConnection(array);

						if (serverConnection.checkReadLatest(fileName))
						{
							serverConnection.notifyReadLatest(fileName);
						}
					} catch (Exception e)
					{
						System.out.println("Could not find server: " + array[1]);
					}
				}
				sc.close();

				openedWriteFiles.remove(i);
				sendValue = true;
			}

		}

		for (int i = 0; i < openedDirtyFiles.size(); i++)
		{
			if (openedDirtyFiles.get(i)[0].equals(fileName) && openedDirtyFiles.get(i)[1].equals(userNum))
			{
				if (delete)
				{
					File file = new File(FILE_PATH + '/' + fileName);
					file.delete();
				}
				openedDirtyFiles.remove(i);
				sendValue = true;

				File file = new File(FILE_PATH + '/' + READ_DIRTY_PATH + fileName);
				file.delete();
			}
		}

		for (int i = 0; i < openedReadLatest.size(); i++)
		{
			if (openedReadLatest.get(i)[0].equals(fileName) && openedReadLatest.get(i)[1].equals(userNum))
			{
				openedReadLatest.remove(i);
				if ((checkTitle(openedReadLatest, fileName).size() == 0))
				{
					File file = new File(FILE_PATH + '/' + LATEST_PATH + fileName);
					file.delete();
				}
				sendValue = true;
			}
		}
		return sendValue;
	}

	/**
	 * returns a list of Files on the server
	 * 
	 * @return the list of files on the server
	 */
	@Override
	public String listFiles()
	{
		File directory = new File(FILE_PATH);
		StringBuffer contents = new StringBuffer("");

		for (File file : directory.listFiles())
		{
			if (!file.getName().equals("config.txt"))
			{
				contents.append(file.getName() + "\n");
			}
		}
		return contents.toString();
	}

	/**
	 * list of files currently opened to reading
	 * 
	 * @return the list of files
	 */
	@Override
	public String listOpenFiles()
	{
		StringBuffer contents = new StringBuffer("");
		for (String[] fileName : openedReadFiles)
		{
			contents.append(fileName[0] + " is opened to read by: " + fileName[1] + "\n");
		}
		for (String[] fileName : openedWriteFiles)
		{
			contents.append(fileName[0] + " is opened to write by: " + fileName[1] + "\n");
		}
		for (String[] fileName : openedDirtyFiles)
		{
			contents.append(fileName[0] + " is opened to dirty read by: " + fileName[1] + "\n");
		}
		for (String[] fileName : openedReadLatest)
		{
			contents.append(fileName[0] + " is opened to read latest by: " + fileName[1] + "\n");
		}
		return contents.toString();
	}

	/**
	 * open a file on the server and dump its contents to the client
	 * author: Merlin
	 * @param title
	 *            the title of the file
	 * @return the contents of the file
	 */
	@Override
	public String readFile(String title)
	{
		try
		{
			Scanner s = new Scanner(new File(FILE_PATH + '/' + title));
			StringBuffer contents = new StringBuffer("");
			while (s.hasNext())
			{
				contents.append(s.nextLine() + "\n");
			}

			s.close();
			return contents.toString();
		} catch (FileNotFoundException e)
		{
			System.out.println("Could not find file '" + title + "'");
		}
		return null;
	}

	/**
	 * creates a connection to a database
	 * 
	 * @param args
	 * @return
	 * @throws InvalidName
	 * @throws NotFound
	 * @throws CannotProceed
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName
	 */
	private FileSystem createConnection(String[] args)
			throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName
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
	 * 
	 * @param list
	 * @param as
	 * @return list of users using the file name
	 */
	private ArrayList<String> checkTitle(ArrayList<String[]> list, String as)
	{
		ArrayList<String> array = new ArrayList<String>();
		for (String[] stringArray : list)
		{
			if (stringArray[0].equals(as))
			{
				array.add(stringArray[1]);
			}
		}
		return array;
	}

	/**
	 * NOTE BONUS READ LATEST CODE
	 */

	/**
	 * returns if the file is open to readLatest
	 * 
	 * @param fileName
	 *            file name to check
	 * @return if the file is open of the server for read latest
	 */
	@Override
	public boolean checkReadLatest(String fileName)
	{
		boolean booleanValue = true;
		if (checkTitle(openedReadLatest, fileName).size() == 0)
		{
			booleanValue = false;
		}
		return booleanValue;
	}

	/**
	 * Opens the file to be allowed to read latest
	 * 
	 * @param fileName
	 *            filename to update
	 * @param userNum
	 *            user value to assign to
	 * @return if the file was opened
	 */
	@Override
	public boolean openReadLatest(String fileName, String userNum)
	{
		boolean flag = false;

		if (new File(FILE_PATH + '/' + fileName).exists())
		{
			String[] stringarray = { fileName, userNum };
			openedReadLatest.add(stringarray);
			BufferedWriter filewriter = null;
			Scanner file = null;
			try
			{
				filewriter = new BufferedWriter(new FileWriter(new File(FILE_PATH + '/' + LATEST_PATH + fileName)));
				file = new Scanner(new File(FILE_PATH + '/' + fileName));

				while (file.hasNext())
				{
					filewriter.write((file.nextLine() + "\n"));
				}
				file.close();
				filewriter.close();
			} catch (IOException e)
			{
			}

			flag = true;
		} else
		{
			Scanner sc = null;
			try
			{
				sc = new Scanner(new File("config.txt"));
			} catch (FileNotFoundException e1)
			{
			}

			while (sc.hasNext() && !flag)
			{
				String[] array = sc.nextLine().split(" ");
				FileSystem serverConnection;
				try
				{
					serverConnection = createConnection(array);

					if (serverConnection.hasFile(fileName) || !serverConnection.checkWrite(fileName))
					{
						FileWriter fileWriter = new FileWriter(new File(FILE_PATH + '/' + LATEST_PATH + fileName));
						fileWriter.append(serverConnection.readFile(fileName) + "\n");
						fileWriter.close();
						String[] stringarray = { fileName, userNum };
						openedReadLatest.add(stringarray);
						flag = true;
					}
				} catch (Exception e)
				{
					System.out.println("Could not find server: " + array[1]);
				}
			}
			sc.close();
		}
		return flag;
	}

	/**
	 * Notifies that something has changed in the inputed file
	 * 
	 * @param fileName
	 *            of the file that changed
	 * @return returns true if a file was read
	 */
	@Override
	public boolean notifyReadLatest(String fileName)
	{
		boolean readFile = false;
		if (checkTitle(openedReadLatest, fileName).size() != 0)
		{
			readFile = true;

			Scanner sc = null;
			try
			{
				sc = new Scanner(new File("config.txt"));
			} catch (FileNotFoundException e1)
			{
			}

			while (sc.hasNext())
			{
				String[] array = sc.nextLine().split(" ");
				FileSystem serverConnection;
				try
				{
					serverConnection = createConnection(array);

					boolean flag = serverConnection.checkWrite(fileName);
					if (!flag)
					{
						String string = serverConnection.readFile(fileName);

						if (!string.equals(""))
							;
						{
							FileWriter fileWriter = new FileWriter(new File(FILE_PATH + '/' + LATEST_PATH + fileName));
							fileWriter.write(string);
							fileWriter.close();
						}
					}
				} catch (Exception e)
				{
					System.out.println("Could not find server: " + array[1]);
				}
			}
			sc.close();
		}
		return readFile;
	}
	/**
	 * END BONUS CODE
	 */
}

/**
 * This is the class that runs on the server
 * 
 * @author merlin
 *
 */
public class FileSystemServer
{

	/**
	 * @param args
	 *            ignored
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