package com.sg.business.visualization.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.business.model.ProjectProvider;
import com.sg.business.visualization.ui.IProjectProviderHolderListener;
import com.sg.business.visualization.ui.ProjectProviderHolder;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.part.IRefreshablePart;
import com.sg.widgets.part.StandaloneViewPart;

public abstract class AbstractDashWidgetView extends StandaloneViewPart
		implements IProjectProviderHolderListener,IRefreshablePart {

	protected ProjectProvider projectProvider;
	private Composite panel;
	private ProjectProviderHolder holder;

	@Override
	final protected void createContent(Composite parent) {
		this.panel = parent;
		loadData(parent);
	}

	private void loadData(final Composite parent) {
		holder = ProjectProviderHolder.getInstance();
		holder.addListener(this);
		projectProvider = holder.getProjectProvider();
		loadProjectProvider();
	}

	@Override
	public void dispose() {
		holder.removeListener(this);
		super.dispose();
	}

	protected abstract void drawContent(Composite parent);

	protected void layout(ChartCanvas chart, int i, int j) {
		chart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, i, j));
	}

	@Override
	public void parameterChanged(Object[] oldParameters, Object[] newParameters) {
		clean();
		loadProjectProvider();
	}

	private void loadProjectProvider() {
		if(projectProvider!=null){
			projectProvider.getData();
			drawContent(panel);
		}
	}

	private void clean() {
		Control[] children = panel.getChildren();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				if (!children[i].isDisposed()) {
					children[i].dispose();
				}
			}
		}
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {
		clean();
		loadProjectProvider();
	}

	@Override
	public void projectProviderChanged(ProjectProvider newProjectProvider,
			ProjectProvider oldProjectProvider) {
		projectProvider = newProjectProvider;
		projectProvider.addParameterChangedListener(this);
		clean();
		loadProjectProvider();
	}
	
}
