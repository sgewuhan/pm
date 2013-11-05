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
import com.tmt.pdm.client.Starter;

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
		
		Commons.LOGGER.info("Import Finished.");

	}

	/***
	 * 
	 * @param classOuid
	 * @throws Exception
	 */
	private void run(String classOuid) throws Exception {
		ArrayList result = Starter.dos.list(classOuid);
		for (int i = 0; i < result.size(); i++) {
			ArrayList item = (ArrayList) result.get(i);
			String ouid = (String) item.get(0);
			syncItem(ouid,null);
		}
	}

	public void syncItem(String ouid,Document doc) throws Exception {
		DOSChangeable doso = Starter.dos.get(ouid);
		HashMap valueMap = doso.getValueMap();

		BasicDBObject data = new BasicDBObject();
		data.put(Document.F_DOCUMENT_NUMBER, doso.get("md$number"));
		data.put(Document.F_DESC, doso.get("md$description"));
		data.put("pdm_ouid", ouid);
		transferPDMField(valueMap, data);

		if(doc==null){
			doc = ModelService.createModelObject(data, Document.class);
			doc.setValue(Document.F__ID, new ObjectId());
		}
		ArrayList files = Starter.dos.listFile(ouid);
		HashMap tempMap;
		if (files == null) {
			return;
		}
		
		BasicBSONList fileList = new BasicBSONList();
		for (int j = 0; j < files.size(); j++) {
			tempMap = (HashMap) files.get(j); // a row of search result
			if (tempMap == null)
				continue;
			DBObject ref = syncFile( tempMap);
			fileList.add(ref);
		}
		
		doc.setValue(Document.F_VAULT, fileList);
		doc.setValue(Document.F_FOLDER_ID, getFolderId());
		doc.doSave(new BackgroundContext());

		Commons.LOGGER.info("Import Successs."+doso);
	}

	protected ObjectId getFolderId(){
		return null;
	}

	private void transferPDMField(HashMap valueMap, DBObject data) {
		Iterator iter = valueMap.keySet().iterator();

		while (iter.hasNext()) {
			String key = (String) iter.next();
			String field = key.replaceAll("\\@", "_");
			field = field.replaceAll("\\$", "_");
			field = field.replaceAll("\\.", "_");
			data.put("pdm_"+field, valueMap.get(key));
		}
	}

	private DBObject syncFile( HashMap tempMap)
			throws IIPRequestException, FileNotFoundException {
		// String fileTypeId = (String) tempMap.get("md$filetype.id");
		 String fileTypeDescription = (String) tempMap.get("md$description");
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

		final String path = Starter.dss.getLocalPath(filePath);
		File file = new File(fileTypeDescription);
		final DB db = getDB();
		final String fileName = file.getName();
		final ObjectId fid = new ObjectId();

		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					FileInputStream in = new FileInputStream(path);
					FileUtil.upload(in,fid, fileName, getNamespace(), db);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		t.start();

		RemoteFile rf = RemoteFile.createEmptyRemoteFile(db.getName(),
				getNamespace());
		rf.setFileName(fileName);
		DBObject refData = rf.getOutputRefData();
		refData.put("_id", fid);
		transferPDMField(tempMap, refData);
		return refData;
	}

	protected abstract String getNamespace();

	protected abstract DB getDB();

	protected abstract String getClassOuid();

}
