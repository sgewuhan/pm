package com.sg.business.model.toolkit;

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
}
