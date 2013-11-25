package com.sg.business.visualization.labelprovide;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.UserProjectPerf;
import com.sg.business.resource.BusinessResource;

public class MyProjectSetLabelProvider extends ColumnLabelProvider {
	
	private DBCollection col;
	public MyProjectSetLabelProvider() {
		super();
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USERPROJECTPERF);
	}

	@Override
	public String getText(Object element) {
		PrimaryObject dbo = ((PrimaryObject) element);
		if (dbo instanceof UserProjectPerf) {
			UserProjectPerf pperf = (UserProjectPerf) dbo;
			long cnt = getCount(pperf);
			long wipCnt = getWipCount(pperf);
			StringBuffer sb = new StringBuffer();
			
			sb.append("<a href=\""
					+ pperf.get_id().toString()
					+ "\" target=\"_rwt\">");
			sb.append("<img src='");
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_48,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
			sb.append("' style='border-style:none;position:absolute; right:20; bottom:6; display:block;' width='28' height='28' />");
			sb.append("</a>");
			
			sb.append("<b>");
			sb.append(pperf.getDesc());
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
			return sb.toString();
		}
		return "";
	}

	private long getWipCount(UserProjectPerf pperf) {
		Object desc = pperf.getValue(UserProjectPerf.F_DESC);
		Object userid = pperf.getValue(UserProjectPerf.F_USERID);
		DBCursor cur = col.find(new BasicDBObject().append(UserProjectPerf.F_DESC, desc).append(UserProjectPerf.F_USERID, userid));
		int count = cur.size();
		while(cur.hasNext()){
			DBObject next = cur.next();
			ObjectId projectid = (ObjectId) next.get(UserProjectPerf.F_PROJECT_ID);
			Project project = ModelService.createModelObject(Project.class, projectid);
			if(!ILifecycle.STATUS_WIP_VALUE.equals(project.getLifecycleStatus())){
				count--;
			}
		}
		return count;
	}

	private long getCount(UserProjectPerf pperf) {
		Object desc = pperf.getValue(UserProjectPerf.F_DESC);
		Object userid = pperf.getValue(UserProjectPerf.F_USERID);
		long count = col.count(new BasicDBObject().append(UserProjectPerf.F_DESC, desc).append(UserProjectPerf.F_USERID, userid));
		return count;
	}

}
