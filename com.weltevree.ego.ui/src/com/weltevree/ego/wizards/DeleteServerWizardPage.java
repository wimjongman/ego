package com.weltevree.ego.wizards;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
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

/** 
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class DeleteServerWizardPage extends WizardPage
{
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

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public DeleteServerWizardPage(ISelection selection)
    {
        super("wizardPage");
        setTitle("Delete Go Server Definition Wizard");
        setDescription("This wizard helps you to remove a server definition.");
        this.selection = selection;
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(Composite parent)
    {
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
        server.setEnabled(false);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        server.setLayoutData(gd);
        server.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });

        label_3 = new Label(container, SWT.NONE);
        label_3.setText("P&ort:");

        port = new Text(container, SWT.BORDER);
        port.setEnabled(false);
        port.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });

        gd_3 = new GridData(GridData.FILL_HORIZONTAL);
        port.setLayoutData(gd_3);
 
        label_2 = new Label(container, SWT.NONE);
        label_2.setText("&login:");

        login = new Text(container, SWT.BORDER);
        login.setEnabled(false);
        gd_1 = new GridData(GridData.FILL_HORIZONTAL);
        login.setLayoutData(gd_1);
        login.setText("guest");

        labeltje = new Label(container, SWT.NONE);
        labeltje.setText("&Password:");

        password = new Text(container, SWT.BORDER);
        password.setEnabled(false);
        password.setEchoChar('*');
        gd_2 = new GridData(GridData.FILL_HORIZONTAL);
        password.setLayoutData(gd_2);
        password.setText("anonymous");
        password.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        label = new Label(container, SWT.NULL);
        label.setText("&Description:");

        serverDescription = new Text(container, SWT.BORDER | SWT.SINGLE);
        serverDescription.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        serverDescription.setLayoutData(gd);
        serverDescription.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
  
        final Label filenameLabel = new Label(container, SWT.NONE);
        filenameLabel.setText("Filename:");

        fileName = new Text(container, SWT.BORDER);
        fileName.setEnabled(false);
        fileName.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        
        initialize();
        dialogChanged();
        setControl(container);
 
        GoServer gos = ((DeleteServerWizard) this.getWizard()).getGoServer();
        setServer(gos.getServerAddress());
        setPort(gos.getPort());
        setLogin(gos.getLogin());
        setPassword(gos.getPassword());
        setServerDescription(gos.getDescription());
        setFileName(gos.getFileName());
        

    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */

    private void initialize()
    {
        if (selection != null && selection.isEmpty() == false
                && selection instanceof IStructuredSelection)
        {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            if (ssel.size() > 1) return;
            Object obj = ssel.getFirstElement();
            if (obj instanceof IResource)
            {
                IContainer container;
                if (obj instanceof IContainer)
                    container = (IContainer) obj;
                else
                    container = ((IResource) obj).getParent();
                server.setText(container.getFullPath().toString());
            }
        }
    }

    /**
     * Ensures that both text fields are set.
     */

    private void dialogChanged()
    {

        updateStatus(null);
    }

    private void updateStatus(String message)
    {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public void setLogin(String login)
    {
        this.login.setText(login);
    }

    public void setPassword(String password)
    {
        this.password.setText(password);
    }

    public void setPort(String port)
    {
        this.port.setText(port);

    }

    public void setServer(String server)
    {
        this.server.setText(server);

    }

    public void setServerDescription(String serverDescription)
    {
        this.serverDescription.setText(serverDescription);
    }

	public Text getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName.setText(fileName);
	}
}