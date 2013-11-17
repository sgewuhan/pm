package com.sg.bpm.workflow.taskform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.jbpm.task.Task;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.model.IEditorSaveHandler;

//import com.sg.widgets.part.editor.IDataObjectDialogCallback;

public class TaskFormConfig {

	private String processDefinitionId;

	private String taskName;

	private String editorId;

	private List<ProcessParameter> processParameters = new ArrayList<ProcessParameter>();

	private IConfigurationElement ic;

	public TaskFormConfig(IConfigurationElement ic) {

		this.ic = ic;

		processDefinitionId = ic.getAttribute("processDefinitionId");
		taskName = ic.getAttribute("name");
		editorId = ic.getAttribute("editorId");

		IConfigurationElement[] parameters = ic.getChildren("processParameter");
		for (int i = 0; i < parameters.length; i++) {
			IConfigurationElement cce = parameters[i];
			ProcessParameter pp = new ProcessParameter(cce);
			processParameters.add(pp);
		}
	}

	public String getTaskFormId() {

		return processDefinitionId + "@" + taskName;
	}

	public String getEditorId() {

		return editorId;
	}

	public PrimaryObject getTaskFormInput(PrimaryObject taskFormData, Task task) {

		try {
			ITaskFormInputHandler inputDelegator = (ITaskFormInputHandler) ic
					.createExecutableExtension("inputHandler");
			if (inputDelegator != null) {
				return inputDelegator.getTaskFormInputData(taskFormData, this,task);
			}
		} catch (CoreException e) {
		}
		return taskFormData;
	}

	// public IDataObjectDialogCallback getTaskFormDialogHandler() {
	// try {
	// IDataObjectDialogCallback callback = (IDataObjectDialogCallback) ic
	// .createExecutableExtension("saveHandler");
	// return callback;
	// } catch (CoreException e) {
	// }
	// return null;
	// }

	public IValidationHandler getValidationHandler() {
		try {
			IValidationHandler handler = (IValidationHandler) ic
					.createExecutableExtension("validation");
			return handler;
		} catch (CoreException e) {
		}
		return null;
	}

	public List<ProcessParameter> getProcessParameters() {

		return processParameters;
	}

	public String[] getPersistentFields() {

		String persistentFields = ic.getAttribute("persistentFields");
		return persistentFields == null ? new String[] {} : persistentFields
				.split(",");
	}

	public IWorkflowInfoProvider getWorkflowInformationProvider() {
		try {
			IWorkflowInfoProvider handler = (IWorkflowInfoProvider) ic
					.createExecutableExtension("workflowInfo");
			return handler;
		} catch (CoreException e) {
		}
		return null;

	}

	public Map<String, Object> getInputParameter(PrimaryObject taskFormData)
			throws Exception {
		Object inputValue = taskFormData.getValue("processinput");
		Map<String, Object> result = new HashMap<String, Object>();
		if(inputValue instanceof Map<?, ?>){
			Iterator<?> iter = ((Map<?,?>) inputValue).keySet().iterator();
			while(iter.hasNext()){
				Object field = iter.next();
				String key = field.toString();
				Object v = ((Map<?,?>) inputValue).get(field);
				result.put(key, v);
			}
			/**
			 * 继续执行代理方法
			 */
//			if(!result.isEmpty()){
//				return result;
//			}else{
//				return null;
//			}
		}
		
		List<ProcessParameter> ps = getProcessParameters();
		if (ps.size() == 0) {
			return result;
		}
		for (int i = 0; i < ps.size(); i++) {
			ProcessParameter pi = ps.get(i);
			String processParameter = pi.getprocessParameterName();
			String taskDatakey = pi.getTaskFormName();
			IProcessParameterDelegator pd = pi.getProcessParameterDelegator();
			if (pd != null) {// 如果设置了取值代理
				try {
					Object value = pd.getValue(processParameter, taskDatakey,
							taskFormData);
					result.put(processParameter, value);
				} catch (Exception e) {
					throw new Exception(getTaskFormId() + "参数取值错误。"
							+ pd.getClass().getName());
				}
			} else {// 如果没有设置代理，直接从表单取值
				if (taskFormData != null) {
					Object value = taskFormData.getValue(taskDatakey);
					result.put(processParameter, value);
				}
			}
		}

		return result;

	}

	public IEditorSaveHandler getSaveHandler() {
		try {
			IEditorSaveHandler handler = (IEditorSaveHandler) ic
					.createExecutableExtension("saveHandler");
			return handler;
		} catch (CoreException e) {
		}
		return null;

	}

}
