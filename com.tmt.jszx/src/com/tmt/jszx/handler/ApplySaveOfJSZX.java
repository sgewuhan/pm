package com.tmt.jszx.handler;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.drools.runtime.process.ProcessInstance;
import org.eclipse.core.runtime.IProgressMonitor;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IRoleParameter;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.bpmservice.BPMServiceContext;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ApplySaveOfJSZX implements IDataObjectDialogCallback {

	public ApplySaveOfJSZX() {
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		TaskForm taskform = (TaskForm) input.getData();
		/**
		 * 可以直接调用taskform的addWorkParticipates方法
		 */
		Work work = taskform.getWork();
		ProcessInstance processInstance = work.getExecuteProcess();
		IContext context = new BPMServiceContext(
				processInstance.getProcessName(),
				processInstance.getProcessId());

		/**
		 * 可以使用以下的语句直接添加chief_engineer字段的内容到work
		 */
		// taskform.addWorkParticipatesFromField(new String[]{"chief_master"});
		List<String> userList = new ArrayList<String>();
		String chiefMaster = (String) taskform.getValue("chief_engineer"); //$NON-NLS-1$
		Object dept = taskform.getValue("dept"); //$NON-NLS-1$
		// ***************************************************

		String deptDirector = getValueByDept(dept, Role.ROLE_DEPT_MANAGER_ID,
				work, context);
		String deputyDirector = getValueByDept(dept,
				IRoleConstance.ROLE_DEPUTY_DIRECTOR_ID, work, context);
		String proAdmin = getValueByDept(dept, Role.ROLE_PROJECT_ADMIN_ID,
				work, context);

		userList.add(deptDirector);
		userList.add(chiefMaster);
		userList.add(deputyDirector);
		userList.add(proAdmin);

		work.doAddParticipateList(userList);
		return true;
	}

	public String getValueByDept(Object dept, String roleNumber, Work work,
			IContext context) {

		if (dept instanceof ObjectId) {
			Organization org = ModelService.createModelObject(
					Organization.class, (ObjectId) dept);
			Role role = org.getRole(roleNumber, Organization.ROLE_SEARCH_UP);
			if (role != null) {
				// 使用TYPE为TYPE_WORK_PROCESS的RoleParameter，传入工作ID进行人员指派
				IRoleParameter roleParameter = work.getAdapter(IRoleParameter.class);
				List<PrimaryObject> assignment = role.getAssignment(roleParameter);
				if (assignment != null && assignment.size() > 0) {
					return ((RoleAssignment) assignment.get(0)).getUserid();
				}
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

	@Override
	public boolean okPressed() {
		return false;
	}

	@Override
	public void cancelPressed() {

	}

	@Override
	public boolean needSave() {
		return true;
	}

}
