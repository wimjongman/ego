package com.weltevree.ego.preferences;


import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.weltevree.ego.ui.Activator;

public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public PreferencePage() {
		super(FieldEditorPreferencePage.GRID);
	}

	protected void createFieldEditors() {
		{
			addField(new FileFieldEditor("boardImage",
					"Board Background Image", getFieldEditorParent()));
		}

		{
			addField(new FileFieldEditor("blackStone", "Black Stone Image",
					getFieldEditorParent()));
		}

		{
			addField(new FileFieldEditor("whiteStone", "White Stone Image",
					getFieldEditorParent()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {

		getPreferenceStore().setToDefault("boardImage");

	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return Activator.getDefault().getPreferenceStore();
	}

	public void init(IWorkbench workbench) {
	}

}
