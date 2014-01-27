package com.sg.business.vault.handler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.bson.types.ObjectId;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Container;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class UploadZIP extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		String path = "D:/dssroot/pm2.zip";
		try {
			IContext context = new CurrentAccountContext();
			Organization org = null;
			if (selected instanceof Container) {
				Container container = (Container) selected;
				ObjectId root_id = container.get_id();
				org = ModelService.createModelObject(Organization.class,
						root_id, false);
			} else if (selected instanceof Folder) {
				Folder folder = (Folder) selected;
				ObjectId root_id = folder.getRoot_id();
				org = ModelService.createModelObject(Organization.class,
						root_id, false);
			}

			if (org != null) {
				readZIP(org, path, context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unused", "resource" })
	private void readZIP(Organization org, String zipPath, IContext context)
			throws Exception {
		ZipFile zf = new ZipFile(zipPath);
		InputStream in = new BufferedInputStream(new FileInputStream(zipPath));
		Charset charset = Charset.forName("GBK");
		ZipInputStream zin = new ZipInputStream(in, charset);
		ZipEntry ze = zin.getNextEntry();
		Map<String, ObjectId> folderMap = new HashMap<String, ObjectId>();
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
					initDocument(org, context, folderMap, name);
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
				}
				folder_id = createFolder(org, context, folderMap, folder_id,
						path);
			}
		}
		return folder_id;
	}

	private void initDocument(Organization org, IContext context,
			Map<String, ObjectId> folderMap, String name) throws Exception {
		String[] paths = name.split("/");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < paths.length - 1; i++) {
			list.add(paths[i]);
		}
		ObjectId folder_id = initFolder(org, context, folderMap,
				list.toArray(new String[0]));
		createDocument(org, context, folder_id, paths[paths.length - 1]);
	}

	private void createDocument(Organization org, IContext context,
			ObjectId folder_id, String name) {

	}

	private ObjectId createFolder(Organization org, IContext context,
			Map<String, ObjectId> folderMap, ObjectId parent_id, String name)
			throws Exception {
		BasicDBObject folderData = new BasicDBObject();
		folderData.put(Folder.F_DESC, name);
		ObjectId folderRootId = new ObjectId();
		folderData.put(Folder.F__ID, folderRootId);
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
		folder.doInsert(context);
		folderMap.put(name, folder.get_id());
		return folder.get_id();
	}
}
