package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.RoleDefinition;
/**
 * 传入的是PrimeryObject,需要强制转换为RoleDefinition
 * @author Administrator
 *
 */
public class RoleDefinitionLabelProvider2 extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		DBObject data = ((PrimaryObject)element).get_data();
		RoleDefinition rd = ModelService.createModelObject(data,RoleDefinition.class);
		return rd.getImage();
	}

	@Override
	public String getText(Object element) {
		DBObject data = ((PrimaryObject)element).get_data();
		RoleDefinition rd = ModelService.createModelObject(data,RoleDefinition.class);
		return rd.getLabel();
	}


}
