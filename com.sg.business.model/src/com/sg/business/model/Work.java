package com.sg.business.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.drools.runtime.process.ProcessInstance;
import org.eclipse.core.runtime.Assert;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.runtime.Workflow;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.business.model.toolkit.MessageToolkit;
import com.sg.business.model.toolkit.ProjectToolkit;

/**
 * <p>
 * ����
 * <p/>
 * ��������������Ŀ�еĹ����ֽ�ṹ
 * 
 * @author jinxitao
 * 
 */
public class Work extends AbstractWork implements IProjectRelative, ISchedual,
		IProcessControlable, ILifecycle, IReferenceContainer {

	/**
	 * �����ı༭��ID
	 */
	public static final String EDITOR = "editor.work";

	/**
	 * �������õı༭��ID
	 */
	public static final String EDITOR_SETTING = "editor.work.setting";

	/**
	 * ����ģ�����ɾ�����������͵��ֶ�
	 */
	public static final String F_MANDATORY = "mandatory";

	/**
	 * �����˵�id userid
	 */
	public static final String F_CHARGER = "chargerid";

	/**
	 * ָ���ߵ�id
	 */
	public static final String F_ASSIGNER = "assignerid";

	/**
	 * �����е���
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * �����������ִ����
	 */
	public static final String F_WF_CHANGE_ACTORS = "wf_change_actors";

	/**
	 * ����ִ������ִ����
	 */
	public static final String F_WF_EXECUTE_ACTORS = "wf_execute_actors";

	/**
	 * �����¹������򵼱༭��
	 */
	public static final String EDITOR_LAUNCH = "editor.work.launch";

	/**
	 * ����
	 */
	public static final String F_DESCRIPTION = "description";

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
	 * �����¼�����
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

	/**
	 * ���ع���������Ŀ_id
	 * 
	 * @return
	 */
	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	/**
	 * ���ع����ƻ���ʼʱ��
	 * 
	 * @return Date
	 */
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

	/**
	 * ���ع����ƻ����ʱ��
	 * 
	 * @return Date
	 */
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

	/**
	 * ���ع���ʵ�ʿ�ʼʱ��
	 * 
	 * @return Date
	 */
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

	/**
	 * ���ع���ʵ�����ʱ��
	 * 
	 * @return Date
	 */
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

	/**
	 * ���ع�����ʵ�ʹ�ʱ
	 * 
	 * @return Double
	 */
	public Double getActualWorks() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Double) getValue(F_ACTUAL_WORKS);
		} else {
			Double works = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Double w = child.getActualWorks();
				if (w != null) {
					if (works == null) {
						works = w;
					} else {
						works = works + w;
					}
				}
			}
			return works;
		}
	}

	/**
	 * ���ع����ļƻ���ʱ
	 * 
	 * @return Double
	 */
	public Double getPlanWorks() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Double) getValue(F_PLAN_WORKS);
		} else {
			Double works = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Double w = child.getPlanWorks();
				if (w != null) {
					if (works == null) {
						works = w;
					} else {
						works = works + w;
					}
				}
			}
			return works;
		}
	}

	/**
	 * ���ع�����ʵ�ʹ���
	 * 
	 * @return Integer
	 */
	public Integer getActualDuration() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Integer) getValue(F_ACTUAL_DURATION);
		} else {
			Integer duration = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Integer d = child.getActualDuration();
				if (d != null) {
					if (duration == null) {
						duration = d;
					} else if (duration < d) {
						duration = d;
					}
				}
			}
			return duration;
		}
	}

	/**
	 * ���ع����ļƻ�����
	 * 
	 * @return Integer
	 */
	public Integer getPlanDuration() {
		List<PrimaryObject> children = getChildrenWork();
		if (children.size() == 0) {
			return (Integer) getValue(F_PLAN_DURATION);
		} else {
			Integer duration = null;
			for (int i = 0; i < children.size(); i++) {
				Work child = (Work) children.get(i);
				Integer d = child.getPlanDuration();
				if (d != null) {
					if (duration == null) {
						duration = d;
					} else if (duration < d) {
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
	 *            ,�ĵ�ģ�嶨��
	 * @return Deliverable
	 */
	public Deliverable makeDeliverableDefinition(DocumentDefinition docd) {
		DBObject data = new BasicDBObject();
		data.put(Deliverable.F_WORK_ID, get_id());

		data.put(Deliverable.F_PROJECT_ID, getValue(F_PROJECT_ID));

		if (docd != null) {
			data.put(Deliverable.F_DOCUMENT_DEFINITION_ID, docd.get_id());
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

	public boolean canCheck() {
		// δ��ɺ�δȡ����
		String lc = getLifecycleStatus();
		return (!STATUS_CANCELED_VALUE.equals(lc))
				&& (!STATUS_FINIHED_VALUE.equals(lc));
	}

	public boolean canCommit() {
		String lc = getLifecycleStatus();

		return STATUS_NONE_VALUE.equals(lc);
	}

	public boolean canStart() {
		String lc = getLifecycleStatus();
		return STATUS_ONREADY_VALUE.equals(lc)
				|| STATUS_PAUSED_VALUE.equals(lc);
	}

	public boolean canPause() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc);
	}

	public boolean canFinish() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc) || STATUS_PAUSED_VALUE.equals(lc);
	}

	public boolean canCancel() {
		String lc = getLifecycleStatus();
		return STATUS_WIP_VALUE.equals(lc) || STATUS_PAUSED_VALUE.equals(lc);
	}

	/**
	 * ���㹤��
	 * 
	 * @param fStart
	 *            ,��ʼ����
	 * @param fFinish
	 *            ,�������
	 * @param fDuration
	 *            ,����
	 * @throws Exception
	 */
	public void checkAndCalculateDuration(String fStart, String fFinish,
			String fDuration) throws Exception {
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
			Calendar sdate = Utils.getDayBegin(start);
			Calendar edate = Utils.getDayEnd(finish);
			long l = (edate.getTimeInMillis() - sdate.getTimeInMillis())
					/ (1000 * 60 * 60 * 24);
			setValue(fDuration, new Integer((int) l));
		}
	}

	/**
	 * ���������ϼ�����
	 * 
	 * @return List
	 */
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

	/**
	 * ��������ִ����
	 * 
	 * @param wfRoleAss
	 * @param roleAssign
	 * @return BasicDBObject
	 */
	private BasicDBObject getWorkFlowActors(DBObject wfRoleAss,
			Map<ObjectId, List<PrimaryObject>> roleAssign) {
		AbstractRoleAssignment assItem;
		List<PrimaryObject> assignments;
		String userid;
		BasicDBObject wfRoleActors = new BasicDBObject();

		Iterator<String> iter = wfRoleAss.keySet().iterator();
		while (iter.hasNext()) {
			String actionName = iter.next();
			ObjectId actorRoleId = (ObjectId) wfRoleAss.get(actionName);
			if (actorRoleId != null) {
				assignments = roleAssign.get(actorRoleId);
				if (assignments != null && !assignments.isEmpty()) {
					// String[] actorList = new String[assignments.size()];
					// for (int j = 0; j < assignments.size(); j++) {
					// assItem = (AbstractRoleAssignment) assignments.get(j);
					// userid = assItem.getUserid();
					// actorList[j] = userid;
					// }
					// wfRoleActors.put(actionName, actorList);

					// ֻ����ָ��һ����
					assItem = (AbstractRoleAssignment) assignments.get(0);
					userid = assItem.getUserid();
					wfRoleActors.put(actionName, userid);
				}
			}
		}
		return wfRoleActors;
	}

	/**
	 * ���ع����ĸ����˽�ɫ
	 * 
	 * @return ProjectRole
	 */
	public ProjectRole getChargerRoleDefinition() {
		return getChargerRoleDefinition(ProjectRole.class);
	}

	public List<PrimaryObject> getDeliverable() {
		return getRelationById(F__ID, Deliverable.F_WORK_ID, Deliverable.class);
	}

	public List<PrimaryObject> getDeliverableDocuments() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		List<PrimaryObject> d = getDeliverable();
		for (int i = 0; i < d.size(); i++) {
			Deliverable ditem = (Deliverable) d.get(i);
			Document doc = ditem.getDocument();
			if (doc != null) {
				result.add(doc);
			}
		}
		return result;
	}

	public List<ICheckListItem> checkPlan() {
		Project project = getProject();
		Map<ObjectId, List<PrimaryObject>> ram = project.getRoleAssignmentMap();
		return checkPlan(project, ram);
	}

	/**
	 * 1. ��鹤���ļƻ�ʱ�䣺����û���趨�ƻ���ʼ���ƻ���ɡ��ƻ���ʱ��Ҷ���� <br/>
	 * 2. ��鹤���ĸ����� ������û���趨�����ˣ�����û���趨ָ���ߵ�Ҷ���� <br/>
	 * 3. �����������趨 �����棬û��ָ������ִ���ߵĹ��� <br/>
	 * 4. �������� <br/>
	 * 4.1. ��鹤���Ƿ���н�������棬û�н������Ҷ���� 4.2. ��齻�����ĵ�û�е����ļ���Ϊģ�壺����
	 * 
	 * @param project
	 * 
	 * @param roleMap
	 * 
	 * @return
	 */
	public List<ICheckListItem> checkPlan(Project project,
			Map<ObjectId, List<PrimaryObject>> roleMap) {
		ArrayList<ICheckListItem> result = new ArrayList<ICheckListItem>();
		List<PrimaryObject> childrenWork = getChildrenWork();
		if (childrenWork.size() > 0) {// ������¼���ֱ�ӷ����¼��ļ����
			for (int i = 0; i < childrenWork.size(); i++) {
				Work childWork = (Work) childrenWork.get(i);
				result.addAll(childWork.checkPlan(project, roleMap));
			}
		} else {
			// ****************************************************************************************
			// 1 ��鹤���ļƻ���ʼ�ͼƻ����
			Object value = getPlanStart();
			boolean passed = true;
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤����������",
						"�����ļƻ���ʼû��ȷ��", "�����ύǰȷ����", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PLAN_START);
				result.add(checkItem);
				passed = false;
			}

			value = getPlanFinish();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤����������",
						"�����ļƻ����ʱ��û��ȷ��", "�����ύǰȷ����", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PLAN_START);
				result.add(checkItem);
				passed = false;
			}

			value = getPlanWorks();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤����������",
						"�����ļƻ���ʱû��ȷ��", "��ȷ���üƻ���ʱ��ʾ�ù��������㹤ʱ�����������ϣ�������������ύǰȷ����",
						ICheckListItem.WARRING);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PLAN_WORKS);
				result.add(checkItem);
				passed = false;
			}

			value = getDesc();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤����������",
						"��������Ϊ��", "�����ύǰȷ����", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_DESC);
				result.add(checkItem);
			}

			if (passed) {
				CheckListItem checkItem = new CheckListItem("��鹤����������");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}
			passed = true;

			// ****************************************************************************************
			// 2 ��鸺����
			value = getCharger();
			if (value == null) {
				CheckListItem checkItem = new CheckListItem("��鹤��ִ����",
						"����������Ϊ��", "�����ύǰȷ����", ICheckListItem.ERROR);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_CHARGER);
				result.add(checkItem);
				passed = false;
			}

			// 3.��������
			value = getParticipatesIdList();
			if (value == null || ((BasicBSONList) value).isEmpty()) {
				CheckListItem checkItem = new CheckListItem("��鹤��ִ����",
						"û����ӹ���������", "�����ύǰȷ����", ICheckListItem.WARRING);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setEditorId(Project.EDITOR_CREATE_PLAN);
				checkItem.setEditorPageId(Project.EDITOR_PAGE_WBS);
				checkItem.setKey(F_PARTICIPATE);
				result.add(checkItem);
				passed = false;
			}
			if (passed) {
				CheckListItem checkItem = new CheckListItem("��鹤��ִ����");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}
			passed = true;

			// 4.1 ��鹤����������� ������û��ָ�����̸�����
			String title = "��鹤���������";
			String process = F_WF_CHANGE;
			String editorId = Project.EDITOR_CREATE_PLAN;
			String pageId = Project.EDITOR_PAGE_WBS;
			passed = ProjectToolkit.checkProcessInternal(project, this, result,
					roleMap, title, process, editorId, pageId);
			if (passed) {
				CheckListItem checkItem = new CheckListItem(title);
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}

			// 4.2 �����Ŀ�ύ������ ������û��ָ�����̸�����
			title = "��鹤��ִ������";
			process = F_WF_EXECUTE;
			passed = ProjectToolkit.checkProcessInternal(project, this, result,
					roleMap, title, process, editorId, pageId);
			if (passed) {
				CheckListItem checkItem = new CheckListItem(title);
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}

			passed = true;
			// ��鹤��������
			List<PrimaryObject> docs = getDeliverableDocuments();
			if (docs.isEmpty()) {
				CheckListItem checkItem = new CheckListItem("��齻����",
						"�ù���û���趨������", "�ύǰ������趨�����ɹ���ִ�к���ִ����������ӣ������ύǰȷ����",
						ICheckListItem.WARRING);
				checkItem.setData(project);
				checkItem.setSelection(this);
				checkItem.setKey(F_PARTICIPATE);
				result.add(checkItem);
				passed = false;
			}
			if (passed) {
				CheckListItem checkItem = new CheckListItem("��齻����");
				checkItem.setData(project);
				checkItem.setSelection(this);
				result.add(checkItem);
			}

		}

		return result;
	}

	public User getCharger() {
		String chargerId = getChargerId();
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return User.getUserById(chargerId);
	}

	public String getChargerId() {
		return (String) getValue(F_CHARGER);
	}

	public String getAssignerId() {
		return (String) getValue(F_ASSIGNER);
	}

	public BasicBSONList getParticipatesIdList() {
		return (BasicBSONList) getValue(F_PARTICIPATE);
	}

	@Override
	public boolean isWorkflowActivate(String fieldKey) {
		return Boolean.TRUE.equals(getValue(fieldKey + POSTFIX_ACTIVATED));
	}

	@Override
	public DroolsProcessDefinition getProcessDefinition(String fieldKey) {
		DBObject processData = (DBObject) getValue(fieldKey);
		if (processData != null) {
			return new DroolsProcessDefinition(processData);
		}
		return null;
	}

	@Override
	public String getProcessActionActor(String key, String nodeActorParameter) {
		DBObject data = getProcessActorsMap(key);
		if (data == null) {
			return null;
		}
		return (String) data.get(nodeActorParameter);
	}

	public DBObject getProcessActorsMap(String key) {
		return (DBObject) getValue(key + POSTFIX_ACTORS);
	}

	public DBObject getProcessAssignmentRoleMap(String key) {
		return (DBObject) getValue(key + POSTFIX_ASSIGNMENT);
	}

	@Override
	public ProjectRole getProcessActionAssignment(String key,
			String nodeActorParameter) {
		// ȡ����ɫָ��
		DBObject data = getProcessAssignmentRoleMap(key);
		if (data == null) {
			return null;
		}
		ObjectId roleId = (ObjectId) data.get(nodeActorParameter);
		if (roleId != null) {
			return ModelService.createModelObject(ProjectRole.class, roleId);
		}
		return null;
	}

	@Override
	public String getLifecycleStatus() {
		String lc = (String) getValue(F_LIFECYCLE);
		if (lc == null) {
			return STATUS_NONE_VALUE;
		} else {
			return lc;
		}
	}

	@Override
	public String getLifecycleStatusText() {
		String lc = getLifecycleStatus();
		return LifecycleToolkit.getLifecycleStatusText(lc);
	}

	/**
	 * ���͵������ĸ����ˡ������ߡ�ָ���ߣ���Ϣ��Ҫ������������<br/>
	 * ������Ϣ�����Ǻϲ���Ϣ��������ͬһ�˵�ͬһ���͵���ϢӦ������Ϊһ��<br/>
	 * 
	 * @param messageList
	 *            , �������������Ϣ�б�������ͬ�û��Ŀ��Ժϲ�
	 * @return
	 */
	public Map<String, Message> getCommitMessage(
			Map<String, Message> messageList) {
		// 1. ȡ����������
		appendMessageForCharger(messageList);

		// 2. ȡ����ָ����
		appendMessageForAssigner(messageList);

		// 3. ��ȡ������
		appendMessageForParticipate(messageList);

		// 4. ��ȡ���̵�ִ����
		appendMessageForChangeWorkflowActor(messageList);

		appendMessageForExecuteWorkflowActor(messageList);

		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childwork = (Work) children.get(i);
			childwork.getCommitMessage(messageList);
		}
		return messageList;
	}

	public void appendMessageForCharger(Map<String, Message> messageList) {
		Message message;
		String userId = getChargerId();
		if (userId != null) {
			message = messageList.get(userId);
			if (message == null) {
				message = MessageToolkit.createProjectCommitMessage(userId);
				messageList.put(userId, message);
			}
			MessageToolkit.appendMessageContent(message, "������" + ": " + getLabel());
			message.appendTargets(this, EDITOR, Boolean.TRUE);
			messageList.put(userId, message);
		}
	}

	public void appendMessageForAssigner(Map<String, Message> messageList) {
		Message message;
		String userId;
		userId = getAssignerId();
		if (userId != null) {
			message = messageList.get(userId);
			if (message == null) {
				message = MessageToolkit.createProjectCommitMessage(userId);
				messageList.put(userId, message);
			}
			MessageToolkit.appendMessageContent(message, "Ϊ����ָ�ɸ����˺Ͳ����ߣ�����" + ": "
					+ getLabel());
			message.appendTargets(this, EDITOR, Boolean.TRUE);
			messageList.put(userId, message);
		}
	}

	public void appendMessageForParticipate(Map<String, Message> messageList) {
		Message message;
		String userId;
		List<?> userIdList = getParticipatesIdList();
		if (userIdList != null) {
			for (int i = 0; i < userIdList.size(); i++) {
				userId = (String) userIdList.get(i);
				message = messageList.get(userId);
				if (message == null) {
					message = MessageToolkit.createProjectCommitMessage(userId);
					messageList.put(userId, message);
				}
				MessageToolkit.appendMessageContent(message, "���빤��" + ": "
						+ getLabel());
				message.appendTargets(this, EDITOR, Boolean.TRUE);
			}
		}
	}

	public void appendMessageForExecuteWorkflowActor(
			Map<String, Message> messageList) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList, F_WF_EXECUTE,
				"ִ������");
	}

	public void appendMessageForChangeWorkflowActor(
			Map<String, Message> messageList) {
		MessageToolkit.appendWorkflowActorMessage(this, messageList, F_WF_CHANGE,
				"�������");
	}

	/**
	 * �󶨹���������
	 * 
	 * @param workflowKey
	 *            ,�������ؼ��֣�ִ�����̻����Ǳ������ {@link #F_WF_EXECUTE} {@link #F_WF_CHANGE}
	 * @param workflowDefinition
	 */
	public void bindingWorkflowDefinition(String workflowKey,
			DBObject workflowDefinition) {
		setValue(workflowKey, workflowDefinition.get("KEY"));
		setValue(workflowKey + POSTFIX_ACTIVATED,
				workflowDefinition.get(POSTFIX_ACTIVATED));
		setValue(workflowKey + POSTFIX_ACTORS,
				workflowDefinition.get(POSTFIX_ACTORS));
		setValue(workflowKey + POSTFIX_ASSIGNMENT,
				workflowDefinition.get(POSTFIX_ASSIGNMENT));
	}

	/**
	 * ���湤��
	 * 
	 * @return boolean
	 */
	@Override
	public boolean doSave(IContext context) throws Exception {
		// if (calendarCaculater == null) {
		// calendarCaculater = new CalendarCaculater(getProjectId());
		// }

		checkAndCalculateDuration(F_PLAN_START, F_PLAN_FINISH, F_PLAN_DURATION);
		checkAndCalculateDuration(F_ACTUAL_START, F_ACTUAL_FINISH,
				F_ACTUAL_DURATION);

		// ͬ�������ˡ����̻ִ���˵������Ĳ����ߡ�
		ensureParticipatesConsistency();

		super.doSave(context);

		// Work parent = (Work) getParent();
		// if (parent != null) {
		// parent.doUpdateSummarySchedual(calendarCaculater, context);
		// }else{
		// doUpdateProjectSchedual(context);
		// }

		return true;

	}

	/**
	 * ȷ�������Ĳ����߰��������ĸ����ˡ�����ִ����
	 */
	public void ensureParticipatesConsistency() {
		// ��ȡ�����ĸ�����
		String chargerId = getChargerId();
		addParticipate(chargerId);

		// ������̵�ִ����
		if (isWorkflowActivate(F_WF_EXECUTE)) {
			DBObject processActorsMap = getProcessActorsMap(F_WF_EXECUTE);
			Iterator<String> iter = processActorsMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				String userId = (String) processActorsMap.get(key);
				addParticipate(userId);
			}
		}

	}

	public void addParticipate(String chargerId) {
		BasicBSONList participatesIdList = getParticipatesIdList();
		if (participatesIdList == null) {
			participatesIdList = new BasicDBList();
			setValue(F_PARTICIPATE, participatesIdList);
		}
		if (!participatesIdList.contains(chargerId)) {
			participatesIdList.add(chargerId);
		}
	}

	/**
	 * Ϊ�������¼������ĸ�����,������,��������ִ����ָ���û�
	 * 
	 * @param roleAssign
	 * @param context
	 * @throws Exception
	 */
	public void doAssignment(Map<ObjectId, List<PrimaryObject>> roleAssign,
			IContext context) throws Exception {
		AbstractRoleAssignment assItem;
		List<PrimaryObject> assignments;
		String userid;
		boolean modified = false;

		// ���ø�����
		ObjectId roleId = (ObjectId) getValue(F_CHARGER_ROLE_ID);
		if (roleId != null) {
			assignments = roleAssign.get(roleId);
			if (assignments != null && !assignments.isEmpty()) {
				assItem = (AbstractRoleAssignment) assignments.get(0);
				userid = assItem.getUserid();
				setValue(F_CHARGER, userid);
				modified = true;
			}
		}

		// ����ָ����
		roleId = (ObjectId) getValue(F_ASSIGNMENT_CHARGER_ROLE_ID);
		if (roleId != null) {
			assignments = roleAssign.get(roleId);
			if (assignments != null && !assignments.isEmpty()) {
				assItem = (AbstractRoleAssignment) assignments.get(0);
				userid = assItem.getUserid();
				setValue(F_ASSIGNER, userid);
				modified = true;
			}
		}

		// ���ò�����
		BasicBSONList roleIds = (BasicBSONList) getValue(F_PARTICIPATE_ROLE_SET);
		if (roleIds != null && roleIds.size() > 0) {
			List<String> participates = new ArrayList<String>();
			for (int i = 0; i < roleIds.size(); i++) {
				DBObject object = (DBObject) roleIds.get(i);
				assignments = roleAssign.get(object.get(F__ID));
				if (assignments != null && !assignments.isEmpty()) {
					for (int j = 0; j < assignments.size(); j++) {
						assItem = (AbstractRoleAssignment) assignments.get(j);
						userid = assItem.getUserid();
						participates.add(userid);
					}
				}
			}
			if (participates.size() > 0) {
				setValue(F_PARTICIPATE, participates);
				modified = true;
			}
		}

		// ���ñ��������ִ����

		DBObject wfRoleAss = (DBObject) getValue(F_WF_CHANGE_ASSIGNMENT);
		if (wfRoleAss != null) {
			BasicDBObject wfRoleActors = getWorkFlowActors(wfRoleAss,
					roleAssign);
			if (!wfRoleActors.isEmpty()) {
				setValue(F_WF_CHANGE_ACTORS, wfRoleActors);
				modified = true;
			}
		}

		// ����ִ�й�������ִ����
		wfRoleAss = (DBObject) getValue(F_WF_EXECUTE_ASSIGNMENT);
		if (wfRoleAss != null) {
			BasicDBObject wfRoleActors = getWorkFlowActors(wfRoleAss,
					roleAssign);
			if (!wfRoleActors.isEmpty()) {
				setValue(F_WF_EXECUTE_ACTORS, wfRoleActors);
				modified = true;
			}
		}
		if (modified) {
			doSave(context);
		}

		// �����¼�
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work child = (Work) children.get(i);
			child.doAssignment(roleAssign, context);
		}
	}

	/**
	 * �½�������
	 * 
	 * @param doc
	 *            ,�ĵ�
	 * @param context
	 * @return Deliverable
	 * @throws Exception
	 */
	public Deliverable doAddDeliverable(Document doc, IContext context)
			throws Exception {
		Deliverable deli = makeDeliverableDefinition();
		deli.setValue(Deliverable.F_DOCUMENT_ID, doc.get_id());
		deli.doInsert(context);
		return deli;

	}

	public void doCancel(IContext context) throws Exception {
		Assert.isTrue(canCancel(), "�����ĵ�ǰ״̬����ִ��ȡ������");
		Map<String, Object> params = new HashMap<String,Object>();

		doCancelBefore(context, params);

		// TODO Auto-generated method stub

		doCancelAfter(context, params);
	}

	public void doFinish(IContext context) throws Exception {
		Assert.isTrue(canFinish(), "�����ĵ�ǰ״̬����ִ����ɲ���");
		Map<String, Object> params = new HashMap<String,Object>();

		doFinishBefore(context, params);

		// TODO Auto-generated method stub

		doFinishAfter(context, params);

	}

	public void doPause(IContext context) throws Exception {
		Assert.isTrue(canPause(), "�����ĵ�ǰ״̬����ִ����ͣ����");
		Map<String, Object> params = new HashMap<String,Object>();

		doPauseBefore(context, params);

		// TODO Auto-generated method stub

		doPauseAfter(context, params);
	}

	/**
	 * ��������
	 */
	public void doStart(IContext context) throws Exception {
		// �ж��ܷ����������״̬
		Assert.isTrue(canStart(), "�����ĵ�ǰ״̬����ִ����������");

		Map<String, Object> params = new HashMap<String,Object>();
		//����ǰ����
		doStartBefore(context,params);

		// �ж��Ƿ�ʹ��ִ�й�����
		if (isWorkflowActivate(F_WF_EXECUTE)) {
			// ����ǣ�����������
			Workflow wf = getWorkflow(F_WF_EXECUTE);
			ProcessInstance processInstance = wf.startHumanProcess(params);
			Assert.isNotNull(processInstance, "��������ʧ��");

			setValue(F_WF_EXECUTE+POSTFIX_INSTANCEID, processInstance.getId());
		}

		// �����¼�ͬ�������Ĺ���
		List<PrimaryObject> children = getChildrenWork();
		for (int i = 0; i < children.size(); i++) {
			Work childWork = (Work) children.get(i);
			//����¼��Ĺ����Ƿ����������ϼ�ͬ������
			if (Boolean.TRUE.equals(childWork
					.getValue(F_SETTING_AUTOSTART_WHEN_PARENT_START))) {
				//�����¼�����
				childWork.doStart(context);
			}
		}

		// ��ǹ����Ľ�����
		setValue(F_LIFECYCLE, STATUS_WIP_VALUE);
		
		// ���ù�����ʵ�ʿ�ʼʱ��
		setValue(F_ACTUAL_START, new Date());
		
		// ����
		doSave(context);

		// ��ʾ��������
		doWorkActionNotice(context, "��������");

		//���ú���
		doStartAfter(context,params);
	}

	public Workflow getWorkflow(String key) {
		DBObject actors = getProcessActorsMap(key);
		@SuppressWarnings("unchecked")
		Map<String,String> actorParameter = actors.toMap();
		DroolsProcessDefinition processDefintion = getProcessDefinition(key);
		Workflow wf = new Workflow(processDefintion,actorParameter,this);
		return wf;
	}

	/**
	 * ���͹���������ɵ�֪ͨ
	 * @param context ��ǰ��������
	 * @param actionName �������ı�����
	 * @throws Exception ������Ϣ���ֵĴ���
	 */
	public void doWorkActionNotice(IContext context, String actionName)
			throws Exception {
		Message message = ModelService.createModelObject(Message.class);
		//�����ռ���
		BasicBSONList participatesIdList = getParticipatesIdList();
		Assert.isTrue(participatesIdList != null
				&& participatesIdList.size() > 0, "�����Ĳ�����Ϊ��");
		message.setValue(Message.F_RECIEVER, participatesIdList);
		
		//����֪ͨ����
		message.setValue(Message.F_DESC, actionName + "֪ͨ");
		message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);

		//���÷�����
		String userId = context.getAccountInfo().getUserId();
		String userName = context.getAccountInfo().getUserName();
		message.setValue(Message.F_SENDER, userId);
	
		//���÷���ʱ��
		message.setValue(Message.F_SENDDATE, new Date());
		
		//����֪ͨ����
		String content = "<span style='font-size:14px'>" + "����: "
				+ "</span><br/><br/>" + userName + "|" + userId + "ִ����"
				+ actionName + getLabel() + "<br/>���Ǹù����Ĳ����ߣ���֪����<br/><br/>";
		message.setValue(Message.F_CONTENT, content);
		MessageToolkit.appendEndMessage(message);
		
		//���õ�������
		message.appendTargets(this, EDITOR, false);

	}


	@Override
	public BasicBSONList getTargetList() {
		return (BasicBSONList) getValue(F_TARGETS);
	}

	/**
	 * �������ĳ���ִ���߶�Ӧ���û�
	 * 
	 * @param nodeActor
	 * @return
	 */
	public List<User> getPermittedUserOfWorkflowActor(String processKey,
			String nodeActor) {
		DBObject processRoleDefinitions = getProcessAssignmentRoleMap(processKey);
		ObjectId _id = (ObjectId) processRoleDefinitions.get(nodeActor);

		List<PrimaryObject> ralist;
		if (getProjectId() != null) {
			ProjectRole roled = ModelService.createModelObject(
					ProjectRole.class, _id);
			ralist = roled.getAssignment();

		} else {
			Role role = ModelService.createModelObject(Role.class, _id);
			ralist = role.getAssignment();
		}
		List<User> result = new ArrayList<User>();
		if (ralist != null) {
			for (int i = 0; i < ralist.size(); i++) {
				AbstractRoleAssignment ra = (AbstractRoleAssignment) ralist
						.get(i);
				String userid = ra.getUserid();
				User user = User.getUserById(userid);
				result.add(user);
			}
		}

		return result;
	}

	
	private void doStartAfter(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "start.after",params);
	}

	private void doStartBefore(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "start.before",params);
	}

	private void doPauseAfter(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "pause.after",params);

	}

	private void doPauseBefore(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "pause.before",params);

	}

	private void doFinishAfter(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "finish.after",params);

	}

	private void doFinishBefore(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "finish.before",params);

	}

	private void doCancelAfter(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "cancel.after",params);

	}

	private void doCancelBefore(IContext context,Map<String, Object> params) throws Exception {
		ModelActivator.executeEvent(this, "cancel.before",params);

	}
}
