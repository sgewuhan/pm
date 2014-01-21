package com.sg.business.work.home;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.mobnut.db.model.DataSet;
import com.sg.business.model.dataset.projecttemplate.LaunchableWorkSet;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.ISidebarItem;

public class Launchablelist implements ISidebarItem {

	private ListViewer viewer;
	private LaunchableWorkSet dataSetFactory;

	public Launchablelist() {
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {
		DataSet ds = dataSetFactory.getDataSet();
		viewer.setInput(ds.getDataItems());
	}

	@Override
	public Composite create(Composite parent) {
		viewer = new ListViewer(parent, SWT.SINGLE);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new HTMLAdvanceLabelProvider());
		viewer.setUseHashlookup(true);
		
		List list = viewer.getList();
		list.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(36));
		dataSetFactory = new LaunchableWorkSet();
		doRefresh();
		return parent;
	}

	@Override
	public ISelectionProvider getSelectionProvider() {
		return viewer;
	}


}
