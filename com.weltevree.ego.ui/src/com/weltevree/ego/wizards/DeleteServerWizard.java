package com.weltevree.ego.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.weltevree.ego.model.GoServer;
import com.weltevree.ego.ui.Activator;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class DeleteServerWizard extends Wizard implements INewWizard {
	private GoServer gos;

	private DeleteServerWizardPage page;

	private ISelection selection;

	/**
	 * Constructor for NewServerWizard.
	 */
	public DeleteServerWizard(GoServer gos) {
		super();
		this.gos = gos;
	//	setNeedsProgressMonitor(true);
		setForcePreviousAndNextButtons(false);
		this.addPages();
	}

	/**
	 * Adding the page to the wizard.
	 */

	public final void addPages() {
		page = new DeleteServerWizardPage(selection);
		addPage(page);
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(String fileName)

	{

		try {
			final URL home = Platform.resolve(Activator.getDefault().getBundle()
					.getEntry("/"));

			final File file = new File(home.getPath() + "local/"
					+ System.getProperty("user.name") + "/" + fileName.trim());

			file.delete();

		} catch (Exception e) {
			MessageDialog.openError(getShell(), "Error",
					"The server could not be removed." + "\n\nBecause of: "
							+ e.getMessage());
		}
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		
		boolean finishedOk = true;

		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				if (gos.isConnected())
					gos.disconnect();
				doFinish(gos.getFileName());
			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			finishedOk = false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			finishedOk = false;
		}
		return finishedOk;
	}

	public GoServer getGoServer() {
		return gos;
	}
}