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
import com.mongodb.DB;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
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

public class LinkPDMDocAndDraw extends AbstractNavigatorHandler {

	private void createDocument(Work work, String ouid) throws Exception {
		DOSChangeable pdmObject = Starter.dos.get(ouid);
		
		Deliverable deli = work.makeDeliverableDefinition();
		deli.setValue(Deliverable.F_DESC, pdmObject.get("md$description"));
		
		deli.doSave(new CurrentAccountContext());
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
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		List<?> docContainer = getDocumentAndDrawingContainerCode();
		List<?> partContainer = getPartContainerCode();

		if (docContainer == null) {
			MessageUtil.showToast("您所在的组织尚未确定PDM系统中可使用图文档容器。", SWT.ICON_ERROR);
			return;
		}
		Shell shell = part.getSite().getShell();
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
					createDocument((Work) selected,sel[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			vc.doRefresh();
		}

	}

}
