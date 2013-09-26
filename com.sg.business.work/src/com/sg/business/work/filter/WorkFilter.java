package com.sg.business.work.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.Work;
import com.sg.widgets.viewer.CTreeViewer;

public class WorkFilter extends ViewerFilter {

	private int code;

	public WorkFilter(int code) {
		this.code = code;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Work parent = (Work) parentElement;
		Work work = (Work) element;
		switch (code) {
		case WorkFilterAction.SHOW_WORK_ON_READY:
			return selectByLifecycle((CTreeViewer)viewer, parent, work,
					ILifecycle.STATUS_ONREADY_VALUE);
		default:
			break;
		}

		return false;
	}

	private boolean selectByLifecycle(CTreeViewer viewer, Work parent, Work work,
			String status) {
		if(status.equals(work.getLifecycleStatus())){
			return true;
		}else{
			Widget[] items = viewer.testFindItems(work);
			return selectByLifecycleForChildren((TreeItem[])items,status);
		}
	}

	private boolean selectByLifecycleForChildren(TreeItem[] items, String status) {
		for (int i = 0; i < items.length; i++) {
			TreeItem[] children = items[i].getItems();
			
			
		}
		return false;
	}

}
