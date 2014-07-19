package com.jythonui.server.authrealm;

import com.jython.serversecurity.IOObjectAdmin;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.objectauth.IRealmResources;

class ObjectAuthResources implements IRealmResources {

    @Override
    public IOObjectAdmin getAdmin() {
        return Holder.getAdmin();
    }

    @Override
    public IGetLogMess getLogMess() {
        return Holder.getM();
    }

}
