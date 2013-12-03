package com.sg.business.model;

import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class BIDataBuilder implements ISchedualJobRunnable {

	private DBCollection col;

	private static ConcurrentHashMap<ObjectId, OrganizationProjectProvider> cache = new ConcurrentHashMap<ObjectId, OrganizationProjectProvider>();

	public BIDataBuilder() {
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
	}

	@Override
	public boolean run() throws Exception {
		Commons.loginfo("初始化组织项目集绩效分析器...");
		cache.clear();
		DBCursor cur = col.find(null,
				new BasicDBObject().append(Organization.F__ID, 1));
		while (cur.hasNext()) {
			DBObject dbObject = (DBObject) cur.next();
			ObjectId id = (ObjectId) dbObject.get(Organization.F__ID);
			loadOrganization(id, true);
		}
		Commons.loginfo("组织项目集绩效分析器初始化完成");
		return true;
	}

	private static OrganizationProjectProvider loadOrganization(ObjectId id,
			boolean fullload) {
		if (id != null) {
			Organization org = ModelService.createModelObject(
					Organization.class, id);

			OrganizationProjectProvider projectProvider = ModelService
					.createModelObject(OrganizationProjectProvider.class);
			projectProvider.setOrganization(org);
			if (fullload) {
				projectProvider.getData();
				cache.put(id, projectProvider);
			}
			return projectProvider;
		}
		return null;
	}

	public static OrganizationProjectProvider getProjectProvider(ObjectId orgId) {
		if (orgId != null) {
			OrganizationProjectProvider p = cache.get(orgId);
			if (p == null) {
				return loadOrganization(orgId, false);
			} else {
				return p;
			}
		}
		return null;
	}

}
