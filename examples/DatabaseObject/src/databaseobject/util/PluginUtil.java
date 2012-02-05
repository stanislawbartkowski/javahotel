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
package databaseobject.util;

import java.net.URI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.IConnection;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import databaseobject.Activator;
import databaseobject.messages.Messages;

/**
 * Utility (static) class with different useful methods
 * 
 * @author sbartkowski
 * 
 */

public class PluginUtil {

	private PluginUtil() {

	}

	/**
	 * Returns default Shell
	 * 
	 * @return Shell
	 */
	static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	/**
	 * Show message
	 * 
	 * @param title
	 *            Title of the message window (can be null)
	 * @param mess
	 *            message content
	 */
	public static void showMess(String title, String mess) {
		MessageDialog.openInformation(getShell(), title, mess);
	}

	/**
	 * Display error message (one string)
	 * 
	 * @param errMessage
	 *            String to display as an error message.
	 */
	public static void errorMessage(String errMessage) {
		Shell parent = getShell();
		MessageBox messageBox = new MessageBox(parent, SWT.ICON_ERROR);
		messageBox.setMessage(errMessage);
		messageBox.open();
	}

	/**
	 * Display error message with java exception
	 * 
	 * @param errMessage
	 *            error message
	 * @param error
	 *            Exception to display
	 */
	public static void errorMessage(String errMessage, Throwable error) {
		Status sta = new Status(IStatus.ERROR, Activator.PLUGIN_ID, errMessage,
				error);
		ErrorDialog err = new ErrorDialog(getShell(), null, null, sta,
				IStatus.ERROR);
		err.open();
	}

	/**
	 * Retrieves IResource being selected
	 * 
	 * @param sel
	 *            ISelection
	 * @return
	 */
	public static IResource extractSelection(ISelection sel) {
		if (!(sel instanceof IStructuredSelection))
			return null;
		IStructuredSelection ss = (IStructuredSelection) sel;
		Object element = ss.getFirstElement();
		if (element instanceof IResource)
			return (IResource) element;
		if (!(element instanceof IAdaptable))
			return null;
		IAdaptable adaptable = (IAdaptable) element;
		Object adapter = adaptable.getAdapter(IResource.class);
		return (IResource) adapter;
	}

	/**
	 * Get connection name from project properties
	 * 
	 * @param iP
	 *            IProject
	 * @return connection name
	 */
	public static String getConnectionId(IProject iP) {
		try {
			String id = iP.getPersistentProperty(new QualifiedName("", //$NON-NLS-1$
					CommonProp.DATABASECONNECTION_PROPERTY));
			return id;
		} catch (CoreException e) {
			LogUtil.log(Messages.PluginUtil_1, e);
		}
		return null;
	}

	/**
	 * Get connection from project properties optionally signals that connection
	 * is not define
	 * 
	 * @param iP
	 *            IProject
	 * @param signal
	 *            true: signal error if connection not defined
	 * @return IConnectionProfile or null if not defined
	 */
	public static IConnectionProfile getConnection(IProject iP, boolean signal) {
		String id = getConnectionId(iP);
		if (id == null) {
			if (signal) {
				errorMessage(Messages.PluginUtil_2);
			}
			return null;
		}
		List<IConnectionProfile> pList = getConnectionList();
		for (IConnectionProfile p : pList) {
			String pId = p.getInstanceID();
			if (pId.equals(id)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Get list of all DB2 database connection defined in Database tools
	 * 
	 * @return List of connections
	 */
	public static List<IConnectionProfile> getConnectionList() {
		ProfileManager pManager = ProfileManager.getInstance();
		IConnectionProfile[] pI = pManager.getProfiles();
		List<IConnectionProfile> pList = new ArrayList<IConnectionProfile>();
		for (IConnectionProfile p : pI) {
			String pName = p.getProviderId();
			// Important: checks if DB2 connection
			if (pName.contains(CommonProp.DBType)) {
				pList.add(p);
			}
		}
		return pList;
	}

	private static boolean verifyActionForProject(IProject iP)
			throws CoreException {
		if (!iP.hasNature(CommonProp.NATURE_ID)) {
			errorMessage(Messages.PluginUtil_3);
			return false;
		}
		return true;
	}

	/**
	 * Verify if selection is on folder belonging to valid project and proper
	 * name
	 * 
	 * @param iP
	 *            IProject
	 * @param i
	 *            ISelection
	 * @param folder
	 *            folder name to compare to ('PL/SQL')
	 * @return true: ok false: not valid and error message was displayed
	 * @throws CoreException
	 */
	public static boolean verifyActionForFolder(IProject iP, ISelection i,
			String folder) throws CoreException {
		if (!verifyActionForProject(iP)) {
			return false;
		}
		IResource iR = extractSelection(i);
		if (!(iR instanceof IFolder)) {
			errorMessage(Messages.PluginUtil_4);
			return false;
		}
		IFolder f = (IFolder) iR;
		IPath pa = f.getFullPath();
		String se = pa.lastSegment();
		if ((se != null) && !se.equals(folder)) {
			errorMessage(Messages.PluginUtil_5 + folder + Messages.PluginUtil_6);
			return false;
		}
		return true;
	}

	/**
	 * Verify if selection is on script file
	 * 
	 * @param iP
	 *            IProject
	 * @param i
	 *            ISelection
	 * @return true: ok, false : not valid and error message was displayed
	 * @throws CoreException
	 */
	public static boolean verifyActionForDB2File(IProject iP, ISelection i)
			throws CoreException {
		if (!verifyActionForProject(iP)) {
			return false;
		}
		IResource iR = extractSelection(i);
		if (!(iR instanceof IFile)) {
			errorMessage(Messages.PluginUtil_7);
			return false;
		}
		IFile f = (IFile) iR;
		IPath pa = f.getFullPath();
		String se = pa.lastSegment();
		if ((se != null) && !se.endsWith(".db2")) { //$NON-NLS-1$
			errorMessage(Messages.PluginUtil_9);
			return false;
		}
		return true;
	}

	/**
	 * Get connection for project
	 * 
	 * @param iP
	 *            IProject
	 * @return Connection (jdbc)
	 */
	public static Connection getConnection(IProject iP) {
		IConnectionProfile iProf = PluginUtil.getConnection(iP, true);
		if (iProf == null) {
			return null;
		}
		IStatus s = iProf.connect();
		if (!s.isOK()) {
			return null;
		}
		IConnection iC = iProf.createConnection("java.sql.Connection"); //$NON-NLS-1$
		if (iC == null) {
			return null;
		}
		Connection jdbc = (Connection) iC.getRawConnection();
		return jdbc;
	}

	/**
	 * Return full path name for selection
	 * 
	 * @param arg0
	 *            ISelection
	 * @return path file name
	 */
	public static String getFileName(ISelection arg0) {
		ITreeSelection iSel = (ITreeSelection) arg0;
		TreePath[] t = iSel.getPaths();
		TreePath fPath = t[0];
		Object o = fPath.getLastSegment();
		File f = (File) o;
		URI locationURI = f.getLocationURI();
		URI u = locationURI;
		String fileName = u.getPath();
		return fileName;
	}
}
