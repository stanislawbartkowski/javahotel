package com.javahotel.db.copy;

public class PattGetSymParam {
	
	private final String pId;
	private final String patt;
	
	public PattGetSymParam(String pId,String pAtt) {
		this.pId = pId;
		this.patt = pAtt;
	}

	public String getPId() {
		return pId;
	}

	public String getPatt() {
		return patt;
	}

}
