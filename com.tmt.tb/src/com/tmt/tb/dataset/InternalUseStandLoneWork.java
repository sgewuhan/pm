package com.tmt.tb.dataset;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class InternalUseStandLoneWork extends MasterDetailDataSetFactory {

	public InternalUseStandLoneWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
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
							"project", new CurrentAccountContext());
					if (obj != null) {
						ObjectId _id = new ObjectId(obj);
						Project project = ModelService.createModelObject(
								Project.class, _id);
						Organization org = project.getFunctionOrganization();
						
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
