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
package org.transform.jpk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author sbartkowski
 * 
 *         Class to conduct data uploading and receiving the final status
 *
 */

public class UPLOAD {

	private static boolean post_blob(INIT i) throws IOException {
		LOG.log("Teraz wysyłam zakodowany plik");
		URL url = new URL(i.getFileURL());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		// conn.setRequestMethod("PUT");
		conn.setRequestMethod(i.getMethod());
		byte[] data = UTIL.getZipAesData();
		conn.setRequestProperty("Content-Length", "" + data.length);
		// conn.setRequestProperty("x-ms-blob-type", "BlockBlob");
		for (Map.Entry<String, String> ma : i.getHeader().entrySet())
			conn.setRequestProperty(ma.getKey(), ma.getValue());
		conn.getOutputStream().write(data);
		return getResult(conn, new StringBuffer());
	}

	static class INIT {

		JsonObject jsonObject;

		private String getPar(String mName) {
			JsonArray a = jsonObject.get("RequestToUploadFileList").getAsJsonArray();
			JsonObject o = a.get(0).getAsJsonObject();
			return o.get(mName).getAsString();
		}

		String getFileURL() {
			return getPar("Url");
		}

		String getMethod() {
			return getPar("Method");
		}

		String getReference() {
			return jsonObject.get("ReferenceNumber").getAsString();
		}

		Map<String, String> getHeader() {
			Map<String, String> h = new HashMap<String, String>();
			JsonArray a = jsonObject.get("RequestToUploadFileList").getAsJsonArray();
			JsonObject o = a.get(0).getAsJsonObject();
			JsonArray hA = o.get("HeaderList").getAsJsonArray();
			if (hA != null)
				for (int i = 0; i < hA.size(); i++) {
					JsonObject elem = hA.get(i).getAsJsonObject();
					String key = elem.get("Key").getAsString();
					String val = elem.get("Value").getAsString();
					h.put(key, val);
				}
			return h;
		}
	}

