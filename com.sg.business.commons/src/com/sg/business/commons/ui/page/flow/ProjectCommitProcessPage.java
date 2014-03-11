package com.sg.business.commons.ui.page.flow;

import com.sg.business.model.Project;

/**
 * 项目提交时，如果指定了流程，将自动创建独立工作用于项目提交<br/>
 * 该独立工作的角色设置是组织的角色，而并非项目角色定义。所以，需要覆盖获取角色定义部分的代码
 * @author zhonghua
 *
 */
public class ProjectCommitProcessPage extends AbstractProjectProcessPage {


	@Override
	protected String getProcessKey() {
		return Project.F_WF_COMMIT;
	}


}
