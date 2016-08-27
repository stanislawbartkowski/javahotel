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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Main class for creating and transforming JPK files
 */

public class JPK {
	
	private static final String publicKeyPlaceHolder = "{EncryptionKey}";
	private static final String hashdocPlaceHolder = "{hashDoc}";
	private static final String ivfilePlaceHolder = "{ivFile}";
	private static final String hashFilePlaceHolder = "{hashFile}";
	private static final String xmlfilenamePlaceHolder = "{vatfilexml}";
	private static final String zipfileaesPlaceHolder = "{vatzipfileaes}";
	private static final String xmlfilelenPlaceHolder = "{xmlfilelen}";
	private static final String zipfileaeslenPlaceHolder = "{vatfileaeslen}";

	private static final int HASHSIZE = 44;
	private static final int CONTENTFILEHASHSIZE = 24;

	private JPK() {

	}

	private static String base64S(byte[] b) {
		return Base64.getEncoder().encodeToString(b);
	}

	private static class EncryptedData {
		private final byte[] iv;
		private final byte[] encoded;

		EncryptedData(byte[] iv, byte[] encoded) {
			this.iv = iv;
			this.encoded = encoded;
		}

		String encodedS() {
			return base64S(encoded);
		}

		String ivS() {
			return base64S(iv);
		}

		byte[] encoded() {
			return encoded;
		}
	}

	private static Key genkey() throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(256); // 192 and 256 bits may not be available
		return kgen.generateKey();
	}

	static X509Certificate readCert(String pKey) throws CertificateException, FileNotFoundException {
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
		FileInputStream is = new FileInputStream(pKey);
		return (X509Certificate) fact.generateCertificate(is);
	}

	private static EncryptedData encryptKey(X509Certificate certificate, Key key) throws Exception {
		PublicKey pk = certificate.getPublicKey();
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		byte[] b = cipher.doFinal(key.getEncoded());
		return new EncryptedData(null, b);
	}

	private static String toHash(byte[] s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.update(s); // Change this to "UTF-16" if needed
		byte[] digest = md.digest();
		return base64S(digest);
	}
	
	private static String toHashMD5(byte[] s) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(s);
		return base64S(thedigest);
	}

	private static EncryptedData encryptFile(Key key, byte[] tocode) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		assert iv.length == 16;
		byte[] b = cipher.doFinal(tocode);
		return new EncryptedData(iv, b);
	}

	private static Document toE(String xml) throws ParserConfigurationException, SAXException, IOException {
		Reader r = new StringReader(xml);
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return db.parse(new InputSource(r));
	}

	private static String getHashXML(String vatFile) throws IOException, NoSuchAlgorithmException {
		byte[] x = UTIL.getFileData(vatFile);
		return toHash(x);
	}

	/**
	 * Prepares InitUpload.xml file ready for singing and encodes VAT file containing financial data
	 * @param conffile Configuration file
	 * @param vatFile VAT file ready to be uploaded
	 * @throws Exception
	 * 
	 * Steps implemented:
	 * 1. Generate random symmetric key
	 * 2. Read public certificate
	 * 3. Encode symmetric key using public key extracted from certificate
	 * 4. Generate hash for input file 
	 * 5. Insert generated value into xml pattern
	 * 6. Encode compressed VAT input file
	 * 7. Save encoded file
	 * 8. Generate MD5 hash for encoded data
	 * 9. Prepare InitUpload.xml from xml pattern by inserting generated data
	 * 10. Save the result
	 */
	public static void Prepare(String conffile, String vatFile) throws Exception {

		PROP.readConf(conffile, vatFile);
		try {
			LOG.log("Kompresuję plik " + vatFile + " na " + PROP.getZipFile().getPath());
			/** Compress input file */
			UTIL.zipFile(vatFile, PROP.getZipFile());
			LOG.log("Generuję klucz symetryczny");
			/** 1. Generate symmetric key */
			Key key = genkey();
			/** 2. Read public certificate */
			LOG.log("Odczytuję publiczny certyfikat " + PROP.getPublicKey().getPath());
			X509Certificate cert = readCert(PROP.getPublicKey().getPath());
			LOG.log("Koduję klucz symetryczny za pomocą klucza publicznego");
			/** 3. Encode symmetric key */
			EncryptedData eKey = encryptKey(cert, key);
			LOG.log("Obliczam skrót SHA dla pliku " + vatFile);
			/** 4. Generate SHA-256 hash value for input file */
			String hashDoc = getHashXML(vatFile);
			assert HASHSIZE == hashDoc.length();
			String xml = PROP.getPattern();
			/** 5. Insert generated hash into xml pattern */
			String xml1 = xml.replace(publicKeyPlaceHolder, eKey.encodedS()).replace(hashdocPlaceHolder, hashDoc);

			LOG.log("Koduję plik " + PROP.getZipFile().getPath() + " za pomocą klucza symetrycznego");
			/** 6. Encode compressed VAT input file */
			EncryptedData f = encryptFile(key, UTIL.getZipData());

			// save encoded date
			LOG.log("Zapamiętuję plik w postaci zakodowanej " + PROP.getZipAesFile().getPath());
			/** 7. Save encoded file */
			FileOutputStream fileOuputStream = new FileOutputStream(PROP.getZipAesFile());
			fileOuputStream.write(f.encoded());
			fileOuputStream.close();

			LOG.log("Obliczam skrót MD5 dla zakodowanej części");
			/** 8. Generate MD5 hash for encoded data */
            String hashEes = toHashMD5(UTIL.getZipAesData());    			
			assert CONTENTFILEHASHSIZE == hashEes.length();

			/** 9. Prepare InitUpload.xml from xml pattern by inserting generated data */
			String xml2 = xml1.replace(ivfilePlaceHolder, f.ivS());
			String xml3 = xml2.replace(xmlfilenamePlaceHolder, PROP.getXMLFile());
			xml3 = xml3.replace(xmlfilelenPlaceHolder, "" + new File(vatFile).length());
			xml3 = xml3.replace(zipfileaeslenPlaceHolder, "" + f.encoded.length);
			xml3 = xml3.replace(zipfileaesPlaceHolder, PROP.getZipAesFile().getName());
			xml3 = xml3.replace(hashFilePlaceHolder, hashEes);
			Document doc = toE(xml3);
			LOG.log("Generuję plik nagłówkowy " + PROP.getInitOutputFile());
			/** 10. Save the result, InitUpload.xml is ready for signing */
			UTIL.writeXMLToFile(doc, PROP.getInitOutputFile());
		} catch (Exception e) {
			LOG.ex(e);
			System.exit(4);
		}
	}

}
