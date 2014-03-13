package com.sg.sales.ui.block;

import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.Company;
import com.sg.sales.model.dataset.MyCustomerDataSet;
import com.sg.widgets.block.button.BusinessContentBlock;
import com.sg.widgets.block.button.ButtonBlock;

public class CompanyBlock extends ButtonBlock {

	private static final String PERSPECTIVE = "sales.customer";
	private MyCustomerDataSet dataset;

	public CompanyBlock(Composite parent) {
		super(parent);
	}
	
	@Override
	protected void init() {
		dataset = new com.sg.sales.model.dataset.MyCustomerDataSet();
		super.init();
	}


	@Override
	protected String getPerspective() {
		return PERSPECTIVE;
	}

	@Override
	protected DBCollection getCollection() {
		return DBActivator.getCollection(IModelConstants.DB,
				Sales.C_COMPANY);
	}

	@Override
	protected void doAdd() {
		Company po = ModelService.createModelObject(new BasicDBObject(),
				Company.class);
		UIFrameworkUtils.navigateTo(po, UIFrameworkUtils.NAVIGATE_BY_EDITOR,true);
	}

	@Override
	protected Class<? extends PrimaryObject> getContentClass() {
		return Company.class;
	}

	@Override
	protected DBObject getSearchCondition() {
		return dataset.getQueryCondition();
	}

	@Override
	protected BusinessContentBlock createBlockContent(Composite contentArea,
			PrimaryObject po) {
		return new CompanyContentBlock(contentArea);
	}

}
