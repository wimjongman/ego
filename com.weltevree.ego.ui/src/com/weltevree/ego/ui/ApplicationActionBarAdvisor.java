package com.weltevree.ego.ui;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		try {
			register(ActionFactory.CLOSE.create(window));
			register(ActionFactory.SAVE.create(window));
			register(ActionFactory.SAVE_AS.create(window));
			register(ActionFactory.QUIT.create(window));
			register(ActionFactory.INTRO.create(window));
			register(ActionFactory.ABOUT.create(window));
			register(ActionFactory.HELP_CONTENTS.create(window));
		} catch (final RuntimeException e) {
			Activator.getDefault().getLog().log(
					new Status(Status.ERROR, Activator.ID, e.getMessage(), e));
		}
	}

	protected void fillMenuBar(IMenuManager menuBar) {

	}

}
