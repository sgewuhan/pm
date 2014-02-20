package com.sg.business.pm2.home.widget;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.Block;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.editor.DataObjectEditor;

public class DocBlock extends Block implements ISelectionChangedListener {

	private static final int ITEM_HIGHT = 36;
	private static final String PERSPECTIVE = "perspective.vault";
	private ListViewer viewer;

	public DocBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void createContent(Composite parent) {
		parent.setLayout(new FormLayout());

		viewer = new ListViewer(parent, SWT.SINGLE);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new HTMLAdvanceLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					DBObject dbo = (DBObject) element;
					ObjectId _id = (ObjectId) dbo.get("id");
					Document document = ModelService.createModelObject(
							Document.class, _id);
					return super.getText(document);
				}
				return "";
			}
		});
		viewer.setUseHashlookup(true);
		viewer.addSelectionChangedListener(this);

		List list = viewer.getList();
		list.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(ITEM_HIGHT));
		FormData fd = new FormData();
		list.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);

		doRefresh();
	}

	@Override
	public void doRefresh() {
		int limit = getContentHeight() / ITEM_HIGHT;
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

		viewer.setInput(documentList);
		super.doRefresh();
	}

	public int getContentHeight() {
		return 201;
	}

	@Override
	protected void go() {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			workbench.showPerspective(PERSPECTIVE, window);
		} catch (WorkbenchException e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
		} else {
			Object element = ((IStructuredSelection)selection) .getFirstElement();
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

}
