package com.sg.business.model.registry;

import org.eclipse.core.runtime.IConfigurationElement;

import com.sg.business.model.IFileServerDelegator;
import com.sg.widgets.registry.config.Configurator;

public class FileServerConfigurator extends Configurator {


	private String document;

	public FileServerConfigurator(IConfigurationElement ce) {
		super(ce);
		this.document =getString("document"); //$NON-NLS-1$
	}
	
	public String getDocument() {
		return document;
	}


	public IFileServerDelegator getFileServerDelegator(){
		return (IFileServerDelegator) getExecutable("fileServerDelegator"); //$NON-NLS-1$
	}
	
	
}
