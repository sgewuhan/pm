package com.sg.business.commons.labelprovider;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.RoleDefinition;
/**
 * 传入的是PrimeryObject,需要强制转换为RoleDefinition
 * @author Administrator
 *
 */
public class ProjectRoleLabelProvider2 extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		DBObject data = ((PrimaryObject)element).get_data();
		RoleDefinition rd = ModelService.createModelObject(data,RoleDefinition.class);
		return rd.getImage();
	}

	@Override
	public String getText(Object element) {
		DBObject data = ((PrimaryObject)element).get_data();
		ProjectRole rd = ModelService.createModelObject(ProjectRole.class,(ObjectId)data.get(ProjectRole.F__ID));
		return rd.getLabel();
	}


}
