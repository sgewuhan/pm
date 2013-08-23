package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;

/**
 * <p>
 * ����
 * <p/>
 * ��������������Ŀ�еĹ����ֽ�ṹ
 * 
 * @author jinxitao
 * 
 */
public class Work extends AbstractWork implements IProjectRelative, ISchedual {

	/**
	 * �����ı༭��ID
	 */
	public static final String EDITOR = "editor.work";

	/**
	 * ����ģ�����ɾ�����������͵��ֶ�
	 */
	public static final String F_MANDATORY = "mandatory";

	private CalendarCaculater calendarCaculater;

	/**
	 * ���ع���������Ŀ
	 * 
	 * @return Project
	 */
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}

	/**
	 * �½��¼�����
	 * 
	 * @return Work
	 */
	@Override
	public Work makeChildWork() {
		DBObject data = new BasicDBObject();
		data.put(F_PARENT_ID, get_id());
		data.put(F_ROOT_ID, getValue(F_ROOT_ID));

		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq + 1));

		data.put(F_PROJECT_ID, getValue(F_PROJECT_ID));

		Work po = ModelService.createModelObject(data, Work.class);
		return po;
	}

	/**
	 * ���ع���������Ŀ
	 * 
	 * @return PrimaryObject
	 */
	@Override
	public PrimaryObject getHoster() {
		return getProject();
	}

	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	public Date getPlanStart() {
		Date d = (Date) getValue(F_PLAN_START);
		if (d != null) {
			return Utils.getDayBegin(d).getTime();
		}
		return null;
	}

	public Date getPlanFinish() {
		Date d = (Date) getValue(F_PLAN_FINISH);
		if (d != null) {
			return Utils.getDayEnd(d).getTime();
		}
		return null;
	}

	public Date getActualStart() {
		Date d = (Date) getValue(F_ACTUAL_START);
		if (d != null) {
			return Utils.getDayBegin(d).getTime();
		}
		return null;
	}

	public Date getActualFinish() {
		Date d = (Date) getValue(F_ACTUAL_FINISH);
		if (d != null) {
			return Utils.getDayEnd(d).getTime();
		}
		return null;
	}


	/**
	 * �½�����������
	 * 
	 * @return Deliverable
	 */
	@Override
	public Deliverable makeDeliverableDefinition() {
		return makeDeliverableDefinition(null);
	}

	/**
	 * �½�����������
	 * 
	 * @param docd
	 *            ,�������ĵ�����
	 * @return
	 */
	public Deliverable makeDeliverableDefinition(DocumentDefinition docd) {
		DBObject data = new BasicDBObject();
		data.put(Deliverable.F_WORK_ID, get_id());

		data.put(Deliverable.F_PROJECT_ID, getValue(F_PROJECT_ID));

		if (docd != null) {
			data.put(Deliverable.F_DOCUMENT_ID, docd.get_id());
			data.put(Deliverable.F_DESC, docd.getDesc());
		}

		Deliverable po = ModelService
				.createModelObject(data, Deliverable.class);

		return po;
	}

	/**
	 * ������������
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "����";
	}

	/**
	 * ���ع���Ĭ�ϱ༭��ID
	 * 
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}

	/**
	 * �жϹ����������Ƿ�ֻ��
	 * 
	 * @param column
	 *            ,����������
	 * @param context
	 * @return boolean
	 */
	public boolean canEdit(String column, IContext context) {
		return true;
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		if (calendarCaculater == null) {
			calendarCaculater = new CalendarCaculater(getProjectId());
		}

		checkAndCalculateDuration(calendarCaculater, F_PLAN_START,
				F_PLAN_FINISH, F_PLAN_DURATION);
		checkAndCalculateDuration(calendarCaculater, F_ACTUAL_START,
				F_ACTUAL_FINISH, F_ACTUAL_DURATION);
	
		super.doSave(context);
		
		Work parent = (Work) getParent();
		if (parent != null) {
			parent.doUpdateSummarySchedual(calendarCaculater, context);
		}else{
			doUpdateProjectSchedual(context);
		}
		
		calendarCaculater = null;
		return true;

	}

	private void doUpdateProjectSchedual(IContext context) throws Exception {
		Project project = getProject();
		Object value = getPlanStart();
		if(value!=null){
			project.setValue(Project.F_PLAN_START, value);
		}
		
		value = getPlanFinish();
		if(value!=null){
			project.setValue(Project.F_PLAN_FINISH, value);
		}

		value = getValue(F_PLAN_DURATION);
		if(value!=null){
			project.setValue(Project.F_PLAN_DURATION, value);
		}
		
		value = getActualStart();
		if(value!=null){
			project.setValue(Project.F_ACTUAL_START, value);
		}
		
		value = getActualFinish();
		if(value!=null){
			project.setValue(Project.F_ACTUAL_FINISH, value);
		}
		
		value = getValue(F_ACTUAL_DURATION);
		if(value!=null){
			project.setValue(Project.F_ACTUAL_DURATION,value );
		}

		project.doSave(context);
		
	}

	private void doUpdateSummarySchedual(CalendarCaculater calendarCaculater,
			IContext context) throws Exception {
		Date start = null;
		Date finish = null;
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work child = (Work) children.get(i);
			Date s = child.getPlanStart();
			Date f = child.getPlanFinish();
			if (s!=null&&(start == null || s.before(start))) {
				start = s;
			}
			if (f!=null&&(finish == null || f.after(finish))) {
				finish = f;
			}
		}
		this.calendarCaculater = calendarCaculater;
		
		Date os = getPlanStart();
		Date of = getPlanFinish();
		
		if(os.equals(start)&&of.equals(finish)){
			return;
		}
		
		setValue(F_PLAN_START, start);
		setValue(F_PLAN_FINISH, finish);
		doSave(context);
	}

	public void checkAndCalculateDuration(CalendarCaculater cc, String fStart,
			String fFinish, String fDuration) throws Exception {
		Date start = (Date) getValue(fStart);
		if (start != null) {
			start = Utils.getDayBegin(start).getTime();
		}

		Date finish = (Date) getValue(fFinish);
		if (finish != null) {
			finish = Utils.getDayEnd(finish).getTime();
		}

		if (start != null && finish != null) {
			// ����Ƿ�Ϸ�
			if (start.after(finish)) {
				throw new Exception("��ʼ���ڱ��������������");
			}
			// ���㹤��
			int workingdays = cc.getWorkingDays(start, finish);
			setValue(fDuration, new Integer(workingdays));
		}
	}

	public List<Work> getThisAndAllParents() {
		List<Work> result = new ArrayList<Work>();
		result.add(this);
		Work parent = (Work) getParent();
		while (parent != null) {
			result.add(parent);
			parent = (Work) parent.getParent();
		}
		return result;
	}
}
