package com.sg.business.visualization.labelprovide;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;

public class ProjectNumber extends ColumnLabelProvider {

	private DBCollection orgCol;
	public ProjectNumber() {
		super();
		orgCol = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_PROJECT);
	}
	
	@Override
	public String getText(Object element) {
		PrimaryObject dbo = ((PrimaryObject)element);
		if(dbo instanceof Organization){
			Organization organization = (Organization) dbo;
			DBCursor cur = orgCol.find(new BasicDBObject().append(Project.F_LAUNCH_ORGANIZATION,organization.get_id()));
			return String.valueOf(cur.size());
		}
		return String.valueOf(0);
	}


}
