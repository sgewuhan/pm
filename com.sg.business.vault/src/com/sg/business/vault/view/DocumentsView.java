package com.sg.business.vault.view;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mobnut.db.file.RemoteFileSet;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.fields.FileSetPreviewer;
import com.sg.widgets.part.view.AccountSensitiveTableView;

public class DocumentsView extends AccountSensitiveTableView implements
		ISelectionChangedListener {

	boolean showPreview = false;
	private SashForm content;
	private FileSetPreviewer previewPanel;

	@Override
	public void createPartControl(Composite parent) {
		content = new SashForm(parent, SWT.VERTICAL);
		content.SASH_WIDTH = 2;

		Composite tablePanel = new Composite(content, SWT.NONE);
		super.createPartControl(tablePanel);

		createPreviewPanel(content);
		content.setWeights(new int[] { 1, 0 });

	}

	private void createPreviewPanel(SashForm content) {
		Composite panel = new Composite(content,SWT.NONE);
		panel.setLayout(new FormLayout());
		
		Label line = new Label(panel,SWT.NONE);
		Color sepColor = Widgets.getColor(content.getDisplay(), 192, 192, 192);
		line.setBackground(sepColor);
		FormData fd = new FormData();
		line.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment();
		fd.height = 1;
		fd.right = new FormAttachment(100,0);
		
		previewPanel =	new FileSetPreviewer(panel);
		fd = new FormData();
		previewPanel.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment(line,3);
		fd.right = new FormAttachment(100,0);
		fd.bottom = new FormAttachment(100,0);
	}

	public void switchPreview() {
		showPreview = !showPreview;
		if (showPreview) {
			navi.getViewer().addPostSelectionChangedListener(this);
			content.setWeights(new int[] { 6, 4 });
		} else {
			navi.getViewer().removePostSelectionChangedListener(this);
			content.setWeights(new int[] { 1, 0 });
		}

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (!selection.isEmpty()) {
			Document po = (Document) selection.getFirstElement();
			RemoteFileSet fs = po.getVault();
			previewPanel.setFileset(fs);
		} else {
			previewPanel.setFileset(new RemoteFileSet(IModelConstants.DB,
					IModelConstants.C_DOCUMENT));
		}
	}

}
