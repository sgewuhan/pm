package com.tmt.tb.dataset;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.TaskForm;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectChangeDocument extends MasterDetailDataSetFactory {

	private IContext context;


	public ProjectChangeDocument() {
		super(IModelConstants.DB, IModelConstants.C_FOLDER);
		//setQueryCondition(new BasicDBObject().append(Folder.F_PARENT_ID, null));
		context = new CurrentAccountContext();
	}

	@Override
	protected String getDetailCollectionKey() {
		return Folder.F_PROJECT_ID;
	}

	
	@Override
	protected Object getMasterValue() {
		if (master != null) {
			if (master instanceof TaskForm) {
				TaskForm taskForm = (TaskForm) master;
				String obj;
				try {
					obj = (String) taskForm.getProcessInstanceVarible(
							"project", context);
					if (obj != null) {
						ObjectId _id = new ObjectId(obj);
						return _id;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return null;
	}
}
