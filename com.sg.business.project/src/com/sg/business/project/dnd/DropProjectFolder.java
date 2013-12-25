package com.sg.business.project.dnd;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.project.nls.Messages;
import com.sg.widgets.commons.dnd.DropPrimaryObjectTarget;
import com.sg.widgets.viewer.CTreeViewer;
import com.sg.widgets.viewer.ViewerControl;

public class DropProjectFolder extends DropPrimaryObjectTarget {

	
	
	@Override
	protected void doDrop(String sourceId, List<PrimaryObject> dragsItems,
			DropTargetEvent event, ViewerControl targetViewerControl) {
		if (dragsItems == null || dragsItems.isEmpty()) {
			return;
		}
		if (event.item == null) {
			return;
		}
		Object targetPo = event.item.getData();
		if (targetPo instanceof Folder) {
			Folder targetFolder = (Folder) targetPo;
			if (hasRoot(dragsItems)) {
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION
						| SWT.YES | SWT.NO);
				mb.setMessage(Messages.get().DropProjectFolder_0);
				mb.setText(Messages.get().DropProjectFolder_1);
				int ret = mb.open();
				if (ret == SWT.YES) {
					try {
						moveToOtherFolder(dragsItems, targetFolder,
								targetViewerControl);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					moveToOtherFolder(dragsItems, targetFolder,
							targetViewerControl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		super.doDrop(sourceId, dragsItems, event, targetViewerControl);
	}

	private boolean hasRoot(List<PrimaryObject> dragsItems) {
		for (PrimaryObject primaryObject : dragsItems) {
			if (primaryObject instanceof Folder) {
				Folder dragsFolder = (Folder) primaryObject;
				if (dragsFolder.getParent_id() == null) {
					return true;
				}
			}
		}
		return false;
	}

	private void moveToOtherFolder(List<PrimaryObject> dragsItems,
			Folder targetFolder, ViewerControl targetViewerControl)
			throws Exception {
		for (PrimaryObject primaryObject : dragsItems) {
			Folder parentFolder = null;
			if (primaryObject instanceof Folder) {
				Folder dragsFolder = (Folder) primaryObject;
				if (dragsFolder.getParent_id() != null) {
					dragsFolder.doMoveToOtherFolder(targetFolder.get_id());
					parentFolder = (Folder) dragsFolder
							.getParentFolder();
				}
			} else if (primaryObject instanceof Document) {
				Document dragsDocument = (Document) primaryObject;
				dragsDocument.doMoveToOtherFolder(targetFolder.get_id());
				parentFolder = dragsDocument.getFolder();
			}
			CTreeViewer viewer = (CTreeViewer) targetViewerControl.getViewer();
			viewer.refresh(targetFolder);
			if (parentFolder != null) {
				viewer.refresh(parentFolder);
			}
			viewer.expandToLevel(targetFolder, CTreeViewer.ALL_LEVELS);
		}
	}
}
