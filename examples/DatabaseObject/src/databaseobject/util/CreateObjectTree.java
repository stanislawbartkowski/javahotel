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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.feature.tree.FeatureTree;
import org.feature.tree.FeatureTree.TreeItem;

import databaseobject.messages.Messages;

/**
 * Static class with some common properties and methods
 * 
 * @author sbartkowski
 * 
 */

public class CreateObjectTree {

	/** Statement enumerating all PL/SQL packages. */
	private static final String SELECTPACKAGESTATEMEN = "SELECT MODULESCHEMA, MODULENAME FROM SYSCAT.MODULES WHERE MODULETYPE = 'P' ORDER BY MODULESCHEMA"; //$NON-NLS-1$
	/** Statement retrieving package specification. */
	private static final String SELECTPACKAGESPEC = "SELECT TEXT FROM DBA_SOURCE WHERE SCHEMA = ? AND TYPE = 'PACKAGE' AND NAME = ?"; //$NON-NLS-1$
	/** Statement retrieving package body. */
	private static final String SELECTPACKAGEBODY = "SELECT TEXT FROM DBA_SOURCE WHERE SCHEMA = ? AND TYPE = 'PACKAGE BODY' AND NAME = ?"; //$NON-NLS-1$

	private CreateObjectTree() {
	}

	private static final String ROOT = "root"; //$NON-NLS-1$
	private static final String SCHEMA = "schema"; //$NON-NLS-1$
	private static final String PACKAGE = "package"; //$NON-NLS-1$

	/**
	 * Creates package as feature tree (root -> schema -> package)
	 * 
	 * @param jdbc
	 *            Connection
	 * @return FeatureTree data structure
	 * @throws SQLException
	 */
	public static FeatureTree createPLSQLTree(Connection jdbc)
			throws SQLException {

		TreeItem root = new TreeItem(ROOT, Messages.CreateObjectTree_6, false);
		Statement stmt = jdbc.createStatement();
		// get all PL/SQL packages
		ResultSet res = stmt.executeQuery(SELECTPACKAGESTATEMEN);
		String aSchema = null;
		TreeItem schema = null;
		while (res.next()) {
			// retrieves one by one and creates FeatureTree
			String mod = res.getString("MODULESCHEMA"); //$NON-NLS-1$
			String name = res.getString("MODULENAME"); //$NON-NLS-1$
			if (aSchema == null || !aSchema.equals(mod)) {
				schema = new TreeItem(SCHEMA, mod, false);
				aSchema = mod;
				root.AddChild(schema);
			}
			TreeItem packName = new TreeItem(PACKAGE, name, false);
			schema.AddChild(packName);
		}
		stmt.close();
		return new FeatureTree(root);

	}

	private static String getText(Connection jdbc, String schema, String name,
			boolean body) throws SQLException {
		PreparedStatement stmt = jdbc.prepareStatement(body ? SELECTPACKAGEBODY
				: SELECTPACKAGESPEC);
		stmt.setString(1, schema);
		stmt.setString(2, name);
		ResultSet res = stmt.executeQuery();
		if (!res.next()) {
			stmt.close();
			return ""; //$NON-NLS-1$
		}
		String n = res.getString(1);
		stmt.close();
		return n;
	}

	/**
	 * Container class with package schema and package name
	 * 
	 * @author sbartkowski
	 * 
	 */
	public static class PackageName {
		private final String schemaName;
		private final String packageName;

		private PackageName(String schemaName, String packageName) {
			super();
			this.schemaName = schemaName;
			this.packageName = packageName;
		}

		public String getSchemaName() {
			return schemaName;
		}

		public String getPackageName() {
			return packageName;
		}
	}

	/**
	 * Get package name from TreeItem
	 * 
	 * @param t
	 *            TreeItem
	 * @return PackageName class with the result
	 */
	public static PackageName getPackageName(TreeItem t) {
		TreeItem schema = t.getParent();
		String schemaName = schema.getDisplayName();
		String pack = t.getDisplayName();
		return new PackageName(schemaName, pack);
	}

	/**
	 * Container class with package content, specification and body
	 * 
	 * @author sbartkowski
	 * 
	 */
	public static class PackageObject {
		private final String spec;
		private final String body;

		PackageObject(String spec, String body) {
			this.spec = spec;
			this.body = body;
		}

		public String getSpec() {
			return spec;
		}

		public String getBody() {
			return body;
		}

	}

	/**
	 * Retrieves package content from database
	 * 
	 * @param jdbc
	 *            Conection
	 * @param pa
	 *            Packagname
	 * @return PackageObject (body and specification)
	 * @throws SQLException
	 */
	public static PackageObject getPackage(Connection jdbc, PackageName pa)
			throws SQLException {
		String spec = getText(jdbc, pa.getSchemaName(), pa.getPackageName(),
				false);
		String body = getText(jdbc, pa.getSchemaName(), pa.getPackageName(),
				true);
		return new PackageObject(spec, body);
	}

	/**
	 * Retrieves package content from database (the same like above but
	 * different parameter)
	 * 
	 * @param jdbc
	 *            Connection
	 * @param t
	 *            TreeItem describing package name
	 * @return PackageObject
	 * @throws SQLException
	 */
	public static PackageObject getPackage(Connection jdbc, TreeItem t)
			throws SQLException {
		if (t.getId().equals(PACKAGE)) {
			PackageName pa = getPackageName(t);
			return getPackage(jdbc, pa);
		}
		return null;
	}

}
