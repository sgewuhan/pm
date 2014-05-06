package com.tmt.gs.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class PrintDocOfGS extends SingleDBCollectionDataSetFactory {

	public IContext context;
	private User user;

	public PrintDocOfGS() {
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
		context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getUserId();
		user = UserToolkit.getUserById(userId);
		List<PrimaryObject> doclist = new ArrayList<PrimaryObject>();
		List<PrimaryObject> projectlist = user.getChargeProject(null);
		for (PrimaryObject po : projectlist) {
			Project project = (Project) po;
			List<PrimaryObject> worklist = project.getChildrenWork();
			for (PrimaryObject works : worklist) {
				Work work = (Work) works;
				List<PrimaryObject> documents = work.getDeliverableDocuments();
				for (int i = 0; i < documents.size(); i++) {
					PrimaryObject docs = documents.get(i);
					Document document = (Document) docs;
					if ("STATUS_RELEASED_ID".equals(document.getLifecycle())) {
						doclist.add(document);
					}
				}
			}
		}
	}

}
