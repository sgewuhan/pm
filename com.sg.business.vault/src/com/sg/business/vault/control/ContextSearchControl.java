package com.sg.business.vault.control;

import java.util.ArrayList;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;
import com.sg.business.vault.index.SearchJob;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.view.NavigatorPart;

public class ContextSearchControl extends WorkbenchWindowControlContribution {

	public ContextSearchControl() {
	}

	public ContextSearchControl(String id) {
		super(id);
	}

	@Override
	protected Control createControl(Composite parent) {
		final Composite panel = new Composite(parent, SWT.BORDER);
		panel.setBackgroundMode(SWT.INHERIT_DEFAULT);
		panel.setLayout(new FormLayout());
		final Text text = new Text(panel, SWT.NONE);
		text.setMessage("<ÊäÈë¼ìË÷¹Ø¼ü×Ö>");
		FormData fd = new FormData();
		text.setLayoutData(fd);
		fd.top = new FormAttachment(0, 2);
		fd.left = new FormAttachment(0, 2);
		fd.width = 320;
		fd.bottom = new FormAttachment(100, -2);

		Button go = new Button(panel, SWT.PUSH);
		go.setData(RWT.CUSTOM_VARIANT, "whitebutton");
		go.setImage(BusinessResource.getImage(BusinessResource.IMAGE_SEARCH24));
		fd = new FormData();
		go.setLayoutData(fd);
		fd.left = new FormAttachment(text, 3);
		fd.width = 26;
		fd.height = 26;

		go.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				doSearch(panel.getDisplay(), text.getText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		return panel;
	}

	protected void doSearch(final Display display, String text) {
		if (text.length() == 0) {
			MessageUtil
					.showToast(null, "ÄÚÈÝ¼ìË÷", "ÇëÊäÈë¼ìË÷Ìõ¼þ", SWT.ICON_INFORMATION);
		}
		final SearchJob job = new SearchJob(text);
		job.setUser(true);
		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						ArrayList<PrimaryObject> result = job.getSearchResult();
						ContextSearchControl.this.loadData(result);
					}
				});
			}
		});
		job.schedule();

	}

	protected void loadData(ArrayList<PrimaryObject> result) {
		NavigatorPart part = (NavigatorPart) getWorkbenchWindow()
				.getActivePage().findView("vault.documents.contextsearch");
		DataSet dataSet= new DataSet(result);
		part.getNavigator().getViewerControl().setDataSet(dataSet);
	}

}
