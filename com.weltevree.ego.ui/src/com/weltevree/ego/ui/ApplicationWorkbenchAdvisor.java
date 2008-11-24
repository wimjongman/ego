package com.weltevree.ego.ui;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}

	@Override
	public void preWindowOpen(IWorkbenchWindowConfigurer configurer) {
		super.preWindowOpen(configurer);
		configurer.setInitialSize(new Point(800, 700));
		configurer.setShowCoolBar(true);
		// configurer.setShowStatusLine(false);
		configurer.setTitle("EGo");
		// TODO Auto-generated method stub

	}

}
