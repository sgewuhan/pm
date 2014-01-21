package com.sg.business.work.home.action;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Work;
import com.sg.business.work.home.WorkDetail;

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
		WorkDetail view = (WorkDetail) page.findView("pm2.work.detail");
		view.setInputWork(work);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
