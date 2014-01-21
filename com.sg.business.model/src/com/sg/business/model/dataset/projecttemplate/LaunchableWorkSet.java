package com.sg.business.model.dataset.projecttemplate;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class LaunchableWorkSet extends DataSetFactory {

	private User user;
	private DBCollection workdCol;

	public LaunchableWorkSet() {
		String userId = new CurrentAccountContext().getConsignerId();
		user = UserToolkit.getUserById(userId);
		workdCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK_DEFINITION);
	}

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		// 得到当前用户所在的组织及上级组织
		List<ObjectId> orgidList = new ArrayList<ObjectId>();
		Organization org = user.getOrganization();
		while (org != null) {
			ObjectId id = org.get_id();
			orgidList.add(id);
			org = (Organization) org.getParentOrganization();
		}
		DBCursor cur = workdCol.find(new BasicDBObject()
			.append(WorkDefinition.F_ORGANIZATION_ID,new BasicDBObject().append("$in", orgidList))
			.append(WorkDefinition.F_ACTIVATED,Boolean.TRUE)
			.append(WorkDefinition.F_WORK_TYPE,WorkDefinition.WORK_TYPE_STANDLONE)
			.append(WorkDefinition.F_INTERNAL_TYPE,new BasicDBObject().append("$nin", new String[]{WorkDefinition.INTERNAL_TYPE_CHANGE,WorkDefinition.INTERNAL_TYPE_CHANGEITEM,WorkDefinition.INTERNAL_TYPE_CHANGERANGE}))
			);
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		cur.sort(new BasicDBObject().append(WorkDefinition.F_ORGANIZATION_ID, 1));
		while(cur.hasNext()){
			DBObject dbo = cur.next();
			result.add(ModelService.createModelObject(dbo, WorkDefinition.class));
		}
		return result;
	}

	@Override
	public long getTotalCount() {
		return 100;
	}

}
