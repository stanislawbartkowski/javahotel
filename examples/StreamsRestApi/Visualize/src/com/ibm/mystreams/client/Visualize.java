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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.google.gwt.visualization.client.visualizations.GeoMap;
import com.google.gwt.visualization.client.visualizations.ImageAreaChart;
import com.google.gwt.visualization.client.visualizations.ImageBarChart;
import com.google.gwt.visualization.client.visualizations.ImageChart;
import com.google.gwt.visualization.client.visualizations.ImageLineChart;
import com.google.gwt.visualization.client.visualizations.ImagePieChart;
import com.google.gwt.visualization.client.visualizations.ImageSparklineChart;
import com.google.gwt.visualization.client.visualizations.IntensityMap;
import com.google.gwt.visualization.client.visualizations.MapVisualization;
import com.google.gwt.visualization.client.visualizations.MotionChart;
import com.google.gwt.visualization.client.visualizations.OrgChart;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.ibm.mystreams.client.GetMetricsUrl.IGetMetricsUrl;
import com.ibm.mystreams.client.IMainFrame.POS;
import com.ibm.mystreams.client.VisualizeCharts.IChartW;
import com.ibm.mystreams.client.connections.ConnectionsList;
import com.ibm.mystreams.client.mainframe.IClickGoButton;
import com.ibm.mystreams.client.mainframe.IClickNewConnection;
import com.ibm.mystreams.client.mainframe.MainFrame;
import com.ibm.mystreams.client.modal.ModalDialog;
import com.ibm.mystreams.client.modal.ModalDialog.IModalDialog;
import com.ibm.mystreams.shared.ConnectionData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Visualize implements EntryPoint {

	private Timer t;

	private class NewConnection implements IClickNewConnection {

		@Override
		public void click() {
			IModalDialog iDial = ModalDialog.construct();
			ConnectionsList w = new ConnectionsList(iDial);
			iDial.show(w, M.L().listOfConnectionLabel());
		}
	}

	private class GetValues implements GetMetrics.IGetMetrics {

		private final IChartW chartW;
		private final IChartW chartB;
		private final IChartW chartL;

		GetValues(IChartW chartW, IChartW chartB, IChartW chartL) {
			this.chartW = chartW;
			this.chartB = chartB;
			this.chartL = chartL;
		}

		@Override
		public void next(int excellent, int rating0) {
			chartW.refresh(excellent, rating0);
			chartB.refresh(excellent, rating0);
			chartL.refresh(excellent, rating0);
		}

	}

	private class GetMetricsUrlR implements IGetMetricsUrl {

		private final ConnectionData data;
		private final IChartW chartW;
		private final IChartW chartB;
		private final IChartW chartL;

		GetMetricsUrlR(ConnectionData data, IChartW chartW, IChartW chartB,
				IChartW chartL) {
			this.data = data;
			this.chartW = chartW;
			this.chartB = chartB;
			this.chartL = chartL;
		}

		@Override
		public void setS(final String url) {
			GetMetrics.getMetric(data, url, new GetValues(chartW, chartB,
					chartL));
			t = new Timer() {

				@Override
				public void run() {
					GetMetrics.getMetric(data, url, new GetValues(chartW,
							chartB, chartL));
				}

			};
			t.scheduleRepeating(15000); // every 15 seconds
		}

	}

	private class GoAction implements IClickGoButton {

		@Override
		public void go(ConnectionData con) {

			IChartW chartW = VisualizeCharts.create();
			IChartW chartB = VisualizeCharts.createB();
			IChartW chartL = VisualizeCharts.createL();
			GetMetricsUrl.getMetrictRest(con, new GetMetricsUrlR(con, chartW,
					chartB, chartL));
			M.getiFrame().setCentre(POS.UPLEFT, chartW.getW());
			M.getiFrame().setCentre(POS.UPRIGHT, chartB.getW());
			M.getiFrame().setCentre(POS.DOWN, chartL.getW());
		}

	}

	private void startV() {
		VisualizationUtils.loadVisualizationApi(
				new Runnable() {

					@Override
					public void run() {
						RootPanel.get().add(
								new MainFrame(new NewConnection(),
										new GoAction()));
					}
				}, CoreChart.PACKAGE, Gauge.PACKAGE, GeoMap.PACKAGE,
				ImageChart.PACKAGE, ImageLineChart.PACKAGE,
				ImageAreaChart.PACKAGE, ImageBarChart.PACKAGE,
				ImagePieChart.PACKAGE, IntensityMap.PACKAGE,
				MapVisualization.PACKAGE, MotionChart.PACKAGE,
				OrgChart.PACKAGE, Table.PACKAGE, ImageSparklineChart.PACKAGE);
	}

	@Override
	public void onModuleLoad() {
		startV();
	}
}
