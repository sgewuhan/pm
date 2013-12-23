package com.sg.business.model.dataset.project;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * 项目管理集合
 * </p>
 * 继承于{@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory},
 * 用于显示当前用户可以进行管理的项目<br/>
 * 包括以下功能： <li> <li> <li>
 * 
 * @author yangjun
 * 
 */
public class ManagementProjectRootWork extends SingleDBCollectionDataSetFactory {

	private User user;

	/**
	 * 项目管理集合构造函数，用于设置项目管理集合的存放数据库及数据存储表
	 */
	public ManagementProjectRootWork() {
		// 设置项目管理集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_WORK);
		// 获取当前用户信息
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {
			// 获得当前帐号可管理的项目职能组织

			// 获取当前用户具有业务管理员角色的组织
			List<PrimaryObject> orglist = user
					.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_PROJECT_ADMIN_ID);
			ObjectId[] ids = new ObjectId[orglist.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = orglist.get(i).get_id();
			}
			// 获取当前用户具有业务管理员角色的组织项下的项目
			BasicDBObject condition = new BasicDBObject();
			condition.put(Project.F_FUNCTION_ORGANIZATION,
					new BasicDBObject().append("$in", ids)); //$NON-NLS-1$

			// 该项目的状态必须是进行中
			condition.put(Project.F_LIFECYCLE, Project.STATUS_WIP_VALUE);

			DBCollection projectCol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_PROJECT);
			DBCursor cur = projectCol.find(condition, new BasicDBObject()
					.append(Project.F_WORK_ID, 1).append(Project.F_DESC, 1));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				BasicDBObject data = new BasicDBObject().append(Work.F__ID,
						dbo.get(Project.F_WORK_ID)).append(Work.F_DESC,
						dbo.get(Project.F_DESC));
				Work work = ModelService.createModelObject(data, Work.class);
				result.add(work);
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return new DataSet(result);
	}

}
