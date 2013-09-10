package com.sg.bpm.workflow.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.eclipse.core.runtime.Assert;

import com.mobnut.db.model.DocumentModelDefinition;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.sg.bpm.service.BPM;
import com.sg.bpm.service.HTService;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;

public class Workflow {

	private static final String PROCESS_VAR_CONTENT = "content";
	private final PrimaryObject host;
	private final DroolsProcessDefinition processDefintion;
	private final String key;

	public Workflow(DroolsProcessDefinition processDefintion, PrimaryObject po,String key) {
		this.host = po;
		this.processDefintion = processDefintion;
		this.key = key;
	}

	public Workflow(String processId, long processInstanceId) throws Exception {
		processDefintion = new DroolsProcessDefinition(processId);
		Assert.isNotNull(processDefintion, "无法确定流程ID对应的流程定义");
		StatefulKnowledgeSession session = processDefintion.getKnowledgeSession();
		Assert.isNotNull(session, "无法获得流程定义的知识库进程");
		WorkflowProcessInstance pi = (WorkflowProcessInstance) session
				.getProcessInstance(processInstanceId);
		WorkflowProcessInstance instance = (WorkflowProcessInstance) pi;
		String jsonContent = (String) instance.getVariable(PROCESS_VAR_CONTENT);
		
		
		Assert.isLegal(jsonContent!=null, "获取模型的json参数不可为空");
		DBObject result = (DBObject) JSON.parse(jsonContent);
		Assert.isLegal(result!=null, "获取模型的参数无法解析为正确的JSON字符串");
		key = (String) result.get("key");
		String className = (String) result.get("class");
		DBObject data = (DBObject) result.get("data");
		DocumentModelDefinition modelDef = ModelService.getDocumentModelDefinition(className);
		Assert.isNotNull(modelDef, "无法确定类的模型定义:"+className);
		host = ModelService.createModelObject(data, modelDef.getModelClass());
		
	}

	public ProcessInstance startHumanProcess(
			Map<String, String> actorParameter, Map<String, Object> params)
			throws Exception {

		if (params == null) {
			params = new HashMap<String, Object>();
		}

		HTService ts = BPM.getHumanTaskService();
		if (actorParameter != null) {
			Collection<String> idSet = actorParameter.values();
			Iterator<String> iter = idSet.iterator();
			while (iter.hasNext()) {
				String item = iter.next();
				if (item.contains(",")) {
					String[] subItems = item.split(",");
					for (int i = 0; i < subItems.length; i++) {
						ts.addParticipateUser(subItems[i]);
					}
				} else {
					ts.addParticipateUser(item);
				}
			}

			params.putAll(actorParameter);
		}

		StatefulKnowledgeSession ksession = processDefintion
				.getKnowledgeSession();
		
		BasicDBObject result = new BasicDBObject();
		result.put("data", host.get_data());
		result.put("key", key);
		result.put("class",host.getClass().getName());
		String var = JSON.serialize(result);
		
		params.put(PROCESS_VAR_CONTENT, var);

		ProcessInstance pi = ksession.startProcess(
				processDefintion.getProcessId(), params);

		return pi;
	}

	public PrimaryObject getHost() {
		return host;
	}

	public String getKey() {
		return key;
	}
}
