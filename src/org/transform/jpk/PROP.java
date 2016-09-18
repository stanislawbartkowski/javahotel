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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Helper class to keep and save properties
 */

class PROP {

	private static final Properties prop = new Properties();

	private static final String CONF = "conf";
	private static final String WORKD = "workdir";
	private static final String PUBLICKEY = "publickey";
	private static final String CERT = "cert";
	private static final String URL = "url";
	private static final String FINISH = "finish";
	private static final String PATTFILE = "initupload-enveloped-pattern.xml";
	private static final String GET = "get";
	private static String VATZIP; // set from input vatFile
	private static String WORKDIR; // working directory
	private static final String INITUPLOAD = "InitUpload.xml";
	private static final String FILENAME = "FILENAME";
	private static final String REFERENCENUMBER = "REFERENCENUMBER";

	private PROP() {
	}

	private static String getWork() {
		return WORKDIR;
	}

	private static File fileName() {
		return new File(getWork(), FILENAME);
	}

	private static File referenceFile() {
		return new File(getWork(), REFERENCENUMBER);
	}

	static private File logFile() {
		return new File(getWork(), "JPK.log");
	}

	static private void checkProp(String confFile, boolean clear, String workdir) throws Exception {
		String[] props = { CONF, WORKD, PUBLICKEY, CERT, URL, FINISH, GET };
		for (String s : props)
			if (!prop.containsKey(s))
				throw new Exception(confFile + ", brak parameteru " + s);
		File dir = new File(prop.getProperty(CONF));
		if (!dir.isDirectory())
			throw new Exception(prop.getProperty(CONF) + " brak takiego katalogu");
		if (workdir != null)
			WORKDIR = workdir;
		else
			WORKDIR = prop.getProperty(WORKD);
		File w = new File(getWork());
		if (!w.isDirectory())
			w.mkdir();
		if (!w.isDirectory())
			throw new Exception(getWork() + " brak takiego katalogu i nie można założyć");
		LOG.setConfig(prop.getProperty(CONF), logFile().getPath());
		if (clear) {
			for (File f : w.listFiles())
				f.delete();
			UTIL.writeFile(fileName(), VATZIP);
		} else
			VATZIP = UTIL.getFile(fileName());
		LOG.setConfig(prop.getProperty(CONF), logFile().getPath());
		if (!getPublicKey().exists())
			throw new Exception(getPublicKey().getPath() + " brak pliku z kluczem publicznym do szyfrowania");
		if (!getCert().exists())
			throw new Exception(getPublicKey().getPath() + " brak pliku z publicznym certyfikatem");
	}

	static void readConf(String confFile, String vatFile) throws Exception {
		VATZIP = new File(vatFile).getName();
		prop.load(new FileInputStream(new File(confFile)));
		checkProp(confFile, true, null);
		LOG.log("Odczytane parametery z katalogu " + prop.getProperty(CONF));
	}

	static void readConfW(String confFile, String workdir) throws Exception {
		prop.load(new FileInputStream(new File(confFile)));
		checkProp(confFile, false, workdir);
		LOG.log("Odczytane parametery z katalogu " + prop.getProperty(CONF));
	}

	static String getPattern() throws IOException {
		File f = new File(prop.getProperty(CONF), PATTFILE);
		if (!f.exists())
			throw new IOException(f.getPath() + " plik nie istnieje.");
		if (f.isDirectory())
			throw new IOException(prop.getProperty(f.getPath()) + " powinien być plikiem, nie katalogiem.");
		return new String(Files.readAllBytes(Paths.get(f.toURI())));
	}

	static File getZipFile() {
		return new File(getWork(), VATZIP + ".zip");
	}

	static String getXMLFile() {
		return VATZIP;
	}

	static File getZipAesFile() {
		return new File(getWork(), VATZIP + ".zip.aes");
	}

	static File getPublicKey() {
		return new File(prop.getProperty(CONF), prop.getProperty(PUBLICKEY));
	}

	static File getInitOutputFile() {
		return new File(getWork(), INITUPLOAD);
	}

	static File getCert() {
		return new File(prop.getProperty(CONF), prop.getProperty(CERT));
	}

	static String getURL() {
		return prop.getProperty(URL);
	}

	static String getFinishURL() {
		return prop.getProperty(FINISH);
	}

	static String getGetURL() {
		return prop.getProperty(GET);
	}

	static File getUPOFile() {
		return new File(getWork(), "UPO");
	}

	static void saveReferenceNumber(String referenceNumber) throws IOException {
		UTIL.writeFile(referenceFile(), referenceNumber);
	}

	static String readReferenceNumber() throws IOException {
		return new String(UTIL.getFileData(referenceFile().getPath()));
	}

}
