package com.sg.business.model.dataset.projecttemplate;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * �������弯��
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}��
 * ���ڻ����Ŀģ��Ĺ���������Ϣ��Ӧ���ڹ���ǰ���ù�ϵ��ѡ����Ŀģ��Ĺ�������<br/>
 * �������¹��ܣ�
 * <li>��ȡ��Ŀģ��Ĺ������弯��
 * <li>���õ�ǰ����Ϊ��ǰ��Ŀģ�������Ĺ���������Ϣ
 * 
 * @author yangjun
 *
 */
public class WorkDefConnectionOfProjecttemplateOption extends
		OptionDataSetFactory {

	/**
	 * �������弯�ϵĹ��캯��,���ù�������������ݿ⼰���ݴ洢��
	 */
	public WorkDefConnectionOfProjecttemplateOption() {
		//���ù�������������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
	}

	/**
	 * ���õ�ǰ����Ϊ��ǰ��Ŀģ�������Ĺ���������Ϣ
	 * @param data : ��ǰ�༭����
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		//��ȡ��ǰ��������Ŀģ����Ϣ
		Object projectTemplateId = data
				.getValue(WorkDefinition.F_PROJECT_TEMPLATE_ID);
		//���ò�ѯ����Ϊ��ǰ��Ŀģ�������Ĺ���������Ϣ
		setQueryCondition(new BasicDBObject().append(
				WorkDefinition.F_PROJECT_TEMPLATE_ID,
				projectTemplateId));
	}

}
