package com.sg.business.management.editor;



import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.commons.util.Utils;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.model.WorkTimeProgram;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public abstract class AbstractTypePageDelegator extends AbstractFormPageDelegator {

	private TreeViewer treeViewer;
	private WorkTimeProgram workTimeProgram;

	@Override
	public void commit(boolean onSave) {
		setDirty(false);
	}

	@Override
	public void setFocus() {

	}

	@Override
	public Composite createPageContent(IManagedForm mForm, Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		super.createPageContent(mForm, parent, input, conf);
		parent.setLayout(new FormLayout());
		// 创建容器
		final Composite pane = new Composite(parent, SWT.NONE);
		// 1.容器里面创建一棵树
		treeViewer = new TreeViewer(pane, SWT.FULL_SELECTION);
		treeViewer.getTree().setHeaderVisible(true);
		// 2.设置内容提供者
		treeViewer.setContentProvider(new OptionProvider());

		// 3.创建一个树列查看器
		TreeViewerColumn col = new TreeViewerColumn(treeViewer, SWT.LEFT);
		// 设置树列的宽度
		col.getColumn().setWidth(200);
		col.getColumn().setText("名称");
		// 设置列查看器的标签提供者
		col.setLabelProvider(new ColumnLabelProvider() {
			// 获取元素的文本
			@Override
			public String getText(Object element) {
				// element可能是列类型，也可能是列类型选项，但是无论是列类型还是列类型选项，都是DBObject
				DBObject data = (DBObject) element;
				// 返回列类型或列类型选项的名称
				return (String) data.get(WorkTimeProgram.F_DESC);
			}
		});

		// 4.从编辑器的input中获取列类型字段的值，并设置为TreeViewer的输入
		workTimeProgram = (WorkTimeProgram) input.getData();
		Object value = workTimeProgram.getValue(getFieldName());
		if (value == null) {
			value = new BasicDBList();
			workTimeProgram.setValue(getFieldName(), value);
		}
		treeViewer.setInput(value);

		// 5.添加菜单
		Menu menu = new Menu(treeViewer.getTree());
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(Messages.get().CreateDeliverable_4);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doAddType(pane.getShell());

			}

		});
		menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(Messages.get().AddOption);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doAddTypeOption(pane.getShell());

			}

		});
		menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(Messages.get().RemoveDeliverable_4);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doDeleteItem();
			}
		});
		treeViewer.getTree().setMenu(menu);
		treeViewer.expandAll();
		// 6.布局
		pane.setLayout(new FillLayout());

		return pane;

	}

	protected void doDeleteItem() {
		//判断所选节点的类型，是列类型还是列类型选项，如果没选，是不进行任何操作的
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		if(selection.isEmpty()){
			return;
		}
		//获取所选的选项中的第一个元素
		DBObject element = (DBObject) selection.getFirstElement();
		//获取所选元素的id
		ObjectId _id = (ObjectId) element.get(WorkTimeProgram.F__ID);
		DBObject type = workTimeProgram.removeTypeOrOption(_id,getFieldName());
		if(type!=null){
			treeViewer.refresh(type);
		}else{
			treeViewer.refresh();
		}
		setDirty(true);
		return;
		
	}



	protected void doAddType(Shell shell) {
		IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {
				// 1.判断newText不可为空，或空格
				if (newText.trim().isEmpty()) {
					return Messages.get().CanNotEmpty;
				}
				// 判断newText不能重复
				Object value = workTimeProgram.getValue(getFieldName());
				if (value instanceof BasicBSONList) {
					for (int i = 0; i < ((BasicBSONList) value).size(); i++) {
						DBObject option = (DBObject) ((BasicBSONList) value)
								.get(i);
						if (option.get(WorkTimeProgram.F_DESC).equals(newText)) {
							return Messages.get().DuplicateName;
						}
					}
				}
				return null;
			}
		};
		// 显示文本输入框
		InputDialog ip = new InputDialog(shell,
				Messages.get().CreateDeliverable_4, null, null, validator);
		// 打开文本输入框
		int open = ip.open();
		// 点的不是ok，就返回
		if (open != InputDialog.OK) {
			return;
		}
		// 获取文本输入框的值
		String typeName = ip.getValue();
		// 构造一个列类型对象
		workTimeProgram.appendType(typeName,getFieldName());
		// 刷新
		treeViewer.refresh();
		// 设置编辑器可以保存

		setDirty(true);
	}

	

	protected void doAddTypeOption(Shell shell) {
		// 判断是否选中一个列类型
		IStructuredSelection selection = (IStructuredSelection) treeViewer
				.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		final DBObject type = (DBObject) selection.getFirstElement();
		if (!type.containsField(WorkTimeProgram.F_TYPE_OPTIONS)) {
			return;
		}
		IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {
				// 1.判断newText不可为空，或空格
				if (newText.trim().isEmpty()) {
					return Messages.get().CanNotEmpty;
				}
				// 判断newText不能重复
				Object options =type.get(WorkTimeProgram.F_TYPE_OPTIONS);
				if (options instanceof BasicBSONList) {
					for (int i = 0; i < ((BasicBSONList) options).size(); i++) {
						DBObject option = (DBObject) ((BasicBSONList) options)
								.get(i);
						if (option.get(WorkTimeProgram.F_DESC).equals(newText)) {
							return Messages.get().DuplicateName;
						}
					}
				}
				return null;
			}
		};
		// 显示文本输入框
		InputDialog ip = new InputDialog(shell,
				Messages.get().CreateDeliverable_4, null, null, validator);
		// 打开文本输入框
		int open = ip.open();
		// 点的按钮不是ok，就返回
		if (open != InputDialog.OK) {
			return;
		}
		// 获取文本输入框的值
		String optionName = ip.getValue();
		workTimeProgram.appendOption(type, optionName);
		// 刷新
		treeViewer.refresh(type);
		Object[] elements = treeViewer.getExpandedElements();
		treeViewer.setExpandedElements(Utils.arrayAppend(elements, new Object[]{type}));
		// 设置编辑器可以保存

		setDirty(true);
	}

	

	protected abstract String getFieldName();

}
