package com.tmt.gs.dataset;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.TaskForm;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class GSProjectChangeDocument extends MasterDetailDataSetFactory {

	private IContext context;


	public GSProjectChangeDocument() {
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
							"project", context); //$NON-NLS-1$
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
