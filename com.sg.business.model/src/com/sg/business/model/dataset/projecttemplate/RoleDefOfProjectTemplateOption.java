package com.sg.business.model.dataset.projecttemplate;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * ��������ĸ�����,�е��ߺ�ָ���ߵĽ�ɫ����
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}��
 * ���ڻ�ù�������ĸ�����,�е��ߺ�ָ���ߵĽ�ɫ��Ϣ��Ӧ���ڹ���������ѡ������,�е��ߺ�ָ���߽�ɫ<br/>
 * �������¹��ܣ�
 * <li>��ȡ��������ĸ�����,�е��ߺ�ָ���ߵĽ�ɫ����
 * <li>���õ�ǰ����Ϊ��ǰ��Ŀģ�������Ľ�ɫ��Ϣ
 * 
 * @author yangjun
 *
 */
public class RoleDefOfProjectTemplateOption extends OptionDataSetFactory {

	/**
	 * ��������ĸ�����,�е��ߺ�ָ���ߵĽ�ɫ���ϵĹ��캯��,���ý�ɫ�������ݿ⼰���ݴ洢��
	 */
	public RoleDefOfProjectTemplateOption() {
		//���ý�ɫ�������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
	}

	/**
	 * ���õ�ǰ����Ϊ��ǰ��Ŀģ�������Ľ�ɫ��Ϣ
	 * @param data : ��ǰ�༭����
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		//��ȡ��ǰ����������������Ŀģ����Ϣ
		WorkDefinition workd = (WorkDefinition) data;
		ProjectTemplate projectTemplate = workd.getProjectTemplate();
		//���ò�ѯ����Ϊ��ǰ��Ŀģ�������Ľ�ɫ��Ϣ
		setQueryCondition(new BasicDBObject().append(
				RoleDefinition.F_PROJECT_TEMPLATE_ID, projectTemplate.get_id()));
	}

}
