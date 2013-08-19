package com.sg.business.model.dataset.folder;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.Container;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;

/**
 * <p>
 * 获得授予当前用户“文档管理员”权限的组织容器 包括：所属组织的文件夹，负责的项目的文件夹
 * </p>
 * <br/>
 * 继承于 {@link com.mobnut.db.model.DataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class VaultOfGuestRole extends DataSetFactory {
	
	/**
	 * 当前用户可访问的组织容器集合数
	 */
	private long count;

	/**
	 * 获取授予当前用户“文档访问者”权限的组织容器
	 * @param ds : 组织容器数据集
	 * @return 实例化的{@link com.sg.business.model.Organization}集合
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// 从当前的进程中获得登录用户的信息
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		// 在当前用户所在的组织以及下级组织中获取"是容器"的组织
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();

		// 添加授予“文档访问者”角色的组织容器
		addRoleGrantedOrganizationContainer(currentUser,
				Role.ROLE_VAULT_GUEST_ID,Container.TYPE_GUEST_GRANTED, containers);

		//获取当前用户可访问的组织容器集合数
		count = containers.size();
		return containers;
	}

	/**
	 * 获取授予当前用户的组织容器
	 * @param user ： 登录用户的信息
	 * @param roleNumber : 角色编号
	 * @param containerType ： 授权信息,信息来源于{@link com.sg.business.model.Container}
	 * @param containers ： 组织容器集合
	 */
	private void addRoleGrantedOrganizationContainer(User user,
			String roleNumber, int containerType, List<PrimaryObject> containers) {
		// 获得用户被授予的roleNumber对应的角色
		List<PrimaryObject> roles = user.getRoles(roleNumber);

		// 取出这些角色的所属组织的id
		ObjectId[] orgIds = new ObjectId[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			orgIds[i] = ((Role) roles.get(i)).getOrganization_id();
		}

		// 查询属于容器的组织
		DBCollection orgCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject condition = new BasicDBObject();
		condition.put(Organization.F__ID,
				new BasicDBObject().append("$in", orgIds));
		condition.put(Organization.F_IS_CONTAINER, Boolean.TRUE);
		
		//将查询出的组织适配到组织容器集合中
		DBCursor cur = orgCol.find(condition);
		while (cur.hasNext()) {
			Organization org = ModelService.createModelObject(cur.next(),
					Organization.class);
			Container container = Container.adapter(org, containerType);
			if(!containers.contains(container)){
				containers.add(container);
			}
		}
	}

	/**
	 * 用于翻页查询时预测全部的页数
	 */
	@Override
	public long getTotalCount() {
		return count;
	}

}
