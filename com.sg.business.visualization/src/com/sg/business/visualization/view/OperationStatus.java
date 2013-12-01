package com.sg.business.visualization.view;

import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DataSet;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.dataset.organization.OrgOfOwnerManager;
import com.sg.business.visualization.chart.Dash;
import com.sg.widgets.part.StandaloneViewPart;

public class OperationStatus extends StandaloneViewPart {

	@Override
	protected void createContent(Composite parent) {
		OrgOfOwnerManager oom = new OrgOfOwnerManager();
		DataSet ds = oom.getDataSet();
		if(ds!=null && !ds.isEmpty()){
			Organization org = (Organization) ds.getDataItems().get(0);
			Dash dash = new Dash(parent);
			ProjectProvider pp = org.getAdapter(ProjectProvider.class);
			dash.setProjectProvider(pp);
		}
	}


}
