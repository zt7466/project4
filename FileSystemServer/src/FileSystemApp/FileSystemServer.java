package FileSystemApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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

class FileSystemImpl extends FileSystemPOA
{
	private ORB orb;
	private ArrayList<String> openedReadFiles = new ArrayList<String>();
	private ArrayList<String> openedWriteFiles = new ArrayList<String>();
	private ArrayList<String> openedDirtyFiles = new ArrayList<String>();

	private final String FILEPATH = "directory";

	public void setORB(ORB orb_val)
	{
		orb = orb_val;
	}

	// implement sayHello() method
	public String sayHello()
	{
		return "\nHello world !!\n";
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
		if(openedWriteFiles.contains(title))
		{
			return false;
		}
		File file = new File(FILEPATH + '/' + title);
		return file.exists();
	}

	/**
	 * TODO: This may work
	 */
	@Override
	public boolean openRead(String title)
	{
		if(hasFile(title))
		{
			openedReadFiles.add(title);
			return true;
		}
		else
		{
			try
			{
				Scanner sc = new Scanner(new File("config.txt"));
				while (sc.hasNext())
				{
					String[] array = sc.nextLine().split(" ");
					FileSystem serverConnection = createConnection(array);
					if(serverConnection.hasFile(title))
					{
						FileWriter fileWriter = new FileWriter(new File(FILEPATH + '/' + title));
						fileWriter.append(serverConnection.readFile(title));
						fileWriter.close();
						openedReadFiles.add(title);
						sc.close();
						return true;
					}
				}
				sc.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * TODO FIX THIS
	 */
	@Override
	public boolean openWrite(String title)
	{
		boolean foundFile = false;
		if(hasFile(title))
		{
			try
			{
				Scanner sc = new Scanner(new File("config.txt"));
				while (sc.hasNext())
				{
					String[] array = sc.nextLine().split(" ");
					FileSystem serverConnection = createConnection(array);
					if(!serverConnection.openWrite(title))
					{
						return false;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return foundFile;
	}

	@Override
	public void markDirty(String title)
	{
		if(openedReadFiles.contains(title))
		{
			System.out.println("Marking" + title + "dirty");
			openedReadFiles.remove(title);
			openedDirtyFiles.add(title);
		}
		else if(hasFile(title))
		{
			File file = new File(FILEPATH + '/' + title);
			file.delete();
		}
	}

	/**
	 * TODO: Need to know about file sizes
	 */
	@Override
	public String readRecord(String fileName, int recordNumber)
	{
		return null;
	}

	/**
	 * TODO: Need to know about files sizes
	 */
	@Override
	public void writeRecord(String fileName, int recordNumber, String record)
	{

	}

	/**
	 * Closes the file if open
	 */
	@Override
	public void closeFile(String fileName)
	{
		if(openedReadFiles.contains(fileName))
		{
			openedReadFiles.remove(fileName);
		}
		if(openedWriteFiles.contains(fileName))
		{
			openedReadFiles.remove(fileName);
		}
		if(openedDirtyFiles.contains(fileName))
		{
			File file = new File(FILEPATH + '/' + fileName);
			file.delete();
			openedDirtyFiles.remove(fileName);
		}
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
		for(String fileName : openedReadFiles)
		{
			contents.append(fileName + "\n");
		}
		for(String fileName : openedWriteFiles)
		{
			contents.append(fileName + "\n");
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
