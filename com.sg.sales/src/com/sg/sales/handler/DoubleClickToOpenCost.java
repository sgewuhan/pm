package com.sg.sales.handler;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.sg.sales.model.WorkCost;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectDialog;

public class DoubleClickToOpenCost implements IDoubleClickListener {

	private static final String editorId = "sales.cost.editor.check";

	public DoubleClickToOpenCost() {
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		WorkCost workCost = (WorkCost) selection.getFirstElement();
		try {
			DataObjectDialog.openDialog(workCost, editorId, false, null);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
