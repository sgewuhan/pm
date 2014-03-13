package com.sg.business.commons.ui.block;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.list.ListBlock;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.editor.DataObjectEditor;

public class DocBlock extends ListBlock {

	private static final String PERSPECTIVE = "perspective.vault";

	public DocBlock(Composite parent) {
		super(parent);
	}
	
	@Override
	protected Object doGetData() {
		int limit = getCountByHeight();
		
		java.util.List<DBObject> documentList = new ArrayList<DBObject>();

		String consignerId = context.getConsignerId();
		User user = UserToolkit.getUserById(consignerId);
		java.util.List<DBObject> result = user.getLastOpen();
		for (DBObject dbo : result) {
			String col = (String) dbo.get("col");
			if (IModelConstants.C_DOCUMENT.equals(col)) {
				documentList.add(dbo);
				limit--;
				if (limit == 0) {
					break;
				}
			}
		}
		return documentList;
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
		} else {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			if (element instanceof BasicDBObject) {
				BasicDBObject dbo = (BasicDBObject) element;
				ObjectId _id = (ObjectId) dbo.get("id");
				String editorId = (String) dbo.get("editor");
				boolean editable = Boolean.TRUE.equals(dbo.get("editable"));
				try {
					DataObjectEditor.open(_id, editorId, editable, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		}
	}

	@Override
	protected HTMLAdvanceLabelProvider getLabelProvider() {
		return new HTMLAdvanceLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					ObjectId _id = (ObjectId) dbo.get("id");
					Document document = ModelService.createModelObject(
							Document.class, _id);
					if (document.get_data() != null) {
						return super.getText(document);
					} else{
						StringBuffer sb = new StringBuffer();
						sb.append("<span style='font-family:Î¢ÈíÑÅºÚ;font-size:9pt;color:#ff0000;margin: 8px'><del>"); //$NON-NLS-1$
						sb.append((String) dbo.get("desc"));
						sb.append("</del></span>"); //$NON-NLS-1$
						return sb.toString();
					}
				}
				return "";
			}
		};
	}


	@Override
	protected String getPerspective() {
		return PERSPECTIVE;
	}

	@Override
	protected void select(Object element) {
		if (element instanceof BasicDBObject) {
			BasicDBObject dbo = (BasicDBObject) element;
			ObjectId _id = (ObjectId) dbo.get("id");
			String editorId = (String) dbo.get("editor");
			boolean editable = Boolean.TRUE.equals(dbo.get("editable"));
			try {
				DataObjectEditor.open(_id, editorId, editable, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}		
	}

}
