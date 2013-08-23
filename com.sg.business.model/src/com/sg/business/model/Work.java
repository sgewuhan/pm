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
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			Date d = (Date) getValue(F_PLAN_START);
			if (d != null) {
				return Utils.getDayBegin(d).getTime();
			} else {
				return null;
			}
		} else {
			Date start = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Date s = child.getPlanStart();
				if (s != null && (start == null || s.before(start))) {
					start = s;
				}
			}
			return start;
		}
	}

	public Date getPlanFinish() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			Date d = (Date) getValue(F_PLAN_FINISH);
			if (d != null) {
				return Utils.getDayEnd(d).getTime();
			} else {
				return null;
			}
		} else {
			Date finish = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Date f = child.getPlanFinish();
				if (f != null && (finish == null || f.after(finish))) {
					finish = f;
				}
			}
			return finish;
		}
	}

	public Date getActualStart() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			Date d = (Date) getValue(F_ACTUAL_START);
			if (d != null) {
				return Utils.getDayBegin(d).getTime();
			} else {
				return null;
			}
		} else {
			Date start = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Date s = child.getActualStart();
				if (s != null && (start == null || s.before(start))) {
					start = s;
				}
			}
			return start;
		}
	}

	public Date getActualFinish() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			Date d = (Date) getValue(F_ACTUAL_FINISH);
			if (d != null) {
				return Utils.getDayEnd(d).getTime();
			} else {
				return null;
			}
		} else {
			Date finish = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Date f = child.getActualFinish();
				if (f != null && (finish == null || f.after(finish))) {
					finish = f;
				}
			}
			return finish;
		}
	}

	public Double getActualWorks() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Double) getValue(F_ACTUAL_WORKS);
		}else{
			Double works = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Double w = child.getActualWorks();
				if(w!=null){
					if(works == null){
						works = w;
					}else{
						works = works + w;
					}
				}
			}
			return works;
		}
	}

	public Double getPlanWorks() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Double) getValue(F_PLAN_WORKS);
		}else{
			Double works = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Double w = child.getPlanWorks();
				if(w!=null){
					if(works == null){
						works = w;
					}else{
						works = works + w;
					}
				}
			}
			return works;
		}
	}

	public Integer getActualDuration() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Integer) getValue(F_ACTUAL_DURATION);
		}else{
			Integer duration = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Integer d = child.getActualDuration();
				if(d!=null){
					if(duration == null){
						duration = d;
					}else if(duration<d){
						duration = d;
					}
				}
			}
			return duration;
		}
	}

	public Integer getPlanDuration() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Integer) getValue(F_PLAN_DURATION);
		}else{
			Integer duration = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Integer d = child.getPlanDuration();
				if(d!=null){
					if(duration == null){
						duration = d;
					}else if(duration<d){
						duration = d;
					}
				}
			}
			return duration;
		}
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

		// Work parent = (Work) getParent();
		// if (parent != null) {
		// parent.doUpdateSummarySchedual(calendarCaculater, context);
		// }else{
		// doUpdateProjectSchedual(context);
		// }

		calendarCaculater = null;
		return true;

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

	public List<Work> getAllParents() {
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
