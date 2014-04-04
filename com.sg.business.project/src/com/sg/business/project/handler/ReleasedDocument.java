package com.sg.business.project.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class ReleasedDocument extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("请选中工作", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Work) {
			Work work = (Work) selected;
			List<PrimaryObject> documents = work.getDeliverableDocuments();
			for (PrimaryObject po : documents) {
				Document document = (Document) po;
				try {
					document.doSetLifeCycleStatus(new CurrentAccountContext(),
							Document.STATUS_RELEASED_ID);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		}

	}
}
