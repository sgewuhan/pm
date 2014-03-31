package com.tmt.pdm.dcpdm.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
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
import com.mongodb.BasicDBList;
import com.sg.business.document.editor.DocumentWorkflowHistory;
import com.sg.business.model.Document;
import com.sg.widgets.part.SimpleSection;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.IEditorPageLayoutProvider;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.handler.DCPDMUtil;
import com.tmt.pdm.dcpdm.label.DCPDMObjectLabelProvider;
import com.tmt.pdm.dcpdm.selector.DCPDMObjectSelectWizard;

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

		Button button = new Button(bar, SWT.PUSH);
		button.setText("选择DCPDM图纸对象");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					selectDCPDMObject(input, panel.getShell());
				} catch (Exception e1) {
				}
			}
		});

		button = new Button(bar, SWT.PUSH);
		button.setText("下载全部");
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

		section.setText("文件包");
		Composite table = createFileContentTable(section, input);
		section.setClient(table);
		return section;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void selectDCPDMObject(PrimaryObjectEditorInput input, Shell shell) {
		DCPDMObjectSelectWizard wiz = new DCPDMObjectSelectWizard();

		WizardDialog wizardDialog = new WizardDialog(shell, wiz);

		int ok = wizardDialog.open();
		if (ok == WizardDialog.OK) {
			Set<String> set = wiz.getSelectedObjectOuid();
			if (set.size() > 0) {
				BasicDBList value =  new BasicDBList();
				value.addAll(set);
				Object oldValues = input.getData().getValue("pdm_ouids");
				if(oldValues instanceof List){
					List oldList = (List) oldValues;
					if(oldList.containsAll(value)&&value.containsAll(oldList)){
						return;
					}
				}
				
				input.getData().setValue("pdm_ouids", value);
				this.setDirty(true);
				try {
					loadFileValue(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
		setDirty(false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void downloadAll(PrimaryObjectEditorInput input, Shell shell)
			throws Exception {
		List pdm_ouids = (List) input.getData().getValue("pdm_ouids");
		String zipFileName = (String) input.getData().getValue(
				Document.F_DOCUMENT_NUMBER);
		if (pdm_ouids != null) {
			ArrayList fileList = new ArrayList();
			for (int i = 0; i < pdm_ouids.size(); i++) {
				ArrayList fl = Starter.dos.listFile((String) pdm_ouids.get(i));
				fileList.addAll(fl);
			}
			if (fileList != null) {
				DCPDMUtil.download(zipFileName, fileList, shell);
			}
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

			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object inputElement) {
				return ((List) inputElement).toArray();
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
			@SuppressWarnings("rawtypes")
			List pdm_ouids = (List) input.getData().getValue("pdm_ouids");
			loadFileValue(pdm_ouids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}

	@SuppressWarnings("rawtypes")
	private void loadFileValue(List pdm_ouids) throws Exception {
		if (pdm_ouids != null) {
			fileViewer.setInput(pdm_ouids);
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
