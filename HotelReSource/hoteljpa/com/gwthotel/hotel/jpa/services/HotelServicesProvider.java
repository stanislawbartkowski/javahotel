package com.gwthotel.hotel.jpa.services;

import javax.inject.Provider;

import com.gwthotel.hotel.jpa.AbstractCrudProvider;
import com.gwthotel.hotel.services.IHotelServices;

public class HotelServicesProvider extends AbstractCrudProvider implements
        Provider<IHotelServices> {

    @Override
    public IHotelServices get() {
        return new HotelJpaServices(eFactory, iGen);
    }

}