package com.sg.sales.model;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public class POItem extends PrimaryObject {

	public static final String F_CONTRACT_ID = "contract_id";

	public double getSummary() {
		Double price = getDoubleValue("unitprice");
		Double qty = getDoubleValue("qty");
		if (price == null || qty == null) {
			return 0d;
		}
		return price.doubleValue() * qty.doubleValue();
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		boolean b = super.doSave(context);
		if (b) {
			// 更新合同总金额
			Contract contract = getContract();
			Assert.isNotNull(contract);
			contract.doCalculateAmountSummary();
		}
		return b;
	}

	public Contract getContract() {
		ObjectId id = (ObjectId) getValue(F_CONTRACT_ID);
		if (id == null) {
			return null;
		}
		return ModelService.createModelObject(Contract.class, id);
	}
}
