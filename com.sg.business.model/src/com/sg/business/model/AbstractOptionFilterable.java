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
 * ����Ŀģ�������ÿ��԰�����Щѡ���ÿ��ѡ��ж�����һ��ѡ��
 * 
 * �����ﶨ����߹����������ѡ���ֶΣ����ֶ�Ϊ�����������±�ʾ��
 * <br/>
 * <code>
 * {[optionset:"��׼",option:"IRIS",value:����],
 * [optionset:"��׼",option:"GB",value:�ų�],
 * [optionset:"��Ʒ����",option:"���",value:�ų�]}
 * </code>
 * 
 * @author zhonghua
 * 
 */
public abstract class AbstractOptionFilterable extends PrimaryObject {

	/**
	 * ѡ��ֵ��ֵ��Ϊ���룬��ѡ���ų�
	 */
	public static final String SF_VALUE = "value";

	/**
	 * ѡ��µ�ѡ��
	 */
	public static final String SF_OPTION = "option";

	/**
	 * ѡ�
	 */
	public static final String SF_OPTIONSET = "optionset";

	/**
	 * ����ģ�崴������ӣ��ҽ�ֹɾ��
	 */
	public static final String VALUE_MANDATORY = "����";

	/**
	 * ����ģ�崴������ӣ�����ɾ��
	 */
	public static final String VALUE_OPTION = "��ѡ";

	public static final String VALUE_EMPTY = "";

	/**
	 * ����ģ�崴�������
	 */
	public static final String VALUE_EXCLUDE = "�ų�";

	/**
	 * ����׼��ѡ�������
	 */
	public static final String OPTIONSET_NAME_STANDARD = "��׼";

	public static final String OPTIONSET_NAME_PRODUCTTYPE = "��Ʒ����";

	public static final String OPTIONSET_NAME_PROJECTTYPE = "��Ŀ����";

	public static final String[] VALUE_SET = new String[] { VALUE_EMPTY,
			VALUE_MANDATORY, VALUE_OPTION, VALUE_EXCLUDE };

	public static final String F_OPTION_FILTERS = "optionFilters";

	/**
	 * ����ѡ���ѡ���ֵ�����룬��ѡ���ų���
	 * 
	 * @param optionSet
	 *            ,ѡ�
	 * @param option
	 *            ,ѡ��
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
	 * ����ѡ���ѡ���ֵ
	 * 
	 * @param optionSet
	 *            ,ѡ�༯
	 * @param option
	 *            ,ѡ��
	 * @param value
	 *            ,ֵ
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
