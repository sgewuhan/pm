package com.sg.business.finance.rndcost;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.dataset.finance.FinanceOrganizationDataSet;

public class CostCenterSelector extends WorkbenchWindowControlContribution {

	public CostCenterSelector() {
	}

	public CostCenterSelector(String id) {
		super(id);
	}

	@Override
	protected Control createControl(Composite parent) {
		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);

		ComboViewer viewer = new ComboViewer(parent,SWT.READ_ONLY);
		viewer.getControl().setData(RWT.CUSTOM_VARIANT, "whitebutton");
		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof PrimaryObject){
					return ((PrimaryObject) element).getLabel();
				}
				return super.getText(element);
			}
		});
		
		DataSet ds = new FinanceOrganizationDataSet().getDataSet();
		viewer.setInput(ds.getDataItems());
		return viewer.getControl();
	}

}
