package com.sg.bpm.workflow;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.sg.bpm.service.BPM;
import com.sg.bpm.service.HTService;
import com.sg.bpm.workflow.taskform.TaskFormConfig;

/**
 * The activator class controls the plug-in life cycle
 */
public class WorkflowActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.bpm.workflow"; //$NON-NLS-1$

	// The shared instance
	private static WorkflowActivator plugin;
	
	private Map<String, TaskFormConfig> taskStartFormMap = new HashMap<String, TaskFormConfig>();

	private Map<String, TaskFormConfig> taskCompleteFormMap = new HashMap<String, TaskFormConfig>();

	/**
	 * The constructor
	 */
	public WorkflowActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		loadConfig();
		syncAccountToBPM();
	}

	public void syncAccountToBPM() {
		HTService s = BPM.getHumanTaskService();
		s.addParticipateUser("Administrator");

//		// 将用户信息同步到流程管理器
//		DBCollection col = DBActivator.getCollection("pm2", "account");
//		DBCursor cur = col.find(new BasicDBObject().append("activated", Boolean.TRUE),new BasicDBObject().append("userid", 1));
//		while(cur.hasNext()){
//			DBObject usersItem = cur.next();
//			String id = (String) usersItem.get("userid");
//			s.addParticipateUser(id);
//		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static WorkflowActivator getDefault() {
		return plugin;
	}
	
	
	private void loadConfig() {

		IExtensionRegistry eReg = Platform.getExtensionRegistry();
		IExtensionPoint ePnt = eReg.getExtensionPoint(PLUGIN_ID, "taskForm");
		if (ePnt == null)
			return;
		IExtension[] exts = ePnt.getExtensions();
		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] confs = exts[i].getConfigurationElements();
			for (int j = 0; j < confs.length; j++) {
				if ("taskForm".equals(confs[j].getName())) {
					TaskFormConfig element = new TaskFormConfig(confs[j]);
					if (element.isStartForm()) {
						taskStartFormMap.put(element.getTaskFormId(), element);
					} else if (element.isCompleteForm()) {
						taskCompleteFormMap.put(element.getTaskFormId(),
								element);
					}
				}
			}
		}

	}

	public TaskFormConfig getTaskCompleteFormConfig(String processDefinitionId,
			String taskName) {

		return taskCompleteFormMap.get(processDefinitionId + "@" + taskName);
	}

	public TaskFormConfig getTaskStartFormConfig(String processDefinitionId,
			String taskName) {

		return taskStartFormMap.get(processDefinitionId + "@" + taskName);
	}

}
