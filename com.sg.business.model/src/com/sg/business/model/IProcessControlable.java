package com.sg.business.model;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;

public interface IProcessControlable {

	public static final String POSTFIX_ACTIVATED = "_activated";

	public static final String POSTFIX_ASSIGNMENT = "_assignment";

	public static final String POSTFIX_ACTORS = "_actors";
	
	boolean isWorkflowActivate(String fieldKey) ;
	
	DroolsProcessDefinition getProcessDefinition(String fieldKey);

	ProjectRole getProcessActionAssignment(String key, String nodeActorParameter);

	
}
