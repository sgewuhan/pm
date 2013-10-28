package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public abstract class AbstractWorksMetadata extends PrimaryObject implements
		IProjectRelative {

	public static final String F_WORKID = "workid";
	public static final String F_USERID = "userid";
	public static final String F_COMMITDATE = "commitdate";
	//完成工时
	public static final String F_WORKS = "works";
	public static final String F_DATECODE = "datecode";
	public static final String F_PROJECTDESC = "projectdesc";
	public static final String F_WORKDESC = "workdesc";
	public static final String F_PLANWORKS = "planworks";

	@Override
	public boolean doSave(IContext context) throws Exception {

		Object value = getValue(F_WORKS);
		if (!(value instanceof Double)) {
			throw new Exception("缺少实际工时数据");
		}

		value = getValue(F_USERID);
		if (!(value instanceof String)) {
			throw new Exception("缺少执行人");
		}

		Object workid = getValue(F_WORKID);
		if (!(workid instanceof ObjectId)) {
			throw new Exception("缺少目标工作");
		}

		Work work = ModelService.createModelObject(Work.class,
				(ObjectId) workid);
		Project project = work.getProject();

		String ls = work.getLifecycleStatus();
		if (!Work.STATUS_WIP_VALUE.equals(ls)) {
			throw new Exception("只能为进行中的工作提交工时记录");
		}

		value = getValue(F_DATECODE);
		Date dateValue = null;
		if (value instanceof Date) {
			dateValue = (Date) value;
			if (dateValue.after(new Date())) {
				throw new Exception("提交工时日期不能晚于当前日期");
			}

		} else if (!(value instanceof Long)) {
			dateValue = new Date();
		}else{
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(((Long)value).longValue()*24*60*60*1000);
			dateValue = cal.getTime();
		}

		Date as = work.getActualStart();
		if (dateValue.before(as)) {
			throw new Exception("提交工时日期不能早于工作的实际开始日期");
		}

		Long dateCode = new Long(dateValue.getTime() / (24 * 60 * 60 * 1000));
		setValue(F_DATECODE, dateCode);

		value = getValue(F_WORKDESC);
		if (value == null) {
			setValue(F_WORKDESC, work.getLabel());
		}

		value = getValue(F_PLANWORKS);
		if (value == null) {
			setValue(F_PLANWORKS, work.getPlanWorks());
		}

		value = getValue(F_PROJECT_ID);
		if (value == null) {
			setValue(F_PROJECT_ID, project.get_id());
		}

		value = getValue(F_PROJECTDESC);
		if (value == null) {
			setValue(F_PROJECTDESC, project.getLabel());
		}

		return super.doSave(context);
	}

	@Override
	public Project getProject() {
		Object projectId = getValue(F_PROJECT_ID);
		if (projectId instanceof ObjectId) {
			return ModelService.createModelObject(Project.class,
					(ObjectId) projectId);
		}
		return null;
	}

	@Override
	public String getLabel() {
		Object value = getValue(F_DATECODE);
		if (!(value instanceof Long)) {
			return "";
		}
		long millis = ((Long) value).longValue() * 24 * 60 * 60 * 1000;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		Date date = cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public Work getWork() {
		Object workid = getValue(F_WORKID);
		if (workid instanceof ObjectId) {
			Work work = ModelService.createModelObject(Work.class,
					(ObjectId) workid);
			return work;
		}
		return null;
	}

}
