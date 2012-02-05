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

package org.feature.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Model (data structure) for keeping tree of features.
 * 
 * @author sbartkowski
 * 
 */

public class FeatureTree {

	/** Root of the tree. */
	private final TreeItem root;

	public TreeItem getRoot() {
		return root;
	}

	public FeatureTree(TreeItem root) {
		this.root = root;
	}

	/**
	 * Tree item.
	 * 
	 * @author sbartkowski
	 * 
	 */
	public static class TreeItem {
		/** Identifier. */
		private final String id;
		/** Name displayed on the screen. */
		private final String displayName;
		/** Item (node) value : boolean here. */
		private boolean checked;

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public String getId() {
			return id;
		}

		public String getDisplayName() {
			return displayName;
		}

		public List<TreeItem> getNodes() {
			return nodes;
		}

		/** List of nodes, empty if leaf. */
		private final List<TreeItem> nodes;

		private TreeItem parent;

		public TreeItem getParent() {
			return parent;
		}

		private void setParent(TreeItem parent) {
			this.parent = parent;
		}

		public boolean isLeaf() {
			return nodes.isEmpty();
		}

		public TreeItem(String id, String displayName, boolean checked) {
			this.id = id;
			this.displayName = displayName;
			this.checked = checked;
			nodes = new ArrayList<TreeItem>();
		}

		public void AddChild(TreeItem child) {
			this.nodes.add(child);
			child.setParent(this);
		}

		public TreeItem(String displayName, boolean checked) {
			this(displayName, displayName, checked);
		}
	}

}
