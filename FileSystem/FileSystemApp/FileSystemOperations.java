package FileSystemApp;

/**
 * FileSystemApp/FileSystemOperations.java . Generated by the IDL-to-Java
 * compiler (portable), version "3.2" from FileSystem.idl Thursday, November 10,
 * 2016 5:57:37 PM EST
 */

public interface FileSystemOperations
{
	/**
	 * @return just get a string from the server to the client
	 */
	String sayHello();

	/**
	 * open a file on the server and dump its contents to the client
	 * @param title the title of the file
	 * @return the contents of the file
	 */
	String readFile(String title);

	/**
	 * make the server shut down
	 */
	void shutdown();

	/**
	 * Checks if a server has a certain file
	 * @param title of the file to find
	 * @return true or false weather the file exists on the server
	 */
	public boolean hasFile(String title);

	/**
	 * returns a list of Files on the server
	 * @return the list of files on the server
	 */
	public String listFiles();

	/**
	 * opens a file to read
	 * @param title of file to open
	 * @return true or false if the file was opened
	 */
	public boolean openRead(String title , String userNum);

	/**
	 *opens a file to write
	 * @param title of the file to open
	 * @return t/f if the file was opened
	 */
	public boolean openWrite(String title, String userNum);

	/**
	 * reads a given record from a given file
	 * @param fileName to pull the record from
	 * @param recordNumber to pull from the file
	 * @return the record pulled from the file
	 */
	public String readRecord(String fileName, int recordNumber, String userNum);

	/**
	 * writes a given record to a line in the given file
	 * @param fileName the file to add the record to
	 * @param recordNumber the place the record is to be placed
	 * @param record the record to add
	 */
	public String writeRecord(String fileName, int recordNumber, String record, String userNum);

	/**
	 * close a file weather it is opened to read or write
	 * @param fileName
	 */
	public boolean closeFile(String fileName, String userNum);

	/**
	 * list of files currently opened to reading
	 * @return the list of files
	 */
	public String listOpenFiles();

	/**
	 * Marks a file as dirty if it is opened to read when someone opens it to write
	 * @param title to make dirty
	 */
	boolean markDirty(String title);

	/**
	 * checks if the file should be deleted
	 * @param title
	 * @return
	 */
	boolean checkWrite(String title);

	/**
	 * Method for adding the file to the list of files opened to readLatest
	 * @param fileName
	 * @param userNum
	 * @return
	 */
	boolean openReadLatest(String fileName, String userNum);

	/**
	 * Method for reading the lastest version of a file
	 * @param fileName
	 * @return
	 */
	boolean notifyReadLatest(String fileName);

}
