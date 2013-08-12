package com.sg.business.management.editor.page;

import java.util.ArrayList;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.AbstractOptionFilterableItem;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.INavigatorPageBodyPartCreater;
import com.sg.widgets.viewer.CTreeViewer;

public class DeliverablesBody implements INavigatorPageBodyPartCreater {

	@Override
	public void createNavigatorBody(Composite body, NavigatorControl navi,
			PrimaryObjectEditorInput input) {
		navi.createPartContent(body);
		// 创建选项列
		// 读取选择项
		CTreeViewer viewer = (CTreeViewer) navi.getViewer();
		ProjectTemplate projectTemplate = (ProjectTemplate) input.getData();
		ArrayList<?> standardSet = (ArrayList<?>) projectTemplate
				.getStandardOptionSet();
		createColumns(standardSet, viewer, "标准");

		ArrayList<?> productTypeSet = (ArrayList<?>) projectTemplate
				.getProductOptionSet();
		createColumns(productTypeSet, viewer, "产品类型");

		ArrayList<?> projectTypeSet = (ArrayList<?>) projectTemplate
				.getProjectOptionSet();
		createColumns(projectTypeSet, viewer, "项目类型");

	}

	private void createColumns(ArrayList<?> set, final CTreeViewer viewer,
			final String optionSetName) {
		if (set == null) {
			return;
		}
		TreeViewerColumn col;
		for (int i = 0; i < set.size(); i++) {
			final String optionName = (String) set.get(i);
			col = new TreeViewerColumn(viewer, SWT.CENTER);
			if(i==0){
				col.getColumn().setText(optionSetName + "\n" + optionName);
			}else{
				col.getColumn().setText("\n"+optionName);
			}
			col.getColumn().setWidth(80);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof AbstractOptionFilterableItem) {
						return ((AbstractOptionFilterableItem) element)
								.getOptionValueSetting(optionSetName,
										optionName);
					}
					return "";
				}
			});
			final CellEditor editor = new ComboBoxCellEditor(viewer.getTree(),
					AbstractOptionFilterableItem.VALUE_SET, SWT.READ_ONLY);

			col.setEditingSupport(new EditingSupport(viewer) {

				@Override
				protected void setValue(Object element, Object value) {
					if (value instanceof Integer) {
						try {
							String setValue = AbstractOptionFilterableItem.VALUE_SET[((Integer) value)
									.intValue()];
							AbstractOptionFilterableItem item = (AbstractOptionFilterableItem) element;
							item.doSetOptionValueSetting(optionSetName,
									optionName, setValue,
									new CurrentAccountContext());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					viewer.update(element, null);
				}

				@Override
				protected Object getValue(Object element) {
					String value = ((AbstractOptionFilterableItem) element)
							.getOptionValueSetting(optionSetName, optionName);
					for (int i = 0; i < AbstractOptionFilterableItem.VALUE_SET.length; i++) {
						if (AbstractOptionFilterableItem.VALUE_SET[i]
								.equals(value)) {
							return new Integer(i);
						}
					}
					return new Integer(0);
				}

				@Override
				protected CellEditor getCellEditor(Object element) {
					return editor;
				}

				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
			});
		}

	}

}
