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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.ViewPart;

import com.weltevree.ego.editors.BoardEditor;
import com.weltevree.ego.editors.EditorInput;
import com.weltevree.ego.model.Game;
import com.weltevree.ego.model.GamesViewContentProvider;
import com.weltevree.ego.model.GoServer;
import com.weltevree.ego.model.TreeParent;
import com.weltevree.ego.ui.Activator;

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

public class GamesView extends ViewPart implements SelectionListener {

	private TableColumn gameType;

	private TableColumn gameNumber;

	private TableColumn observers;

	private TableColumn rated;

	private TableColumn byoyomi;

	private TableColumn komi;

	private TableColumn handicap;

	private TableColumn board;

	private TableColumn move;

	private TableColumn rankb;

	private TableColumn blackPlayer;

	private TableColumn rankw;

	private TableColumn whitePlayer;

	private Table table;

	public static final String ID_VIEW = "com.weltevree.ego.views.GamesView";

	private ISelectionListener pageSelectionListener;

	private TableViewer viewer;

	private Action actionRefresh;

	private Action doubleClickAction;

	private GoServer goServer;

	private int sortIndex = 0;

	private boolean sortAscending = true;

	private Action actionEdit;

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {

			if (!(obj instanceof Game))
				return "Not a Game";

			Game game = (Game) obj;

			return getColumnTextFromGame(game, index);
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
			if (!(elements[0] instanceof Game))
				return;

			TreeMap map = new TreeMap();

			for (int ix = 0; ix < elements.length; ix++) {
				map.put(getColumnSortFromGame((Game) elements[ix], sortIndex)
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
	public GamesView() {
	}

	private String getColumnTextFromGame(Game game, int index) {
		switch (index) {
		case 0:
			return Integer.toString(game.getGameNumber());
		case 1:
			return game.getWhitePlayer().getName();
		case 2:
			return game.getWhitePlayer().getRank();
		case 3:
			return game.getBlackPlayer().getName();
		case 4:
			return game.getBlackPlayer().getRank();
		case 5:
			return Integer.toString(game.getMoveNumber());
		case 6:
			return Integer.toString(game.getBoardSize());
		case 7:
			return Integer.toString(game.getHandicap());
		case 8:
			return Float.toString(game.getKomi());
		case 9:
			return Integer.toString(game.getByoyomi());
		case 10:
			return game.getRated();
		case 11:
			return game.getGameType();
		case 12:
			return Integer.toString(game.getObservers());

		default:
			return "vul hier wat in";
		}
	}

	private String getColumnSortFromGame(Game game, int index) {

		DecimalFormat df = new DecimalFormat("00000.00");

		switch (index) {
		case 0:
			return df.format(Long.parseLong(Integer.toString(game
					.getGameNumber())));
		case 1:
			return game.getWhitePlayer().getName();
		case 2:
			return df.format(Long.parseLong(Integer.toString(game
					.getWhitePlayer().getRankToInt())));
		case 3:
			return game.getBlackPlayer().getName();
		case 4:
			return df.format(Long.parseLong(Integer.toString(game
					.getBlackPlayer().getRankToInt())));
		case 5:
			return df.format(Long.parseLong(Integer.toString(game
					.getMoveNumber())));
		case 6:
			return df.format(Long.parseLong(Integer.toString(game
					.getBoardSize())));
		case 7:
			return df.format(Long.parseLong(Integer
					.toString(game.getHandicap())));
		case 8:
			return df.format(Float.parseFloat(Float.toString(game.getKomi())));
		case 9:
			return df.format(Long
					.parseLong(Integer.toString(game.getByoyomi())));
		case 10:
			return game.getRated();
		case 11:
			return game.getGameType();
		case 12:
			return df.format(Long.parseLong(Integer.toString(game
					.getObservers())));

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

		gameNumber = new TableColumn(table, SWT.NONE);
		gameNumber.addSelectionListener(this);
		gameNumber.setMoveable(true);
		gameNumber.setWidth(25);
		gameNumber.setText("ID");

		whitePlayer = new TableColumn(table, SWT.NONE);
		whitePlayer.addSelectionListener(this);
		whitePlayer.setMoveable(true);
		whitePlayer.setWidth(100);
		whitePlayer.setText("White");

		rankw = new TableColumn(table, SWT.NONE);
		rankw.addSelectionListener(this);
		rankw.setMoveable(true);
		rankw.setWidth(35);
		rankw.setText("Rank White");

		blackPlayer = new TableColumn(table, SWT.NONE);
		blackPlayer.addSelectionListener(this);
		blackPlayer.setMoveable(true);
		blackPlayer.setWidth(100);
		blackPlayer.setText("Black");

		rankb = new TableColumn(table, SWT.NONE);
		rankb.addSelectionListener(this);
		rankb.setMoveable(true);
		rankb.setWidth(35);
		rankb.setText("Rank Black");

		move = new TableColumn(table, SWT.NONE);
		move.addSelectionListener(this);
		move.setMoveable(true);
		move.setWidth(40);
		move.setText("Move");

		board = new TableColumn(table, SWT.NONE);
		board.addSelectionListener(this);
		board.setMoveable(true);
		board.setWidth(30);
		board.setText("Board");

		handicap = new TableColumn(table, SWT.NONE);
		handicap.addSelectionListener(this);
		handicap.setMoveable(true);
		handicap.setWidth(30);
		handicap.setText("Handicap");

		komi = new TableColumn(table, SWT.NONE);
		komi.addSelectionListener(this);
		komi.setMoveable(true);
		komi.setWidth(35);
		komi.setText("Komi");

		byoyomi = new TableColumn(table, SWT.NONE);
		byoyomi.addSelectionListener(this);
		byoyomi.setMoveable(true);
		byoyomi.setWidth(30);
		byoyomi.setText("Byoyomi");

		rated = new TableColumn(table, SWT.NONE);
		rated.addSelectionListener(this);
		rated.setMoveable(true);
		rated.setWidth(50);
		rated.setText("Rated");

		gameType = new TableColumn(table, SWT.NONE);
		gameType.addSelectionListener(this);
		gameType.setMoveable(true);
		gameType.setWidth(60);
		gameType.setText("Game Type");

		observers = new TableColumn(table, SWT.NONE);
		observers.addSelectionListener(this);
		observers.setMoveable(true);
		observers.setWidth(50);
		observers.setText("Observers");

		viewer.setContentProvider(new GamesViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
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

		System.out.println("Connected " + gos.isConnected());

		if (!gos.isConnected())
			return;

		goServer = gos;
		if (gos.getGames().isEmpty())
			gos.loadGames();
		viewer.setInput(goServer);
		viewer.refresh();

		setContentDescription("Games on: " + goServer.getDescription());

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				GamesView.this.fillContextMenu(manager);
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
		manager.add(actionEdit);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(actionRefresh);
	}

	private void makeActions() {
		actionRefresh = new Action() {
			public void run() {
				goServer.loadGames();
				viewer.refresh();
			}
		};
		actionRefresh.setText("Refresh");
		actionRefresh.setToolTipText("Refreshes the games list");
		actionRefresh.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_REDO));

		actionEdit = new Action() {
			public void run() {

				EditorInput gei = new EditorInput();

				ISelection selection = viewer.getSelection();

				gei.setObject(((IStructuredSelection) selection)
						.getFirstElement());

				try {
					Activator.getDefault().getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().openEditor((IEditorInput) gei,
									BoardEditor.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		};
		
		actionEdit.setText("Observe");
		actionEdit.setToolTipText("Observes this game");
		actionEdit.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_NEW_WIZARD));

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
				"Games View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void widgetSelected(SelectionEvent e) {

		if (e.getSource() == gameNumber)
			if (sortIndex == 0)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 0;
				sortAscending = true;
			}

		if (e.getSource() == whitePlayer)
			if (sortIndex == 1)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 1;
				sortAscending = true;
			}

		if (e.getSource() == rankw)
			if (sortIndex == 2)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 2;
				sortAscending = true;
			}

		if (e.getSource() == blackPlayer)
			if (sortIndex == 3)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 3;
				sortAscending = true;
			}

		if (e.getSource() == rankb)
			if (sortIndex == 4)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 4;
				sortAscending = true;
			}

		if (e.getSource() == move)
			if (sortIndex == 5)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 5;
				sortAscending = true;
			}

		if (e.getSource() == board)
			if (sortIndex == 6)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 6;
				sortAscending = true;
			}

		if (e.getSource() == handicap)
			if (sortIndex == 7)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 7;
				sortAscending = true;
			}

		if (e.getSource() == komi)
			if (sortIndex == 8)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 8;
				sortAscending = true;
			}

		if (e.getSource() == byoyomi)
			if (sortIndex == 9)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 9;
				sortAscending = true;
			}

		if (e.getSource() == rated)
			if (sortIndex == 10)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 10;
				sortAscending = true;
			}

		if (e.getSource() == gameType)
			if (sortIndex == 11)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 11;
				sortAscending = true;
			}

		if (e.getSource() == observers)
			if (sortIndex == 12)
				sortAscending = sortAscending ? false : true;
			else {
				sortIndex = 12;
				sortAscending = true;
			}

		viewer.refresh();

	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

}