package com.sg.business.visualization.view;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectProvider;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.view.TreeNavigator;

public class OrganizationNavigator extends TreeNavigator {

	public OrganizationNavigator() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		final StructuredViewer viewer = getNavigator().getViewer();
		Tree tree = (Tree) viewer.getControl();
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try{
						String _id = event.text.substring(event.text.lastIndexOf("/")+1);
						Organization org = ModelService.createModelObject(Organization.class,new ObjectId(_id));
						open(org);
						viewer.setSelection(new StructuredSelection(org));
					}catch(Exception e){
						MessageUtil.showToast(e);
					}
				}
			}
		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection isel = (IStructuredSelection) event.getSelection();
				Object element = isel.getFirstElement();
				if(element instanceof Organization){
					try {
						open((Organization) element);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}
				}
			}
		});
	}

	private void open(Organization org) throws Exception {
		ProjectProvider pp = org.getAdapter(ProjectProvider.class);
		DataObjectEditor.open(pp, "editor.visualization.projectset", true, null);
	}

}
