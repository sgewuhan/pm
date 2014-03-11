package com.sg.sales.dataset;


import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;


public class PMChangeProcessDataSet extends  SingleDBCollectionDataSetFactory  {

	
	public PMChangeProcessDataSet() {
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	/*@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				TaskForm taskForm = (TaskForm) master;
				Object obj;
				try {
					obj = taskForm.getProcessInstanceVarible("reviewer_list", //$NON-NLS-1$
							context);
					List<PrimaryObject> result = new ArrayList<PrimaryObject>();
					if (obj instanceof ArrayList) {
						List<String> reviewer_list = (ArrayList<String>) obj;
						if (reviewer_list != null) {
							for (int i = 0; i < reviewer_list.size(); i++) {
								String userid = (String) reviewer_list.get(i);
								User user = UserToolkit.getUserById(userid);
								result.add(user);
							}
						}
					}
					return new DataSet(result);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}*/

}
