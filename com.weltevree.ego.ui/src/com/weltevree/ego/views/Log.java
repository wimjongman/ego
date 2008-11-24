package com.weltevree.ego.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class Log extends ViewPart
{
    public static final String ID_VIEW = "com.weltevree.ego.views.Log";
 

    class ContentProvider implements IStructuredContentProvider {
        public Object[] getElements(Object inputElement)
        {
            return new Object[] { "item_0", "item_1", "item_2" };
        }
        public void dispose()
        {
        }
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            System.out.println(oldInput);
            System.out.println(newInput);
            System.out.println();

        }
    }
    private TableViewer viewer;

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {

        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new ContentProvider());
        viewer.setInput(getViewSite());
        
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        viewer.getControl().setFocus();
        
    }
    private void createActions()
    {
    }
    private void initializeToolBar()
    {
        IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
    }
    private void initializeMenu()
    {
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
    }}