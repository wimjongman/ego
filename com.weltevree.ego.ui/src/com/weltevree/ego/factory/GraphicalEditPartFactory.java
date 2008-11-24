package com.weltevree.ego.factory;


import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.weltevree.ego.editors.GameController;

public class GraphicalEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		
		System.out.println("GraphicalEditPartFactory: " + model.toString());
		
		GameController ggc = new GameController();
	
		
		return ggc;
		// TODO Auto-generated method stub
//		return null;
	}

}
