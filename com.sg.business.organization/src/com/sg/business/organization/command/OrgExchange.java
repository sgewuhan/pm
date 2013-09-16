package com.sg.business.organization.command;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.sqldb.utility.SQLResult;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;
import com.sg.widgets.part.BackgroundContext;

/**
 * <p>
 * 同步组织
 * </p>
 * 用于同步HR和PM系统的组织，包括以下功能： <li>根据组织编号构造HR和PM系统完整的组织结构（其中包括组织编号、组织全称、子组织、父组织） <li>
 * 获取差异的子组织 <li> <li> <li>
 * 
 * @author 杨骏
 * 
 */
public class OrgExchange {

	/**
	 * 组织编号
	 */
	private String orgId;

	/**
	 * 组织全称
	 */
	private String desc;

	/**
	 * 子组织
	 */
	private Set<OrgExchange> children = new HashSet<OrgExchange>();

	/**
	 * 父组织的PM的_ID
	 */
	private ObjectId pmParentId;

	/**
	 * 组织的PMID
	 */
	private ObjectId pmId;

	/**
	 * 父组织
	 */
	private OrgExchange parent;

	private boolean checkHR;

	/**
	 * 消息标题
	 */
	public static String MESSAGE_DESC = "系统消息：HR系统中存在组织被删除或更改了上级组织！";

	/**
	 * 消息内容（后半部分）
	 */
	public static String MESSAGE_CONTENT_AFTER = "”已被删除或更改了上级组织，请在PM系统中更改归属其组织的人员、角色、项目等信息，并删除对应的组织！";

	/**
	 * 消息内容（后半部分）
	 */
	public static String MESSAGE_CONTENT_BEFORE = "HR系统中组织：“";

