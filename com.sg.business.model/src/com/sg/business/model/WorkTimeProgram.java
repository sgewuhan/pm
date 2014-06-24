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
	 * ��Ŀ����
	 */
	public static final String F_WORKTIME_PARA_Y = "worktimepara_y";
	/**
	 * ��������
	 */
	public static final String F_WORKTIME_PARA_X = "worktimepara_x";
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
	 * ����ѡ�����paraY �Ӽ�¼���ֶΣ�DBObject����
	 */
	public static final String F_WORKTIME_PARA_OPTIONS = "options";

	/**
	 * ��ʱ���ݵ�������ѡ��id
	 */
	public static final String F_WORKTIMEDATA_PARA_Y_OPTION_ID = "para_y_option_id";

	/**
	 * ��ʱ���ݵĹ�ʱ����ѡ��id
	 */
	public static final String F_WORKTIMEDATA_PARA_X_OPTION_ID = "para_x_option_id";

	/**
	 * ��ʱ���ݵĹ�ʱ
	 */
	public static final String F_WORKTIMEDATA_AMOUNT = "amount";

	public Double getWorkTimeData(ObjectId paraXOption, ObjectId paraYOption) {
		// 1.ȡ����ʱ����
		BasicBSONList workTimeData = (BasicBSONList) this
				.getValue(F_WORKTIMEDATA);
		if (workTimeData == null) {
			return null;
		}
		for (int i = 0; i < workTimeData.size(); i++) {
			DBObject workTime = (DBObject) workTimeData.get(i);
			ObjectId paraYOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_PARA_Y_OPTION_ID);
			ObjectId paraXOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_PARA_X_OPTION_ID);
			if (paraYOption.equals(paraYOptionId)
					&& paraXOption.equals(paraXOptionId)) {
				return (Double) workTime.get(F_WORKTIMEDATA_AMOUNT);
			}
		}
		return null;
	}

	public void makeWorkTimeData(ObjectId paraXOptionId,
			ObjectId paraYOptionId, Double value) {
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
			ObjectId _paraYOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_PARA_Y_OPTION_ID);
			ObjectId _paraXOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_PARA_X_OPTION_ID);
			if (_paraYOptionId.equals(paraYOptionId)
					&& _paraXOptionId.equals(paraXOptionId)) {
				// ������ڣ���Ҫ�滻�ü�¼�Ĺ�ʱ����ֵ
				workTime.put(F_WORKTIMEDATA_AMOUNT, value);
				return;
			}
		}
		// ��������ڣ���Ҫ���һ����¼
		DBObject workTime = new BasicDBObject();
		workTime.put(F_WORKTIMEDATA_PARA_Y_OPTION_ID, paraYOptionId);
		workTime.put(F_WORKTIMEDATA_PARA_X_OPTION_ID, paraXOptionId);
		workTime.put(F_WORKTIMEDATA_AMOUNT, value);
		workTimeData.add(workTime);
	}

	public void appendType(String typeName, String fieldName) {
		DBObject type = new BasicDBObject();
		type.put(F__ID, new ObjectId());
		type.put(F_DESC, typeName);
		type.put(F_WORKTIME_PARA_OPTIONS, new BasicDBList());
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
		Object options = type.get(F_WORKTIME_PARA_OPTIONS);
		if (!(options instanceof BasicBSONList)) {
			options = new BasicDBList();
			type.put(F_WORKTIME_PARA_OPTIONS, options);
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
						.get(F_WORKTIME_PARA_OPTIONS);
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
			Set<ObjectId> paraYOptionIdSet = getOptionIdSet(F_WORKTIME_PARA_Y);
			Set<ObjectId> paraXOptionIdSet = getOptionIdSet(F_WORKTIME_PARA_X);
			// ������ʱ����list
			for (int i = 0; i < workTimeData.size(); i++) {
				// ��ȡÿ����ʱ����
				DBObject data = (DBObject) workTimeData.get(i);
				// ��ȡ������ѡ��id
				ObjectId paraYOptionId = (ObjectId) data
						.get(F_WORKTIMEDATA_PARA_Y_OPTION_ID);
				ObjectId paraXOptionId = (ObjectId) data
						.get(F_WORKTIMEDATA_PARA_X_OPTION_ID);
				if (!paraYOptionIdSet.contains(paraYOptionId)
						|| !paraXOptionIdSet.contains(paraXOptionId)) {
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
					.get(F_WORKTIME_PARA_OPTIONS);
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
	 * 
	 * @param optionId
	 * @param typeFieldName
	 * @return
	 */
	public DBObject getParaX(ObjectId optionId, String typeFieldName) {
		BasicBSONList types = (BasicBSONList) getValue(typeFieldName);
		for (Object object : types) {
			DBObject type = (DBObject) object;
			BasicBSONList options = (BasicBSONList) type
					.get(F_WORKTIME_PARA_OPTIONS);
			for (Object object2 : options) {
				DBObject option = (DBObject) object2;
				if (option.get(F__ID).equals(optionId)) {
					return type;
				}
			}
		}
		return null;
	}

	/**
	 * 2014.6.18�ս����ʱ��������/ͣ�õ�����
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter.equals(IActivateSwitch.class)) {
			return (T) new ActivateSwitch(this);
		}
		return super.getAdapter(adapter);
	}

	/**
	 * 2014.6.23�� �༭������ѡ��
	 * 
	 * @param _id
	 * @param fieldName workspara_y
	 * @param typeName  �ı�������ֵ
	 * @return
	 */
	public DBObject modifyTypeOrOption(ObjectId _id,
			String typeName,String fieldName) {
		BasicBSONList types = (BasicBSONList) getValue(fieldName);
		if(types==null){
			return null;
		}
		for (int i = 0; i < types.size(); i++) {
			DBObject type = (DBObject) types.get(i);
			if (_id.equals(type.get(F__ID))) {
				//��ǰ��ʱ��������
				String typeDesc = (String) type.get(F_DESC);
				DBCollection col = getCollection();
				BasicDBObject query = new BasicDBObject().append(
						F_DESC, typeDesc);
				BasicDBObject update = new BasicDBObject().append("$set",
						new BasicDBObject()
								.append(F_DESC, typeName));
				col.update(query, update);
				type.put(F_DESC, typeName);
			} else {
				// ��ѡԪ�ص�id��������id��һ�£��ͻ�ȡ�����͵�ѡ���
				BasicBSONList options = (BasicBSONList) type
						.get(F_WORKTIME_PARA_OPTIONS);
				// ����������ѡ���
				for (int j = 0; j < options.size(); j++) {
					DBObject option = (DBObject) options.get(j);
					// �ж���ѡԪ��id��ѡ��idһ�µĻ�����ɾ����ˢ����ѡ�������͡�����
					if (_id.equals(option.get(F__ID))) {
						//��ǰ��ʱ����ѡ������
						String optionDesc = (String) option.get(F_DESC);
						DBCollection col = getCollection();
						BasicDBObject query = new BasicDBObject().append(
								F_DESC, optionDesc);
						BasicDBObject update = new BasicDBObject().append("$set",
								new BasicDBObject()
										.append(F_DESC, typeName));
						col.update(query, update);
						option.put(F_DESC, typeName);
						return type;
					}
				}
			}
		}
		return null;
	}

}
