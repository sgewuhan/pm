package com.sg.business.visualization.option;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserProjectPerf;
import com.sg.widgets.commons.options.IFieldOptionProvider;
import com.sg.widgets.registry.config.Option;

public class ProjectSetOption implements IFieldOptionProvider {

	public ProjectSetOption() {
	}

	@Override
	public Option getOption(Object input, Object data, String key, Object value) {
		List<Option> options = new ArrayList<Option>();
		
		String userid = (String) ((PrimaryObject)data).getValue("userid"); //$NON-NLS-1$
		DBCollection col = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_USERPROJECTPERF);
		DBCursor cur = col.find(new BasicDBObject().append(UserProjectPerf.F_USERID, userid));
		while(cur.hasNext()){
			DBObject next = cur.next();
			String desc = (String) next.get(UserProjectPerf.F_DESC);
			Option option=new Option(desc,desc,desc,null);
			if(!options.contains(option)){
				options.add(option);
			}
		}
		return new Option("","","",options.toArray(new Option[0])); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}


}
