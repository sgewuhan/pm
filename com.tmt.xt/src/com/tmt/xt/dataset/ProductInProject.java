package com.tmt.xt.dataset;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProductInProject extends MasterDetailDataSetFactory {

	public ProductInProject() {
		super(IModelConstants.DB, IModelConstants.C_PRODUCT);
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
				ObjectId _id;
				try {
					_id = (ObjectId) taskForm.getValue("project_id");
					Project project = ModelService.createModelObject(
							Project.class, _id);
					List<PrimaryObject> result = project.getSubconcessionsProduct();
					if (result != null) {
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
