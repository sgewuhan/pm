package com.sg.business.visualization.labelprovide;

import java.util.Calendar;
import java.util.Date;

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
import com.sg.business.resource.BusinessResource;

public class OrganizationProjectCount extends ColumnLabelProvider {

	private DBCollection projectCol;

	public OrganizationProjectCount() {
		super();
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	@Override
	public String getText(Object element) {
		PrimaryObject dbo = ((PrimaryObject) element);
		if (dbo instanceof Organization) {
			Organization organization = (Organization) dbo;
			long wipCnt = getWipCount(organization);
			long cnt = getCountOfYear(organization);
			if (cnt == 0 && wipCnt == 0) {
				return "";
			}
			StringBuffer sb = new StringBuffer();
			sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:9pt;font-weight:bold;color:#99cc00'>");
			sb.append(wipCnt);
			sb.append("</span>");
			sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:9pt;font-weight:bold'>");
			sb.append("/" + cnt);
			sb.append("</span>");
			
			sb.append("<a href=\""
					+ organization.get_id().toString()
					+ "\" target=\"_rwt\">");
			sb.append("<img src='");
				sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_24,
						BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
			sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='24' height='24' />");
			sb.append("</a>");

			
			return sb.toString();
		}
		return "";
	}

	private long getCountOfYear(Organization organization) {
		long cnt = projectCol.count(getQueryCondtion(organization));
		return cnt;
	}

	private long getWipCount(Organization organization) {

		long wipCnt = projectCol.count(new BasicDBObject().append(
				Project.F_LAUNCH_ORGANIZATION, organization.get_id()).append(
				ILifecycle.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE));
		return wipCnt;

	}

	private DBObject getQueryCondtion(Organization organization) {
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
		dbo.put(Project.F_LAUNCH_ORGANIZATION, organization.get_id());
		dbo.put(ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE }));
		dbo.put("$or",
				new BasicDBObject[] {
						new BasicDBObject().append(Project.F_PLAN_START,
								new BasicDBObject().append("$gte", start))
								.append(Project.F_PLAN_START,
										new BasicDBObject()
												.append("&lte", stop)),
						new BasicDBObject().append(Project.F_ACTUAL_START,
								new BasicDBObject().append("$gte", start))
								.append(Project.F_ACTUAL_START,
										new BasicDBObject()
												.append("$lte", stop)),
						new BasicDBObject().append(Project.F_PLAN_FINISH,
								new BasicDBObject().append("$gte", start))
								.append(Project.F_PLAN_FINISH,
										new BasicDBObject()
												.append("$lte", stop)),
						new BasicDBObject().append(Project.F_ACTUAL_FINISH,
								new BasicDBObject().append("$gte", start))
								.append(Project.F_ACTUAL_FINISH,
										new BasicDBObject()
												.append("$lte", stop)),
						new BasicDBObject().append(Project.F_ACTUAL_START,
								new BasicDBObject().append("$lte", start))
								.append(Project.F_ACTUAL_START,
										new BasicDBObject()
												.append("$gte", stop)) });

		return dbo;
	}

}
