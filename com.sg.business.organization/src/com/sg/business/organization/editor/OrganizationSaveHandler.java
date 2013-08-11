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
	 * 	���ݱ������֯����ά������֯�ر��Ľ�ɫ
	 */
	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		//�����֯�Ǿ�����Ŀ����ְ�ܵ���֯����Ҫ�Զ������Ŀ����Ա��ɫ
		Organization org = (Organization) input.getData();
		if(Boolean.TRUE.equals(org.isFunctionDepartment())){
			//�ж��Ƿ�������role,�������������ӡ�����ж�ͨ������Ψһ��������������������������⡣
			boolean hasRole = org.hasRole(Role.ROLE_PROJECT_ADMIN_ID);
			if(!hasRole){
				org.doAddRole(Role.ROLE_PROJECT_ADMIN_ID,Role.ROLE_PROJECT_ADMIN_TEXT);
			}
			hasRole = org.hasRole(Role.ROLE_BUSINESS_ADMIN_ID);
			if(!hasRole){
				org.doAddRole(Role.ROLE_BUSINESS_ADMIN_ID,Role.ROLE_BUSINESS_ADMIN_TEXT);
			}
		}
		
		
		//�����֯�Ǿ����ĵ���������֯����Ҫ�Զ�����ĵ������ߺ��ĵ�����Ա�Ľ�ɫ
		if(Boolean.TRUE.equals(org.isContainer())){
			//�ж��Ƿ�������role
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
