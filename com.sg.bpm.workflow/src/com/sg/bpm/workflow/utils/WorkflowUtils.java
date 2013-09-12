package com.sg.bpm.workflow.utils;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DocumentModelDefinition;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class WorkflowUtils {

	/**
	 * 将工作流里面的#{}变量参数取出里面的变量
	 * 
	 * @param param
	 * @return
	 */
	public static String parseAssignmentParameterName(String param) {

		int start = param.indexOf("#{");
		int end = param.indexOf("}");
		if (start != 0 || end == -1) {
			return null;
		} else {
			return param.substring(2, end);
		}
	}

	public static PrimaryObject getHostFromJSON(String jsonContent) {
		try {
			DBObject data = (DBObject) JSON.parse(jsonContent);
			Object dbo = data.get("data");
			if (dbo instanceof DBObject) {
				ObjectId id = (ObjectId) ((DBObject) dbo).get("_id");
				String className = (String) data.get("class");
				DocumentModelDefinition modelDef = ModelService
						.getDocumentModelDefinition(className);
				PrimaryObject host = ModelService.createModelObject(
						modelDef.getModelClass(), id);
				return host;
			}
		} catch (Exception e) {
		}
		return null;
	}
	

	public static DBObject getProcessInfoFromJSON(String jsonContent) {
		try {
			DBObject data = (DBObject) JSON.parse(jsonContent);
			Object key = data.get("key");
			Object dbo = data.get("data");
			if (dbo instanceof DBObject) {
				return (DBObject) ((DBObject) dbo).get((String) key);
			}
		} catch (Exception e) {
		}
		return null;
	}
}
