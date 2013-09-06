package com.sg.business.commons.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class StatusLabelProvider extends ConfiguratorColumnLabelProvider {
	
	@Override
	public String getText(Object element) {
//		if(element instanceof ILifecycle){
//			ILifecycle lc = (ILifecycle) element;
//			return lc.getLifecycleStatusText();
//		}else{
//			return "";
//		}
		return "";
	}
	
	@Override
	public Image getImage(Object element) {
		if(element instanceof ILifecycle){
			ILifecycle lc = (ILifecycle) element;
			 String s = lc.getLifecycleStatus();
			 return LifecycleToolkit.getLifecycleStatusImage(s);
		}else{
			return null;
		}
	}
}
