package com.sg.business.home.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IPresentableObject;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
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

		viewer = new TableViewer(parent, SWT.NONE);
		viewer.getTable().setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		final SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT);
		new ColumnAutoResizer(viewer.getTable(), column.getColumn());
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {

					DBObject dbo = (DBObject) element;

					ObjectId id = (ObjectId) dbo.get("id");
					String desc = (String) dbo.get("desc");
					String col = (String) dbo.get("col");
					String db = (String) dbo.get("db");

					Class<? extends PrimaryObject> modelClass = ModelService
							.getModelClass(db, col);

					PrimaryObject po = ModelService.createModelObject(
							modelClass, id,false);

					StringBuffer sb = new StringBuffer();
					sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:9pt'>");

					sb.append("<span style='float:right'>");
					Long date = (Long) dbo.get("date");
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(date.longValue());
					sb.append("   "+sdf.format(cal.getTime()));
					sb.append("</span>");
					
					if (po==null) {
						sb.append("<del>");
						sb.append(desc);
						sb.append("</del>");
					} else {
						String typeName = po.getTypeName();
						sb.append(typeName + ": ");
						String label = po.getLabel();
						label = Utils.getLimitLengthString(label, 20);
						sb.append(label);
					}


					sb.append("</span>");
					return sb.toString();
				}
				return super.getText(element);
			}

			@Override
			public Image getImage(Object element) {
				DBObject dbo = (DBObject) element;
				ObjectId id = (ObjectId) dbo.get("id");
				String col = (String) dbo.get("col");
				String db = (String) dbo.get("db");
				Class<? extends PrimaryObject> modelClass = ModelService
						.getModelClass(db, col);

				PrimaryObject po = ModelService.createModelObject(modelClass,
						id);
				if (po != null) {
					return po.getImage();
				} else {
					return null;
				}
			}
		});
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		setInput();
		getSite().setSelectionProvider(viewer);
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

			ObjectId id = (ObjectId) dbo.get("id");
			String col = (String) dbo.get("col");
			String db = (String) dbo.get("db");
			String editor = (String) dbo.get("editor");
			boolean editable = Boolean.TRUE.equals(dbo.get("editable"));

			String type = (String) dbo.get("type");

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

}
