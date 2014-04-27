package com.gwthotel.hotel.server.guice;

import com.gwthotel.auth.IRealmResources;
import com.gwthotel.hotel.server.service.H;
import com.jython.serversecurity.IOObjectAdmin;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;

class HotelAuthResources implements IRealmResources {

    @Override
    public IOObjectAdmin getAdmin() {
        return Holder.getAdmin();
    }

    @Override
    public IGetLogMess getLogMess() {
        return H.getL();
    }

}
