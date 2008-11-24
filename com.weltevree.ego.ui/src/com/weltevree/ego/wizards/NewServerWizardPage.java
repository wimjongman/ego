package com.weltevree.ego.wizards;


import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.weltevree.ego.model.GoServer;
import com.weltevree.ego.model.TreeObject;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewServerWizardPage extends WizardPage {
	private Label fileNameLabel;

	private Text fileName;

	private Text port;

	private Label label_3;

	private Text password;

	private Label labeltje;

	private Text login;

	private Label label_2;

	private Text server;

	private Text serverDescription;

	private ISelection selection;

	protected boolean syncName = true;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewServerWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("New Go Server Definition Wizard");
		setDescription("This wizard creates a new Go server definition to which you can connect.");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		GridData gd_1;
		GridData gd_2;
		GridData gd_3;
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Server:");

		server = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		server.setLayoutData(gd);
		server.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label_3 = new Label(container, SWT.NONE);
		label_3.setText("P&ort:");

		port = new Text(container, SWT.BORDER);
		port.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		port.setText("6969");
		gd_3 = new GridData(GridData.FILL_HORIZONTAL);
		port.setLayoutData(gd_3);

		label_2 = new Label(container, SWT.NONE);
		label_2.setText("&login:");

		login = new Text(container, SWT.BORDER);
		gd_1 = new GridData(GridData.FILL_HORIZONTAL);
		login.setLayoutData(gd_1);
		login.setText("guest");

		labeltje = new Label(container, SWT.NONE);
		labeltje.setText("&Password:");

		password = new Text(container, SWT.BORDER);
		gd_2 = new GridData(GridData.FILL_HORIZONTAL);
		password.setLayoutData(gd_2);
		password.setText("anonymous");
		password.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Description:");

		serverDescription = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		serverDescription.setLayoutData(gd);
		serverDescription.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (syncName) {
					setFileName(getServerDescription() + ".dat");
					syncName = true;
				}
				dialogChanged();
			}
		});

		initialize();
		dialogChanged();
		setControl(container);

		fileNameLabel = new Label(container, SWT.NONE);
		fileNameLabel.setText("&Filename:");

		fileName = new Text(container, SWT.BORDER);
		final GridData gd_4 = new GridData(GridData.FILL_HORIZONTAL);
		fileName.setLayoutData(gd_4);
		fileName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				syncName = false;
				dialogChanged();
			}
		});

		/*
		 * If one of the severs was selected then display the information of
		 * that server.
		 */

		if (selection != null) {
			setTitle("Change Go Server Definition Wizard");
			TreeObject to = (TreeObject) ((IStructuredSelection) selection)
					.getFirstElement();
			GoServer gos = (GoServer) to.getObject();
			password.setText(gos.getPassword());
			login.setText(gos.getLogin());
			server.setText(gos.getServerAddress());
			serverDescription.setText(gos.getDescription());
			port.setText(gos.getPort());
			fileName.setText(gos.getFileName());
			syncName = false;
			fileNameLabel.setEnabled(false);
			fileName.setEnabled(false);
		}

	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {

	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		if (getServer().length() == 0) {
			updateStatus("Server must be specified");
			return;
		}

		if (getDescription().length() == 0) {
			updateStatus("Description must be specified");
			return;
		}

		if (getFileName().length() == 0) {
			updateStatus("Filename must be specified");
			return;
		}

		if (!(getPort().trim().matches("[0-9].*") && Integer.parseInt(getPort()
				.trim()) < 65535)) {
			updateStatus("Port number must be numeric and less then 65535");
			return;
		}

		if (getLogin().length() == 0) {
			updateStatus("login must be specified");
			return;
		}

		if (getPassword().length() == 0) {
			updateStatus("password must be specified");
			return;
		}

		updateStatus(null);
	}

	private void setFileName(String string) {
		fileName.setText(string);

	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * @return Returns the description.
	 */
	public String getServerDescription() {
		return serverDescription.getText();
	}

	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login.getText();
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password.getText();
	}

	/**
	 * @return Returns the port.
	 */
	public String getPort() {
		return port.getText();
	}

	/**
	 * @return Returns the server.
	 */
	public String getServer() {
		return server.getText();
	}

	public String getFileName() {
		return fileName.getText();
	}

}