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
package com.jythonui.server.jython;

import java.sql.Timestamp;
import java.util.Date;

import com.gwtmodel.containertype.ContainerInfo;
import com.gwtmodel.containertype.ContainerType;
import com.jythonui.server.IConvertJythonTimestamp;

public class ConvertPython27 implements IConvertJythonTimestamp {

	// 2015/05/15 : official Jython version
	// While converting from Time hours is shifted !!
	@Override
	public Timestamp fromJython(Object o) {
		Timestamp t = (Timestamp) o;
		if (ContainerInfo.getContainerType() == ContainerType.APPENGINE)
			return t;
		Date d = new Date();
		d.setTime(t.getTime());
		d.setHours(d.getHours() - 1);
		// !!! something strange, if month >=3 and month < 10 then hours is
		// decreased by 1 again !!!!
		if (d.getMonth() >= 3 && d.getMonth() < 10)
			d.setHours(d.getHours() - 1);
		// d.setDate(d.getDate()-1);
		return new Timestamp(d.getTime());
	}
}
