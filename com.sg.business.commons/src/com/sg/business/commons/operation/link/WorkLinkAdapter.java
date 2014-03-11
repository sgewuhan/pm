package com.sg.business.commons.operation.link;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Work;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;

public class WorkLinkAdapter implements SelectionListener {

	public WorkLinkAdapter() {
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		if (event.detail == RWT.HYPERLINK) {
			try {
				String action = event.text.substring(
						event.text.lastIndexOf("/") + 1, //$NON-NLS-1$
						event.text.indexOf("@")); //$NON-NLS-1$
				String _data = event.text
						.substring(event.text.indexOf("@") + 1); //$NON-NLS-1$
				if ("gowork".equals(action)) { //$NON-NLS-1$
					goWork(_data, event);
				}
			} catch (Exception e) {
			}
		}
	}

	private void goWork(String _data, SelectionEvent event) {
		Work work = ModelService.createModelObject(Work.class,
				new ObjectId(_data));
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		PrimaryObjectDetailFormView view = (PrimaryObjectDetailFormView) page.findView("pm2.work.detail");
		IEditorInputFactory inputFactory = work.getAdapter(IEditorInputFactory.class);
		view.setInput(inputFactory.getInput(null));
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

}
