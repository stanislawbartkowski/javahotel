package com.mygwt.server;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwtmodel.table.mailcommon.CListOfMailProperties;
import com.gwtmodel.table.mailcommon.CMailToSend;
import com.gwtmodel.util.FileUtil;
import com.gwtmodel.util.SendMail;
import com.mygwt.client.MyRemoteService;

@SuppressWarnings("serial")
public class MyRemoteServiceImpl extends RemoteServiceServlet implements
		MyRemoteService {

	@Override
	public CListOfMailProperties getListOfMailBoxes() {
		File f = FileUtil.getResourceDir(this.getClass());
		FileFilter filter = new FileFilter() {

			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return false;
				}
				return pathname.getName().endsWith(".properties");
			}
		};
		File[] dir = f.listFiles(filter);
		CListOfMailProperties cma = new CListOfMailProperties();
		if ((dir == null) || (dir.length == 0)) {
			cma.setErrMess("No mail boxes defined ");
			return cma;
		}
		for (int i = 0; i < dir.length; i++) {
			Properties pro = new Properties();
			try {
				pro.load(new FileReader(dir[i]));
			} catch (FileNotFoundException e) {
				cma.setErrMess(e.getMessage());
				return cma;
			} catch (IOException e) {
				cma.setErrMess(e.getMessage());
				return cma;
			}
			Map<String, String> ma = FileUtil.PropertyToMap(pro);
			ma.put(SendMail.PROP_FILE_NAME, dir[i].getAbsolutePath());
			cma.getmList().add(ma);
		}
		return cma;
	}

	@Override
	public String sendMail(CMailToSend mail) {
		return SendMail.postMail(mail.isText(),
				FileUtil.MapToProperty(mail.getBox()), mail.getTo(),
				mail.getHeader(), mail.getContent(), mail.getFrom());
	}

}
