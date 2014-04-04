package com.tmt.pdm.dcpdm.editor;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.Section;

import com.mobnut.commons.html.HtmlUtil;
import com.sg.business.document.editor.DocumentWorkflowHistory;
import com.sg.business.model.Document;
import com.sg.widgets.part.SimpleSection;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.IEditorPageLayoutProvider;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.handler.DCPDMUtil;
import com.tmt.pdm.dcpdm.label.DCPDMObjectLabelProvider;

import dyna.framework.iip.IIPRequestException;

public class DrawingPackagePage extends DocumentWorkflowHistory implements
		IEditorPageLayoutProvider {

	private TreeViewer fileViewer;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		setFormInput(input);
		Composite panel = new Composite(parent, SWT.NONE);
		Composite flow = super.createPageContent(panel, input, conf);
		Composite file = createFileContent(panel, input);

		panel.setLayout(new FormLayout());
		FormData fd = new FormData();
		file.setLayoutData(fd);
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment();
		fd.width = 400;
		fd.bottom = new FormAttachment(100);

		fd = new FormData();
		flow.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment(file);
		fd.width = 400;
		fd.bottom = new FormAttachment(100);

		return panel;
	}

	private Composite createFileContent(final Composite panel,
			final PrimaryObjectEditorInput input) {
		SimpleSection section = new SimpleSection(panel, Section.EXPANDED
				| Section.SHORT_TITLE_BAR | Section.TWISTIE
				| Section.DESCRIPTION);
		section.setDescription("请使用DCPDM系统或DCPDM Pro/E插件上传图纸");
		section.setFont(font);
		Composite bar = new Composite(section, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.fill = true;
		layout.marginBottom = 0;
		bar.setLayout(layout);

		// Button
		// button = new Button(bar, SWT.PUSH);
		// button.setText("选择...");
		// button.setData(RWT.CUSTOM_VARIANT,"small_white");
		// button.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// try {
		// selectDCPDMObject(input, panel.getShell());
		// } catch (Exception e1) {
		// }
		// }
		// });

		Button button = new Button(bar, SWT.PUSH);
		button.setText("下载全部");
		button.setData(RWT.CUSTOM_VARIANT, "small_white");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					downloadAll(input, panel.getShell());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		section.setTextClient(bar);

		section.setText("DCPDM图纸对象");
		Composite table = createFileContentTable(section, input);
		section.setClient(table);
		return section;
	}

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
		setDirty(false);
	}

	protected void downloadAll(PrimaryObjectEditorInput input, Shell shell)
			throws Exception {
		String pdm_ouid = (String) input.getData().getValue("pdm_ouid");
		String zipFileName = (String) input.getData().getValue(
				Document.F_DOCUMENT_NUMBER);
		if (pdm_ouid != null) {
			ArrayList<Map<String, Object>> fileList = DCPDMUtil
					.getFileCascade(pdm_ouid);
			DCPDMUtil.download(zipFileName, fileList, shell);
		}
	}

	private Composite createFileContentTable(SimpleSection section,
			PrimaryObjectEditorInput input) {
		fileViewer = new TreeViewer(section, SWT.MULTI);
		Tree table = fileViewer.getTree();
		HtmlUtil.enableMarkup(table);
		table.setData(RWT.CUSTOM_ITEM_HEIGHT, 36);
		TreeViewerColumn col = new TreeViewerColumn(fileViewer, SWT.LEFT);
		col.getColumn().setWidth(300);
		col.setLabelProvider(new DCPDMObjectLabelProvider());
		fileViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean hasChildren(Object element) {
				return true;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return ((Object[]) inputElement);
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof String) {
					try {
						@SuppressWarnings("rawtypes")
						ArrayList list = Starter.dos
								.listFile((String) parentElement);
						if (list != null) {
							return list.toArray();
						}
					} catch (IIPRequestException e) {
					}
				}
				return new Object[0];
			}
		});

		fileViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		try {
			String pdm_ouid = (String) input.getData().getValue("pdm_ouid");
			loadFileValue(pdm_ouid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}

	@SuppressWarnings("rawtypes")
	private void loadFileValue(String pdm_ouid) throws Exception {
		if (pdm_ouid != null) {
			fileViewer.setInput(new String[] { pdm_ouid });
			return;
		}
		fileViewer.setInput(new ArrayList());
	}

	@Override
	public boolean createBody() {
		return true;
	}

	@Override
	public IEditorPageLayoutProvider getPageLayout() {
		return this;
	}

	@Override
	public void layout(Control body, Control customerPage) {
		FormData fd = new FormData();
		body.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.width = 400;
		fd.bottom = new FormAttachment(100);

		fd = new FormData();
		customerPage.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment(body, 10);
		fd.bottom = new FormAttachment(100);
		fd.width = 800;

	}
}
