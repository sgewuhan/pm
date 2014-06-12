package com.sg.business.taskforms.bpmservice;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.bpm.service.task.ServiceProvider;
import com.sg.bpm.workflow.utils.WorkflowUtils;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;

public class ProjectChangeLinkProject extends ServiceProvider {

	@Override
	public Map<String, Object> run(Object parameter) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object content = getInputValue("content"); //$NON-NLS-1$
		String _id = (String) getInputValue("project"); //$NON-NLS-1$
		if (_id == null) {
			result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
			result.put("returnMessage", "无法关联到相应项目中"); //$NON-NLS-1$
		} else {
			if (content instanceof String) {
				String jsonContent = (String) content;
				try {
					ObjectId project_id = new ObjectId(_id);
					PrimaryObject host = WorkflowUtils
							.getHostFromJSON(jsonContent);
					DBCollection workCol = DBActivator.getCollection(
							IModelConstants.DB, IModelConstants.C_WORK);
					workCol.update(new BasicDBObject().append(Work.F__ID,
							host.get_id()), new BasicDBObject().append("$set",
							new BasicDBObject().append(Work.F_PROJECT_ID,
									project_id)));
				} catch (Exception e) {
					result.put("returnCode", "ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
					result.put("returnMessage", e.getMessage()); //$NON-NLS-1$
				}
			}
		}
		return result;
	}

}
