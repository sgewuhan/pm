package com.sg.business.visualization.contentprovider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.ModelRelation;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.StructuredDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.widgets.registry.config.TreeConfigurator;
import com.sg.widgets.viewer.RelationContentProvider;

public class OrgProjectSetContentProvider extends RelationContentProvider {

	private Object[] aviOrg;
	DBCollection projectcol;
	DBCollection orgcol;
	DBCollection usercol;

	public OrgProjectSetContentProvider() {
		super();
		projectcol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		orgcol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		usercol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
	}

	public OrgProjectSetContentProvider(TreeConfigurator configurator) {
		super(configurator);
		projectcol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		orgcol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		PrimaryObject po = (PrimaryObject) parentElement;
		Set<ModelRelation> childrenMRSet = mrmap.get(po.getClass());
		List<PrimaryObject> children = new ArrayList<PrimaryObject>();
		if (childrenMRSet != null && !childrenMRSet.isEmpty()) {
			Iterator<ModelRelation> iter = childrenMRSet.iterator();
			while (iter.hasNext()) {
				ModelRelation mr = iter.next();
				List<PrimaryObject> relationDataList = getRelationByModel(mr,
						po);
				children.addAll(relationDataList);
			}
		}
		return getElements(children);
	}

	@Override
	public boolean hasChildren(Object parentElement) {
//		return super.hasChildren(parentElement);

		PrimaryObject po = (PrimaryObject) parentElement;
		Set<ModelRelation> childrenMRSet = mrmap.get(po.getClass());
		if (childrenMRSet != null && !childrenMRSet.isEmpty()) {
			Iterator<ModelRelation> iter = childrenMRSet.iterator();
			while (iter.hasNext()) {
				ModelRelation mr = iter.next();
				long count = getRelationCountByModel(mr,po);
				if (count > 0) {
					return true;
				}
			}
		}
		return false;

	
		
	}

	public List<PrimaryObject> getRelationByModel(ModelRelation mr,
			PrimaryObject po) {
		Class<? extends PrimaryObject> end2Class = mr.getEnd2Class();
		IRelationConditionProvider irc = mr.getRelationConditionProvider();
		
		DBObject condition = null;
		if ("organization_organization".equals(mr.getId())) {
			
			if (irc != null) {
				condition = irc.getCondition(po);
			} else {
				condition = new BasicDBObject();
				condition.put(mr.getEnd2Key(), po.getValue(mr.getEnd1Key()));
				condition.put(Organization.F__ID, new BasicDBObject().append(
						"$in", getAvailableOrganization()));
			}

		} else if ("organization_projectmanager".equals(mr.getId())) {
				condition = new BasicDBObject();
				condition.put(User.F_USER_ID,
						new BasicDBObject().append("$in", getAvailableUser(po)));
		}
		StructuredDBCollectionDataSetFactory sdf = po
				.getRelationDataSetFactory(end2Class, condition);
		DBObject sort = mr.getSort();
		if (sort != null) {
			sdf.setSort(sort);
		}
		return sdf.getDataSet().getDataItems();
	}
	
	
	public long getRelationCountByModel(ModelRelation mr,PrimaryObject po) {
		Class<? extends PrimaryObject> end2Class = mr.getEnd2Class();
		IRelationConditionProvider irc = mr.getRelationConditionProvider();
		DBObject condition = null;
		if ("organization_organization".equals(mr.getId())) {
			
			if (irc != null) {
				condition = irc.getCondition(po);
			} else {
				condition = new BasicDBObject();
				condition.put(mr.getEnd2Key(), po.getValue(mr.getEnd1Key()));
				condition.put(Organization.F__ID, new BasicDBObject().append(
						"$in", getAvailableOrganization()));
			}

		} else if ("organization_projectmanager".equals(mr.getId())) {
				condition = new BasicDBObject();
				condition.put(User.F_USER_ID,
						new BasicDBObject().append("$in", getAvailableUser(po)));
		}
		StructuredDBCollectionDataSetFactory sdf = po.getRelationDataSetFactory(
				end2Class, condition);
		return sdf.getTotalCount();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object[] getAvailableOrganization() {
		if (aviOrg == null) {
			Set<ObjectId> set = new HashSet<ObjectId>();
			List prjOrgList = projectcol.distinct(
					Project.F_LAUNCH_ORGANIZATION, new BasicDBObject().append(
							ILifecycle.F_LIFECYCLE,
							new BasicDBObject().append("$in", new String[] {
									ILifecycle.STATUS_FINIHED_VALUE,
									ILifecycle.STATUS_WIP_VALUE })));
			set.addAll(prjOrgList);

			List parentOrgList = orgcol.distinct(Organization.F_PARENT_ID,
					new BasicDBObject().append(Organization.F__ID,
							new BasicDBObject().append("$in", prjOrgList)));
			while (parentOrgList != null
					&& !parentOrgList.isEmpty()
					|| (parentOrgList.size() == 1 && parentOrgList.get(0) != null)) {
				set.addAll(parentOrgList);
				parentOrgList = orgcol.distinct(Organization.F_PARENT_ID,
						new BasicDBObject().append(Organization.F__ID,
								new BasicDBObject()
										.append("$in", parentOrgList)));
			}

			aviOrg = set.toArray(new Object[0]);
		}

		return aviOrg;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getAvailableUser(PrimaryObject po) {
			Set<ObjectId> set = new HashSet<ObjectId>();
			List prjManagerList = projectcol.distinct(
					Project.F_CHARGER,
					new BasicDBObject().append(
							ILifecycle.F_LIFECYCLE,
							new BasicDBObject().append("$in", new String[] {
									ILifecycle.STATUS_FINIHED_VALUE,
									ILifecycle.STATUS_WIP_VALUE })).append(
							Project.F_LAUNCH_ORGANIZATION, po.get_id()));
			set.addAll(prjManagerList);
		return set.toArray(new Object[0]);
	}

}
