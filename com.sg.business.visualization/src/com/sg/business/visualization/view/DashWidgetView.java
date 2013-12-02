package com.sg.business.visualization.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DataSet;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.dataset.organization.OrgOfOwnerManager;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.part.StandaloneViewPart;

public abstract class DashWidgetView extends StandaloneViewPart {

	protected ProjectProvider projectProvider;

	@Override
	final protected void createContent(Composite parent) {
		loadData(parent);
	}

	private void loadData(final Composite parent) {
		final OrgOfOwnerManager oom = new OrgOfOwnerManager();
		DataSet ds = oom.getDataSet();
		if (!ds.isEmpty()) {
			Organization org = (Organization) ds.getDataItems().get(0);
			projectProvider = org.getAdapter(ProjectProvider.class);
			projectProvider.getData();
			drawContent(parent);
		}
	}

	protected abstract void drawContent(Composite parent);

	protected void layout(ChartCanvas chart, int i, int j) {
		chart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, i, j));
	}
}
