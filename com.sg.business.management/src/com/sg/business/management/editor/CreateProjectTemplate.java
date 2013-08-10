package com.sg.business.management.editor;

import com.mobnut.db.model.IPrimaryObjectEventListener;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BudgetItem;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.IMasterListenerPart;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class CreateProjectTemplate extends ChildPrimaryObjectCreator {

	@Override
	protected String getMessageForEmptySelection() {
		return "����Ҫѡ����֯����д���";
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		Organization parentOrg = (Organization) po.getParentPrimaryObject();
		// ������Ŀģ���������֯
		parentOrg.makeProjectTemplate((ProjectTemplate) po);
	}

	@Override
	protected boolean doSaveBefore(PrimaryObjectEditorInput input,
			String operation) {
		if (IPrimaryObjectEventListener.INSERTED.equals(operation)) {
			ProjectTemplate po = (ProjectTemplate) input.getData();
			try {
				createBudgetRoot(po);
				createWBSRoot(po);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return super.doSaveBefore(input, operation);

	}

	private void createWBSRoot(PrimaryObject po) throws Exception {
		if (po.getValue(ProjectTemplate.F_WORK_DEFINITON_ID) == null) {
			WorkDefinition wbsRoot = WorkDefinition
					.CREATE_ROOT(WorkDefinition.WORK_TYPE_PROJECT);
			wbsRoot.doSave(new CurrentAccountContext());
			po.setValue(ProjectTemplate.F_WORK_DEFINITON_ID, wbsRoot.get_id());
			
		}
	}

	private void createBudgetRoot(PrimaryObject po) throws Exception {
		if (po.getValue(ProjectTemplate.F_BUDGET_ID) == null) {
			BudgetItem biRoot = BudgetItem.COPY_DEFAULT_BUDGET_ITEM();
			biRoot.doSave(new CurrentAccountContext());
			po.setValue(ProjectTemplate.F_BUDGET_ID, biRoot.get_id());
		}
	}

	@Override
	protected boolean doSaveAfter(PrimaryObjectEditorInput input,
			String operation) {

		// ˢ���б�
		IMasterListenerPart part = (IMasterListenerPart) Widgets
				.getViewPart("management.projecttemplates");
		if (part != null) {
			part.reloadMaster();
		}
		return super.doSaveAfter(input, operation);
	}

	@Override
	protected boolean needHostPartListenSaveEvent() {
		return false;
	}
}
