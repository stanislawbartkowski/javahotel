/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.db2.sb.scriptlauncher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class containing method for creating DB2 command line
 * 
 * @author sbartkowski
 * 
 */
public class DB2LineFactory {

	/** DB2 CLP separator. */
	private static final String SEP = ";"; //$NON-NLS-1$

	/**
	 * Thread to gather output from 'exec' to avoid blocking
	 * 
	 * @author sbartkowski
	 * 
	 */
	private static class ReadI extends Thread {

		private final InputStream i;

		ReadI(InputStream i) {
			this.i = i;
		}

		@Override
		public void run() {
			int in;
			try {
				while ((in = i.read()) != -1) {
					char ch = (char) in;
					System.out.print(ch);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create connection string to DB2 server
	 * 
	 * @param db2Alias
	 *            Database alias
	 * @param db2User
	 *            User/login name
	 * @param db2Password
	 *            Password
	 * @return Connection string
	 */
	private static String createConnectString(String db2Alias, String db2User,
			String db2Password) {

		String s = "CONNECT TO " + db2Alias + " USER " + db2User + " USING " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ db2Password;
		return s;

	}

	/**
	 * Common entry for executing command (if both script and script content are
	 * null then connect to the database only
	 * 
	 * @param parent
	 *            Shell parent
	 * @param pMonitor
	 *            Progress monitor
	 * @param db2Alias
	 *            Database alias
	 * @param db2User
	 *            Login/User
	 * @param db2Password
	 *            Password
	 * @param script
	 *            String to run (if not null)
	 * @param scriptContent
	 *            Content to run (if not null)
	 * @throws IOException
	 *             Exception
	 * @throws InterruptedException
	 *             Exception
	 */
	static void executeDB2Script(Shell parent, IProgressMonitor pMonitor,
			String db2Alias, String db2User, String db2Password, String script,
			String scriptContent) throws IOException, InterruptedException {

		String con = createConnectString(db2Alias, db2User, db2Password);
		Process p;
		if (pMonitor != null) {
			pMonitor.beginTask(Messages.CONNECT_AND_EXECUTE,
					IProgressMonitor.UNKNOWN);
		}
		File temp = null;
		if (script == null && scriptContent == null) {
			// connect to database only
			System.out.println(Messages.CONNECTING);
			p = Runtime.getRuntime().exec("db2 " + con); //$NON-NLS-1$
		} else {
			// copy content or script to temporary file
			temp = File.createTempFile("script", ".tempsql"); //$NON-NLS-1$ //$NON-NLS-2$
			temp.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			// add connection string at the beginning
			out.write(con);
			// add command separator
			out.write(SEP);
			out.write(System.getProperty("line.separator").toString()); //$NON-NLS-1$
			Reader r;
			if (scriptContent != null) {
				r = new StringReader(scriptContent);
			} else {
				File f = new File(script);
				r = new FileReader(f);
			}
			int ch;
			while ((ch = r.read()) != -1) {
				out.write(ch);
			}
			out.write(SEP);
			out.close();
			System.out.println(Messages.CONNECTING_AND_RUNNING);
			// command to start clp
			String com = "db2 -stvf " + temp.getAbsolutePath(); //$NON-NLS-1$
			p = Runtime.getRuntime().exec(com);
		}
		// gather output
		Thread r1 = new ReadI(p.getInputStream());
		Thread r2 = new ReadI(p.getErrorStream());
		r1.run();
		r2.run();
		p.waitFor();
		if (temp != null) {
			// remove temporary file
			temp.delete();
		}
		if (pMonitor != null) {
			pMonitor.done();
		}
		if (parent != null) {
			// display result message
			int e = p.exitValue();
			MessageBox messageBox;
			if (e == 0) {
				messageBox = new MessageBox(parent, SWT.ICON_INFORMATION);
				messageBox.setMessage(Messages.OK_CONNECTED);
			} else {
				messageBox = new MessageBox(parent, SWT.ICON_ERROR);
				messageBox
						.setMessage(Messages.FAILURE_CANNOT_CONNECT + e + ")"); //$NON-NLS-1$
			}
			messageBox.open();
		}
	}

}
