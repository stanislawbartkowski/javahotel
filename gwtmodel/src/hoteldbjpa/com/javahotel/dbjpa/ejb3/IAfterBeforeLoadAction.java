package com.javahotel.dbjpa.ejb3;

import com.javahotel.dbutil.log.GetLogger;

public interface IAfterBeforeLoadAction {

	void afterLoadAction(GetLogger log, JpaEntity jpa, Object o);
	void beforePersistAction(GetLogger log, JpaEntity jpa, Object o);

}
