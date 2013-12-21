package com.sg.business.performence.orgproject;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.performence.nls.Messages;
import com.sg.business.performence.ui.calendar.ResourceCalender;
import com.sg.widgets.part.CurrentAccountContext;

public class OrgProjectResCalender extends ResourceCalender {
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		setTitleToolTip(Messages.get().OrgProjectResCalender_0);
	}
	
	@Override
	protected IContentProvider getContentProvider() {
		return new OrgProjectResContentProvider();
	}

	@Override
	protected List<PrimaryObject> getInput() {
		// ��ȡ��ǰ�û����ι����߽�ɫ�Ĳ���
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		User user = UserToolkit.getUserById(userId);
		List<PrimaryObject> orglist = user
				.getRoleGrantedInAllOrganization(Role.ROLE_DEPT_MANAGER_ID);
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();
		
		for (int i = 0; i < orglist.size(); i++) {
			Organization org = (Organization) orglist.get(i);
			boolean hasParent = false;
			for (int j = 0; j < input.size(); j++) {
				Organization inputOrg = (Organization) input.get(j);
				if (inputOrg.isSuperOf(org)) {
					hasParent = true;
					break;
				}
				if (org.isSuperOf(inputOrg)) {
					input.remove(j);
					break;
				}
			}
			if (!hasParent) {
				input.add(org);
			}
		}
		
		return input;
	}

	@Override
	protected EditingSupport getEditingSupport(int year, int month,
			int dateOfMonth) {
		return null;
	}


}
