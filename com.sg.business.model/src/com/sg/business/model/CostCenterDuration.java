package com.sg.business.model;

import org.eclipse.jface.util.Util;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class CostCenterDuration implements IAccountPeriod {

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

	@Override
	public Double getAccountValue(String accountNumber) {
		loadData();
		return rndPeriodCost.getAccountValue(accountNumber);
	}

	private void loadData() {
		if (rndPeriodCost == null) {
			// 读取数据
			// 获得从SAP得到的该期数据镜像
			DBObject dbo = costCol.findOne(new BasicDBObject()
			.append(RNDPeriodCost.F_YEAR, year)
			.append(RNDPeriodCost.F_MONTH, month)
			.append(RNDPeriodCost.F_COSTCENTERCODE,
					organization.getCostCenterCode()));
			if(dbo!=null){
				rndPeriodCost = ModelService.createModelObject(dbo, RNDPeriodCost.class);
			}else{
				rndPeriodCost = null;
			}
		}
	}

	public void setYearDuration(int year) {
		this.year = year;
		rndPeriodCost = null;
	}

	public void setMonthDuration(int month) {
		this.month = month;
		rndPeriodCost = null;
	}

	public void setOrganization(Organization org) {
		if (Util.equals(organization, org)) {
			return;
		}
		this.organization = org;
		rndPeriodCost = null;
	}

	@Override
	public String toString() {
		if (organization == null) {
			return "";
		}
		return organization.getCostCenterCode();
	}

}
