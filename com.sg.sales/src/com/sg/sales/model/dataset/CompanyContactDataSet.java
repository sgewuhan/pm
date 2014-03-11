package com.sg.sales.model.dataset;

import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.Company;
import com.sg.sales.model.ICompanyRelative;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class CompanyContactDataSet extends MasterDetailDataSetFactory {

	public CompanyContactDataSet() {
		super(IModelConstants.DB, Sales.C_CONTACT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ICompanyRelative.F_COMPANY_ID;
	}
	
	@Override
	protected Object getMasterValue() {
		if(master instanceof Company){
			return master.get_id();
		}else if(master instanceof ICompanyRelative){
			return ((ICompanyRelative) master).getCompanyId();
		}
		return super.getMasterValue();
	}

}
