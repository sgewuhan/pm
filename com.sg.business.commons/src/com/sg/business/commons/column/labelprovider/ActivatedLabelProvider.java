package com.sg.business.commons.column.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class ActivatedLabelProvider extends ConfiguratorColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		PrimaryObject po = (PrimaryObject)element;
		String key = getFieldName();
		if(Boolean.TRUE.equals(po.getValue(key))){
			return BusinessResource.getImage(BusinessResource.IMAGE_ACTIVATED_16);
		}else if(Boolean.FALSE.equals(po.getValue(key))){
			return BusinessResource.getImage(BusinessResource.IMAGE_DISACTIVATED_16);
		}else {
			return null;
		}
		
	}

	@Override
	public String getText(Object element) {
		return ""; //$NON-NLS-1$
	}


}
