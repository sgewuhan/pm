package com.tmt.pdm.dcpdm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bson.types.BasicBSONList;

import com.mobnut.commons.util.file.OSServerFile;
import com.mobnut.db.file.IServerFile;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.IFileServerDelegator;
import com.tmt.pdm.client.Starter;

import dyna.framework.iip.IIPRequestException;

@SuppressWarnings("rawtypes")
public class FileServerDelegator implements IFileServerDelegator {

	@Override
	public List<IServerFile> getFiles(PrimaryObject document) {
		List<IServerFile> result = new ArrayList<IServerFile>();
		String pdmOuid = ((Document) document).getPDMOuid();

		ArrayList files = null;
		try {
			files = Starter.dos.listFile(pdmOuid);
		} catch (IIPRequestException e) {
			e.printStackTrace();
		}
		HashMap tempMap;
		if (files == null) {
			return result;
		}

		BasicBSONList fileList = new BasicBSONList();
		for (int j = 0; j < files.size(); j++) {
			tempMap = (HashMap) files.get(j); // a row of search result
			if (tempMap == null)
				continue;
			try {
				String fileName = getFileName(tempMap);
				String filePath;
				filePath = getFilePath(tempMap);
				OSServerFile ref = new OSServerFile(filePath, fileName);
				result.add(ref);
			} catch (IIPRequestException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private String getFilePath(HashMap tempMap) throws IIPRequestException {
		String filePath = (String) tempMap.get("md$path"); // server side
		return Starter.dss.getLocalPath(filePath);
	}

	private String getFileName(HashMap tempMap) {
		String fileTypeDescription = (String) tempMap.get("md$description");
		File file = new File(fileTypeDescription);
		return file.getName();
	}

}
