package com.sg.business.project.editor.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.model.IPrimaryObjectValueChangeListener;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.viewer.WorkTimeTypeOptionProvider;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class WorkTimeSettingPage extends AbstractFormPageDelegator implements
		IPrimaryObjectValueChangeListener {

	private ComboViewer programSelector;
	private TableViewer workTimeTypeSelector;
	private TreeViewer columnTypeSelector;
	private Project project;
	private static final int MARGIN = 4;

	/**
	 * ����ҳ������
	 */
	@Override
	public Composite createPageContent(IManagedForm mForm, Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		super.createPageContent(mForm, parent, input, conf);

		// ��������ѡ������ComboViewer���ͣ�����������
		programSelector = createProgramSelector(parent);
		// ��ȡ����ѡ�����Ŀؼ�
		Control programSelectorControl = programSelector.getControl();
		// ������ʱ����ѡ����������������
		workTimeTypeSelector = createWorkTimeTypeSelector(parent);
		// ��ȡ��ʱ����ѡ�����Ŀؼ�
		Control workTimeTypeSelectorControl = workTimeTypeSelector.getControl();
		// ����������ѡ����
		columnTypeSelector = createColumnTypeSelector(parent);
		Control columnTypeSelectorControl = columnTypeSelector.getControl();

		// ����parentΪ������
		parent.setLayout(new FormLayout());

		// ʵ����һ��FormData����
		FormData fd = new FormData();
		// ���÷���ѡ��ؼ��Ĳ�������
		programSelectorControl.setLayoutData(fd);
		// ���÷���ѡ��ؼ��Ķ����߾�
		fd.top = new FormAttachment(0, MARGIN);
		// ���÷���ѡ��ؼ�����߱߾�
		fd.left = new FormAttachment(0, MARGIN);
		// ���÷���ѡ��ؼ��ĸ߶�
		fd.height = 24;
		// ���÷���ѡ��ؼ��Ŀ��
		fd.width = 360;

		// ʵ����һ��FormData����
		fd = new FormData();
		// ���ù�ʱ����ѡ��ؼ��Ĳ�������
		workTimeTypeSelectorControl.setLayoutData(fd);
		// ���ù�ʱ����ѡ��ؼ���Է���ѡ��ؼ��Ķ����߾�
		fd.top = new FormAttachment(programSelectorControl, MARGIN);
		// ���ù�ʱ����ѡ��ؼ��ĵײ��߾�
		fd.bottom = new FormAttachment(100, -MARGIN);
		// ���ù�ʱ����ѡ��ؼ�����߱߾�
		fd.left = new FormAttachment(0, MARGIN);
		// ���ù�ʱ����ѡ��ؼ����ұ߱߾�
		fd.right = new FormAttachment(50, -MARGIN / 2);

		// ʵ����һ��FormData����
		fd = new FormData();
		// ����������ѡ��ؼ��Ĳ�������
		columnTypeSelectorControl.setLayoutData(fd);
		// ����������ѡ��ؼ���Է���ѡ��ؼ��Ķ����߾�
		fd.top = new FormAttachment(programSelectorControl, MARGIN);
		// ����������ѡ��ؼ��ĵײ��߾�
		fd.bottom = new FormAttachment(100, -MARGIN);
		// ����������ѡ��ؼ�����߱߾�
		fd.left = new FormAttachment(50, MARGIN / 2);
		// ����������ѡ��ؼ����ұ߱߾�
		fd.right = new FormAttachment(100, -MARGIN);

		// �ӱ༭�������л�ȡ���ݣ����������project
		project = (Project) input.getData();
		// ������Ŀ����Ŀģ��id�ֶε�ֵ
		project.addFieldValueListener(Project.F_PROJECT_TEMPLATE_ID, this);
		// ��������
		return parent;
	}

	/**
	 * ������ʱ����ѡ����
	 * 
	 * @param parent
	 * @return
	 */
	private TableViewer createWorkTimeTypeSelector(Composite parent) {
		// 1.������鿴��
		final TableViewer tableViewer = new TableViewer(parent, SWT.BORDER
				| SWT.FULL_SELECTION);

		// 2.�Ա�鿴������һЩ��������
		// ���ñ�鿴���Ŀؼ���ı�ͷ�ɼ�
		tableViewer.getTable().setHeaderVisible(true);

		// 3.������鿴������
		// ��һ�����ǹ�ʱ������
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
		// ���ñ�鿴���еĿؼ��е��ı�
		column.getColumn().setText("��ʱ����");
		// ���ñ�鿴���еĿؼ��еĿ��
		column.getColumn().setWidth(145);
		// ���ñ�鿴���еĿؼ��е�LabelProvider
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// element��DBObject�Ĺ�ʱ���ͣ����ع�ʱ���͵�����
				return (String) ((DBObject) element)
						.get(WorkTimeProgram.F_DESC);
			}
		});

		// �ڶ����ǹ�ʱ����ѡ���У���ʾ����Ŀ�������õĹ�ʱ����ѡ����ṩ�༭�����ڱ༭ѡ��
		column = new TableViewerColumn(tableViewer, SWT.LEFT);
		column.getColumn().setText("��ʱ����ѡ��");
		column.getColumn().setWidth(150);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// element��DBObject�Ĺ�ʱ���ͣ�ȡ����ʱ���͵�id
				ObjectId workTimeTypeId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// ����Ŀ�л�ö�Ӧ��ʱ���͵�ѡ��
				DBObject workTimeTypeOption = project
						.getWorkTimeTypeOption(workTimeTypeId);
				// ����Ŀû�ж�Ӧ�Ĺ�ʱ����ѡ��ʱ�����ؿ��ַ���
				if (workTimeTypeOption == null) {
					return "";
				}
				// ������Ŀ�ж�Ӧ��ʱ���͵�ѡ������
				String optionDesc = (String) workTimeTypeOption
						.get(WorkTimeProgram.F_DESC);
				return optionDesc;
			}
		});
		// Ϊ�ڶ������ñ༭��
		column.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				// element��DBObject�Ĺ�ʱ���ͣ�value��editorѡ���DBObject�Ĺ�ʱ����ѡ��
				DBObject option = (DBObject) value;
				// ��ȡ��ʱ���͵�id
				ObjectId workTimeTypeId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// ��ȡ��ʱ���͵�����
				String workTimeTypeDesc = (String) ((DBObject) element)
						.get(WorkTimeProgram.F_DESC);
				// ����project�ķ������ù�ʱ����ѡ�����������
				project.makeWorkTimeTypeOption(workTimeTypeId,
						workTimeTypeDesc, option);
				// ������������
				setDirty(true);
				// ���±�鿴����Ӧ�Ĺ�ʱ����Ԫ��
				tableViewer.update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				// element��DBObject�Ĺ�ʱ����
				// ��ȡ��ʱ���͵�id
				ObjectId workTimeTypeId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// ͨ����ʱ����id����ȡ��Ŀ�е�DBObject�Ĺ�ʱ����ѡ��
				DBObject workTimeTypeOptionOfProject = project
						.getWorkTimeTypeOption(workTimeTypeId);
				// �ж���Ŀ�еĹ�ʱ����ѡ�Ϊ��
				if (workTimeTypeOptionOfProject != null) {
					// ��ȡ��Ŀ�еĹ�ʱ����ѡ���id
					ObjectId optionIdOfProject = (ObjectId) workTimeTypeOptionOfProject
							.get(WorkTimeProgram.F__ID);
					// ��ȡ��ʱ���͵�BsonListѡ��
					BasicBSONList options = (BasicBSONList) ((DBObject) element)
							.get(WorkTimeProgram.F_WORKTIME_TYPE_OPTIONS);
					for (Iterator<?> iterator = options.iterator(); iterator
							.hasNext();) {
						// ������ʱ���͵�ѡ����DBObject�Ĺ�ʱ����ѡ��Ԫ��
						DBObject option = (DBObject) iterator.next();
						// ��ȡ��ʱ����ѡ���id
						ObjectId optionId = (ObjectId) option
								.get(WorkTimeProgram.F__ID);
						// ����Ŀ�еĹ�ʱ����ѡ��id�빤ʱ���͵�ѡ��idһ��ʱ�����ر����õ���ѡ��
						if (optionId.equals(optionIdOfProject)) {
							return option;
						}
					}
				}
				return null;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				// ����������鿴�����༭��
				ComboBoxViewerCellEditor ce = new ComboBoxViewerCellEditor(
						tableViewer.getTable(), SWT.READ_ONLY);
				// ����������鿴��
				ComboViewer cv = ce.getViewer();
				// Ϊ������鿴������contentProviderΪArrayContentProvider
				cv.setContentProvider(ArrayContentProvider.getInstance());
				// Ϊ������鿴������LabelProvider
				cv.setLabelProvider(new LabelProvider() {
					@Override
					public String getText(Object element) {
						// element��DBObject�Ĺ�ʱ���ͣ����ع�ʱ���͵�����
						return (String) ((DBObject) element)
								.get(WorkTimeProgram.F_DESC);
					}
				});
				// ��ȡ��ʱ���͵�BsonListѡ��
				BasicBSONList workTimeTypeOptions = (BasicBSONList) ((DBObject) element)
						.get(WorkTimeProgram.F_WORKTIME_TYPE_OPTIONS);
				// ����ʱ����ѡ�����������鿴��
				cv.setInput(workTimeTypeOptions);
				// ����������鿴��
				return ce;
			}

			// ���õ�Ԫ��ɱ༭
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		// ArrayContentProvider.getInstance()���Ի��һ��ArrayContentProvider
		// ���������������ε�input����һ����ʵ��List�ӿڵ��࣬�ڶ�������������
		// List���������е�ÿ��Ԫ�أ���setInput�Ժ����Ϊ����Ԫ�ش���
		// ArrayContentProvider�ṩ��ѡ��ʵ����IStructuredSelection
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		return tableViewer;
	}

	private TreeViewer createColumnTypeSelector(Composite parent) {
		// 1.�������鿴��,������������
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION
				| SWT.BORDER);
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		// 2.�������鿴������
		TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
		column.getColumn().setText("");
		column.getColumn().setWidth(260);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// elementΪDBObject�������ͻ���������ѡ������������ͻ���������ѡ�������ȡdesc�ֶ�
				return (String) ((DBObject) element)
						.get(WorkTimeProgram.F_DESC);
			}
		});

		column = new TreeViewerColumn(treeViewer, SWT.LEFT);
		column.getColumn().setText("");
		column.getColumn().setWidth(32);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// elementΪDBObject��������
				return "";
			}

			@Override
			public Image getImage(Object element) {
				// �ж�element�Ƿ���������
				if (((DBObject) element)
						.containsField(WorkTimeProgram.F_WORKTIME_TYPE_OPTIONS)) {
					return null;
				}
				// element����������ʱ��Ϊ������ѡ�ȥ������ѡ��id
				ObjectId columnTypeOptionId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				boolean isSelectedWorkTimeOption = project
						.isSelectedWorkTimeOption(columnTypeOptionId,
								Project.F_WORKTIME_COLUMNTYPES);
				if (isSelectedWorkTimeOption) {
					return Widgets.getImage(ImageResource.CHECKED_16);
				} else {
					return Widgets.getImage(ImageResource.UNCHECKED_16);
				}

			}
		});

		// 3.�����༭��
		column.setEditingSupport(new EditingSupport(treeViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				// element��DBObject�Ĺ�ʱ������ѡ�value��true/false
				WorkTimeProgram workTimeProgram = project.getWorkTimeProgram();
				DBObject type = workTimeProgram.getWorkTimeType(
						(ObjectId) ((DBObject) element)
								.get(WorkTimeProgram.F__ID),
						WorkTimeProgram.F_COLUMNTYPES);
				
				// ����project�ķ������ù�ʱ����ѡ�����������
				project.selectWorkTimeColumnTypeOption((ObjectId)type.get(WorkTimeProgram.F__ID),
						(String)type.get(WorkTimeProgram.F_DESC), (DBObject)element,Boolean.TRUE.equals(value));
				
				// ������������
				setDirty(true);
				// ���±�鿴����Ӧ�Ĺ�ʱ����Ԫ��
				treeViewer.update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				// ��ΪcanEdit���жϣ�����elementֻ����������ѡ��
				ObjectId optionId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				return project.isSelectedWorkTimeOption(optionId,
						Project.F_WORKTIME_COLUMNTYPES);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(treeViewer.getTree());
			}

			@Override
			protected boolean canEdit(Object element) {
				// element�������ͻ���������ѡ��,ֻ��������ѡ��ſ��Ա༭
				return !((DBObject) element)
						.containsField(WorkTimeProgram.F_WORKTIME_TYPE_OPTIONS);
			}
		});
		treeViewer.setContentProvider(new WorkTimeTypeOptionProvider());

		return treeViewer;
	}

	private ComboViewer createProgramSelector(Composite parent) {
		// ����������鿴��
		ComboViewer cv = new ComboViewer(parent, SWT.BORDER);
		// ArrayContentProvider.getInstance()���Ի��һ��ArrayContentProvider
		// ���������������ε�input����һ����ʵ��List�ӿڵ��࣬�ڶ�������������
		// List���������е�ÿ��Ԫ�أ���setInput�Ժ����Ϊ����Ԫ�ش���
		// ArrayContentProvider�ṩ��ѡ��ʵ����IStructuredSelection
		cv.setContentProvider(ArrayContentProvider.getInstance());
		// ����������鿴����LabelProvider
		cv.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				// element��WorkTimeProgram���͵Ĺ�ʱ����
				return (String) ((WorkTimeProgram) element).getDesc();
			}
		});
		// Ϊ������鿴�����ѡ��ı�����������������������ΪcontentProvider��ArrayContentProvider
		cv.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// �����ṹѡ����
				StructuredSelection selection = (StructuredSelection) event
						.getSelection();
				// ��ȡѡ��ĵ�һ��Ԫ�أ����Ԫ���ǹ�ʱ����
				WorkTimeProgram workTimeProgram = (WorkTimeProgram) selection
						.getFirstElement();
				// ��ѡ��Ĺ�ʱ����id���浽��Ŀ��
				project.makeSelectedWorkTimeProgram(workTimeProgram);
				// ������������
				setDirty(true);
				// ���ù�ʱ����ѡ������Input,��һ�������ǹ�ʱ����
				setWorkTimeTypeSelectorInput(workTimeProgram);
				// ����������ѡ������Input�����빤ʱ����
				setColumnTypeSelectorInput(workTimeProgram);
				
			}
		});
		return cv;
	}

	/**
	 * ����������ѡ������Input
	 * @param workTimeProgram
	 */
	protected void setColumnTypeSelectorInput(WorkTimeProgram workTimeProgram) {
		BasicBSONList columnTypes = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_COLUMNTYPES);
		columnTypeSelector.setInput(columnTypes);
	}

	/**
	 * ���ù�ʱ����ѡ������input
	 * 
	 * @param workTimeProgram
	 */
	private void setWorkTimeTypeSelectorInput(WorkTimeProgram workTimeProgram) {
		// ��ȡ��ʱ����
		BasicBSONList workTimeTypes = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIMETYPES);
		// BasicBsonList�̳���ArrayList����workTimeTypes�е�ÿ��Ԫ����DBObject��������F_id,F_desc,F_Type_Options�����ֶ�
		workTimeTypeSelector.setInput(workTimeTypes);
	}

	/**
	 * ���÷���ѡ������input������Ĳ�������Ŀģ��
	 * 
	 * @param projectTemplate
	 */
	private void setProgramSelectorInput(PrimaryObject projectTemplate) {
		// ��ȡ��Ŀģ���й����Ĺ�ʱ������BSonList����
		BasicBSONList workTimePrograms = (BasicBSONList) projectTemplate
				.getValue(ProjectTemplate.F_WORKTIMEPROGRAMS);
		// Ϊcv����input
		// input��һ��arraylist
		List<WorkTimeProgram> input = new ArrayList<WorkTimeProgram>();
		if (workTimePrograms != null) {
			for (int i = 0; i < workTimePrograms.size(); i++) {
				ObjectId programId = (ObjectId) workTimePrograms.get(i);
				WorkTimeProgram workTimeProgram = ModelService
						.createModelObject(WorkTimeProgram.class, programId);
				input.add(workTimeProgram);
			}
		}

		programSelector.setInput(input);
		columnTypeSelector.setInput(new BasicDBList());
		workTimeTypeSelector.setInput(new BasicDBList());
		
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void commit(boolean onSave) {
		setDirty(false);
	}

	/**
	 * ������Ŀ����Ŀģ���ֶ�ֵ�ĸı䣬Ϊ��ʱ����ѡ�����ṩ����
	 */
	@Override
	public void valueChanged(String key, Object oldValue, Object newValue) {
		if (Project.F_PROJECT_TEMPLATE_ID.equals(key)) {
			if (newValue instanceof ObjectId) {
				ProjectTemplate projectTemplate = ModelService
						.createModelObject(ProjectTemplate.class,
								(ObjectId) newValue);
				if (projectTemplate != null) {
					setProgramSelectorInput(projectTemplate);
					project.clearWorkTimeProgram();
				}
			}
		}
	}

}
