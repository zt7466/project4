package FileSystemApp;

/**
 * FileSystemApp/_FileSystemStub.java . Generated by the IDL-to-Java compiler
 * (portable), version "3.2" from FileSystem.idl Thursday, November 10, 2016
 * 5:57:37 PM EST
 *
 * This helps the client side by packing up the parameters, invoking the call to
 * the server and unpacking the results
 */

public class _FileSystemStub extends org.omg.CORBA.portable.ObjectImpl implements FileSystemApp.FileSystem
{

	/**
	 * @see FileSystemApp.FileSystemOperations#sayHello()
	 */
	public String sayHello()
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("sayHello", true);
			$in = _invoke($out);
			String $result = $in.read_string();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return sayHello();
		} finally
		{
			_releaseReply($in);
		}
	}

	/**
	 * @see FileSystemApp.FileSystemOperations#readFile(java.lang.String)
	 */
	public String readFile(String title)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("readFile", true);
			$out.write_string(title);
			$in = _invoke($out);
			String $result = $in.read_string();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return readFile(title);
		} finally
		{
			_releaseReply($in);
		}
	}


	public boolean hasFile(String title)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("hasFile", true);
			$out.write_string(title);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return hasFile(title);
		} finally
		{
			_releaseReply($in);
		}
	}

	@Override
	public boolean checkWrite(String title)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("checkWrite", true);
			$out.write_string(title);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return checkWrite(title);
		} finally
		{
			_releaseReply($in);
		}
	}



	@Override
	public String listFiles()
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("listFiles", true);
			$in = _invoke($out);
			String $result = $in.read_string();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return listFiles();
		} finally
		{
			_releaseReply($in);
		}
	}

	@Override
	public boolean openRead(String title, String userNum)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("openRead", true);
			$out.write_string(title);
			$out.write_string(userNum);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return openRead(title, userNum);
		} finally
		{
			_releaseReply($in);
		}
	}

	@Override
	public boolean openWrite(String title, String userNum)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("openWrite", true);
			$out.write_string(title);
			$out.write_string(userNum);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return openWrite(title, userNum);
		} finally
		{
			_releaseReply($in);
		}
	}

	public boolean markDirty(String fileName)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("markDirty", true);
			$out.write_string(fileName);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return markDirty(fileName);
		} finally
		{
			_releaseReply($in);
		}
	}

	@Override
	public String listOpenFiles()
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("listOpenFiles", true);
			$in = _invoke($out);
			String $result = $in.read_string();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return listOpenFiles();
		} finally
		{
			_releaseReply($in);
		}
	}

	public boolean closeFile(String fileName, String userNum)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("closeFile", true);
			$out.write_string(fileName);
			$out.write_string(userNum);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			 return closeFile(fileName, userNum);
		} finally
		{
			_releaseReply($in);
		}
	}

	public String readRecord(String fileName, int recordNumber, String userNum)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("readRecord", true);
			$out.write_string(fileName);
			$out.write_long(recordNumber);
			$out.write_string(userNum);
			$in = _invoke($out);
			String $result = $in.read_string();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return readRecord(fileName, recordNumber, userNum);
		} finally
		{
			_releaseReply($in);
		}
	}

	public String writeRecord(String fileName, int recordNumber, String record, String userNum)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("writeRecord", true);
			$out.write_string(fileName);
			$out.write_long(recordNumber);
			$out.write_string(record);
			$out.write_string(userNum);
			$in = _invoke($out);
			String $result = $in.read_string();
			return $result;

		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return writeRecord(fileName, recordNumber, record, userNum);
		} finally
		{
			_releaseReply($in);
		}
	}


	/**
	 * @see FileSystemApp.FileSystemOperations#shutdown()
	 */
	public void shutdown()
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("shutdown", false);
			$in = _invoke($out);
			return;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			shutdown();
		} finally
		{
			_releaseReply($in);
		}
	}

	// Type-specific CORBA::Object operations
	private static String[] __ids =
	{ "IDL:FileSystemApp/FileSystem:1.0" };

	/**
	 * @see org.omg.CORBA.portable.ObjectImpl#_ids()
	 */
	public String[] _ids()
	{
		return (String[]) __ids.clone();
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException
	{
		String str = s.readUTF();
		String[] args = null;
		java.util.Properties props = null;
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		try
		{
			org.omg.CORBA.Object obj = orb.string_to_object(str);
			org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
			_set_delegate(delegate);
		} finally
		{
			orb.destroy();
		}
	}

	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException
	{
		String[] args = null;
		java.util.Properties props = null;
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		try
		{
			String str = orb.object_to_string(this);
			s.writeUTF(str);
		} finally
		{
			orb.destroy();
		}
	}

	@Override
	public boolean openReadLatest(String fileName, String userNum) {
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("openReadLatest", true);
			$out.write_string(fileName);
			$out.write_string(userNum);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			 return openReadLatest(fileName, userNum);
		} finally
		{
			_releaseReply($in);
		}
	}

	@Override
	public boolean notifyReadLatest(String fileName)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("notifyReadLatest", true);
			$out.write_string(fileName);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			 return notifyReadLatest(fileName);
		} finally
		{
			_releaseReply($in);
		}
	}

	/**
	 * Server for reading
	 */
	@Override
	public boolean checkReadLatest(String fileName)
	{
		org.omg.CORBA.portable.InputStream $in = null;
		try
		{
			org.omg.CORBA.portable.OutputStream $out = _request("checkReadLatest", true);
			$out.write_string(fileName);
			$in = _invoke($out);
			boolean $result = $in.read_boolean();
			return $result;
		} catch (org.omg.CORBA.portable.ApplicationException $ex)
		{
			$in = $ex.getInputStream();
			String _id = $ex.getId();
			throw new org.omg.CORBA.MARSHAL(_id);
		} catch (org.omg.CORBA.portable.RemarshalException $rm)
		{
			return checkWrite(fileName);
		} finally
		{
			_releaseReply($in);
		}
	}

}