	/**
	 * 构造函数
	 * 
	 * @param id
	 *            : 组织编号
	 * @param isPm
	 *            : 标识是从PM系统还是从HR系统中读取组织，True时为PM系统，False时为HR系统
	 */
	public OrgExchange(String id, boolean isPm) throws Exception {
		if (isPm) {
			initByPm(id);
		} else {
			initBySql(id);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param OrgId
	 *            : 组织编号
	 * @param Desc
	 *            : 组织全称
	 * @param Parent
	 *            : 父组织
	 */
	public OrgExchange(String OrgId, String Desc, OrgExchange Parent) {
		this.orgId = OrgId;
		this.desc = Desc;
		this.parent = Parent;
		this.children.addAll(initBySqlChildren(this));
	}

	/**
	 * 构造函数
	 * 
	 * @param OrgId
	 *            : 组织编号
	 * @param Desc
	 *            : 组织全称
	 * @param PmParentId
	 *            : PM父组织id
	 * @param Parent
	 *            : 父组织
	 */
	public OrgExchange(String OrgId, String Desc, ObjectId PmParentId,
			OrgExchange Parent) {
		this.orgId = OrgId;
		this.desc = Desc;
		this.setPmId(PmParentId);
		this.pmParentId = Parent.getPmId();
		this.parent = Parent;
		this.children.addAll(initByPmChildren(this));
	}

	/**
	 * 初始化PM系统的组织
	 * 
	 * @param id
	 *            : 组织编号
	 */
	private void initByPm(String id) throws Exception {
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		if(coll == null ){
			throw new Exception("无法连接PM数据库");
		}
		DBObject condition;
		// 判断是否顶级组织
		// 为null表示为顶级组织，这是通过parent_id来获取PM组织
		// 不为null表示为非顶级组织，这时通过organizationnumber来获取PM组织
		if (id == null) {
			condition = new BasicDBObject()
					.append(Organization.F_PARENT_ID, id);
		} else {
			condition = new BasicDBObject().append(
					Organization.F_ORGANIZATION_NUMBER, id);
		}
		// 构造PM系统的组织
		DBObject row = coll.findOne(condition);
		if (row != null) {
			orgId = (String) row.get(Organization.F_ORGANIZATION_NUMBER);
			desc = (String) row.get(Organization.F_FULLDESC);
			pmParentId = (ObjectId) row.get(Organization.F_PARENT_ID);
			setPmId((ObjectId) row.get(Organization.F__ID));
			children.addAll(initByPmChildren(this));
		}
	}

	/**
	 * 初始化PM系统的子组织
	 * 
	 * @param parentOrgExchange
	 *            : {@link OrgExchange},父组织
	 * @return {@link HashSet},为子组织集合
	 */
	private Set<OrgExchange> initByPmChildren(OrgExchange parentOrgExchange) {
		Set<OrgExchange> childrenSet = new HashSet<OrgExchange>();
		// 通过parent_id来获取子组织
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBCursor childCursor = coll.find(new BasicDBObject().append(
				Organization.F_PARENT_ID, parentOrgExchange.getPmId()));
		// 循环构造当前组织的子组织
		while (childCursor.hasNext()) {
			DBObject childRow = childCursor.next();
			String childrenOrgId = (String) childRow
					.get(Organization.F_ORGANIZATION_NUMBER);
			String childrenDesc = (String) childRow
					.get(Organization.F_FULLDESC);
			ObjectId childrenPmId = (ObjectId) childRow.get(Organization.F__ID);
			OrgExchange orgExchange = new OrgExchange(childrenOrgId,
					childrenDesc, childrenPmId, parentOrgExchange);
			childrenSet.add(orgExchange);

		}
		return childrenSet;
	}

	/**
	 * 初始化PM系统的组织
	 * 
	 * @param id
	 *            : 组织编号
	 */
	private void initBySql(String id) throws Exception {
		try {
			SQLResult result;
			// 判断是否顶级组织
			// 为null表示为顶级组织，这是通过ldunitid=‘1’来获取HR组织
			// 不为null表示为非顶级组织，这时通过unitid来获取HR组织
			if (id == null) {
				result = SQLUtil.SQL_QUERY("hr",
						"select * from pm_unit where ldunitid = '1'");
				// result =
				// SQLUtil.SQL_QUERY("hr","select * from tb_nczz.pm_unit where ldunitid = '1'");
			} else {
				result = SQLUtil.SQL_QUERY("hr",
						"select * from pm_unit where unitid = '" + id + "'");

				// result =
				// SQLUtil.SQL_QUERY("hr","select * from tb_nczz.pm_unit where unitid = '"
				// + id + "'");
			}
			// 构造HR系统的组织
			if (!result.isEmpty()) {
				List<SQLRow> dataSet = result.getData();
				SQLRow row = dataSet.get(0);
				orgId = "" + row.getValue("unitid");
				desc = "" + row.getValue("unitname");
				children.addAll(initBySqlChildren(this));
			}
		} catch (Exception e) {
			throw new Exception("无法连接HR数据库");
		}
	}

	/**
	 * 初始化HR系统的子组织
	 * 
	 * @param parentOrgExchange
	 *            : {@link OrgExchange},父组织
	 * @return {@link HashSet},为子组织集合
	 */
	private Set<OrgExchange> initBySqlChildren(OrgExchange parentOrgExchange) {
		Set<OrgExchange> childrenSet = new HashSet<OrgExchange>();
		SQLResult result;
		SQLRow row;
		try {
			// 通过ldunitid来获取子组织
			result = SQLUtil.SQL_QUERY("hr",
					"select * from pm_unit where ldunitid = '"
							+ parentOrgExchange.orgId + "'");
			// result =
			// SQLUtil.SQL_QUERY("hr","select * from tb_nczz.pm_unit where ldunitid = '"+
			// parentOrgExchange.orgId + "'");
			if (!result.isEmpty()) {
				// 循环构造当前组织的子组织
				Iterator<SQLRow> iter = result.iterator();
				while (iter.hasNext()) {
					row = iter.next();
					String childrenOrgId = "" + row.getValue("unitid");
					String childrenDesc = "" + row.getValue("unitname");

					OrgExchange orgExchange = new OrgExchange(childrenOrgId,
							childrenDesc, parentOrgExchange);
					childrenSet.add(orgExchange);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childrenSet;
	}

	/**
	 * @return 当前组织编号
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @return 当前组织全称
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return 当前组织的子组织，{@link HashSet}
	 */
	public Set<OrgExchange> getChildren() {
		return children;
	}

	/**
	 * @return 当前组织的PM系统的parent_id，{@link ObjectId}
	 */
	public ObjectId getPmParentId() {
		return pmParentId;
	}

	/**
	 * @return 当前组织的父组织，{@link OrgExchange}
	 */
	public OrgExchange getParent() {
		return parent;
	}

	/**
	 * @return 当前组织的PM系统_ID，{@link ObjectId}
	 */
	public ObjectId getPmId() {
		return pmId;
	}

	/**
	 * 设置当前组织的PM系统_ID
	 * 
	 * @param pmId
	 *            : {@link ObjectId},PM系统_ID
	 */
	public void setPmId(ObjectId pmId) {
		this.pmId = pmId;
	}

	public void setCheckHR(boolean checkHR) {
		this.checkHR = checkHR;
	}

	/**
	 * @return : {@link ObjectId}类型，当前组织的PM系统parent_id，在使用时需要设置属性{@link #parent}
	 *         的{@link #orgId}
	 */
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

	/**
	 * 获取两个组织的子组织的差异部分
	 * 
	 * @param otherOrg
	 *            : {@link OrgExchange},被比较的组织
	 * @return : {@link HashSet},子组织的差异部分
	 */
	public Set<OrgExchange> getDifferentChildren(OrgExchange otherOrg) {
		Set<OrgExchange> result = new HashSet<OrgExchange>();
		result.addAll(children);
		result.removeAll(otherOrg.children);
		return result;
	}

	/**
	 * 获取两个组织的子组织的相同部分
	 * 
	 * @param otherOrg
	 *            : {@link OrgExchange},被比较的组织
	 * @return : {@link HashSet},子组织的相同部分
	 */
	public Set<OrgExchange> getSameChildren(OrgExchange otherOrg) {
		Set<OrgExchange> result = new HashSet<OrgExchange>();
		result.addAll(children);
		result.removeAll(getDifferentChildren(otherOrg));
		return result;
	}

	/**
	 * 比较两个组织的全称是否相同
	 * 
	 * @param otherOrg
	 *            : {@link OrgExchange},被比较的组织
	 * @return : 相同时为true。
	 */
	public boolean getDifferentName(OrgExchange otherOrg) {
		return desc.equals(otherOrg.desc);
	}

	/**
	 * 将当前组织插入到PM系统中
	 */
	public void doAddAllHR() {
		// 获取当前组织的parentId
		ObjectId parentId = this.getParentId();
		doAddAllHR(parentId);
	}

	/**
	 * 将当前组织插入到PM系统中
	 * 
	 * @param parentId
	 *            : PM系统的parentId
	 */
	public void doAddAllHR(ObjectId parentId) {
		if (!checkHR) {
			return;
		}
		ObjectId _id = new ObjectId();
		// 插入当前组织
		doAddHR(this, _id, parentId);
		// 循环迭代调用本方法，插入子组织
		for (OrgExchange orgExchangeChildren : children) {
			if (orgExchangeChildren.checkHR) {
				orgExchangeChildren.doAddAllHR(_id);
			}
		}
	}

	/**
	 * 将组织插入到PM系统中
	 * 
	 * @param otherOrg
	 *            : 需要插入的组织
	 * @param _id
	 *            : 插入的PM的_id
	 * @param parentId
	 *            : PM系统的parentId
	 */
	public void doAddHR(OrgExchange otherOrg, ObjectId _id, ObjectId parentId) {
		DBCollection roleCollection = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ORGANIZATION);

		// 设置插入的组织信息
		BasicDBObject data = new BasicDBObject();
		data.put(Organization.F_ORGANIZATION_NUMBER, otherOrg.orgId);
		data.put(Organization.F_FULLDESC, otherOrg.desc);
		// 如果没有父组织，则默认将全称作为简称，如果存在，则简称将去掉和父组相同的部分
		// 同时设置组织的编辑器，如果没有父组织，则使用顶级组织编辑器，如果存在，则使用部门和团队编辑器
		if (otherOrg.parent == null) {
			data.put(Organization.F_DESC, otherOrg.desc);
			data.put(PrimaryObject.F__EDITOR, Organization.EDITOR_TEAM);
		} else {
			data.put(Organization.F_DESC,
					otherOrg.desc.replaceFirst(otherOrg.parent.desc, ""));
			data.put(PrimaryObject.F__EDITOR, Organization.EDITOR_SUBTEAM);
		}
		// 设置系统信息
		DBObject accountInfo = new BasicDBObject();
		AccountInfo account = new BackgroundContext().getAccountInfo();
		accountInfo.put("userid", account.getUserId());
		accountInfo.put("username", account.getUserName());
		data.put(Organization.F__ID, _id);
		data.put(Organization.F_PARENT_ID, parentId);
		data.put(PrimaryObject.F__CDATE, new Date());
		data.put(PrimaryObject.F__CACCOUNT, accountInfo);

		WriteResult wr = roleCollection.insert(data);
		if (wr.getN() > 0) {
			ModelService.createModelObject(data, Organization.class);
		}
	}

	/**
	 * 修改PM系统中当前组织的全称
	 */
	public void doRenameHR() {
		if (!checkHR) {
			return;
		}
		Organization organization;
		organization = ModelService.createModelObject(Organization.class);
		organization.setValue(Organization.F__ID, pmId);
		organization.setValue(Organization.F_FULLDESC, desc);
		try {
			organization.doSave(new BackgroundContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/**
	 * 发送需要消息给系统管理员，内容为需要删除的组织
	 * 
	 * @param removeSet
	 *            : {@link HashSet}，需要删除的组织
	 */
	// public void sendMessage(Set<OrgExchange> removeSet) {
	// Message message;
	// // 设置消息内容
	// String messageContent = null;
	// for (OrgExchange orgExchange : removeSet) {
	// messageContent = setMessageContent(orgExchange);
	// }
	//
	// // 获取系统管理员角色的用户信息
	// List<PrimaryObject> user = User.getAdmin();
	// BasicBSONList recieverList = new BasicBSONList();
	// for (int i = 0; i < user.size(); i++) {
	// recieverList.add(user.get(i).getValue(User.F_USER_ID));
	// }
	//
	// message = ModelService.createModelObject(Message.class);
	// message.setValue(Message.F_CONTENT, messageContent);
	// message.setValue(Message.F_DESC, OrgExchange.MESSAGE_DESC);
	// message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);
	// message.setValue(Message.F_RECIEVER, recieverList);
	// try {
	// message.doSave(new BackgroundContext());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private String setMessageContent(OrgExchange orgExchange) {
	// String messageContent = null;
	// if (orgExchange.checkHR) {
	// if (messageContent == null) {
	// messageContent = OrgExchange.MESSAGE_CONTENT_BEFORE
	// + orgExchange.desc + OrgExchange.MESSAGE_CONTENT_AFTER;
	// }
	//
	// for (OrgExchange childrenOrgExchange : orgExchange.children) {
	// messageContent = messageContent + "<br/>" +
	// setMessageContent(childrenOrgExchange);
	// }
	// }
	//
	// return messageContent;
	// }
}
