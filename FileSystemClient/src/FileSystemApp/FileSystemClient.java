package FileSystemApp;

import org.omg.CosNaming.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.omg.CORBA.*;

/**
 * A simple client that just gets a
 * @author Merlin
 *
 */
public class FileSystemClient
{
	static FileSystem fileSystemImpl;

	private final static int RUN_NUMBER = 41;
	/**
	 * Just do each operation once
	 * @param args ignored
	 */
	public static void main(String args[])
	{
		try
		{
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "FileSystem";
			fileSystemImpl = FileSystemHelper.narrow(ncRef.resolve_str(name));

			System.out.println("Obtained a handle on server object: " + fileSystemImpl);
			String userData = fileSystemImpl.sayHello();

<<<<<<< HEAD
//			System.out.println(fileSystemImpl.hasFile("test4.txt"));
			fileSystemImpl.openWrite("test2.txt", userData);
			System.out.println(fileSystemImpl.listOpenFiles());
			System.out.println(fileSystemImpl.closeFile("test2.txt", userData));

=======
			System.out.println(fileSystemImpl.hasFile("test4.txt"));
			//System.out.println(fileSystemImpl.listFiles());
			//System.out.println(fileSystemImpl.openRead("test5.txt"));
>>>>>>> master

		} catch (Exception e)
		{
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}
	}

}
