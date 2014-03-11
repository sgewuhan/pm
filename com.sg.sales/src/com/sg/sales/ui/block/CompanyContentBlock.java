package com.sg.sales.ui.block;

import org.eclipse.swt.widgets.Composite;

import com.sg.sales.model.Company;
import com.sg.widgets.block.BusinessContentBlock;

public class CompanyContentBlock extends BusinessContentBlock {

	public CompanyContentBlock(Composite parent) {
		super(parent);
	}
	
	@Override
	protected String getBodyText(boolean hoverMask) {
		Company company = (Company) getInput();
		return company.getLevel();
	}

}
