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
 * ͬ����֯
 * </p>
 * ����ͬ��HR��PMϵͳ����֯���������¹��ܣ� <li>������֯��Ź���HR��PMϵͳ��������֯�ṹ�����а�����֯��š���֯ȫ�ơ�����֯������֯�� <li>
 * ��ȡ���������֯ <li> <li> <li>
 * 
 * @author �
 * 
 */
public class OrgExchange {

	/**
	 * ��֯���
	 */
	private String orgId;

	/**
	 * ��֯ȫ��
	 */
	private String desc;

	/**
	 * ����֯
	 */
	private Set<OrgExchange> children = new HashSet<OrgExchange>();

	/**
	 * ����֯��PM��_ID
	 */
	private ObjectId pmParentId;

	/**
	 * ��֯��PMID
	 */
	private ObjectId pmId;

	/**
	 * ����֯
	 */
	private OrgExchange parent;

	private boolean checkHR;

	/**
	 * ��Ϣ����
	 */
	public static String MESSAGE_DESC = "ϵͳ��Ϣ��HRϵͳ�д�����֯��ɾ����������ϼ���֯��";

	/**
	 * ��Ϣ���ݣ���벿�֣�
	 */
	public static String MESSAGE_CONTENT_AFTER = "���ѱ�ɾ����������ϼ���֯������PMϵͳ�и��Ĺ�������֯����Ա����ɫ����Ŀ����Ϣ����ɾ����Ӧ����֯��";

	/**
	 * ��Ϣ���ݣ���벿�֣�
	 */
	public static String MESSAGE_CONTENT_BEFORE = "HRϵͳ����֯����";

	/**
	 * ���캯��
	 * 
	 * @param id
	 *            : ��֯���
	 * @param isPm
	 *            : ��ʶ�Ǵ�PMϵͳ���Ǵ�HRϵͳ�ж�ȡ��֯��TrueʱΪPMϵͳ��FalseʱΪHRϵͳ
	 */
	public OrgExchange(String id, boolean isPm) throws Exception {
		if (isPm) {
			initByPm(id);
		} else {
			initBySql(id);
		}
	}

	/**
	 * ���캯��
	 * 
	 * @param OrgId
	 *            : ��֯���
	 * @param Desc
	 *            : ��֯ȫ��
	 * @param Parent
	 *            : ����֯
	 */
	public OrgExchange(String OrgId, String Desc, OrgExchange Parent) {
		this.orgId = OrgId;
		this.desc = Desc;
		this.parent = Parent;
		this.children.addAll(initBySqlChildren(this));
	}

