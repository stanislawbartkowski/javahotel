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

import java.util.List;

import com.google.gwt.user.client.Window;
import com.ibm.mystreams.client.database.IDatabase;
import com.ibm.mystreams.client.util.CommonCallBack;
import com.ibm.mystreams.client.util.Util;
import com.ibm.mystreams.shared.ConnectionData;

/**
 * Calculates REST url used for obtaining metrics 1) First find an instance 2)
 * Secondly find a job
 * 
 * @author sbartkowski
 * 
 */
class GetMetricsUrl {

	private GetMetricsUrl() {

	}

	/**
	 * Callback function
	 * 
	 * @author sbartkowski
	 * 
	 */
	interface IGetMetricsUrl {
		void setS(String url);
	}

	private static class GetJobs extends CommonCallBack {

		private final String urlJobs;
		private final IGetMetricsUrl iGet;

		GetJobs(String urlJobs, IGetMetricsUrl iGet) {
			this.urlJobs = urlJobs;
			this.iGet = iGet;
		}

		@Override
		public void doResult(String jSon) {
			// Find a job
			String id = Util.getJobId(jSon, "TestFlow");
			if (id == null) {
				Window.alert(urlJobs + M.L().cannotFindJob());
				return;
			}
			String u = urlJobs + "/" + id + "/operators/MetricsSink_7/metrics";
			// REST url found - pass to callback function
			iGet.setS(u);
		}

	}

	/**
	 * Does two things: 1) Find and set a list of instances for given connection
	 * 2) Select an instance (if more then one)
	 * 
	 * @author sbartkowski
	 * 
	 */
	private static class GetInstances extends CommonCallBack {

		private final ConnectionData con;
		private final IGetMetricsUrl iGet;

		GetInstances(ConnectionData con, IGetMetricsUrl iGet) {
			this.con = con;
			this.iGet = iGet;
		}

		@Override
		public void doResult(String jSon) {
			IDatabase iBase = M.getIbase();
			List<String> instances = Util.getListOfInstances(jSon);
			// Set list of instance in the database
			Util.setInstances(iBase.toS(con), instances);
			// Refresh list of instances in the combobox
			M.getiFrame().refreshListOfInstances();
			String s = Util.getCurrentInstance(iBase.toS(con));
			String insta = null;
			// Search if there exist one selected
			if (s != null)
				for (String i : instances)
					if (s.equals(i))
						insta = s;
			// if nothing selected then pick up the first one
			if (insta == null)
				insta = instances.get(0);
			String url = "instances/" + insta + "/jobs";
			// now ready to find a job
			M.getS().callRest(con, url, new GetJobs(url, iGet));
		}
	}

	static void getMetrictRest(ConnectionData con, IGetMetricsUrl i) {
		M.getS().callRest(con, "instances", new GetInstances(con, i));

	}

}