	private static String createJSON(INIT i) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("ReferenceNumber", i.getReference());
		JsonArray a = new JsonArray();
		a.add(PROP.getZipAesFile().getName());
		jsonObject.add("AzureBlobNameList", a);
		return jsonObject.toString();
	}

	private static boolean finish(INIT i) throws IOException, UnrecoverableKeyException, KeyManagementException,
			CertificateException, KeyStoreException, NoSuchAlgorithmException {
		String json = createJSON(i);
		LOG.log(json);
		StringBuffer buf = new StringBuffer();
		return post(new URL(PROP.getFinishURL()), json.getBytes(), buf, "application/json");
	}

	private static SSLContext getSSL() throws CertificateException, KeyStoreException, NoSuchAlgorithmException,
			IOException, UnrecoverableKeyException, KeyManagementException {
		X509Certificate cert = JPK.readCert(PROP.getCert().getPath());
		String alias = "alias";

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null);
		trustStore.setCertificateEntry(alias, cert);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(trustStore, null);
		KeyManager[] keyManagers = kmf.getKeyManagers();

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(trustStore);
		TrustManager[] trustManagers = tmf.getTrustManagers();

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagers, trustManagers, null);
		return sslContext;

	}

	private static boolean post(URL url, byte[] data, StringBuffer buf, String contentType)
			throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException,
			UnrecoverableKeyException, KeyManagementException {
		SSLContext sslContext = getSSL();
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(sslContext.getSocketFactory());
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestProperty("Content-Length", "" + data.length);
		conn.getOutputStream().write(data);
		return getResult(conn, buf);
	}

	private static INIT post_init() throws CertificateException, NoSuchAlgorithmException, IOException,
			KeyStoreException, UnrecoverableKeyException, KeyManagementException {
		LOG.log("Rozpoczynam wysyłanie nagłówka");

		StringBuffer buf = new StringBuffer();
		if (!post(new URL(PROP.getURL()), UTIL.getFileData(PROP.getInitOutputFile().getPath()), buf, "application/xml"))
			return null;

		INIT i = new INIT();
		JsonParser parser = new JsonParser();
		JsonElement jsonTree = parser.parse(buf.toString());
		i.jsonObject = jsonTree.getAsJsonObject();
		return i;
	}

	/**
	 * Data uploading
	 * 
	 * @param conffile
	 *            Configuration file
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             Steps implemented:<br>
	 *             <ul>
	 *             <li>1. Upload InitUpload.xml file, keep the result,
	 *             particularly ReferenceNumber</li>
	 *             <li>2. Upload encoded data file</li>
	 *             <li>3. FinishUpload</li>
	 *             </ul>
	 */
	public static void upload(String conffile) throws Exception {
		PROP.readConfW(conffile, null);
		try {
			/** 1. Upload Initupload.xml file */
			INIT i = post_init();
			// keep the result in INIT structure
			if (i == null)
				System.exit(4);
			LOG.log("URL  " + i.getFileURL());
			LOG.log("Reference Number " + i.getReference());
			// store reference number for later reuse
			PROP.saveReferenceNumber(i.getReference());
			/** 2. Upload encoded data file */
			if (!post_blob(i))
				System.exit(4);
			LOG.log("Wysłanie kończących danych");
			/** 3. Finish */
			if (!finish(i))
				System.exit(4);
			LOG.log("Wysyłanie całej paczki zakończone sukcesem");
			// get the result
			getUPO(conffile, null);
		} catch (Exception e) {
			LOG.ex(e);
			System.exit(4);
		}

	}

	private static boolean getResult(HttpURLConnection conn, StringBuffer buf) throws IOException {
		int code = conn.getResponseCode();
		InputStream is;
		if (code >= 400) {
			LOG.log("Połączenie zakończone błędem. Kod błędu " + code);
			is = conn.getErrorStream();
		} else {
			LOG.log("Połączenie zakończone sukcesem, Kod " + code);
			is = conn.getInputStream();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		String line;
		while ((line = reader.readLine()) != null) {
			LOG.log(line);
			buf.append(line);
		}
		return code < 400;
	}

	private static boolean readUPO(String referenceNumber, StringBuffer buf)
			throws IOException, UnrecoverableKeyException, KeyManagementException, CertificateException,
			KeyStoreException, NoSuchAlgorithmException {
		URL url = new URL(PROP.getGetURL() + referenceNumber);
		SSLContext sslContext = getSSL();
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(sslContext.getSocketFactory());
		conn.setRequestMethod("GET");
		return getResult(conn, buf);
	}

	private static void extractUPO(StringBuffer buf) throws IOException {
		JsonParser parser = new JsonParser();
		JsonElement jsonTree = parser.parse(buf.toString());
		JsonObject j  = jsonTree.getAsJsonObject();
		String upo = j.get("Upo").getAsString();
		if ("".equals(upo)) {
			LOG.log("Dokument UPO nie pojawił się w otrzymanych danych");
			return;
		}
		File f = PROP.getUPOFile();
		LOG.log("Zapisanie dokumentu UPO do pliku " + f.getAbsolutePath());
		UTIL.writeFile(f, upo);
	}

	/**
	 * Assuming that uploading is successful, retrieve the status code using
	 * ReferenceNumber.<br>
	 * ReferenceNumber is stored in the file kept in work directory.<br>
	 * 
	 * @param conffile
	 *            Configuration file
	 * @param workdir
	 *            If not null working directory, takes precedence over workdir
	 *            in configuration file
	 * @throws Exception
	 */
	public static void getUPO(String conffile, String workdir) throws Exception {
		// Read conf data
		PROP.readConfW(conffile, workdir);
		try {
			// get ReferenceNumber
			String referenceNumber = PROP.readReferenceNumber();
			LOG.log("Reference Number " + referenceNumber);
			// read UPO, status
			StringBuffer buf = new StringBuffer();
			if (readUPO(referenceNumber, buf))
				extractUPO(buf);

		} catch (Exception e) {
			LOG.ex(e);
			System.exit(4);
		}
	}

}
