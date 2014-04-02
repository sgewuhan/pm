package com.sg.business.commons.ui.sidebar;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.ISidebarItem;
import com.sg.widgets.part.LoadingIdentifier;

public abstract class AbstractListViewSideItem implements ISidebarItem {

	private ListViewer viewer;
	private DataSet ds;
	private LoadingIdentifier loadingIdentifier;
	private Label loading;
	protected IContext context;
	private Job job;
	private boolean reloaded;
	private ISelection runSetSelection;

	public AbstractListViewSideItem() {
		context = new CurrentAccountContext();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}
	
	@Override
	public StructuredViewer getViewer() {
		return viewer;
	}

	@Override
	public void doRefresh() {
		reloaded = false;
		final Control control = viewer.getControl();
		if (control.isDisposed()) {
			closeLoadingIdentifier();

			return;
		}
		final Display display = control.getDisplay();
		if (job != null) {
			job.cancel();
		}
		job = new Job("Refresh Data") {

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
						if (control.isDisposed()) {
							closeLoadingIdentifier();
							return;
						}
						viewer.setInput(ds.getDataItems());
						if(runSetSelection != null){
							viewer.setSelection(runSetSelection, true);
							runSetSelection = null;
						}
						reloaded = true;
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
		Composite bg = new Composite(parent, SWT.NONE);
		bg.setLayout(new FormLayout());

		viewer = new ListViewer(bg, SWT.SINGLE | SWT.V_SCROLL) {

			@Override
			public void setSelection(final ISelection selection) {
				if (reloaded) {
					super.setSelection(selection, true);
					runSetSelection = null;
				} else {
					 runSetSelection = selection;
				}
			}
		};
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		HTMLAdvanceLabelProvider labelProvider = new HTMLAdvanceLabelProvider();
		labelProvider.setKey("inlist");
		viewer.setLabelProvider(labelProvider);
		viewer.setUseHashlookup(true);
		IElementComparer comparer = getComparer();
		viewer.setComparer(comparer);

		List list = viewer.getList();
		HtmlUtil.enableMarkup(list);
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(36));
		FormData fd = new FormData();
		list.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		// 显示数据读取中
		loading = new Label(bg, SWT.NONE);
		HtmlUtil.enableMarkup(loading);
		fd = new FormData();
		loading.setLayoutData(fd);
		fd.width = 64;
		fd.height = 64;
		fd.left = new FormAttachment(50, -32);
		fd.top = new FormAttachment(50, -32);
		loading.moveAbove(null);

		doRefresh();
		return bg;
	}

	private IElementComparer getComparer() {
		return new IElementComparer() {

			@Override
			public boolean equals(Object a, Object b) {
				if (a instanceof PrimaryObject && b instanceof PrimaryObject) {
					ObjectId id1 = ((PrimaryObject) a).get_id();
					ObjectId id2 = ((PrimaryObject) b).get_id();
					return Utils.doCompare(id1, id2) == 0;
				}
				return Utils.doCompare(a, b) == 0;
			}

			@Override
			public int hashCode(Object element) {
				return element.hashCode();
			}
		};

	}

	protected abstract DataSetFactory getDataSetFactory();

	@Override
	public ISelectionProvider getSelectionProvider() {
		return viewer;
	}

	private void showLoadingIdentifier() {
		loadingIdentifier = new LoadingIdentifier();
		loadingIdentifier.open();
		loading.setText("<img src='ajax_loader_blue_64.gif' width='" + 64
				+ "' height='" + 64 + "'/>");
		loading.moveAbove(null);
	}

	private void closeLoadingIdentifier() {
		if (loadingIdentifier != null && !loadingIdentifier.isDisposed()) {
			loadingIdentifier.close();
		}
		loading.setText("");
		loading.moveBelow(null);
	}

	public Boolean isExpand() {
		return null;
	}

}
