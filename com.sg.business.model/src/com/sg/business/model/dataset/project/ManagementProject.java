package com.sg.business.model.dataset.project;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * 项目管理集合
 * </p>
 * 继承于{@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory},
 * 用于显示当前用户可以进行管理的项目<br/>
 * 包括以下功能：
 * <li>
 * <li>
 * <li>
 * 
 * @author yangjun
 *
 */
public class ManagementProject extends SingleDBCollectionDataSetFactory {

	private User user;

	/**
	 * 项目管理集合构造函数，用于设置项目管理集合的存放数据库及数据存储表
	 */
	public ManagementProject() {
		//设置项目管理集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	/**
	 * 获取当前账号可管理的具有项目管理职能的组织
	 * 
	 * @return 返回当前用户可管理的具有项目管理职能的组织，
	 * 为{@link com.mongodb.DBObject}类型的数据
	 */
	@Override
	public DBObject getQueryCondition() {
		// 获得当前帐号可管理的项目职能组织
		try {
			//获取当前用户具有项目管理员角色的组织
			List<PrimaryObject> orglist = user
					.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_PROJECT_ADMIN_ID);
			ObjectId[] ids = new ObjectId[orglist.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = orglist.get(i).get_id();
			}
			//获取当前用户具有业务管理员角色的组织项下的项目
			BasicDBObject condition = new BasicDBObject();
			condition.put(Project.F_FUNCTION_ORGANIZATION,
					new BasicDBObject().append("$in", ids)); //$NON-NLS-1$
			return condition;
		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null); //$NON-NLS-1$
		}
	}

}
