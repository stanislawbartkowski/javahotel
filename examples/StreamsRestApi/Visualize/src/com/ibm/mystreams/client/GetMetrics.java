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

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.ibm.mystreams.client.util.CommonCallBack;
import com.ibm.mystreams.client.util.Util;
import com.ibm.mystreams.shared.ConnectionData;

/**
 * 
 * @author sbartkowski Utility class returning (through callback function) next
 *         pair of metrics.
 */

class GetMetrics {

	private GetMetrics() {

	}

	/**
	 * Callback function
	 * 
	 * @author sbartkowski
	 * 
	 */
	interface IGetMetrics {
		void next(int excellent, int rating0);
	}

	private static class GetM extends CommonCallBack {

		private final IGetMetrics iGet;

		GetM(IGetMetrics iGet) {
			this.iGet = iGet;
		}

		@Override
		public void doResult(String jSon) {
			// analyze JSon string and extract metrics values
			JSONArray jsonArray = Util.getArray(jSon, "metrics");
			int excelent = -1, rating0 = -1;
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject instance = jsonArray.get(i).isObject();
				JSONString instanceName = instance.get("name").isString();
				if (instanceName.stringValue().equals("excelent")) {
					excelent = (int) instance.get("value").isNumber()
							.doubleValue();
				}
				if (instanceName.stringValue().equals("rating0")) {
					rating0 = (int) instance.get("value").isNumber()
							.doubleValue();
				}
			}
			// launch call back function
			iGet.next(excelent, rating0);
		}

	}

	/**
	 * Provides next set of metrics
	 * 
	 * @param data
	 *            connection data
	 * @param url
	 *            REST url to obtain metrics
	 * @param i
	 *            callback function
	 */
	static void getMetric(ConnectionData data, String url, IGetMetrics i) {
		M.getS().callRest(data, url, new GetM(i));
	}

}
