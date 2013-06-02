package com.gwthotel.admin;

import java.math.BigDecimal;

import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;

public class VatTax extends PropDescription {

    private static final long serialVersionUID = 1L;

    public BigDecimal getVatLevel() {
        return getAttrBig(IHotelConsts.VATLEVELPROP);
    }

}
