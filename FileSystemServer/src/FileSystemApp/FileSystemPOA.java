package FileSystemApp;

/**
 * FileSystemApp/FileSystemPOA.java . Generated by the IDL-to-Java compiler
 * (portable), version "3.2" from FileSystem.idl Thursday, November 10, 2016
 * 2:45:54 PM EST
 */

public abstract class FileSystemPOA extends org.omg.PortableServer.Servant
		implements FileSystemApp.FileSystemOperations, org.omg.CORBA.portable.InvokeHandler
{

	// Constructors

	private static java.util.Hashtable<String, Integer> _methods = new java.util.Hashtable<String, Integer>();
	static
	{
		_methods.put("sayHello", new java.lang.Integer(0));
		_methods.put("shutdown", new java.lang.Integer(1));
		_methods.put("readFile", new java.lang.Integer(2));
		_methods.put("hasFile", new java.lang.Integer(3));
		_methods.put("listFiles", new java.lang.Integer(4));
		_methods.put("openRead", new java.lang.Integer(5));
		_methods.put("readRecord", new java.lang.Integer(6));
		_methods.put("closeFile", new java.lang.Integer(7));
		_methods.put("listOpenFiles", new java.lang.Integer(8));
		_methods.put("openWrite", new java.lang.Integer(9));
		_methods.put("writeRecord", new java.lang.Integer(10));
		_methods.put("markDirty", new java.lang.Integer(11));
<<<<<<< HEAD
		_methods.put("checkWrite", new java.lang.Integer(12));
=======
>>>>>>> master
	}

	/**
	 * @see org.omg.CORBA.portable.InvokeHandler#_invoke(java.lang.String, org.omg.CORBA.portable.InputStream, org.omg.CORBA.portable.ResponseHandler)
	 */
	public org.omg.CORBA.portable.OutputStream _invoke(String $method, org.omg.CORBA.portable.InputStream in,
			org.omg.CORBA.portable.ResponseHandler $rh)
	{
		org.omg.CORBA.portable.OutputStream out = null;
		java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
		if (__method == null)
			throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

		switch (__method.intValue())
		{
		case 0: // FileSystemApp/FileSystem/sayHello
		{
			String $result = null;
			System.out.println("Saying Hello");
			$result = this.sayHello();
			out = $rh.createReply();
			out.write_string($result);
			break;
		}

		case 1: // FileSystemApp/FileSystem/shutdown
		{
			this.shutdown();
			out = $rh.createReply();
			break;
		}

		/**
		 * case for reading in a contents of a file
		 */
		case 2:
		{
			String fileTitle = in.read_string();
			System.out.println("Starting to read file! " + fileTitle);
			String $result = null;
			$result = this.readFile(fileTitle);
			out = $rh.createReply();
			out.write_string($result);
			break;
		}

		/**
<<<<<<< HEAD
		 * Case for if the server has a file
=======
		 * Case for if the file has a file
>>>>>>> master
		 */
		case 3:
		{
			String fileTitle = in.read_string();
			System.out.println("Looking for file: " + fileTitle);
			boolean $result = false;
			$result = this.hasFile(fileTitle);
			out = $rh.createReply();
			out.write_boolean($result);
			break;
		}

		/**
		 * Case for listing all the files
		 */
		case 4:
		{
			System.out.println("Listing the Files: ");
			String $result = null;
			$result = this.listFiles();
			out = $rh.createReply();
			out.write_string($result);
			break;
		}

		/**
		 * case for opening a file to read
		 */
		case 5:
		{
			String fileTitle = in.read_string();
<<<<<<< HEAD
			String userNum = in.read_string();
			System.out.println("Reading for file: " + fileTitle);
			boolean $result = false;
			$result = this.openRead(fileTitle,userNum);
=======
			System.out.println("Reading for file: " + fileTitle);
			boolean $result = false;
			$result = this.openRead(fileTitle);
>>>>>>> master
			out = $rh.createReply();
			out.write_boolean($result);
			break;
		}

		/**
		 * Case to handle reading a record
		 */
		case 6:
		{
			String fileTitle = in.read_string();
			int fileNumber = in.read_long();
<<<<<<< HEAD
			String userNum = in.read_string();
			System.out.println("reading record number: " + fileNumber +  " in file " +  fileTitle);
			String $result = null;
			$result = this.readRecord(fileTitle, fileNumber, userNum);
=======
			System.out.println("reading record number: " + fileNumber +  "in file" +  fileTitle);
			String $result = null;
			$result = this.readRecord(fileTitle, fileNumber);
>>>>>>> master
			out = $rh.createReply();
			out.write_string($result);
			break;
		}

		/**
		 * Case to handle closing a record
		 */
		case 7:
		{
			String fileTitle = in.read_string();
<<<<<<< HEAD
			String userNum = in.read_string();
			System.out.println("Closing File:" +  fileTitle);
			boolean $result = this.closeFile(fileTitle,userNum);
			out = $rh.createReply();
			out.write_boolean($result);
=======
			System.out.println("Closing File:" +  fileTitle);
			this.closeFile(fileTitle);
>>>>>>> master
			break;
		}

		/**
		 * Case for listing all the files
		 */
		case 8:
		{
			System.out.println("Listing the Open Files: ");
			String $result = null;
			$result = this.listOpenFiles();
			out = $rh.createReply();
			out.write_string($result);
			break;
		}

		/**
		 * case for opening a file to write
		 */
		case 9:
		{
			String fileTitle = in.read_string();
<<<<<<< HEAD
			String userNum = in.read_string();
			System.out.println("Opening file: " + fileTitle + " for write");
			boolean $result = false;
			$result = this.openWrite(fileTitle,userNum);
=======
			System.out.println("Writing for file: " + fileTitle);
			boolean $result = false;
			$result = this.openWrite(fileTitle);
>>>>>>> master
			out = $rh.createReply();
			out.write_boolean($result);
			break;
		}

		/**
<<<<<<< HEAD
		 * Case to handle writing a record
=======
		 * Case to handle reading a record
>>>>>>> master
		 */
		case 10:
		{
			String fileTitle = in.read_string();
			int fileNumber = in.read_long();
			String record = in.read_string();
<<<<<<< HEAD
			String userNum = in.read_string();
			System.out.println("reading record number: " + fileNumber +  "in file" +  fileTitle);
			String $result = null;
			$result = this.writeRecord(fileTitle, fileNumber, record, userNum);
			out = $rh.createReply();
			out.write_string($result);
=======
			System.out.println("reading record number: " + fileNumber +  "in file" +  fileTitle);
			this.writeRecord(fileTitle, fileNumber, record);
>>>>>>> master
			break;
		}

		/**
<<<<<<< HEAD
		 * Case to mark a file is dirty
=======
		 * Case to handle closing a record
>>>>>>> master
		 */
		case 11:
		{
			String fileTitle = in.read_string();
			System.out.println("Checking File is open:" + fileTitle);
<<<<<<< HEAD
			boolean $result = false;
			$result = this.markDirty(fileTitle);;
			out = $rh.createReply();
			out.write_boolean($result);
			break;
		}

		/**
		 * Checks a file if it has a file to delete
		 */
		case 12:
		{
			String fileTitle = in.read_string();
			System.out.println("Looking for file: " + fileTitle);
			boolean $result = false;
			$result = this.checkWrite(fileTitle);
			out = $rh.createReply();
			out.write_boolean($result);
			break;
		}
=======
			this.markDirty(fileTitle);
			break;
		}

>>>>>>> master
		default:
			throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
		}

		return out;
	} // _invoke

	// Type-specific CORBA::Object operations
	private static String[] __ids =
	{ "IDL:FileSystemApp/FileSystem:1.0" };

	/**
	 * @see org.omg.PortableServer.Servant#_all_interfaces(org.omg.PortableServer.POA, byte[])
	 */
	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId)
	{
		return (String[]) __ids.clone();
	}

	/**
	 * @return the FileSystem we are working with
	 */
	public FileSystem _this()
	{
		return FileSystemHelper.narrow(super._this_object());
	}

	/**
	 * @param orb our Corba object
	 * @return the FileSystem it contains
	 */
	public FileSystem _this(org.omg.CORBA.ORB orb)
	{
		return FileSystemHelper.narrow(super._this_object(orb));
	}

}
