/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.gwtmodel.table.view.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.gwtmodel.table.DataTreeLevel;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;

/**
 * @author hotel
 * 
 */
class PresentationTree implements IGwtTableView {

	private final IRowClick iClick;
	private final ScrollPanel sPanel = new ScrollPanel();
	private IGwtTableModel model = null;
	private WChoosedLine wChoosed;
	private final SingleSelectionModel<Integer> sel = new SingleSelectionModel<Integer>();
	private WSize lastMSize = null;
	private CellTree tree;

	private void setEmpty() {
		wChoosed = new WChoosedLine();
	}

	private class SelectionChange implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			Integer se = sel.getSelectedObject();
			if (se == null) {
				return;
			}
			if (lastMSize == null) {
				return;
			}
			wChoosed = new WChoosedLine(se, lastMSize);
			if (model.getIClicked() != null) {
				model.getIClicked().clicked(wChoosed);
			}
			if (iClick != null) {
				iClick.execute(false);
			}
			if (model.unSelectAtOnce()) {
				sel.setSelected(se, false);
			}
		}
	}

	private class MyHasCell<T> implements HasCell<Integer, T> {

		private final IVField fie;
		private final Cell ce;

		MyHasCell(IVField v) {
			PresentationCellFactory cFactory = new PresentationCellFactory(null);
			this.fie = v;
			// ce = cFactory.constructCell(v);
			ce = null;
		}

		@Override
		public Cell<T> getCell() {
			return ce;
		}

		@Override
		public FieldUpdater<Integer, T> getFieldUpdater() {
			return null;
		}

		@Override
		public T getValue(Integer object) {
			IVModelData v = model.get(object);
			return (T) FUtils.getValue(v, fie);
		}
	}

	private class SpaceCell extends AbstractCell<String> {

		SpaceCell() {
			super("click");
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				String value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value);
		}
	}

	/**
	 * It servers to purposes: 1) Space between columns 2) Supplies to "click"
	 * event to CompositeCell
	 * 
	 * @param <String>
	 */
	private class SpaceHasCell<String> implements HasCell<Integer, String> {

		@Override
		public Cell<String> getCell() {
			return (Cell<String>) new SpaceCell();
		}

		@Override
		public FieldUpdater<Integer, String> getFieldUpdater() {
			return null;
		}

		@Override
		public String getValue(Integer object) {
			return (String) "  ";
		}
	}

	private class CustomTreeModel implements TreeViewModel {

		private class MyCell extends CompositeCell<java.lang.Integer> {

			MyCell(List<HasCell<Integer, ?>> cellList) {
				// super("click");
				super(cellList);
			}

			@Override
			public void onBrowserEvent(Context context, Element parent,
					Integer value, NativeEvent event,
					ValueUpdater<Integer> valueUpdater) {
				super.onBrowserEvent(context, parent, value, event,
						valueUpdater);
				lastMSize = new WSize(event.getClientY(), event.getClientX(),
						0, 0);
			}
			// @Override
			// public void render(com.google.gwt.cell.client.Cell.Context
			// context,
			// Integer value, SafeHtmlBuilder sb) {
			// sb.append(value);
			// }
		}

		@Override
		public boolean isLeaf(Object value) {
			Integer i = (Integer) value;
			if (i.intValue() == IDataListType.ROOT) {
				return false;
			}
			int level = model.treeLevel(i);
			return DataTreeLevel.isLeaf(level);
		}

		@Override
		public <T> NodeInfo<?> getNodeInfo(T value) {
			Integer i = (Integer) value;
			int level;
			if (i.intValue() == IDataListType.ROOT) {
				level = -1;
				i = -1;
			} else {
				level = DataTreeLevel.getLevel(model.treeLevel(i));
			}
			ListDataProvider<java.lang.Integer> dataProvider = new ListDataProvider<java.lang.Integer>();
			for (i++; i < model.getSize(); i++) {
				int llevel = DataTreeLevel.getLevel(model.treeLevel(i));
				if (level + 1 == llevel) {
					dataProvider.getList().add(new java.lang.Integer(i));
				}
				if (level == llevel) {
					break;
				}
			}
			sel.addSelectionChangeHandler(new SelectionChange());
			List<HasCell<Integer, ?>> cellList = new ArrayList<HasCell<Integer, ?>>();

			VListHeaderContainer vo = model.getHeaderList();
			List<VListHeaderDesc> li = vo.getVisHeList();
			for (VListHeaderDesc he : li) {
				MyHasCell my = new MyHasCell(he.getFie());
				cellList.add(my);
				cellList.add(new SpaceHasCell());
			}

			if (iClick != null) {
				return new DefaultNodeInfo<java.lang.Integer>(dataProvider,
						new MyCell(cellList), sel, null);
			}
			return new DefaultNodeInfo<java.lang.Integer>(dataProvider,
					new MyCell(cellList));
		}
	}

	PresentationTree(IRowClick iClick) {
		this.iClick = iClick;
		setEmpty();
	}

	@Override
	public Widget getGWidget() {
		return sPanel;
	}

	@Override
	public void refresh() {
		if (model == null || !model.containsData()) {
			return;
		}
		TreeViewModel tmodel = new CustomTreeModel();
		tree = new CellTree(tmodel, new Integer(IDataListType.ROOT));
		sPanel.clear();
		sPanel.add(tree);
		if (model.getHeaderList().getTreeHeight() != null) {
			sPanel.setHeight(model.getHeaderList().getTreeHeight());
		}
	}

	@Override
	public WChoosedLine getClicked() {
		return wChoosed;
	}

	private class NodeLevel {

		NodeLevel(int level) {
			index = -1;
			this.level = level;
		}

		int index;
		final int level;
	}

	@Override
	public void setClicked(int clickedno, boolean whileFind) {
		Stack<NodeLevel> st = new Stack<NodeLevel>();
		st.push(new NodeLevel(-1));
		for (int i = 0; i <= clickedno; i++) {
			int treelevel = model.treeLevel(i);
			int level = DataTreeLevel.getLevel(treelevel);
			while (level <= st.peek().level) {
				st.pop();
			}
			st.peek().index++;
			if (!DataTreeLevel.isLeaf(treelevel)) {
				st.push(new NodeLevel(level));
			}
		}
		TreeNode no = tree.getRootTreeNode();
		for (int i = 0; i < st.size(); i++) {
			int index = st.get(i).index;
			if (index == -1) {
				break;
			}
			no = no.setChildOpen(index, true);
		}
		sel.setSelected(clickedno, true);
		// TODO: I do not know how to set the position of the row just selected
		// so set the upper-left row of the whole tree widget
		lastMSize = new WSize(tree.getAbsoluteLeft(), tree.getAbsoluteTop(), 0,
				0);
	}

	@Override
	public IGwtTableModel getViewModel() {
		return model;
	}

	@Override
	public void setModel(IGwtTableModel model) {
		this.model = model;
		refresh();
	}

	@Override
	public void setModifyRowStyle(IModifyRowStyle iMod) {
	}

	@Override
	public void setEditable(ChangeEditableRowsParam eParam) {
	}

	@Override
	public List<IGetSetVField> getVList(int rowno) {
		return null;
	}

	@Override
	public void removeSort() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSorted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPageSize(int pageSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public WSize getRowWidget(int rowno) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRow(int rownum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRow(int rownum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showInvalidate(int rowno, InvalidateFormContainer errContainer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNoWrap(boolean noWrap) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isNoWrap() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSortColumn(IVField col, boolean inc) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gwtmodel.table.view.table.IGwtTableView#refreshFooter(com.gwtmodel
	 * .table.IVModelData)
	 */
	@Override
	public void refreshFooter(IVModelData footer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshHeader() {
		// TODO Auto-generated method stub

	}

	@Override
	public void redrawRow(int rowno) {
		// TODO Auto-generated method stub

	}

}
