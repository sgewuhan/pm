package com.sg.business.visualization.labelprovide;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.ProductTypeProvider;
import com.sg.business.model.User;
import com.sg.business.model.UserProjectPerf;
import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.dataset.ProjectSetFolder;

public class ProjectSetNavigatorLabelProvider extends ColumnLabelProvider {


	@Override
	public String getText(Object element) {
		PrimaryObject po = ((PrimaryObject) element);
		if (po instanceof Organization) {
			return getOrganizationText((Organization) po);
		} else if (po instanceof User) {
			return getUserText((User) po);
		} else if (po instanceof UserProjectPerf) {
			return getUserProjectSetText((UserProjectPerf) po);
		} else if (po instanceof ProductTypeProvider) {
			return getProductTypeText((ProductTypeProvider) po);
		} else if (po instanceof ProjectSetFolder) {
			return getFolderText((ProjectSetFolder) po);
		} else {
			return po.getLabel();
		}
	}

	private String getFolderText(ProjectSetFolder po) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-family:Î¢ÈíÑÅºÚ;font-size:9pt;display:block; width=1000px;'>");

		String imageUrl = "<img src='" + po.getImageURL()
				+ "' style='float:left;padding:2px' width='24' height='24' />";

		sb.append(imageUrl);
		sb.append("<b>");
		sb.append(po.getLabel());

