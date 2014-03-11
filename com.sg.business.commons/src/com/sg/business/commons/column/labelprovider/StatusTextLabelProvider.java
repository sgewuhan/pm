package com.sg.business.commons.column.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.sg.business.model.ILifecycle;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class StatusTextLabelProvider extends ConfiguratorColumnLabelProvider {
	
	@Override
	public String getText(Object element) {
		if(element instanceof ILifecycle){
			ILifecycle lc = (ILifecycle) element;
			return lc.getLifecycleStatusText();
		}else{
			return "";
		}
	}
	
	@Override
	public Image getImage(Object element) {
		return null;
	}
}
