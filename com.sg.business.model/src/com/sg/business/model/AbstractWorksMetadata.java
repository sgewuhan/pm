package com.sg.business.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public abstract class AbstractWorksMetadata extends PrimaryObject implements IProjectRelative{

	public static final String F_WORKID = "workid";
	public static final String F_USERID = "userid";
	public static final String F_COMMITDATE = "commitdate";
	public static final String F_WORKS = "works";
	public static final String F_DATECODE = "datecode";
	public static final String F_PROJECTDESC = "projectdesc";
	public static final String F_WORKDESC = "workdesc";
	public static final String F_PLANWORKS = "planworks";
	
	@Override
	public boolean doSave(IContext context) throws Exception {
		
		Object value = getValue(F_WORKS);
		if(!(value instanceof Double)){
			throw new Exception("缺少实际工时数据");
		}
		
		value = getValue(F_USERID);
		if(!(value instanceof String)){
			throw new Exception("缺少执行人");
		}
		
		Object workid = getValue(F_WORKID);
		if(!(workid instanceof ObjectId)){
			throw new Exception("缺少目标工作");
		}
		
		Work work = ModelService.createModelObject(Work.class, (ObjectId)workid);
		Project project = work.getProject();
		
		value = getValue(F_WORKDESC);
		if(value == null){
			setValue(F_WORKDESC, work.getLabel());
		}
		
		value = getValue(F_PLANWORKS);
		if(value == null){
			setValue(F_PLANWORKS, work.getPlanWorks());
		}
		
		value = getValue(F_PROJECT_ID);
		if(value == null){
			setValue(F_PROJECT_ID, project.get_id());
		}
		
		value = getValue(F_PROJECTDESC);
		if(value == null){
			setValue(F_PROJECTDESC,project.getLabel());
		}
		
		value = getValue(F_DATECODE);
		if(value instanceof Date){
			Long dateCode = new Long(((Date)value).getTime()/(24*60*60*1000));
			setValue(F_DATECODE, dateCode);
		}else if(!(value instanceof Long)){
			Date date = new Date();
			Long dateCode = new Long(date.getTime()/(24*60*60*1000));
			setValue(F_DATECODE, dateCode);
		}
		
		
		
		
		
		return super.doSave(context);
	}
	
	@Override
	public Project getProject() {
		Object projectId = getValue(F_PROJECT_ID);
		if(projectId instanceof ObjectId){
			return ModelService.createModelObject(Project.class, (ObjectId)projectId);
		}
		return null;
	}
	
}
