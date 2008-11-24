package com.weltevree.ego.model;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

/**
 * Class for objects that appear in trees.
 * 
 * @author Weltevree jongw
 * @copyright Weltevree Beheer BV 2005
 * 
 */
public class TreeObject implements IAdaptable, IGoEventProvider {

	private String sortCode;

	private String name;

	private String imageKey = "Search.gif";

	private Object object;

	private TreeParent parent;

	private HashSet listeners = new HashSet();

	private boolean expandable = true;

	public TreeObject(String name) {
		this.name = name;
	}

	public TreeObject(Object object, String name, String sortCode,
			IGoEventListener listener) {
		this.sortCode = sortCode;
		this.object = object;
		this.name = name;
		addEventListener(listener);
	}

	/**
	 * Returns the name of this object.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the imaekey of this object.
	 * 
	 * @return
	 */
	public String getImageKey() {
		return imageKey;
	}

	/**
	 * Returns the object of this object.
	 * 
	 * @return
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * Sets the parent of this object.
	 * 
	 * @param parent
	 */
	public void setParent(TreeParent parent) {
		this.parent = parent;
		if (parent != null)
			parent.fireEvent(IGoEventProvider.EVENT_CHANGE);
	}

	/**
	 * Returns the parent of this object.
	 * 
	 * @return
	 */
	public TreeParent getParent() {
		return parent;
	}

	public String toString() {
		return getName();
	}

	public Object getAdapter(Class key) {
		if (key.isInstance(this))
			return this;
		return Platform.getAdapterManager().getAdapter(this, key);
	}

	/**
	 * @param imageKey
	 *            The imageKey to set.
	 */
	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
		fireEvent(EVENT_CHANGE);
	}

	/**
	 * @return Returns the sortCode.
	 */
	public String getSortCode() {
		return sortCode + toString();
	}

	/**
	 * @param sortCode
	 *            The sortCode to set.
	 */
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
		fireEvent(EVENT_CHANGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.weltevree.ego.model.IGoEventProvider#addEventListener(com.weltevree.ego.model.IGoEventListener)
	 */
	public void addEventListener(IGoEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Implementation of this method runs over the listeners and invokes the
	 * eventFired method in the listeners.
	 * 
	 * @see com.weltevree.ego.model.IGoEventProvider#fireEvent(int)
	 */
	public void fireEvent(int event) {

		Iterator it = listeners.iterator();
		while (it.hasNext())
			((IGoEventListener) it.next()).eventFired(event, new EventObject(
					this));
	}

	/**
	 * Checks if this treeobject is expandable or that is serves a special goal
	 * (like starting a filter).
	 * 
	 * @return
	 */
	public boolean isExpandable() {
		return expandable;
	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}

	public void setName(String name) {
		if (this.name.equals(name))
			return;
		this.name = name;
		fireEvent(EVENT_CHANGE);
	}

	public void setObject(Object object) {
		this.object = object;
		fireEvent(EVENT_CHANGE);
	}
}
