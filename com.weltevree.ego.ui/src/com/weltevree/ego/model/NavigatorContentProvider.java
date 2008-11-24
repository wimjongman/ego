package com.weltevree.ego.model;

import java.util.Enumeration;
import java.util.EventObject;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;

import com.weltevree.ego.preferences.PreferencePage;
import com.weltevree.ego.ui.Activator;
import com.weltevree.ego.wizards.NewServerWizard;

public class NavigatorContentProvider implements IStructuredContentProvider,
		ITreeContentProvider, IGoEventListener

{
	private TreeParent invisibleRoot;

	TreeParent serverNode, newServerNode, preferenceNode;

	private TreeViewer viewer;

	/**
	 * 
	 */
	public NavigatorContentProvider() {
		super();
	}

	public void dispose() {
	}

	public void eventFired(int event, EventObject object) {

		TreeParent gtp = (TreeParent) object.getSource();

		if (event == IGoEventProvider.EVENT_EXPAND) {

			if (gtp.getName().equals("Servers"))
				return;

			if (!(gtp.getChildren()[0].getName().startsWith("Dummy")))
				return;

			loadParent(gtp);

			if (gtp.getObject().toString().startsWith("Filter")) {
				viewer.setExpandedState(gtp, gtp.isExpandable());
			}
		}

		if (event == IGoEventProvider.EVENT_CHANGE) {
			System.out.println("Change on " + gtp.getName());
		}

		if (event == IGoEventProvider.EVENT_ADD) {
			System.out.println("Add on " + gtp.getName());
			if (gtp.getParent() != null)
				System.out.println("Parent " + gtp.getParent().getName());
		}

		viewer.refresh(gtp, true);

	}

	public Object[] getChildren(Object parent) {

		if (parent instanceof TreeParent) {
			return ((TreeParent) parent).getChildren();
		}
		return new Object[0];
	}

	public Object[] getElements(Object parent) {
		if (invisibleRoot == null) {
			initialize();
			return getChildren(invisibleRoot);
		}

		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof TreeObject) {
			return ((TreeObject) child).getParent();
		}
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof TreeParent)
			return ((TreeParent) parent).hasChildren();
		return false;
	}

	/*
	 * Setup the model
	 */
	private void initialize() {

		serverNode = new TreeParent("Servers", "Servers", "AA", this);
		serverNode.setImageKey("System.gif");

		newServerNode = new TreeParent("NewServerWizard", "New Server", "AA", this);
		newServerNode.setImageKey("ApplicationFilter.gif");
		newServerNode.setExpandable(false);
		serverNode.addChild(newServerNode);
		newServerNode.addChild(new TreeObject("Dummy"));
	
		loadServers(serverNode);

		preferenceNode = new TreeParent("Systems", "Preferences", "BB", this);
		preferenceNode.setImageKey("preference_page.gif");
		preferenceNode.addChild(new TreeObject("Dummy"));

		invisibleRoot = new TreeParent("", "InvisibleRoot", "AA", this);
		invisibleRoot.addChild(serverNode);
		invisibleRoot.addChild(preferenceNode);

	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {

		if (viewer != v)
			viewer = (TreeViewer) v;

		if (newInput instanceof TreeObject) {

			TreeObject gi = (TreeObject) newInput;
			gi.addEventListener(this);

		}
	}

	public void loadParent(Object parent) {

		if (!(parent instanceof TreeParent))
			return;

		final TreeParent tp = (TreeParent) parent;

		if (tp.hasChildren()) {
			if (tp.getChildren()[0].getName().startsWith("Dummy")) {
				tp.removeChildren();

//				if (tp.getObject() instanceof Wizard) {
//					return;
//				}

				/*
				 * Start the new go server dialog.
				 */
				if (tp.getObject().toString().equals("NewServerWizard")) {
					
					NewServerWizard wiz = new NewServerWizard();
					
					WizardDialog wd = new WizardDialog(null, wiz);
					wd.open();
					if (wiz.getGoServer() != null) {
						TreeParent tmp = new TreeParent(wiz.getGoServer(),
								wiz.getGoServer().getDescription(), "BB", this);
						tmp.setImageKey(wiz.getGoServer().getImageKey());
						tmp.addChild(new TreeObject("Dummy"));
						tp.getParent().addChild(tmp);

					}
					tp.addChild(new TreeObject("Dummy"));
				}

				/*
				 * Start the preferences dialog.
				 */
				if (tp.getName().startsWith("Preferences")) {
					BusyIndicator.showWhile(null, new Runnable() {
						public void run() {
							PreferencePage gp = new PreferencePage();
													}
					});

					return;
				}
			}
		}
	}

	/**
	 * @param serverNode
	 */
	private void loadServers(TreeParent p1) {

		GoServer gos = null;

		Object obj = null;
		Enumeration num = null;

		Activator.loadUserData();
		num = Activator.getUserData();

		while (num.hasMoreElements()) {
			obj = num.nextElement();

			if (obj instanceof GoServer) {
				gos = (GoServer) obj;

				TreeParent p2 = new TreeParent(gos, gos.getDescription(),
						"BB", this);

				p2.setImageKey(gos.getImageKey());

				p1.addChild(p2);
				p2.addChild(new TreeObject("Dummy"));

			}
		}
	}
}
