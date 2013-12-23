package com.sg.business.visualization.ui;

import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.rap.rwt.RWT;

import com.mobnut.db.model.DataSet;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.dataset.organization.OrgOfOwnerManager;

public class ProjectProviderHolder {

	private ProjectProvider projectProvider;

	private ListenerList listeners;

	ProjectProviderHolder() {
		final OrgOfOwnerManager oom = new OrgOfOwnerManager();
		DataSet ds = oom.getDataSet();
		if (!ds.isEmpty()) {
			Organization org = (Organization) ds.getDataItems().get(0);
			projectProvider = org.getAdapter(ProjectProvider.class);
		} else {
			projectProvider = null;
		}
	}

	@SuppressWarnings("rawtypes")
	public List getProjectProviderList() {
		final OrgOfOwnerManager oom = new OrgOfOwnerManager();
		DataSet ds = oom.getDataSet();
		return ds.getDataItems();
	}

	public ProjectProvider getProjectProvider() {
		return projectProvider;
	}

	public void setCurrentProjectProvider(ProjectProvider provider) {
		ProjectProvider oldProjectProvider = projectProvider;
		projectProvider = provider;
		fireProjectProviderChanged(projectProvider, oldProjectProvider);
	}

	private void fireProjectProviderChanged(ProjectProvider newProjectProvider,
			ProjectProvider oldProjectProvider) {
		if (listeners != null) {
			Object[] lis = listeners.getListeners();
			for (int i = 0; i < lis.length; i++) {
				if (lis != null) {
					((IProjectProviderHolderListener) lis[i])
							.projectProviderChanged(newProjectProvider,
									oldProjectProvider);
				}
			}
		}
	}

	public static ProjectProviderHolder getInstance() {
		Object value = RWT.getUISession().getAttribute("projectProviderHolder"); //$NON-NLS-1$
		if (value instanceof ProjectProviderHolder) {
			return (ProjectProviderHolder) value;
		} else {
			ProjectProviderHolder holder = new ProjectProviderHolder();
			RWT.getUISession().setAttribute("projectProviderHolder", holder); //$NON-NLS-1$
			return holder;
		}
	}

	public void addListener(IProjectProviderHolderListener listener) {
		if (listeners == null) {
			listeners = new ListenerList();
		}
		listeners.add(listener);
		if (projectProvider != null) {
			projectProvider.addParameterChangedListener(listener);
		}
	}

	public void removeListener(IProjectProviderHolderListener listener) {
		if (listener != null && listeners != null) {
			listeners.remove(listener);
		}
		if (projectProvider != null) {
			projectProvider.removeParameterChangedListener(listener);
		}
	}

}
