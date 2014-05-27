package com.sg.business.model.dataset;


import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WorkTimeProgramOfProjectTemplate extends
		MasterDetailDataSetFactory {

	public WorkTimeProgramOfProjectTemplate() {
		super(IModelConstants.DB, IModelConstants.C_WORKTIMEPROGRAM);
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkTimeProgram.F__ID;
	}
	
	@Override
	protected Object getMasterValue() {
		//master����Ϊ�����������������Ŀģ�壬�������Ƕ��������������Ŀģ�壬��ʹ��ProjectTemplate.F_WORKTIMEPROGRAMS�ֶα�����õĹ�ʱ����
		return new BasicDBObject().append("$in", master.getValue(ProjectTemplate.F_WORKTIMEPROGRAMS));
		
	}

}
