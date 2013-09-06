package com.sg.bpm.workflow.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.service.BPM;
import com.sg.bpm.service.HTService;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;

public class Workflow {

	private PrimaryObject host;
	private Map<String, String> actorParameter;
	private DroolsProcessDefinition processDefintion;

	public Workflow(DroolsProcessDefinition processDefintion,
			Map<String, String> actorParameter, PrimaryObject po) {
		this.host = po;
		this.actorParameter = actorParameter;
		this.processDefintion = processDefintion;
	}

	public ProcessInstance startHumanProcess(Map<String, Object> params)
			throws Exception {
		
		if(params==null){
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
		
		

		StatefulKnowledgeSession ksession = processDefintion.getKnowledgeSession();
		params.put("content", host.getJSON());
		
		ProcessInstance pi = ksession.startProcess(processDefintion.getProcessId(), params);

		return pi;
	}
}
