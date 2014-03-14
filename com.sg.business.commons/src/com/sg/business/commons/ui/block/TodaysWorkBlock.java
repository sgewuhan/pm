package com.sg.business.commons.ui.block;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.operation.link.WorkLinkAdapter;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.Work;
import com.sg.business.model.WorksPerformence;
import com.sg.business.model.dataset.work.OwnerPerformenceWorkDataSet;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.Block;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.editor.DataObjectDialog;

public class TodaysWorkBlock extends Block {

	public static final int BLOCKWIDTH = 300;
	private ListViewer viewer;
	private OwnerPerformenceWorkDataSet ds;
	private static final int ITEM_HIGHT = 50;

	public TodaysWorkBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void go() {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			workbench.showPerspective(UIFrameworkUtils.PERSPECTIVE_WORK, window);
		} catch (WorkbenchException e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	protected void createContent(Composite parent) {
		init();

		parent.setLayout(new FormLayout());
		List list = new List(parent, SWT.SINGLE);
		list.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				viewer.setSelection(new StructuredSelection(new Object[] {}));
			}
		});
		list.addSelectionListener(new WorkLinkAdapter());
		viewer = new ListViewer(list);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		HTMLAdvanceLabelProvider labelProvider = new HTMLAdvanceLabelProvider();
		labelProvider.setKey("record");
		labelProvider.setViewer(viewer);
		labelProvider.setWidthHint(BLOCKWIDTH);
		labelProvider.setHeightHint(ITEM_HIGHT);

		viewer.setLabelProvider(labelProvider);
		viewer.setUseHashlookup(true);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection sel = event.getSelection();
				if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
					select((Work) ((IStructuredSelection) sel)
							.getFirstElement());
				}
			}
		});

		HtmlUtil.enableMarkup(list);
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(ITEM_HIGHT));
		FormData fd = new FormData();
		list.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);

		doRefresh();
	}

	protected String getHomeViewId() {
		return UIFrameworkUtils.VIEW_HOME_COMMON;
	}

	protected void select(Work work) {
		String userid = context.getAccountInfo().getConsignerId();
		Date date = new Date();
		WorksPerformence po = work.getWorksPerformence(date, userid);

		if (po != null) {
			int ok = MessageUtil.showMessage(null,
					Messages.get(getDisplay()).AddWorkRecord_0,
					Messages.get(getDisplay()).AddWorkRecord_1,
					SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			if (ok != SWT.YES) {
				return;
			}
		} else {
			po = work.makeTodayWorksPerformence(userid);
		}

		try {
			DataObjectDialog dialog = DataObjectDialog.openDialog(po, "editor.create.workrecord", //$NON-NLS-1$
					true, null);
			int rc = dialog.getReturnCode();
			if(rc == DataObjectDialog.OK){
				doRefresh();
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	private void init() {
		ds = new OwnerPerformenceWorkDataSet();
	}

	@Override
	protected Object doGetData() {
		Date today = new Date();
		DataSet dataSet = ds.getDataSet();
		java.util.List<PrimaryObject> input = new ArrayList<PrimaryObject>();
		java.util.List<PrimaryObject> items = dataSet.getDataItems();
		Iterator<PrimaryObject> iter = items.iterator();
		int limit = getContentHeight() / ITEM_HIGHT;
		while (iter.hasNext() && input.size() < limit) {
			PrimaryObject po = iter.next();
			Work work = (Work) po;
			if (!work.isSummaryWork()) {
				WorksPerformence record = work.getWorksPerformence(today,
						context.getUserId());
				if (record == null) {
					input.add(work);
				}
			}
		}
		return input;
	}

	@Override
	protected void doDisplayData(Object data) {
		viewer.setInput(data);
	}

	public int getContentHeight() {
		return 201;
	}

}
