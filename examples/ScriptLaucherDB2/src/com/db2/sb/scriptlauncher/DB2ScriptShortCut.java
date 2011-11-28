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

import java.net.URI;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchGroup;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Launch shortcut
 * 
 * @author sbartkowski
 * 
 */
public class DB2ScriptShortCut implements ILaunchShortcut {
	
	private final static String NEW_CONFIGURATION = "New_configuration";

	private Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	/**
	 * Show a selection dialog that allows the user to choose one of the
	 * specified launch configurations. Return the chosen config, or
	 * <code>null</code> if the user canceled the dialog.
	 */
	protected ILaunchConfiguration chooseConfiguration(
			ILaunchConfiguration[] configurations) {
		IDebugModelPresentation labelProvider = DebugUITools
				.newDebugModelPresentation();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				getShell(), labelProvider);
		dialog.setElements(configurations);
		dialog.setTitle(Messages.DB2ScriptTabView_List); //$NON-NLS-1$
		dialog.setMessage(Messages.DB2ScriptTabView_Choose); //$NON-NLS-1$
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		return null;
	}

	/**
	 * Execute script
	 * 
	 * @param script
	 *            Script file name (if not null)
	 * @param content
	 *            Script content (if not null)
	 */
	private void runDB2(String script, String content) {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager
				.getLaunchConfigurationType("com.db2.sb.scriptlauncher.DB2ScriptLauncher");
		ILaunchConfiguration[] configurations = null;
		ILaunchConfigurationWorkingCopy workingCopy = null;
		ILaunchConfiguration configuration = null;
		try {
			configurations = manager.getLaunchConfigurations(type);

			if (configurations.length == 0) {
				// Open launch configuration dialog for enter first configuration
				String name = manager.generateLaunchConfigurationName(NEW_CONFIGURATION);
				ILaunchConfigurationWorkingCopy wc = type.newInstance(null,
						name);
				configuration = wc.doSave();
				LaunchConfigurationManager lcm = DebugUIPlugin.getDefault().getLaunchConfigurationManager();
				
				ILaunchGroup group = lcm.getLaunchGroup(type, ILaunchManager.RUN_MODE);
				DebugUITools.openLaunchConfigurationPropertiesDialog(getShell(), configuration, group.getIdentifier());
			} else {
				if (configurations.length == 1) {
					configuration = configurations[0];
				} else {
					// Choose configuration (server) if more than one
					configuration = chooseConfiguration(configurations);
					if (configuration == null) {
						return;
					}
				}
			}

			// get parameters, configuration
			workingCopy = configuration.copy(configuration.getName());
			workingCopy.setAttribute(DB2ScriptTabView.DB_SCRIPT_TO_RUN, script);
			workingCopy.setAttribute(DB2ScriptTabView.DB_CONTENT_TO_RUN,
					content);
			configuration = workingCopy.doSave();

		} catch (CoreException e) {
			e.printStackTrace();
			DB2LogUtil.launchererrorLog(e);
			return;
		}

		// execute
		DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);

	}

	@SuppressWarnings("restriction")
	@Override
	public void launch(ISelection arg0, String arg1) {
		ITreeSelection iSel = (ITreeSelection) arg0;
		TreePath[] t = iSel.getPaths();
		TreePath fPath = t[0];
		Object o = fPath.getLastSegment();
		File f = (File) o;
		URI locationURI = f.getLocationURI();
		URI u = locationURI;
		String fileName = u.getPath();
		runDB2(fileName, null);

	}

	@Override
	public void launch(IEditorPart arg0, String arg1) {
		ITextEditor i = (ITextEditor) arg0;
		IDocumentProvider dProvider = i.getDocumentProvider();
		IDocument iDok = dProvider.getDocument(i.getEditorInput());
		String content = iDok.get();
		runDB2(null, content);

	}
}
