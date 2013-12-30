package com.sg.business.visualization.data;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectMonthData;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectRevenue extends MasterDetailDataSetFactory {

	public ProjectRevenue() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_MONTH_DATA);
		setSort(new BasicDBObject().append(ProjectMonthData.F_YEAR, -1)
				.append(ProjectMonthData.F_MONTH, -1));
	}

	@Override
	protected Object getMasterValue() {
		Project project = ((Project) master);
		return project.get_id(); //$NON-NLS-1$
	}

	@Override
	protected String getDetailCollectionKey() {
		return ProjectMonthData.F_PROJECTID;
	}
	
//	@Override
//	public DataSet getDataSet() {
//		Project project = ((Project) master);
//		ArrayList<PrimaryObject> result = new ArrayList<PrimaryObject>();
//		if(master == null){
//			return new DataSet(result);
//		}
//		List<DBObject> data = project.getAggregationRevenue();
//		if(data == null){
//			return new DataSet(result);
//		}
//		for (int i = 0; i < data.size(); i++) {
//			DBObject dbo = data.get(i);
//			Object period = dbo.get(PrimaryObject.F__ID);
//			dbo.put(PrimaryObject.F__ID, new ObjectId());
//			dbo.put("period", period);
//			result.add(ModelService.createModelObject(dbo, DummyModel.class));
//		}
//		return new DataSet(result);
//	}

}
