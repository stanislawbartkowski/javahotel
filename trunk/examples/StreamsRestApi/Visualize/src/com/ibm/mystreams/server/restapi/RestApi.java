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
package com.ibm.mystreams.server.restapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

import com.ibm.mystreams.server.GreetingServiceImpl;
import com.ibm.mystreams.shared.ConnectionData;

public class RestApi {

	private RestApi() {
	}

	private static final String restURL = "https://{0}:{1}/streams/rest/{2}";

	private final static Logger l = Logger.getLogger(GreetingServiceImpl.class
			.getName());

	private static class BusinessIntelligenceX509TrustManager implements
			X509TrustManager {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new java.security.cert.X509Certificate[] {};
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
			// no-op
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
			// no-op
		}

	}

	public static String readString(ConnectionData data, String restReq)
			throws NoSuchAlgorithmException, KeyManagementException,
			IOException {

		TrustManager[] trustAllCerts = new TrustManager[] { new BusinessIntelligenceX509TrustManager() };
		SSLContext sc;

		sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());

		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};

		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		URL url = new URL(MessageFormat.format(restURL, data.getHost(),
				data.getPort(), restReq));
		String userInfo = data.getUser() + ":" + data.getPassword();
		String authToken = "Basic "
				+ DatatypeConverter.printBase64Binary(userInfo.getBytes());
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", authToken);
		conn.setRequestMethod("GET");
		conn.connect();
		l.info("Response code: " + conn.getResponseCode());
		l.info("Content type: " + conn.getHeaderField("Content-Type"));
		String response = new BufferedReader(new InputStreamReader(
				conn.getInputStream())).readLine();
		l.info("Response: " + response);
		conn.disconnect();
		return response;
	}

}
