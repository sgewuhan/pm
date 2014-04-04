package com.tmt.pdm.dcpdm.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.mobnut.db.model.IContext;
import com.sg.business.commons.ui.part.WorkListCreater;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.tmt.pdm.dcpdm.selector.DCPDMObjectSelectWizard;

public class LinkPDMDocAndDrawOfProjectChange extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Event evenTriggert = (Event) event.getTrigger();
		MenuItem menuitem = (MenuItem) evenTriggert.widget;
		Menu menu = menuitem.getParent();
		WorkListCreater workListCreater = (WorkListCreater) menu
				.getData("treeViewer");
		IStructuredSelection selection = workListCreater.getSelection();
		if (selection != null && !selection.isEmpty()) {
			Object element = selection.getFirstElement();
			final Work work;
			if (element instanceof Work) {
				work = (Work) element;
			} else if (element instanceof Deliverable) {
				Deliverable deliverable = (Deliverable) element;
				work = (Work) deliverable.getParentPrimaryObjectCache();
			} else {
				work = null;
			}
			if (work != null) {
				try {
					Set<String> result = DCPDMObjectSelectWizard
							.selectPDMObject(workListCreater.getShell());
					if (result == null) {
						return null;
					}
					List<Document> documentList = new ArrayList<Document>();

					Iterator<String> iter = result.iterator();
					while (iter.hasNext()) {
						String ouid = iter.next();
						try {
							IContext context = new CurrentAccountContext();
							Document doc = DCPDMUtil.createDocument(
									ouid, context);
							documentList.add(doc);
						} catch (Exception e) {
						}
					}
					workListCreater.createDeliverable(work, documentList, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}

			}
		}

		return null;
	}
}
