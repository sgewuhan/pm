package com.sg.business.commons.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerColumn;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.editingsupport.IEditingSupportor;
import com.sg.widgets.registry.config.ColumnConfigurator;

public abstract class NavigatorSelectorEditing implements IEditingSupportor {

	public class NavigatorEditingSupport extends EditingSupport {

		private ColumnConfigurator configurator;
		private ColumnViewer viewer;
		private CellEditor editor;
		private String navigatorId;

		public NavigatorEditingSupport(String navigatorId,
				final ColumnViewer viewer, final ColumnConfigurator configurator) {
			super(viewer);
			this.configurator = configurator;
			this.viewer = viewer;
			this.navigatorId = navigatorId;
			this.editor = createEditor();
		}

		private CellEditor createEditor() {
			return new NavigatorEditor(navigatorId, viewer) {

				@Override
				protected Object getValueFromSelection(IStructuredSelection is) {
					return NavigatorSelectorEditing.this
							.getValueFromSelection(is);
				}

			};
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		@Override
		protected boolean canEdit(Object element) {
			return NavigatorSelectorEditing.this.canEdit(element);
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof PrimaryObject) {
				PrimaryObject po = (PrimaryObject) element;
				return po.getValue(configurator.getColumn());
			}
			return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof PrimaryObject) {
				PrimaryObject po = (PrimaryObject) element;
				po.setValue(configurator.getColumn(), value);
				viewer.update(po, null);
			}
		}

	}

	public NavigatorSelectorEditing() {
	}

	protected Object getValueFromSelection(IStructuredSelection is) {
		if (is == null || is.isEmpty()) {
			return null;
		}
		return is.getFirstElement();
	}

	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	public EditingSupport createEditingSupport(ColumnViewer viewer,
			ViewerColumn viewerColumn, ColumnConfigurator configurator) {

		return new NavigatorEditingSupport(getNavigatorId(), viewer,
				configurator);
	}

	protected abstract String getNavigatorId();

}
