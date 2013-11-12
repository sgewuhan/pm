package com.tmt.jszx.dataset;

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

public class TaskFormWorker extends MasterDetailDataSetFactory {

	public TaskFormWorker() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
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
				try {
					Object dept = taskForm.getProcessInstanceVarible("dept",getContext());
					ObjectId orgid=new ObjectId((String)dept);
					Organization org = ModelService.createModelObject(
							Organization.class, orgid);
				
					List<PrimaryObject> orgList = new ArrayList<PrimaryObject>();
					orgList.add(org);
					return new DataSet(orgList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}

/*	private List<PrimaryObject> searchUser(List<PrimaryObject> list,
			Organization org) {
		list.addAll(org.getUser());
		List<PrimaryObject> childrenOrgs = org.getChildrenOrganization();
		if (childrenOrgs != null && childrenOrgs.size() > 0) {
			for (int i = 0; i < childrenOrgs.size(); i++) {
				Organization childrenOrg = (Organization) childrenOrgs.get(i);
				searchUser(list, childrenOrg);
			}
		}
		return list;
	}*/
}