/*
 * Copyright 2011 stanislawbartkowski@gmail.com
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
package com.db2.sb.scriptlauncher;

import java.io.IOException;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;

public class DB2ScriptTabView extends JavaLaunchTab {

	private Text db2Alias;
	private Text db2User;
	private Text db2Password;

	public final static String DBACCESS_ID = "com.db2.sb.scriptlauncher.DB2_ACCESS"; //$NON-NLS-1$

	public final static String DB_SCRIPT_TO_RUN = "com.db2.sb.scriptlauncher.DB_SCRIPT_TO_RUN"; //$NON-NLS-1$
	public final static String DB_CONTENT_TO_RUN = "com.db2.sb.scriptlauncher.DB_CONTENT_TO_RUN"; //$NON-NLS-1$

	private final static String DBNAME_LABEL = Messages.DB2ScriptTabView_DB2_ALIAS_NAME;
	private final static String DB_USER = Messages.DB2ScriptTabView_DB2_USER_NAME;
	private final static String DB_PASSWORD = Messages.DB2ScriptTabView_DB2_PASSWORD;

	@SuppressWarnings("restriction")
	@Override
	public void createControl(final Composite parent) {

		ModifyListener listener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				updateLaunchConfigurationDialog();
			}
		};

		Composite comp = SWTFactory.createComposite(parent, 1, 1,
				GridData.CENTER);
		setControl(comp);
		// Composite namecomp = SWTFactory.createComposite(comp, comp.getFont(),
		// 4, 1, GridData.FILL_HORIZONTAL, 0, 0);
		Group namecomp = SWTFactory.createGroup(comp,
				Messages.DB2ScriptTabView_DB2_ACCESS_DATA, 4, 1,
				GridData.FILL_HORIZONTAL);

		SWTFactory.createLabel(namecomp, DBNAME_LABEL, 1);
		db2Alias = SWTFactory.createSingleText(namecomp, 1);
		db2Alias.addModifyListener(listener);

		// namecomp = SWTFactory.createComposite(comp, comp.getFont(), 4, 1,
		// GridData.FILL_HORIZONTAL, 0, 0);

		SWTFactory.createLabel(namecomp, DB_USER, 1);
		db2User = SWTFactory.createSingleText(namecomp, 1);
		db2User.addModifyListener(listener);

		// namecomp = SWTFactory.createComposite(comp, comp.getFont(), 4, 1,
		// GridData.FILL_HORIZONTAL, 0, 0);

		SWTFactory.createLabel(namecomp, DB_PASSWORD, 1);
		db2Password = SWTFactory.createSingleText(namecomp, 1);
		db2Password.addModifyListener(listener);

		Composite bcomp = SWTFactory.createComposite(comp, comp.getFont(), 4,
				1, GridData.FILL_HORIZONTAL, 0, 0);
		Button b = SWTFactory.createPushButton(bcomp,
				Messages.DB2ScriptTabView_DB2_BUTTON_TEST, null);

		b.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					updateLaunchConfigurationDialog();
					DB2LineFactory.executeDB2Script(parent.getShell(), null,
							db2Alias.getText(), db2User.getText(),
							db2Password.getText(), null, null);
				} catch (IOException e) {
					e.printStackTrace();
					DB2LogUtil.launchererrorLog(e);
				} catch (InterruptedException e) {
					e.printStackTrace();
					DB2LogUtil.launchererrorLog(e);
				} catch (PartInitException e) {
					DB2LogUtil.launchererrorLog(e);
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public String getName() {
		return Messages.DB2ScriptTabView_DB2_LAUNCHER_NAME;
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {
		DB2AccessData a = DB2AccessData.contructDefa();
		arg0.setAttribute(DBACCESS_ID, a.getParamList());
	}

	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		DB2AccessData a = DB2AccessData.construct(config);
		db2Alias.setText(a.getDb2Alias());
		db2User.setText(a.getDb2User());
		db2Password.setText(a.getDb2Password());
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		DB2AccessData a = DB2AccessData.contructDefa();
		a.setDb2Alias(db2Alias.getText());
		a.setDb2User(db2User.getText());
		a.setDb2Password(db2Password.getText());
		configuration.setAttribute(DBACCESS_ID, a.getParamList());
	}

}
