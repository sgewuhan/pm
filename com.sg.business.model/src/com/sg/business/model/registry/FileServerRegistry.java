package com.sg.business.model.registry;

import org.eclipse.core.runtime.IConfigurationElement;

import com.sg.business.model.ModelActivator;
import com.sg.widgets.registry.Registry;
import com.sg.widgets.registry.config.Configurator;

public class FileServerRegistry extends Registry {

	@Override
	protected Configurator getModel(IConfigurationElement ce) {
		return new FileServerConfigurator(ce);
	}

	@Override
	protected String getRootElement() {
		return "fileserver"; //$NON-NLS-1$
	}

	@Override
	protected String getExtPointName() {
		return "fileserver"; //$NON-NLS-1$
	}

	@Override
	protected String getNamespace() {
		return ModelActivator.PLUGIN_ID;//$NON-NLS-1$
	}
}
