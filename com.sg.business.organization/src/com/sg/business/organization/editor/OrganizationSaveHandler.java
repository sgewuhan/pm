package com.sg.business.organization.editor;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class OrganizationSaveHandler implements IEditorSaveHandler {

	public OrganizationSaveHandler() {
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}

	/**
	 * 	根据保存的组织属性维护该组织必备的角色
	 */
	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		//如果组织是具有项目管理职能的组织，需要自动添加项目管理员角色
		Organization org = (Organization) input.getData();
		if(Boolean.TRUE.equals(org.isFunctionDepartment())){
			//判断是否存在这个role,如果不存在再添加。这个判断通过建立唯一索引来解决可以免除事务处理的问题。
			boolean hasRole = org.hasRole(Role.ROLE_PROJECT_ADMIN_ID);
			if(!hasRole){
				org.doAddRole(Role.ROLE_PROJECT_ADMIN_ID,Role.ROLE_PROJECT_ADMIN_TEXT);
			}
			hasRole = org.hasRole(Role.ROLE_BUSINESS_ADMIN_ID);
			if(!hasRole){
				org.doAddRole(Role.ROLE_BUSINESS_ADMIN_ID,Role.ROLE_BUSINESS_ADMIN_TEXT);
			}
		}
		
		
		//如果组织是具有文档容器的组织，需要自动添加文档访问者和文档管理员的角色
		if(Boolean.TRUE.equals(org.isContainer())){
			//判断是否存在这个role
			boolean hasRole = org.hasRole(Role.ROLE_VAULT_ADMIN_ID);
			if(!hasRole){
				org.doAddRole(Role.ROLE_VAULT_ADMIN_ID,Role.ROLE_VALUT_ADMIN_TEXT);
			}
			hasRole = org.hasRole(Role.ROLE_VAULT_GUEST_ID);
			if(!hasRole){
				org.doAddRole(Role.ROLE_VAULT_GUEST_ID,Role.ROLE_VAULT_GUEST_TEXT);
			}
		}

		
		return true;
	}

}
