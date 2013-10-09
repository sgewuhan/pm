package com.sg.business.management.editor.page;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.BudgetItem;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class BugetTemplatePage implements IPageDelegator, IFormPart {

	private TreeViewer viewer;
	private BudgetItem templateRoot;
	private boolean isDirty;
	private IManagedForm form;
	private PrimaryObjectEditorInput input;

	public BugetTemplatePage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		this.input = input;

		PrimaryObject pjTemplate = input.getData();

		// 创建根据默认的预算科目创建一棵树并具有勾选框。
		BudgetItem defaultRoot = BudgetItem.GET_DEFAULT_BUDGET_ITEM();
		Object templateRootId = pjTemplate
				.getValue(ProjectTemplate.F_BUDGET_ID);
		if (!(templateRootId instanceof ObjectId)) {
			// 如果没有预算定义则从默认的预算复制一份
			templateRoot = BudgetItem.COPY_DEFAULT_BUDGET_ITEM();
		} else {
			templateRoot = ModelService.createModelObject(BudgetItem.class,
					(ObjectId) templateRootId);
			if (templateRoot == null) {
				templateRoot = BudgetItem.COPY_DEFAULT_BUDGET_ITEM();
			}
		}

		viewer = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.CHECK);
		viewer.setLabelProvider(new LabelProvider());
		viewer.setContentProvider(new BudgetItemTreeContentProvider());
		viewer.setInput(defaultRoot.getChildren());
		viewer.expandAll();
		// 设置勾选状态
		setCheckedItem(viewer.getTree().getItems(), templateRoot.getChildren());
		// 添加勾选侦听器，勾选下级后上级自动被勾选
		setCheckListner();
		return (Composite) viewer.getControl();
	}

	private void setCheckListner() {
		viewer.getTree().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeItem = (TreeItem) e.item;
				if (treeItem.getChecked()) {// 上级都需要选中
					setParentCheck(treeItem);
				} else {// 下级都必须不选
					setChildrenCheck(treeItem);
				}
				isDirty = true;
				form.dirtyStateChanged();
			}

			private void setChildrenCheck(TreeItem treeItem) {
				int itemCount = treeItem.getItemCount();
				TreeItem[] children = treeItem.getItems();
				if (itemCount > 0) {
					for (int i = 0; i < children.length; i++) {
						children[i].setChecked(false);
						setChildrenCheck(children[i]);
					}
				}
			}

			private void setParentCheck(TreeItem treeItem) {
				TreeItem pi = treeItem.getParentItem();
				if (pi instanceof TreeItem) {
					pi.setChecked(true);
					setParentCheck(pi);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void setCheckedItem(TreeItem[] childrenItems, BudgetItem[] children) {
		for (int i = 0; i < childrenItems.length; i++) {
			BudgetItem bi = (BudgetItem) childrenItems[i].getData();
			for (int j = 0; j < children.length; j++) {
				if (bi.getDesc().equals(children[j].getDesc())) {// 如果名称相同
					childrenItems[i].setChecked(true);// 显示为勾选
					// 检查下级
					if (childrenItems[i].getItemCount() > 0) {
						setCheckedItem(childrenItems[i].getItems(),
								children[j].getChildren());
					}
					break;
				}
			}
		}
	}

	@Override
	public IFormPart getFormPart() {
		return this;
	}

	@Override
	public void initialize(IManagedForm form) {
		this.form = form;
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public void commit(boolean onSave) {
		if (!onSave) {
			// 将树的选中情况提交值到input
			BasicDBList childrenData = new BasicDBList();
			setDataFromTreeItems(viewer.getTree().getItems(), childrenData);
			templateRoot.setValue(BudgetItem.F_CHILDREN, childrenData);
			try {
				templateRoot.doSave(new CurrentAccountContext());
				PrimaryObject pjTemplate = input.getData();
				pjTemplate.setValue(ProjectTemplate.F_BUDGET_ID,
						templateRoot.get_id());
			} catch (Exception e) {
			}
		} else {
			isDirty = false;
			form.dirtyStateChanged();
		}
	}

	private void setDataFromTreeItems(TreeItem[] treeItems,
			BasicDBList budgetItemList) {
		if (treeItems != null && treeItems.length > 0) {
			for (int i = 0; i < treeItems.length; i++) {
				if (!treeItems[i].getChecked()) {
					continue;
				}
				BudgetItem ti = (BudgetItem) treeItems[i].getData();
				DBObject bi = new BasicDBObject();
				bi.put(BudgetItem.F__ID, ti.get_id());
				bi.put(BudgetItem.F_DESC, ti.getDesc());
				bi.put(BudgetItem.F_DESC_EN, ti.getDesc_e());
				if (treeItems[i].getItemCount() > 0) {
					BasicDBList childrenBudgetItemList = new BasicDBList();
					setDataFromTreeItems(treeItems[i].getItems(),
							childrenBudgetItemList);
					bi.put(BudgetItem.F_CHILDREN, childrenBudgetItemList);
				}
				budgetItemList.add(bi);
			}
		}
	}

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	@Override
	public void setFocus() {
		viewer.getTree().setFocus();
	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {
	}

	@Override
	public boolean canRefresh() {
		return false;
	}

}
