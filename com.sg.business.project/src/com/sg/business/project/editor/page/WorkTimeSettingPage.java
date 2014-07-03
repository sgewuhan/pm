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
	 * 创建页面内容
	 */
	@Override
	public Composite createPageContent(IManagedForm mForm, Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		super.createPageContent(mForm, parent, input, conf);
		Section section = new SimpleSection(parent, Section.EXPANDED
				| Section.SHORT_TITLE_BAR);
		section.setText(Messages.get().WorkTimeProgramModify);
		Composite composite = new Composite(section, SWT.NONE);
		// 从编辑器输入中获取数据，这个数据是project
		project = (Project) input.getData();
		// isWorkTimeProgramReadonly =
		// project.canWorkTimeProgramReadonly(context);
		// isWorkTimeParaXReadonly = project.canWorkTimeParaXReadonly(context);
		// isWorkTimeParaYReadonly = project.canWorkTimeParaYReadonly(context);
		editable = project.canEditWorkTimesSetting(input.getContext());
		// 创建方案选择器，ComboViewer类型，参数是容器
		programSelector = createProgramSelector(composite);
		// 获取方案选择器的控件
		Control programSelectorControl = programSelector.getControl();
		// 创建列类型选择器
		paraYSelector = createParaYSelector(composite);
		Control paraYSelectorControl = paraYSelector.getControl();
		// 创建工时类型选择器，参数是容器
		paraXSelector = createParaXSelector(composite);
		// 获取工时类型选择器的控件
		Control paraXSelectorControl = paraXSelector.getControl();

		// 设置parent为表单布局
		composite.setLayout(new FormLayout());

		// 实例化一个FormData对象
		FormData fd = new FormData();
		// 设置方案选择控件的布局数据
		programSelectorControl.setLayoutData(fd);
		// 设置方案选择控件的顶部边距
		fd.top = new FormAttachment(0, MARGIN);
		// 设置方案选择控件的左边边距
		fd.left = new FormAttachment(0, MARGIN);
		// 设置方案选择控件的高度
		fd.height = 24;
		// 设置方案选择控件的宽度
		fd.width = 360;

		// 实例化一个FormData对象
		fd = new FormData();
		// 设置工时类型选择控件的布局数据
		paraYSelectorControl.setLayoutData(fd);
		// 设置工时类型选择控件相对方案选择控件的顶部边距
		fd.top = new FormAttachment(programSelectorControl, MARGIN);
		// 设置工时类型选择控件的底部边距
		fd.bottom = new FormAttachment(100, -MARGIN);
		// 设置工时类型选择控件的左边边距
		fd.left = new FormAttachment(0, MARGIN);
		// 设置工时类型选择控件的右边边距
		fd.right = new FormAttachment(50, -MARGIN / 2);

		// 实例化一个FormData对象
		fd = new FormData();
		// 设置列类型选择控件的布局数据
		paraXSelectorControl.setLayoutData(fd);
		// 设置列类型选择控件相对方案选择控件的顶部边距
		fd.top = new FormAttachment(programSelectorControl, MARGIN);
		// 设置列类型选择控件的底部边距
		fd.bottom = new FormAttachment(100, -MARGIN);
		// 设置列类型选择控件的左边边距
		fd.left = new FormAttachment(50, MARGIN / 2);
		// 设置列类型选择控件的右边边距
		fd.right = new FormAttachment(100, -MARGIN);

		// 如果项目已经持久化了，只需打开，无需侦听
		if (project.isPersistent()) {
			Organization organization = project.getFunctionOrganization();
			setProgramSelectorInput(organization);

		} else {
			// 侦听项目的项目模板id字段的值
			project.addFieldValueListener(Project.F_PROJECT_TEMPLATE_ID, this);
		}
		// 返回容器
		section.setClient(composite);
		return section;
	}

	/**
	 * 创建工时类型选择器
	 * 
	 * @param parent
	 * @param readOnly
	 * @return
	 */
	private TableViewer createParaXSelector(Composite parent) {
		// 1.创建表查看器
		final TableViewer tableViewer = new TableViewer(parent, SWT.BORDER
				| SWT.FULL_SELECTION);

		// 2.对表查看器进行一些基本设置
		// 设置表查看器的控件表的表头可见
		tableViewer.getTable().setHeaderVisible(true);

		// 3.创建表查看器的列
		// 第一个列是工时类型列
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
		// 设置表查看器列的控件列的文本
		column.getColumn().setText("工作工时参数");
		// 设置表查看器列的控件列的宽度
		column.getColumn().setWidth(145);
		// 设置表查看器列的控件列的LabelProvider
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// element是DBObject的工时类型，返回工时类型的名称
				return (String) ((DBObject) element)
						.get(WorkTimeProgram.F_DESC);
			}
		});

		// 第二列是工时类型选项列，显示本项目中已设置的工时类型选项，并提供编辑器用于编辑选项
		column = new TableViewerColumn(tableViewer, SWT.LEFT);
		column.getColumn().setText("选项");
		column.getColumn().setWidth(150);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// element是DBObject的工时类型，取出工时类型的id
				ObjectId paraXId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// 从项目中获得对应工时类型的选项
				DBObject paraXOption = project.getParaXOption(paraXId);
				// 当项目没有对应的工时类型选项时，返回空字符串
				if (paraXOption == null) {
					return "";
				}
				// 返回项目中对应工时类型的选项名称
				String optionDesc = (String) paraXOption
						.get(WorkTimeProgram.F_DESC);
				return optionDesc;
			}
		});
		// 为第二列设置编辑器
		// 2014.6.24日 为项目启动后还可以修改工作工时参数,将if判断注释
		// 2014.6.25 项目负责人和项目管理员都可以在项目未提交计划之前修改工时参数
		if (editable) {
			setEditingSupportForParaX(tableViewer, column);
		}
		// 它适用于两种情形的input，第一种是实现List接口的类，第二种是数组类型
		// List或者数组中的每个元素，在setInput以后会作为表格的元素存在
		// ArrayContentProvider提供的选择实现了IStructuredSelection
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		return tableViewer;
	}

	/**
	 * 工作工时参数编辑器
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
				// element是DBObject的工时类型，value是editor选择的DBObject的工时类型选项
				DBObject option = (DBObject) value;
				// 获取工时类型的id
				ObjectId paraXId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// 获取工时类型的名称
				String paraXDesc = (String) ((DBObject) element)
						.get(WorkTimeProgram.F_DESC);
				// 调用project的方法设置工时类型选项，传三个参数
				project.makeParaXOption(paraXId, paraXDesc, option);
				project.noticeValueChanged(Project.F_WORKTIME_PARA_X);

				// 设置数据脏了
				setDirty(true);
				// 更新表查看器对应的工时类型元素
				tableViewer.update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				// element是DBObject的工时类型
				// 获取工时类型的id
				ObjectId paraXId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// 通过工时类型id，获取项目中的DBObject的工时类型选项
				DBObject paraXOptionOfProject = project.getParaXOption(paraXId);
				// 判断项目中的工时类型选项不为空
				if (paraXOptionOfProject != null) {
					// 获取项目中的工时类型选项的id
					ObjectId optionIdOfProject = (ObjectId) paraXOptionOfProject
							.get(WorkTimeProgram.F__ID);
					// 获取工时类型的BsonList选项
					BasicBSONList options = (BasicBSONList) ((DBObject) element)
							.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
					for (Iterator<?> iterator = options.iterator(); iterator
							.hasNext();) {
						// 遍历工时类型的选项，获得DBObject的工时类型选项元素
						DBObject option = (DBObject) iterator.next();
						// 获取工时类型选项的id
						ObjectId optionId = (ObjectId) option
								.get(WorkTimeProgram.F__ID);
						// 当项目中的工时类型选项id与工时类型的选项id一致时，返回遍历得到的选项
						if (optionId.equals(optionIdOfProject)) {
							return option;
						}
					}
				}
				return null;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				// 创建下拉框查看器表格编辑器
				ComboBoxViewerCellEditor ce = new ComboBoxViewerCellEditor(
						tableViewer.getTable(), SWT.READ_ONLY);
				// 创建下拉框查看器
				ComboViewer cv = ce.getViewer();
				// 为下拉框查看器设置contentProvider为ArrayContentProvider
				cv.setContentProvider(ArrayContentProvider.getInstance());
				// 为下拉框查看器设置LabelProvider
				cv.setLabelProvider(new LabelProvider() {
					@Override
					public String getText(Object element) {
						// element是DBObject的工时类型，返回工时类型的名称
						return (String) ((DBObject) element)
								.get(WorkTimeProgram.F_DESC);
					}
				});
				// 获取工时类型的BsonList选项
				BasicBSONList paraXOptions = (BasicBSONList) ((DBObject) element)
						.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
				// 将工时类型选项存入下拉框查看器
				cv.setInput(paraXOptions);
				// 返回下拉框查看器
				return ce;
			}

			// 设置单元格可编辑
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
	}

	private TreeViewer createParaYSelector(Composite parent) {
		// 1.创建树查看器,并作基本设置
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION
				| SWT.BORDER);
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		// 2.创建树查看器的列
		TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
		column.getColumn().setText("项目工时参数");
		column.getColumn().setWidth(260);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// element为DBObject的列类型或者列类型选项，无论是列类型还是列类型选项，都可以取desc字段
				return (String) ((DBObject) element)
						.get(WorkTimeProgram.F_DESC);
			}
		});

		column = new TreeViewerColumn(treeViewer, SWT.LEFT);
		column.getColumn().setText("选项");
		column.getColumn().setWidth(32);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// element为DBObject的列类型
				return "";
			}

			@Override
			public Image getImage(Object element) {
				// 判断element是否是列类型
				if (((DBObject) element)
						.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS)) {
					return null;
				}
				// element不是列类型时，为列类型选项，去列类型选项id
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

		// 3.创建编辑器
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
				// element是DBObject的工时列类型选项，value是true/false
				WorkTimeProgram workTimeProgram = project.getWorkTimeProgram();
				DBObject type = workTimeProgram.getParaX(
						(ObjectId) ((DBObject) element)
								.get(WorkTimeProgram.F__ID),
						WorkTimeProgram.F_WORKTIME_PARA_Y);

				// 调用project的方法设置工时类型选项，传三个参数
				project.selectWorkTimeParaYOption(
						(ObjectId) type.get(WorkTimeProgram.F__ID),
						(String) type.get(WorkTimeProgram.F_DESC),
						(DBObject) element, Boolean.TRUE.equals(value));
				project.noticeValueChanged(Project.F_WORKTIME_PARA_Y);

				// 设置数据脏了
				setDirty(true);
				// 更新表查看器对应的工时类型元素
				treeViewer.update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				// 因为canEdit已判断，所有element只能是列类型选项
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
				// element是列类型或者列类型选项,只有列类型选项才可以编辑
				return !((DBObject) element)
						.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
			}
		});
	}

	private ComboViewer createProgramSelector(Composite parent) {
		// 创建下拉框查看器
		ComboViewer cv = new ComboViewer(parent, SWT.READ_ONLY);
		// ArrayContentProvider.getInstance()可以获得一个ArrayContentProvider
		// 它适用于两种情形的input，第一种是实现List接口的类，第二种是数组类型
		// List或者数组中的每个元素，在setInput以后会作为表格的元素存在
		// ArrayContentProvider提供的选择实现了IStructuredSelection
		cv.setContentProvider(ArrayContentProvider.getInstance());
		// 设置下拉框查看器的LabelProvider
		cv.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				// element是WorkTimeProgram类型的工时方案
				if (element instanceof WorkTimeProgram) {
					return (String) ((WorkTimeProgram) element).getDesc();
				}
				return super.getText(element);
			}
		});
		cv.getCombo().setEnabled(editable);
		if (editable) {
			// 为下拉框查看器添加选择改变的侦听器，这个侦听器是因为contentProvider是ArrayContentProvider
			cv.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					// 创建结构选择器
					StructuredSelection selection = (StructuredSelection) event
							.getSelection();
					WorkTimeProgram workTimeProgram = null;
					if (!selection.isEmpty()) {
						// 获取选择的第一个元素，这个元素是工时方案,可能为空
						Object element = selection.getFirstElement();
						if (element instanceof WorkTimeProgram) {
							workTimeProgram = (WorkTimeProgram) element;
						}
					}
					// 将选择的工时方案id保存到项目中
					project.makeSelectedWorkTimeProgram(workTimeProgram);
					project.noticeValueChanged(Project.F_WORKTIMEPROGRAM_ID);
					// 设置数据脏了
					setDirty(true);
					// 设置工时类型选择器的Input,传一个参数是工时方案
					setparaXSelectorInput(workTimeProgram);
					// 设置列类型选择器的Input，传入工时方案
					setParaYSelectorInput(workTimeProgram);

				}
			});
		}
		return cv;
	}

	/**
	 * 设置列类型选择器的Input
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
	 * 设置工时类型选择器的input
	 * 
	 * @param workTimeProgram
	 */
	private void setparaXSelectorInput(WorkTimeProgram workTimeProgram) {
		if (workTimeProgram != null) {
			// 获取工时类型
			BasicBSONList paraXs = (BasicBSONList) workTimeProgram
					.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
			// BasicBsonList继承与ArrayList，而paraXs中的每个元素是DBObject，包括了F_id,F_desc,F_Type_Options三个字段
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
				// 设置工时类型选择器的Input,传一个参数是工时方案
				setparaXSelectorInput(program);
				// 设置列类型选择器的Input，传入工时方案
				setParaYSelectorInput(program);
			}

		} else {
			programSelector.setInput(new Object[0]);
		}
	}

	/**
	 * 设置方案选择器的input，传入的参数是项目模板
	 * 
	 * @param projectTemplate
	 */
	private void setProgramSelectorInput(ProjectTemplate projectTemplate) {
		// 获取项目模板中关联的工时方案，BSonList类型
		BasicBSONList workTimePrograms = (BasicBSONList) projectTemplate
				.getValue(ProjectTemplate.F_WORKTIMEPROGRAMS);
		// 为cv设置input
		// input是一个arraylist
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
	 * 侦听项目中项目模板字段值的改变，为工时方案选择器提供输入
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
		// 验证工时方案是否已选，如果选择了，就验证工时类型选项是否全选和列类型选项是否选择了，如果没选，就
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
