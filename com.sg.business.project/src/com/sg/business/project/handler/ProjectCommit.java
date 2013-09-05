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
	 * �ύ��Ŀ�ƻ�<br/>
	 * �ж���Ŀ�ƻ��Ƿ������ύ���̣�����������ύ���̣�ʹ�����̽����ύ ��<br>
	 * ���û�ж������̣�ֱ�ӷ�����Ϣ��<br/>

	 * 
	 * @param context
	 * @throws Exception
	 */
	public void doCommit(Project project,IContext context) throws Exception {
		if(project.isCommitWorkflowActivate()){
			Work work = project.makeWorkflowCommitableWork(null,context);
			//�򿪶�����������������ĶԻ���༭��
			DataObjectWizard.open(work, Work.EDITOR_LAUNCH, true, null);
			//�ڸñ༭��ȷ������������
			work.doStart(context);
		}else{
			project.doCommitWithSendMessage(context);
		}
	}

}
