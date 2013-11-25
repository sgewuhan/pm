package com.sg.business.model.dataset.visualization;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IDataSetFactory;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserProjectPerf;
import com.sg.widgets.part.CurrentAccountContext;

public class MyProjectSet extends SingleDBCollectionDataSetFactory implements
		IDataSetFactory {

	public MyProjectSet() {
		super(IModelConstants.DB, IModelConstants.C_USERPROJECTPERF);
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		setQueryCondition(new BasicDBObject().append(UserProjectPerf.F_USERID,
				userId));
	}

	@Override
	public DataSet getDataSet() {
		boolean isExist;
		List<PrimaryObject> datalist = new ArrayList<PrimaryObject>();
		DataSet dataSet = super.getDataSet();
		List<PrimaryObject> dataItems = dataSet.getDataItems();
		for (PrimaryObject po : dataItems) {
			isExist=false;
			String desc = po.getDesc();
			for(PrimaryObject po2 : datalist){
				String desc2 = po2.getDesc();
				if(desc.equals(desc2)){
					isExist=true;
					break;
				}
			}
			if(!isExist){
				datalist.add(po);
			}
		}

		return new DataSet(datalist);
	}

}
