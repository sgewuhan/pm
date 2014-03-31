package com.tmt.pdm.dcpdm;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.commons.util.service.DownloadServiceHandler;
import com.tmt.pdm.client.Starter;

public class DCPDMFSDownloadServiceHandler extends DownloadServiceHandler {
	public static final String ID = "DCPDMFSDownloadServiceHandler";

	@Override
	protected byte[] getBytes(HttpServletRequest request) {
		String path = request.getParameter("dsspath"); //$NON-NLS-1$

		try {
			InputStream is;
			is = Starter.dss.getInputStream(path);
			long size = Starter.dss.getFileSize(path);
			return FileUtil.getBytes(is,size);
		} catch (Exception e) {
		}
		return null;
	}
	
	
}