package com.sg.business.commons.link;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import com.mobnut.commons.util.Office2PDFJob;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.file.RemoteFileSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.sg.business.model.Document;
import com.sg.business.model.OrganizationDistributeFileBase;
import com.sg.widgets.fileupload.FileDialog;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class DeliveryLinkAdapter implements SelectionListener {

	public DeliveryLinkAdapter() {
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		if (event.detail == RWT.HYPERLINK) {
			try {
				String action = event.text.substring(
						event.text.lastIndexOf("/") + 1, //$NON-NLS-1$
						event.text.indexOf("@")); //$NON-NLS-1$
				String _data = event.text
						.substring(event.text.indexOf("@") + 1); //$NON-NLS-1$
				if ("downloadall".equals(action)) { //$NON-NLS-1$
					doZipDownload(_data, event);
				} else if ("download".equals(action)) { //$NON-NLS-1$
					doDownload(_data, event);
				} else if ("upload".equals(action)) { //$NON-NLS-1$
					doUpload(_data, event);
				}
			} catch (Exception e) {
			}
		}
	}

	private void doUpload(String _data, SelectionEvent event) throws Exception {
		Document doc = ModelService.createModelObject(Document.class,
				new ObjectId(_data));
		List<RemoteFile> originalRFiles = doc
				.getGridFSFileValue(Document.F_VAULT);

		OrganizationDistributeFileBase filebase = new OrganizationDistributeFileBase();

		String fileDB = filebase.getDB(doc);
		String namespace = filebase.getNamespace(doc);
		RemoteFileSet fileSet = new RemoteFileSet(fileDB, namespace);
		fileSet.setOriginalRemoteFileSet(originalRFiles);
		fileSet.setUpdatedRemoteFileSet(originalRFiles);

		// 显示文件上传对话框
		TreeItem item = (TreeItem) event.item;
		// item.getBounds();
//		final Point point = item.getParent().toDisplay(item.getBounds().x,
//				item.getBounds().y + item.getBounds().height);

		FileDialog fileDialog = new FileDialog(getShell(), SWT.MULTI
				| SWT.NO_TRIM) ;

		fileDialog.open();
		String[] serverFileNames = fileDialog.getFileNames();
		String[] clientFileNames = fileDialog.getClientFileNames();
		if (serverFileNames == null || serverFileNames.length == 0
				|| clientFileNames == null || clientFileNames.length == 0) {
			return;
		}
		CurrentAccountContext context = new CurrentAccountContext();
		for (int i = 0; i < serverFileNames.length; i++) {
			File file = new File(serverFileNames[i]);
			RemoteFile rf = RemoteFile.createEmptyRemoteFile(fileDB, namespace);
			rf.setFileUploaded(clientFileNames[i], file, new ObjectId());
			fileSet.add(rf, context);
			createPreview(rf, fileDialog.getParent().getDisplay());
		}
		updateFileSetValue(doc, fileSet, context);
		Tree tree = item.getParent();
		ViewerControl vc = (ViewerControl) tree.getData("navi");
		vc.doReloadData();
	}

	private void updateFileSetValue(Document doc, RemoteFileSet fileSet,
			IContext context) throws Exception {
		doc.reload();
		fileSet.saveServerFileToDB();
		doc.setValue(Document.F_VAULT, fileSet.getUpdatedData(), null, true);
		doc.doSave(context);
	}

	private void doDownload(String _data, SelectionEvent event)
			throws UnsupportedEncodingException {
		_data = URLDecoder.decode(_data, "utf-8"); //$NON-NLS-1$
		DBObject dbo = (DBObject) JSON.parse(_data);
		String db = (String) dbo.get("d"); //$NON-NLS-1$
		String namespace = (String) dbo.get("n"); //$NON-NLS-1$
		String oid = (String) dbo.get("o"); //$NON-NLS-1$
		String fileName = (String) dbo.get("a"); //$NON-NLS-1$
		String filepath = (String) dbo.get("f"); //$NON-NLS-1$
		if (filepath == null) {
			FileUtil.download(getShell(), db, namespace, oid, fileName);
		} else {
			FileUtil.download(getShell(), filepath, fileName);
		}
	}

	private Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	protected void doZipDownload(String _data, SelectionEvent event)
			throws IOException {
		Document doc = ModelService.createModelObject(Document.class,
				new ObjectId(_data));
		BufferedInputStream origin = null;

		String pathname = System.getProperty("user.dir") + "/temp"; //$NON-NLS-1$ //$NON-NLS-2$
		File tempFolder = new File(pathname);
		if (!tempFolder.isDirectory()) {
			tempFolder.mkdir();
		}

		String zipFileName = pathname + "/" //$NON-NLS-1$
				+ doc.getDesc() + ".zip"; //$NON-NLS-1$
		FileOutputStream dest = new FileOutputStream(zipFileName);
		ZipOutputStream out = new ZipOutputStream(
				new BufferedOutputStream(dest), Charset.forName("GBK")); //$NON-NLS-1$

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
			FileUtil.download(getShell(), zipFile, zipFile.getName());
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

	private void createPreview(final RemoteFile rf, final Display display) {
		File serverFile = rf.getServerFile();
		int fileType = FileUtil.getFileType(serverFile);

		if (fileType == FileUtil.FILETYPE_OFFICE_FILE) {
			final ObjectId pvOid = new ObjectId();
			Office2PDFJob job = new Office2PDFJob();
			String previewPath = serverFile.getParent() + "/" //$NON-NLS-1$
					+ pvOid.toString() + ".pdf"; //$NON-NLS-1$
			job.setSourceFile(serverFile);
			final File previewFile = new File(previewPath);
			job.setTargetFile(previewFile);
			job.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {

					display.asyncExec(new Runnable() {

						@Override
						public void run() {
							rf.setPreviewUploaded(previewFile, pvOid);
						}
					});
				}
			});
			job.schedule(1000);
		} else {
		}
	}
}
