package com.tmt.pdm.dcpdm.handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Shell;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.IDeliverable;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.OrganizationDistributeFileBase;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;
import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.DCPDMFSDownloadServiceHandler;
import com.tmt.pdm.dcpdm.nls.Messages;
import com.tmt.pdm.dcpdm.sync.ImportData;

import dyna.framework.service.dos.DOSChangeable;

public class DCPDMUtil {
	public static String getDownloadURL(String path, String filename) {
		String handlerUrl = FileUtil.getServiceUrl(
				DCPDMFSDownloadServiceHandler.ID, //$NON-NLS-1$
				new DCPDMFSDownloadServiceHandler());
		StringBuilder url = new StringBuilder();
		url.append(handlerUrl);
		url.append("&dsspath="); //$NON-NLS-1$
		try {
			String fn = URLEncoder.encode(path, "utf-8"); //$NON-NLS-1$
			url.append(fn);
		} catch (UnsupportedEncodingException e) {
			url.append(path);
		}
		url.append("&filename="); //$NON-NLS-1$
		try {
			String fn = URLEncoder.encode(filename, "utf-8"); //$NON-NLS-1$
			url.append(fn);
		} catch (UnsupportedEncodingException e) {
			url.append(filename);
		}

		String encodedURL = RWT.getResponse().encodeURL(url.toString());
		return encodedURL;

	}

