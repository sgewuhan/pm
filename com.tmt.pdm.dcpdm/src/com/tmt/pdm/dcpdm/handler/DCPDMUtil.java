package com.tmt.pdm.dcpdm.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import com.mobnut.commons.util.Utils;
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
import com.tmt.pdm.dcpdm.nls.Messages;
import com.tmt.pdm.dcpdm.sync.ImportData;

import dyna.framework.service.dos.DOSChangeable;

public class DCPDMUtil {

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

	private static List<?> getDocumentAndDrawingContainerCode(String userId) {
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

	private static List<?> getPartContainerCode(String userId) {
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

	private static void writePDMInfo(String ouid, Document document) throws Exception {
		ImportData ip = new ImportData() {

			@Override
			protected String getNamespace() {
				return "vault_file"; //$NON-NLS-1$
			}

			@Override
			protected DB getDB() {
				OrganizationDistributeFileBase filebase = new OrganizationDistributeFileBase();
				return DBActivator.getDB(filebase.getDB());
			}

			@Override
			protected String getClassOuid() {
				return null;
			}
		};

		ip.syncItem(ouid, document);		
	}
}
