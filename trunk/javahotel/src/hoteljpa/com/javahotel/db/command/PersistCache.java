package com.javahotel.db.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.context.IPersistCache;
import com.javahotel.db.jtypes.IId;

class PersistCache implements IPersistCache {

	class H {
		H(Class<?> parentcla, String parentname, Class<?> cla, String name,
				boolean persist) {
			this.cla = cla;
			this.name = name;
			this.parentcla = parentcla;
			this.parentname = parentname;
			this.persist = persist;
		}

		public boolean isPersist() {
			return persist;
		}

		private int hKey() {
			int ha = cla.getSimpleName().hashCode();
			ha += name.hashCode();
			if (parentcla != null) {
				ha += parentcla.getSimpleName().hashCode();
				ha += parentname.hashCode();
			}
			return ha;
		}

		@Override
		public boolean equals(Object o) {
			H h = (H) o;
			if (h.cla != this.cla) {
				return false;
			}
			if (!h.name.equals(this.name)) {
				return false;
			}
			if (h.parentcla != null) {
				if (h.parentcla != this.parentcla) {
					return false;
				}
				if (!h.parentname.equals(this.parentname)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public int hashCode() {
			return hKey();
		}

		private final Class<?> cla;
		private final String name;
		private final Class<?> parentcla;
		private final String parentname;
		private boolean persist;

		public void setPersist(boolean persist) {
			this.persist = persist;
		}
	}

	private Map<H, IId> h = new HashMap<H, IId>();

	public void clear() {
		h.clear();

	}

	public IId get(Class<?> cla, String name) {
		H ha = new H(null, null, cla, name, false);
		IId i = h.get(ha);
		return i;
	}

	public void put(Class<?> cla, String name, IId id, boolean persist) {
		H ha = new H(null, null, cla, name, persist);
		h.put(ha, id);

	}

	public IId get(Class<?> parentcla, String parentname, Class<?> cla,
			String name) {
		if (name == null) {
			return null;
		}
		H ha = new H(parentcla, parentname, cla, name, false);
		IId i = h.get(ha);
		return i;
	}

	public void put(Class<?> parentcla, String parentname, Class<?> cla,
			String name, IId id, boolean persist) {
		H ha = new H(parentcla, parentname, cla, name, persist);
		h.put(ha, id);
	}

	public void persistRecords(ICommandContext iC) {
		Set<H> k = h.keySet();
		Iterator<H> ite = k.iterator();
		while (ite.hasNext()) {
			H ha = ite.next();
			if (ha.isPersist()) {
				IId id = h.get(ha);
				iC.getJpa().changeRecord(id);
				ha.setPersist(false);
			}
		}
	}
}

// public List<IId> getAll(Class<?> cla) {
// List<IId> col = new ArrayList<IId>();
// List<IId> val = h.values();
// for (IId i : val) {
// if (i.getClass() == cla) {
// col.add(i);
// }
// }
// return col;
// }

