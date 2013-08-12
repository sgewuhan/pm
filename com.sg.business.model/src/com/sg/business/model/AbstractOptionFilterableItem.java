package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class AbstractOptionFilterableItem extends PrimaryObject {

	public static final String SF_VALUE = "value";

	public static final String SF_OPTION = "option";

	public static final String SF_OPTIONSET = "optionset";

	/**
	 * 根据模板创建后添加，且禁止删除
	 */
	public static final String VALUE_MONDARY = "必须";

	/**
	 * 根据模板创建后添加，可以删除
	 */
	public static final String VALUE_OPTION = "可选";

	public static final String VALUE_EMPTY = "";

	/**
	 * 根据模板创建后不添加
	 */
	public static final String VALUE_EXCLUDE = "排除";

	public static final String[] VALUE_SET = new String[] { VALUE_EMPTY,
			VALUE_MONDARY, VALUE_OPTION, VALUE_EXCLUDE };

	public static final String F_OPTION_FILTERS = "optionFilters";

	/*
	 * 在项目模板中设置可以包含哪些选项集，每个选项集中都包含一组选项
	 * 
	 * 交付物定义或者工作定义包含选项字段，该字段为数组类型如下表示：
	 * 
	 * {[optionset:"标准",option:"IRIS",value:必须],
	 * [optionset:"标准",option:"GB",value:排除],
	 * [optionset:"产品类型",option:"球绞",value:排除]}
	 */

	public String getOptionValueSetting(String optionSet, String option) {
		Object value = getValue(F_OPTION_FILTERS);
		if (value instanceof List<?>) {
			List<?> optionFilters = (List<?>) value;
			for (int i = 0; i < optionFilters.size(); i++) {
				DBObject filter = (DBObject) optionFilters.get(i);
				Object itemOptionSet = filter.get(SF_OPTIONSET);
				Object itemOption = filter.get(SF_OPTION);
				if (optionSet.equals(itemOptionSet)
						&& option.equals(itemOption)) {
					return (String) filter.get(SF_VALUE);
				}
			}
		}
		return VALUE_EMPTY;
	}

	/**
	 * @param optionSet
	 * @param option
	 * @param value
	 * @param context
	 * @throws Exception
	 */
	public void doSetOptionValueSetting(String optionSet, String option,
			Object value, IContext context) throws Exception {
		BasicDBList filters = (BasicDBList) getValue(F_OPTION_FILTERS);
		if (filters == null) {
			filters = new BasicDBList();
		}
		BasicDBObject filterElement = new BasicDBObject().append(SF_OPTIONSET, optionSet)
				.append(SF_OPTION, option).append(SF_VALUE, value);
		boolean has = filters.contains(filterElement);
		if (has) {
			return;
		}
		for (int i = 0; i < filters.size(); i++) {
			DBObject filter = (DBObject) filters.get(i);
			if(optionSet.equals(filter.get(SF_OPTIONSET))&&option.equals(filter.get(SF_OPTION))){
				filters.remove(i);
				break;
			}
		}
		
		filters.add(filterElement);
		setValue(F_OPTION_FILTERS, filters);
		doSave(context);
	}

}
