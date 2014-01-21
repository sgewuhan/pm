package com.sg.business.model;

import com.sg.business.resource.nls.Messages;

/**
 * 工作定义的前后置关系
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkDefinitionConnection extends AbstractWorkConnection implements IProjectTemplateRelative{
	
	/**
	 * 工作定义的编辑器ID
	 */
	public static final String EDITOR = "editor.workDefinitionConnection"; //$NON-NLS-1$
	
	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().WorkDefinitionConnection_1;
	}
}
