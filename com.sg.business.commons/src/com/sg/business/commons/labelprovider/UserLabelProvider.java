package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.User;
import com.sg.business.resource.BusinessResource;

public class UserLabelProvider extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		if(((PrimaryObject)element).getValue(User.F_ORGANIZATION_ID)==null){
			return BusinessResource.getImage(BusinessResource.IMAGE_USER2_16);
		}else{
			return BusinessResource.getImage(BusinessResource.IMAGE_USER_16);
		}
	}

	@Override
	public String getText(Object element) {
		return (String)((PrimaryObject)element).getValue(User.F_USER_ID);
	}
}
