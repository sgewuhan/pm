package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;

public class WorkOvertimeLabelProvider extends ColumnLabelProvider {
	@Override
	public String getText(Object element) {
		return "";
	}
	
	@Override
	public Image getImage(Object element) {
		Work work = (Work)element;
		if(work.isDelayFinish()){
			return BusinessResource.getImage(BusinessResource.IMAGE_FLAG_RED_16);
		}else if(work.isRemindNow()){
			return BusinessResource.getImage(BusinessResource.IMAGE_FLAG_YELLOW_16);
		}
		return null;
	}
}
