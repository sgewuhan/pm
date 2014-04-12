package com.sg.business.vault.handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bson.types.ObjectId;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.file.RemoteFileSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Container;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.OrganizationDistributeFileBase;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.fileupload.FileDialog;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class UploadZIP extends AbstractNavigatorHandler {

	private static String ROOT = "*root*";
	private static String SERVICE_PATH = "D:/";

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();
		FileDialog fileDialog = new FileDialog(shell, SWT.MULTI | SWT.NO_TRIM);
		fileDialog.open();
		String[] serverFileNames = fileDialog.getFileNames();
		String[] clientFileNames = fileDialog.getClientFileNames();
		if (serverFileNames == null || serverFileNames.length == 0
				|| clientFileNames == null || clientFileNames.length == 0) {
			return;
		}
		try {
			for (String path : serverFileNames) {
				IContext context = new CurrentAccountContext();
				Organization org = null;
				Folder folder = null;
				if (selected instanceof Container) {
					Container container = (Container) selected;
					ObjectId root_id = container.get_id();
					org = ModelService.createModelObject(Organization.class,
							root_id, false);
					File file = new File(path);
					String name = file.getName();
					folder = createFolder(org, context, null, name);
				} else if (selected instanceof Folder) {
					folder = (Folder) selected;
					ObjectId root_id = folder.getRoot_id();
					org = ModelService.createModelObject(Organization.class,
							root_id, false);
				}

				if (org != null && folder != null) {
					readZIP(org, path, context, folder);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		vc.doReloadData();
	}

	private void readZIP(Organization org, String zipPath, IContext context,
			Folder folder) throws Exception {
		InputStream in = new BufferedInputStream(new FileInputStream(zipPath));
		Charset charset = Charset.forName("GBK");
		ZipInputStream zin = new ZipInputStream(in, charset);
		ZipEntry ze = zin.getNextEntry();
		Map<String, ObjectId> folderMap = new HashMap<String, ObjectId>();
		folderMap.put(ROOT, folder.get_id());
		while (ze != null) {
			if (ze.isDirectory()) {
				String name = ze.getName();
				name = name.substring(0, name.length() - 1);
				String[] paths = name.split("/");
				initFolder(org, context, folderMap, paths);

			} else {
				long size = ze.getSize();
				if (size > 0) {
					String name = ze.getName();
					initDocument(org, context, folderMap, name, zipPath, zin);
				}
			}
			ze = zin.getNextEntry();
		}
		zin.closeEntry();
	}

	private ObjectId initFolder(Organization org, IContext context,
			Map<String, ObjectId> folderMap, String[] paths) throws Exception {
		ObjectId folder_id = null;
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			folder_id = folderMap.get(path);
			if (folder_id == null) {
				if (i > 0) {
					folder_id = folderMap.get(paths[i - 1]);
				} else {
					folder_id = folderMap.get(ROOT);
				}
				folder_id = createFolder(org, context, folderMap, folder_id,
						path);
			}
		}
		return folder_id;
	}

	private void initDocument(Organization org, IContext context,
			Map<String, ObjectId> folderMap, String name, String zipPath,
			ZipInputStream zin) throws Exception {
		zipPath = zipPath + "/" + name;
		String[] paths = name.split("/");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < paths.length - 1; i++) {
			list.add(paths[i]);
		}
		ObjectId folder_id = null;
		if (list.size() == 0) {
			folder_id = folderMap.get(ROOT);
		} else {
			folder_id = initFolder(org, context, folderMap,
					list.toArray(new String[0]));
		}
		if (folder_id != null) {
			createDocument(org, context, folder_id, paths[paths.length - 1],
					zin);
		}
	}

	private void createDocument(Organization org, IContext context,
			ObjectId folder_id, String name, ZipInputStream zin)
			throws Exception {

		Folder folder = ModelService.createModelObject(Folder.class, folder_id);
		Document doc = folder.makeCreateDocument(context);
		doc.setValue(Document.F_DESC, name);
		doc.doInsert(context);

		List<RemoteFile> originalRFiles = doc
				.getGridFSFileValue(Document.F_VAULT);
		OrganizationDistributeFileBase filebase = new OrganizationDistributeFileBase();
		String fileDB = filebase.getDB(doc);
		String namespace = filebase.getNamespace(doc);
		RemoteFileSet fileSet = new RemoteFileSet(fileDB, namespace);
		fileSet.setOriginalRemoteFileSet(originalRFiles);
		fileSet.setUpdatedRemoteFileSet(originalRFiles);

		File file = new File(SERVICE_PATH + name);
		BufferedOutputStream output = new BufferedOutputStream(
				new FileOutputStream(file));
		byte[] datas = new byte[2048];
		int count;
		while ((count = zin.read(datas)) != -1) {
			output.write(datas, 0, count);
		}
		output.close();

		RemoteFile rf = RemoteFile.createEmptyRemoteFile(fileDB, namespace);
		rf.setFileUploaded(name, file, new ObjectId());
		fileSet.add(rf, context);

		doc.reload();
		fileSet.saveServerFileToDB();
		doc.setValue(Document.F_VAULT, fileSet.getUpdatedData(), null, true);
		doc.doSave(context);
		file.delete();
	}

	private ObjectId createFolder(Organization org, IContext context,
			Map<String, ObjectId> folderMap, ObjectId parent_id, String name)
			throws Exception {
		Folder folder = createFolder(org, context, parent_id, name);
		folderMap.put(name, folder.get_id());
		return folder.get_id();
	}

	private Folder createFolder(Organization org, IContext context,
			ObjectId parent_id, String name) throws Exception {
		BasicDBObject folderData = new BasicDBObject();
		folderData.put(Folder.F_DESC, name);
		String containerCollection, containerDB;
		containerCollection = IModelConstants.C_ORGANIZATION;
		Container container = Container.adapter(org,
				Container.TYPE_ADMIN_GRANTED);
		containerDB = (String) container.getValue(Container.F_SOURCE_DB);
		folderData.put(Folder.F_CONTAINER_DB, containerDB);
		folderData.put(Folder.F_CONTAINER_COLLECTION, containerCollection);
		folderData.put(Folder.F_ROOT_ID, org.get_id());
		folderData.put(Folder.F_PARENT_ID, parent_id);
		Folder folder = ModelService
				.createModelObject(folderData, Folder.class);
		folder.doSave(context);
		return folder;
	}
}
