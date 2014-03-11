package com.sg.business.work.launch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.ui.flow.ProcessSettingPanel;
import com.sg.business.commons.ui.page.flow.AbstractProcessPage;
import com.sg.business.model.AbstractRoleAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.dataset.organization.OrgRoot;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class WorkFlowSettingPage extends WizardPage {

	private PrimaryObjectEditorInput input;
	private AbstractProcessPage page;

	protected WorkFlowSettingPage() {
		super("WORKFLOW_SETTING_PAGE"); //$NON-NLS-1$
		setTitle(Messages.get().WorkFlowSettingPage_1);
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_WORKFLOW_72));
	}

	@Override
	public void createControl(Composite parent) {
		page = new AbstractProcessPage() {

			@Override
			protected int getProcessSettingControl() {
				return ProcessSettingPanel.ACTOR_SELECTOR
						| ProcessSettingPanel.ROLE_SELECTOR;
			}

			@Override
			protected DataSet getRoleDataSet() {
				// 不可选择角色
				return null;
			}

			@Override
			protected List<DroolsProcessDefinition> getProcessDefinition() {
				// 无需选择流程
				return null;
			}

			@Override
			protected String getProcessKey() {
				return Work.F_WF_EXECUTE;
			}
			
			@Override
			protected String getActorNavigatorId(AbstractRoleDefinition roled) {
				if(roled == null){
					return "organization.user.selector"; //$NON-NLS-1$
				}
				
				return super.getActorNavigatorId(roled);
			}

			@Override
			protected DataSet getActorDataSet(AbstractRoleDefinition roled) {
				// 如果角色定义不为空，取角色下的用户
				List<PrimaryObject> result = new ArrayList<PrimaryObject>();
				if (roled != null) {
					Role role = roled.getOrganizationRole();
					List<PrimaryObject> assignments = role.getAssignment();
					for (int i = 0; i < assignments.size(); i++) {
						AbstractRoleAssignment ass = (AbstractRoleAssignment) assignments
								.get(i);
						String userid = ass.getUserid();
						User user = UserToolkit.getUserById(userid);
						if (!result.contains(user)) {
							result.add(user);
						}
					}
					return new DataSet(result);
				} else {// 限定角色为空，可以从组织选择用户
					OrgRoot factory = new OrgRoot();
//					UserDataSetFactory factory = new UserDataSetFactory();
//					factory.setQueryCondition(new BasicDBObject().append(
//							User.F_ACTIVATED, Boolean.TRUE));
					return factory.getDataSet();
				}

			}

		};
		page.createPageContent(parent, input, null);
		ProcessSettingPanel psp = page.getProcessSettingPanel();
		psp.setRoleSelectEnable(false);
		setControl(psp);
	}

	public void setInput(PrimaryObjectEditorInput input) {
		this.input = input;
	}

	public void refresh() {
		if (page != null) {
			page.refresh();
			Composite control = (Composite) getControl();
			control.layout();
		}
	}

}
