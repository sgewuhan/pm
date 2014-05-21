package com.sg.business.commons.column.editingsupport;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.editingsupport.IEditingSupportor;
import com.sg.widgets.commons.editingsupport.TextPopupCellEditor;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class PlanWorksEditing implements IEditingSupportor {

	@Override
	public EditingSupport createEditingSupport(ColumnViewer viewer,
			ViewerColumn viewerColumn, final ColumnConfigurator configurator) {

		final IContext content = new CurrentAccountContext();
		
		final CellEditor editor = new TextPopupCellEditor((Composite) viewer.getControl(),Utils.TYPE_DOUBLE);
		
		EditingSupport es = new EditingSupport(viewer) {

			@Override
			protected CellEditor getCellEditor(Object element) {
				return editor;
			}

			@Override
			protected boolean canEdit(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					if(work.isSummaryWork()){
						return false;
					}else{
						return work.canEdit(content);
					}
				}
				return false;
			}

			@Override
			protected Object getValue(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					return work.getValue(configurator.getColumn());
				}
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Work) {
					Work work = (Work) element;
					work.setValue(Work.F_PLAN_WORKS, value);
					try {
						work.doSave(new CurrentAccountContext());
						List<Work> toUpdate = work.getAllParents();
						toUpdate.add(work);
						getViewer().update(toUpdate.toArray(), null);
					} catch (Exception e) {
						MessageUtil.showToast(e);
						try {
							work.reload(Work.F_PLAN_WORKS);
						} catch (Exception e1) {
						}
					}
				}
			}

		};
		return es;
	}


}