	@Deprecated
	private static boolean createDocumentFromDCPDM(Work work,
			List<?> docContainer, List<?> partContainer, Shell shell) {
		PDMObjectSelector selector = new PDMObjectSelector(shell, docContainer,
				partContainer);
		int ret = selector.open();
		if (ret == PDMObjectSelector.OK) {
			String[] sel = selector.getSelection();
			if (sel.length == 0) {
				return false;
			}

			for (int i = 0; i < sel.length; i++) {
				try {
					DCPDMUtil.createDocument(work, sel[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

	public static List<?> getDocumentAndDrawingContainerCode(String userId) {
		User user = UserToolkit.getUserById(userId);

		Organization org = user.getOrganization();

		while (org != null) {
			List<?> code = org.getDocumentAndDrawingContainerCode();
			if (code != null) {
				return (List<?>) code;
			} else {
				org = (Organization) org.getParentOrganization();
			}
		}
		return null;
	}

	public static List<?> getPartContainerCode(String userId) {
		User user = UserToolkit.getUserById(userId);

		Organization org = user.getOrganization();

		while (org != null) {
			List<?> code = org.getPartContainerCode();
			if (code != null) {
				return (List<?>) code;
			} else {
				org = (Organization) org.getParentOrganization();
			}
		}
		return null;
	}

	@Deprecated
	public static void createDocument(Work work, String ouid) throws Exception {
		IContext context = new CurrentAccountContext();

		// 取出PDM中的对象
		DOSChangeable pdmObject = Starter.dos.get(ouid);
		Object docNumber = pdmObject.get("md$number"); //$NON-NLS-1$
		Document document = null;
		if (!Utils.isNullOrEmptyString(docNumber)) {
			DBCollection docCol = DBActivator.getCollection(IModelConstants.DB,
					IModelConstants.C_DOCUMENT);
			DBObject docData = docCol.findOne(new BasicDBObject().append(
					Document.F_DOCUMENT_NUMBER, docNumber));
			if (docData != null) {
				document = ModelService.createModelObject(docData,
						Document.class);
			}
		}
		Deliverable deli;

		// 没有对应的PDM对象
		if (document == null) {
			deli = work.makeDeliverableDefinition(IDeliverable.TYPE_OUTPUT);
		} else {
			deli = work.makeDeliverableDefinition(IDeliverable.TYPE_LINK);
			deli.setValue(Deliverable.F_DOCUMENT_ID, document.get_id());
		}
		deli.setValue(Deliverable.F_DESC, pdmObject.get("md$description")); //$NON-NLS-1$
		deli.doSave(context);
		document = deli.getDocument();
		writePDMInfo(ouid, document);
	}
	
	@Deprecated
	public static void createDocument(Work work, String ouid, IContext context) throws Exception {

		// 取出PDM中的对象
		DOSChangeable pdmObject = Starter.dos.get(ouid);
		Object docNumber = pdmObject.get("md$number"); //$NON-NLS-1$
		Document document = null;
		if (!Utils.isNullOrEmptyString(docNumber)) {
			DBCollection docCol = DBActivator.getCollection(IModelConstants.DB,
					IModelConstants.C_DOCUMENT);
			DBObject docData = docCol.findOne(new BasicDBObject().append(
					Document.F_DOCUMENT_NUMBER, docNumber));
			if (docData != null) {
				document = ModelService.createModelObject(docData,
						Document.class);
			}
		}
		Deliverable deli;

		// 没有对应的PDM对象
		if (document == null) {
			deli = work.makeDeliverableDefinition(IDeliverable.TYPE_OUTPUT);
		} else {
			deli = work.makeDeliverableDefinition(IDeliverable.TYPE_LINK);
			deli.setValue(Deliverable.F_DOCUMENT_ID, document.get_id());
		}
		deli.setValue(Deliverable.F_DESC, pdmObject.get("md$description")); //$NON-NLS-1$
		deli.doSave(context);
		document = deli.getDocument();
		writePDMInfo(ouid, document);
	}
	
	public static void createDocument2(Work work, String ouid, IContext context) throws Exception {

		// 取出PDM中的对象
		DOSChangeable pdmObject = Starter.dos.get(ouid);
		Object docId = pdmObject.get("pm_id"); //$NON-NLS-1$
		String pdmDesc = (String) pdmObject.get("md$description");
		String number = (String) pdmObject.get("md$number");
		String pdmModelName = (String) pdmObject.get("name");
		String desc;
		if(pdmModelName!=null){
			desc = pdmModelName;
		}else if(pdmDesc!=null){
			desc = pdmDesc;
		}else{
			desc = "";
		}
		
		Document doc = null;
		if (!Utils.isNullOrEmptyString(docId)) {
			DBCollection docCol = DBActivator.getCollection(IModelConstants.DB,
					IModelConstants.C_DOCUMENT);
			DBObject docData = docCol.findOne(new BasicDBObject().append(
					Document.F__ID, new ObjectId((String)docId)));
			//可系统中查找到该文档
			if (docData != null) {
				doc = ModelService.createModelObject(docData,
						Document.class);
			}
		}
		Deliverable deli;

		// 没有对应的PDM对象
		if (doc == null) {
			deli = work.makeDeliverableDefinition(IDeliverable.TYPE_OUTPUT);
		} else {
			deli = work.makeDeliverableDefinition(IDeliverable.TYPE_LINK);
			deli.setValue(Deliverable.F_DOCUMENT_ID, doc.get_id());
		}
		deli.setValue(Deliverable.F_DESC, desc); 
		deli.doSave(context);
		doc = deli.getDocument();
		if(doc==null){
			doc = ModelService.createModelObject(Document.class);
			doc.setValue(Document.F__ID, new ObjectId());
		}

		doc.setValue(Document.F_DOCUMENT_NUMBER, number); 
		doc.setValue(Document.F_DESC, desc); 
		doc.setValue(Document.F_PDM_OUID, ouid);
		doc.setValue(Document.F__EDITOR, "editor.document.dcpdm");
		doc.doSave(context);
		pdmObject = Starter.dos.get(ouid);
		pdmObject.put("pm_id", doc.get_id().toString());
		Starter.dos.set(pdmObject);
	}

	@Deprecated
	public static boolean createDocumentFromDCPDM(String userId, Work work,
			Shell shell) throws Exception {
		List<?> docContainer = DCPDMUtil
				.getDocumentAndDrawingContainerCode(userId);
		List<?> partContainer = DCPDMUtil.getPartContainerCode(userId);

		if (docContainer == null) {
			throw new Exception(Messages.get().DCPDMUtil_2);
		}

		return createDocumentFromDCPDM(work, docContainer, partContainer, shell);
	}
	
	@Deprecated
	public static List<Document> getDocumentFromDCPDM(String userId, Shell shell)
			throws Exception {
		List<?> docContainer = DCPDMUtil
				.getDocumentAndDrawingContainerCode(userId);
		List<?> partContainer = DCPDMUtil.getPartContainerCode(userId);

		if (docContainer == null) {
			throw new Exception(Messages.get().DCPDMUtil_3);
		}

		PDMObjectSelector selector = new PDMObjectSelector(shell, docContainer,
				partContainer);
		int ret = selector.open();
		if (ret == PDMObjectSelector.OK) {
			String[] sel = selector.getSelection();
			if (sel.length == 0) {
				return null;
			}
			List<Document> result = new ArrayList<Document>();
			for (int i = 0; i < sel.length; i++) {
				DOSChangeable pdmObject = Starter.dos.get(sel[i]);
				Object docNumber = pdmObject.get("md$number"); //$NON-NLS-1$
				Document document = null;
				if (!Utils.isNullOrEmptyString(docNumber)) {
					DBCollection docCol = DBActivator.getCollection(
							IModelConstants.DB, IModelConstants.C_DOCUMENT);
					DBObject docData = docCol.findOne(new BasicDBObject()
							.append(Document.F_DOCUMENT_NUMBER, docNumber));
					if (docData != null) {
						document = ModelService.createModelObject(docData,
								Document.class);
					}
				}

				if (document == null) {
					// 创建文档对象
					document = ModelService.createModelObject(Document.class);
					writePDMInfo(sel[i], document);

				}
				if (document != null) {
					result.add(document);
				}
			}
			return result;
		}
		return null;

	}

	@Deprecated
	private static void writePDMInfo(String ouid, final Document document)
			throws Exception {
		ImportData ip = new ImportData() {

			@Override
			protected String getNamespace() {
				return "vault_file"; //$NON-NLS-1$
			}

			@Override
			protected DB getDB() {
				OrganizationDistributeFileBase filebase = new OrganizationDistributeFileBase();
				return DBActivator.getDB(filebase.getDB(document));
			}

			@Override
			protected String getClassOuid() {
				return null;
			}
		};

		ip.syncItem(ouid, document);
	}

	public static void download(String fileNameWithoutExtension,ArrayList<Map<String, Object>> fileList, Shell shell) {
		if (fileList == null || fileList.isEmpty()) {
			return;
		}

		try {
			BufferedInputStream origin = null;

			String pathname = System.getProperty("user.dir") + "/temp/" + Utils.getRandomString(8, true); //$NON-NLS-1$ //$NON-NLS-2$
			File tempFolder = new File(pathname);
			if (!tempFolder.isDirectory()) {
				tempFolder.mkdirs();
			}

			String zipFileName = pathname + "/" //$NON-NLS-1$
					+ fileNameWithoutExtension //$NON-NLS-1$
					+".zip"; //$NON-NLS-1$
			FileOutputStream dest = new FileOutputStream(zipFileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest), Charset.forName("GBK")); //$NON-NLS-1$

			byte data[] = new byte[2048];

			Set<String> fileNameSet = new HashSet<String>();
			for (int i = 0; i < fileList.size(); i++) {
				Map<String, Object> map = fileList.get(i);
				
				String desc = (String) map.get("md$description");
				String filePath = (String) map.get("md$path");
				File file = new File(desc);
				
				String fileName;
				InputStream is;

				fileName = checkName(file.getName(), fileNameSet);
				is = Starter.dss.getInputStream(filePath);
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
				FileUtil.download(shell, zipFile, zipFile.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private static String checkName(String fileName, Set<String> fileNameSet) {
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
}
