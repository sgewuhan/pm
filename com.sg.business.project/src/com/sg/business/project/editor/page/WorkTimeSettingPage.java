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
	 * 创建页面内容
	 */
	@Override
	public Composite createPageContent(IManagedForm mForm, Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		super.createPageContent(mForm, parent, input, conf);

		// 创建方案选择器，ComboViewer类型，参数是容器
		programSelector = createProgramSelector(parent);
		// 获取方案选择器的控件
		Control programSelectorControl = programSelector.getControl();
		// 创建工时类型选择器，参数是容器
		workTimeTypeSelector = createWorkTimeTypeSelector(parent);
		// 获取工时类型选择器的控件
		Control workTimeTypeSelectorControl = workTimeTypeSelector.getControl();
		// 创建列类型选择器
		columnTypeSelector = createColumnTypeSelector(parent);
		Control columnTypeSelectorControl = columnTypeSelector.getControl();

		// 设置parent为表单布局
		parent.setLayout(new FormLayout());

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
		workTimeTypeSelectorControl.setLayoutData(fd);
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
		columnTypeSelectorControl.setLayoutData(fd);
		// 设置列类型选择控件相对方案选择控件的顶部边距
		fd.top = new FormAttachment(programSelectorControl, MARGIN);
		// 设置列类型选择控件的底部边距
		fd.bottom = new FormAttachment(100, -MARGIN);
		// 设置列类型选择控件的左边边距
		fd.left = new FormAttachment(50, MARGIN / 2);
		// 设置列类型选择控件的右边边距
		fd.right = new FormAttachment(100, -MARGIN);

		// 从编辑器输入中获取数据，这个数据是project
		project = (Project) input.getData();
		// 侦听项目的项目模板id字段的值
		project.addFieldValueListener(Project.F_PROJECT_TEMPLATE_ID, this);
		// 返回容器
		return parent;
	}

	/**
	 * 创建工时类型选择器
	 * 
	 * @param parent
	 * @return
	 */
	private TableViewer createWorkTimeTypeSelector(Composite parent) {
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
		column.getColumn().setText("工时类型");
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
		column.getColumn().setText("工时类型选项");
		column.getColumn().setWidth(150);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// element是DBObject的工时类型，取出工时类型的id
				ObjectId workTimeTypeId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// 从项目中获得对应工时类型的选项
				DBObject workTimeTypeOption = project
						.getWorkTimeTypeOption(workTimeTypeId);
				// 当项目没有对应的工时类型选项时，返回空字符串
				if (workTimeTypeOption == null) {
					return "";
				}
				// 返回项目中对应工时类型的选项名称
				String optionDesc = (String) workTimeTypeOption
						.get(WorkTimeProgram.F_DESC);
				return optionDesc;
			}
		});
		// 为第二列设置编辑器
		column.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				// element是DBObject的工时类型，value是editor选择的DBObject的工时类型选项
				DBObject option = (DBObject) value;
				// 获取工时类型的id
				ObjectId workTimeTypeId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// 获取工时类型的名称
				String workTimeTypeDesc = (String) ((DBObject) element)
						.get(WorkTimeProgram.F_DESC);
				// 调用project的方法设置工时类型选项，传三个参数
				project.makeWorkTimeTypeOption(workTimeTypeId,
						workTimeTypeDesc, option);
				// 设置数据脏了
				setDirty(true);
				// 更新表查看器对应的工时类型元素
				tableViewer.update(element, null);
			}

			@Override
			protected Object getValue(Object element) {
				// element是DBObject的工时类型
				// 获取工时类型的id
				ObjectId workTimeTypeId = (ObjectId) ((DBObject) element)
						.get(WorkTimeProgram.F__ID);
				// 通过工时类型id，获取项目中的DBObject的工时类型选项
				DBObject workTimeTypeOptionOfProject = project
						.getWorkTimeTypeOption(workTimeTypeId);
				// 判断项目中的工时类型选项不为空
				if (workTimeTypeOptionOfProject != null) {
					// 获取项目中的工时类型选项的id
					ObjectId optionIdOfProject = (ObjectId) workTimeTypeOptionOfProject
							.get(WorkTimeProgram.F__ID);
					// 获取工时类型的BsonList选项
					BasicBSONList options = (BasicBSONList) ((DBObject) element)
							.get(WorkTimeProgram.F_WORKTIME_TYPE_OPTIONS);
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
				BasicBSONList workTimeTypeOptions = (BasicBSONList) ((DBObject) element)
						.get(WorkTimeProgram.F_WORKTIME_TYPE_OPTIONS);
				// 将工时类型选项存入下拉框查看器
				cv.setInput(workTimeTypeOptions);
				// 返回下拉框查看器
				return ce;
			}

			// 设置单元格可编辑
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		// ArrayContentProvider.getInstance()可以获得一个ArrayContentProvider
		// 它适用于两种情形的input，第一种是实现List接口的类，第二种是数组类型
		// List或者数组中的每个元素，在setInput以后会作为表格的元素存在
		// ArrayContentProvider提供的选择实现了IStructuredSelection
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		return tableViewer;
	}

	private TreeViewer createColumnTypeSelector(Composite parent) {
		// 1.创建树查看器,并作基本设置
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION
				| SWT.BORDER);
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		// 2.创建树查看器的列
		TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
		column.getColumn().setText("");
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
		column.getColumn().setText("");
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
						.containsField(WorkTimeProgram.F_WORKTIME_TYPE_OPTIONS)) {
					return null;
				}
				// element不是列类型时，为列类型选项，去列类型选项id
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

		// 3.创建编辑器
		column.setEditingSupport(new EditingSupport(treeViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				// element是DBObject的工时列类型选项，value是true/false
				WorkTimeProgram workTimeProgram = project.getWorkTimeProgram();
				DBObject type = workTimeProgram.getWorkTimeType(
						(ObjectId) ((DBObject) element)
								.get(WorkTimeProgram.F__ID),
						WorkTimeProgram.F_COLUMNTYPES);
				
				// 调用project的方法设置工时类型选项，传三个参数
				project.selectWorkTimeColumnTypeOption((ObjectId)type.get(WorkTimeProgram.F__ID),
						(String)type.get(WorkTimeProgram.F_DESC), (DBObject)element,Boolean.TRUE.equals(value));
				
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
						Project.F_WORKTIME_COLUMNTYPES);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(treeViewer.getTree());
			}

			@Override
			protected boolean canEdit(Object element) {
				// element是列类型或者列类型选项,只有列类型选项才可以编辑
				return !((DBObject) element)
						.containsField(WorkTimeProgram.F_WORKTIME_TYPE_OPTIONS);
			}
		});
		treeViewer.setContentProvider(new WorkTimeTypeOptionProvider());

		return treeViewer;
	}

	private ComboViewer createProgramSelector(Composite parent) {
		// 创建下拉框查看器
		ComboViewer cv = new ComboViewer(parent, SWT.BORDER);
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
				return (String) ((WorkTimeProgram) element).getDesc();
			}
		});
		// 为下拉框查看器添加选择改变的侦听器，这个侦听器是因为contentProvider是ArrayContentProvider
		cv.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// 创建结构选择器
				StructuredSelection selection = (StructuredSelection) event
						.getSelection();
				// 获取选择的第一个元素，这个元素是工时方案
				WorkTimeProgram workTimeProgram = (WorkTimeProgram) selection
						.getFirstElement();
				// 将选择的工时方案id保存到项目中
				project.makeSelectedWorkTimeProgram(workTimeProgram);
				// 设置数据脏了
				setDirty(true);
				// 设置工时类型选择器的Input,传一个参数是工时方案
				setWorkTimeTypeSelectorInput(workTimeProgram);
				// 设置列类型选择器的Input，传入工时方案
				setColumnTypeSelectorInput(workTimeProgram);
				
			}
		});
		return cv;
	}

	/**
	 * 设置列类型选择器的Input
	 * @param workTimeProgram
	 */
	protected void setColumnTypeSelectorInput(WorkTimeProgram workTimeProgram) {
		BasicBSONList columnTypes = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_COLUMNTYPES);
		columnTypeSelector.setInput(columnTypes);
	}

	/**
	 * 设置工时类型选择器的input
	 * 
	 * @param workTimeProgram
	 */
	private void setWorkTimeTypeSelectorInput(WorkTimeProgram workTimeProgram) {
		// 获取工时类型
		BasicBSONList workTimeTypes = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIMETYPES);
		// BasicBsonList继承与ArrayList，而workTimeTypes中的每个元素是DBObject，包括了F_id,F_desc,F_Type_Options三个字段
		workTimeTypeSelector.setInput(workTimeTypes);
	}

	/**
	 * 设置方案选择器的input，传入的参数是项目模板
	 * 
	 * @param projectTemplate
	 */
	private void setProgramSelectorInput(PrimaryObject projectTemplate) {
		// 获取项目模板中关联的工时方案，BSonList类型
		BasicBSONList workTimePrograms = (BasicBSONList) projectTemplate
				.getValue(ProjectTemplate.F_WORKTIMEPROGRAMS);
		// 为cv设置input
		// input是一个arraylist
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

}
