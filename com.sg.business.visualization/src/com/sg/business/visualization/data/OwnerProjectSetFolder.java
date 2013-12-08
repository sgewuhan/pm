package com.sg.business.visualization.data;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserProjectPerf;
import com.sg.business.visualization.VisualizationActivator;
import com.sg.business.visualization.data.ProjectSetFolder;

public class OwnerProjectSetFolder extends ProjectSetFolder {

	@Override
	public Object[] getChildren() {
		DBCollection collection = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USERPROJECTPERF);
		DBCursor cur = collection.find(new BasicDBObject().append(
				UserProjectPerf.F_USERID, user.getUserid()));
		Object[] result = new Object[cur.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = ModelService.createModelObject(cur.next(),
					UserProjectPerf.class);
		}
		return result;
	}

	@Override
	public boolean hasChildren() {
		DBCollection collection = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USERPROJECTPERF);
		return collection.find(
				new BasicDBObject().append(UserProjectPerf.F_USERID,
						user.getUserid())).count() > 0;
	}

	@Override
	public String getLabel() {
		return "自定义项目组合";
	}

	@Override
	public String getImageURL() {
		return FileUtil.getImageURL("owner_folder_32.png", VisualizationActivator.PLUGIN_ID);
	}

	@Override
	public String getDescription() {
		return "按您定义的项目组合";
	}
}
