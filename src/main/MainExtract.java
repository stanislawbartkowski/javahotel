/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
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

package main;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.migration.extractor.ObjectExtractor;
import org.migration.fix.FixObject;
import org.migration.fix.impl.ForeignFixTail;
import org.migration.fix.impl.GlobalTableFixPrimary;
import org.migration.fix.impl.ProcedFixNEq;
import org.migration.fix.impl.RemoveEditionable;
import org.migration.fix.impl.Replace32767;
import org.migration.fix.impl.SequenceFixMaxValue;
import org.migration.fix.impl.TableFixIndexName;
import org.migration.fix.impl.TableFixPrimaryKey;
import org.migration.fix.impl.TableFixTail;
import org.migration.fix.impl.TableFixTypes;
import org.migration.fix.impl.TableFixUnique;
import org.migration.properties.PropHolder;
import org.migration.tasks.ExtractObjects;

public class MainExtract {

	private static void e(String s) {
		System.out.println(s);
	}

	private final static Logger l = Logger.getLogger(MainExtract.class.getName());

	private static void drawhelp() {
		e("Parameters");
		e(" <input file> <output dir> <propertyfile>");
		System.exit(4);
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 3)
			drawhelp();
		String propname = args[2];
		l.info("Read properties from " + propname);
		Properties prop = new Properties();
		prop.load(new FileInputStream(propname));
		// merge with default
		PropHolder.getProp().putAll(prop);
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixUnique());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixTail());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixIndexName());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixTypes());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixPrimaryKey());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new Replace32767());
		
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new TableFixIndexName());
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new TableFixTail());
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new GlobalTableFixPrimary());
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new Replace32767());
		
		FixObject.register(ObjectExtractor.OBJECT.SEQUENCE, new SequenceFixMaxValue());
		
		FixObject.register(ObjectExtractor.OBJECT.PROCEDURE, new ProcedFixNEq());
		FixObject.register(ObjectExtractor.OBJECT.PROCEDURE, new Replace32767());
		FixObject.register(ObjectExtractor.OBJECT.PROCEDURE, new RemoveEditionable());
				
		FixObject.register(ObjectExtractor.OBJECT.FUNCTION, new ProcedFixNEq());
		FixObject.register(ObjectExtractor.OBJECT.FUNCTION, new Replace32767());
		FixObject.register(ObjectExtractor.OBJECT.FUNCTION, new RemoveEditionable());

		
		FixObject.register(ObjectExtractor.OBJECT.BODY, new ProcedFixNEq());
		FixObject.register(ObjectExtractor.OBJECT.BODY, new Replace32767());
		FixObject.register(ObjectExtractor.OBJECT.BODY, new RemoveEditionable());

		
		FixObject.register(ObjectExtractor.OBJECT.FOREIGNKEY, new ForeignFixTail());
		
		FixObject.register(ObjectExtractor.OBJECT.PACKAGE, new Replace32767());
		FixObject.register(ObjectExtractor.OBJECT.PACKAGE, new RemoveEditionable());
		
		FixObject.register(ObjectExtractor.OBJECT.TRIGGER, new Replace32767());

		FixObject.register(ObjectExtractor.OBJECT.TYPE, new Replace32767());
		
		ExtractObjects.extractObjects(args[0], args[1]);
	}

}
