package com.tmt.gs.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class PrintDocOfGS extends MasterDetailDataSetFactory{
	
	public IContext context;
	private User user;

	public PrintDocOfGS(String dbName, String collectionName) {
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
		context=new CurrentAccountContext();
		String userId = context.getAccountInfo().getUserId();
		user=UserToolkit.getUserById(userId);
	}

	public DataSet getDataSet(){
		if(master!=null){
			if(master instanceof TaskForm){
				List<PrimaryObject> doclist=new ArrayList<PrimaryObject>();
				List<PrimaryObject> chargeProject = user.getChargeProject(null);
				for (int i = 0; i < chargeProject.size(); i++) {
					PrimaryObject po = chargeProject.get(i);
					if(po instanceof Project){
						Project project=(Project) po;
						Work work = project.getWBSRoot();
						List<PrimaryObject> documents = work.getDeliverableDocuments();
						for (int j = 0; j < documents.size(); j++) {
							PrimaryObject document = documents.get(j);
							if(document instanceof Document){
								Document doc=(Document) document;
								String lifecycle = doc.getLifecycle();
								if("STATUS_RELEASED_ID".equals(lifecycle)){
									doclist.add(doc);
									return new DataSet(doclist);
								}
							}
						}
						
					}
				}
			}
		}
		return super.getDataSet();
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

}
