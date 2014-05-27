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
		//master可能为独立工作定义或者项目模板，但无论是独立工作定义或项目模板，都使用ProjectTemplate.F_WORKTIMEPROGRAMS字段保存可用的工时方案
		return new BasicDBObject().append("$in", master.getValue(ProjectTemplate.F_WORKTIMEPROGRAMS));
		
	}

}
