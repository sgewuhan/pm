package com.tmt.pdm.dcpdm.selector;

import java.util.Iterator;
import java.util.Set;

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
import org.eclipse.swt.widgets.TreeItem;

import com.mobnut.commons.util.Utils;
import com.tmt.pdm.client.Starter;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.dos.DOSChangeable;

public class AdvanceSearchPage extends WizardPage {

	private TreeViewer viewer;
	private DCPDMObjectSelectWizard wizard;

	protected AdvanceSearchPage(DCPDMObjectSelectWizard wizard) {
		super("高级选择");
		this.wizard = wizard;
		setTitle("选择相关的图文档");
	}

	@Override
	public void createControl(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayout(new FormLayout());
		final Tree tree = createTree(panel);

		Button selectAll = new Button(panel, SWT.PUSH);
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

		Button clearAll = new Button(panel, SWT.PUSH);
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

		updateMessage();

		setPageComplete(true);
		setControl(panel);
	}

	private Tree createTree(Composite parent) {
		viewer = new TreeViewer(parent, SWT.CHECK);
		Tree tree = viewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem ti = (TreeItem) e.item;
				select(new TreeItem[] { ti }, false, ti.getChecked(), true);
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

		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		return tree;
	}

	protected void clearAll() {
		Tree tree = viewer.getTree();
		TreeItem[] items = tree.getItems();
		select(items, true, false, true);
	}

	private void select(TreeItem[] items, boolean selectItem, boolean check,
			boolean cascade) {
		for (int i = 0; i < items.length; i++) {
			if (selectItem) {
				items[i].setChecked(check);
			}
			if (cascade) {
				select(items[i].getItems(), selectItem, check, cascade);
			}
		}
		updateSelection();
		updateMessage();
	}

	private void updateSelection() {
		Tree tree = viewer.getTree();
		TreeItem[] items = tree.getItems();
		updateSelectionFromItems(items);
	}

	private void updateSelectionFromItems(TreeItem[] items) {
		for (int i = 0; i < items.length; i++) {
			DOSChangeable dos = (DOSChangeable) items[i].getData();
			DCPDMObjectSelectWizard wizard = getWizard();
			if (items[i].getChecked()) {
				wizard.selectedObjectOuid.add((String) dos.get("ouid"));
			} else {
				wizard.selectedObjectOuid.remove(dos.get("ouid"));
			}
			updateSelectionFromItems(items[i].getItems());
		}
	}

	private void updateMessage() {
		DCPDMObjectSelectWizard wizard = getWizard();
		if (wizard == null || wizard.selectedObjectOuid.isEmpty()) {
			setMessage("请选择DCPDM的图文档对象");
		} else {
			Set<String> selected = wizard.selectedObjectOuid;
			StringBuffer sb = new StringBuffer();
			sb.append("选择了:");
			Iterator<String> iter = selected.iterator();
			while (iter.hasNext()) {
				String ouid = (String) iter.next();
				try {
					DOSChangeable dos = Starter.dos.get(ouid);
					sb.append(dos.get("md$number"));
					sb.append(" ");
				} catch (Exception e) {
				}
			}
			setMessage(sb.toString());
		}
	}

	protected void selectAll() {
		Tree tree = viewer.getTree();
		TreeItem[] items = tree.getItems();
		select(items, true, true, true);
	}

	protected void setInput(Object input) {
		if (input instanceof Object[]) {
			Object[] _input = (Object[]) input;
			Object[] result = new Object[_input.length];
			for (int i = 0; i < result.length; i++) {
				String ouid = (String) _input[i];
				try {
					result[i] = Starter.dos.get(ouid);
					getWizard().selectedObjectOuid.add(ouid);
				} catch (Exception e) {
				}
			}
			viewer.setInput(result);
		} else {
			viewer.setInput(new Object[0]);
		}
		Tree tree = viewer.getTree();
		TreeItem[] root = tree.getItems();
		for (int i = 0; i < root.length; i++) {
			root[i].setChecked(true);
		}
		updateMessage();
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
		return wizard;
	}

}