	/**
	 * ���캯��
	 * 
	 * @param OrgId
	 *            : ��֯���
	 * @param Desc
	 *            : ��֯ȫ��
	 * @param PmParentId
	 *            : PM����֯id
	 * @param Parent
	 *            : ����֯
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
	 * ��ʼ��PMϵͳ����֯
	 * 
	 * @param id
	 *            : ��֯���
	 */
	private void initByPm(String id) throws Exception {
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		if(coll == null ){
			throw new Exception("�޷�����PM���ݿ�");
		}
		DBObject condition;
		// �ж��Ƿ񶥼���֯
		// Ϊnull��ʾΪ������֯������ͨ��parent_id����ȡPM��֯
		// ��Ϊnull��ʾΪ�Ƕ�����֯����ʱͨ��organizationnumber����ȡPM��֯
		if (id == null) {
			condition = new BasicDBObject()
					.append(Organization.F_PARENT_ID, id);
		} else {
			condition = new BasicDBObject().append(
					Organization.F_ORGANIZATION_NUMBER, id);
		}
		// ����PMϵͳ����֯
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
	 * ��ʼ��PMϵͳ������֯
	 * 
	 * @param parentOrgExchange
	 *            : {@link OrgExchange},����֯
	 * @return {@link HashSet},Ϊ����֯����
	 */
	private Set<OrgExchange> initByPmChildren(OrgExchange parentOrgExchange) {
		Set<OrgExchange> childrenSet = new HashSet<OrgExchange>();
		// ͨ��parent_id����ȡ����֯
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBCursor childCursor = coll.find(new BasicDBObject().append(
				Organization.F_PARENT_ID, parentOrgExchange.getPmId()));
		// ѭ�����쵱ǰ��֯������֯
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
	 * ��ʼ��PMϵͳ����֯
	 * 
	 * @param id
	 *            : ��֯���
	 */
	private void initBySql(String id) throws Exception {
		try {
			SQLResult result;
			// �ж��Ƿ񶥼���֯
			// Ϊnull��ʾΪ������֯������ͨ��ldunitid=��1������ȡHR��֯
			// ��Ϊnull��ʾΪ�Ƕ�����֯����ʱͨ��unitid����ȡHR��֯
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
			// ����HRϵͳ����֯
			if (!result.isEmpty()) {
				List<SQLRow> dataSet = result.getData();
				SQLRow row = dataSet.get(0);
				orgId = "" + row.getValue("unitid");
				desc = "" + row.getValue("unitname");
				children.addAll(initBySqlChildren(this));
			}
		} catch (Exception e) {
			throw new Exception("�޷�����HR���ݿ�");
		}
	}

	/**
	 * ��ʼ��HRϵͳ������֯
	 * 
	 * @param parentOrgExchange
	 *            : {@link OrgExchange},����֯
	 * @return {@link HashSet},Ϊ����֯����
	 */
	private Set<OrgExchange> initBySqlChildren(OrgExchange parentOrgExchange) {
		Set<OrgExchange> childrenSet = new HashSet<OrgExchange>();
		SQLResult result;
		SQLRow row;
		try {
			// ͨ��ldunitid����ȡ����֯
			result = SQLUtil.SQL_QUERY("hr",
					"select * from pm_unit where ldunitid = '"
							+ parentOrgExchange.orgId + "'");
			// result =
			// SQLUtil.SQL_QUERY("hr","select * from tb_nczz.pm_unit where ldunitid = '"+
			// parentOrgExchange.orgId + "'");
			if (!result.isEmpty()) {
				// ѭ�����쵱ǰ��֯������֯
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
	 * @return ��ǰ��֯���
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @return ��ǰ��֯ȫ��
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return ��ǰ��֯������֯��{@link HashSet}
	 */
	public Set<OrgExchange> getChildren() {
		return children;
	}

	/**
	 * @return ��ǰ��֯��PMϵͳ��parent_id��{@link ObjectId}
	 */
	public ObjectId getPmParentId() {
		return pmParentId;
	}

	/**
	 * @return ��ǰ��֯�ĸ���֯��{@link OrgExchange}
	 */
	public OrgExchange getParent() {
		return parent;
	}

	/**
	 * @return ��ǰ��֯��PMϵͳ_ID��{@link ObjectId}
	 */
	public ObjectId getPmId() {
		return pmId;
	}

	/**
	 * ���õ�ǰ��֯��PMϵͳ_ID
	 * 
	 * @param pmId
	 *            : {@link ObjectId},PMϵͳ_ID
	 */
	public void setPmId(ObjectId pmId) {
		this.pmId = pmId;
	}

	public void setCheckHR(boolean checkHR) {
		this.checkHR = checkHR;
	}

	/**
	 * @return : {@link ObjectId}���ͣ���ǰ��֯��PMϵͳparent_id����ʹ��ʱ��Ҫ��������{@link #parent}
	 *         ��{@link #orgId}
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
	 * ��ȡ������֯������֯�Ĳ��첿��
	 * 
	 * @param otherOrg
	 *            : {@link OrgExchange},���Ƚϵ���֯
	 * @return : {@link HashSet},����֯�Ĳ��첿��
	 */
	public Set<OrgExchange> getDifferentChildren(OrgExchange otherOrg) {
		Set<OrgExchange> result = new HashSet<OrgExchange>();
		result.addAll(children);
		result.removeAll(otherOrg.children);
		return result;
	}

	/**
	 * ��ȡ������֯������֯����ͬ����
	 * 
	 * @param otherOrg
	 *            : {@link OrgExchange},���Ƚϵ���֯
	 * @return : {@link HashSet},����֯����ͬ����
	 */
	public Set<OrgExchange> getSameChildren(OrgExchange otherOrg) {
		Set<OrgExchange> result = new HashSet<OrgExchange>();
		result.addAll(children);
		result.removeAll(getDifferentChildren(otherOrg));
		return result;
	}

	/**
	 * �Ƚ�������֯��ȫ���Ƿ���ͬ
	 * 
	 * @param otherOrg
	 *            : {@link OrgExchange},���Ƚϵ���֯
	 * @return : ��ͬʱΪtrue��
	 */
	public boolean getDifferentName(OrgExchange otherOrg) {
		return desc.equals(otherOrg.desc);
	}

	/**
	 * ����ǰ��֯���뵽PMϵͳ��
	 */
	public void doAddAllHR() {
		// ��ȡ��ǰ��֯��parentId
		ObjectId parentId = this.getParentId();
		doAddAllHR(parentId);
	}

	/**
	 * ����ǰ��֯���뵽PMϵͳ��
	 * 
	 * @param parentId
	 *            : PMϵͳ��parentId
	 */
	public void doAddAllHR(ObjectId parentId) {
		if (!checkHR) {
			return;
		}
		ObjectId _id = new ObjectId();
		// ���뵱ǰ��֯
		doAddHR(this, _id, parentId);
		// ѭ���������ñ���������������֯
		for (OrgExchange orgExchangeChildren : children) {
			if (orgExchangeChildren.checkHR) {
				orgExchangeChildren.doAddAllHR(_id);
			}
		}
	}

	/**
	 * ����֯���뵽PMϵͳ��
	 * 
	 * @param otherOrg
	 *            : ��Ҫ�������֯
	 * @param _id
	 *            : �����PM��_id
	 * @param parentId
	 *            : PMϵͳ��parentId
	 */
	public void doAddHR(OrgExchange otherOrg, ObjectId _id, ObjectId parentId) {
		DBCollection roleCollection = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ORGANIZATION);

		// ���ò������֯��Ϣ
		BasicDBObject data = new BasicDBObject();
		data.put(Organization.F_ORGANIZATION_NUMBER, otherOrg.orgId);
		data.put(Organization.F_FULLDESC, otherOrg.desc);
		// ���û�и���֯����Ĭ�Ͻ�ȫ����Ϊ��ƣ�������ڣ����ƽ�ȥ���͸�����ͬ�Ĳ���
		// ͬʱ������֯�ı༭�������û�и���֯����ʹ�ö�����֯�༭����������ڣ���ʹ�ò��ź��Ŷӱ༭��
		if (otherOrg.parent == null) {
			data.put(Organization.F_DESC, otherOrg.desc);
			data.put(PrimaryObject.F__EDITOR, Organization.EDITOR_TEAM);
		} else {
			data.put(Organization.F_DESC,
					otherOrg.desc.replaceFirst(otherOrg.parent.desc, ""));
			data.put(PrimaryObject.F__EDITOR, Organization.EDITOR_SUBTEAM);
		}
		// ����ϵͳ��Ϣ
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
	 * �޸�PMϵͳ�е�ǰ��֯��ȫ��
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
	 * ������Ҫ��Ϣ��ϵͳ����Ա������Ϊ��Ҫɾ������֯
	 * 
	 * @param removeSet
	 *            : {@link HashSet}����Ҫɾ������֯
	 */
	// public void sendMessage(Set<OrgExchange> removeSet) {
	// Message message;
	// // ������Ϣ����
	// String messageContent = null;
	// for (OrgExchange orgExchange : removeSet) {
	// messageContent = setMessageContent(orgExchange);
	// }
	//
	// // ��ȡϵͳ����Ա��ɫ���û���Ϣ
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
