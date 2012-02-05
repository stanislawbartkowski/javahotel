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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.feature.tree.FeatureTree;
import org.feature.tree.FeatureTree.TreeItem;

import databaseobject.messages.Messages;
import databaseobject.util.CommonProp;
import databaseobject.util.CreateObjectTree;
import databaseobject.util.CreateObjectTree.PackageName;
import databaseobject.util.PluginUtil;
import databaseobject.util.ProjectSupport;

/**
 * Static class with methods related to package extraction
 * @author sbartkowski
 *
 */
class ExtractObjects {

	private ExtractObjects() {

	}

	private static void addNames(List<PackageName> pList, TreeItem t) {
		for (TreeItem i : t.getNodes()) {

			if (i.isLeaf()) {
				if (i.isChecked()) {
					pList.add(CreateObjectTree.getPackageName(i));
				}
				continue;
			}
			addNames(pList, i);
		}
	}

	private static void createFile(IProject iP,
			CreateObjectTree.PackageObject packa, String path,
			String packageName, boolean spec) throws CoreException {
		String p = path + "/" + packageName + "_" //$NON-NLS-1$ //$NON-NLS-2$
				+ (spec ? "spec" : "body") + ".db2"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		IFile file = iP.getFile(p);
		InputStream source = new ByteArrayInputStream(spec ? packa.getSpec()
				.getBytes() : packa.getBody().getBytes());
		file.create(source, false, null);
	}

	/**
	 * Extracts PL/SQL packages checked by user and creates project files
	 * @param iP IProject
	 * @param jdbc Connection (jdbc)
	 * @param tree Contains package selection to retrieve
	 * @return number of packages successfully retrieved 
	 * @throws CoreException 
	 * @throws SQLException
	 */
	static int extract(IProject iP, Connection jdbc, FeatureTree tree)
			throws CoreException, SQLException {
		TreeItem root = tree.getRoot();
		List<PackageName> pList = new ArrayList<PackageName>();
		addNames(pList, root);
		if (pList.isEmpty()) {
			// user checked nothing
			PluginUtil.errorMessage(Messages.ExtractObjects_5);
			return 0;
		}
		int i = 0;
		for (PackageName pa : pList) {
			// extracts packages one after one
			String[] paths = { CommonProp.PLSQLPACKAGE, pa.getSchemaName() };
			ProjectSupport.createFolder(iP, paths);
			CreateObjectTree.PackageObject packa = CreateObjectTree.getPackage(
					jdbc, pa);
			String path = CommonProp.PLSQLPACKAGE + "/" + pa.getSchemaName(); //$NON-NLS-1$
			// package specification
			createFile(iP, packa, path, pa.getPackageName(), true);
			// package body
			createFile(iP, packa, path, pa.getPackageName(), false);
			i++;
		}
		return i;
	}
}
