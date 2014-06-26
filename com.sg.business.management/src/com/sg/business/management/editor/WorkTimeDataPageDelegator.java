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
	 * @author �׳���
	 *
	 */

	private class WorkTimeDataEditingSupport extends EditingSupport {

		private ObjectId paraYOptionId;

		/**
		 * ���췽��
		 * 
		 * @param paraYOptionId
		 *            ������ѡ��id
		 */
		public WorkTimeDataEditingSupport(ObjectId paraYOptionId) {
			// ���ø���Ĺ��췽��
			super(viewer);
			// �������������ѡ��id��ֵ��ȫ�ֱ���������ѡ��id
			this.paraYOptionId = paraYOptionId;
		}

		/**
		 * ���ر༭��
		 * 
		 * @param element
		 *            ��ʱ���ͻ�ʱ����ѡ��
		 * @return
		 */
		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		/**
		 * elementΪ��ʱ����ʱ�����ɱ༭
		 * 
		 * @param element
		 *            ��ʱ���ͻ�ʱ����ѡ��
		 * @return
		 */
		@Override
		protected boolean canEdit(Object element) {
			DBObject paraXOrParaXOption = (DBObject) element;
				// ��elementǿתΪDBObject����
				// ����paraXOrOption��������ʱ����ѡ��
				return !paraXOrParaXOption
						.containsField(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
		}

		/**
		 * ��ȡ��ʱ����
		 * 
		 * @param element
		 *            ��ʱ���ͻ�ʱ����ѡ��
		 * @return
		 */
		@Override
		protected Object getValue(Object element) {
			// ��elementǿתΪDBObject����
			DBObject paraXOrOption = (DBObject) element;
			// ��ȡ��ʱ����ѡ���id
			ObjectId paraXOptionId = (ObjectId) paraXOrOption
					.get(WorkTimeProgram.F__ID);
			// ��ȡ��ʱ���ݣ���������������һ�������ǹ�ʱ����ѡ��id���ڶ���������������ѡ��id
			Double amount = workTimeProgram.getWorkTimeData(paraXOptionId,
					paraYOptionId);
			return amount;
		}

		/**
		 * ���湤ʱ���ݣ������¹�ʱ���ͻ�ʱ����ѡ��
		 * 
		 * @param element
		 *            ��ʱ���ͻ�ʱ����ѡ��
		 * @param value
		 *            ��ʱ����
		 */
		@Override
		protected void setValue(Object element, Object value) {
			// �жϹ�ʱ����Ϊ�ջ�ʱ������Double����
			if (value == null || value instanceof Double) {
				// ����ʱ���ͻ�ʱ����ѡ��ǿתΪDBObject����
				DBObject paraXOrOption = (DBObject) element;
				// ��ȡ��ʱ����ѡ��id
				ObjectId paraXOptionId = (ObjectId) paraXOrOption
						.get(WorkTimeProgram.F__ID);
				// �滻�Ѵ��ڵĹ�ʱ���ݻ��¼�һ����ʱ����
				workTimeProgram.makeWorkTimeData(paraXOptionId, paraYOptionId,
						(Double) value);
				// ������������
				setDirty(true);
				// ���¹�ʱ���ͻ�ʱ����ѡ��
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
		labelColumn.setText("��ʱ���ͼ�ѡ��"); //$NON-NLS-1$

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
		// 1.��ȡ����ʱ������������
		BasicBSONList paraY = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_Y);
		// 2.���������ʹ�������
		for (int i = 0; i < paraY.size(); i++) {
			createColumnGroup((DBObject) paraY.get(i));
		}

	}

	private void createColumnGroup(DBObject paraY) {
		// 1.��������
		GridColumnGroup group = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		group.setExpanded(true);

		// 2.ȡ������������
		String groupText = (String) paraY.get(WorkTimeProgram.F_DESC);
		// 3.����Ϊ���������
		group.setText(groupText);
		// 4.ȡ��������ѡ��
		BasicBSONList options = (BasicBSONList) paraY
				.get(WorkTimeProgram.F_WORKTIME_PARA_OPTIONS);
		
		// 2014.6.23 �������������Ч������
		createSummaryColumn(group);
		
		// 5.����������ѡ�����
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
		// ����������
		String columnText = (String) paraYOption.get(WorkTimeProgram.F_DESC);
		column.setText(columnText); //$NON-NLS-1$
		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		vColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// �ж�Ԫ���ǹ�ʱ���ͻ��ǹ�ʱ����ѡ��
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
				// 2014.6.23 �޸Ĺ�ʱ������ʾ������
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
