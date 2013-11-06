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

import com.mobnut.commons.util.Office2PDFJob;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.commons.util.file.GridFSFilePrevieweUtil;
import com.mobnut.commons.util.file.OSServerFile;
import com.mobnut.db.file.GridServerFile;
import com.mobnut.db.file.IServerFile;
import com.mobnut.db.file.RemoteFile;

public class WorkFlowWorkDeliveryPreviewer extends ViewPart implements
		ISelectionListener {

	private IServerFile selected;
	private Browser previewer;
	private GridFSFilePrevieweUtil previewUtil;
	private IWorkbenchPage page;

	@Override
	public void createPartControl(Composite parent) {
		previewer = new Browser(parent, SWT.NONE);
		previewUtil = new GridFSFilePrevieweUtil();
		page = getViewSite().getWorkbenchWindow().getActivePage();
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
		if (!(element instanceof IServerFile)) {
			return;
		}

		if (Util.equals(selected, element)) {
			return;
		}
		setPreview((IServerFile) element);

	}

	private void setPreview(IServerFile element) {
		selected = element;
		if (selected instanceof GridServerFile) {
			createPreview(((GridServerFile) selected).getRemoteFile());
		} else if (selected instanceof OSServerFile) {
			createPreview((OSServerFile) selected);
		}

	}

	private void createPreview(final OSServerFile osfile) {
		final Display display = getSite().getShell().getDisplay();

		String pathname = System.getProperty("user.dir") + "/temp/"
				+ Utils.getRandomString(8, true);
		File folder = new File(pathname);
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}
		String fileName = osfile.getFileName();
		String previewPath = pathname + "/" + fileName.substring(0,
				fileName.lastIndexOf(".")) + ".pdf";

		int fileType = FileUtil.getFileType(fileName);

		if (fileType == FileUtil.FILETYPE_OFFICE_FILE) {
			Office2PDFJob job = new Office2PDFJob();
			final File serverFile = osfile.getServerFile();
			job.setSourceFile(serverFile);
			final File previewFile = new File(previewPath);
			job.setTargetFile(previewFile);
			job.setUser(true);

			job.addJobChangeListener(new JobChangeAdapter() {

				@Override
				public void done(IJobChangeEvent event) {
					display.asyncExec(new Runnable() {

						@Override
						public void run() {
							previewOSFile(previewFile);
						}

					});
					super.done(event);
				}
			});
			job.schedule();

		}

	}

	private void createPreview(final RemoteFile remoteFile) {
		previewUtil.setRemoteFile(remoteFile);
		if (!previewUtil.isPreviewAvailable()) {
			final Display display = getSite().getShell().getDisplay();

			String pathname = System.getProperty("user.dir") + "/temp";
			File file;
			try {
				file = remoteFile.createServerFile(pathname);
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}

			final ObjectId pvOid = new ObjectId();
			String masterfileName = file.getName();
			String previewPath = file.getParent()
					+ "/" //$NON-NLS-1$
					+ masterfileName.substring(0,
							masterfileName.lastIndexOf(".")) + ".pdf"; //$NON-NLS-1$
			final File previewFile = new File(previewPath);
			Job job = previewUtil.createGeneratePDFJob(file, previewPath);

			job.addJobChangeListener(new JobChangeAdapter() {

				@Override
				public void done(IJobChangeEvent event) {
					display.asyncExec(new Runnable() {

						@Override
						public void run() {
							remoteFile.setPreviewUploaded(previewFile, pvOid);
							try {
								remoteFile.addPreview();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
							previewRemoteFile();
						}

					});
					super.done(event);
				}
			});
			job.schedule();
		} else {
			previewRemoteFile();
		}
	}

	private void previewOSFile(File previewFile) {
		StringBuffer url = new StringBuffer();
		url.append("/pdf/open?"); //$NON-NLS-1$
		String filePath = previewFile.getPath();
		url.append("path="); //$NON-NLS-1$
		url.append(filePath);
		previewer.setUrl(url.toString());
	}

	private void previewRemoteFile() {
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
