package com.sg.business.performence.works;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class MonthFolder extends PrimaryObject {

	public static final String F_USERID = "userid"; //$NON-NLS-1$
	public static final String F_YEAR = "year"; //$NON-NLS-1$
	public static final String F_MONTH = "month"; //$NON-NLS-1$

	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}
	
	@Override
	public String getLabel() {
		return ""+getValue(F_YEAR)+"-"+((Integer)getValue(F_MONTH)+1); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_FOLDER_16);
	}
}
