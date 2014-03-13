package com.sg.business.home.view;

import java.util.Calendar;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IPresentableObject;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.UIConstants;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatablePart;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ColumnAutoResizer;

public class LastOpened extends ViewPart implements INavigatablePart {

	private TableViewer viewer;

	public LastOpened() {
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setLinesVisible(true);
		UIFrameworkUtils.enableMarkup(table);

		TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT);
		new ColumnAutoResizer(table, column.getColumn());
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (!check(element)) {
					return ""; //$NON-NLS-1$
				}

				try {

					DBObject dbo = (DBObject) element;

					ObjectId id = (ObjectId) dbo.get("id"); //$NON-NLS-1$
					String desc = (String) dbo.get("desc"); //$NON-NLS-1$
					String col = (String) dbo.get("col"); //$NON-NLS-1$
					String db = (String) dbo.get("db"); //$NON-NLS-1$

					Class<? extends PrimaryObject> modelClass = ModelService
							.getModelClass(db, col);

					PrimaryObject po = ModelService.createModelObject(
							modelClass, id, false);

					StringBuffer sb = new StringBuffer();
					sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:9pt'>"); //$NON-NLS-1$

					sb.append("<span style='float:right'>"); //$NON-NLS-1$
					Long date = (Long) dbo.get("date"); //$NON-NLS-1$
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(date.longValue());
					sb.append("   " + String.format(Utils.FORMATE_DATE_COMPACT_SASH, cal)); //$NON-NLS-1$
					sb.append("</span>"); //$NON-NLS-1$

					if (po == null) {
						sb.append("<del>"); //$NON-NLS-1$
						sb.append(desc);
						sb.append("</del>"); //$NON-NLS-1$
					} else {
						String typeName = po.getTypeName();
						sb.append(typeName + ": "); //$NON-NLS-1$
						String label = po.getLabel();
						label = Utils.getLimitLengthString(label, 20);
						sb.append(label);
					}

					sb.append("</span>"); //$NON-NLS-1$
					return sb.toString();
				} catch (Exception e) {
				}
				return super.getText(element);
			}

			@Override
			public Image getImage(Object element) {
				if (!check(element)) {
					return null;
				}
				DBObject dbo = (DBObject) element;
				ObjectId id = (ObjectId) dbo.get("id"); //$NON-NLS-1$
				String col = (String) dbo.get("col"); //$NON-NLS-1$
				String db = (String) dbo.get("db"); //$NON-NLS-1$
				Class<? extends PrimaryObject> modelClass = ModelService
						.getModelClass(db, col);

				PrimaryObject po = ModelService.createModelObject(modelClass,
						id);
				try {
					if (po != null) {
						return po.getImage();
					}
				} catch (Exception e) {
				}
				return null;
			}
		});
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		setInput();
		getSite().setSelectionProvider(viewer);
	}

	protected boolean check(Object element) {
		if (!(element instanceof DBObject)) {
			return false;
		}

		DBObject dbo = (DBObject) element;
		Object value = dbo.get("id"); //$NON-NLS-1$
		if (!(value instanceof ObjectId)) {
			return false;
		}
		value = dbo.get("col"); //$NON-NLS-1$
		if (!(value instanceof String)) {
			return false;
		}
		value = dbo.get("db"); //$NON-NLS-1$
		if (!(value instanceof String)) {
			return false;
		}

		return true;
	}

	private void setInput() {
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();

		User user = UserToolkit.getUserById(userId);
		List<DBObject> result = user.getLastOpen();
		viewer.setInput(result.toArray());
	}

	@Override
	public void setFocus() {
		viewer.getTable().setFocus();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {
		setInput();
	}

	@Override
	public void setMasterChanged(PrimaryObject master, PrimaryObject oldMaster,
			IWorkbenchPart part) {
	}

	@Override
	public void reloadMaster() {

	}

	@Override
	public boolean canEdit() {
		return viewer.getSelection() != null
				&& !viewer.getSelection().isEmpty();
	}

	@Override
	public boolean canCreate() {
		return false;
	}

	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public boolean canRead() {
		return false;
	}

	@Override
	public boolean hasMultiEditor() {
		return false;
	}

	@Override
	public void doEdit() {
		IStructuredSelection is = (IStructuredSelection) viewer.getSelection();
		Object data = is.getFirstElement();
		if (data instanceof DBObject) {
			DBObject dbo = (DBObject) data;

			ObjectId id = (ObjectId) dbo.get("id"); //$NON-NLS-1$
			String col = (String) dbo.get("col"); //$NON-NLS-1$
			String db = (String) dbo.get("db"); //$NON-NLS-1$
			String editor = (String) dbo.get("editor"); //$NON-NLS-1$
			boolean editable = Boolean.TRUE.equals(dbo.get("editable")); //$NON-NLS-1$

			String type = (String) dbo.get("type"); //$NON-NLS-1$

			Class<? extends PrimaryObject> modelClass = ModelService
					.getModelClass(db, col);

			PrimaryObject po = ModelService.createModelObject(modelClass, id);

			if (po == null) {
				return;
			}

			if (UIConstants.DATAEDITOR.equals(type)) {
				try {
					DataObjectEditor.open(po, editor, editable, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}

		}
	}

	@Override
	public void doCreate() {
	}

	@Override
	public void doDelete() {
	}

	@Override
	public void doCreate(String editorId) {
	}

	@Override
	public void doEdit(String editorId, String pageId) {
		doEdit();
	}

	@Override
	public void doEdit(String editorId, String pageId, String opentype,
			Boolean editable) {
		doEdit();
	}

	@Override
	public boolean canExport() {
		return false;
	}

	@Override
	public void doExport() {
	}

	@Override
	public boolean canImport() {
		return false;
	}

	@Override
	public void doImport() {
	}

	@Override
	public boolean canProvideComparableObject() {
		return false;
	}

	@Override
	public List<IPresentableObject> getPresentableObject() {
		return null;
	}

	@Override
	public NavigatorControl getNavigator() {
		return null;
	}

	@Override
	public IToolBarManager getToolBarManager() {
		return getToolBarManager();
	}

	@Override
	public void activate() {
		setFocus();
	}

	@Override
	public void setNavigatorControl(NavigatorControl navigatorControl) {
	}

}
