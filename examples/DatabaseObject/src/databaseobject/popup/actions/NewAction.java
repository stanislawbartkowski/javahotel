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

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.feature.tree.FeatureTree;
import org.feature.tree.FeatureTree.TreeItem;
import org.feature.tree.TreeDialog;

import databaseobject.messages.Messages;
import databaseobject.util.CommonProp;
import databaseobject.util.CreateObjectTree;
import databaseobject.util.PluginUtil;

/**
 * Class called as a response to extract packages action
 * @author sbartkowski
 *
 */
public class NewAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelection iSel = null;

	/**
	 * Constructor for Action1.
	 */
	public NewAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		IWorkbench iworkbench = PlatformUI.getWorkbench();
		IWorkbenchWindow i = iworkbench.getActiveWorkbenchWindow();
		ISelection sel = i.getSelectionService().getSelection();
		IResource ii = PluginUtil.extractSelection(sel);
		final IProject iP = ii.getProject();

		try {

			// valid only for PL/SQL folder in Database Object profile
			if (!PluginUtil.verifyActionForFolder(iP, iSel,
					CommonProp.PLSQLPACKAGE)) {
				return;
			}
			final Connection jdbc = PluginUtil.getConnection(iP);
			final FeatureTree packages = CreateObjectTree.createPLSQLTree(jdbc);

			TreeDialog.ITreeDoubleCheckListener dListener = new TreeDialog.ITreeDoubleCheckListener() {

				@Override
				public void doSomething(TreeItem item) {
					try {
						// draw package (body and specification)
						CreateObjectTree.PackageObject pa = CreateObjectTree
								.getPackage(jdbc, item);						
						if (pa != null) {
							DrawPackage da = new DrawPackage(shell, pa);
							da.open();
						}
					} catch (SQLException e) {
						PluginUtil.errorMessage(Messages.ErrorWhileReagingSQL, e);
					}
				}

			};
			
			IButtonSelected extractAction = new IButtonSelected() {

				@Override
				public boolean action() {
					try {
						int i = ExtractObjects.extract(iP,jdbc, packages);
						if (i == 0) { return false; }
						String message = MessageFormat.format(Messages.MessageDeployed, new Object[] {i});
						PluginUtil.showMess(null, message);						
						return true;
					} catch (CoreException e) {
						PluginUtil.errorMessage(Messages.ErrorWhileWritingSQL, e);
					} catch (SQLException e) {
						PluginUtil.errorMessage(Messages.ErrorWhileWritingSQL, e);
					}
					return false;

				}

			};
			ExtractDialog dial = new ExtractDialog(shell, SWT.APPLICATION_MODAL
					| SWT.DIALOG_TRIM, packages, dListener, extractAction);
			dial.open();

		} catch (CoreException e) {
			PluginUtil.errorMessage(Messages.ErrorWhileReagingSQL, e);
		} catch (SQLException e) {
			PluginUtil.errorMessage(Messages.ErrorWhileReagingSQL, e);
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		iSel = selection;
	}

}
