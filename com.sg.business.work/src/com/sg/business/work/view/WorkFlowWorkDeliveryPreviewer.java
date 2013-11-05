package com.sg.business.work.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.commons.util.file.GridFSFilePrevieweUtil;
import com.mobnut.db.file.RemoteFile;

public class WorkFlowWorkDeliveryPreviewer extends ViewPart implements
		ISelectionListener {

	private RemoteFile selected;
	private Browser previewer;
	private GridFSFilePrevieweUtil previewUtil;
	private IWorkbenchPage page;

	@Override
	public void createPartControl(Composite parent) {
		previewer = new Browser(parent, SWT.NONE);
		previewUtil = new GridFSFilePrevieweUtil();
		page = getViewSite().getWorkbenchWindow().getActivePage()
				;
		page.addPostSelectionListener(this);
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection == null || selection.isEmpty()) {
			selected = null;
		}
		Object element = ((IStructuredSelection) selection).getFirstElement();
		if (!(element instanceof RemoteFile)) {
			return;
		}

		if (Util.equals(selected, element)) {
			return;
		}

		previewUtil.setRemoteFile((RemoteFile) element);
		setPreview((RemoteFile) element);

	}

	
	private void setPreview(RemoteFile element) {
		selected = element;
		if (!previewUtil.isPreviewAvailable()) {
			final Display display = getSite().getShell().getDisplay();
			
			
			String pathname = System.getProperty("user.dir") + "/temp";
			File file;
			try {
				file = selected.createServerFile(pathname);
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
			
			final ObjectId pvOid = new ObjectId();
			String masterfileName = file.getName();
			String previewPath = file.getParent() + "/" //$NON-NLS-1$
					+ masterfileName.substring(0, masterfileName.lastIndexOf(".")) + ".pdf"; //$NON-NLS-1$
			final File previewFile = new File(previewPath);
			Job job = previewUtil. createGeneratePDFJob(file,previewPath);
			
			job.addJobChangeListener(new JobChangeAdapter() {

				@Override
				public void done(IJobChangeEvent event) {
					display.asyncExec(new Runnable() {

						@Override
						public void run() {
							selected.setPreviewUploaded(previewFile, pvOid);
							try {
								selected.addPreview();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
							preview();
						}

					});
					super.done(event);
				}
			});
			job.schedule();
		}else{
			preview();
		}

	}
	private void preview() {
		if (previewUtil.isHTML()) {
			previewer.setText(previewUtil.getHTML());
		} else {
			previewer.setUrl(previewUtil.getURL());
		}
		
	}
	
	@Override
	public void dispose() {
		page.removePostSelectionListener(this);
		super.dispose();
	}

}
