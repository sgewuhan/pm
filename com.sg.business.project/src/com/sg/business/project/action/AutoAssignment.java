package com.sg.business.project.action;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.sg.business.model.Project;
import com.sg.business.project.ProjectActivator;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.NavigatorAction;

/**
 * @author zhonghua
 */
public class AutoAssignment extends NavigatorAction {

	public AutoAssignment() {
		setText("按角色自动指派");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_ASSIGNMENT_24));
	}

	@Override
	public void execute() throws Exception {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		int result = MessageUtil
				.showMessage(
						shell,
						"按角色自动指派",
						"将要运行按角色指派。\n工作的负责人、参与者和流程活动的执行人都将按照角色进行分配人员。\n运行完成后，将覆盖您以前的设定。\n\n选择YES继续，选择NO取消操作。\n",
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		if (result == SWT.YES) {
			// 获得项目
			final Project project = (Project) getInput().getData();
			final Display display = shell.getDisplay();
			final IContext context = new CurrentAccountContext();
			Job job = new Job("按角色自动指派"){

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("设置角色数据", IProgressMonitor.UNKNOWN);
					try {
						project.doAssignmentByRole(context);
					} catch (Exception e) {
						return new Status(Status.ERROR, ProjectActivator.PLUGIN_ID,
								Status.ERROR, "按角色自动指派出错", e);
					}
					return Status.OK_STATUS;
				}
				
			};
			
			job.addJobChangeListener(new JobChangeAdapter(){
				@Override
				public void done(IJobChangeEvent event) {
					display.syncExec(new Runnable() {
						
						@Override
						public void run() {
							getNavigator().reload(true);
							getNavigator().getViewerControl().loadColumnGroup("team");							
						}
					});
					
					super.done(event);
				}
			});
			job.setUser(true);
			job.schedule();
		}
	}
}
