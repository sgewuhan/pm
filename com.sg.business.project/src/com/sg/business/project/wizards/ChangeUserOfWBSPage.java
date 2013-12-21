package com.sg.business.project.wizards;

import java.util.List;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.IPresentableObject;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.business.project.nls.Messages;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.INavigatablePart;
import com.sg.widgets.part.NavigatorControl;

public class ChangeUserOfWBSPage extends WizardPage implements INavigatablePart {
	private NavigatorControl navi;
	private String navigatorid;
	private PrimaryObject master;

	protected ChangeUserOfWBSPage(String sName, String sTitle,
			String sDescription, String navigatorid, PrimaryObject master) {
		super(sName);
		setTitle(sTitle);
		setDescription(sDescription);
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
						List<PrimaryObject> changeWork = wiz.getChangeWork();
						changeWork.clear();
						Tree control2 = (Tree) getControl();
						TreeItem[] treeItems = control2.getItems();
						setChangeWork(changeWork, treeItems);
						ChangeUserOfWBSPage.this.getContainer().updateButtons();
					}

					public void setChangeWork(List<PrimaryObject> changeWork,
							TreeItem[] treeItems) {
						for (TreeItem treeItem : treeItems) {
							if (treeItem.getChecked()) {
								changeWork.add((PrimaryObject) treeItem
										.getData());
							}
							if (treeItem.getItemCount() > 0) {
								setChangeWork(changeWork, treeItem.getItems());
							}
						}
					}

				});

		StructuredViewer viewer = navi.getViewer();

		TreeViewerColumn column = new TreeViewerColumn((TreeViewer) viewer,
				SWT.CENTER);
		column.getColumn().setText(Messages.get().ChangeUserOfWBSPage_0);
		column.getColumn().setWidth(60);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				Work work = (Work) element;
				String chargerId = work.getChargerId();
				ChangeUserWizard wiz = (ChangeUserWizard) getWizard();
				if (chargerId != null
						&& chargerId.equals(wiz.getChangedUserId())) {
					return Widgets.getImage(ImageResource.CHECKED_2_16);
				} else {
					return null;
				}
			}

			@Override
			public String getText(Object element) {
				return ""; //$NON-NLS-1$
			}
		});
		column = new TreeViewerColumn((TreeViewer) viewer, SWT.CENTER);
		column.getColumn().setText(Messages.get().ChangeUserOfWBSPage_2);
		column.getColumn().setWidth(60);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				Work work = (Work) element;
				String assignerId = work.getAssignerId();
				ChangeUserWizard wiz = (ChangeUserWizard) getWizard();
				if (assignerId != null
						&& assignerId.equals(wiz.getChangedUserId())) {
					return Widgets.getImage(ImageResource.CHECKED_2_16);
				} else {
					return null;
				}
			}

			@Override
			public String getText(Object element) {
				return ""; //$NON-NLS-1$
			}
		});
		column = new TreeViewerColumn((TreeViewer) viewer, SWT.CENTER);
		column.getColumn().setText(Messages.get().ChangeUserOfWBSPage_4);
		column.getColumn().setWidth(60);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				Work work = (Work) element;
				String userId;
				List<?> participatesIdList = work.getParticipatesIdList();
				ChangeUserWizard wiz = (ChangeUserWizard) getWizard();
				if (participatesIdList != null) {
					for (int i = 0; i < participatesIdList.size(); i++) {
						userId = (String) participatesIdList.get(i);
						if (userId.equals(wiz.getChangedUserId())) {
							return Widgets.getImage(ImageResource.CHECKED_2_16);
						}
					}
					return null;
				} else {
					return null;
				}
			}

			@Override
			public String getText(Object element) {
				return ""; //$NON-NLS-1$
			}
		});
		column = new TreeViewerColumn((TreeViewer) viewer, SWT.CENTER);
		column.getColumn().setText(Messages.get().ChangeUserOfWBSPage_6);
		column.getColumn().setWidth(70);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				Work work = (Work) element;
				ChangeUserWizard wiz = (ChangeUserWizard) getWizard();
				IProcessControl pc = (IProcessControl) work
						.getAdapter(IProcessControl.class);
				String changedUserId = (String) wiz.getChangedUserId();
				if (changedUserId != null) {
					if (ProjectToolkit.checkProcessInternal(pc,
							IWorkCloneFields.F_WF_EXECUTE, changedUserId)) {
						return Widgets.getImage(ImageResource.CHECKED_2_16);
					} else {
						return null;
					}
				} else {
					return null;
				}

			}

			@Override
			public String getText(Object element) {
				return ""; //$NON-NLS-1$
			}
		});
		column = new TreeViewerColumn((TreeViewer) viewer, SWT.CENTER);
		column.getColumn().setText(Messages.get().ChangeUserOfWBSPage_8);
		column.getColumn().setWidth(70);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				Work work = (Work) element;
				ChangeUserWizard wiz = (ChangeUserWizard) getWizard();
				IProcessControl pc = (IProcessControl) work
						.getAdapter(IProcessControl.class);
				if (ProjectToolkit.checkProcessInternal(pc,
						IWorkCloneFields.F_WF_CHANGE,
						(String) wiz.getChangedUserId())) {
					return Widgets.getImage(ImageResource.CHECKED_2_16);
				} else {
					return null;
				}

			}

			@Override
			public String getText(Object element) {
				return ""; //$NON-NLS-1$
			}
		});

		setControl(navi.getViewer().getControl());
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage() {
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
		// TODO Auto-generated method stub

	}

}