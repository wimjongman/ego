package com.weltevree.ego.editors;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.weltevree.ego.model.Game;

public class EditorInput implements IEditorInput {

	private Object object;

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {

		String name = "";

		if (object instanceof Game) {
			Game gog = (Game) object;
			name = gog.getBlackPlayer().getName() + " vs. "
					+ gog.getWhitePlayer().getName();
		}

		return name;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {

		return getName();
	}

	public Object getAdapter(Class adapter) {

		return null;
	}
}
