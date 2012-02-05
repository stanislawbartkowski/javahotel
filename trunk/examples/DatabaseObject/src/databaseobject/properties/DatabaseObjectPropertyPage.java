/*
 * Copyright 2012 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package databaseobject.properties;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import databaseobject.messages.Messages;
import databaseobject.util.CommonProp;
import databaseobject.util.LogUtil;
import databaseobject.util.PluginUtil;

/**
 * Class used for implementing project properties
 * (contains only database DB2 connection)
 * @author sbartkowski
 *
 */
public class DatabaseObjectPropertyPage extends PropertyPage {

	private static final String PROJECT_TITLE = Messages.DatabaseObjectPropertyPage_0;
	private static final String DATABASEC = Messages.DatabaseObjectPropertyPage_1;
	private static final String DEFAULT_DATABASE = ""; //$NON-NLS-1$

	private static final int TEXT_FIELD_WIDTH = 50;
	private final List<IConnectionProfile> pList;

	private CCombo databaseConn;

	private IProject getProject() {
		IResource i = (IResource) getElement();
		return i.getProject();
	}

	private CCombo constructCCombo(Composite parent) {
		CCombo combo = new CCombo(parent, SWT.BORDER | SWT.READ_ONLY);
		for (IConnectionProfile p : pList) {
			combo.add(p.getName());
		}
		return combo;
	}

	public DatabaseObjectPropertyPage() {
		super();
		pList = PluginUtil.getConnectionList();
	}

	private void addFirstSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for path field
		Label pathLabel = new Label(composite, SWT.NONE);
		pathLabel.setText(PROJECT_TITLE);

		// Path text field
		Text pathValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
		IProject iP = getProject();
		iP.getName();
		pathValueText.setText(iP.getName());
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	private void addSecondSection(Composite parent) {
		if (pList.size() == 0) {
			PluginUtil
					.errorMessage(Messages.DatabaseObjectPropertyPage_2);
		}
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText(DATABASEC);

		// Owner text field
		// databaseConn = new Text(composite, SWT.SINGLE | SWT.BORDER);
		databaseConn = constructCCombo(parent);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		databaseConn.setLayoutData(gd);

		// Populate database combo field
		IProject iP = getProject();
		IConnectionProfile iConn = PluginUtil.getConnection(iP,false);
		if (iConn != null) {
			databaseConn.setText(iConn.getName());
		}
		else {
			databaseConn.setText(DEFAULT_DATABASE);
		}
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		addFirstSection(composite);
		addSeparator(composite);
		addSecondSection(composite);
		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected void performDefaults() {
		super.performDefaults();
		// Populate the owner text field with the default value
		databaseConn.setText(null);
	}

	public boolean performOk() {
		// store the value in the owner text field
		try {
			IProject i = getProject();
			String id = null;
			String dName = databaseConn.getText();
			for (IConnectionProfile p : pList) {
				if (p.getName().equals(dName)) {
					id = p.getInstanceID();
					break;
				}
			}
			
			i.setPersistentProperty(new QualifiedName("", //$NON-NLS-1$
					CommonProp.DATABASECONNECTION_PROPERTY), id);
			
		} catch (CoreException e) {
			LogUtil.log(Messages.DatabaseObjectPropertyPage_5, e);
		}
		return true;
	}

}