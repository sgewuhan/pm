package com.sg.business.model;

/**
 * ���������ǰ���ù�ϵ
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkDefinitionConnection extends AbstractWorkConnection implements IProjectTemplateRelative{
	public static final String EDITOR = "editor.workDefinitionConnection";
	
	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��������ǰ���ù�ϵ";
	}
}
