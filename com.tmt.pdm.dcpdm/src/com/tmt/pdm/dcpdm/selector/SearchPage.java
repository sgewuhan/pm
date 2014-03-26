package com.tmt.pdm.dcpdm.selector;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.mobnut.commons.util.Utils;
import com.tmt.pdm.dcpdm.nls.Messages;

public class SearchPage extends WizardPage {
	protected SearchPage() {
		super("请选择DCPDM的图文档数据类型");
		setTitle("请选择DCPDM的图文档数据类型");
	}

	private static final int WIDTH = 400;

	private TableViewer viewer;
	private IStructuredSelection selection;

	private void createColumn(final int i, String name) {
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setWidth(80);
		col.getColumn().setText(name);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ArrayList<?> ar = ((ArrayList<?>) element);
				Object value = ar.get(i);
				return Utils.getStringValue(value);
			}
		});
	}

	protected void searchData(final String input) {

		// final ArrayList result = new ArrayList();
		//
		// Job job = new Job("检索PDM数据") {
		// @Override
		// protected IStatus run(IProgressMonitor monitor) {
		// for (int i = 0; i < docContainers.size(); i++) {
		// ArrayList fieldlist = new ArrayList();
		//					fieldlist.add("80001a79");// 编号 //$NON-NLS-1$
		//					fieldlist.add("80001a7a");// 名称 //$NON-NLS-1$
		// fieldlist.add(Messages.get().PDMObjectSelector_11);// 版本
		// // fieldlist.add("86366c51");// 阶段
		//					fieldlist.add("80001aac");// 创建时间 //$NON-NLS-1$
		//					fieldlist.add("80001a7c");// 创建人 //$NON-NLS-1$
		//
		// HashMap condition = new HashMap();
		//					condition.put("80001a79", "*" + input + "*"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//					condition.put("version.condition.type", "wip"); //$NON-NLS-1$ //$NON-NLS-2$
		// try {
		// ArrayList r = Starter.dos.list(
		// (String) docContainers.toArray()[i], fieldlist,
		// condition);
		// if (r != null) {
		// result.addAll(r);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// return Status.OK_STATUS;
		// }
		// };
		// job.setUser(true);
		// job.schedule();
		//
		// job.addJobChangeListener(new JobChangeAdapter() {
		// @Override
		// public void done(IJobChangeEvent event) {
		// getShell().getDisplay().asyncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		// viewer.setInput(result);
		// }
		// });
		// }
		// });

	}

	@SuppressWarnings("rawtypes")
	public String[] getSelection() {
		if (selection == null) {
			return new String[0];
		} else {
			String[] result = new String[selection.size()];
			Object[] ar = selection.toArray();
			for (int i = 0; i < result.length; i++) {
				result[i] = (String) ((ArrayList) ar[i]).get(0);
			}
			return result;
		}
	}

	@Override
	public DCPDMObjectSelector2 getWizard() {
		return (DCPDMObjectSelector2) super.getWizard();
	}

	@Override
	public void createControl(Composite parent) {
		Composite pane = new Composite(parent, SWT.NONE);
		pane.setLayout(new FormLayout());
		Label label = new Label(pane, SWT.NONE);
		label.setText("请输入编号或名称：\n无需加*,例如:输入ABC将检索编号或名称包含ABC的DCPDM图文档");
		FormData fd = new FormData();
		label.setLayoutData(fd);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, -10);
		fd.top = new FormAttachment(0, 10);
		fd.width = WIDTH;

		final Text inputText = new Text(pane, SWT.BORDER | SWT.ICON_SEARCH);
		Button goSearch = new Button(pane, SWT.PUSH);
		goSearch.setText(Messages.get().PDMObjectSelector_1);
		goSearch.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				searchData(inputText.getText());
			}
		});

		fd = new FormData();
		goSearch.setLayoutData(fd);
		fd.top = new FormAttachment(label, 10);
		fd.right = new FormAttachment(100, -10);

		fd = new FormData();
		inputText.setLayoutData(fd);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(goSearch, -10);
		fd.top = new FormAttachment(label, 10);

		viewer = new TableViewer(pane, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.VIRTUAL | SWT.MULTI);
		Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		viewer.setContentProvider(ArrayContentProvider.getInstance());

		// 编号列
		createColumn(1, "编号");
		createColumn(2, "名称");
		createColumn(4, "创建时间");
		createColumn(5, "创建人");

		Control control = viewer.getControl();
		FormData fd1 = new FormData();
		control.setLayoutData(fd1);
		fd1.left = new FormAttachment(0, 10);
		fd1.right = new FormAttachment(100, -10);
		fd1.top = new FormAttachment(inputText, 10);
		fd1.bottom = new FormAttachment(100, -10);
		fd1.width = WIDTH;
		fd1.height = 200;

		setControl(pane);
	}

}
