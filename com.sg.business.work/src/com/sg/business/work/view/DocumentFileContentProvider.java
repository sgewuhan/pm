package com.sg.business.work.view;

import java.util.List;

import com.mobnut.db.file.IServerFile;
import com.sg.business.model.Document;
import com.sg.widgets.viewer.RelationContentProvider;

public class DocumentFileContentProvider extends RelationContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Document) {
			Document doc = (Document) parentElement;
			List<IServerFile> files = doc.getServerFileValue();
			return files.toArray();
		} else {
			return new Object[0];
		}
	}

	@Override
	public boolean hasChildren(Object parentElement) {
		return parentElement instanceof Document;
//		return getChildren(parentElement).length > 0;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

}
