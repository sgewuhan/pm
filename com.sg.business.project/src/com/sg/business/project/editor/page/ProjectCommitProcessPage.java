package com.sg.business.project.editor.page;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;

/**
 * ��Ŀ�ύʱ�����ָ�������̣����Զ�������������������Ŀ�ύ<br/>
 * �ö��������Ľ�ɫ��������֯�Ľ�ɫ����������Ŀ��ɫ���塣���ԣ���Ҫ���ǻ�ȡ��ɫ���岿�ֵĴ���
 * @author zhonghua
 *
 */
public class ProjectCommitProcessPage extends AbstractWorkProcessPage {

	@Override
	protected String getWorkflowKey() {
		return Project.F_WF_COMMIT;
	}

	@Override
	protected List<PrimaryObject> getRoleDefinitions(PrimaryObject po) {
		if(po instanceof Project){
			Project project = (Project) po;
			return project.getRoleDefinitions();
		}else{
			return null;
		}
	}
}
