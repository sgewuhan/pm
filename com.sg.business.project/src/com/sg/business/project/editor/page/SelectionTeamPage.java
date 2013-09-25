package com.sg.business.project.editor.page;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.widgets.commons.labelprovider.PrimaryObjectLabelProvider;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class SelectionTeamPage extends AbstractFormPageDelegator {

	private Project project;
//	private boolean editable;
	private TreeViewer OrgTree;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		project = (Project) input.getData();
		OrgTree = createTree(parent,SWT.NONE);
		OrgTree.setInput(project.getProjectRole());
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
				if(element instanceof ProjectRole){
					ProjectRole projectRole = (ProjectRole)element;
					return !projectRole.getAssignment().isEmpty();
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				if(element instanceof PrimaryObject){
					PrimaryObject primaryObject = (PrimaryObject) element;
					return primaryObject.getParentPrimaryObject();
				}
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof List) {
					@SuppressWarnings("unchecked")
					List<PrimaryObject> element = (List<PrimaryObject>) inputElement;
					return element.toArray(new PrimaryObject[] {});
				}
				return null;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if(parentElement instanceof ProjectRole){
					ProjectRole projectRole = (ProjectRole)parentElement;
					List<PrimaryObject> children = projectRole.getAssignment();
					return children.toArray();
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
