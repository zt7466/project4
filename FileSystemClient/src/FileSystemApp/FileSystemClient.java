package FileSystemApp;

import org.omg.CosNaming.*;

import java.util.Scanner;

import org.omg.CORBA.*;

/**
 * A simple client that just gets a
 * @author Merlin
 *
 */
public class FileSystemClient
{
	static FileSystem fileSystemImpl;

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

			Scanner sc = new Scanner(System.in);
			String inputst = "";
			while(!inputst.equals("-1"))
			{
				System.out.print("Input Command (keywords prints commands):");
				inputst = sc.next();

				if(inputst.toLowerCase().equals("open"))
				{
					System.out.print("write, read, or readLatest:");
					String inputst2 = sc.next();
					if(inputst2.toLowerCase().equals("read"))
					{
						System.out.print("Enter the name of the file you want to open to read:");
						String fileName = sc.next();
						System.out.println(fileSystemImpl.openRead(fileName, userData));
					}
					else if(inputst2.toLowerCase().equals("write"))
					{
						System.out.print("Enter the name of the file you want to open to write:");
						String fileName = sc.next();
						System.out.println(fileSystemImpl.openWrite(fileName, userData));
					}
					else if(inputst2.toLowerCase().equals("readlatest"))
					{
						System.out.print("Enter the name of the file you want to open to write:");
						String fileName = sc.next();
						System.out.println(fileSystemImpl.openReadLatest(fileName, userData));
					}
					else
					{
						System.out.println("Not valid input:");
					}
				}
				else if(inputst.toLowerCase().equals("read"))
				{
					System.out.print("Enter the name of the file you want to read from:");
					String fileName = sc.next();

					System.out.print("Enter the record number you want to read:");
					int recordNumber = sc.nextInt();
					System.out.println(fileSystemImpl.readRecord(fileName, recordNumber, userData));
				}
				else if(inputst.toLowerCase().equals("write"))
				{
					System.out.print("Enter the name of the file you want to write to:");
					String fileName = sc.next();

					System.out.print("Enter the record number you want to write to:");
					int recordNumber = sc.nextInt();

					sc.nextLine();
					System.out.print("Enter the record put in:");
					String record = sc.nextLine();

					System.out.println(fileSystemImpl.writeRecord(fileName, recordNumber, record, userData));
				}
				else if(inputst.toLowerCase().equals("close"))
				{
					System.out.print("Enter the name of the file you want to close:");
					String fileName = sc.next();

					System.out.println(fileSystemImpl.closeFile(fileName, userData));
				}
				else if(inputst.toLowerCase().equals("list"))
				{
					System.out.print("list all or open files:");
					String inputst2 = sc.next();
					System.out.println();
					if(inputst2.toLowerCase().equals("all"))
					{
						System.out.println(fileSystemImpl.listFiles());
					}
					else if(inputst2.toLowerCase().equals("open"))
					{
						System.out.println(fileSystemImpl.listOpenFiles());
					}
					else
					{
						System.out.println("Not valid input:");
					}
				}
				else if(inputst.toLowerCase().equals("keywords"))
				{
					System.out.println("useable keywords: open, read, write, close, list");
				}
				else
				{
					System.out.println("Not valid input:");
				}
//	//			System.out.println(fileSystemImpl.hasFile("test4.txt"));
//				fileSystemImpl.openRead("test3.txt", userData);
//				//System.out.println(fileSystemImpl.writeRecord("test2.txt", 3, "abcde12345", userData));
//				System.out.println(fileSystemImpl.readRecord("test3.txt", 3, userData));
//				System.out.println(fileSystemImpl.closeFile("test3.txt", userData));

			}
			sc.close();
		} catch (Exception e)
		{
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}

	}

}
