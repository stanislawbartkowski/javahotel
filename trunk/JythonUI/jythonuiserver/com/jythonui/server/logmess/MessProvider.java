package com.jythonui.server.logmess;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Provider;

import com.jythonui.server.getmess.GetLogMessFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.ICommonConsts;

public class MessProvider implements Provider<IGetLogMess> {

    private static final String fileName = ICommonConsts.RESOURCES
            + "/mess/mess.properties";

    private Properties mess = null;

    private void readProp() {
        if (mess != null)
            return;
        mess = new Properties();
        InputStream i = MessProvider.class.getClassLoader()
                .getResourceAsStream(fileName);
        try {
            mess.load(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IGetLogMess get() {
        readProp();
        return GetLogMessFactory.construct(mess);
    }

}
