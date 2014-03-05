package com.sg.sales.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.IWorkRelative;
import com.sg.business.model.Work;

public class WorkCost extends PrimaryObject implements IWorkRelative{

	public static final String F_CATAGORY = "catagory";
	public static final String F_COSTOWNERID = "costowner";
	public static final String F_FINISHDATE = "finishdate";
	public static final String F_STARTDATE = "startdate";

	@Override
	public AbstractWork getWork() {
		return ModelService.createModelObject(Work.class,
				(ObjectId) getValue(F_WORK_ID));
	}

	public String getCostOwnerId() {
		return (String) getValue(F_COSTOWNERID);
	}

	public Date getFinishDate() {
		return (Date) getValue(F_FINISHDATE);
	}
	public Date getStartDate() {
		return (Date) getValue(F_STARTDATE);
	}

}
