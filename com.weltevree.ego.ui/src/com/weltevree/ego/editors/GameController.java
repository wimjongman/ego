/**
 * 
 */
package com.weltevree.ego.editors;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.weltevree.ego.figures.Board;
import com.weltevree.ego.model.Game;

/**
 * @author Weltevree jongw
 * @copyright Weltevree Beheer BV 2005
 * 
 */
public class GameController extends AbstractGraphicalEditPart {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		Figure f = new Figure();
        f.setOpaque(true);        
        f.setLayoutManager(new GridLayout(1,true));
        return f;	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}
	@Override
	protected List getModelChildren() {
		
		EditorInput inp = (EditorInput) getModel();
		Game game = (Game) inp.getObject();
		game.getBoardPoints();
		int size = game.getBoardSize();
		Board b = new Board(game.getBoardSize(), this);
		b.setLayoutManager(new XYLayout());
		return null;
	}

}
