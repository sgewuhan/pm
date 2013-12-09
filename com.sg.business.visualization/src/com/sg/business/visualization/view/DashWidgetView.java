package com.sg.business.visualization.view;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mobnut.db.model.DataSet;
import com.sg.business.model.IParameterListener;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.dataset.organization.OrgOfOwnerManager;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.part.StandaloneViewPart;

public abstract class DashWidgetView extends StandaloneViewPart implements IParameterListener {

	protected ProjectProvider projectProvider;
	private Composite panel;

	@Override
	final protected void createContent(Composite parent) {
		this.panel = parent;
		loadData(parent);
	}

	private void loadData(final Composite parent) {

		Object value = RWT.getApplicationContext().getAttribute(
				"projectProvider");
		if (value instanceof ProjectProvider) {
			projectProvider = (ProjectProvider) value;
		} else {
			final OrgOfOwnerManager oom = new OrgOfOwnerManager();
			DataSet ds = oom.getDataSet();
			if (!ds.isEmpty()) {
				Organization org = (Organization) ds.getDataItems().get(0);
				projectProvider = org.getAdapter(ProjectProvider.class);
				RWT.getApplicationContext().setAttribute("projectProvider",
						projectProvider);
			}
		}
		if (projectProvider != null) {
			projectProvider.addParameterChangedListener(this);
			projectProvider.getData();
			drawContent(parent);
		}
	}


	protected abstract void drawContent(Composite parent);

	protected void layout(ChartCanvas chart, int i, int j) {
		chart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, i, j));
	}
	
	@Override
	public void parameterChanged(Object[] oldParameters, Object[] newParameters) {
		projectProvider.getData();
		Control[] children = panel.getChildren();
		if(children!=null){
			for (int i = 0; i < children.length; i++) {
				if(!children[i].isDisposed()){
					children[i].dispose();
				}
			}
		}
		drawContent(panel);
	}
}
