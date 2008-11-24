package com.weltevree.ego.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.weltevree.ego.util.ObjectFromDisk;

/**
 * The main plugin class to be used in the desktop.
 */
public class Activator extends AbstractUIPlugin {
	// The shared instance.
	private static Activator plugin;

	// Resource bundle.
	/**
	 * @uml.property name="resourceBundle"
	 */
	private ResourceBundle resourceBundle;

	// Images and user data
	private static ImageRegistry images;

	private static Hashtable userData;

	public static final String FIRST = "*FIRST";

	public static final String NEXT = "*NEXT";
	
	public final static String ID = "com.weltevree.ego.ui";

	/**
	 * The constructor.
	 */
	public Activator() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle
					.getBundle("com.weltevree.ego.TDPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		initPreferenceStore();
	}

	private void initPreferenceStore() {

		IPreferenceStore ps = getPreferenceStore();

		ps.setDefault("boardImage",
				"C:\\Program Files\\Moray For Windows\\POVScn\\board.gif");
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		images = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {

		ResourceBundle bundle = Activator.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 * 
	 * @uml.property name="resourceBundle"
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * Gets an image
	 */
	public static Image getImage(String imageKey) {
		if (images == null)
			return null;
		return (Image) images.getDescriptor(imageKey.toUpperCase())
				.createImage();
	}

	/**
	 * Gets an imagedescriptot
	 */
	public static ImageDescriptor getImageDescriptor(String imageKey) {
		if (images == null)
			return null;
		return images.getDescriptor(imageKey.toUpperCase());
	}

	/**
	 * Gets user data in an Enumeration
	 */
	public static Enumeration getUserData() {
		return userData.elements();
	}

	/**
	 * Loads the images used to display in the Tree.
	 */
	public static void loadImages() {
		if (!(images == null))
			return;

		// Load the images from disk
		URL home = null;
		try {
			home = FileLocator.resolve(Activator.getDefault().getBundle()
					.getEntry("/"));
		} catch (IOException e) {
			return;
		}
		images = new ImageRegistry(plugin.getWorkbench().getDisplay());
		String dir = home.getPath() + "/icons/";
		File f = new File(dir);
		String[] files = f.list();
		for (int ix = 0; ix < files.length; ix++) {
			ImageDescriptor im = AbstractUIPlugin.imageDescriptorFromPlugin(
					ID, "/icons/" + files[ix]);
			images.put(files[ix].toUpperCase(), im);
		}
	}

	/**
	 * Loads the user data from Disk
	 */
	public static void loadUserData() {
		try {
			if (userData == null)
				userData = new Hashtable();
			Hashtable temp = new Hashtable();

			URL home = FileLocator.resolve(Activator.getDefault().getBundle()
					.getEntry("/"));

			String dir = home.getFile() + "local/"
					+ System.getProperty("user.name") + "/";

			File f = new File(dir);
			if (!f.exists())
				f.mkdirs();

			String[] files = f.list();
			if (files == null)
				return;

			ArrayList al = new ArrayList();
			for (int ix = 0; ix < files.length; ix++)
				al.add(files[ix]);
			Collections.sort(al);

			// Add all objects to the userData if not already loaded.
			for (int ix = 0; ix < files.length; ix++) {
				if (((String) al.get(ix)).endsWith(".dat")) {
					Object obj = ObjectFromDisk.getObject(dir
							+ (String) al.get(ix));

					if (userData.containsKey(obj.toString()))
						temp.put(userData.get(obj.toString()).toString(),
								userData.get(obj.toString()));
					else
						temp.put(obj.toString(), obj);
				}
			}

			// HSuTCWTxt
			userData = temp;
		} catch (Exception e) {
			System.out.println(e.getMessage() + ". in GoPlugin.loadUserData()");
		}
	}
}
