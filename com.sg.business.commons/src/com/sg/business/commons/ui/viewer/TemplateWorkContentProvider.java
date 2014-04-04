package com.sg.business.commons.ui.viewer;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Work;

public class TemplateWorkContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<Work>) inputElement).toArray(new Work[0]);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Object[] result = new Object[0];
		if (parentElement instanceof Work) {
			Work work = (Work) parentElement;
			// if(work.isPersistent()){
			// List<PrimaryObject> deliverList = work.getDeliverable();
			// result=new Object[deliverList.size()];
			// int i=0;
			// for (PrimaryObject po : deliverList) {
			// po.setParentPrimaryObject(work);
			// result[i++]=po;
			// }
			// }else{
			Object value = work.getValue(Work.TEMPLATE_DELIVERABLE);

			Object item;
			if (value instanceof List<?>) {
				result = new Object[((List<?>) value).size()];
				for (int i = 0; i < ((List<?>) value).size(); i++) {
					item = ((List<?>) value).get(i);
					result[i] = ModelService.createModelObject((DBObject) item,
							Deliverable.class);
					((Deliverable) result[i]).setParentPrimaryObject(work);
				}
			} else if (value instanceof Object[]) {
				result = new Object[((Object[]) value).length];
				for (int i = 0; i < ((Object[]) value).length; i++) {
					item = ((Object[]) value)[i];
					result[i] = ModelService.createModelObject((DBObject) item,
							Deliverable.class);
					((Deliverable) result[i]).setParentPrimaryObject(work);
				}
			}

			// }

		}
		return result;
	}

	@Override
	public Object getParent(Object element) {

		if (element instanceof PrimaryObject) {
			return ((PrimaryObject) element).getParentPrimaryObjectCache();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof Work;
	}

}
