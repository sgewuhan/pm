package com.sg.business.management.editor;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.commons.util.Utils;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.viewer.ParaXOptionProvider;
import com.sg.business.model.WorkTimeProgram;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public abstract class AbstractTypePageDelegator extends
		AbstractFormPageDelegator {

	private TreeViewer treeViewer;
	private WorkTimeProgram workTimeProgram;
	private MenuManager menuManager;
	private Action menuAddType;
	private Shell shell;
	private Action menuAddOption;
	private Action menuEdit;
	private Action menuDelete;

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
		this.shell = parent.getShell();
		// ��������
		final Composite pane = new Composite(parent, SWT.NONE);
		// 1.�������洴��һ����
		treeViewer = new TreeViewer(pane, SWT.FULL_SELECTION);
		final Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		// 2.���������ṩ��
		treeViewer.setContentProvider(new ParaXOptionProvider());

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
		
		if(workTimeProgram.canEdit(input.getContext())){
			// 5.��Ӳ˵�
			createMenu();
			tree.addMouseListener(new MouseListener() {
				@Override
				public void mouseUp(MouseEvent e) {
				}
				@Override
				public void mouseDown(MouseEvent e) {
					TreeItem item = tree.getItem(new Point(e.x, e.y));
					if (item == null) {
						treeViewer.setSelection(StructuredSelection.EMPTY);
					}
				}
				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
			});
		}
		
		treeViewer.expandAll();
		// 6.����
		pane.setLayout(new FillLayout());

		return pane;

	}

	private void createMenu() {
		createActions();
		Tree tree = treeViewer.getTree();
		menuManager = new MenuManager("popup");
		menuManager.createContextMenu(tree);
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection sel = (IStructuredSelection) treeViewer
						.getSelection();
				if (sel == null || sel.isEmpty()) {
					manager.add(menuAddType);
				} else {
					DBObject element = (DBObject) sel.getFirstElement();
					if (element
							.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS)) {
						// ����
						manager.add(menuAddType);
						manager.add(menuAddOption);
						manager.add(menuEdit);
						manager.add(menuDelete);
					} else {
						manager.add(menuEdit);
						manager.add(menuDelete);
					}
				}
			}
		});

		tree.setMenu(menuManager.getMenu());
	}

	private void createActions() {
		menuAddType = new Action(Messages.get().CreateDeliverable_4) {
			@Override
			public void run() {
				doAddType();
			}
		};

		menuAddOption = new Action(Messages.get().AddOption) {
			@Override
			public void run() {
				doAddTypeOption();
			}

		};
		// 2014.6.23 �����༭�˵�
		menuEdit = new Action(Messages.get().EditWork_6) {
			@Override
			public void run() {
				doModifyItem();
			}
		};

		menuDelete = new Action(Messages.get().RemoveDeliverable_4) {
			@Override
			public void run() {
				doDeleteItem();
			}
		};
	}

	/**
	 * 2014.6.23 �༭������ѡ��
	 */
	protected void doModifyItem() {
		// �ж���ѡ�ڵ�����ͣ��������ͻ���������ѡ����ûѡ���ǲ������κβ�����
		IStructuredSelection selection = (IStructuredSelection) treeViewer
				.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		// ��ȡ��ѡ��ѡ���еĵ�һ��Ԫ��
		final DBObject value = (DBObject) selection.getFirstElement();
		// ��ȡ��ѡԪ�ص�id
		ObjectId _id = (ObjectId) value.get(WorkTimeProgram.F__ID);

		IInputValidator vali = new IInputValidator() {
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
						DBObject type = (DBObject) ((BasicBSONList) value).get(i);
						if (type.get(WorkTimeProgram.F_DESC)
								.equals(newText)) {
							return Messages.get().DuplicateName;
						}else{
							BasicBSONList options = (BasicBSONList) type.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
							for (int j = 0; j < options.size(); j++) {
								DBObject option = (DBObject) options
										.get(j);
								if (option.get(WorkTimeProgram.F_DESC).equals(newText)) {
									return Messages.get().DuplicateName;
								}
							}
						}
					}
				} else {
				}
				return null;
			}
		};
		InputDialog ip = new InputDialog(shell, Messages.get().EditWork_6,
				null, null, vali);
		// ���ı������
		int open = ip.open();
		// ��Ĳ���ok���ͷ���
		if (open != InputDialog.OK) {
			return;
		}
		// ��ȡ�ı�������ֵ
		String typeName = ip.getValue();
		// ����һ�������Ͷ���
		workTimeProgram.modifyTypeOrOption(_id, typeName, getFieldName());
		// ˢ��
		treeViewer.refresh();
		// ���ñ༭�����Ա���

		setDirty(true);

	}

	protected void doDeleteItem() {
		// �ж���ѡ�ڵ�����ͣ��������ͻ���������ѡ����ûѡ���ǲ������κβ�����
		IStructuredSelection selection = (IStructuredSelection) treeViewer
				.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		// ��ȡ��ѡ��ѡ���еĵ�һ��Ԫ��
		DBObject element = (DBObject) selection.getFirstElement();
		// ��ȡ��ѡԪ�ص�id
		ObjectId _id = (ObjectId) element.get(WorkTimeProgram.F__ID);
		DBObject type = workTimeProgram.removeTypeOrOption(_id, getFieldName());
		if (type != null) {
			treeViewer.refresh(type);
		} else {
			treeViewer.refresh();
		}
		setDirty(true);
		return;

	}

	protected void doAddType() {
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
		workTimeProgram.appendType(typeName, getFieldName());
		// ˢ��
		treeViewer.refresh();
		// ���ñ༭�����Ա���

		setDirty(true);
	}

	protected void doAddTypeOption() {
		// �ж��Ƿ�ѡ��һ��������
		IStructuredSelection selection = (IStructuredSelection) treeViewer
				.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		final DBObject type = (DBObject) selection.getFirstElement();
		if (!type.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS)) {
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
				Object options = type
						.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
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
		workTimeProgram.appendOption(type, optionName);
		// ˢ��
		treeViewer.refresh(type);
		Object[] elements = treeViewer.getExpandedElements();
		treeViewer.setExpandedElements(Utils.arrayAppend(elements,
				new Object[] { type }));
		// ���ñ༭�����Ա���

		setDirty(true);
	}

	protected abstract String getFieldName();

}
