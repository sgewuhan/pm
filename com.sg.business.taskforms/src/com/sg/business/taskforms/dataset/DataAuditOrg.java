package com.sg.business.taskforms.dataset;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.taskforms.IRoleConstance;

public class DataAuditOrg extends SingleDBCollectionDataSetFactory {

	public DataAuditOrg() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		DBCollection roleCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE);
		DBCursor roleCursor = roleCol.find(new BasicDBObject().append(
				Role.F_ROLE_NUMBER, IRoleConstance.ROLE_DATAAUDIT_ID));
		while (roleCursor.hasNext()) {
			DBObject roleData = roleCursor.next();
			ObjectId org_id = (ObjectId) roleData.get(Role.F_ORGANIZATION_ID);
			Organization org = ModelService.createModelObject(
					Organization.class, org_id);
			result.add(org);
		}
		return new DataSet(result);
	}

}
