package com.sg.business.work.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

import com.sg.business.model.UserTask;
import com.sg.widgets.part.view.TableNavigator;

public class WorkFlowWorkDelivery extends TableNavigator {

	// private TableViewer fileViewer;

	@Override
	public void createPartControl(Composite parent) {

		// parent.setLayout(new FormLayout());
		// Composite filePanel = new Composite(parent, SWT.NONE);
		// FormData fd = new FormData();
		// filePanel.setLayoutData(fd);
		// fd.right = new FormAttachment(100);
		// fd.top = new FormAttachment();
		// fd.bottom = new FormAttachment(100);
		// fd.width = 200;
		// filePanel.setLayout(new FillLayout());
		// createFileViewer(filePanel);
		//
		// Composite documentPanel = new Composite(parent, SWT.NONE);
		// documentPanel.setLayout(new FillLayout());
		super.createPartControl(parent);
		// fd = new FormData();
		// documentPanel.setLayoutData(fd);
		// fd.left = new FormAttachment();
		// fd.top = new FormAttachment();
		// fd.bottom = new FormAttachment(100);
		// fd.right = new FormAttachment(filePanel, -2);
		//
		// navi.getViewer().addSelectionChangedListener(
		// new ISelectionChangedListener() {
		//
		// @Override
		// public void selectionChanged(SelectionChangedEvent event) {
		// IStructuredSelection sel = (IStructuredSelection) event
		// .getSelection();
		// setFileViewerInput((Document) sel.getFirstElement());
		// }
		// });
	}

	// protected void setFileViewerInput(Document document) {
	// List<RemoteFile> fileValue = document.getFileValue(Document.F_VAULT);
	// fileViewer.setInput(fileValue);
	// }
	//
	// private void createFileViewer(Composite filePanel) {
	// fileViewer = new TableViewer(filePanel,SWT.FULL_SELECTION);
	// fileViewer.setContentProvider(ArrayContentProvider.getInstance());
	// fileViewer.setLabelProvider(new LabelProvider(){
	// @Override
	// public String getText(Object element) {
	// return super.getText(element);
	// }
	// });
	//
	// }

	@Override
	protected void updatePartName(IWorkbenchPart part) {
		if (master != null) {
			String workname = ((UserTask) master).getWorkName();
			setPartName(workname + " ÎÄµµ");
		} else {
			setPartName(originalPartName);
		}
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
	public boolean canEdit() {
		return false;
	}
	
	@Override
	public boolean canRefresh() {
		return super.canRefresh();
	}
	
	@Override
	public boolean canRead() {
		return false;
	}

}
