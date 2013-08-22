package com.sg.business.model;

/**
 * 工作的前后置关系
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkConnection extends AbstractWorkConnection implements IProjectRelative {
	public static final String EDITOR = "editor.workConnection";

	
	@Override
	public String getTypeName() {
		return "工作前后置关系";
	}
}
