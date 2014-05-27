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
	 * 列类型
	 */
	public static final String F_COLUMNTYPES = "columntypes";
	/**
	 * 工时类型
	 */
	public static final String F_WORKTIMETYPES = "worktimetypes";
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
	 * 类型选项，用于ColumnType 子记录的字段，DBObject类型
	 */
	public static final String F_WORKTIME_TYPE_OPTIONS = "options";

	/**
	 * 工时数据的列类型选项id
	 */
	public static final String F_WORKTIMEDATA_COLUMNTYPEOPTION_ID = "columntypeoption_id";

	/**
	 * 工时数据的工时类型选项id
	 */
	public static final String F_WORKTIMEDATA_WORKTIMETYPEOPTION_ID = "worktimetypeoption_id";

	/**
	 * 工时数据的工时
	 */
	public static final String F_WORKTIMEDATA_AMOUNT = "amount";

	public Double getWorkTimeData(ObjectId workTimeTypeOption,
			ObjectId columnTypeOption) {
		// 1.取出工时数据
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
			ObjectId _columnTypeOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_COLUMNTYPEOPTION_ID);
			ObjectId _workTimeTypeOptionId = (ObjectId) workTime
					.get(F_WORKTIMEDATA_WORKTIMETYPEOPTION_ID);
			if (_columnTypeOptionId.equals(columnTypeOptionId)
					&& _workTimeTypeOptionId.equals(workTimeTypeOptionId)) {
				// 如果存在，需要替换该记录的工时数据值
				workTime.put(F_WORKTIMEDATA_AMOUNT, value);
				return;
			}
		}
		// 如果不存在，需要添加一条记录
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
		// 将列类型插入到工时方案中
		((BasicBSONList) value).add(type);
	}

	public void appendOption(final DBObject type, String optionName) {
		// 构造一个列类型选项对象
		DBObject option = new BasicDBObject();
		option.put(F__ID, new ObjectId());
		option.put(F_DESC, optionName);
		Object options = type.get(F_WORKTIME_TYPE_OPTIONS);
		if (!(options instanceof BasicBSONList)) {
			options = new BasicDBList();
			type.put(F_WORKTIME_TYPE_OPTIONS, options);
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
						.get(F_WORKTIME_TYPE_OPTIONS);
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
			Set<ObjectId> columnTypeOptionIdSet = getOptionIdSet(F_COLUMNTYPES);
			Set<ObjectId> workTimeTypeOptionIdSet = getOptionIdSet(F_WORKTIMETYPES);
			// 遍历工时数据list
			for (int i = 0; i < workTimeData.size(); i++) {
				// 获取每条工时数据
				DBObject data = (DBObject) workTimeData.get(i);
				// 获取列类型选项id
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
