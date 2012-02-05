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
package databaseobject.popup.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import databaseobject.messages.Messages;
import databaseobject.util.PluginUtil;

/**
 * Class called as a response to "Deploy package" menu item
 * @author sbartkowski
 *
 */

public class DeployAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelection iSel = null;

	@Override
	public void run(IAction arg0) {
		IWorkbench iworkbench = PlatformUI.getWorkbench();
		IWorkbenchWindow i = iworkbench.getActiveWorkbenchWindow();
		ISelection sel = i.getSelectionService().getSelection();
		IResource ii = PluginUtil.extractSelection(sel);
		final IProject iP = ii.getProject();
		try {
			// check if clicked on DB2 script file 
			if (!PluginUtil.verifyActionForDB2File(iP, iSel)) {
				return;
			}
			// get jdbc connection
			Connection jdbc = PluginUtil.getConnection(iP);
			// read and prepare script as string
			String fileName = PluginUtil.getFileName(sel);
			FileReader r = new FileReader(new File(fileName));
			String sql = "";
			int ch;
			while ((ch = r.read()) != -1) {
				sql += (char) ch;
			}

			// run sql statement (deploy PL/SQL package here)
			Statement stmt = jdbc.createStatement();
			stmt.execute(sql);
		} catch (CoreException e) {
			PluginUtil.errorMessage(Messages.ErrorWhileWritingSQL, e);
		} catch (SQLException e) {
			PluginUtil.errorMessage(Messages.ErrorWhileWritingSQL, e);
		} catch (FileNotFoundException e) {
			PluginUtil.errorMessage(Messages.ErrorWhileWritingSQL, e);
		} catch (IOException e) {
			PluginUtil.errorMessage(Messages.ErrorWhileWritingSQL, e);
		}

	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		iSel = selection;
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

}
