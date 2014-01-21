package com.sg.business.project.wizards;

import java.util.List;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.IPresentableObject;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.INavigatablePart;
import com.sg.widgets.part.NavigatorControl;

public class ChangeUserOfOrgUserPage extends WizardPage implements
		INavigatablePart {
	private NavigatorControl navi;
	private String navigatorid;
	private PrimaryObject master;

	protected ChangeUserOfOrgUserPage(String sName, String sTitle,
			String sDescription, String navigatorid, PrimaryObject master) {
		super(sName);
		setTitle(sTitle);
		setDescription(sDescription);
		setMessage(Messages.get().ChangeUserOfOrgUserPage_0);
		this.navigatorid = navigatorid;
		this.master = master;
	}

	@Override
	public void createControl(Composite parent) {
		navi = new NavigatorControl(navigatorid, this);
		navi.createPartContent(parent);
		navi.masterChanged(master, null, null);
		navi.getViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						ChangeUserWizard wiz = (ChangeUserWizard) getWizard();
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						if (selection != null && !selection.isEmpty()) {
							Object element = selection.getFirstElement();
							if (element instanceof User) {
								User assignment = (User) element;
								
								wiz.setChangeUserId(assignment.getUserid());
								setMessage(Messages.get().ChangeUserOfOrgUserPage_1
										+ UserToolkit.getUserById(wiz
												.getChangedUserId())
												+ Messages.get().ChangeUserOfOrgUserPage_2
												+ UserToolkit.getUserById(assignment
														.getUserid()) + "\" "); //$NON-NLS-1$
								setErrorMessage(null);
							} else {
								wiz.setChangeUserId(null);
								
							}
						} else {
							wiz.setChangeUserId(null);
							
						}
						ChangeUserOfOrgUserPage.this.getContainer()
								.updateButtons();
					}
				});
		setControl(navi.getViewer().getControl());
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage() {
		ISelection selection = navi.getViewer().getSelection();
		if (selection != null && !selection.isEmpty()) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			if (element instanceof User) {
				User assignment = (User) element;
				ChangeUserWizard wiz = (ChangeUserWizard) getWizard();
				if (wiz.getChangedUserId().equals(assignment.getUserid())) {
					setErrorMessage(Messages.get().ChangeUserOfOrgUserPage_4
							+ UserToolkit.getUserById(wiz
									.getChangedUserId())
							+ Messages.get().ChangeUserOfOrgUserPage_5
							+ UserToolkit.getUserById(assignment.getUserid())
							+ "\" "); //$NON-NLS-1$
				} else {
					return true;
				}
			}
		}
		return false;
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
		navi.getViewer().refresh();
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
	public void doEdit(String editorId, String pageId, String opentype,
			Boolean editable) {
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

	@Override
	public void activate() {
	}

	@Override
	public void setNavigatorControl(NavigatorControl navigatorControl) {
	}

}