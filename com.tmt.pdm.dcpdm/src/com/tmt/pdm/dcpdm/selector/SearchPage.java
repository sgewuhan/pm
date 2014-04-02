package com.tmt.pdm.dcpdm.selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.mobnut.commons.util.Utils;
import com.sg.widgets.MessageUtil;
import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.nls.Messages;

import dyna.framework.iip.IIPRequestException;

public class SearchPage extends WizardPage implements ISelectionChangedListener {
	private ArrayList<String> fieldlist;

	private DCPDMObjectSelectWizard wizard;

	protected SearchPage(DCPDMObjectSelectWizard wizard) {
		super("查找DCPDM图文档");
		setTitle("输入编号或名称查找DCPDM图文档");
		setMessage("输入时无需加*，大小写有别，使用西文逗号分隔多条件。查询结果可以多选。\n例如:输入ABC将检索编号或名称包含ABC的DCPDM图文档。");
		this.wizard = wizard;

		fieldlist = new ArrayList<String>();
		fieldlist.add("80001a79");// 编号 //$NON-NLS-1$
		fieldlist.add("86054a45");// 名称 //$NON-NLS-1$
		fieldlist.add("80001a7a");// 描述 //$NON-NLS-1$
		fieldlist.add("80001aac");// 创建人 //$NON-NLS-1$

	}

	private static final int WIDTH = 400;

	private TableViewer viewer;

	private TableColumn createColumn(final int i, String name) {
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(name);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ArrayList<?> ar = ((ArrayList<?>) element);
				Object value = ar.get(i);
				return Utils.getStringValue(value);
			}
		});
		return col.getColumn();
	}

	private TableColumn createTitleColumn() {
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setWidth(120);
		col.getColumn().setText("类型");
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ArrayList<?> ar = ((ArrayList<?>) element);
				String value = (String) ar.get(0);
				try {
					String classOuid = Starter.dos.getClassOuid(value);
					return getWizard().ouid_name.get(classOuid);
				} catch (IIPRequestException e) {
				}
				return "未知类型";
			}
		});
		return col.getColumn();
	}

	protected void searchData(final String input) {
		final ArrayList<?> result = new ArrayList<>();
		final String[] keys = input.split(",");
		if (input.isEmpty()) {
			MessageUtil.showToast("请输入条件", SWT.ICON_INFORMATION);
			return;
		}

		Job job = new Job("检索PDM数据") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				Iterator<String> iter = getWizard().docContainers.iterator();
				while (iter.hasNext()) {
					for (int index = 0; index < keys.length; index++) {
						String classOuid = iter.next();
						appendResult(result, keys[index], classOuid, "80001a79");
						appendResult(result, keys[index], classOuid, "86054a45");
						appendResult(result, keys[index], classOuid, "80001a7a");
					}
				}
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();

		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				Shell shell = getShell();
				if (shell.isDisposed() || shell == null) {
					return;
				}
				Display display = shell.getDisplay();
				if (display.isDisposed()) {
					return;
				}
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						viewer.setInput(result);
					}
				});
			}
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void appendResult(final ArrayList<?> result, String key,
			String classOuid, String fieldName) {
		try {
			HashMap<String, String> condition = new HashMap<String, String>();
			condition.put(fieldName, "*" + key + "*"); //$NON-NLS-1$ //$NON-NLS-2$ 
			condition.put("version.condition.type", "wip"); //$NON-NLS-1$ //$NON-NLS-2$
			ArrayList r = Starter.dos.list(classOuid, fieldlist, condition);
			if (r != null) {
				result.addAll(r);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public DCPDMObjectSelectWizard getWizard() {
		return wizard;
	}

	@Override
	public void createControl(Composite parent) {
		Composite pane = new Composite(parent, SWT.NONE);
		pane.setLayout(new FormLayout());

		final Text inputText = new Text(pane, SWT.BORDER);
		Button goSearch = new Button(pane, SWT.PUSH);
		goSearch.setText(Messages.get().PDMObjectSelector_1);
		goSearch.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				searchData(inputText.getText().trim());
			}
		});

		FormData fd = new FormData();
		goSearch.setLayoutData(fd);
		fd.top = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, -10);

		fd = new FormData();
		inputText.setLayoutData(fd);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(goSearch, -10);
		fd.top = new FormAttachment(0, 10);

		viewer = new TableViewer(pane, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.VIRTUAL | SWT.MULTI);
		Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.addSelectionChangedListener(this);
		TableColumn col = createTitleColumn();
		col = createColumn(1, "编号");
		col.setWidth(120);
		col = createColumn(2, "名称");
		col.setWidth(120);
		col = createColumn(3, "描述");
		col.setWidth(120);
		col = createColumn(4, "创建人");
		col.setWidth(60);
		Control control = viewer.getControl();
		FormData fd1 = new FormData();
		control.setLayoutData(fd1);
		fd1.left = new FormAttachment(0, 10);
		fd1.right = new FormAttachment(100, -10);
		fd1.top = new FormAttachment(inputText, 10);
		fd1.bottom = new FormAttachment(100, -10);
		fd1.width = WIDTH;
		fd1.height = 200;
		inputText.setFocus();
		setControl(pane);
		setPageComplete(viewer.getSelection() != null
				&& !viewer.getSelection().isEmpty());
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		getWizard().setSelection(viewer.getSelection());
		setPageComplete(selection != null && !selection.isEmpty());
	}

}
