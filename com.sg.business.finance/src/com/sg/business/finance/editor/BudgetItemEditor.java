package com.sg.business.finance.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.sg.business.model.BudgetItem;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

public class BudgetItemEditor extends EditorPart {

	private TreeViewer viewer;
	private BudgetItem root;
	private boolean dirty;

	public BudgetItemEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			root.doSave(new CurrentAccountContext());
			this.dirty = false;
			this.firePropertyChange(PROP_DIRTY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);

	}

	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.FULL_SELECTION);

		ITreeContentProvider contentProvider = new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean hasChildren(Object element) {
				return ((BudgetItem) element).hasChildren();
			}

			@Override
			public Object getParent(Object element) {
				return ((BudgetItem) element).getParent();
			}

			@Override
			public Object[] getElements(Object inputElement) {

				return (Object[]) inputElement;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return ((BudgetItem) parentElement).getChildren();
			}
		};
		/*
		 * TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.LEFT);
		 * column.getColumn().setWidth(100);
		 */
		viewer.setLabelProvider(new LabelProvider());
		viewer.setContentProvider(contentProvider);
		root = BudgetItem.GET_DEFAULT_BUDGET_ITEM();
		viewer.setInput(new Object[] { root });

		Menu menu = new Menu(viewer.getTree());
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("添加科目");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doAddItem();
			}
		});
		menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("删除科目");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doDeleteItem();
			}
		});

		menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("编辑科目");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doEditItem();
			}
		});
		viewer.getTree().setMenu(menu);
		viewer.expandAll();

		setDND();

	}

	private void setDND() {
		DragSource dragSource = new DragSource(viewer.getControl(),
				DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		DragSourceListener dragsl = new DragBudgetItemSource(viewer);
		dragSource.addDragListener(dragsl);

		DropTarget dropTarget = new DropTarget(viewer.getControl(),
				DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		DropTargetListener dropsl = new DropBudgetItemTarget(this,viewer);
		dropTarget.addDropListener(dropsl);

	}

	protected void doEditItem() {
		Shell shell = getSite().getShell();

		BudgetItem budgetItem = getSelectedBudgetItem();
		if (budgetItem == null) {
			MessageUtil
					.showMessage(shell, "预算科目", "没有选中预算科目", SWT.ICON_WARNING);
			return;
		}
		InputDialog id = new InputDialog(shell, "预算科目", "请输入科目名称", "", null);
		if (InputDialog.OK == id.open()) {
			String budgetItemName = id.getValue();
			budgetItem.editBudgetItem(budgetItemName);
			viewer.refresh(budgetItem);
			this.dirty = true;
			this.firePropertyChange(PROP_DIRTY);
		}

	}

	protected void doDeleteItem() {

		Shell shell = getSite().getShell();

		BudgetItem budgetItem = getSelectedBudgetItem();
		if (budgetItem == null) {
			MessageUtil.showMessage(shell, "预算科目", "没有选中上级预算科目",
					SWT.ICON_WARNING);
			return;
		}
		BudgetItem parent = budgetItem.getParent();
		if (parent == null) {
			MessageUtil.showMessage(shell, "预算科目", "顶级预算科目不可删除",
					SWT.ICON_WARNING);
			return;
		}
		parent.removeChild(budgetItem);
		viewer.refresh(parent);
		this.dirty = true;
		this.firePropertyChange(PROP_DIRTY);
	}

	protected void doAddItem() {
		Shell shell = getSite().getShell();

		BudgetItem parentBudgetItem = getSelectedBudgetItem();
		if (parentBudgetItem == null) {
			MessageUtil
					.showToast(shell, "预算科目", "没有选中上级预算科目", SWT.ICON_WARNING);
			return;
		}

		InputDialog id = new InputDialog(shell, "预算科目", "请输入科目名称", "", null);
		if (InputDialog.OK == id.open()) {
			String budgetItemName = id.getValue();
			parentBudgetItem.createChild(budgetItemName);
			viewer.refresh(parentBudgetItem);
			this.dirty = true;
			this.firePropertyChange(PROP_DIRTY);
		}
	}

	private BudgetItem getSelectedBudgetItem() {

		IStructuredSelection is = (IStructuredSelection) viewer.getSelection();
		if (is != null && !is.isEmpty()) {
			return (BudgetItem) is.getFirstElement();
		}
		return null;
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void setDirty(boolean b) {
		this.dirty = b;
		firePropertyChange(PROP_DIRTY);
	}

}
