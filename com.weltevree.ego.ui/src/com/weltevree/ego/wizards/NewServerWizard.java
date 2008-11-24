package com.weltevree.ego.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.weltevree.ego.model.GoServer;
import com.weltevree.ego.ui.Activator;
import com.weltevree.ego.util.ObjectToDisk;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class NewServerWizard extends Wizard implements INewWizard {
	private NewServerWizardPage page;

	private ISelection selection;

	private GoServer goServer;

	/**
	 * Constructor for NewServerWizard.
	 */
	public NewServerWizard() {
		super();
	//	setNeedsProgressMonitor(true);
		setForcePreviousAndNextButtons(false);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewServerWizardPage(selection);
		addPage(page);
		
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {

		IRunnableWithProgress op = new IRunnableWithProgress() {

			final String servert = page.getServer();

			final String serverDescription = page.getServerDescription();

			final String login = page.getLogin();

			final String password = page.getPassword();

			final String port = page.getPort();

			final String filename = page.getFileName();

			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {

				goServer = new GoServer();
				goServer.setServerAddress(servert);
				goServer.setDescription(serverDescription);
				goServer.setLogin(login);
				goServer.setPort(port);
				goServer.setPassword(password);
				goServer.setFileName(filename);
				doFinish(goServer);

			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(GoServer goServer) {
		/*
		 * Run this in the UI thread.
		 */
		final GoServer server = goServer;

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					/*
					 * Create the File object to save in. If it exists, ask for
					 * a confirmation to replace.
					 */
					URL home = Platform.resolve(Activator.getDefault()
							.getBundle().getEntry("/"));

					final File file = new File(home.getPath() + "local/"
							+ System.getProperty("user.name") + "/"
							+ server.getFileName().trim());

					if (file.exists())

						if (!MessageDialog.openConfirm(Display.getDefault()
								.getActiveShell(), "Confirm",
								"Do you want to overwrite the existing"
										+ " server definition?"))
							file.delete();

					/*
					 * Try to save the GoServer object. If it fails the show a
					 * message.
					 */
					FileOutputStream fos = new FileOutputStream(file);
					new ObjectToDisk((Object) server, fos);

				} catch (Exception e) {
					MessageDialog
							.openError(
									Display.getDefault().getActiveShell(),
									"Error",
									"The server could not be saved."
											+ "\nProbably because of problems with the filename."
											+ "\nThe file name used for saving was"
											+ "\n"
											+ server.getFileName().trim()
											+ "\n\nReal Error: "
											+ e.getMessage());
				}
			}
		});
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

	public GoServer getGoServer() {
		return goServer;
	}
}