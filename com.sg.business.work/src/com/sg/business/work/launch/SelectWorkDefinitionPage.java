package com.sg.business.work.launch;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.dataset.organization.LaunchOrgOfCurrentUser;
import com.sg.business.resource.BusinessResource;
import com.sg.business.work.nls.Messages;
import com.sg.widgets.part.NavigatablePartAdapter;
import com.sg.widgets.part.NavigatorControl;

public class SelectWorkDefinitionPage extends WizardPage {

	private NavigatorControl navi;
	private NavigatablePartAdapter navigatorPart;
	protected WorkDefinition workd;

	protected SelectWorkDefinitionPage() {
		super("SELECT_WORK_DEFINITION"); //$NON-NLS-1$
		setTitle(Messages.get().SelectWorkDefinitionPage_1);
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_TEMPLATE_72));
	}

	@Override
	public void createControl(Composite parent) {
		Composite content = new Composite(parent, SWT.NONE);
		navigatorPart = createNavigatorPart();
		navi = new NavigatorControl("management.organization.standlonework", //$NON-NLS-1$
				navigatorPart);
		navi.createPartContent(content);

		// 过滤不是当前用户所在的组织
		LaunchOrgOfCurrentUser dsf = new LaunchOrgOfCurrentUser();
		final List<PrimaryObject> items = dsf.getDataSet().getDataItems();
		ViewerFilter viewerFilter = new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element instanceof Organization) {
					return items.contains(element);
				}
				return true;
			}
		};
		navi.getViewer().setFilters(new ViewerFilter[] { viewerFilter });
		navi.getViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection is = (IStructuredSelection) event
								.getSelection();

						boolean complete = is != null
								&& !is.isEmpty()
								&& is.getFirstElement() instanceof WorkDefinition;
						
						if (complete) {
							WorkDefinition workDefinition = (WorkDefinition) is
									.getFirstElement();
							if (workDefinition.isActivated()) {
								workd = workDefinition;
							} else {
								workd = null;
								complete = false;
							}
						} else {
							workd = null;
						}
						setPageComplete(complete);
						getWizard().setWorkDefinition(workd);
					}
				});
		setControl(content);
		setPageComplete(false);

	}

	private NavigatablePartAdapter createNavigatorPart() {
		return new NavigatablePartAdapter() {
		};
	}

	@Override
	public LaunchWorkWizard getWizard() {
		return (LaunchWorkWizard) super.getWizard();
	}

}
