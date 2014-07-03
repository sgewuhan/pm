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
import org.eclipse.ui.forms.widgets.Section;

import com.mobnut.db.model.IPrimaryObjectValueChangeListener;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.viewer.ParaXOptionProvider;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkTimeProgram;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.SimpleSection;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.IValidable;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.part.editor.page.IEditorPageLayoutProvider;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class WorkTimeSettingPage extends AbstractFormPageDelegator implements
		IPrimaryObjectValueChangeListener, IValidable {

	private ComboViewer programSelector;
	private TableViewer paraXSelector;
	private TreeViewer paraYSelector;
	private Project project;
	private boolean editable;
	private static final int MARGIN = 4;

	@Override
	public boolean createBody() {
		return true;
	}

	@Override
	public IEditorPageLayoutProvider getPageLayout() {
		return new IEditorPageLayoutProvider() {

			@Override
			public void layout(Control body, Control customerPage) {
				FormData fd;
				fd = new FormData();
				body.setLayoutData(fd);
				fd.top = new FormAttachment();
				fd.left = new FormAttachment();
				fd.right = new FormAttachment(100);

				fd = new FormData();
				customerPage.setLayoutData(fd);
				fd.top = new FormAttachment(body);
				fd.left = new FormAttachment();
				fd.right = new FormAttachment(100);
				fd.bottom = new FormAttachment(100);
			}
		};
	}

	/**
	 * ����ҳ������
	 */
	@Override
	public Composite createPageContent(IManagedForm mForm, Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		super.createPageContent(mForm, parent, input, conf);
		Section section = new SimpleSection(parent, Section.EXPANDED
				| Section.SHORT_TITLE_BAR);
		section.setText(Messages.get().WorkTimeProgramModify);
		Composite composite = new Composite(section, SWT.NONE);
		// �ӱ༭�������л�ȡ���ݣ����������project
		project = (Project) input.getData();
		// isWorkTimeProgramReadonly =
		// project.canWorkTimeProgramReadonly(context);
		// isWorkTimeParaXReadonly = project.canWorkTimeParaXReadonly(context);
		// isWorkTimeParaYReadonly = project.canWorkTimeParaYReadonly(context);
		editable = project.canEditWorkTimesSetting(input.getContext());
		// ��������ѡ������ComboViewer���ͣ�����������
		programSelector = createProgramSelector(composite);
		// ��ȡ����ѡ�����Ŀؼ�
		Control programSelectorControl = programSelector.getControl();
		// ����������ѡ����
		paraYSelector = createParaYSelector(composite);
		Control paraYSelectorControl = paraYSelector.getControl();
		// ������ʱ����ѡ����������������
		paraXSelector = createParaXSelector(composite);
		// ��ȡ��ʱ����ѡ�����Ŀؼ�
		Control paraXSelectorControl = paraXSelector.getControl();

		// ����parentΪ������
		composite.setLayout(new FormLayout());

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
		paraYSelectorControl.setLayoutData(fd);
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
		paraXSelectorControl.setLayoutData(fd);
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
			Organization organization = project.getFunctionOrganization();
			setProgramSelectorInput(organization);

		} else {
			// ������Ŀ����Ŀģ��id�ֶε�ֵ
			project.addFieldValueListener(Project.F_PROJECT_TEMPLATE_ID, this);
		}
		// ��������
		section.setClient(composite);
		return section;
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
		column.getColumn().setText("ѡ��");
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
		if (editable) {
			setEditingSupportForParaX(tableViewer, column);
		}
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
	private void setEditingSupportForParaX(final TableViewer tableViewer,
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
				return true;
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
		column.getColumn().setText("��Ŀ��ʱ����");
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
		column.getColumn().setText("ѡ��");
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
		if (editable) {
			setEditingSupportForParaY(treeViewer, column);
		}

		treeViewer.setContentProvider(new ParaXOptionProvider());

		return treeViewer;
	}

	private void setEditingSupportForParaY(final TreeViewer treeViewer,
			TreeViewerColumn column) {
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
				return !((DBObject) element)
						.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
			}
		});
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
				if (element instanceof WorkTimeProgram) {
					return (String) ((WorkTimeProgram) element).getDesc();
				}
				return super.getText(element);
			}
		});
		cv.getCombo().setEnabled(editable);
		if (editable) {
			// Ϊ������鿴�����ѡ��ı�����������������������ΪcontentProvider��ArrayContentProvider
			cv.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					// �����ṹѡ����
					StructuredSelection selection = (StructuredSelection) event
							.getSelection();
					WorkTimeProgram workTimeProgram = null;
					if (!selection.isEmpty()) {
						// ��ȡѡ��ĵ�һ��Ԫ�أ����Ԫ���ǹ�ʱ����,����Ϊ��
						Object element = selection.getFirstElement();
						if (element instanceof WorkTimeProgram) {
							workTimeProgram = (WorkTimeProgram) element;
						}
					}
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
		}
		return cv;
	}

	/**
	 * ����������ѡ������Input
	 * 
	 * @param workTimeProgram
	 */
	protected void setParaYSelectorInput(WorkTimeProgram workTimeProgram) {
		if (workTimeProgram != null) {
			BasicBSONList paraYs = (BasicBSONList) workTimeProgram
					.getValue(WorkTimeProgram.F_WORKTIME_PARA_Y);
			paraYSelector.setInput(paraYs);
		}else{
			paraYSelector.setInput(new Object[0]);
		}
	}

	/**
	 * ���ù�ʱ����ѡ������input
	 * 
	 * @param workTimeProgram
	 */
	private void setparaXSelectorInput(WorkTimeProgram workTimeProgram) {
		if (workTimeProgram != null) {
			// ��ȡ��ʱ����
			BasicBSONList paraXs = (BasicBSONList) workTimeProgram
					.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
			// BasicBsonList�̳���ArrayList����paraXs�е�ÿ��Ԫ����DBObject��������F_id,F_desc,F_Type_Options�����ֶ�
			paraXSelector.setInput(paraXs);
		} else {
			paraXSelector.setInput(new Object[0]);
		}
	}

	private void setProgramSelectorInput(Organization organization) {
		List<PrimaryObject> programs = organization.getWorkTimePrograms();
		if (programs != null && !programs.isEmpty()) {
			Object[] input = new Object[programs.size()+1];
			input[0] = "";
			System.arraycopy(programs.toArray(),0,input,1,programs.size());
			programSelector.setInput(input);
			WorkTimeProgram program = project.getWorkTimeProgram();
			if (program != null) {
				programSelector.setSelection(new StructuredSelection(
						new Object[] { program }));
				// ���ù�ʱ����ѡ������Input,��һ�������ǹ�ʱ����
				setparaXSelectorInput(program);
				// ����������ѡ������Input�����빤ʱ����
				setParaYSelectorInput(program);
			}

		} else {
			programSelector.setInput(new Object[0]);
		}
	}

	/**
	 * ���÷���ѡ������input������Ĳ�������Ŀģ��
	 * 
	 * @param projectTemplate
	 */
	private void setProgramSelectorInput(ProjectTemplate projectTemplate) {
		// ��ȡ��Ŀģ���й����Ĺ�ʱ������BSonList����
		BasicBSONList workTimePrograms = (BasicBSONList) projectTemplate
				.getValue(ProjectTemplate.F_WORKTIMEPROGRAMS);
		// Ϊcv����input
		// input��һ��arraylist
		List<WorkTimeProgram> programs = new ArrayList<WorkTimeProgram>();
		if (workTimePrograms != null&& !programs.isEmpty()) {
			for (int i = 0; i < workTimePrograms.size(); i++) {
				ObjectId programId = (ObjectId) workTimePrograms.get(i);
				WorkTimeProgram workTimeProgram = ModelService
						.createModelObject(WorkTimeProgram.class, programId);
				programs.add(workTimeProgram);
			}
			Object[] input = new Object[programs.size()+1];
			input[0] = "";
			System.arraycopy(programs.toArray(),0,input,1,programs.size());
			programSelector.setInput(input);
		} else {
			programSelector.setInput(new Object[0]);
		}
		paraYSelector.setInput(new BasicDBList());
		paraXSelector.setInput(new BasicDBList());

	}

	@Override
	public void setFocus() {

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
			setDirty(false);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void commit(boolean onSave) {
	}

}
