package com.sg.business.visualization.labelprovide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProductTypeProvider;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.CurrentAccountContext;

public class ProductTypeLabelProvider extends ColumnLabelProvider {
	private DBCollection projectCol;
	private User user;

	public ProductTypeLabelProvider() {
		super();
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public String getText(Object element) {
		PrimaryObject dbo = ((PrimaryObject) element);
		if (dbo instanceof ProductTypeProvider) {
			ProductTypeProvider producttTypeProvider = (ProductTypeProvider) dbo;
			long cnt = getCountOfYear(producttTypeProvider);
			long wipCnt = getWipCount(producttTypeProvider);
			StringBuffer sb = new StringBuffer();
			
			sb.append("<a href=\""
					+ producttTypeProvider.getDesc()+","+producttTypeProvider.getUserId()
					+ "\" target=\"_rwt\">");
			sb.append("<img src='");
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_48,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
			sb.append("' style='border-style:none;position:absolute; right:20; bottom:6; display:block;' width='28' height='28' />");
			sb.append("</a>");
			
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;display:block; width=1000px;'>");
			
			sb.append("<b>");
			sb.append(producttTypeProvider.getDesc());
			if (cnt != 0 || wipCnt != 0) {
				sb.append("<span style='font-weight:bold'>");
				sb.append("<span style='color:#99cc00'>");
				sb.append(" ");
				sb.append(wipCnt);
				sb.append("</span>");
				sb.append(" ");
				sb.append("/" + cnt);
				sb.append("</span>");
			}
			sb.append("</b>");
			sb.append("</span>");
			return sb.toString();
		}
		return "";
	}

	private long getCountOfYear(
			ProductTypeProvider productTypeProvider) {
		long count = projectCol
				.count(getQueryCondtion(productTypeProvider));
		return count;
	}

	private long getWipCount(
			ProductTypeProvider productTypeProvider) {

		long count = projectCol.count(new BasicDBObject()
				.append(Project.F_PRODUCT_TYPE_OPTION,
						productTypeProvider.getDesc())
				.append(ILifecycle.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE)
				.append(Project.F_LAUNCH_ORGANIZATION,
						new BasicDBObject().append("$in", getUerOrgId())));
		return count;

	}

	private DBObject getQueryCondtion(
			ProductTypeProvider productTypeProvider) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date start = calendar.getTime();
		calendar.add(Calendar.YEAR, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		Date stop = calendar.getTime();

		DBObject dbo = new BasicDBObject();
		dbo.put(Project.F_PRODUCT_TYPE_OPTION,
				productTypeProvider.getDesc());
		dbo.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", getUerOrgId()));
		dbo.put(ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE }));
		dbo.put("$or",
				new BasicDBObject[] {

						new BasicDBObject().append(Project.F_ACTUAL_START,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(Project.F_PLAN_FINISH,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(Project.F_ACTUAL_FINISH,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(
								"$and",
								new BasicDBObject[] {
										new BasicDBObject().append(
												Project.F_ACTUAL_START,
												new BasicDBObject().append(
														"$lte", start)),
										new BasicDBObject().append(
												Project.F_ACTUAL_FINISH,
												new BasicDBObject().append(
														"$gte", stop)) }) });
		return dbo;
	}

	protected List<ObjectId> getUerOrgId() {
		List<ObjectId> list = new ArrayList<ObjectId>();
		List<PrimaryObject> userOrg = getUserOrg(
				new ArrayList<PrimaryObject>(), getInput());
		for (PrimaryObject po : userOrg) {
			list.add(po.get_id());
		}
		return list;

	}

	protected List<PrimaryObject> getUserOrg(List<PrimaryObject> list,
			List<PrimaryObject> childrenList) {
		list.addAll(childrenList);
		for (PrimaryObject po : childrenList) {
			List<PrimaryObject> childrenOrg = ((Organization) po)
					.getChildrenOrganization();
			getUserOrg(list, childrenOrg);
		}
		return list;
	}

	protected List<PrimaryObject> getInput() {
		// 获取当前用户担任管理者角色的部门
		List<PrimaryObject> orglist = user
				.getRoleGrantedInAllOrganization(Role.ROLE_DEPT_MANAGER_ID);
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();

		for (int i = 0; i < orglist.size(); i++) {
			Organization org = (Organization) orglist.get(i);
			boolean hasParent = false;
			for (int j = 0; j < input.size(); j++) {
				Organization inputOrg = (Organization) input.get(j);
				if (inputOrg.isSuperOf(org)) {
					hasParent = true;
					break;
				}
				if (org.isSuperOf(inputOrg)) {
					input.remove(j);
					break;
				}
			}
			if (!hasParent) {
				input.add(org);
			}
		}

		return input;
	}

}
