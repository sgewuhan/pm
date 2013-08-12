package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class AbstractOptionFilterableItem extends PrimaryObject {

	/**
	 * ����ģ�崴������ӣ��ҽ�ֹɾ��
	 */
	public static final String VALUE_MONDARY = "����";

	/**
	 * ����ģ�崴������ӣ�����ɾ��
	 */
	public static final String VALUE_OPTION = "��ѡ";

	public static final String VALUE_EMPTY = "";

	/**
	 * ����ģ�崴�������
	 */
	public static final String VALUE_EXCLUDE = "�ų�";

	public static final String[] VALUE_SET = new String[] { VALUE_EMPTY,
			VALUE_MONDARY, VALUE_OPTION, VALUE_EXCLUDE };

	public static final String F_OPTION_FILTERS = "optionFilters";

	/*
	 * ����Ŀģ�������ÿ��԰�����Щѡ���ÿ��ѡ��ж�����һ��ѡ��
	 * 
	 * �����ﶨ����߹����������ѡ���ֶΣ����ֶ�Ϊ�����������±�ʾ��
	 * 
	 * {[optionset:"��׼",option:"IRIS",value:����],
	 * [optionset:"��׼",option:"GB",value:�ų�],
	 * [optionset:"��Ʒ����",option:"���",value:�ų�]}
	 */

	public String getOptionValueSetting(String optionSet, String option) {
		Object value = getValue(F_OPTION_FILTERS);
		if (value instanceof List<?>) {
			List<?> optionFilters = (List<?>) value;
			for (int i = 0; i < optionFilters.size(); i++) {
				DBObject filter = (DBObject) optionFilters.get(i);
				Object itemOptionSet = filter.get("optionset");
				Object itemOption = filter.get("option");
				if (optionSet.equals(itemOptionSet)
						&& option.equals(itemOption)) {
					return (String) filter.get("value");
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
		BasicDBObject filterElement = new BasicDBObject().append("optionset", optionSet)
				.append("option", option).append("value", value);
		boolean has = filters.contains(filterElement);
		if (has) {
			return;
		}
		for (int i = 0; i < filters.size(); i++) {
			DBObject filter = (DBObject) filters.get(i);
			if(optionSet.equals(filter.get("optionset"))&&option.equals(filter.get("option"))){
				filters.remove(i);
				break;
			}
		}
		
		filters.add(filterElement);
		setValue(F_OPTION_FILTERS, filters);
		doSave(context);
	}

}
