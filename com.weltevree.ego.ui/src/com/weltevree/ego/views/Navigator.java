package com.weltevree.ego.views;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.weltevree.ego.model.NavigatorContentProvider;
import com.weltevree.ego.model.GoServer;
import com.weltevree.ego.model.TreeObject;
import com.weltevree.ego.model.TreeParent;
import com.weltevree.ego.ui.Activator;
import com.weltevree.ego.wizards.DeleteServerWizard;
import com.weltevree.ego.wizards.NewServerWizard;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class Navigator extends ViewPart {
	public static final String ID_VIEW = "com.weltevree.ego.views.Navigator";

	private TreeViewer viewer;

	private DrillDownAdapter drillDownAdapter;

	private Action actionDelete;

	private Action actionRefresh;

	private Action actionChange;

	private Action actionTreeExpand;

	private Action actionConnect;

	private Action actionDisconnect;

	private Action actionGetGames;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			TreeObject to = (TreeObject) obj;
			return (Image) Activator.getImage(to.getImageKey().toUpperCase());
		}
	}

	class NameSorter extends ViewerSorter {
		public int compare(Viewer viewer, Object e1, Object e2) {

			/*
			 * If the two components are both treeobjects then compare their
			 * sortCode rather then toString(); This gives us some control over
			 * the placement of the treeitems,
			 */
			if ((e1 instanceof TreeObject) && (e2 instanceof TreeObject))
				return super.compare(viewer, ((TreeObject) e1).getSortCode(),
						((TreeObject) e2).getSortCode());

			return super.compare(viewer, e1, e2);
		}
	}

	/**
	 * The constructor.
	 */
	public Navigator() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		Activator.loadImages();
		Activator.loadUserData();

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new NavigatorContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		hookExpandAction();
		contributeToActionBars();
		getSite().setSelectionProvider(viewer);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				Navigator.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(actionDelete);
		manager.add(new Separator());
		manager.add(actionRefresh);
	}

	private void fillContextMenu(IMenuManager manager) {

		/*
		 * Determine the actions available in the context menu. We can only
		 * delete GoServer definitions. Connect and disconnect can only be done
		 * if the current state indicates this.
		 */
		boolean canConnect = true, canDisconnect = true, isGoServer = true;

		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		Iterator it = sel.iterator();
		while (it.hasNext()) {
			TreeObject gt = (TreeObject) it.next();
			if (!(gt.getObject() instanceof GoServer)) // not
			{
				isGoServer = canConnect = canDisconnect = false;
				break;
			}

			GoServer gs = (GoServer) gt.getObject();

			if (gs.isConnected())
				canConnect = false;
			else
				canDisconnect = false;

		}

		/*
		 * Determine the actions available in the context menu. We can only
		 * delete GoServer definitions. Connect and disconnect can only be done
		 * if the current state indicates this.
		 */
		if (isGoServer)
			manager.add(actionDelete);
		if (canConnect) {
			manager.add(actionConnect);
			manager.add(actionChange);
		}
		if (canDisconnect) {
			manager.add(actionDisconnect);
			manager.add(actionGetGames);
		}

		if (isGoServer && canDisconnect) {
			manager.add(new Separator());
			drillDownAdapter.addNavigationActions(manager);
		}

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		actionDelete = new Action() {
			public void run() {

				StructuredSelection sel = (StructuredSelection) viewer
						.getSelection();
				TreeParent tp = (TreeParent) sel.getFirstElement();

				if (tp.getObject() instanceof GoServer) {
					DeleteServerWizard wiz = new DeleteServerWizard(
							(GoServer) tp.getObject());
					WizardDialog wd = new WizardDialog(null, wiz);
					wd.open();
					if (wd.getReturnCode() != WizardDialog.CANCEL) {
						tp.getParent().removeChild(tp);
					}
				}
			}
		};

		actionDelete.setAccelerator(SWT.DEL);
		actionDelete.setText("Delete");
		actionDelete.setToolTipText("Delete this definition");
		actionDelete.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_DELETE));

		actionConnect = new Action() {
			public void run() {

				try {
					IRunnableWithProgress op = new IRunnableWithProgress() {

						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {

							StructuredSelection sel = (StructuredSelection) viewer
									.getSelection();
							Iterator iterator = sel.iterator();
							monitor.beginTask("Connecting to " + sel.size()
									+ " servers", sel.size() * 3);
							int ix = 1;
							while (iterator.hasNext()) {
								TreeParent tp = (TreeParent) iterator
										.next();

								if (tp.getObject() instanceof GoServer) {
									GoServer gos = (GoServer) tp.getObject();
									monitor.subTask("Connecting to "
											+ gos.getDescription() + ". Player: "
											+ gos.getLogin());
									try {
										gos.connect();
										monitor.worked(ix++);
										monitor.subTask("Loading games ... ");
										gos.loadGames();
										monitor.worked(ix++);
										monitor.subTask("Loading users ... ");
										gos.loadGames();
										monitor.worked(ix++);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									tp.setImageKey(gos.getImageKey());
									viewer.setSelection(viewer.getSelection());

								}
							}
							monitor.done();
						}
					};

					new ProgressMonitorDialog(viewer.getControl().getShell())
							.run(false, true, op);
				} catch (InvocationTargetException e) {
					// handle exception
				} catch (InterruptedException e) {
					// handle cancelation
				}

			}
		};

		actionConnect.setAccelerator(SWT.ALT | 'C');
		actionConnect.setText("Connect");
		actionConnect.setToolTipText("Connect to this server");
		actionConnect.setImageDescriptor(Activator
				.getImageDescriptor("connect_co.gif"));

		actionDisconnect = new Action() {
			public void run() {

				try {
					IRunnableWithProgress op = new IRunnableWithProgress() {

						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {

							// IWorkbenchActivitySupport rs =
							// GoPlugin.getDefault().getWorkbench().getActivitySupport();

							StructuredSelection sel = (StructuredSelection) viewer
									.getSelection();
							Iterator iterator = sel.iterator();
							monitor.beginTask("Disconnecting " + sel.size()
									+ " servers", sel.size());
							int ix = 1;
							while (iterator.hasNext()) {
								TreeParent tp = (TreeParent) iterator
										.next();

								if (tp.getObject() instanceof GoServer) {
									GoServer gos = (GoServer) tp.getObject();
									monitor.subTask("Disconnecting from "
											+ gos.getDescription() + ". Player: "
											+ gos.getLogin());

									gos.disconnect();
									monitor.worked(ix++);

									tp.setImageKey(gos.getImageKey());
								}
							}
							monitor.done();
						}
					};

					new ProgressMonitorDialog(viewer.getControl().getShell())
							.run(false, true, op);
				} catch (InvocationTargetException e) {
					// handle exception
				} catch (InterruptedException e) {
					// handle cancelation
				}

			}
		};

		actionDisconnect.setAccelerator(SWT.ALT | 'D');
		actionDisconnect.setText("Disconnect");
		actionDisconnect.setToolTipText("Close connection to this server");
		actionDisconnect.setImageDescriptor(Activator
				.getImageDescriptor("disconnect_co.gif"));

		actionGetGames = new Action() {
			public void run() {

				try {
					IRunnableWithProgress op = new IRunnableWithProgress() {

						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {

							StructuredSelection sel = (StructuredSelection) viewer
									.getSelection();
							
							Iterator iterator = sel.iterator();
							monitor.beginTask("Getting games from "
									+ sel.size() + " servers", sel.size());
							int ix = 1;
							while (iterator.hasNext()) {
								TreeParent tp = (TreeParent) iterator
										.next();

								if (tp.getObject() instanceof GoServer) {
									GoServer gos = (GoServer) tp.getObject();
									monitor.subTask("Getting games from");

									gos.loadGames();
									monitor.worked(ix++);

								}
							}
							monitor.done();
						}
					};

					new ProgressMonitorDialog(viewer.getControl().getShell())
							.run(false, true, op);
				} catch (InvocationTargetException e) {
					// handle exception
				} catch (InterruptedException e) {
					// handle cancelation
				}

			}
		};

		actionGetGames.setText("Games List");
		actionGetGames.setToolTipText("Close connection to this server");
		actionGetGames.setImageDescriptor(Activator
				.getImageDescriptor("disconnect_co.gif"));

		actionRefresh = new Action() {
			public void run() {
				viewer.refresh();
			}
		};
		actionRefresh.setText("Refresh");
		actionRefresh.setToolTipText("Refresh");
		actionRefresh.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_REDO));

		actionChange = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();

				NewServerWizard wiz = new NewServerWizard();
				wiz.init(Activator.getDefault().getWorkbench(),
						(IStructuredSelection) selection);
				WizardDialog wd = new WizardDialog(viewer.getControl()
						.getShell(), wiz);

				if (wd.open() == Window.OK) {
					((TreeObject) obj).setName(wiz.getGoServer()
							.getDescription());
					((TreeParent) obj).setObject(wiz.getGoServer());
				}

			}
		};

		actionChange.setText("Change");
		actionChange.setToolTipText("Change");
		actionChange.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_NEW_WIZARD));

		actionTreeExpand = new Action() {
			public void run() {
				TreeParent tp = (TreeParent) viewer.getData("Event");
				tp.fireEvent(TreeParent.EVENT_EXPAND);
			}
		};
	}

	private void hookExpandAction() {
		viewer.addTreeListener(new ITreeViewerListener() {

			public void treeCollapsed(TreeExpansionEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void treeExpanded(TreeExpansionEvent arg0) {
				viewer.setData("Event", arg0.getElement());
				actionTreeExpand.run();
			}
		});
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if (((GoServer) ((TreeParent) ((IStructuredSelection) event
						.getSelection()).getFirstElement()).getObject())
						.isConnected())
					actionDisconnect.run();
				else
					actionConnect.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Main",
				message);

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}