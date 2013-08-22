package com.sg.business.model;


public class RoleDefinition extends AbstractRoleDefinition implements IProjectTemplateRelative{

	/**
	 * 创建角色的编辑器
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition";
	

	
	@Override
	public String getTypeName() {
		return "角色定义";
	}
}
