package com.sg.business.model;


public class RoleDefinition extends AbstractRoleDefinition implements IProjectTemplateRelative{

	/**
	 * ������ɫ�ı༭��
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition";
	

	
	@Override
	public String getTypeName() {
		return "��ɫ����";
	}
}
