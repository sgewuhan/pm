package com.sg.business.organization.command;

import java.util.Date;
import java.util.HashSet;import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.sqldb.utility.SQLResult;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;

public class OrgExchange {

	private String orgId;

	private String desc;

	private Set<OrgExchange> children = new HashSet<OrgExchange>();

	private ObjectId pmParentId;

	private OrgExchange parent;

	public static String MESSAGE_DESC = "系统消息：HR系统中存在组织被删除！";
	public static String MESSAGE_CONTENT_AFTER = "”已被删除，请在系统中删除对应的组织！";
	public static String MESSAGE_CONTENT_BEFORE = "组织：“";

	public OrgExchange(String id, boolean isPm) {
		if (isPm) {
			initByPm(id);
		} else {
			initBySql(id);
		}
	}

	public OrgExchange(String childrenOrgId, String childrenDesc,
			OrgExchange childrenParent) {
		this.orgId = childrenOrgId;
		this.desc = childrenDesc;
		this.parent = childrenParent;
		this.children.addAll(initBySqlChildren(this));
	}

	public OrgExchange(String childrenOrgId, String childrenDesc,
			ObjectId childrenPmParentId, OrgExchange childrenParent) {
		this.orgId = childrenOrgId;
		this.desc = childrenDesc;
		this.pmParentId = childrenPmParentId;
		this.parent = childrenParent;
		this.children.addAll(initByPmChildren(this));
	}

	private void initByPm(String id) {
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject condition;
		if (id == null) {
			condition = new BasicDBObject()
					.append(Organization.F_PARENT_ID, id);
		} else {
			condition = new BasicDBObject().append(
					Organization.F_ORGANIZATION_NUMBER, id);
		}
		DBObject row = coll.findOne(condition);
		if (row != null) {
			orgId = (String) row.get(Organization.F_ORGANIZATION_NUMBER);
			desc = (String) row.get(Organization.F_FULLDESC);
			pmParentId = (ObjectId) row.get(Organization.F__ID);
			children.addAll(initByPmChildren(this));
		}
	}

	private Set<OrgExchange> initByPmChildren(OrgExchange parentOrgExchange) {
		Set<OrgExchange> childrenSet = new HashSet<OrgExchange>();
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBCursor childCursor = coll.find(new BasicDBObject().append(
				Organization.F_PARENT_ID, parentOrgExchange.pmParentId));
		while (childCursor.hasNext()) {
			DBObject childRow = childCursor.next();
			String childrenOrgId = (String) childRow
					.get(Organization.F_ORGANIZATION_NUMBER);
			String childrenDesc = (String) childRow
					.get(Organization.F_FULLDESC);
			ObjectId childrenPmId = (ObjectId) childRow.get(Organization.F__ID);
			OrgExchange orgExchange = new OrgExchange(childrenOrgId,
					childrenDesc, childrenPmId, parentOrgExchange);
			if(parentOrgExchange.parent == null){
			System.out.println(orgExchange.orgId);
			}
			childrenSet.add(orgExchange);

		}
		return childrenSet;
	}

