package com.sg.business.management.editor;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.viewer.ParaXOptionProvider;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.editingsupport.TextPopupCellEditor;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class WorkTimeDataPageDelegator extends AbstractFormPageDelegator {

	/**
	 * @author 雷成洋
	 *
	 */

	private class WorkTimeDataEditingSupport extends EditingSupport {

		private ObjectId paraYOptionId;

		/**
		 * 构造方法
		 * 
		 * @param paraYOptionId
		 *            列类型选项id
		 */
		public WorkTimeDataEditingSupport(ObjectId paraYOptionId) {
			// 调用父类的构造方法
			super(viewer);
			// 将传入的列类型选项id赋值给全局变量列类型选项id
			this.paraYOptionId = paraYOptionId;
		}

		/**
		 * 返回编辑器
		 * 
		 * @param element
		 *            工时类型或工时类型选项
		 * @return
		 */
		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		/**
		 * element为工时类型时，不可编辑
		 * 
		 * @param element
		 *            工时类型或工时类型选项
		 * @return
		 */
		@Override
		protected boolean canEdit(Object element) {
			DBObject paraXOrParaXOption = (DBObject) element;
				// 将element强转为DBObject类型
				// 返回paraXOrOption不包含工时类型选项
				return !paraXOrParaXOption
						.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
		}

		/**
		 * 获取工时数据
		 * 
		 * @param element
		 *            工时类型或工时类型选项
		 * @return
		 */
		@Override
		protected Object getValue(Object element) {
			// 将element强转为DBObject类型
			DBObject paraXOrOption = (DBObject) element;
			// 获取工时类型选项的id
			ObjectId paraXOptionId = (ObjectId) paraXOrOption
					.get(WorkTimeProgram.F__ID);
			// 获取工时数据，传两个参数，第一个参数是工时类型选项id，第二个参数是列类型选项id
			Double amount = workTimeProgram.getWorkTimeData(paraXOptionId,
					paraYOptionId);
			return amount;
		}

		/**
		 * 保存工时数据，并更新工时类型或工时类型选项
		 * 
		 * @param element
		 *            工时类型或工时类型选项
		 * @param value
		 *            工时数据
		 */
		@Override
		protected void setValue(Object element, Object value) {
			// 判断工时数据为空或工时数据是Double类型
			if (value == null || value instanceof Double) {
				// 将工时类型或工时类型选项强转为DBObject类型
				DBObject paraXOrOption = (DBObject) element;
				// 获取工时类型选项id
				ObjectId paraXOptionId = (ObjectId) paraXOrOption
						.get(WorkTimeProgram.F__ID);
				// 替换已存在的工时数据或新加一条工时数据
				workTimeProgram.makeWorkTimeData(paraXOptionId, paraYOptionId,
						(Double) value);
				// 设置数据已脏
				setDirty(true);
				// 更新工时类型或工时类型选项
				viewer.update(element, null);
			}

		}

	}

	private GridTreeViewer viewer;
	private WorkTimeProgram workTimeProgram;
	private TextPopupCellEditor editor;

	public WorkTimeDataPageDelegator() {
	}

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
		setFormInput(input);
		workTimeProgram = (WorkTimeProgram) input.getData();

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		createTree(composite);

		return composite;
	}

	private void createTree(Composite composite) {
		viewer = new GridTreeViewer(composite, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.getGrid().setHeaderVisible(true);
		viewer.setContentProvider(getContentProvider());
		viewer.setAutoExpandLevel(2);
		createFristColumn();
		editor = new TextPopupCellEditor((Composite) viewer.getControl(),
				Utils.TYPE_DOUBLE);

		createGridColumns();
		viewer.getControl().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent event) {
				editor.dispose();
			}
		});

		setInput();
	}

	private void setInput() {
		Object value = workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
		viewer.setInput(value);
	}

	private void createFristColumn() {
		GridColumn labelColumn = new GridColumn(viewer.getGrid(), SWT.NONE);
		labelColumn.setWidth(180);
		labelColumn.setAlignment(SWT.LEFT);
		labelColumn.setHeaderFont(font);
		labelColumn.setText("工时类型及选项"); //$NON-NLS-1$

		GridViewerColumn vColumn = new GridViewerColumn(viewer, labelColumn);
		vColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					return "" + ((DBObject) element).get(PrimaryObject.F_DESC);
				}
				return super.getText(element);
			}
		});

	}

	private void createGridColumns() {
		// 1.获取本工时方案的列类型
		BasicBSONList paraY = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_Y);
		// 2.根据列类型创建列组
		for (int i = 0; i < paraY.size(); i++) {
			createColumnGroup((DBObject) paraY.get(i));
		}

	}

	private void createColumnGroup(DBObject paraY) {
		// 1.创建列组
		GridColumnGroup group = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		group.setExpanded(true);

		// 2.取出列类型名称
		String groupText = (String) paraY.get(WorkTimeProgram.F_DESC);
		// 3.设置为列组的名称
		group.setText(groupText);
		// 4.取出列类型选项
		BasicBSONList options = (BasicBSONList) paraY
				.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
		
		// 2014.6.23 解决收缩功能无效的问题
		createSummaryColumn(group);
		
		// 5.根据列类型选项创建列
		for (int i = 0; i < options.size(); i++) {
			final DBObject paraYOption = (DBObject) options.get(i);
			createGridColumn(group, paraYOption);
		}

	}

	private void createSummaryColumn(GridColumnGroup group) {
		GridColumn column = new GridColumn(group, SWT.NONE);
		column.setWidth(80);
		column.setAlignment(SWT.CENTER);
		column.setDetail(false);
		column.setSummary(true);
		GridViewerColumn gvColumn = new GridViewerColumn(viewer, column);
		gvColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return "";
			}
			
			@Override
			public Color getBackground(Object element) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
			}
		});
	}

	private void createGridColumn(GridColumnGroup group,
			final DBObject paraYOption) {
		GridColumn column = new GridColumn(group, SWT.NONE);
		column.setWidth(80);
		column.setAlignment(SWT.CENTER);
		column.setDetail(true);
		column.setSummary(false);
		// 设置列名称
		String columnText = (String) paraYOption.get(WorkTimeProgram.F_DESC);
		column.setText(columnText); //$NON-NLS-1$
		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		vColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// 判断元素是工时类型还是工时类型选项
				DBObject paraXOrOption = (DBObject) element;
				if (paraXOrOption
						.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS)) {
					return "";
				}
				Double amount = workTimeProgram.getWorkTimeData(
						(ObjectId) paraXOrOption.get(WorkTimeProgram.F__ID),
						(ObjectId) paraYOption.get(WorkTimeProgram.F__ID));
				if (amount == null) {
					return "";
				}
				// 2014.6.23 修改工时数据显示的问题
				/*
				 * DecimalFormat df = new DecimalFormat(Utils.NF_NUMBER_P2);
				 * return df.format(amount);
				 */
				
				return String.format("%.1f",amount);
			}
		});
		if(workTimeProgram.canEdit(getInput().getContext())){
			vColumn.setEditingSupport(new WorkTimeDataEditingSupport(
					(ObjectId) paraYOption.get(WorkTimeProgram.F__ID)));
		}

	}

	private IContentProvider getContentProvider() {
		return new ParaXOptionProvider();
	}
}
