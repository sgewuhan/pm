package com.sg.business.model.dataset.project;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * �����ĸ�����,�����ߺ�ָ���ߵĽ�ɫ����
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}��
 * ���ڻ�ù����ĸ�����,�����ߺ�ָ���ߵĽ�ɫ��Ϣ��Ӧ���ڹ�����ѡ������,�����ߺ�ָ���߽�ɫ<br/>
 * �������¹��ܣ�
 * <li>��ȡ�����ĸ�����,�����ߺ�ָ���ߵĽ�ɫ����
 * <li>���õ�ǰ����Ϊ��ǰ��Ŀ�����Ľ�ɫ��Ϣ
 * 
 * @author yangjun
 *
 */
public class RoleDefOfProjectOption extends OptionDataSetFactory {

	/**
	 * �����ĸ�����,�����ߺ�ָ���ߵĽ�ɫ���ϵĹ��캯��,������Ŀ��ɫ�������ݿ⼰���ݴ洢��
	 */
	public RoleDefOfProjectOption() {
		//������Ŀ��ɫ�������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_PROJECT_ROLE);
	}

	/**
	 * ���õ�ǰ����Ϊ��ǰ��Ŀ�����Ľ�ɫ��Ϣ
	 * @param data : ��ǰ�༭����
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		//��ȡ��ǰ������������Ŀ��Ϣ
		Work workd = (Work) data;
		Project project = workd.getProject();
		//���ò�ѯ����Ϊ��ǰ��Ŀ�����Ľ�ɫ��Ϣ
		setQueryCondition(new BasicDBObject().append(
				ProjectRole.F_PROJECT_ID, project.get_id()));
	}

}
