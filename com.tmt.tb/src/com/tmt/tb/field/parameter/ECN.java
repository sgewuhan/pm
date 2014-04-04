package com.tmt.tb.field.parameter;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;

/**
 * 2014/04/04
 * 
 * @author yangjun
 * 
 */
@Deprecated
public class ECN implements IProcessParameterDelegator {

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		List<DBObject> ecn = new ArrayList<DBObject>();
		if (value instanceof List<?>) {
			List<?> list = (List<?>) value;
			for (Object obj : list) {
				if (obj instanceof PrimaryObject) {
					PrimaryObject po = (PrimaryObject) obj;
					ecn.add(po.get_data());
				}
			}
			return ecn;
		}
		return null;
	}

}
