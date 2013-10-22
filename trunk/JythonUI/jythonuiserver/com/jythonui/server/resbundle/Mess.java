/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.resbundle;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.gwtmodel.util.ReadUTF8Properties;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.Util;
import com.jythonui.server.getmess.GetLogMessFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.CustomMessages;
import com.jythonui.shared.JythonUIFatal;

public class Mess implements IAppMess {

	private final IJythonUIServerProperties iRes;
	private IGetLogMess iMess = null;
	private String lastloca = null;
	static final private Logger log = Logger.getLogger(Mess.class.getName());

	static private void error(String mess, Throwable e) {
		log.log(Level.SEVERE, mess);
		throw new JythonUIFatal(mess, e);
	}

	static private void debug(String mess) {
		log.fine(mess);
	}

	@Inject
	public Mess(IJythonUIServerProperties iRes) {
		this.iRes = iRes;
	}

	private boolean localeChanged() {
		String loc = Util.getLocale();
		if ((loc == null) && (lastloca == null))
			return false;
		if ((loc == null) && (lastloca != null))
			return true;
		if ((loc != null) && (lastloca == null))
			return true;
		return !loc.equals(lastloca);
	}

	private void setMess() {
		if ((iMess == null) || !iRes.isCached() || localeChanged()) {
			String propdefaS = iRes.getBundleBase() + "/messages.properties";
			String propS = null;
			String loc = Util.getLocale();
			debug("setMess locale=" + loc);
			if (loc != null) {
				propS = iRes.getBundleBase() + "/messages_" + Util.getLocale()
						+ ".properties";
				debug("locale not null " + propS);
			}
			Properties defa = null;
			try {
				defa = ReadUTF8Properties.readProperties(propdefaS);
			} catch (IOException e) {
				error(Holder.getM().getMess(IErrorCode.ERRORCODE46,
						ILogMess.ERRORWHILELOADINGBUNDLERESOURCE,
						iRes.getBundleBase()), e);
			}
			Properties prop = null;
			try {
				if (propS != null)
					prop = ReadUTF8Properties.readProperties(propS);
			} catch (IOException e1) {
				// expected, do nothing
				debug(propS + " not found");
			}
			if (prop == null)
				iMess = GetLogMessFactory.construct(defa);
			else {
				iMess = GetLogMessFactory.construct(prop, defa);
				debug("combine : " + propS + " " + propdefaS);

			}
			lastloca = loc;
		}
	}

	@Override
	public String getMess(String errCode, String key, String... params) {
		setMess();
		return iMess.getMess(errCode, key, params);
	}

	@Override
	public String getMessN(String key, String... params) {
		setMess();
		return iMess.getMessN(key, params);
	}

	@Override
	public CustomMessages getCustomMess() {
		if (iRes.getBundleBase() == null)
			return null;
		setMess();
		CustomMessages cust = new CustomMessages();
		Map<String, String> ma = iMess.getMess();
		for (String key : ma.keySet()) {
			cust.setAttr(key, ma.get(key));
		}
		return cust;
	}

	@Override
	public Map<String, String> getMess() {
		return iMess.getMess();
	}

}
