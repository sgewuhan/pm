package com.tmt.pdm.dcpdm.selector;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.mobnut.commons.util.Utils;
import com.tmt.pdm.client.Starter;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.dos.DOSChangeable;

public class AdvanceSearchPage extends WizardPage {

	private TreeViewer viewer;

	protected AdvanceSearchPage() {
		super("高级选择");
		setTitle("选择相关的图文档");
		setMessage("请选择相关的其他DCPDM对象");
	}

	@Override
	public void createControl(Composite parent) {
		Composite pane = new Composite(parent, SWT.NONE);
		pane.setLayout(new FormLayout());
		viewer = new TreeViewer(pane, SWT.CHECK);
		final Tree tree = viewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(e.item);
			}
		});
		viewer.setContentProvider(new RelationDCDPMObjectContentProvider(
				getWizard().allContainers));

		TreeColumn col = createTitleColumn();
		col = createColumn("md$number", "编号");
		col.setWidth(120);
		col = createColumn("md$description", "名称");
		col.setWidth(200);
		col = createColumn("md$user", "创建人");
		col.setWidth(60);

		Button selectAll = new Button(pane, SWT.PUSH);
		selectAll.setText("选择全部");
		selectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAll();
			}
		});
		FormData fd = new FormData();
		selectAll.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100, -10);
		fd.height = 24;

		Button clearAll = new Button(pane, SWT.PUSH);
		clearAll.setText("清除选择");
		clearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearAll();
			}
		});
		fd = new FormData();
		clearAll.setLayoutData(fd);
		fd.left = new FormAttachment(selectAll, 10);
		fd.bottom = new FormAttachment(100, -10);
		fd.height = 24;

		fd = new FormData();
		tree.setLayoutData(fd);
		fd.left = new FormAttachment(0, 10);
		fd.top = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, 10);
		fd.bottom = new FormAttachment(selectAll, -4);

		setPageComplete(true);
		setControl(pane);
	}

	protected void clearAll() {
		// TODO Auto-generated method stub

	}

	protected void selectAll() {
		// TODO Auto-generated method stub

	}

	protected void setInput(Object input) {
		if (input instanceof Object[]) {
			Object[] _input = (Object[]) input;
			Object[] result = new Object[_input.length];
			for (int i = 0; i < result.length; i++) {
				String ouid = (String) ((ArrayList<?>) _input[i]).get(0);
				try {
					result[i] = Starter.dos.get(ouid);
				} catch (IIPRequestException e) {
				}
			}
			viewer.setInput(result);
			return;
		}
		viewer.setInput(input);
	}

	private TreeColumn createColumn(final String key, String name) {
		TreeViewerColumn col = new TreeViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(name);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DOSChangeable dos = (DOSChangeable) element;
				return Utils.getStringValue(dos.get(key));
			}
		});
		return col.getColumn();
	}

	private TreeColumn createTitleColumn() {
		TreeViewerColumn col = new TreeViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setWidth(120);
		col.getColumn().setText("类型");
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DOSChangeable dos = (DOSChangeable) element;
				Object ouid = dos.get("ouid");
				try {
					if (ouid != null) {
						String classOuid;
						classOuid = Starter.dos.getClassOuid((String) ouid);
						return getWizard().ouid_name.get(classOuid);
					}
				} catch (IIPRequestException e) {
				}

				return "未知类型";
			}
		});
		return col.getColumn();
	}

	@Override
	public DCPDMObjectSelectWizard getWizard() {
		return (DCPDMObjectSelectWizard) super.getWizard();
	}

}