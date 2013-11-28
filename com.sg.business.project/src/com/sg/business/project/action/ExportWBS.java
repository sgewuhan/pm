package com.sg.business.project.action;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.NavigatorAction;
import com.sg.widgets.part.NavigatorControl;

public class ExportWBS extends NavigatorAction {

	public ExportWBS() {
		setText("µ¼³ö");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_EXPORT_24));
	}

	@Override
	protected void execute() throws Exception {
		List<PrimaryObject> list=new ArrayList<PrimaryObject>();
		NavigatorControl control = getNavigator();
		if (control.canExport()) {
			Project project = (Project) getInput().getData();
			DBCollection collection = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_WORK);
			DBCursor cur = collection.find(new BasicDBObject().append(Work.F_PROJECT_ID,
					new BasicDBObject().append("$in", project.get_id())));
			while(cur.hasNext()){
				DBObject next = cur.next();
				Work work = ModelService.createModelObject(next,Work.class);
				list.add(work);
			}
			
//			setInput();
			control.doExport();

		}

	}

}
