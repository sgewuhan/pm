package com.sg.business.project.page;

import java.util.List;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.IPresentableObject;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractRoleAssignment;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.INavigatablePart;
import com.sg.widgets.part.NavigatorControl;

public class TransferUsersPage extends WizardPage implements INavigatablePart {
	private NavigatorControl navi;
	private String navigatorid;
	private PrimaryObject master;
	private int id;

	protected TransferUsersPage(String sTitle, String sDescription,
			String navigatorid, PrimaryObject master, int i) {
		super(sTitle);
		setTitle(sTitle);
		setDescription(sDescription);
		this.navigatorid = navigatorid;
		this.master = master;
		this.id = i;
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
						TransferUsersPage.this.getContainer().updateButtons();
						if (id == 0) {
							TransferUsersWizard wiz = (TransferUsersWizard) getWizard();
							IStructuredSelection selection = (IStructuredSelection) event
									.getSelection();
							if (selection != null && !selection.isEmpty()) {
								Object element = selection.getFirstElement();
								if (element instanceof AbstractRoleAssignment) {
									AbstractRoleAssignment assignment = (AbstractRoleAssignment) element;

									wiz.setUserId(assignment.getUserid());
								} else {
									wiz.setUserId(null);

								}
							} else {
								wiz.setUserId(null);

							}

						}
					}
				});

		if (id == 2) {
			StructuredViewer viewer = navi.getViewer();

			TreeViewerColumn column = new TreeViewerColumn((TreeViewer) viewer,
					SWT.LEFT);
			column.getColumn().setText("¸ºÔðÈË");
			column.getColumn().setWidth(60);
			column.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public Image getImage(Object element) {
					Work work = (Work) element;
					String chargerId = work.getChargerId();
					TransferUsersWizard wiz = (TransferUsersWizard) getWizard();
					if (chargerId != null && chargerId.equals(wiz.getUserId())) {
						return Widgets.getImage(ImageResource.CHECKED_16);
					} else {
						return null;
					}
				}

				@Override
				public String getText(Object element) {
					Work work = (Work) element;
					return work.getChargerId();
				}
			});
			

		}

		setControl(navi.getViewer().getControl());
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage() {
		if (id == 0) {
			ISelection selection = navi.getViewer().getSelection();
			if (selection != null && !selection.isEmpty()) {
				Object element = ((IStructuredSelection) selection)
						.getFirstElement();
				if (element instanceof AbstractRoleAssignment) {
					return true;
				}
			}
			return false;
		} else if (id == 1) {
			ISelection selection = navi.getViewer().getSelection();
			if (selection != null && !selection.isEmpty()) {
				Object element = ((IStructuredSelection) selection)
						.getFirstElement();
				if (element instanceof User) {
					return true;
				}
			}
			return false;
		}
		return true;
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

}
