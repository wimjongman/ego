package com.weltevree.ego.model;

import java.util.EventObject;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/*
 * The content provider class is responsible for providing objects to the
 * view. It can wrap existing objects in adapters or simply return objects
 * as-is. These objects may be sensitive to the current input of the view,
 * or ignore it and always show the same content (like Task List, for
 * example).
 */

public class PlayerViewContentProvider implements IStructuredContentProvider,
		IGoEventListener {
	private Viewer viewer;

	private GoServer goServer;

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		if (viewer != null)
			viewer = v;

		if (newInput instanceof GoServer)
			goServer = (GoServer) newInput;

	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		if (goServer != null)
			return goServer.getUsers().values().toArray();
		return new String[] {};
	}

	public void eventFired(int event, EventObject object) {

	}
}