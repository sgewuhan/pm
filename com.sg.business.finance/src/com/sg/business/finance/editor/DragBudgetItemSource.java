package com.sg.business.finance.editor;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class DragBudgetItemSource implements DragSourceListener {

	private TreeViewer viewer;

	public DragBudgetItemSource(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (selection == null || selection.isEmpty()) {
			return;
		} else {
			ArrayList<DBObject> items = new ArrayList<DBObject>();
			Iterator<?> iter = selection.iterator();
			while (iter.hasNext()) {
				PrimaryObject po = (PrimaryObject) iter.next();
				DBObject dataItem = po.get_data();
				items.add(dataItem);
			}
			doDrag(items, event);
		}
	}

	protected void doDrag(Object data, DragSourceEvent event) {
		event.data = JSON.serialize(data);
        
	}

	@Override
	public void dragFinished(DragSourceEvent event) {

	}

}
