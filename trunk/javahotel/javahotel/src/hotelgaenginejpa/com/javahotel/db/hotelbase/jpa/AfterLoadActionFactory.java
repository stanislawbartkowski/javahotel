package com.javahotel.db.hotelbase.jpa;

import com.javahotel.dbjpa.ejb3.IAfterBeforeLoadAction;

public class AfterLoadActionFactory {

	private AfterLoadActionFactory() {

	}

	public static IAfterBeforeLoadAction getAction() {
		return new AfterBeforeLoadAction();
	}

}
