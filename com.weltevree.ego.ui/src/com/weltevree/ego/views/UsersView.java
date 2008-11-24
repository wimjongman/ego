package com.weltevree.ego.views;

import java.text.DecimalFormat;
import java.util.TreeMap;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;

import com.weltevree.ego.model.GoServer;
import com.weltevree.ego.model.TreeParent;
import com.weltevree.ego.model.Player;
import com.weltevree.ego.model.PlayerViewContentProvider;

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

public class UsersView extends ViewPart implements SelectionListener {

	private TableColumn userName;

	private TableColumn info;

	private TableColumn country;

	private TableColumn rank;

	private TableColumn gamesWon;

	private TableColumn gamesLost;

	private TableColumn observing;

	private TableColumn playing;

	private TableColumn idleTime;

	private TableColumn flags;

	private TableColumn language;

	private Table table;

	public static final String ID_VIEW = "com.weltevree.ego.views.UsersView";

	private ISelectionListener pageSelectionListener;

	private TableViewer viewer;

	private Action actionRefresh;

	private Action doubleClickAction;

	private GoServer goServer;

	private int sortIndex = 0;

	private boolean sortAscending = true;

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object obj, int index) {

			if (!(obj instanceof Player))
				return "Not a user";

			Player player = (Player) obj;

			return populateColumn(player, index);
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	class NameSorter extends ViewerSorter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerSorter#sort(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object[])
		 */
		public void sort(Viewer viewer, Object[] elements) {

			if (elements.length == 0)
				return;
			if (!(elements[0] instanceof Player))
				return;

			TreeMap map = new TreeMap();

			for (int ix = 0; ix < elements.length; ix++) {
				map.put(getColumnSortFromUser((Player) elements[ix], sortIndex)
						.toUpperCase()
						+ Integer.toString(ix), elements[ix]);
			}

			Object[] array = map.values().toArray();
			for (int ix = 0; ix < elements.length; ix++) {
				if (sortAscending)
					elements[ix] = array[ix];
				else
					elements[ix] = array[elements.length - ix - 1];
			}
		}
	}

	/**
	 * The constructor.
	 */
	public UsersView() {
	}

	private String populateColumn(Player player, int index) {
		switch (index) {
		case 0:
			return player.getName();
		case 1:
			return player.getInfo();
		case 2:
			return player.getCountry();
		case 3:
			return player.getRank();
		case 4:
			return Integer.toString(player.getGamesWon());
		case 5:
			return Integer.toString(player.getGamesLost());
		case 6:
			return Integer.toString(player.getObserving());
		case 7:
			return Integer.toString(player.getPlaying());
		case 8:
			return player.getIdleTime();
		case 9:
			return player.getFlags();
		case 10:
			return player.getLanguage();

		default:
			return "vul hier wat in";
		}
	}

	private String getColumnSortFromUser(Player player, int index) {

		DecimalFormat df = new DecimalFormat("00000.00");

		switch (index) {
		case 0:
			return player.getName();
		case 1:
			return player.getInfo();
		case 2:
			return player.getCountry();
		case 3:
			return df.format(Long.parseLong(Integer.toString(player
					.getRankToInt())));
		case 4:
			return df.format(Long.parseLong(Integer.toString(player
					.getGamesLost())));
		case 5:
			return df.format(Long.parseLong(Integer
					.toString(player.getGamesWon())));
		case 6:
			return df.format(Long.parseLong(Integer.toString(player
					.getObserving())));
		case 7:
			return df.format(Long
					.parseLong(Integer.toString(player.getPlaying())));
		case 8:
			return player.getIdleTime();
		case 9:
			return player.getFlags();
		case 10:
			return player.getLanguage();

		default:
			return "vul hier wat in";
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		userName = new TableColumn(table, SWT.NONE);
		userName.addSelectionListener(this);
		userName.setMoveable(true);
		userName.setWidth(68);
		userName.setText("Name");

		info = new TableColumn(table, SWT.NONE);
		info.addSelectionListener(this);
		info.setMoveable(true);
		info.setWidth(100);
		info.setText("Info");

		country = new TableColumn(table, SWT.NONE);
		country.addSelectionListener(this);
		country.setMoveable(true);
		country.setWidth(70);
		country.setText("Country");

		rank = new TableColumn(table, SWT.NONE);
		rank.addSelectionListener(this);
		rank.setMoveable(true);
		rank.setWidth(35);
		rank.setText("Rank");

		gamesWon = new TableColumn(table, SWT.NONE);
		gamesWon.addSelectionListener(this);
		gamesWon.setMoveable(true);
		gamesWon.setWidth(35);
		gamesWon.setText("Won");

		gamesLost = new TableColumn(table, SWT.NONE);
		gamesLost.addSelectionListener(this);
		gamesLost.setMoveable(true);
		gamesLost.setWidth(35);
		gamesLost.setText("Lost");

		observing = new TableColumn(table, SWT.NONE);
		observing.addSelectionListener(this);
		observing.setMoveable(true);
		observing.setWidth(35);
		observing.setText("Observing");

		playing = new TableColumn(table, SWT.NONE);
		playing.addSelectionListener(this);
		playing.setMoveable(true);
		playing.setWidth(35);
		playing.setText("Playing");

		idleTime = new TableColumn(table, SWT.NONE);
		idleTime.addSelectionListener(this);
		idleTime.setMoveable(true);
		idleTime.setWidth(40);
		idleTime.setText("Idle Time");

		flags = new TableColumn(table, SWT.NONE);
		flags.addSelectionListener(this);
		flags.setMoveable(true);
		flags.setWidth(35);
		flags.setText("Flags");

		language = new TableColumn(table, SWT.NONE);
		language.addSelectionListener(this);
		language.setMoveable(true);
		language.setWidth(100);
		language.setText("Language");

		viewer.setContentProvider(new PlayerViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		if (goServer == null)
			viewer.setInput(getViewSite());
		else
			viewer.setInput(goServer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		hookPageSelection();
		contributeToActionBars();
	}

	private void hookPageSelection() {

		pageSelectionListener = new ISelectionListener() {

			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				pageSelectionChanged(part, selection);

			}

		};

		getSite().getPage().addPostSelectionListener(pageSelectionListener);
	}

	private void pageSelectionChanged(IWorkbenchPart part, ISelection selection) {

		if (goServer == null || !goServer.isConnected())
			setContentDescription("Please select an active server");

		if (part == this)
			return;
		if (!(selection instanceof IStructuredSelection))
			return;

		IStructuredSelection sel = (IStructuredSelection) selection;
		if (sel.size() > 1)
			return;

		TreeParent gtp = (TreeParent) sel.getFirstElement();
		if (gtp == null)
			return;
		if (!(gtp.getObject() instanceof GoServer))
			return;

		GoServer gos = (GoServer) gtp.getObject();

		if (!gos.isConnected())
			return;

		goServer = gos;
		if (gos.getUsers().isEmpty())
			gos.loadUsers();
		if (viewer != null) {
			viewer.setInput(goServer);
			viewer.refresh();
		}
		setViewDescription();
	}

	private void setViewDescription(){
		setContentDescription("Users on: " + goServer.getDescription() + " ("
				+ goServer.getUsers().size() + ")");
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				UsersView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		if (pageSelectionListener != null)
			getSite().getPage().removePostSelectionListener(
					pageSelectionListener);
		super.dispose();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(actionRefresh);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(actionRefresh);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(actionRefresh);
	}

	private void makeActions() {
		actionRefresh = new Action() {
			public void run() {
				goServer.loadUsers();
				viewer.refresh();
				setViewDescription();
			}
		};
		actionRefresh.setText("Refresh");
		actionRefresh.setToolTipText("Refreshes the users list");
		actionRefresh.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_REDO));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"Users View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void widgetSelected(SelectionEvent e) {

		if (e.getSource() == userName)
			if (sortIndex == 0)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 0;
				sortAscending = true;
			}

		if (e.getSource() == info)
			if (sortIndex == 1)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 1;
				sortAscending = true;
			}

		if (e.getSource() == country)
			if (sortIndex == 2)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 2;
				sortAscending = true;
			}

		if (e.getSource() == rank)
			if (sortIndex == 3)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 3;
				sortAscending = true;
			}

		if (e.getSource() == gamesWon)
			if (sortIndex == 4)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 4;
				sortAscending = true;
			}

		if (e.getSource() == gamesLost)
			if (sortIndex == 5)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 5;
				sortAscending = true;
			}

		if (e.getSource() == observing)
			if (sortIndex == 6)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 6;
				sortAscending = true;
			}

		if (e.getSource() == playing)
			if (sortIndex == 7)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 7;
				sortAscending = true;
			}

		if (e.getSource() == idleTime)
			if (sortIndex == 8)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 8;
				sortAscending = true;
			}

		if (e.getSource() == flags)
			if (sortIndex == 9)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 9;
				sortAscending = true;
			}

		if (e.getSource() == language)
			if (sortIndex == 10)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 10;
				sortAscending = true;
			}

		viewer.refresh();

	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite)
	 */
	public void init(IViewSite site) throws PartInitException {
		super.init(site);

		ISelection sel = site.getPage().getSelection(Navigator.ID_VIEW);
		if (sel != null) {
			pageSelectionChanged(null, sel);
		}

	}

}