package com.tmt.pdm.dcpdm.sync;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mongodb.DB;

public class ImportKHBackup extends ImportData {

	@Override
	protected String getClassOuid() {
		return "863f5d12";//asDocument //$NON-NLS-1$
	}

	@Override
	protected String getNamespace() {
		return "vault_file"; //$NON-NLS-1$
	}

	@Override
	protected DB getDB() {
		return DBActivator.getDB("kh"); //$NON-NLS-1$
	}

	@Override
	protected ObjectId getFolderId() {
		return new ObjectId("526f302029f260085cc7491d"); //$NON-NLS-1$
	}
	
}
