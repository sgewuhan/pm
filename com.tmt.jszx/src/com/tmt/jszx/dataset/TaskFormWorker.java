package com.tmt.jszx.dataset;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class TaskFormWorker extends MasterDetailDataSetFactory {

		public TaskFormWorker() {
			super(IModelConstants.DB, IModelConstants.C_WORK);
		}

		@Override
		protected String getDetailCollectionKey() {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public DataSet getDataSet() {
			if (master != null) {
				if (master instanceof TaskForm) {
					TaskForm taskForm = (TaskForm) master;
					Work work = taskForm.getWork();
					List<Map<String, Object>> historys = (List<Map<String, Object>>) work
							.getValue(IWorkCloneFields.F_WF_EXECUTE
									+ IProcessControl.POSTFIX_HISTORY);
					
					for (Map<String, Object> history : historys) {
						String taskname = (String) history.get(IProcessControl.F_WF_TASK_NAME);
						if("申请技术支持".equals(taskname)){
							Object dept = history.get("form_dept");
							if(dept instanceof ObjectId){
								Organization org = ModelService.createModelObject(Organization.class, (ObjectId)dept);
								List<PrimaryObject> allUser = org.getUser();
								return new DataSet(allUser);
						}
					}
					
				}
			}
		}
		return super.getDataSet();
		}
}