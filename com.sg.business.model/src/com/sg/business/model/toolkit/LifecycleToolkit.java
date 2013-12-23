package com.sg.business.model.toolkit;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.sg.business.model.ILifecycle;
import com.sg.business.resource.BusinessResource;

public class LifecycleToolkit {
	public static String getLifecycleStatusText(String lifecycleValue) {
		if (ILifecycle.STATUS_CANCELED_VALUE.equals(lifecycleValue)) {
			return ILifecycle.STATUS_CANCELED_TEXT;
		}
		if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycleValue)) {
			return ILifecycle.STATUS_FINIHED_TEXT;
		}
		if (ILifecycle.STATUS_NONE_VALUE.equals(lifecycleValue)) {
			return ILifecycle.STATUS_NONE_TEXT;
		}
		if (ILifecycle.STATUS_ONREADY_VALUE.equals(lifecycleValue)) {
			return ILifecycle.STATUS_ONREADY_TEXT;
		}
		if (ILifecycle.STATUS_PAUSED_VALUE.equals(lifecycleValue)) {
			return ILifecycle.STATUS_PAUSED_TEXT;
		}
		if (ILifecycle.STATUS_WIP_VALUE.equals(lifecycleValue)) {
			return ILifecycle.STATUS_WIP_TEXT;
		}
		return ILifecycle.STATUS_NONE_TEXT;
	}

	public static Image getLifecycleStatusImage(String lifecycleValue) {
		if (ILifecycle.STATUS_CANCELED_VALUE.equals(lifecycleValue)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_CANCELED_16);
		}
		if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycleValue)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_FINISHED_16);
		}
		if (ILifecycle.STATUS_NONE_VALUE.equals(lifecycleValue)) {
			return BusinessResource
					.getImage(BusinessResource.IMAGE_PREPARING_16);
		}
		if (ILifecycle.STATUS_ONREADY_VALUE.equals(lifecycleValue)) {
			return BusinessResource.getImage(BusinessResource.IMAGE_READY_16);
		}
		if (ILifecycle.STATUS_PAUSED_VALUE.equals(lifecycleValue)) {
			return BusinessResource.getImage(BusinessResource.IMAGE_PAUSEED_16);
		}
		if (ILifecycle.STATUS_WIP_VALUE.equals(lifecycleValue)) {
			return BusinessResource.getImage(BusinessResource.IMAGE_WIP_16);
		}
		return null;
	}
	
	public static void checkActionMessage(List<Object[]> messages) throws Exception{
		if(messages==null||messages.isEmpty()){
			return;
		}
		
		for (int i = 0; i < messages.size(); i++) {
			String msgtext = messages.get(i)[0]==null?"":messages.get(i)[0].toString(); //$NON-NLS-1$
			Object msgObject = messages.get(i)[1];
			Object msgType = messages.get(i)[2];
			if(new Integer(SWT.ICON_ERROR).equals(msgType)){
				throw new Exception(msgtext+"\n"+msgObject); //$NON-NLS-1$
			}
		}
	}
}
