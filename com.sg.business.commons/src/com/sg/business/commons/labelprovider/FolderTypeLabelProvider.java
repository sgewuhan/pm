package com.sg.business.commons.labelprovider;

import com.sg.business.model.Container;
import com.sg.business.model.Folder;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class FolderTypeLabelProvider extends ConfiguratorColumnLabelProvider {

	public FolderTypeLabelProvider(ColumnConfigurator configurator) {
		super(configurator);
	}

	public FolderTypeLabelProvider() {
	}

	@Override
	public String getText(Object element) {
		if(element instanceof Container){
			return ((Container) element).getContainerTypeText();
		}else if(element instanceof Folder){
			return ((Folder) element).getFolderType();
		}
		return "";
	}


	
	
}
