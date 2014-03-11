package com.sg.sales.model;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public class Income extends PrimaryObject {

	public static final String F_CONTRACT_ID = "contract_id";
	public static final String F_AMOUNT = "amount";

	@Override
	public boolean doSave(IContext context) throws Exception {
		boolean b = super.doSave(context);
		if (b) {
			// 更新合同总金额
			Contract contract = getContract();
			Assert.isNotNull(contract);
			contract.doCalculateIncomeSummary();
		}
		return b;
	}
	
	public Contract getContract() {
		ObjectId id = (ObjectId) getValue(F_CONTRACT_ID);
		if(id == null){
			return null;
		}
		return ModelService.createModelObject(Contract.class, id);
	}

	public double getAmount() {
		return getDoubleValue(F_AMOUNT);
	}
}
