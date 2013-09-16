package com.sg.business.model;

/**
 *  角色定义
 * <p/>
 * 在项目模板和项目中定义的角色
 * @author jinxitao
 *
 */
public class RoleDefinition extends AbstractRoleDefinition implements IProjectTemplateRelative{

	
	
	/**
	 * 创建角色的编辑器
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition";
	

	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "角色定义";
	}
}
