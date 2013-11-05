package com.sg.business.work.view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.file.RemoteFileSet;
import com.mobnut.db.model.ModelService;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.sg.business.model.Document;
import com.sg.business.model.UserTask;
import com.sg.widgets.part.view.TreeNavigator;

public class WorkFlowWorkDelivery extends TreeNavigator {

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Tree tree = (Tree) navi.getControl();
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try {
						String _data = event.text.substring(
								event.text.lastIndexOf("/") + 1,
								event.text.indexOf("@"));
						String action = event.text.substring(event.text
								.indexOf("@") + 1);
						if ("downloadall".equals(action)) {
							Document doc = ModelService.createModelObject(
									Document.class, new ObjectId(_data));
							download(doc);
						} else if("download".equals(action)){
							_data = URLDecoder.decode(_data, "utf-8");
							DBObject dbo = (DBObject) JSON.parse(_data);
							String db = (String) dbo.get("d");
							String namespace = (String) dbo.get("n");
							String oid = (String) dbo.get("o");
							String fileName = (String) dbo.get("a");
							FileUtil.download(getViewSite().getShell(),db, namespace, oid, fileName);
						}
						doRefresh();
					} catch (Exception e) {
					}
				}
			}
		});
	}

	protected void download(Document doc) {
		try {
			BufferedInputStream origin = null;

			String pathname = System.getProperty("user.dir") + "/temp"; //$NON-NLS-1$ //$NON-NLS-2$
			File tempFolder = new File(pathname);
			if (!tempFolder.isDirectory()) {
				tempFolder.mkdir();
			}

			String zipFileName = pathname + "/" //$NON-NLS-1$
					+doc.getDesc() + ".zip"; //$NON-NLS-1$
			FileOutputStream dest = new FileOutputStream(zipFileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest), Charset.forName("GBK")); //$NON-NLS-1$

			byte data[] = new byte[2048];

			Set<String> fileNameSet = new HashSet<String>();
			RemoteFileSet fileSet = doc.getVault();
			List<RemoteFile> fileList = fileSet.getUpdatedRemoteFileSet();
			for (int i = 0; i < fileList.size(); i++) {
				RemoteFile rf = fileList.get(i);
				String fileName;
				InputStream is;
				if (rf.getStatus() == RemoteFile.FILE_IN_DB) {
					String oid = rf.getObjectId();
					String namespace = rf.getNamespace();
					fileName = rf.getFileName();
					String dbName = rf.getDbName();
					DB db = DBActivator.getDB(dbName);
					is = FileUtil.getInputSteam(namespace, oid, db);
				} else if (rf.getStatus() == RemoteFile.FILE_UPLOADED) {
					File file = rf.getServerFile();
					is = new FileInputStream(file);
					fileName = file.getName();
				} else {
					continue;
				}

				fileName = checkName(fileName, fileNameSet);
				origin = new BufferedInputStream(is, 2048);
				ZipEntry entry = new ZipEntry(fileName);
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, 2048)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
			out.close();

			File zipFile = new File(zipFileName);
			if (zipFile.isFile()) {
				FileUtil.download(getSite().getShell(), zipFile, zipFile.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String checkName(String fileName, Set<String> fileNameSet) {
		fileName = fileName.replaceAll("/", "_"); //$NON-NLS-1$ //$NON-NLS-2$
		int i = 1;
		while (fileNameSet.contains(fileName)) {
			fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "(" //$NON-NLS-1$ //$NON-NLS-2$
					+ (i++) + ")" //$NON-NLS-1$
					+ fileName.substring(fileName.lastIndexOf(".")); //$NON-NLS-1$
		}
		fileNameSet.add(fileName);
		return fileName;
	}

	@Override
	protected void updatePartName(IWorkbenchPart part) {
		if (master != null) {
			String workname = ((UserTask) master).getWorkName();
			setPartName(workname + " ÎÄµµ");
		} else {
			setPartName(originalPartName);
		}
	}

	@Override
	public boolean canCreate() {
		return false;
	}

	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public boolean canEdit() {
		return false;
	}

	@Override
	public boolean canRefresh() {
		return super.canRefresh();
	}

	@Override
	public boolean canRead() {
		IStructuredSelection selection = navi.getViewerControl().getSelection();
		if(selection!=null&&!selection.isEmpty()){
			Object selected = selection.getFirstElement();
			return selected instanceof Document;
		}
		return false;
	}

}
