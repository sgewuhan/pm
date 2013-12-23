package com.sg.business.performence.orgproject;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;

public class OrgProjectResContentProvider implements ITreeContentProvider {

	private DBCollection col;

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<?>) inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Organization) {
			List<PrimaryObject> result = ((Organization) parentElement)
					.getChildrenOrganization();
			
			List<PrimaryObject> member = ((Organization) parentElement)
					.getUser();
			String[] participate = new String[member.size()];
			for (int i = 0; i < participate.length; i++) {
				participate[i] = ((User)member.get(i)).getUserid();
			}
			//获得成员参与的项目
			if (member.size() > 0) {
				DBCursor cursor = col.find(new BasicDBObject().append(
						Project.F_PARTICIPATE,
						new BasicDBObject().append("$in", participate))); //$NON-NLS-1$
				while(cursor.hasNext()){
					DBObject projectdata = cursor.next();
					Project project = ModelService.createModelObject(projectdata, Project.class);
					project.setValue("$participate", participate); //$NON-NLS-1$
					result.add(project);
				}
			}

			return result.toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof Organization;
	}

	public OrgProjectResContentProvider() {
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

}
