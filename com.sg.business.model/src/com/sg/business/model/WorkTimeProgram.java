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
	 * 项目类型
	 */
	public static final String F_WORKTIME_PARA_Y = "worktimepara_y";
	/**
	 * 工作类型
	 */
	public static final String F_WORKTIME_PARA_X = "worktimepara_x";
	/**
	 * 工时数据
	 */
	public static final String F_WORKTIMEDATA = "worktimedata";
	/**
	 * 说明
	 */
	public static final String F_DESCRIPTION = "description";
	/**
	 * 是否启用
	 */
	public static final String F_ACTIVATED = "activated";

	/**
	 * 所属组织ID
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$

	/**
	 * 类型选项，用于paraY 子记录的字段，DBObject类型
	 */
	public static final String F_WORKTIME_PARA_OPTIONS = "options";

	/**
	 * 工时数据的列类型选项id
	 */
	public static final String F_WORKTIMEDATA_PARA_Y_OPTION_ID = "para_y_option_id";

	/**
	 * 工时数据的工时类型选项id
	 */
	public static final String F_WORKTIMEDATA_PARA_X_OPTION_ID = "para_x_option_id";

	/**
	 * 工时数据的工时
	 */
	public static final String F_WORKTIMEDATA_AMOUNT = "amount";

	public Double getWorkTimeData(ObjectId paraXOption, ObjectId paraYOption) {
		// 1.取出工时数据
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
		// 1.取出工时数据
		BasicBSONList workTimeData = (BasicBSONList) this
				.getValue(F_WORKTIMEDATA);
		if (workTimeData == null) {
			workTimeData = new BasicDBList();
			this.setValue(F_WORKTIMEDATA, workTimeData);
		}

		// 2. 遍历记录查找是否存在对应的定义
		for (int i = 0; i < workTimeData.size(); i++) {
			DBObject workTime = (DBObject) workTimeData.get(i);
			ObjectId _paraYOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_PARA_Y_OPTION_ID);
			ObjectId _paraXOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_PARA_X_OPTION_ID);
			if (_paraYOptionId.equals(paraYOptionId)
					&& _paraXOptionId.equals(paraXOptionId)) {
				// 如果存在，需要替换该记录的工时数据值
				workTime.put(F_WORKTIMEDATA_AMOUNT, value);
				return;
			}
		}
		// 如果不存在，需要添加一条记录
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
		// 将列类型插入到工时方案中
		((BasicBSONList) value).add(type);
	}

	public void appendOption(final DBObject type, String optionName) {
		// 构造一个列类型选项对象
		DBObject option = new BasicDBObject();
		option.put(F__ID, new ObjectId());
		option.put(F_DESC, optionName);
		Object options = type.get(F_WORKTIME_PARA_OPTIONS);
		if (!(options instanceof BasicBSONList)) {
			options = new BasicDBList();
			type.put(F_WORKTIME_PARA_OPTIONS, options);
		}
		// 将列类型选项插入到列类型下
		((BasicBSONList) options).add(option);
	}

	public DBObject removeTypeOrOption(ObjectId _id, String fieldName) {
		// 获取列类型的集合
		BasicBSONList types = (BasicBSONList) getValue(fieldName);
		// 遍历列类型集合
		for (int i = 0; i < types.size(); i++) {
			DBObject type = (DBObject) types.get(i);
			// 判断所选元素的id是否与列类型id一致，一致就删除、刷新、保存
			if (_id.equals(type.get(F__ID))) {
				types.remove(i);
				return null;
			} else {
				// 所选元素的id与列类型id不一致，就获取列类型的选项集合
				BasicBSONList options = (BasicBSONList) type
						.get(F_WORKTIME_PARA_OPTIONS);
				// 遍历列类型选项集合
				for (int j = 0; j < options.size(); j++) {
					DBObject option = (DBObject) options.get(j);
					// 判断所选元素id与选项id一致的话，就删除、刷新所选的列类型、保存
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
		// 获取工时数据list
		BasicBSONList workTimeData = (BasicBSONList) getValue(F_WORKTIMEDATA);
		if (workTimeData != null) {
			Set<ObjectId> paraYOptionIdSet = getOptionIdSet(F_WORKTIME_PARA_Y);
			Set<ObjectId> paraXOptionIdSet = getOptionIdSet(F_WORKTIME_PARA_X);
			// 遍历工时数据list
			for (int i = 0; i < workTimeData.size(); i++) {
				// 获取每条工时数据
				DBObject data = (DBObject) workTimeData.get(i);
				// 获取列类型选项id
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
		// 同步引用工时方案的项目模板
		// {$pull:{worktimeprograms:ObjectId('53730d6980737491eb208ee3')}}
		// 获取项目模板的集合
		DBCollection projectTemplateCollection = getCollection(IModelConstants.C_PROJECT_TEMPLATE);
		projectTemplateCollection.update(new BasicDBObject().append(
				ProjectTemplate.F_WORKTIMEPROGRAMS, get_id()),
				new BasicDBObject().append("$pull", new BasicDBObject().append(
						ProjectTemplate.F_WORKTIMEPROGRAMS, get_id())), false,
				true);
		super.doRemove(context);
	}

	/**
	 * 给定类型字段名和选项id，获得该选项所属的类型
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
	 * 2014.6.18日解决工时方案启用/停用的问题
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter.equals(IActivateSwitch.class)) {
			return (T) new ActivateSwitch(this);
		}
		return super.getAdapter(adapter);
	}

	/**
	 * 2014.6.23日 编辑参数或选项
	 * 
	 * @param _id
	 * @param fieldName workspara_y
	 * @param typeName  文本输入框的值
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
				//当前工时参数名称
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
				// 所选元素的id与列类型id不一致，就获取列类型的选项集合
				BasicBSONList options = (BasicBSONList) type
						.get(F_WORKTIME_PARA_OPTIONS);
				// 遍历列类型选项集合
				for (int j = 0; j < options.size(); j++) {
					DBObject option = (DBObject) options.get(j);
					// 判断所选元素id与选项id一致的话，就删除、刷新所选的列类型、保存
					if (_id.equals(option.get(F__ID))) {
						//当前工时参数选项名称
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
