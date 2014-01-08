/*
 * Copyright 2014 stanislawbartkowski@gmail.com
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
 * Used only for test purpose (sample copied from Chart Visualization package)
 * 
 * @author sbartkowski
 * 
 */
class SamplePie {

	private static DataTable getDailyActivities() {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Hours per Day");
		data.addRows(5);
		data.setValue(0, 0, "Work");
		data.setValue(0, 1, 11);
		data.setValue(1, 0, "Eat");
		data.setValue(1, 1, 2);
		data.setValue(2, 0, "Commute");
		data.setValue(2, 1, 2);
		data.setValue(3, 0, "Watch TV");
		data.setValue(3, 1, 2);
		data.setValue(4, 0, "Sleep");
		data.setValue(4, 1, 7);
		return data;
	}

	static Widget getWidget() {
		VerticalPanel result = new VerticalPanel();

		/* create a datatable */
		DataTable data = getDailyActivities();

		/* create pie chart */
		PieChart.PieOptions options = PieChart.createPieOptions();
		options.setWidth(400);
		options.setHeight(240);
		options.set3D(true);
		options.setTitle("My Daily Activities");
		options.setLegend(LegendPosition.LEFT);

		PieChart viz = new PieChart(data, options);
		Label status = new Label();
		Label onMouseOverAndOutStatus = new Label();
		result.add(status);
		result.add(viz);
		result.add(onMouseOverAndOutStatus);
		return result;
	}

	static Widget getWidgetB() {
		VerticalPanel result = new VerticalPanel();
		Options options = Options.create();
		options.setHeight(240);
		options.setTitle("Company Performance");
		options.setWidth(400);
		AxisOptions vAxisOptions = AxisOptions.create();
		vAxisOptions.setMinValue(0);
		vAxisOptions.setMaxValue(2000);
		options.setVAxisOptions(vAxisOptions);

		DataTable data = getDailyActivities();
		BarChart viz = new BarChart(data, options);

		Label status = new Label();
		Label onMouseOverAndOutStatus = new Label();
		// viz.addSelectHandler(new SelectionDemo(viz, status));
		// viz.addReadyHandler(new ReadyDemo(status));
		// viz.addOnMouseOverHandler(new
		// OnMouseOverDemo(onMouseOverAndOutStatus));
		// viz.addOnMouseOutHandler(new
		// OnMouseOutDemo(onMouseOverAndOutStatus));
		result.add(status);
		result.add(viz);
		result.add(onMouseOverAndOutStatus);
		return result;
	}

	static DataTable getCompanyPerformanceWithNulls() {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Year");
		data.addColumn(ColumnType.NUMBER, "Sales");
		data.addColumn(ColumnType.NUMBER, "Expenses");
		data.addRows(4);
		data.setValue(0, 0, "2004");
		data.setValue(0, 1, 1000);
		data.setValue(0, 2, 400);
		data.setValue(1, 0, "2005");
		data.setValue(1, 1, 1170);
		data.setValue(1, 2, 460);
		data.setValue(2, 0, "2006");
		data.setValueNull(2, 1);
		data.setValueNull(2, 2);
		data.setValue(3, 0, "2007");
		data.setValue(3, 1, 1030);
		data.setValue(3, 2, 540);
		return data;
	}

	static Widget getWidgetL() {
		VerticalPanel result = new VerticalPanel();

		Options options = Options.create();
		options.setHeight(240);
		options.setTitle("Company Performance");
		options.setWidth(400);
		options.setInterpolateNulls(true);
		AxisOptions vAxisOptions = AxisOptions.create();
		vAxisOptions.setMinValue(0);
		vAxisOptions.setMaxValue(2000);
		options.setVAxisOptions(vAxisOptions);

		DataTable data = getCompanyPerformanceWithNulls();
		LineChart viz = new LineChart(data, options);

		Label status = new Label();
		Label onMouseOverAndOutStatus = new Label();
		// viz.addSelectHandler(new SelectionDemo(viz, status));
		// viz.addReadyHandler(new ReadyDemo(status));
		// viz.addOnMouseOverHandler(new
		// OnMouseOverDemo(onMouseOverAndOutStatus));
		// viz.addOnMouseOutHandler(new
		// OnMouseOutDemo(onMouseOverAndOutStatus));
		result.add(status);
		result.add(viz);
		result.add(onMouseOverAndOutStatus);
		return result;
	}

}
