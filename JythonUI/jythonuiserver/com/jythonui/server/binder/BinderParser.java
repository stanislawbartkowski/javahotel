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
package com.jythonui.server.binder;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.gwtmodel.table.binder.BinderWidget;
import com.jythonui.server.IBinderParser;
import com.jythonui.server.IConsts;
import com.jythonui.server.IGetResourceFile;
import com.jythonui.server.IVerifySchema;

public class BinderParser implements IBinderParser {

	private final IVerifySchema iVerify;
	private final IGetResourceFile iGetResource;

	@Inject
	public BinderParser(IVerifySchema iVerify, IGetResourceFile iGetResource) {
		this.iVerify = iVerify;
		this.iGetResource = iGetResource;
	}

	@Override
	public BinderWidget parse(String fileName) throws SAXException, IOException {
		iVerify.verify(iGetResource.getDialogFile(fileName), IConsts.BINDERXSDFILE, IConsts.HTMLXSDFILE);
		try {
			return BinderReader.parseBinder(iGetResource.getDialogFile(fileName));
		} catch (ParserConfigurationException e) {
			throw new SAXException(e);
		}
	}

}
