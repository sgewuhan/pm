package com.sg.sales.ui.block;

import org.eclipse.swt.widgets.Composite;

import com.sg.sales.model.Company;
import com.sg.widgets.block.BusinessContentBlock;

public class CompanyContentBlock extends BusinessContentBlock {

	public CompanyContentBlock(Composite parent) {
		super(parent);
	}
	
	
	@Override
	protected String getFootText(boolean hoverMask) {
		Company company = (Company) getInput();
		return "客户级别:"+company.getLevel();
	}

}
