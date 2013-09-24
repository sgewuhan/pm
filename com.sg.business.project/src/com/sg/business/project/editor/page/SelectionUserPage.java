package com.sg.business.project.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.User;
import com.sg.widgets.commons.labelprovider.PrimaryObjectLabelProvider;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class SelectionUserPage extends AbstractFormPageDelegator {

	private Project project;
	private TreeViewer OrgTree;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		project = (Project) input.getData();
		OrgTree = createTree(parent, SWT.NONE);
		List<PrimaryObject> inputData = new ArrayList<PrimaryObject>();
		inputData.add(project.getFunctionOrganization());
		OrgTree.setInput(inputData);
		
		return parent;
	}

	private TreeViewer createTree(Composite panel, int style) {
		TreeViewer treeViewer = new TreeViewer(panel, style);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.setLabelProvider(new PrimaryObjectLabelProvider());
		treeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			@Override
			public void dispose() {

			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof Organization) {
					Organization org = (Organization) element;
					if (org.getChildrenOrganization().isEmpty()) {
						return !org.getUser().isEmpty();
					} else {
						return true;
					}
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				if (element instanceof Organization) {
					Organization org = (Organization) element;
					return org.getParentOrganization();
				} else if (element instanceof User) {
					User user = (User) element;
					return (PrimaryObject) user.getOrganization();
				}
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof List) {
					@SuppressWarnings("unchecked")
					List<PrimaryObject> elements = (List<PrimaryObject>) inputElement;
					return elements.toArray(new PrimaryObject[] {});
				}
				return null;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof Organization) {
					Organization org = (Organization) parentElement;
					if (org.getChildrenOrganization().isEmpty()) {
						return org.getUser().toArray(new PrimaryObject[] {});
					} else {
						List<PrimaryObject> children = new ArrayList<PrimaryObject>();
						children.addAll(org.getChildrenOrganization());
						children.addAll(org.getUser());
						return children.toArray(new PrimaryObject[] {});
					}
				}
				return null;
			}
		});

		return treeViewer;
	}

	@Override
	public void commit(boolean onSave) {
	}

	@Override
	public void setFocus() {

	}

}
