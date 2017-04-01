package com.jythonui.server.dict;

import java.math.BigDecimal;

import com.jythonui.server.ISharedConsts;

public class VatTax extends DictEntry {

    private static final long serialVersionUID = 1L;

    public BigDecimal getVatLevel() {
        return getAttrBig(ISharedConsts.VATLEVELPROP);
    }

}
