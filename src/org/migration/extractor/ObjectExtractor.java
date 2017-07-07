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
package org.migration.extractor;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.migration.comparedb2.CUtil;
import org.migration.fix.impl.U;
import org.migration.properties.PropHolder;
import org.migration.tokenizer.ITokenize;
import org.migration.tokenizer.TokenizeFactory;

public class ObjectExtractor implements AutoCloseable {

	public final static String CREATE = "CREATE";
	private final static String OR = "OR";
	public final static String REPLACE = "REPLACE";
	private final static String ALTER = "ALTER";
	private final static String FOREIGN = "FOREIGN";
	private final static String KEY = "KEY";
	private final static String GLOBAL = "GLOBAL";
	private final static String TEMPORARY = "TEMPORARY";
	private final static String TABLE = "TABLE";
	public final static String EDITIONABLE = "EDITIONABLE";
	public final static String NONEDITIONABLE = " NONEDITIONABLE";

	private final static String SEMI = ";";

	private final ITokenize token;
	private Set<String> schemasonly = null;

	private void setSchemas() {
		// = new HashSet<String>();
		String lOfSchemas = PropHolder.getProp().getProperty(PropHolder.SCHEMASONLY);
		if (lOfSchemas == null)
			return;
		String[] o = lOfSchemas.split(",");
		if (o.length < 1)
			return;
		schemasonly = new HashSet<String>();
		for (String s : o)
			schemasonly.add(s);
	}

	public ObjectExtractor(BufferedReader reader) {
		token = TokenizeFactory.provide(reader);
		setSchemas();
	}

	public ObjectExtractor(String inputFile) {
		token = TokenizeFactory.provide(inputFile);
		setSchemas();
	}

	public enum OBJECT {
		TABLE, TYPE, PACKAGE, BODY, INDEX, UNIQUE, ALTERTABLE, PROCEDURE, FUNCTION, TRIGGER, SEQUENCE, VIEW, FOREIGNKEY, GLOBALTEMP
	}

	public interface IObjectExtracted {
		String getName();

		String onTable();

		OBJECT getType();

		List<String> getLines();
	}

	private static final Set<OBJECT> terminatedbySemi;
	private static final Set<OBJECT> terminatedbyTerm;

	static {
		terminatedbySemi = new HashSet<OBJECT>();
		terminatedbyTerm = new HashSet<OBJECT>();
		terminatedbySemi.add(OBJECT.TABLE);
		terminatedbySemi.add(OBJECT.INDEX);
		terminatedbySemi.add(OBJECT.UNIQUE);
		terminatedbySemi.add(OBJECT.ALTERTABLE);
		terminatedbySemi.add(OBJECT.SEQUENCE);
		terminatedbySemi.add(OBJECT.VIEW);
		terminatedbySemi.add(OBJECT.FOREIGNKEY);
		terminatedbySemi.add(OBJECT.GLOBALTEMP);

		terminatedbyTerm.add(OBJECT.PACKAGE);
		terminatedbyTerm.add(OBJECT.BODY);
		terminatedbyTerm.add(OBJECT.PROCEDURE);
		terminatedbyTerm.add(OBJECT.FUNCTION);
		terminatedbyTerm.add(OBJECT.TRIGGER);
		terminatedbyTerm.add(OBJECT.TYPE);
	}

