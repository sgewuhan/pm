package com.sg.business.model;

/**
 *  ��ɫ����
 * <p/>
 * ����Ŀģ�����Ŀ�ж���Ľ�ɫ
 * @author jinxitao
 *
 */
public class RoleDefinition extends AbstractRoleDefinition implements IProjectTemplateRelative{

	
	
	/**
	 * ������ɫ�ı༭��
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition";
	

	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��ɫ����";
	}
}
