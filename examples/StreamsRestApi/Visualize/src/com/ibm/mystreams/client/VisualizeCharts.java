/*
 * Copyright 2015 stanislawbartkowski@gmail.com
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
package com.ibm.mystreams.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

/**
 * Utility class creating three types of charts used in the visualization.
 * 
 * @author sbartkowski
 * 
 */
class VisualizeCharts {

	interface IChartW {

		Widget getW();

		void refresh(int excellent, int rating0);
	}

	private static DataTable createT() {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, M.L().typeLabel());
		data.addColumn(ColumnType.NUMBER, M.L().ratingLabel());
		data.addRows(2);
		data.setValue(0, 0, M.L().excellentLabel());
		data.setValue(1, 0, M.L().rating0Label());
		return data;
	}

	private static abstract class BasicChart implements IChartW {
		protected final DataTable data = createT();
		protected final VerticalPanel result = new VerticalPanel();
		private final Label status = new Label();

		BasicChart() {
			/* create pie chart */
			result.add(status);
		}

		abstract void redraw();

		@Override
		public void refresh(int excellent, int rating0) {
			data.setValue(0, 1, excellent);
			data.setValue(1, 1, rating0);
			redraw();
			Date d = new Date();
			String s = DateTimeFormat.getLongTimeFormat().format(d);
			status.setText(s);
		}

		@Override
		public Widget getW() {
			return result;
		}
	}

	private static class ChartW extends BasicChart {

		private final PieChart viz;
		private final PieChart.PieOptions options = PieChart.createPieOptions();

		ChartW() {
			super();
			/* create pie chart */
			options.setWidth(400);
			options.setHeight(240);
			options.set3D(true);
			options.setTitle(M.L().actualRatingTitle());
			options.setLegend(LegendPosition.LEFT);

			viz = new PieChart(data, options);
			Label onMouseOverAndOutStatus = new Label();
			result.add(viz);
			result.add(onMouseOverAndOutStatus);
		}

		@Override
		void redraw() {
			viz.draw(data, options);
		}
	}

	private static class ChartB extends BasicChart {

		private final BarChart viz;
		private final Options options = Options.create();

		ChartB() {
			super();
			/* create pie chart */
			Options options = Options.create();
			options.setHeight(240);
			options.setTitle(M.L().actualRatingTitle());
			options.setWidth(400);
			AxisOptions vAxisOptions = AxisOptions.create();
			vAxisOptions.setMinValue(0);
			vAxisOptions.setMaxValue(2000);
			options.setVAxisOptions(vAxisOptions);

			viz = new BarChart(data, options);
			Label onMouseOverAndOutStatus = new Label();
			result.add(viz);
			result.add(onMouseOverAndOutStatus);
		}

		@Override
		void redraw() {
			viz.draw(data, options);
		}
	}

	static IChartW create() {
		return new ChartW();
	}

	static IChartW createB() {
		return new ChartB();
	}

	private static class LineChartW implements IChartW {

		private final DataTable data;
		private final Options options = Options.create();
		private final VerticalPanel result = new VerticalPanel();
		private final Label status = new Label();
		private final LineChart viz;

		private static final int NOTIME = 4;

		LineChartW() {
			data = DataTable.create();
			data.addColumn(ColumnType.NUMBER, M.L().timeLabel());
			data.addColumn(ColumnType.NUMBER, M.L().excellentLabel());
			data.addColumn(ColumnType.NUMBER, M.L().rating0Label());
			data.addRows(NOTIME);
			for (int i = 0; i < NOTIME; i++) {
				data.setValue(i, 0, i - NOTIME + 1);
				data.setValueNull(i, 1);
				data.setValueNull(i, 2);
			}
			options.setHeight(240);
			options.setTitle(M.L().actualRatingTitle());
			options.setWidth(450);
			options.setInterpolateNulls(true);
			viz = new LineChart(data, options);
			AxisOptions vAxisOptions = AxisOptions.create();
			vAxisOptions.setMinValue(0);
			vAxisOptions.setMaxValue(2000);
			vAxisOptions.setDirection(1);
			options.setVAxisOptions(vAxisOptions);
			AxisOptions hAxisOptions = AxisOptions.create();
			hAxisOptions.setDirection(-1);
			options.setVAxisOptions(vAxisOptions);
			options.setHAxisOptions(hAxisOptions);
			result.add(status);
			result.add(viz);
			Label onMouseOverAndOutStatus = new Label();
			result.add(onMouseOverAndOutStatus);
		}

		@Override
		public Widget getW() {
			return result;
		}

		private void moveValue(int i, int pos) {
			if (data.isValueNull(i, pos))
				data.setValueNull(i + 1, pos);
			else
				data.setValue(i + 1, pos, data.getValueInt(i, pos));

		}

		@Override
		public void refresh(int excellent, int rating0) {
			for (int i = 0; i < NOTIME - 1; i++) {
				moveValue(i, 1);
				moveValue(i, 2);
			}
			data.setValue(0, 1, excellent);
			data.setValue(0, 2, rating0);
			viz.draw(data);
			Date d = new Date();
			String s = DateTimeFormat.getLongTimeFormat().format(d);
			status.setText(s);
		}

	}

	static IChartW createL() {
		return new LineChartW();
	}

}
