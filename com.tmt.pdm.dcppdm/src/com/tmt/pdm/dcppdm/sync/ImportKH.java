package com.tmt.pdm.dcppdm.sync;

import com.mobnut.db.DBActivator;
import com.mongodb.DB;

public class ImportKH extends ImportData {

	@Override
	protected String getClassOuid() {
		return "863f5d12";//asDocument
	}

	@Override
	protected String getNamespace() {
		return "vault";
	}

	@Override
	protected DB getDB() {
		return DBActivator.getDB("kh");
	}
	
}
