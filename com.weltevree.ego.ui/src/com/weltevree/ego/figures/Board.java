package com.weltevree.ego.figures;

import java.util.HashMap;


import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.weltevree.ego.editors.GameController;
import com.weltevree.ego.preferences.PreferencePage;
import com.weltevree.ego.ui.Activator;

public class Board extends Figure implements LayoutListener {

	private int boardSize = 19;

	protected HashMap disposeList = new HashMap();

	private int height;

	private int offsetX;

	private int offsetY;

	private int stepx;

	private int stepy;

	private int tox;

	private int toy;

	private int width;

	private GameController controller;

	private Composite composite;

	public Board(int boardSize, GameController controller) {
		super();
		this.boardSize = boardSize;
		this.controller = controller;
		composite = controller.getParent().getViewer().getControl().getParent();
	}

	private void paintBoard(Graphics graphics) {

		// disposeList.put(new Integer(graphics.hashCode()), graphics);

		ImageData idta = new ImageData(Activator.getDefault()
				.getPreferenceStore().getString("boardImage"));

		Image img = new Image(null, idta);
		// disposeList.put(new Integer(img.hashCode()), img);

		GC gci = new GC(img);
		// disposeList.put(new Integer(gci.hashCode()), gci);
		graphics.drawImage(img, 0, 0, 800, 600, 0, 0, width, height);

		if (width > height)
			width = (int) ((height * 1.4) / 1.5);
		else
			height = (int) ((width * 1.5) / 1.4);

		height = height / (boardSize) * (boardSize);
		width = width / (boardSize) * (boardSize);

		stepy = height / (boardSize);
		stepx = width / (boardSize);
		toy = height - stepy;
		tox = width - stepx;

		offsetX = (getClientArea().width / 2) - (width / 2);
		offsetY = (getClientArea().height / 2) - (height / 2);

		for (int iy = 0; iy < height - 1; iy += stepy) {
			graphics.drawLine(offsetX, iy + offsetY, tox + offsetX, iy
					+ offsetY);
			graphics.drawText(Integer.toString((iy / stepy) + 1), tox + offsetX
					+ 15, iy);
		}

		for (int ix = 0; ix < width - 1; ix += stepx) {
			graphics.drawLine(ix + offsetX, 0 + offsetY, ix + offsetX, toy
					+ offsetY);
			graphics.drawText(Integer.toString((ix / stepx) + 1), ix + offsetX
					- 5, toy + 10);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void paintFigure(Graphics graphics) {

		Rectangle rect = composite.computeTrim(0, 0, getClientArea().width,
				getClientArea().height);

		height = rect.height;
		width = rect.width;
		setSize(width, height);

		paintBoard(graphics);

		
		composite.setSize(width, height);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#removeNotify()
	 */
	@Override
	public void removeNotify() {
		// TODO Auto-generated method stub
		super.removeNotify();

		System.out.println("Board : removeNotify()");
	}

	/**
	 * @return Returns the boardSize.
	 */
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 * @param boardSize
	 *            The boardSize to set.
	 */
	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;

	}

	public void invalidate(IFigure container) {
		// TODO Auto-generated method stub

	}

	public boolean layout(IFigure container) {
		// TODO Auto-generated method stub
		return false;
	}

	public void postLayout(IFigure container) {
		// TODO Auto-generated method stub

	}

}
