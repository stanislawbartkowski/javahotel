package com.gwthotel.hotel.jpa.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelServicesProvider implements Provider<IHotelServices> {

    @Inject
    private ITransactionContextFactory eFactory;

    @Inject
    @Named(IHotelConsts.MESSNAMED)
    private IGetLogMess lMess;

    @Override
    public IHotelServices get() {
        return new HotelJpaServices(eFactory, lMess);
    }

}