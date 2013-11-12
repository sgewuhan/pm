package com.sg.business.model.dataset.bulletinboard;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.dataset.ContextSingleDataSetFactory;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;

/**
 * <p>
 * 公告板
 * </p>
 * 继承于 {@link SingleDBCollectionDataSetFactory} 用于获取公告板信息<br/>
 * 实现以下几种功能： <li>获取公告板数据信息 <li>设置查询条件 <li>设置排序
 * 
 * @author gdiyang
 * 
 */
public class BulletinBoardDataSet extends ContextSingleDataSetFactory {

	/**
	 * 公告板构造函数
	 */
	public BulletinBoardDataSet() {
		// 设置公告板数据合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_BULLETINBOARD);
	}

	/**
	 * 设置查询条件
	 */
	@Override
	public DBObject getQueryCondition() {
		try {
			// 获取当前用户所在的组织
			List<ObjectId> orgIds = new ArrayList<ObjectId>();
			String userid = getContext().getAccountInfo().getConsignerId();
			User user = UserToolkit.getUserById(userid);
			Organization org = user.getOrganization();
			/**
			 * zhonghua 用户的组织可能为空
			 */
			if (org != null) {
				// 获取当前用户所在组织的下级组织
				searchDown(org, orgIds);
				// 获取当前用户所在组织的上级组织
				searchUp(org, orgIds);
			}

			// 设置查询条件
			BasicDBObject condition = new BasicDBObject();
			condition.put(BulletinBoard.F_ORGANIZATION_ID,
					new BasicDBObject().append("$in", orgIds));
			condition.put(BulletinBoard.F_PARENT_BULLETIN, null);
			condition.put(BulletinBoard.F_PROJECT_ID, null);
			return condition;
		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null);
		}
	}

	/**
	 * 获取组织的下级组织
	 * 
	 * @param org
	 *            : 当前组织
	 * @param list
	 *            : 需查询的组织列表
	 */
	private void searchDown(Organization org, List<ObjectId> list) {
		// 获取当前组织的下级组织
		List<PrimaryObject> children = org.getChildrenOrganization();
		// 循环迭代添加下级组织到需查询的组织列表中
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			if (child.isFunctionDepartment()) {
				list.add(child.get_id());
			}
			searchDown(child, list);
		}
	}

	/**
	 * 获取组织的上级组织
	 * 
	 * @param org
	 *            : 当前组织
	 * @param list
	 *            : 需查询的组织列表
	 */
	private void searchUp(Organization org, List<ObjectId> list) {
		// 添加当前组织到组织列表中
		list.add(0, org.get_id());
		// 迭代添加上级组织到组织列表中
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}

	/**
	 * 设置排序
	 */
	@Override
	public DBObject getSort() {
		// 依据发布日期进行排序
		return new BasicDBObject().append(BulletinBoard.F__ID, -1);
	}
}
