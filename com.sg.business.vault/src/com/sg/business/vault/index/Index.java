package com.sg.business.vault.index;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.mobnut.admin.IFunctionExcutable;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.MessageUtil;

public class Index implements IFunctionExcutable {

	public Index() {
	}

	@Override
	public void run() {
		final Display display = Display.getCurrent();
		Job job = new IndexJob(IModelConstants.DB,IModelConstants.C_DOCUMENT,Document.F_VAULT);
		job.setUser(true);
		job.addJobChangeListener(new JobChangeAdapter(){

			@Override
			public void done(IJobChangeEvent event) {
				display.asyncExec(new Runnable(){

					@Override
					public void run() {
						MessageUtil.showToast(null,"建立索引","索引建立完成",SWT.ICON_INFORMATION);
					}
					
				});
				
			}
			
		});
		job.schedule();
	}

}
