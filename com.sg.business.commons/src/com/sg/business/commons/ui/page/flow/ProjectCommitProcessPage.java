package com.sg.business.commons.ui.page.flow;

import com.sg.business.model.Project;

/**
 * ��Ŀ�ύʱ�����ָ�������̣����Զ�������������������Ŀ�ύ<br/>
 * �ö��������Ľ�ɫ��������֯�Ľ�ɫ����������Ŀ��ɫ���塣���ԣ���Ҫ���ǻ�ȡ��ɫ���岿�ֵĴ���
 * @author zhonghua
 *
 */
public class ProjectCommitProcessPage extends AbstractProjectProcessPage {


	@Override
	protected String getProcessKey() {
		return Project.F_WF_COMMIT;
	}


}
