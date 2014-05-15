package com.sg.business.management.editor;



import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.WorkTimeProgram;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class ColumnTypePageDelegator extends AbstractFormPageDelegator {

	private class OptionProvider implements ITreeContentProvider {

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ((BasicDBList) inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			DBObject parent = ((DBObject) parentElement);
			Object options = parent.get(WorkTimeProgram.F_TYPE_OPTIONS);
			if (options instanceof BasicBSONList) {
				return ((BasicBSONList) options).toArray();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

	}

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
		// ��������
		final Composite pane = new Composite(parent, SWT.NONE);
		// 1.�������洴��һ����
		treeViewer = new TreeViewer(pane, SWT.FULL_SELECTION);
		treeViewer.getTree().setHeaderVisible(true);
		// 2.���������ṩ��
		treeViewer.setContentProvider(new OptionProvider());

		// 3.����һ�����в鿴��
		TreeViewerColumn col = new TreeViewerColumn(treeViewer, SWT.LEFT);
		// �������еĿ��
		col.getColumn().setWidth(200);
		col.getColumn().setText("����");
		// �����в鿴���ı�ǩ�ṩ��
		col.setLabelProvider(new ColumnLabelProvider() {
			// ��ȡԪ�ص��ı�
			@Override
			public String getText(Object element) {
				// element�����������ͣ�Ҳ������������ѡ����������������ͻ���������ѡ�����DBObject
				DBObject data = (DBObject) element;
				// ���������ͻ�������ѡ�������
				return (String) data.get(WorkTimeProgram.F_DESC);
			}
		});

		// 4.�ӱ༭����input�л�ȡ�������ֶε�ֵ��������ΪTreeViewer������
		workTimeProgram = (WorkTimeProgram) input.getData();
		Object value = workTimeProgram.getValue(getFieldName());
		if (value == null) {
			value = new BasicDBList();
			workTimeProgram.setValue(getFieldName(), value);
		}
		treeViewer.setInput(value);

		// 5.��Ӳ˵�
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
		// 6.����
		pane.setLayout(new FillLayout());

		return pane;

	}

	protected void doDeleteItem() {
		//�ж���ѡ�ڵ�����ͣ��������ͻ���������ѡ����ûѡ���ǲ������κβ�����
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		if(selection.isEmpty()){
			return;
		}
		DBObject element = (DBObject) selection.getFirstElement();
		ObjectId _id = (ObjectId) element.get(WorkTimeProgram.F__ID);
		BasicBSONList types=(BasicBSONList) workTimeProgram.getValue(getFieldName());
		for (int i = 0; i < types.size(); i++) {
			DBObject type = (DBObject) types.get(i);
			if(_id.equals(type.get(WorkTimeProgram.F__ID))){
				types.remove(i);
				treeViewer.refresh();
				setDirty(true);
				return;
			}else{
				BasicBSONList options = (BasicBSONList) type.get(WorkTimeProgram.F_TYPE_OPTIONS);
				for (int j = 0; j < options.size(); j++) {
					DBObject option = (DBObject) options.get(j);
					if(_id.equals(option.get(WorkTimeProgram.F__ID))){
						options.remove(j);
						treeViewer.refresh(type);
						setDirty(true);
						return;
					}
				}
			}
		}
		
	}

	protected void doAddType(Shell shell) {
		IInputValidator validator = new IInputValidator() {

			@Override
			public String isValid(String newText) {
				// 1.�ж�newText����Ϊ�գ���ո�
				if (newText.trim().isEmpty()) {
					return Messages.get().CanNotEmpty;
				}
				// �ж�newText�����ظ�
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
		// ��ʾ�ı������
		InputDialog ip = new InputDialog(shell,
				Messages.get().CreateDeliverable_4, null, null, validator);
		// ���ı������
		int open = ip.open();
		// ��Ĳ���ok���ͷ���
		if (open != InputDialog.OK) {
			return;
		}
		// ��ȡ�ı�������ֵ
		String typeName = ip.getValue();
		// ����һ�������Ͷ���
		DBObject type = new BasicDBObject();
		type.put(WorkTimeProgram.F__ID, new ObjectId());
		type.put(WorkTimeProgram.F_DESC, typeName);
		type.put(WorkTimeProgram.F_TYPE_OPTIONS, new BasicDBList());
		Object value = workTimeProgram.getValue(getFieldName());
		if (!(value instanceof BasicBSONList)) {
			value = new BasicDBList();
			workTimeProgram.setValue(getFieldName(), value);
		}
		// �������Ͳ��뵽��ʱ������
		((BasicBSONList) value).add(type);
		// ˢ��
		treeViewer.refresh();
		// ���ñ༭�����Ա���

		setDirty(true);
	}

	protected void doAddTypeOption(Shell shell) {
		// �ж��Ƿ�ѡ��һ��������
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
				// 1.�ж�newText����Ϊ�գ���ո�
				if (newText.trim().isEmpty()) {
					return Messages.get().CanNotEmpty;
				}
				// �ж�newText�����ظ�
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
		// ��ʾ�ı������
		InputDialog ip = new InputDialog(shell,
				Messages.get().CreateDeliverable_4, null, null, validator);
		// ���ı������
		int open = ip.open();
		// ��İ�ť����ok���ͷ���
		if (open != InputDialog.OK) {
			return;
		}
		// ��ȡ�ı�������ֵ
		String optionName = ip.getValue();
		// ����һ��������ѡ�����
		DBObject option = new BasicDBObject();
		option.put(WorkTimeProgram.F__ID, new ObjectId());
		option.put(WorkTimeProgram.F_DESC, optionName);
		Object options = type.get(WorkTimeProgram.F_TYPE_OPTIONS);
		if (!(options instanceof BasicBSONList)) {
			options = new BasicDBList();
			type.put(WorkTimeProgram.F_TYPE_OPTIONS, options);
		}
		// ��������ѡ����뵽��������
		((BasicBSONList) options).add(option);
		// ˢ��
		treeViewer.refresh(type);
		// ���ñ༭�����Ա���

		setDirty(true);
	}

	protected String getFieldName() {
		return WorkTimeProgram.F_COLUMNTYPES;
	}

}
