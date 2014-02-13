package com.sg.business.pm2.home;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.ISidebarItem;

public abstract class AbstractListViewSideItem implements ISidebarItem {

	private ListViewer viewer;
	private DataSetFactory dataSetFactory;

	public AbstractListViewSideItem() {
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
		viewer = new ListViewer(parent, SWT.SINGLE|SWT.V_SCROLL);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new HTMLAdvanceLabelProvider());
		viewer.setUseHashlookup(true);
		
		List list = viewer.getList();
		list.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(36));
		dataSetFactory = createDataSetFactory();
		doRefresh();
		return parent;
	}

	protected abstract DataSetFactory createDataSetFactory() ;

	@Override
	public ISelectionProvider getSelectionProvider() {
		return viewer;
	}


}
