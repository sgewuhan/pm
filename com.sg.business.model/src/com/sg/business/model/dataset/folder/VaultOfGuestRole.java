package com.sg.business.model.dataset.folder;

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
import com.sg.business.model.Container;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * 授权文档库
 * </p>
 * 继承于 {@link com.mobnut.db.model.DataSetFactory}，获得当前用户授权查看（具有文档访问者权限）的组织的文档库
 * 包括：授权查看的组织的文件夹
 * <br/>
 * 实现以下几种功能：
 * <li>获取授权查看的组织的文件夹数
 * <li>获取授权查看的组织的文件夹的List<{@link com.mobnut.db.model.PrimaryObject}>集合
 * 
 * @author yangjun
 * 
 */
public class VaultOfGuestRole extends DataSetFactory {
	
	/**
	 * 当前用户授权查看的组织的文件夹数
	 */
	private long count;

	/**
	 * 获取当前用户授权查看的组织的文件夹的List<{@link com.mobnut.db.model.PrimaryObject}>集合
	 * @param ds : 组织容器数据集
	 * @return 实例化的{@link com.mobnut.db.model.PrimaryObject}集合
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// 从当前的进程中获得登录用户的信息
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		User currentUser = UserToolkit.getUserById(userId);
		
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
	 * 添加授权查看的组织的文件夹到List<{@link com.mobnut.db.model.PrimaryObject}>集合
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
			//虚拟化的组织容器
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
