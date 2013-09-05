package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectWizard;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectCommit extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if(selected instanceof Project){
			ViewerControl vc = getCurrentViewerControl(event);
			Project project = (Project) selected;
			try {
				doCommit(project,new CurrentAccountContext());
				vc.getViewer().update(selected, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}
	
	/**
	 * 提交项目计划<br/>
	 * 判断项目计划是否定义了提交流程，如果定义了提交流程，使用流程进行提交 。<br>
	 * 如果没有定义流程，直接发送消息。<br/>

	 * 
	 * @param context
	 * @throws Exception
	 */
	public void doCommit(Project project,IContext context) throws Exception {
		if(project.isCommitWorkflowActivate()){
			Work work = project.makeWorkflowCommitableWork(null,context);
			//打开定义的用于启动工作的对话框编辑器
			DataObjectWizard.open(work, Work.EDITOR_LAUNCH, true, null);
			//在该编辑器确定后启动工作
			work.doStart(context);
		}else{
			project.doCommitWithSendMessage(context);
		}
	}

}
