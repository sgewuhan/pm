package com.tmt.pdm.dcppdm.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.commons.Commons;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.sg.business.model.Document;
import com.sg.widgets.part.BackgroundContext;
import com.tmt.pdm.dcppdm.DCPDM;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.dos.DOSChangeable;

@SuppressWarnings("rawtypes")
public abstract class ImportData implements Runnable {

	public ImportData() {

	}

	@Override
	public void run() {
		String classOuid = getClassOuid();
		try {
			run(classOuid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * @param classOuid
	 * @throws Exception
	 */
	private void run(String classOuid) throws Exception {
		ArrayList result = DCPDM.dos.list(classOuid);
		for (int i = 0; i < result.size(); i++) {
			ArrayList item = (ArrayList) result.get(i);
			String ouid = (String) item.get(0);
			syncItem(ouid);
		}
	}

	private void syncItem(String ouid) throws Exception {
		DOSChangeable doso = DCPDM.dos.get(ouid);
		HashMap valueMap = doso.getValueMap();

		BasicDBObject data = new BasicDBObject();
		data.put(Document.F_DOCUMENT_NUMBER, doso.get("md$number"));
		data.put(Document.F_DESC, doso.get("md$description"));
		data.put("pdm_ouid", ouid);
		transferPDMField(valueMap, data);

		Document doc = ModelService.createModelObject(data, Document.class);
		doc.setValue(Document.F__ID, new ObjectId());
		ArrayList files = DCPDM.dos.listFile(ouid);
		HashMap tempMap;
		if (files == null) {
			return;
		}
		
		BasicBSONList fileList = new BasicBSONList();
		for (int j = 0; j < files.size(); j++) {
			tempMap = (HashMap) files.get(j); // a row of search result
			if (tempMap == null)
				continue;
			DBObject ref = syncFile(doc, tempMap);
			fileList.add(ref);
		}
		
		doc.setValue(Document.F_VAULT, fileList);

		doc.doInsert(new BackgroundContext());

		Commons.LOGGER.info("Import Successs."+doso);
	}

	private void transferPDMField(HashMap valueMap, DBObject data) {
		Iterator iter = valueMap.keySet().iterator();

		while (iter.hasNext()) {
			String key = (String) iter.next();
			String field = key.replaceAll("\\@", "_");
			field = field.replaceAll("\\$", "_");
			field = field.replaceAll("\\.", "_");
			data.put(field, valueMap.get(key));
		}
	}

	private DBObject syncFile(Document doc, HashMap tempMap)
			throws IIPRequestException, FileNotFoundException {
		// String fileTypeId = (String) tempMap.get("md$filetype.id");
		// String fileTypeDescription = (String) tempMap.get("md$description");
		// // local
		// // full
		// // file
		// // path
		// // stored.
		// String fileVersion = (String) tempMap.get("md$version");
		String filePath = (String) tempMap.get("md$path"); // server side file
															// path. NOT for
															// // client.
		// String checkOutTime = (String) tempMap.get("md$checkedout.date");
		// String checkInTime = (String) tempMap.get("md$checkedin.date");
		// String checkInUserId = (String) tempMap.get("md$user.id");
		// String checkInUserName = (String) tempMap.get("md$user.name");

		String path = DCPDM.dss.getLocalPath(filePath);
		FileInputStream in = new FileInputStream(path);
		File file = new File(filePath);
		DB db = getDB();
		String fileName = file.getName();
		FileUtil.upload(in, fileName, getNamespace(), db);

		RemoteFile rf = RemoteFile.createEmptyRemoteFile(db.getName(),
				getNamespace());
		rf.setFileName(fileName);
		DBObject refData = rf.getOutputRefData();
		transferPDMField(tempMap, refData);
		return refData;
	}

	protected abstract String getNamespace();

	protected abstract DB getDB();

	protected abstract String getClassOuid();

}