	public IObjectExtracted extractNext() {

		// look for CREATE
		String w = null;

		while (true) {

			boolean isalter = false;
			while (true) {
				while ((w = token.readNextWord()) != null) {
					if (CREATE.equalsIgnoreCase(w))
						break;
					if (ALTER.equalsIgnoreCase(w)) {
						isalter = true;
						break;
					}
				}

				if (w == null)
					return null;
				// CREATE, collect lines
				token.startCollectingLines();
				// look for OR
				w = token.readNextWord();
				if (w != null && OR.equalsIgnoreCase(w)) {
					w = token.readNextWord();
					if (w == null)
						return null;
					// look for replace
					if (!REPLACE.equalsIgnoreCase(w))
						continue;
					w = token.readNextWord();
				}
				// skip EDITIONABLE
				if (NONEDITIONABLE.equalsIgnoreCase(w) || EDITIONABLE.equalsIgnoreCase(w))
					w = token.readNextWord();
				// object type
				if (w == null)
					return null;
				break;
			} // while
				// object found
			String oType = w.toUpperCase(); // object type
			OBJECT otype = null;
			// check if object type supported
			for (OBJECT o : OBJECT.values())
				if (o.name().equals(oType))
					otype = OBJECT.valueOf(oType);
			if (otype == null)
				if (GLOBAL.equalsIgnoreCase(w)) {
					otype = OBJECT.GLOBALTEMP;
					w = token.readNextWord();
					if (w == null)
						return null;
					if (!TEMPORARY.equalsIgnoreCase(w))
						continue;
					w = token.readNextWord();
					if (w == null)
						return null;
					if (!TABLE.equalsIgnoreCase(w))
						continue;
					// next item is temporary table name
				} else

					continue;
			// object name
			if (isalter)
				if (otype == OBJECT.TABLE)
					otype = OBJECT.ALTERTABLE;
				// only alter table is supported
				else
					continue;

			w = token.readNextWord();
			if (w == null)
				return null;
			if (otype == OBJECT.UNIQUE) {
				// should be INDEX
				if (!OBJECT.INDEX.name().equalsIgnoreCase(w))
					continue;
				w = token.readNextWord();
				if (w == null)
					return null;
			}
			String oName = w;
			String onTable = null;
			// to recognize package body
			int wno = 0; // words counter
			boolean wasforeign = false;
			while ((w = token.readNextWord()) != null) {
				String uw = w.toUpperCase();
				if (wno == 0)
					// PACKAGE BODY
					if (oName.equalsIgnoreCase(OBJECT.BODY.name())) {
						otype = OBJECT.BODY;
						oName = w;
					}
				if (wno == 1 && (otype == OBJECT.INDEX || otype == OBJECT.UNIQUE))
					onTable = w;
				wno++;
				if (w.equals(SEMI) && terminatedbySemi.contains(otype))
					break;
				if (w.equals(ITokenize.TERMINATEWORD) && terminatedbyTerm.contains(otype))
					break;
				if (wasforeign)
					if (KEY.equals(uw))
						otype = OBJECT.FOREIGNKEY;
				wasforeign = otype == OBJECT.ALTERTABLE && FOREIGN.equals(uw);

			} // while
			final OBJECT objecttype = otype;
			final String objectname = oName.replace("\"", "");
			final String ontablename = onTable;
			if (schemasonly != null) {
				CUtil.IObjectName iName = CUtil.objectName(objectname);
				if (iName.getSchema() != null && !schemasonly.contains(iName.getSchema()))
					continue;
			}
			// make snapshot of current line
			final List<String> cList = new ArrayList<String>();
			cList.addAll(token.getLines());
			return new IObjectExtracted() {

				@Override
				public String getName() {
					// remove "
					return objectname;
				}

				@Override
				public OBJECT getType() {
					return objecttype;
				}

				@Override
				public List<String> getLines() {
					return cList;
				}

				@Override
				public String onTable() {
					return ontablename;
				}
			};

		} // while

	}

	@Override
	public void close() throws Exception {
		token.close();
	}

	public static void fixTerminator(IObjectExtracted i) {
		if (!terminatedbyTerm.contains(i.getType()))
			return;
		String termina = PropHolder.getProp().getProperty(PropHolder.PROCTERMINATOR);
		if (termina == null)
			return;
		// last line
		String line = i.getLines().get(i.getLines().size() - 1);
		if (U.isStatementTerm(line))
			line = termina;
		i.getLines().set(i.getLines().size() - 1, line);
	}

}