	private void initBySql(String id) {
		try {
			SQLResult result;
			if (id == null) {
				result = SQLUtil.SQL_QUERY("hr",
						"select * from pm_unit where ldunitid = '1'");
			} else {
				result = SQLUtil.SQL_QUERY("hr",
						"select * from pm_unit where unitid = '" + id + "'");
			}
			if (!result.isEmpty()) {
				List<SQLRow> dataSet = result.getData();
				SQLRow row = dataSet.get(0);
				orgId = (String) row.getValue("unitid");
				desc = (String) row.getValue("unitname");
				children.addAll(initBySqlChildren(this));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Set<OrgExchange> initBySqlChildren(OrgExchange childrenParent) {
		Set<OrgExchange> childrenSet = new HashSet<OrgExchange>();
		SQLResult result;
		SQLRow row;
		try {
			result = SQLUtil.SQL_QUERY("hr",
					"select * from pm_unit where ldunitid = '"
							+ childrenParent.orgId + "'");

			if (!result.isEmpty()) {
				Iterator<SQLRow> iter = result.iterator();
				while (iter.hasNext()) {
					row = iter.next();
					String childrenOrgId = (String) row.getValue("unitid");
					String childrenDesc = (String) row.getValue("unitname");

					OrgExchange orgExchange = new OrgExchange(childrenOrgId,
							childrenDesc, childrenParent);
					childrenSet.add(orgExchange);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childrenSet;
	}

	public String getOrgId() {
		return orgId;
	}

	public String getDesc() {
		return desc;
	}

	public Set<OrgExchange> getChildren() {
		return children;
	}

	public ObjectId getPmParentId() {
		return pmParentId;
	}

	public OrgExchange getParent() {
		return parent;
	}

	public ObjectId getParentId() {
		ObjectId pmId = null;
		if (parent != null) {
			DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
					IModelConstants.C_ORGANIZATION);
			DBObject condition;
			condition = new BasicDBObject().append(
					Organization.F_ORGANIZATION_NUMBER, parent.orgId);
			DBObject row = coll.findOne(condition);
			if (row != null) {
				pmId = (ObjectId) row.get(Organization.F__ID);
			}
		}
		return pmId;
	}

	public Set<OrgExchange> getDifferentChildren(OrgExchange otherOrg) {
		Set<OrgExchange> result = new HashSet<OrgExchange>();
		result.addAll(children);
		result.removeAll(otherOrg.children);
		return result;
	}

	public Set<OrgExchange> getSameChildren(OrgExchange otherOrg) {
		Set<OrgExchange> result = new HashSet<OrgExchange>();
		result.addAll(children);
		result.removeAll(otherOrg.children);
		Set<OrgExchange> result1 = new HashSet<OrgExchange>();
		result1.addAll(children);
		result1.removeAll(result);
		return result1;
	}

	public boolean getDifferentName(OrgExchange otherOrg) {
		return desc.equals(otherOrg.desc);
	}

	public void doAddAllHR() {
		ObjectId parentId = this.getParentId();
		doAddAllHR(parentId);
	}

	public void doAddAllHR(ObjectId parentId) {
		ObjectId _id = new ObjectId();
		doAddHR(this, _id, parentId);
		for (OrgExchange orgExchangeChildren : children) {
			orgExchangeChildren.doAddAllHR(_id);
		}
	}

	public void doAddHR(OrgExchange otherOrg, ObjectId _id, ObjectId parentId) {
		DBCollection roleCollection = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ORGANIZATION);

		BasicDBObject data = new BasicDBObject();
		data.put(Organization.F_ORGANIZATION_NUMBER, otherOrg.orgId);
		data.put(Organization.F_FULLDESC, otherOrg.desc);
		if (otherOrg.parent == null) {
			data.put(Organization.F_DESC, otherOrg.desc);
		} else {
			data.put(Organization.F_DESC,
					otherOrg.desc.replaceFirst(otherOrg.parent.desc, ""));
		}
		data.put(Organization.F__ID, _id);
		data.put(Organization.F_PARENT_ID, parentId);
		WriteResult wr = roleCollection.insert(data);
		if (wr.getN() > 0) {
			ModelService.createModelObject(data, Organization.class);
		}
	}

	public void doRenameHR() {
		DBCollection organizationCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		organizationCol.update(new BasicDBObject().append(
				Organization.F_ORGANIZATION_NUMBER, orgId), new BasicDBObject()
				.append("$set", new BasicDBObject().append(
						Organization.F_FULLDESC, desc)), false, true);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrgExchange other = (OrgExchange) obj;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		return true;
	}

	public void sendMessage(Set<OrgExchange> removeSet) {
		String messageContent = null;
		for (OrgExchange orgExchange : removeSet) {
			if (messageContent == null) {
				messageContent = OrgExchange.MESSAGE_CONTENT_BEFORE + orgExchange.desc
						+ OrgExchange.MESSAGE_CONTENT_AFTER;
			} else {
				messageContent = messageContent + (char) 13
						+ OrgExchange.MESSAGE_CONTENT_BEFORE + orgExchange.desc
						+ OrgExchange.MESSAGE_CONTENT_AFTER;
			}
		}
		DBCollection roleCollection = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_MESSAGE);

		BasicDBObject data = new BasicDBObject();
		data.put(Message.F_CONTENT, messageContent);
		data.put(Message.F_SENDDATE, new Date());
		data.put(Message.F_DESC, OrgExchange.MESSAGE_DESC);
		List<PrimaryObject> user = User.getAdmin();

		BasicBSONList recieverList = new BasicBSONList();
		for (int i = 0; i < user.size(); i++) {
			recieverList.add(user.get(i).getValue(User.F_USER_ID));
		}
		data.put(Message.F_RECIEVER, recieverList);

		WriteResult wr = roleCollection.insert(data);
		if (wr.getN() > 0) {
			ModelService.createModelObject(data, Message.class);
		}
	}

}
