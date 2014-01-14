package com.sg.business.management.wizard;

import java.util.List;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.IPresentableObject;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.management.nls.Messages;
import com.sg.business.model.AbstractOptionFilterable;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.part.INavigatablePart;
import com.sg.widgets.part.NavigatorControl;

public class PreviewPage extends WizardPage implements INavigatablePart {

	private NavigatorControl navi;

	protected PreviewPage() {
		super(Messages.get().PreviewPage_0);
		
		setDescription(Messages.get().PreviewPage_1);

	}

	@Override
	public void createControl(Composite parent) {
		navi = new NavigatorControl("management.deliverables", this); //$NON-NLS-1$
		navi.createPartContent(parent);
		ProjectPreviewer wiz = (ProjectPreviewer) getWizard();
		ProjectTemplate template = wiz.getProjectTemplate();
		navi.masterChanged(template, null, null);

		setControl(navi.getViewer().getControl());
		setPageComplete(true);
	}
	
	@Override
	public void setMasterChanged(PrimaryObject master, PrimaryObject oldMaster,
			IWorkbenchPart part) {
	}

	@Override
	public void reloadMaster() {

	}

	@Override
	public boolean canRefresh() {
		return false;
	}

	@Override
	public void doRefresh() {
	}

	@Override
	public boolean canEdit() {
		return false;
	}

	@Override
	public boolean canCreate() {
		return false;
	}

	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public boolean canRead() {
		return false;
	}

	@Override
	public boolean hasMultiEditor() {
		return false;
	}

	@Override
	public void doEdit() {
	}

	@Override
	public void doCreate() {
	}

	@Override
	public void doDelete() {
	}

	@Override
	public void doCreate(String editorId) {
	}

	@Override
	public void doEdit(String editorId, String pageId) {
	}
	@Override
	public void doEdit(String editorId, String pageId, String opentype,Boolean editable) {
	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void doExport() {
	}

	@Override
	public boolean canImport() {
		return false;
	}

	@Override
	public void doImport() {
	}

	@Override
	public boolean canProvideComparableObject() {
		return false;
	}

	@Override
	public List<IPresentableObject> getPresentableObject() {
		return null;
	}

	@Override
	public NavigatorControl getNavigator() {
		return navi;
	}

	@Override
	public IToolBarManager getToolBarManager() {
		return null;
	}

	public void setFilters(final BasicDBList conditions) {

		navi.getViewer().setFilters(new ViewerFilter[] { new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element instanceof AbstractOptionFilterable) {
					AbstractOptionFilterable item = (AbstractOptionFilterable) element;
					for (int i = 0; i < conditions.size(); i++) {
						DBObject condition = (DBObject) conditions.get(i);
						String optionSet = (String) condition
								.get(AbstractOptionFilterable.SF_OPTIONSET);
						String option = (String) condition
								.get(AbstractOptionFilterable.SF_OPTION);
						String setting = item.getOptionValueSetting(optionSet,
								option);
						if (AbstractOptionFilterable.VALUE_EXCLUDE
								.equals(setting)) {
							return false;
						}
					}
					return true;
				}

				return false;
			}
		} });
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void setNavigatorControl(NavigatorControl navigatorControl) {
		
	}

}
