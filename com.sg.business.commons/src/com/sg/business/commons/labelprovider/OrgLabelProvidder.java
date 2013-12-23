package com.sg.business.commons.labelprovider;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class OrgLabelProvidder extends ConfiguratorColumnLabelProvider {

	@Override
	protected Object getValue(Object element) {
		if (element instanceof PrimaryObject) {
			return super.getValue(element);
		}else{
			return element;
		}
	}

	@Override
	public String getText(Object element) {
		Organization org = getOrganization(element);
		
		return org == null?"":org.getLabel(); //$NON-NLS-1$
	}
	
	private Organization getOrganization(Object element) {
		if (element instanceof PrimaryObject) {
			Object value = ((PrimaryObject) element).getValue(getFieldName());
			if (value instanceof ObjectId) {
				return ModelService.createModelObject(
						Organization.class, (ObjectId) value);
			}
		} else if (element instanceof ObjectId) {
			return ModelService.createModelObject(
					Organization.class, (ObjectId) element);
		} else if (element instanceof DBObject) {
			return ModelService.createModelObject(
					(DBObject) element, Organization.class);
		} else if(element instanceof List<?> &&((List<?>)element).size()>0 ){
			return getOrganization(((List<?>)element).get(0));
		} else if(element instanceof Object[]){
			return getOrganization(((Object[])element)[0]);

		}
		return null;
	}

	@Override
	public Image getImage(Object element) {
		Organization org = getOrganization(element);
		return org==null?null:org.getImage();
	}
}
