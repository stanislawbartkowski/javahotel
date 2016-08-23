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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

class UTIL {

	private UTIL() {

	}

	static void writeXMLToFile(Document doc, File outFile) throws IOException, TransformerConfigurationException,
			TransformerFactoryConfigurationError, TransformerException, FileNotFoundException {
		// Write the output to a file
		Source source = new DOMSource(doc);

		// Prepare the output file
		outFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(outFile);

		StreamResult result = new StreamResult(fos);

		// Write the DOM document to the file
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);

		fos.close();
	}

	private final static int BUFFER = 4000;

	static void zipFile(String fileName, File outfile) throws IOException {
		FileOutputStream dest = new FileOutputStream(outfile.getPath());
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
		FileInputStream fi = new FileInputStream(fileName);
		byte data[] = new byte[BUFFER];
		BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
		ZipEntry entry = new ZipEntry(new File(fileName).getName());
		out.putNextEntry(entry);
		int count;
		while ((count = origin.read(data, 0, BUFFER)) != -1)
			out.write(data, 0, count);

		origin.close();
		out.close();
	}

	static byte[] getZipData() throws IOException {
		return Files.readAllBytes(PROP.getZipFile().toPath());
	}

	static byte[] getZipAesData() throws IOException {
		return Files.readAllBytes(PROP.getZipAesFile().toPath());
	}

	static byte[] getFileData(String vatFile) throws IOException {
		return Files.readAllBytes(new File(vatFile).toPath());
	}

	static String getFile(File f) throws IOException {
		byte[] c = Files.readAllBytes(f.toPath());
		return new String(c);
	}

	static void writeFile(File f, String s) throws IOException {
		FileOutputStream dest = new FileOutputStream(f);
		dest.write(s.getBytes(), 0, s.getBytes().length);
		dest.close();
	}

}
