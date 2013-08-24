package com.sg.business.model.dataset.project;

import org.bson.types.ObjectId;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * ��Ŀģ�漯��
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}��
 * ���ڻ����Ŀģ��ļ�����Ϣ��Ӧ�����½���Ŀ��ѡ����Ŀģ��<br/>
 * �������¹��ܣ�
 * <li>��ȡ��Ŀģ�漯��
 * <li>���õ�ǰ����Ϊ������֯��ǰ��֯����Ŀģ��
 * 
 * @author yangjun
 *
 */
public class ProjectTemplateOfOrg extends OptionDataSetFactory {

	/**
	 * ��Ŀģ�漯�ϵĹ��캯��,������Ŀģ��������ݿ⼰���ݴ洢��
	 */
	public ProjectTemplateOfOrg() {
		//������Ŀģ��������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
	}

	/**
	 * ���õ�ǰ����Ϊ������֯��ǰ��֯����Ŀģ��
	 * @param data ����ǰ�༭����
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		//��ȡ��ǰ��Ŀ��������Ŀְ����֯
		Project project = (Project) data;
		ObjectId org_id = project.getFunctionOrganizationId();
		//���ò�ѯ����Ϊ������֯��ǰ��֯
		if(org_id!=null){
			setQueryCondition(new BasicDBObject().append(ProjectTemplate.F_ORGANIZATION_ID, org_id).append(ProjectTemplate.F_ACTIVATED, Boolean.TRUE));
		}else{
			setQueryCondition(new BasicDBObject().append(ProjectTemplate.F__ID, null));
		}
		super.setEditorData(data);
	}


}
