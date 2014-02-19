package com.sg.business.pm2.home;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.IContext;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.ISidebarItem;
import com.sg.widgets.part.LoadingIdentifier;

public abstract class AbstractListViewSideItem implements ISidebarItem {

	private ListViewer viewer;
	private DataSet ds;
	private LoadingIdentifier loadingIdentifier;
	protected IContext context;

	public AbstractListViewSideItem() {
		context = new CurrentAccountContext();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {
		final Control control = viewer.getControl();
		if (control.isDisposed()) {
			closeLoadingIdentifier();
			return;
		}
		final Display display = control.getDisplay();
		Job job = new Job("Refresh Data") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				
				ds = getDataSetFactory().getDataSet();
				return Status.OK_STATUS;
			}

		};
		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				if (display.isDisposed()) {
					closeLoadingIdentifier();
					return;
				}
				
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						if(control.isDisposed()){
							closeLoadingIdentifier();
							return;
						}
						viewer.setInput(ds.getDataItems());
						closeLoadingIdentifier();
					}
				});
			}
		});
		showLoadingIdentifier();
		job.schedule();
	}

	@Override
	public Composite create(Composite parent) {
		viewer = new ListViewer(parent, SWT.SINGLE | SWT.V_SCROLL);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new HTMLAdvanceLabelProvider());
		viewer.setUseHashlookup(true);

		List list = viewer.getList();
		list.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(36));
		doRefresh();
		return parent;
	}

	protected abstract DataSetFactory getDataSetFactory();

	@Override
	public ISelectionProvider getSelectionProvider() {
		return viewer;
	}

	private void showLoadingIdentifier() {
		loadingIdentifier = new LoadingIdentifier();
		loadingIdentifier.open();
	}

	private void closeLoadingIdentifier() {
		if (loadingIdentifier != null && !loadingIdentifier.isDisposed()) {
			loadingIdentifier.close();
		}
	}

}