		sb.append("</b>");
		sb.append("<br/>");
		sb.append("<small style='color=#999'>");
		sb.append(po.getDescription());
		sb.append("</small></span>");
		return sb.toString();
	}

	private String getProductTypeText(ProductTypeProvider producttTypeProvider) {

		StringBuffer sb = new StringBuffer();

		sb.append("<a href=\"" + "ProductTypeProvider@"
				+ producttTypeProvider.getDesc() + "@"
				+ producttTypeProvider.getUserId() + "\" target=\"_rwt\">");
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_48,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
		sb.append("' style='border-style:none;position:absolute; right:20; bottom:6; display:block;' width='28' height='28' />");
		sb.append("</a>");

		sb.append("<span style='font-family:Î¢ÈíÑÅºÚ;font-size:9pt;display:block; width=1000px;'>");
		String imageUrl = "<img src='"
				+ FileUtil.getImageURL(BusinessResource.IMAGE_PROJECT_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER)
				+ "' style='float:left;padding:2px' width='24' height='24' />";

		sb.append(imageUrl);
		sb.append(producttTypeProvider.getDesc());
		sb.append("</span>");
		return sb.toString();
	}

	private String getUserProjectSetText(UserProjectPerf po) {
		UserProjectPerf pperf = (UserProjectPerf) po;
		StringBuffer sb = new StringBuffer();

		sb.append("<a href=\"" + "UserProjectPerf@" + pperf.get_id().toString()
				+ "\" target=\"_rwt\">");
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_48,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
		sb.append("' style='border-style:none;position:absolute; right:20; bottom:6; display:block;' width='28' height='28' />");
		sb.append("</a>");

		String imageUrl = "<img src='"
				+ FileUtil.getImageURL(BusinessResource.IMAGE_PROJECT_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER)
				+ "' style='float:left;padding:2px' width='24' height='24' />";

		sb.append(imageUrl);
		sb.append(pperf.getDesc());
		return sb.toString();
	}

	private String getUserText(User po) {
		User user = (User) po;
		StringBuffer sb = new StringBuffer();
		sb.append("<a href=\"" + "User@" + user.get_id().toString()
				+ "\" target=\"_rwt\">");
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_48,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
		sb.append("' style='border-style:none;position:absolute; right:20; bottom:6; display:block;' width='28' height='28' />");
		sb.append("</a>");

		sb.append("<span style='font-family:Î¢ÈíÑÅºÚ;font-size:9pt;display:block; width=1000px;'>");

		String imageUrl = "<img src='"
				+ FileUtil.getImageURL(BusinessResource.IMAGE_USER_24,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER)
				+ "' style='float:left;padding:2px' width='24' height='24' />";

		sb.append(imageUrl);
		sb.append(user.getLabel());
		sb.append("<br/>");
		sb.append("<small style='color=#999'>");
		Organization org = user.getOrganization();
		sb.append(org == null ? "" : org.getPath(2));
		sb.append("</small></span>");

		return sb.toString();
	}

	private String getOrganizationText(Organization po) {
		Organization organization = (Organization) po;
		String label = organization.getLabel();
		String path = organization.getFullName();
		StringBuffer sb = new StringBuffer();
		sb.append("<a href=\"" + "Organization@"
				+ organization.get_id().toString() + "\" target=\"_rwt\">");
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_48,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
		sb.append("' style='border-style:none;position:absolute; right:20; bottom:6; display:block;' width='28' height='28' />");
		sb.append("</a>");

		sb.append("<span style='display:block; width=1000px;'>");

		String imageUrl = "<img src='" + organization.getImageURL()
				+ "' style='float:left;padding:2px' width='24' height='24' />";
		sb.append(imageUrl);
		sb.append("<span style='font-family:Î¢ÈíÑÅºÚ;font-size:9pt'>");
		sb.append(label);
		sb.append("<br/>");
		sb.append("<small style='color=#999'>");
		sb.append(path);
		sb.append("</small>");
		sb.append("</span>");
		sb.append("</span>");

		return sb.toString();
	}

	// private long getCountOfYear(Organization organization) {
	// long cnt = projectCol.count(getQueryCondtion(organization));
	// return cnt;
	// }
	//
	// private long getWipCount(Organization organization) {
	//
	// long wipCnt = projectCol.count(new BasicDBObject().append(
	// Project.F_LAUNCH_ORGANIZATION, organization.get_id()).append(
	// ILifecycle.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE));
	// return wipCnt;
	//
	// }

	// private void setCountOfYear(Organization organization) {
	// long count = projectCol.count(getQueryCondtion(organization));
	// cnt += count;
	// List<PrimaryObject> childrenOrganization = organization
	// .getChildrenOrganization();
	// for (PrimaryObject orgpo : childrenOrganization) {
	// setCountOfYear((Organization) orgpo);
	// }
	// }

	// private void setWipCount(Organization organization) {
	//
	// long count = projectCol.count(new BasicDBObject().append(
	// Project.F_LAUNCH_ORGANIZATION, organization.get_id()).append(
	// ILifecycle.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE));
	// wipCnt += count;
	// List<PrimaryObject> childrenOrganization = organization
	// .getChildrenOrganization();
	// for (PrimaryObject orgpo : childrenOrganization) {
	// setWipCount((Organization) orgpo);
	// }
	//
	// }

	// private DBObject getQueryCondtion(Organization organization) {
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.MONTH, 0);
	// calendar.set(Calendar.DATE, 1);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.SECOND, 0);
	// calendar.set(Calendar.MILLISECOND, 0);
	// Date start = calendar.getTime();
	// calendar.add(Calendar.YEAR, 1);
	// calendar.add(Calendar.MILLISECOND, -1);
	// Date stop = calendar.getTime();
	//
	// DBObject dbo = new BasicDBObject();
	// dbo.put(Project.F_LAUNCH_ORGANIZATION, organization.get_id());
	// dbo.put(ILifecycle.F_LIFECYCLE,
	// new BasicDBObject().append("$in", new String[] {
	// ILifecycle.STATUS_FINIHED_VALUE,
	// ILifecycle.STATUS_WIP_VALUE }));
	// dbo.put("$or",
	// new BasicDBObject[] {
	//
	// new BasicDBObject().append(Project.F_ACTUAL_START,
	// new BasicDBObject().append("$gte", start)
	// .append("$lte", stop)),
	//
	// new BasicDBObject().append(Project.F_PLAN_FINISH,
	// new BasicDBObject().append("$gte", start)
	// .append("$lte", stop)),
	//
	// new BasicDBObject().append(Project.F_ACTUAL_FINISH,
	// new BasicDBObject().append("$gte", start)
	// .append("$lte", stop)),
	//
	// new BasicDBObject().append(
	// "$and",
	// new BasicDBObject[] {
	// new BasicDBObject().append(
	// Project.F_ACTUAL_START,
	// new BasicDBObject().append(
	// "$lte", start)),
	// new BasicDBObject().append(
	// Project.F_ACTUAL_FINISH,
	// new BasicDBObject().append(
	// "$gte", stop)) }) });
	// return dbo;
	// }

}
