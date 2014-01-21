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
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.NavigatorAction;

/**
 * @author zhonghua
 */
public class AutoAssignment extends NavigatorAction {

	public AutoAssignment() {
		setText(Messages.get().AutoAssignment_0);
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
						Messages.get().AutoAssignment_1,
						Messages.get().AutoAssignment_2,
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		if (result == SWT.YES) {
			// 获得项目
			final Project project = (Project) getInput().getData();
			final Display display = shell.getDisplay();
			final IContext context = new CurrentAccountContext();
			Job job = new Job(Messages.get().AutoAssignment_3){

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(Messages.get().AutoAssignment_4, IProgressMonitor.UNKNOWN);
					try {
						project.doAssignmentByRole(context);
					} catch (Exception e) {
						return new Status(Status.ERROR, ProjectActivator.PLUGIN_ID,
								Status.ERROR, Messages.get().AutoAssignment_5, e);
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
							getNavigator().getViewerControl().loadColumnGroup("team");							 //$NON-NLS-1$
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
