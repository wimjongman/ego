package com.weltevree.ego.editors;

import java.awt.FlowLayout;
import java.util.ArrayList;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.weltevree.ego.factory.GraphicalEditPartFactory;
import com.weltevree.ego.ui.Activator;

public class BoardEditor extends GraphicalEditorWithFlyoutPalette {

	public static final String ID = "com.weltevree.ego.editors.BoardEditor";

	private PaletteRoot paletteRoot;

	private FlyoutPreferences flyOutPreferences;

	public BoardEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {

		// TODO Auto-generated method stub
		super.initializeGraphicalViewer();
		setPartName(getEditorInput().getName());

		ArrayList list = new ArrayList();
		{
			PaletteEntry pe = new PaletteEntry("Black", "Black Stone");
			pe.setLargeIcon(Activator.getImageDescriptor("goblack16.gif"));
			list.add(pe);
		}
		{
			PaletteEntry pe = new PaletteEntry("White", "White Stone");
			pe.setLargeIcon(Activator.getImageDescriptor("gowhite16.gif"));
			list.add(pe);
		}

		getPaletteRoot().setChildren(list);

		GameController ggc = new GameController();
	
		ggc.setModel(getEditorInput());
		getGraphicalViewer().setContents(ggc);
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		// TODO Auto-generated method stub
		super.configureGraphicalViewer();

		getGraphicalViewer().setEditPartFactory(
				new GraphicalEditPartFactory());
	}

	@Override
	protected PaletteRoot getPaletteRoot() {

		if (paletteRoot == null) {
			paletteRoot = new PaletteRoot();
			paletteRoot.setLabel("");
		}
		return paletteRoot;
	}

	@Override
	protected FlyoutPreferences getPalettePreferences() {

		if (flyOutPreferences == null) {
			flyOutPreferences = new EgoFlyoutPreferences();
			flyOutPreferences.setPaletteWidth(100);
		}

		return flyOutPreferences;
	}

}
