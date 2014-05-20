package com.sg.business.management.editor;

import java.text.DecimalFormat;
import java.text.Format.Field;
import java.text.NumberFormat;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.labelprovider.PrimaryObjectLabelProvider;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.part.editor.page.IEditorPageLayoutProvider;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class WorkTimeDataPageDelegator extends AbstractFormPageDelegator {

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

	private GridTreeViewer viewer;
	private PrimaryObjectEditorInput input;
	private WorkTimeProgram workTimeProgram;

	public WorkTimeDataPageDelegator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void commit(boolean onSave) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean createBody() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public IEditorPageLayoutProvider getPageLayout() {
		// TODO Auto-generated method stub
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
				fd.top = new FormAttachment(body, 4);
				fd.left = new FormAttachment();
				fd.right = new FormAttachment(100);
				fd.bottom = new FormAttachment(100);
			}
		};
	}

	@Override
	public Composite createPageContent(IManagedForm mForm, Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		// TODO Auto-generated method stub
		super.createPageContent(mForm, parent, input, conf);
		workTimeProgram = (WorkTimeProgram) input.getData();

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		createTree(composite);

		return composite;
	}

	private void createTree(Composite composite) {
		viewer = new GridTreeViewer(composite, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getGrid().setHeaderVisible(true);
		viewer.setContentProvider(getContentProvider());
		viewer.setAutoExpandLevel(2);
		createFristColumn();
		createGridColumns();

		createGridViewerEditor();

		setInput();
	}

	private void setInput() {
		Object value = workTimeProgram
				.getValue(WorkTimeProgram.F_WORKTIMETYPES);
		viewer.setInput(value);
	}

	private void createFristColumn() {
		GridColumn labelColumn = new GridColumn(viewer.getGrid(), SWT.NONE);
		labelColumn.setWidth(180);
		labelColumn.setAlignment(SWT.LEFT);
		labelColumn.setHeaderFont(font);
		labelColumn.setText(""); //$NON-NLS-1$

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
		BasicBSONList columnTypes = (BasicBSONList) workTimeProgram
				.getValue(WorkTimeProgram.F_COLUMNTYPES);
		// 2.根据列类型创建列组
		for (int i = 0; i < columnTypes.size(); i++) {
			createColumnGroup((DBObject) columnTypes.get(i));
		}
	}

	private void createColumnGroup(DBObject columnType) {
		// 1.创建列组
		GridColumnGroup group = new GridColumnGroup(viewer.getGrid(),
				SWT.CENTER);
		// 2.取出列类型名称
		String groupText = (String) columnType.get(WorkTimeProgram.F_DESC);
		// 3.设置为列组的名称
		group.setText(groupText);
		// 4.取出列类型选项
		BasicBSONList options = (BasicBSONList) columnType
				.get(WorkTimeProgram.F_TYPE_OPTIONS);
		// 5.根据列类型选项创建列
		for (int i = 0; i < options.size(); i++) {
			final DBObject columnTypeOption = (DBObject) options.get(i);
			GridColumn column = new GridColumn(group, SWT.NONE);
			column.setWidth(120);
			column.setAlignment(SWT.CENTER);
			column.setDetail(true);
			column.setSummary(false);
			// 设置列名称
			String columnText = (String) columnTypeOption
					.get(WorkTimeProgram.F_DESC);
			column.setText(columnText); //$NON-NLS-1$
			GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
			vColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					// 判断元素是工时类型还是工时类型选项
					DBObject workTimeTypeOrOption = (DBObject) element;
					if (workTimeTypeOrOption
							.containsField(WorkTimeProgram.F_TYPE_OPTIONS)) {
						return "";
					}
					double amount = getAmount((ObjectId) workTimeTypeOrOption
							.get(WorkTimeProgram.F__ID),
							(ObjectId) columnTypeOption
									.get(WorkTimeProgram.F__ID));
					DecimalFormat df=new DecimalFormat(Utils.NF_NUMBER_P2);
					return df.format(amount);
				}
			});
		}
	}

	protected double getAmount(ObjectId workTimeTypeOption,
			ObjectId columnTypeOption) {
		return (Double) null;
	}

	private void createGridViewerEditor() {
		// TODO Auto-generated method stub

	}

	private IContentProvider getContentProvider() {
		return new OptionProvider();
	}
}
