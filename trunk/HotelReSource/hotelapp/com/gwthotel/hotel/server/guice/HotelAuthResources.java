package com.gwthotel.hotel.server.guice;

import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.auth.IRealmResources;
import com.gwthotel.hotel.server.service.H;
import com.jythonui.server.getmess.IGetLogMess;

class HotelAuthResources implements IRealmResources {

    @Override
    public IHotelAdmin getAdmin() {
        return H.getHotelAdmin();
    }

    @Override
    public IGetLogMess getLogMess() {
        return H.getL();
    }


}
