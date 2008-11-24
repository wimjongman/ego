package com.weltevree.ego.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.weltevree.ego.views.GamesView;
import com.weltevree.ego.views.Navigator;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "com.weltevree.ego.ui.perspective";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {

		layout.addView(Navigator.ID_VIEW, IPageLayout.LEFT, 0.25f,
				IPageLayout.ID_EDITOR_AREA);

		layout.addShowViewShortcut(Navigator.ID_VIEW);


		layout.addView(GamesView.ID_VIEW, IPageLayout.BOTTOM, 0.75f,
				IPageLayout.ID_EDITOR_AREA);

//		layout.addView(UsersView.ID_VIEW, IPageLayout.BOTTOM, 0.75f,
//				GamesView.ID_VIEW);
	}
}
