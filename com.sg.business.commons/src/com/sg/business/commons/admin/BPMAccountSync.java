package com.sg.business.commons.admin;

import org.eclipse.swt.SWT;

import com.mobnut.admin.IFunctionExcutable;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.bpm.service.BPM;
import com.sg.bpm.service.HTService;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;

public class BPMAccountSync implements IFunctionExcutable {

	public BPMAccountSync() {
	}

	@Override
	public void run() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_USER);
		DBCursor cur = col.find(null,new BasicDBObject().append(User.F_USER_ID,1));
		while(cur.hasNext()){
			DBObject dbo = cur.next();
			String userid = (String) dbo.get(User.F_USER_ID);
			HTService ts = BPM.getHumanTaskService();
			ts.addParticipateUser(userid);
		}
		MessageUtil.showToast("用户数据同步完成", SWT.ICON_INFORMATION);
	}

}
