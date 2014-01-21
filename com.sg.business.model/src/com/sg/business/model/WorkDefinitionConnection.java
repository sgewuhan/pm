package com.sg.business.model;

import com.sg.business.resource.nls.Messages;

/**
 * ���������ǰ���ù�ϵ
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkDefinitionConnection extends AbstractWorkConnection implements IProjectTemplateRelative{
	
	/**
	 * ��������ı༭��ID
	 */
	public static final String EDITOR = "editor.workDefinitionConnection"; //$NON-NLS-1$
	
	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().WorkDefinitionConnection_1;
	}
}
