package com.sg.sales.ui.block;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.dataset.MyOpportunityDataSet;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.list.ListBlock;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.editor.DataObjectEditor;

public class OpportunityBlock extends ListBlock {

	private static final String PERSPECTIVE = "sales.opportunity";
	private MyOpportunityDataSet dataset;

	public OpportunityBlock(Composite parent) {
		super(parent);
	}
	
	@Override
	protected void init() {
		dataset = new MyOpportunityDataSet();
		super.init();
	}
	
	@Override
	protected Object doGetData() {
		int limit = getCountByHeight();
		dataset.setLimit(limit);
		return dataset.getDataSet().getDataItems();
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
		} else {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			select(element);
		}
	}

	@Override
	protected HTMLAdvanceLabelProvider getLabelProvider() {
		return new HTMLAdvanceLabelProvider();
	}


	@Override
	protected String getPerspective() {
		return PERSPECTIVE;
	}

	@Override
	protected void select(Object element) {
		if (element instanceof PrimaryObject) {
			PrimaryObject primaryObject = (PrimaryObject) element;
			try {
				String editorId = "sales.opportunity.editor";
				DataObjectEditor.open(primaryObject, editorId , true, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}		
	}

}
