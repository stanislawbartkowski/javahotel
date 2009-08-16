package com.javahotel.db.context;

import com.javahotel.db.jtypes.IId;

public interface IPersistCache {

	void clear();

	void put(Class<?> cla, String name, IId id,boolean persist);

	IId get(Class<?> cla, String name);

	void put(Class<?> parent_cla, String parentname, Class<?> cla, String name,
			IId id, boolean persist);

	IId get(Class<?> parent_cla, String parentname, Class<?> cla, String name);

	void persistRecords(ICommandContext iC);

}
