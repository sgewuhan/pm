package com.sg.business.commons;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.mobnut.portal.Portal;
import com.mobnut.portal.user.UserSessionContext;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class DirectApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "perspective.work"; //$NON-NLS-1$
	private IMemento memento;

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		WorkbenchWindowAdvisor advisor = new DirectWorkbenchWindowAdvisor(
				configurer);
		advisor.saveState(memento);
		return advisor;
	}

	public String getInitialWindowPerspectiveId() {
		if (Portal.isScenarioEnable()) {
			return UserSessionContext.getSession().getDefaultPerspective();
		} else {
			return PERSPECTIVE_ID;
		}
	}

	@Override
	public IStatus saveState(IMemento memento) {
		this.memento = memento;
		return super.saveState(memento);
	}

}
