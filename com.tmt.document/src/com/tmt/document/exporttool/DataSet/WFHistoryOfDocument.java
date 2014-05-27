package com.tmt.document.exporttool.DataSet;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.BasicBSONList;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.Document;
import com.sg.business.model.DummyModel;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WFHistoryOfDocument extends MasterDetailDataSetFactory {

	public WFHistoryOfDocument() {
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}
	
	@Override
	public DataSet getDataSet() {
		if(master instanceof Document){
			Document document = (Document) master;
			List<PrimaryObject> list = new ArrayList<PrimaryObject>();
			BasicBSONList wfHistory = document.getWorkflowHistory();
			for (int i = 0; i < wfHistory.size(); i++) {
				DBObject dbo = (DBObject) wfHistory.get(i);
				DummyModel dummyModel = ModelService.createModelObject(dbo, DummyModel.class);
				list.add(dummyModel);
			}
			return new DataSet(list);
		}
		return super.getDataSet();
	}

}
