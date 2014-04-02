package com.sg.business.work.handler;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.IServerFile;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.operation.action.AbstractWorkDetailPageAction;
import com.sg.business.model.Document;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;

public class DownloadAllDocumentFiles extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work work, Control control) {
		List<PrimaryObject> documents = work.getDeliverableDocuments();
		HashMap<String, BufferedInputStream> fileList = new HashMap<>();
		for (PrimaryObject primaryObject : documents) {
			List<IServerFile> sflist = ((Document) primaryObject)
					.getServerFileValue();
			for (IServerFile iServerFile : sflist) {
				appendToFileList(iServerFile, fileList);
			}
		}
		if (fileList.isEmpty()) {
			MessageUtil.showToast("没有可下载的文件", SWT.ICON_WARNING);
			return;
		}
		String filename = fileList.keySet().iterator().next();
		FileUtil.download(filename.substring(0, filename.lastIndexOf(".")),
				fileList, control.getShell());
	}

	private void appendToFileList(IServerFile iServerFile,
			HashMap<String, BufferedInputStream> fileList) {
		String fileName = iServerFile.getFileName();
		Set<String> set = new HashSet<>();
		set.addAll(fileList.keySet());
		fileName = FileUtil.checkName(fileName, set);
		BufferedInputStream bis = iServerFile.read();
		if (bis == null) {
			return;
		}
		fileList.put(fileName, bis);
	}

}
