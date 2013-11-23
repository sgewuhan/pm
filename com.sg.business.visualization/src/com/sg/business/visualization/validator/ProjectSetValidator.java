package com.sg.business.visualization.validator;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserProjectPerf;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class ProjectSetValidator extends AbstractValidator {

	@Override
	protected String getValidMessage(PrimaryObject data) {
		
		Object desc = data.getValue(UserProjectPerf.F_DESC);
		Object userid = data.getValue(UserProjectPerf.F_USERID);
		Object projectid = data.getValue(UserProjectPerf.F_PROJECT_ID);
		
		DBCollection col = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_USERPROJECTPERF);
		DBObject dbo = col.findOne(new BasicDBObject().append(UserProjectPerf.F_USERID, userid).append(UserProjectPerf.F_DESC, desc).append(UserProjectPerf.F_PROJECT_ID, projectid));
		if(dbo!=null){
			return "项目集中已经存在此项目";
		}
		return null;
		
	}
	
	

}
