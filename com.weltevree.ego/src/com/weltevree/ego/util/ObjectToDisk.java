/*
 * Created on 28-okt-04
 *
 * This class is used to write an object to the local disk. 
 */
package com.weltevree.ego.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * The class takes an <code>Object</code> and a <code>FileOutputStream</code>
 * and writes the object to disk. 
 * <p>
 * code sample:
 * <p>
 * <pre> <code>	
 * 			// Write to disk
 *			AnySerializableObject p = new AnySerializableObject();
 *			try {
 *				fos = new FileOutputStream("c:\\dir\\filename");
 *				ObjectToDisk x = new ObjectToDisk((Object) p, fos);
 *	
 *			} catch (Exception e) {
 *				showMessage(e);
 *			} 
 * </code> </pre>
 * @author Wim Jongman Remain BV
 * @version $Revision: 1.1 $
 */
public class ObjectToDisk {
	
	/**
	 * Constructor for ObjectToDisk
	 * @param obj Object
	 * @param fos FileOutputStream
	 * @throws IOException
	 */
	public ObjectToDisk(Object obj, FileOutputStream fos) throws IOException {
		
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);
		oos.close();		
	}
}
