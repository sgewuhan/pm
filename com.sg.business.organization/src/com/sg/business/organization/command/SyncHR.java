package com.sg.business.organization.command;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.IRefreshablePart;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.editor.page.NavigatorPage;
import com.sg.widgets.part.view.NavigatorPart;
import com.sg.widgets.viewer.ViewerControl;

public class SyncHR extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		long start = System.currentTimeMillis();
		OrgExchange pmOrg = new OrgExchange(null, true);
		OrgExchange hrOrg = new OrgExchange(null, false);
		Set<OrgExchange> insertSet = new HashSet<OrgExchange>();
		Set<OrgExchange> removeSet = new HashSet<OrgExchange>();
		Set<OrgExchange> renameSet = new HashSet<OrgExchange>();
		if (!hrOrg.equals(pmOrg)) {
			insertSet.add(hrOrg);
			removeSet.add(pmOrg);
		} else {
			getPMDifferentHR(pmOrg, hrOrg, insertSet, removeSet, renameSet);
		}
		for (OrgExchange orgExchange : insertSet) {
			orgExchange.doAddAllHR();
		}
		for (OrgExchange orgExchange : renameSet) {
			orgExchange.doRenameHR();
		}

		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part instanceof IRefreshablePart) {
			((IRefreshablePart) part).doRefresh();
		}
		System.out.println(System.currentTimeMillis() - start);
		return null;
	}

	private void getPMDifferentHR(OrgExchange pmOrg, OrgExchange hrOrg,
			Set<OrgExchange> insertSet, Set<OrgExchange> removeSet,
			Set<OrgExchange> renameSet) {
		insertSet.addAll(hrOrg.getDifferentChildren(pmOrg));
		removeSet.addAll(pmOrg.getDifferentChildren(hrOrg));
		if (!hrOrg.getDifferentName(pmOrg)) {
			renameSet.add(hrOrg);
		}
		Set<OrgExchange> hrSameChildren = hrOrg.getSameChildren(pmOrg);
		for (OrgExchange orgExchange : hrSameChildren) {
			OrgExchange pmOrgChildren = new OrgExchange(orgExchange.getOrgId(),
					true);
			OrgExchange hrOrgChildren = new OrgExchange(orgExchange.getOrgId(),
					false);
			getPMDifferentHR(pmOrgChildren, hrOrgChildren, insertSet,
					removeSet, renameSet);
		}

	}

}
