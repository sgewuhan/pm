package com.tmt.pdm.dcpdm.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.IDeliverable;
import com.sg.business.model.IWorkRelative;
import com.sg.business.model.Organization;
import com.sg.business.model.OrganizationDistributeFileBase;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;
import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.sync.ImportData;

import dyna.framework.service.dos.DOSChangeable;

public class LinkPDMDocAndDraw2 extends AbstractNavigatorHandler {

	private Deliverable deli;

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

	private void createDocument(Work work, String ouid) throws Exception {
		DOSChangeable pdmObject = Starter.dos.get(ouid);
		deli = null;
		boolean buildDocument = true;
		String documentNumber = (String) pdmObject.get("md$number");
		if (documentNumber != null) {
			BasicDBObject condition = new BasicDBObject();
			condition.put(Document.F_DOCUMENT_NUMBER, documentNumber);
			List<PrimaryObject> list = deli.getRelationByCondition(Document.class,
					condition);
			if (list != null && list.size() > 0) {
				deli = work
						.makeDeliverableDefinition(IDeliverable.TYPE_REFERENCE);
				Document document = (Document) list.get(0);
				deli.setValue(Deliverable.F_DOCUMENT_ID ,document.get_id());
				buildDocument = false;
			}
		}
		if (buildDocument) {
			deli = work.makeDeliverableDefinition(IDeliverable.TYPE_OUTPUT);
		}
		deli.setValue(Deliverable.F_DESC, pdmObject.get("md$description"));

		deli.doSave(new CurrentAccountContext());
		if (buildDocument) {
			Document document = deli.getDocument();
			ImportData ip = new ImportData() {

				@Override
				protected String getNamespace() {
					return "vault_file";
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

	private List<?> getDocumentAndDrawingContainerCode() {
		String userId = new CurrentAccountContext().getConsignerId();
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

	private List<?> getPartContainerCode() {
		String userId = new CurrentAccountContext().getConsignerId();
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

	@Override
	protected void execute(PrimaryObject po, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection se) {
		final Shell shell = part.getSite().getShell();

		final PrimaryObject master = vc.getMaster();
		Work work = getWork(master);
		if (work == null) {
			MessageUtil.showToast(shell, "创建PDM交付物", "请选择流程", SWT.ICON_ERROR);
			return;
		}

		List<?> docContainer = getDocumentAndDrawingContainerCode();
		List<?> partContainer = getPartContainerCode();

		if (docContainer == null) {
			MessageUtil.showToast("您所在的组织尚未确定PDM系统中可使用图文档容器。", SWT.ICON_ERROR);
			return;
		}
		PDMObjectSelector selector = new PDMObjectSelector(shell, docContainer,
				partContainer);
		int ret = selector.open();
		if (ret == PDMObjectSelector.OK) {
			String[] sel = selector.getSelection();
			if (sel.length == 0) {
				return;
			}

			for (int i = 0; i < sel.length; i++) {
				try {
					createDocument(work, sel[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			vc.doReloadData();
		}

	}

	protected Work getWork(PrimaryObject master) {
		if (master instanceof Work) {
			return (Work) master;
		} else if (master instanceof IWorkRelative) {
			IWorkRelative iWorkRelative = (IWorkRelative) master;
			return (Work) iWorkRelative.getWork();
		} else {
			return null;
		}
	}

}
