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
package org.com.powerbi.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PowerBI {

	private PowerBI() {

	}

	/**
	 * Java wrapper around Microsoft REST/API PowerBI interface. Implemented as
	 * static public functions. More details:
	 * https://msdn.microsoft.com/en-us/library/dn877544.aspx
	 */

	private static final String AUTHID = "https://login.windows.net/common/oauth2/token";
	private static final String RESOURCEID = "https://analysis.windows.net/powerbi/api";
	private static final String DATAID = "https://api.powerbi.com/v1.0/myorg/";

	/** PowerBI column types */
	public static final String INT64_TYPE = "Int64";
	public static final String STRING_TYPE = "string";
	public static final String DOUBLE_TYPE = "Double";
	public static final String BOOL_TYPE = "bool";
	public static final String DATETIME_TYPE = "DateTime";

	private static Logger logger = LoggerFactory.getLogger(PowerBI.class);

	/**
	 * Print response to log and output. Important: should be used only in case
	 * of failure.
	 * 
	 * @param response
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	private static void printResponse(HttpResponse response) throws UnsupportedOperationException, IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		String line = "";
		while ((line = rd.readLine()) != null) {
			logger.error(line);
			System.out.println(line);
		}

	}

	/**
	 * Check-in to Azzure and takes authentication token. Authentication token
	 * should be stored and used as a parameter in another PowerBI functions
	 * More details:
	 * https://msdn.microsoft.com/en-us/library/azure/dn798668.aspx
	 * 
	 * @param oauth_username
	 * @param oauth_password
	 * @param oauth_clientid
	 * @return Authentication token or null if authentication failed
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getAuthToken(String oauth_username, String oauth_password, String oauth_clientid)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(AUTHID);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", oauth_username));
		nvps.add(new BasicNameValuePair("password", oauth_password));
		nvps.add(new BasicNameValuePair("client_id", oauth_clientid));
		nvps.add(new BasicNameValuePair("grant_type", "password"));
		nvps.add(new BasicNameValuePair("resource", RESOURCEID));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		try {
			if (response2.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				printResponse(response2);
				return null;
			}
			HttpEntity entity2 = response2.getEntity();
			InputStreamReader r = new InputStreamReader(entity2.getContent());
			String token = ((JsonObject) new JsonParser().parse(r)).get("access_token").getAsString();
			EntityUtils.consume(entity2);
			return token;

		} finally {
			response2.close();
		}

	}

	/**
	 * Helper function. Executes GET method and returns JSON response object.
	 * 
	 * @param token
	 *            Authentication token
	 * @param what
	 *            Resource to get
	 * @return JSON object or null if failure
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static JsonObject getData(String token, String what) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet request = new HttpGet(DATAID + what);
		request.setHeader("Authorization", "Bearer " + token);

		HttpResponse response = httpclient.execute(request);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			printResponse(response);
			return null;
		}

		InputStreamReader r = new InputStreamReader(response.getEntity().getContent());
		JsonObject e = (JsonObject) new JsonParser().parse(r);
		return e;
	}

	/**
	 * Result element from table JSON response table
	 */
	public static class DataSetElem {
		private final String id;
		private final String name;

		DataSetElem(JsonElement e) {
			this.id = e.getAsJsonObject().get("id").getAsString();
			this.name = e.getAsJsonObject().get("name").getAsString();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

	}

	/**
	 * Input cell element to push data into PowerBI table. Construction method
	 * specifies the data type.
	 */
	public static class TableValue {

		private enum valueType {
			isnull, isint, isdouble, isstring, isbool, istime;
		};

		private final long intvalue;
		private final String stringvalue;
		private final double doublevalue;
		private final valueType vType;
		private final boolean boolvalue;
		private final Date timeValue;
		private final String timeS;

		/**
		 * Null value
		 */
		public TableValue() {
			vType = valueType.isnull;
			stringvalue = null;
			intvalue = 0;
			doublevalue = 0;
			boolvalue = false;
			timeValue = null;
			timeS = null;
		}

		/**
		 * Integer value
		 * 
		 * @param intvalue
		 */
		public TableValue(long intvalue) {
			this.intvalue = intvalue;
			vType = valueType.isint;
			stringvalue = null;
			doublevalue = 0;
			boolvalue = false;
			timeValue = null;
			timeS = null;
		}

		/**
		 * Double (float) value
		 * 
		 * @param doublevalue
		 */
		public TableValue(double doublevalue) {
			this.doublevalue = doublevalue;
			stringvalue = null;
			vType = valueType.isdouble;
			intvalue = 0;
			boolvalue = false;
			timeValue = null;
			timeS = null;
		}

		/**
		 * String value
		 * 
		 * @param stringvalue
		 */
		public TableValue(String stringvalue) {
			this.stringvalue = stringvalue;
			intvalue = 0;
			vType = valueType.isstring;
			doublevalue = 0;
			boolvalue = false;
			timeValue = null;
			timeS = null;
		}

		/**
		 * Boolean value
		 * 
		 * @param boolvalue
		 */
		public TableValue(boolean boolvalue) {
			stringvalue = null;
			intvalue = 0;
			vType = valueType.isbool;
			doublevalue = 0;
			this.boolvalue = boolvalue;
			timeValue = null;
			timeS = null;
		}

		/**
		 * Time stamp element, only one input value could be not null
		 * 
		 * @param timevalue
		 *            if not null then timestamp element as java Date type
		 *            transformed to appropriate string
		 * @param timeS
		 *            if not null then direct string representation of timestamp
		 *            will be pushed to PowerBI
		 */
		public TableValue(Date timevalue, String timeS) {
			stringvalue = null;
			intvalue = 0;
			vType = valueType.istime;
			doublevalue = 0;
			boolvalue = false;
			this.timeValue = timevalue;
			this.timeS = timeS;
		}

		long getIntvalue() {
			return intvalue;
		}

		String getStringvalue() {
			return stringvalue;
		}

		boolean isString() {
			return vType == valueType.isstring;
		}

		boolean isNullvalue() {
			return vType == valueType.isnull;
		}

		boolean isDouble() {
			return vType == valueType.isdouble;
		}

		double getDoublevalue() {
			return doublevalue;
		}

		boolean isBool() {
			return vType == valueType.isbool;
		}

		boolean getBoolvalue() {
			return boolvalue;
		}

		boolean isTime() {
			return vType == valueType.istime;
		}

		String getTimeValue() {
			if (timeS != null)
				return timeS;
			SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
			return ft.format(timeValue);
		}

	}

	/**
	 * Get list of datasets defined
	 * https://msdn.microsoft.com/en-us/library/mt203567.aspx
	 * 
	 * @param token
	 *            Authentication token
	 * @return List of DataSetElem
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static List<DataSetElem> getDataSets(String token) throws ClientProtocolException, IOException {
		JsonObject o = getData(token, "datasets");
		logger.debug(o.toString());
		List<DataSetElem> l = new ArrayList<DataSetElem>();
		Iterator<JsonElement> i = o.getAsJsonArray("value").iterator();
		while (i.hasNext()) {
			JsonElement e = i.next();
			l.add(new DataSetElem(e));
		}
		return l;
	}

	/**
	 * Gets dataset id
	 * 
	 * @param token
	 *            Authentication token
	 * @param datasetName
	 *            Dataset name
	 * @return dataset id or null if dataset does not exist
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getDataSetId(String token, String datasetName) throws ClientProtocolException, IOException {
		List<DataSetElem> l = getDataSets(token);
		for (DataSetElem e : l)
			if (datasetName.equals(e.getName()))
				return e.getId();
		return null;
	}

	/**
	 * Get list of tables https://msdn.microsoft.com/en-US/library/mt203556.aspx
	 * 
	 * @param token
	 *            Authentication token
	 * @param datasetId
	 *            Value returned by getDataSetId
	 * @return List of tables names
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static List<String> getDataSetTables(String token, String datasetId)
			throws ClientProtocolException, IOException {
		JsonObject o = getData(token, "datasets/" + datasetId + "/tables");
		List<String> l = new ArrayList<String>();
		Iterator<JsonElement> i = o.getAsJsonArray("value").iterator();
		while (i.hasNext())
			l.add(i.next().getAsJsonObject().get("name").getAsString());
		return l;
	};

	/**
	 * Executes POST method and use JSON object as POST method content
	 * 
	 * @param token
	 *            Authentication token
	 * @param url
	 *            Resource URL
	 * @param o
	 *            Input JSON object
	 * @param codeExpected
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static void postJSON(String token, String url, JsonObject o, int codeExpected)
			throws ClientProtocolException, IOException {
		logger.debug(o.toString());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(DATAID + url);

		StringEntity se = new StringEntity(o.toString());
		se.setContentType("application/json");
		httpPost.setEntity(se);
		httpPost.setHeader("Authorization", "Bearer " + token);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setEntity(se);

		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		logger.debug(response2.toString());
		if (response2.getStatusLine().getStatusCode() != codeExpected)
			printResponse(response2);

	}

	/**
	 * Create a JSON object representing table schema definition Table JSON
	 * format: https://msdn.microsoft.com/en-US/library/mt203551.aspx
	 * 
	 * @param tablename
	 *            Table name
	 * @param tableschema
	 *            Table schema definition
	 * @return JSON object
	 */
	private static JsonObject createDataSchemaObject(String tablename, Map<String, String> tableschema) {
		JsonArray list = new JsonArray();
		for (Entry<String, String> e : tableschema.entrySet()) {
			JsonObject obj = new JsonObject();
			obj.addProperty("name", e.getKey());
			obj.addProperty("dataType", e.getValue());
			list.add(obj);
		}

		JsonObject table = new JsonObject();
		table.addProperty("name", tablename);
		table.add("columns", list);
		return table;
	}

	/**
	 * Creates data set and a table corresponding
	 * https://msdn.microsoft.com/en-US/library/mt203562.aspx
	 * 
	 * @param token
	 *            Authentication token
	 * @param datasetName
	 *            Data set name
	 * @param tableName
	 *            Table name
	 * @param tableschema
	 *            Table schema definition as a map, key : column name and value:
	 *            column data type
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void createDataSet(String token, String datasetName, String tableName,
			Map<String, String> tableschema) throws ClientProtocolException, IOException {

		JsonArray tables = new JsonArray();
		tables.add(createDataSchemaObject(tableName, tableschema));

		JsonObject dataset = new JsonObject();
		dataset.addProperty("name", datasetName);
		dataset.add("tables", tables);
		// object created
		postJSON(token, "/datasets/", dataset, HttpStatus.SC_CREATED);
	}

	/**
	 * Pushes data to PowerBI table.
	 * https://msdn.microsoft.com/en-us/library/mt203561.aspx
	 * 
	 * @param token
	 *            Authentication token
	 * @param datasetId
	 *            Data set id
	 * @param tablename
	 *            Table name
	 * @param rlist
	 *            List of data to be pushed. List of maps. One map contains
	 *            single row.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void addTableRows(String token, String datasetId, String tablename,
			List<Map<String, TableValue>> rlist) throws ClientProtocolException, IOException {
		// create JSON object
		JsonArray rowlist = new JsonArray();
		for (Map<String, TableValue> row : rlist) {
			JsonObject objrow = new JsonObject();
			for (Entry<String, TableValue> r : row.entrySet()) {
				TableValue v = r.getValue();
				String id = r.getKey();
				if (v.isNullvalue())
					objrow.add(id, JsonNull.INSTANCE);
				else if (v.isString())
					objrow.addProperty(id, v.getStringvalue());
				else if (v.isDouble())
					objrow.addProperty(id, v.getDoublevalue());
				else if (v.isBool())
					objrow.addProperty(id, Boolean.toString(v.getBoolvalue()));
				else if (v.isTime())
					objrow.addProperty(id, v.getTimeValue());
				else
					objrow.addProperty(id, v.getIntvalue());
			}
			rowlist.add(objrow);
		}
		JsonObject obj = new JsonObject();
		obj.add("rows", rowlist);
		// JSON created
		logger.debug(rowlist.toString());
		postJSON(token, "/datasets/" + datasetId + "/tables/" + tablename + "/rows", obj, HttpStatus.SC_OK);
	}

	/**
	 * Removes all rows from table
	 * https://msdn.microsoft.com/en-us/library/mt238041.aspx
	 * 
	 * @param token
	 *            Authentication token
	 * @param datasetId
	 *            Data set id
	 * @param tablename
	 *            Table name
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void clearTable(String token, String datasetId, String tablename)
			throws ClientProtocolException, IOException {
		// HttpDelete
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpDelete request = new HttpDelete(DATAID + "/datasets/" + datasetId + "/tables/" + tablename + "/rows");
		request.setHeader("Authorization", "Bearer " + token);

		HttpResponse response = httpclient.execute(request);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			printResponse(response);
	}

	/**
	 * Modifies table schema
	 * https://msdn.microsoft.com/en-us/library/mt203560.aspx Important: this
	 * REST API seems not working
	 * 
	 * @param token
	 *            Authentication token
	 * @param datasetId
	 *            Data set id
	 * @param tablename
	 *            Table name
	 * @param tableschema
	 *            New table schema
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	public static void updateTableSchema(String token, String datasetId, String tablename,
			Map<String, String> tableschema) throws ClientProtocolException, IOException {
		JsonObject table = createDataSchemaObject(tablename, tableschema);
		postJSON(token, "/datasets/" + datasetId + "/tables/" + tablename, table, HttpStatus.SC_CREATED);
	}

	/**
	 * Check if data set and table exist. If not then creates data set and table
	 * 
	 * @param token
	 *            Authentication token
	 * @param datasetName
	 *            Data set name
	 * @param tablename
	 *            Table name
	 * @param tableschema
	 *            Table schema
	 * @return Data set id, existing or just created.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	public static String checkTableDataSet(String token, String datasetName, String tablename,
			Map<String, String> tableschema) throws ClientProtocolException, IOException {
		String datasetId = getDataSetId(token, datasetName);
		if (datasetId == null) {
			createDataSet(token, datasetName, tablename, tableschema);
			return getDataSetId(token, datasetName);
		}
		List<String> list = getDataSetTables(token, datasetId);
		for (String s : list)
			if (s.equals(tablename))
				return datasetId;
		createDataSet(token, datasetName, tablename, tableschema);
		return getDataSetId(token, datasetName);
	}

}
