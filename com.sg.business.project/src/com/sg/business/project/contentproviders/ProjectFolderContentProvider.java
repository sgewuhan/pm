package com.sg.business.project.contentproviders;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.widgets.viewer.RelationContentProvider;

public class ProjectFolderContentProvider extends RelationContentProvider {



	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List<?>) {
			List<?> list = (List<?>) inputElement;
			Collections.sort(list, new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					if (o1 instanceof Folder && o2 instanceof Document) {
						return -1;
					} else if(o1 instanceof Document && o2 instanceof Folder){
						return 1;
					} else {
						return 0;
					}
				}

			});

		}
		return super.getElements(inputElement);
	}
}
