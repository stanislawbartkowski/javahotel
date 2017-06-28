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

package org.migration.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.migration.extractor.ObjectExtractor;
import org.migration.fix.FixObject;
import org.migration.fix.ObjectExtracted;

public class ExtractObjects {

	private ExtractObjects() {

	}

	private static final Logger log = Logger.getLogger(ExtractObjects.class.getName());

	private static void clearDir(File outputDir) {

		for (File f : outputDir.listFiles()) {
			if (f.isDirectory())
				clearDir(f);
			f.delete();
		}
	}

	private final static Set<ObjectExtractor.OBJECT> setO = new HashSet<ObjectExtractor.OBJECT>();

	static {

		setO.addAll(Arrays.asList(ObjectExtractor.OBJECT.BODY, ObjectExtractor.OBJECT.FOREIGNKEY,
				ObjectExtractor.OBJECT.PACKAGE, ObjectExtractor.OBJECT.FUNCTION, ObjectExtractor.OBJECT.PROCEDURE,
				ObjectExtractor.OBJECT.SEQUENCE, ObjectExtractor.OBJECT.TRIGGER, ObjectExtractor.OBJECT.TYPE,
				ObjectExtractor.OBJECT.FUNCTION, ObjectExtractor.OBJECT.VIEW, ObjectExtractor.OBJECT.TABLE,
				ObjectExtractor.OBJECT.GLOBALTEMP));
	}

	private static void writeF(BufferedWriter w, List<String> li) {
		li.forEach(line -> {
			try {
				w.write(line);
				w.newLine();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}); // forEach
	}

	private static void createDirO(File outputDir, ObjectExtractor.OBJECT oType, String oName, ObjectExtracted ee)
			throws IOException {
		File dir = new File(outputDir, oType.name().toLowerCase());
		dir.mkdirs();
		File fout = new File(dir, oName.replace(".", "_").toLowerCase() + ".db2");
		ObjectExtractor.fixTerminator(ee.getO());
		try (BufferedWriter w = new BufferedWriter(new FileWriter(fout))) {
			writeF(w, ee.getO().getLines());
			ee.getAdded().forEach(e -> writeF(w, e.getLines()));
		}
	}

	private static boolean eqS(String f1, String f2) {
		return f1.toUpperCase().equals(f2.toUpperCase());
	}

	private static void addLines(ObjectExtracted ee, String tName, ObjectExtractor.OBJECT eType,
			Map<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>> ma) {
		List<ObjectExtractor.IObjectExtracted> eList = ma.get(eType);
		if (eList == null)
			return;
		eList.stream().filter(e -> eqS(eType == ObjectExtractor.OBJECT.ALTERTABLE ? e.getName() : e.onTable(), tName))
				.forEach(e -> {
					ee.getAdded().add(e);
				});
	}

	private static void extractObject(ObjectExtractor.OBJECT oType, List<ObjectExtractor.IObjectExtracted> li,
			Map<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>> ma, File outputDir) {

		if (li == null)
			return;

		if (setO.contains(oType))
			li.forEach(e -> {
				try {
					log.info(oType.name() + " " + e.getName());
					ObjectExtracted ee = new ObjectExtracted(e);
					if (oType == ObjectExtractor.OBJECT.TABLE || oType == ObjectExtractor.OBJECT.GLOBALTEMP) {
						// add additional object to table creation
						addLines(ee, e.getName(), ObjectExtractor.OBJECT.UNIQUE, ma);
						addLines(ee, e.getName(), ObjectExtractor.OBJECT.INDEX, ma);
						addLines(ee, e.getName(), ObjectExtractor.OBJECT.ALTERTABLE, ma);
					}
					FixObject.fix(ee);
					createDirO(outputDir, oType, e.getName(), ee);
				} catch (IOException e1) {
					throw new UncheckedIOException(e1);
				}
			});
	}

	public static void extractObjects(String inputName, String outputDir) throws FileNotFoundException, Exception {

		log.info("Start extracting objectst");
		log.info("Input file " + inputName);
		log.info("Output directory" + outputDir);
		// clear output directory
		File dir = new File(outputDir);
		if (dir.exists() && dir.isDirectory())
			clearDir(dir);
		dir.delete();
		dir.mkdirs();
		// cleared and recreated
		File fn = new File(inputName);
		ExtractContainer.run(new BufferedReader(new FileReader(fn)),
				(ObjectExtractor.OBJECT oType, List<ObjectExtractor.IObjectExtracted> li,
						Map<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>> ma) -> extractObject(oType,
								li, ma, dir));
		log.info("I'm done, objects extracted to " + outputDir);

	}

}
