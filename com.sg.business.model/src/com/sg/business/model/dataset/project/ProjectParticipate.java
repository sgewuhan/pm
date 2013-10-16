package com.sg.business.model.dataset.project;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectParticipate extends MasterDetailDataSetFactory {

	public ProjectParticipate() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@Override
	public DataSet getDataSet() {
//		if (master != null) {
//			IProjectRelative work = (IProjectRelative) master;
//			Project project = work.getProject();
//			List<?> participates = project.getParticipatesIdList();
//			List<PrimaryObject> result = new ArrayList<PrimaryObject>();
//			if (participates != null) {
//				for (int i = 0; i < participates.size(); i++) {
//					String userid = (String) participates.get(i);
//					User user = UserToolkit.getUserById(userid);
//					result.add(user);
//				}
//			}
//			return new DataSet(result);
//		} else {
//			return super.getDataSet();
//		}
		
		
		if (master != null) {
			Project project=null;
			if(master instanceof TaskForm){
				TaskForm taskForm=(TaskForm)master;
				project=taskForm.getWork().getProject();
			}else{
				IProjectRelative work = (IProjectRelative) master;
	  			project = work.getProject();
			}
			List<?> participates=null;
			if(project!=null){
				 participates = project.getParticipatesIdList();
			}
			List<PrimaryObject> result = new ArrayList<PrimaryObject>();
			if (participates != null) {
				for (int i = 0; i < participates.size(); i++) {
					String userid = (String) participates.get(i);
					User user = UserToolkit.getUserById(userid);
					result.add(user);
				}
			}
			return new DataSet(result);
		} else {
			return super.getDataSet();
		}
	}

}
