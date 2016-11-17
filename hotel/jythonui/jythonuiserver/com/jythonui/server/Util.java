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
package com.jythonui.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Named;

import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.gwtmodel.table.binder.BinderWidget;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.server.resourcemulti.IReadMultiResource;
import com.jythonui.shared.RequestContext;

public class Util extends UtilHelper {

	private Util() {
	}

	@Inject
	@Named(ISharedConsts.JYTHONMESSSERVER)
	private static IGetLogMess gMess;

	public static void setContext(RequestContext context) {
		if (context == null)
			return;
		Holder.setContext(context);
	}

	public static String getToken() {
		RequestContext req = Holder.getRequest();
		if (req == null)
			return null;
		return req.getToken();
	}

	public static String getLocale() {
		RequestContext req = Holder.getRequest();
		if (req == null)
			return null;
		return req.getLocale();
	}

	public static Properties getPropertiesFromFile(String propName) {
		FileInputStream f = null;
		try {
			f = new FileInputStream(propName);
		} catch (FileNotFoundException e) {
			errorLog(gMess.getMess(IErrorCode.ERRORCODE82, ILogMess.CANNOTFINDRESOURCEFILE, propName), null);
			return null;
		}
		Properties prop;
		try {
			prop = ReadUTF8Properties.readProperties(f);
			return prop;
		} catch (IOException e) {
			errorLog(gMess.getMess(IErrorCode.ERRORCODE83, ILogMess.ERRORWHILEREADINGRESOURCEFILE, propName), e);
			return null;
		}
	}

	public static Properties getProperties(String propName) {
		InputStream i = Util.class.getClassLoader().getResourceAsStream(propName);
		if (i == null) {
			errorLog(gMess.getMess(IErrorCode.ERRORCODE1, ILogMess.CANNOTFINDRESOURCEFILE, propName), null);
			return null;
		}
		// return ReadUTF8Properties.readProperties(i);
		// Properties prop = ReadUTF8Properties.readProperties(i);
		try {
			Properties prop = ReadUTF8Properties.readProperties(i);
			return prop;
		} catch (IOException e) {
			errorLog(gMess.getMess(IErrorCode.ERRORCODE2, ILogMess.ERRORWHILEREADINGRESOURCEFILE, propName), e);
			return null;
		}
	}

	public static String getFileName(IJythonUIServerProperties p, String... f) {
		return getFirstURL(p, true, f).toString();
	}

	public static URL getFirstURL(IJythonUIServerProperties p, boolean thrownotexist, String... f) {
		if (p.getResource() == null) {
			errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE17, ILogMess.DIALOGDIRECTORYNULL), null);
		}
		String fName = BUtil.addNameToPath(f);
		traceLog("getFirstURL:" + fName);
		URL u = p.getResource().getFirstUrl(fName);
		if (u == null)
			if (thrownotexist)
				errorLog(SHolder.getM().getMess(IErrorCode.ERRORCODE80, ILogMess.FILENOTFOUND, fName), null);
		return u;
	}

	public static InputStream getDialogFile(IJythonUIServerProperties p, String name) {
		try {
			return getFirstURL(p, true, IConsts.DIALOGDIR, name).openStream();
		} catch (IOException e) {
			errorLog(
					L().getMess(IErrorCode.ERRORCODE54, ILogMess.FILENOTFOUND, getFileName(p, IConsts.DIALOGDIR, name)),
					e);
			return null;
		}
	}

	public static String getBirtFile(IJythonUIServerProperties p, String name) {
		return getFirstURL(p, true, IConsts.BIRTDIR, name).getPath();
	}

	public static List<String> getJythonPackageDirectory(IReadMultiResource iRes) {
		List<String> l = new ArrayList<String>();
		List<URL> uL = iRes.getUrlList(IConsts.PACKAGEDIR);
		for (URL u : uL)
			l.add(u.getPath());
		return l;
		// / return iRes.getRes(IConsts.PACKAGEDIR).getPath();
	}

	public static URL fileNameToURL(String fileName) {
		try {
			return new File(fileName).toURI().toURL();
		} catch (MalformedURLException e) {
			errorLog(L().getMess(IErrorCode.ERRORCODE109, ILogMess.ERRORINFILENAMETOURL, fileName), e);
			return null;
		}
	}

	public static void removeStorageKeys(IStorageRegistry iReg) {
		List<String> keys = iReg.getKeys();
		for (String k : keys)
			iReg.removeEntry(k);
	}

	public static BinderWidget readBinderWidget(String fileName) {
		IBinderParser iParser = Holder.getiBinder();
		try {
			return iParser.parse(fileName);
		} catch (SAXException | IOException e) {
			errorLog(L().getMess(IErrorCode.ERRORCODE130, ILogMess.CANNOTREADBINDERFILE, fileName), e);
			return null;
		}
	}

	/*
	 * Files is not supported in Google App Engine public static String
	 * getStringFromFile(Class cl, String resName) { URL in =
	 * cl.getClassLoader().getResource(resName); if (in == null)
	 * errorLog(gMess.getMess(IErrorCode.ERRORCODE133,
	 * ILogMess.CANNOTFINDRESOURCEFILE, resName)); try { return new
	 * String(Files.readAllBytes(Paths.get(in.toURI()))); } catch (IOException |
	 * URISyntaxException e) { errorLog(gMess.getMess(IErrorCode.ERRORCODE134,
	 * ILogMess.ERRORWHILEREADINGRESOURCEFILE, resName), e); return null; } }
	 */

	/*
	 * public static String getStringFromFile(Class cl, String resName) { URL in
	 * = cl.getClassLoader().getResource(resName); if (in == null)
	 * errorLog(gMess.getMess(IErrorCode.ERRORCODE133,
	 * ILogMess.CANNOTFINDRESOURCEFILE, resName)); try { File fName = new
	 * File(in.toURI()); byte[] buf = new byte[(int)fName.length()];
	 * RandomAccessFile f = new RandomAccessFile(fName, "r"); f.readFully(buf);
	 * return new String(buf); } catch (IOException | URISyntaxException e) {
	 * errorLog(gMess.getMess(IErrorCode.ERRORCODE134,
	 * ILogMess.ERRORWHILEREADINGRESOURCEFILE, resName), e); return null; } }
	 */

	public static String getStringFromFile(Class cl, String resName) {
		InputStream in = cl.getClassLoader().getSystemResourceAsStream(resName);
		if (in == null)
			errorLog(gMess.getMess(IErrorCode.ERRORCODE133, ILogMess.CANNOTFINDRESOURCEFILE, resName));
		return BUtil.readFromFileInput(in);
	}

	public static Map<String, String> readResDataMap(URL resName) {
		Map<String, String> ma = new HashMap<String, String>();
		Properties prop;
		try {
			prop = ReadUTF8Properties.readProperties(resName.openStream());
		} catch (IOException e) {
			errorLog(gMess.getMess(IErrorCode.ERRORCODE139, ILogMess.CANNOTFINDRESOURCEFILE, resName.toString()),e);
			return null;
		}
		MUtil.toElem(ma, prop);
		return ma;
	}

}
