package com.sg.business.model;

/**
 * ������ǰ���ù�ϵ
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkConnection extends AbstractWorkConnection implements IProjectRelative {
	public static final String EDITOR = "editor.workConnection";

	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "����ǰ���ù�ϵ";
	}
}
