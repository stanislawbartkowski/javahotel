package com.gwthotel.admintest.guice;

import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.auth.IRealmResources;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelAuthResources implements IRealmResources {

    @Override
    public IHotelAdmin getAdmin() {
        return ServiceInjector.constructHotelAdmin();
    }

    @Override
    public IGetLogMess getLogMess() {
        return ServiceInjector.getLogMess();
    }


}
