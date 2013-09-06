package com.sg.business.model.registry;

import org.eclipse.core.runtime.IConfigurationElement;

import com.sg.business.model.ModelActivator;
import com.sg.widgets.registry.Registry;
import com.sg.widgets.registry.config.Configurator;

public class WorkEventRegistry extends Registry {

	@Override
	protected Configurator getModel(IConfigurationElement ce) {
		return new WorkEventConfigurator(ce);
	}

	@Override
	protected String getRootElement() {
		return "event";
	}

	@Override
	protected String getExtPointName() {
		return "workevent";
	}

	@Override
	protected String getNamespace() {
		return ModelActivator.PLUGIN_ID;//$NON-NLS-1$
	}
}
