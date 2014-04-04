package com.tmt.pdm.dcpdm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobnut.commons.util.file.OSServerFile;
import com.mobnut.db.file.IServerFile;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.IFileServerDelegator;
import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.handler.DCPDMUtil;

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

		for (int j = 0; j < files.size(); j++) {
			tempMap = (HashMap) files.get(j); // a row of search result
			if (tempMap == null)
				continue;
			try {
				String fileName = getFileName(tempMap);
				String filePath = getFilePath(tempMap);
				final String dssPath = (String) tempMap.get("md$path"); // server side //$NON-NLS-1$
				OSServerFile ref = new OSServerFile(filePath, fileName) {
					@Override
					public BufferedInputStream read() {
						InputStream is;
						try {
							is = Starter.dss.getInputStream(dssPath);
							return new BufferedInputStream(is, 2048);
						} catch (Exception e) {
						}
						return null;
					}
				};
				result.add(ref);
			} catch (IIPRequestException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private String getFilePath(HashMap tempMap) throws IIPRequestException {
		String filePath = (String) tempMap.get("md$path"); // server side //$NON-NLS-1$
		return Starter.dss.getLocalPath(filePath);
	}

	private String getFileName(HashMap tempMap) {
		String fileTypeDescription = (String) tempMap.get("md$description"); //$NON-NLS-1$
		File file = new File(fileTypeDescription);
		return file.getName();
	}

	@Override
	public boolean doSetLifeCycleStatus(PrimaryObject document, String status) {
		if (document != null) {
			String ouid = ((Document) document).getPDMOuid();
			return DCPDMUtil.doSetLifeCycleStatus(ouid, status);
		}
		return false;
	}

	@Override
	public boolean doSetRev(PrimaryObject document) {
		if (document != null) {
			String ouid = ((Document) document).getPDMOuid();
			return DCPDMUtil.doSetRev(ouid,
					((Document) document).getRevId());
		}
		return false;
	}

	@Override
	public boolean setUpdateVersion(PrimaryObject document, String status) {
		if (document != null) {
			String ouid = ((Document) document).getPDMOuid();
			String rev = ((Document) document).getRevId();
			return DCPDMUtil.doSetUpdateVersion(ouid, status, rev);
		}
		return false;
	}

}
