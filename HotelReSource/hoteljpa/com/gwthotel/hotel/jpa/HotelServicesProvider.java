package com.gwthotel.hotel.jpa;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.persistence.EntityManagerFactory;

import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.gwthotel.hotel.services.IHotelServices;

public class HotelServicesProvider implements Provider<IHotelServices> {

    @Inject
    private EntityManagerFactory eFactory;

    @Inject
    @Named(IHotelConsts.MESSNAMED)
    private IGetLogMess lMess;

    @Override
    public IHotelServices get() {
        return new HotelJpaServices(eFactory, lMess);
    }

}