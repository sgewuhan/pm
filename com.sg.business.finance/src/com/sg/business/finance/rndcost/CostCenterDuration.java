package com.sg.business.finance.rndcost;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.commons.eai.RNDPeriodCostAdapter;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.RNDPeriodCost;

public class CostCenterDuration implements IStructuredContentProvider {

	private Organization organization;

	private Integer year;

	private Integer month;

	private DBCollection costCol;

	private RNDPeriodCost rndPeriodCost;

	public CostCenterDuration(Organization organization) {
		this.organization = organization;
	}

	public CostCenterDuration() {
		costCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_COSTCENTER);

	}

	public void load() {
		if (organization != null) {
			// 读取数据
			// 获得从SAP得到的该期数据镜像
			DBObject dbo = null;
			dbo = costCol.findOne(new BasicDBObject()
					.append(RNDPeriodCost.F_YEAR, year)
					.append(RNDPeriodCost.F_MONTH, month)
					.append(RNDPeriodCost.F_COSTCENTERCODE,
							organization.getCostCenterCode()));
			if (dbo != null) {
				rndPeriodCost = ModelService.createModelObject(dbo,
						RNDPeriodCost.class);
			} else {
				String costCenterCode = organization.getCostCenterCode();
				rndPeriodCost = readRNDPeriodCost(costCenterCode);
				rndPeriodCost = ModelService.createModelObject(
						new BasicDBObject(), RNDPeriodCost.class);
				rndPeriodCost.setValue(RNDPeriodCost.F_COSTCENTERCODE,
						organization.getCostCenterCode());
			}
		} else {
			rndPeriodCost = ModelService.createModelObject(new BasicDBObject(),
					RNDPeriodCost.class);
		}
	}

	private RNDPeriodCost readRNDPeriodCost(String costCenterCode) {
		RNDPeriodCostAdapter adapter = new RNDPeriodCostAdapter();
		
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		
		parameter.put(RNDPeriodCostAdapter.YEAR, year);
		parameter.put(RNDPeriodCostAdapter.MONTH, month);
		parameter.put(RNDPeriodCostAdapter.ORGCODE, organization.getCompanyCode());
		parameter.put(RNDPeriodCostAdapter.COSECENTERCODE, organization.getCostCenterCode());
		return adapter.getData(parameter);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (Util.equals(oldInput, newInput)) {
			return;
		}
		if (newInput instanceof CostCenterDurationQueryParameter) {
			CostCenterDurationQueryParameter p = (CostCenterDurationQueryParameter) newInput;
			month = p.month;
			year = p.year;
			organization = p.organization;
			load();
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return new Object[] { rndPeriodCost };
	}

}
