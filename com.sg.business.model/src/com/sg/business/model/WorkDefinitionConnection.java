package com.sg.business.model;

/**
 * 工作定义的前后置关系
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkDefinitionConnection extends AbstractWorkConnection implements IProjectTemplateRelative{
	public static final String EDITOR = "editor.workDefinitionConnection";
	
	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "工作定义前后置关系";
	}
}
