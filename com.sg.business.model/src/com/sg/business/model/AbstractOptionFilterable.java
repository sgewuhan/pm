package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 
 * 在项目模板中设置可以包含哪些选项集，每个选项集中都包含一组选项
 * 
 * 交付物定义或者工作定义包含选项字段，该字段为数组类型如下表示：
 * <br/>
 * <code>
 * {[optionset:"标准",option:"IRIS",value:必须],
 * [optionset:"标准",option:"GB",value:排除],
 * [optionset:"产品类型",option:"球绞",value:排除]}
 * </code>
 * 
 * @author zhonghua
 * 
 */
public abstract class AbstractOptionFilterable extends PrimaryObject {

	/**
	 * 选项值，值可为必须，可选，排除
	 */
	public static final String SF_VALUE = "value";

	/**
	 * 选项集下的选项
	 */
	public static final String SF_OPTION = "option";

	/**
	 * 选项集
	 */
	public static final String SF_OPTIONSET = "optionset";

	/**
	 * 根据模板创建后添加，且禁止删除
	 */
	public static final String VALUE_MANDATORY = "必须";

	/**
	 * 根据模板创建后添加，可以删除
	 */
	public static final String VALUE_OPTION = "可选";

	public static final String VALUE_EMPTY = "";

	/**
	 * 根据模板创建后不添加
	 */
	public static final String VALUE_EXCLUDE = "排除";

	/**
	 * “标准”选项集的名称
	 */
	public static final String OPTIONSET_NAME_STANDARD = "标准";

	public static final String OPTIONSET_NAME_PRODUCTTYPE = "产品类型";

	public static final String OPTIONSET_NAME_PROJECTTYPE = "项目类型";

	public static final String[] VALUE_SET = new String[] { VALUE_EMPTY,
			VALUE_MANDATORY, VALUE_OPTION, VALUE_EXCLUDE };

	public static final String F_OPTION_FILTERS = "optionFilters";

	/**
	 * 返回选项集中选项的值（必须，可选，排除）
	 * 
	 * @param optionSet
	 *            ,选项集
	 * @param option
	 *            ,选项
	 * @return String
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
	 * 设置选项集中选项的值
	 * 
	 * @param optionSet
	 *            ,选相集
	 * @param option
	 *            ,选项
	 * @param value
	 *            ,值
	 * @param context
	 * @throws Exception
	 */
	public void doSetOptionValueSetting(String optionSet, String option,
			Object value, IContext context) throws Exception {
		BasicDBList filters = (BasicDBList) getValue(F_OPTION_FILTERS);
		if (filters == null) {
			filters = new BasicDBList();
		}
		BasicDBObject filterElement = new BasicDBObject()
				.append(SF_OPTIONSET, optionSet).append(SF_OPTION, option)
				.append(SF_VALUE, value);
		boolean has = filters.contains(filterElement);
		if (has) {
			return;
		}
		for (int i = 0; i < filters.size(); i++) {
			DBObject filter = (DBObject) filters.get(i);
			if (optionSet.equals(filter.get(SF_OPTIONSET))
					&& option.equals(filter.get(SF_OPTION))) {
				filters.remove(i);
				break;
			}
		}

		filters.add(filterElement);
		setValue(F_OPTION_FILTERS, filters);
		doSave(context);
	}

}
