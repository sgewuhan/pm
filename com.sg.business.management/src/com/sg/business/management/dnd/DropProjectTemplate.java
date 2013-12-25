package com.sg.business.management.dnd;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.management.nls.Messages;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.commons.dnd.DropPrimaryObjectTarget;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class DropProjectTemplate extends DropPrimaryObjectTarget {

	@Override
	protected void doDrop(String sourceId, List<PrimaryObject> dragsItems,
			DropTargetEvent event, ViewerControl targetViewerControl) {

		if (dragsItems == null || dragsItems.isEmpty()) {
			return;
		}
		if (event.item == null) {
			return;
		}

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES
				| SWT.NO | SWT.CANCEL);
		mb.setMessage(Messages.get().DropProjectTemplate_0);
		mb.setText(Messages.get().DropProjectTemplate_1);
		int ret = mb.open();

		Object targetPo = event.item.getData();
		if (targetPo instanceof Organization) {
			Organization org = (Organization) targetPo;
			for (PrimaryObject po : dragsItems) {
				if (ret == SWT.YES) {
					// 选择yes，复制项目模版
					try {
						org.doCopyProjectTemplates(po,
								new CurrentAccountContext());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (ret == SWT.NO) {
					// 选择NO，移动项目模版归属组织
					if (po instanceof ProjectTemplate) {
						org.doAddProjectTemplate(po.get_id());
						// } else if (po instanceof WorkDefinition) {
						// org.doAddWorkDefinition(po.get_id());
					}
				} else {
					// 选择Cancel
				}
			}
		}
		super.doDrop(sourceId, dragsItems, event, targetViewerControl);
	}

}
