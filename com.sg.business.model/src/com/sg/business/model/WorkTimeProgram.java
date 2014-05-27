package com.sg.business.model;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class WorkTimeProgram extends PrimaryObject {

	/**
	 * ������
	 */
	public static final String F_COLUMNTYPES = "columntypes";
	/**
	 * ��ʱ����
	 */
	public static final String F_WORKTIMETYPES = "worktimetypes";
	/**
	 * ��ʱ����
	 */
	public static final String F_WORKTIMEDATA = "worktimedata";
	/**
	 * ˵��
	 */
	public static final String F_DESCRIPTION = "description";
	/**
	 * �Ƿ�����
	 */
	public static final String F_ACTIVATED = "activated";

	/**
	 * ������֯ID
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$

	/**
	 * ����ѡ�����ColumnType �Ӽ�¼���ֶΣ�DBObject����
	 */
	public static final String F_WORKTIME_TYPE_OPTIONS = "options";

	/**
	 * ��ʱ���ݵ�������ѡ��id
	 */
	public static final String F_WORKTIMEDATA_COLUMNTYPEOPTION_ID = "columntypeoption_id";

	/**
	 * ��ʱ���ݵĹ�ʱ����ѡ��id
	 */
	public static final String F_WORKTIMEDATA_WORKTIMETYPEOPTION_ID = "worktimetypeoption_id";

	/**
	 * ��ʱ���ݵĹ�ʱ
	 */
	public static final String F_WORKTIMEDATA_AMOUNT = "amount";

	public Double getWorkTimeData(ObjectId workTimeTypeOption,
			ObjectId columnTypeOption) {
		// 1.ȡ����ʱ����
		BasicBSONList workTimeData = (BasicBSONList) this
				.getValue(F_WORKTIMEDATA);
		if (workTimeData == null) {
			return null;
		}
		for (int i = 0; i < workTimeData.size(); i++) {
			DBObject workTime = (DBObject) workTimeData.get(i);
			ObjectId columnTypeOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_COLUMNTYPEOPTION_ID);
			ObjectId workTimeTypeOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_WORKTIMETYPEOPTION_ID);
			if (columnTypeOption.equals(columnTypeOptionId)
					&& workTimeTypeOption.equals(workTimeTypeOptionId)) {
				return (Double) workTime.get(F_WORKTIMEDATA_AMOUNT);
			}
		}
		return null;
	}

	public void makeWorkTimeData(ObjectId workTimeTypeOptionId,
			ObjectId columnTypeOptionId, Double value) {
		// 1.ȡ����ʱ����
		BasicBSONList workTimeData = (BasicBSONList) this
				.getValue(F_WORKTIMEDATA);
		if (workTimeData == null) {
			workTimeData = new BasicDBList();
			this.setValue(F_WORKTIMEDATA, workTimeData);
		}

		// 2. ������¼�����Ƿ���ڶ�Ӧ�Ķ���
		for (int i = 0; i < workTimeData.size(); i++) {
			DBObject workTime = (DBObject) workTimeData.get(i);
			ObjectId _columnTypeOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_COLUMNTYPEOPTION_ID);
			ObjectId _workTimeTypeOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_WORKTIMETYPEOPTION_ID);
			if (_columnTypeOptionId.equals(columnTypeOptionId)
					&& _workTimeTypeOptionId.equals(workTimeTypeOptionId)) {
				// ������ڣ���Ҫ�滻�ü�¼�Ĺ�ʱ����ֵ
				workTime.put(F_WORKTIMEDATA_AMOUNT, value);
				return;
			}
		}
		// ��������ڣ���Ҫ���һ����¼
		DBObject workTime = new BasicDBObject();
		workTime.put(F_WORKTIMEDATA_COLUMNTYPEOPTION_ID, columnTypeOptionId);
		workTime.put(F_WORKTIMEDATA_WORKTIMETYPEOPTION_ID, workTimeTypeOptionId);
		workTime.put(F_WORKTIMEDATA_AMOUNT, value);
		workTimeData.add(workTime);
	}

	public void appendType(String typeName, String fieldName) {
		DBObject type = new BasicDBObject();
		type.put(F__ID, new ObjectId());
		type.put(F_DESC, typeName);
		type.put(F_WORKTIME_TYPE_OPTIONS, new BasicDBList());
		Object value = getValue(fieldName);
		if (!(value instanceof BasicBSONList)) {
			value = new BasicDBList();
			setValue(fieldName, value);
		}
		// �������Ͳ��뵽��ʱ������
		((BasicBSONList) value).add(type);
	}

	public void appendOption(final DBObject type, String optionName) {
		// ����һ��������ѡ�����
		DBObject option = new BasicDBObject();
		option.put(F__ID, new ObjectId());
		option.put(F_DESC, optionName);
		Object options = type.get(F_WORKTIME_TYPE_OPTIONS);
		if (!(options instanceof BasicBSONList)) {
			options = new BasicDBList();
			type.put(F_WORKTIME_TYPE_OPTIONS, options);
		}
		// ��������ѡ����뵽��������
		((BasicBSONList) options).add(option);
	}

	public DBObject removeTypeOrOption(ObjectId _id, String fieldName) {
		// ��ȡ�����͵ļ���
		BasicBSONList types = (BasicBSONList) getValue(fieldName);
		// ���������ͼ���
		for (int i = 0; i < types.size(); i++) {
			DBObject type = (DBObject) types.get(i);
			// �ж���ѡԪ�ص�id�Ƿ���������idһ�£�һ�¾�ɾ����ˢ�¡�����
			if (_id.equals(type.get(F__ID))) {
				types.remove(i);
				return null;
			} else {
				// ��ѡԪ�ص�id��������id��һ�£��ͻ�ȡ�����͵�ѡ���
				BasicBSONList options = (BasicBSONList) type
						.get(F_WORKTIME_TYPE_OPTIONS);
				// ����������ѡ���
				for (int j = 0; j < options.size(); j++) {
					DBObject option = (DBObject) options.get(j);
					// �ж���ѡԪ��id��ѡ��idһ�µĻ�����ɾ����ˢ����ѡ�������͡�����
					if (_id.equals(option.get(F__ID))) {
						options.remove(j);
						return type;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
		// ��ȡ��ʱ����list
		BasicBSONList workTimeData = (BasicBSONList) getValue(F_WORKTIMEDATA);
		if (workTimeData != null) {
			Set<ObjectId> columnTypeOptionIdSet = getOptionIdSet(F_COLUMNTYPES);
			Set<ObjectId> workTimeTypeOptionIdSet = getOptionIdSet(F_WORKTIMETYPES);
			// ������ʱ����list
			for (int i = 0; i < workTimeData.size(); i++) {
				// ��ȡÿ����ʱ����
				DBObject data = (DBObject) workTimeData.get(i);
				// ��ȡ������ѡ��id
				ObjectId columnTypeOptionId = (ObjectId) data
						.get(F_WORKTIMEDATA_COLUMNTYPEOPTION_ID);
				ObjectId workTimeTypeOptionId = (ObjectId) data
						.get(F_WORKTIMEDATA_WORKTIMETYPEOPTION_ID);
				if (!columnTypeOptionIdSet.contains(columnTypeOptionId)
						|| !workTimeTypeOptionIdSet
								.contains(workTimeTypeOptionId)) {
					workTimeData.remove(i);
					i--;
				}

			}
		}
		super.doUpdate(context);
	}

	private Set<ObjectId> getOptionIdSet(String fieldName) {
		Set<ObjectId> result = new HashSet<ObjectId>();
		BasicBSONList types = (BasicBSONList) getValue(fieldName);
		for (int i = 0; i < types.size(); i++) {
			DBObject type = (DBObject) types.get(i);
			BasicBSONList options = (BasicBSONList) type
					.get(F_WORKTIME_TYPE_OPTIONS);
			for (int j = 0; j < options.size(); j++) {
				DBObject option = (DBObject) options.get(j);
				result.add((ObjectId) option.get(F__ID));
			}
		}
		return result;
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		// ͬ�����ù�ʱ��������Ŀģ��
		// {$pull:{worktimeprograms:ObjectId('53730d6980737491eb208ee3')}}
		// ��ȡ��Ŀģ��ļ���
		DBCollection projectTemplateCollection = getCollection(IModelConstants.C_PROJECT_TEMPLATE);
		projectTemplateCollection.update(new BasicDBObject().append(
				ProjectTemplate.F_WORKTIMEPROGRAMS, get_id()),
				new BasicDBObject().append("$pull", new BasicDBObject().append(
						ProjectTemplate.F_WORKTIMEPROGRAMS, get_id())), false,
				true);
		super.doRemove(context);
	}

	/**
	 * ���������ֶ�����ѡ��id����ø�ѡ������������
	 * @param optionId
	 * @param typeFieldName
	 * @return
	 */
	public DBObject getWorkTimeType(ObjectId optionId, String typeFieldName) {
		BasicBSONList types = (BasicBSONList) getValue(typeFieldName);
		for (Object object : types) {
			DBObject type = (DBObject) object;
			BasicBSONList options = (BasicBSONList) type
					.get(F_WORKTIME_TYPE_OPTIONS);
			for (Object object2 : options) {
				DBObject option = (DBObject) object2;
				if (option.get(F__ID).equals(optionId)) {
					return type;
				}
			}
		}
		return null;
	}

}
