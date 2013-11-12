package com.tmt.tb.dataset;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class UserSelectorOfTaskforms extends MasterDetailDataSetFactory {

	public UserSelectorOfTaskforms() {
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				TaskForm taskForm = (TaskForm) master;
				List<PrimaryObject> result = new ArrayList<PrimaryObject>();
				try {
					String obj = (String) taskForm.getProcessInstanceVarible(
							"dept", getContext());
					if (obj != null) {
						ObjectId _id = new ObjectId(obj);
						Organization org = ModelService.createModelObject(
								Organization.class, _id);
						result.add(org);
						return new DataSet(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}

}
