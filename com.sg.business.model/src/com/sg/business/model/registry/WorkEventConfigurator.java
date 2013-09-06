package com.sg.business.model.registry;

import org.eclipse.core.runtime.IConfigurationElement;

import com.sg.business.model.event.IEventAction;
import com.sg.business.model.event.IWorkFilter;
import com.sg.widgets.registry.config.Configurator;

public class WorkEventConfigurator extends Configurator {

	private String name;
	private String eventType;

	public WorkEventConfigurator(IConfigurationElement ce) {
		super(ce);
		this.name =getString("name");
		this.eventType = getString("eventType");
	}

	public String getName() {
		return name;
	}

	public String getEventType() {
		return eventType;
	}

	public IWorkFilter getWorkFilter(){
		return (IWorkFilter) getExecutable("workFilter");
	}
	
	public IEventAction getEventAction(){
		return (IEventAction) getExecutable("eventAction");

	}
	
}
