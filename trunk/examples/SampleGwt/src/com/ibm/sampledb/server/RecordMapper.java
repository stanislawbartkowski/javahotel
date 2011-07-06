package com.ibm.sampledb.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ibm.sampledb.shared.OneRecord;
import com.ibm.sampledb.shared.RowFieldInfo;

class RecordMapper implements RowMapper<OneRecord> {

	private final List<RowFieldInfo> fList;

	RecordMapper(List<RowFieldInfo> fList) {
		this.fList = fList;
	}

	private String getS(ResultSet res, int no) throws SQLException {
		String r = res.getString(no);
		if (r == null) {
			return null;
		}
		return r.trim();
	}

	@Override
	public OneRecord mapRow(ResultSet res, int arg1) throws SQLException {

		OneRecord el = new OneRecord();
		for (RowFieldInfo fie : fList) {
			int colPos = fie.getColPos();
			String field = fie.getfId();
			switch (fie.getfType()) {
			case INTEGER:
				Integer i = res.getInt(colPos);
				el.setField(field, i);
				break;
			case DATE:
				Timestamp dVal = res.getTimestamp(colPos);
				el.setField(field, dVal);
				break;
			case STRING:
				String val = getS(res, colPos);
				el.setField(field, val);
				break;
			case NUMBER:
				BigDecimal bVal = res.getBigDecimal(colPos);
				el.setField(field, bVal);
				break;
			}

		}
		return el;
	}
}
