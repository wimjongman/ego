/*
 * Created on 28-okt-04
 *
 * This class is used to read an object from the local disk. 
 */
package com.weltevree.ego.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * This class reads an <code>Object</code> from disk. The <code>Object</code> 
 * is then returned.  
 * 
 * @author Remain BV 
 */
public class ObjectFromDisk {

	/**
	 * This method takes a <code>FileInputStream</code> and returns 
	 * the object.
	 * 
	 * @param fis <code>FileInputStream</code> fis
	 * @return <code>Object</code>
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object getObject(FileInputStream fis)
		throws IOException, ClassNotFoundException {

		Object obj;

		ObjectInputStream ois = new ObjectInputStream(fis);
		obj = ois.readObject();
		ois.close();
		
		return obj;
	}

	/**
	 * This method takes a <code>String</code> as filename 
	 * and returns the object in this file. 
	 * 
	 * @param filename <code>String</code> filename
	 * @return <code>Object</code>
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object getObject(String filename)
		throws IOException, ClassNotFoundException {

		FileInputStream fis = null;

		fis = new FileInputStream(filename);
		
		return getObject(fis);
	}
}
