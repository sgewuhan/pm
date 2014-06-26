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

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.IPrimaryObjectValueChangeListener;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.viewer.ParaXOptionProvider;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.Work;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.IValidable;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class WorkTimeSettingPage extends AbstractFormPageDelegator implements
		IPrimaryObjectValueChangeListener, IValidable {

	private ComboViewer programSelector;
	private TableViewer paraXSelector;
	private TreeViewer paraYSelector;
	private Project project;
	private boolean isWorkTimeProgramReadonly;
	private boolean isWorkTimeParaXReadonly;
	private boolean isWorkTimeParaYReadonly;
	private IContext context;
	private static final int MARGIN = 4;

	/**
	 * ����ҳ������
	 */
	@Override
	public Composite createPageContent(IManagedForm mForm, Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		context = new CurrentAccountContext();
		super.createPageContent(mForm, parent, input, conf);
		// �ӱ༭�������л�ȡ���ݣ����������project
		project = (Project) input.getData();
		isWorkTimeProgramReadonly = project.canWorkTimeProgramReadonly(context);
		isWorkTimeParaXReadonly = project.canWorkTimeParaXReadonly(context);
		isWorkTimeParaYReadonly = project.canWorkTimeParaYReadonly(context);
		// ��������ѡ������ComboViewer���ͣ�����������
		programSelector = createProgramSelector(parent);

		// ��ȡ����ѡ�����Ŀؼ�
		Control programSelectorControl = programSelector.getControl();
		// ������ʱ����ѡ����������������
		paraXSelector = createParaXSelector(parent);
		// ��ȡ��ʱ����ѡ�����Ŀؼ�
		Control paraXSelectorControl = paraXSelector.getControl();
		// ����������ѡ����
		paraYSelector = createParaYSelector(parent);
		Control paraYSelectorControl = paraYSelector.getControl();

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
		paraXSelectorControl.setLayoutData(fd);
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
		paraYSelectorControl.setLayoutData(fd);
		// ����������ѡ��ؼ���Է���ѡ��ؼ��Ķ����߾�
		fd.top = new FormAttachment(programSelectorControl, MARGIN);
		// ����������ѡ��ؼ��ĵײ��߾�
		fd.bottom = new FormAttachment(100, -MARGIN);
		// ����������ѡ��ؼ�����߱߾�
		fd.left = new FormAttachment(50, MARGIN / 2);
		// ����������ѡ��ؼ����ұ߱߾�
		fd.right = new FormAttachment(100, -MARGIN);

		// �����Ŀ�Ѿ��־û��ˣ�ֻ��򿪣���������
		if (project.isPersistent()) {
			WorkTimeProgram program = project.getWorkTimeProgram();
			if (program != null) {
				programSelector.setInput(new Object[] { program });
				programSelector.setSelection(new StructuredSelection(
						new Object[] { program }));
				// ���ù�ʱ����ѡ������Input,��һ�������ǹ�ʱ����
				setparaXSelectorInput(program);
				// ����������ѡ������Input�����빤ʱ����
				setParaYSelectorInput(program);
			}
		} else {
			// ������Ŀ����Ŀģ��id�ֶε�ֵ
			project.addFieldValueListener(Project.F_PROJECT_TEMPLATE_ID, this);
		}
		// ��������
		return parent;
	}

	/**
	 * ������ʱ����ѡ����
	 * 
	 * @param parent
	 * @param readOnly
	 * @return
	 */
	private TableViewer createParaXSelector(Composite parent) {
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
		column.getColumn().setText("������ʱ����");
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
		column.getColumn().setText("������ʱ����ѡ��");
		column.getColumn().setWidth(150);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// element��DBObject�Ĺ�ʱ���ͣ�ȡ����ʱ���͵�id
				ObjectId paraXId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// ����Ŀ�л�ö�Ӧ��ʱ���͵�ѡ��
				DBObject paraXOption = project.getParaXOption(paraXId);
				// ����Ŀû�ж�Ӧ�Ĺ�ʱ����ѡ��ʱ�����ؿ��ַ���
				if (paraXOption == null) {
					return "";
				}
				// ������Ŀ�ж�Ӧ��ʱ���͵�ѡ������
				String optionDesc = (String) paraXOption
						.get(WorkTimeProgram.F_DESC);
				return optionDesc;
			}
		});
		// Ϊ�ڶ������ñ༭��
		// 2014.6.24�� Ϊ��Ŀ�����󻹿����޸Ĺ�����ʱ����,��if�ж�ע��
		// 2014.6.25 ��Ŀ�����˺���Ŀ����Ա����������Ŀδ�ύ�ƻ�֮ǰ�޸Ĺ�ʱ����
		worksParaXInWork(tableViewer, column);
		// ���������������ε�input����һ����ʵ��List�ӿڵ��࣬�ڶ�������������
		// List���������е�ÿ��Ԫ�أ���setInput�Ժ����Ϊ����Ԫ�ش���
		// ArrayContentProvider�ṩ��ѡ��ʵ����IStructuredSelection
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		return tableViewer;
	}

	/**
	 * ������ʱ�����༭��
	 * 
	 * @param tableViewer
	 * @param column
	 * @param readOnly
	 */
	private void worksParaXInWork(final TableViewer tableViewer,
			TableViewerColumn column) {
		column.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				// element��DBObject�Ĺ�ʱ���ͣ�value��editorѡ���DBObject�Ĺ�ʱ����ѡ��
				DBObject option = (DBObject) value;
				// ��ȡ��ʱ���͵�id
				ObjectId paraXId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// ��ȡ��ʱ���͵�����
				String paraXDesc = (String) ((DBObject) element)
						.get(WorkTimeProgram.F_DESC);
				// ����project�ķ������ù�ʱ����ѡ�����������
				project.makeParaXOption(paraXId, paraXDesc, option);
				project.noticeValueChanged(Project.F_WORKTIME_PARA_X);

				// ������������
				setDirty(true);
				// ���±�鿴����Ӧ�Ĺ�ʱ����Ԫ��
				tableViewer.update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				// element��DBObject�Ĺ�ʱ����
				// ��ȡ��ʱ���͵�id
				ObjectId paraXId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// ͨ����ʱ����id����ȡ��Ŀ�е�DBObject�Ĺ�ʱ����ѡ��
				DBObject paraXOptionOfProject = project.getParaXOption(paraXId);
				// �ж���Ŀ�еĹ�ʱ����ѡ�Ϊ��
				if (paraXOptionOfProject != null) {
					// ��ȡ��Ŀ�еĹ�ʱ����ѡ���id
					ObjectId optionIdOfProject = (ObjectId) paraXOptionOfProject
							.get(WorkTimeProgram.F__ID);
					// ��ȡ��ʱ���͵�BsonListѡ��
					BasicBSONList options = (BasicBSONList) ((DBObject) element)
							.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
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
				BasicBSONList paraXOptions = (BasicBSONList) ((DBObject) element)
						.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
				// ����ʱ����ѡ�����������鿴��
				cv.setInput(paraXOptions);
				// ����������鿴��
				return ce;
			}

			// ���õ�Ԫ��ɱ༭
			@Override
			protected boolean canEdit(Object element) {
				if (isWorkTimeProgramReadonly && !isWorkTimeParaXReadonly) {
					ObjectId _id = (ObjectId) ((DBObject) element)
							.get(PrimaryObject.F__ID);
					DBCollection collection = DBActivator.getCollection(
							IModelConstants.DB, IModelConstants.C_WORK);
					DBCursor cursor = collection.find(
							new BasicDBObject()
									.append(Work.F_PROJECT_ID, project.get_id())
									.append(IWorkCloneFields.F_MEASUREMENT,
											IWorkCloneFields.MEASUREMENT_TYPE_STANDARD_ID)
									.append(Work.F_LIFECYCLE,
											new BasicDBObject()
													.append("$ne",
															ILifecycle.STATUS_ONREADY_VALUE)),
							new BasicDBObject().append(
									IWorkCloneFields.F_WORKTIME_PARAX, 1));
					while (cursor.hasNext()) {
						DBObject object = cursor.next();
						BasicBSONList worktimeParaXList = (BasicBSONList) object
								.get(IWorkCloneFields.F_WORKTIME_PARAX);
						for (Object o : worktimeParaXList) {
							DBObject worktimeParaX = (DBObject) o;
							ObjectId para_id = (ObjectId) worktimeParaX
									.get(IWorkCloneFields.F_WORKTIME_PARAX_ID);
							if (_id.equals(para_id)) {
								return false;
							}
						}
					}
				}
				return !isWorkTimeParaXReadonly;
			}
		});
	}

	private TreeViewer createParaYSelector(Composite parent) {
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
						.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS)) {
					return null;
				}
				// element����������ʱ��Ϊ������ѡ�ȥ������ѡ��id
				ObjectId paraYOptionId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				boolean isSelectedWorkTimeOption = project
						.isSelectedWorkTimeOption(paraYOptionId,
								Project.F_WORKTIME_PARA_Y);
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
				DBObject type = workTimeProgram.getParaX(
						(ObjectId) ((DBObject) element)
								.get(WorkTimeProgram.F__ID),
						WorkTimeProgram.F_WORKTIME_PARA_Y);

				// ����project�ķ������ù�ʱ����ѡ�����������
				project.selectWorkTimeParaYOption(
						(ObjectId) type.get(WorkTimeProgram.F__ID),
						(String) type.get(WorkTimeProgram.F_DESC),
						(DBObject) element, Boolean.TRUE.equals(value));
				project.noticeValueChanged(Project.F_WORKTIME_PARA_Y);

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
						Project.F_WORKTIME_PARA_Y);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(treeViewer.getTree());
			}

			@Override
			protected boolean canEdit(Object element) {
				// element�������ͻ���������ѡ��,ֻ��������ѡ��ſ��Ա༭
				return !isWorkTimeParaYReadonly
						&& !((DBObject) element)
								.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
			}
		});
		treeViewer.setContentProvider(new ParaXOptionProvider());

		return treeViewer;
	}

	private ComboViewer createProgramSelector(Composite parent) {
		// ����������鿴��
		ComboViewer cv = new ComboViewer(parent, SWT.READ_ONLY);
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
		cv.getCombo().setEnabled(!isWorkTimeProgramReadonly);
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
				project.noticeValueChanged(Project.F_WORKTIMEPROGRAM_ID);
				// ������������
				setDirty(true);
				// ���ù�ʱ����ѡ������Input,��һ�������ǹ�ʱ����
				setparaXSelectorInput(workTimeProgram);
				// ����������ѡ������Input�����빤ʱ����
				setParaYSelectorInput(workTimeProgram);

			}
		});
		return cv;
	}

	@Override
	protected void setDirty(boolean isDirty) {
		if (isWorkTimeProgramReadonly && isWorkTimeParaXReadonly
				&& isWorkTimeParaYReadonly) {
			return;
		}
		super.setDirty(isDirty);
	}

	/**
	 * ����������ѡ������Input
	 * 
	 * @param workTimeProgram
	 */
	protected void setParaYSelectorInput(WorkTimeProgram workTimeProgram) {
		BasicBSONList paraYs = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_Y);
		paraYSelector.setInput(paraYs);
	}

	/**
	 * ���ù�ʱ����ѡ������input
	 * 
	 * @param workTimeProgram
	 */
	private void setparaXSelectorInput(WorkTimeProgram workTimeProgram) {
		// ��ȡ��ʱ����
		BasicBSONList paraXs = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
		// BasicBsonList�̳���ArrayList����paraXs�е�ÿ��Ԫ����DBObject��������F_id,F_desc,F_Type_Options�����ֶ�
		paraXSelector.setInput(paraXs);
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
		paraYSelector.setInput(new BasicDBList());
		paraXSelector.setInput(new BasicDBList());

	}

	@Override
	public void setFocus() {

	}

	@Override
	public void commit(boolean onSave) {
		if (isWorkTimeProgramReadonly && isWorkTimeParaXReadonly
				&& isWorkTimeParaYReadonly) {
			return;
		}
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

	@Override
	public boolean checkValidOnSave() {
		// ��֤��ʱ�����Ƿ���ѡ�����ѡ���ˣ�����֤��ʱ����ѡ���Ƿ�ȫѡ��������ѡ���Ƿ�ѡ���ˣ����ûѡ����
		try {
			project.checkWorkTimeProgram();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
