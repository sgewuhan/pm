package com.sg.business.work.launch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.page.AbstractProcessSettingPage;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.AbstractRoleAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class WorkFlowSettingPage extends WizardPage {

	private PrimaryObjectEditorInput input;
	private AbstractProcessSettingPage page;

	protected WorkFlowSettingPage() {
		super("WORKFLOW_SETTING_PAGE");
		setTitle("请确定工作执行流程");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_WORKFLOW_72));
	}

	@Override
	public void createControl(Composite parent) {
		Composite content = new Composite(parent, SWT.NONE);
		page = new AbstractProcessSettingPage() {

			@Override
			protected int getProcessSettingControl() {
				return ProcessSettingPanel2.ACTOR_SELECTOR
						| ProcessSettingPanel2.ROLE_SELECTOR;
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
			protected DataSet getActorDataSet(AbstractRoleDefinition roled) {
				// 如果角色定义不为空，取角色下的用户
				List<PrimaryObject> result = new ArrayList<PrimaryObject>();
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
			}

		};
		page.createPageContent(parent, input, null);
		ProcessSettingPanel2 psp = page.getProcessSettingPanel();
		psp.setRoleSelectEnable(false);
		setControl(content);
	}

	public void setInput(PrimaryObjectEditorInput input) {
		this.input = input;
	}

	public void refresh() {
		if (page != null){
			page.refresh();
			Composite control = (Composite)getControl();
			control.layout();
		}
	}

}
