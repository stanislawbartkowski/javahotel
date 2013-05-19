package com.gwthotel.admin.jpa;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.persistence.EntityManagerFactory;

import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class TestHotelAdminProvider implements Provider<IHotelAdmin> {

    @Inject
    @Named(IHotelConsts.TESTFACTORYMANAGER)
    private EntityManagerFactory eFactory;

    @Inject
    @Named(IHotelConsts.MESSNAMED)
    private IGetLogMess lMess;

    @Override
    public IHotelAdmin get() {
        return new HotelAdminJpa(eFactory, lMess);
    }

}
