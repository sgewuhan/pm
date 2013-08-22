package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * <p>����<p/>
 * ��������������Ŀ�еĹ����ֽ�ṹ
 * @author jinxitao
 *
 */
public class Work extends AbstractWork implements IProjectRelative,ISchedual {

	/**
	 * �����ı༭��ID
	 */
	public static final String EDITOR = "editor.work";

	/**
	 * ����ģ�����ɾ�����������͵��ֶ�
	 */
	public static final String F_MANDATORY = "mandatory";

	/**
	 * ���ع���������Ŀ
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
	 * @return PrimaryObject
	 */
	@Override
	public PrimaryObject getHoster() {
		return getProject();
	}

	/**
	 * �½�����������
	 * @return Deliverable
	 */
	@Override
	public Deliverable makeDeliverableDefinition() {
		return makeDeliverableDefinition(null);
	}

	/**
	 * �½�����������
	 * @param docd
	 *         ,�������ĵ�����
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
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "����";
	}
	
	/**
	 * ���ع���Ĭ�ϱ༭��ID
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}

	/**
	 * �жϹ����������Ƿ�ֻ��
	 * @param column
	 *          ,����������
	 * @param context
	 * @return boolean
	 */
	public boolean canEdit(String column,
			IContext context) {
		return true;
	}

	/**
	 * ���¹���
	 * @param key
	 *        ,�������ݵ�KEY
	 * @param value
	 *        ,���ĺ��Value
	 * @param context
	 * @return Work[]
	 */
	public Work[] doUpdateSchedual(String key, Object value,
			IContext context) {
		setValue(key, value);
		return new Work[]{this};
	}
}
