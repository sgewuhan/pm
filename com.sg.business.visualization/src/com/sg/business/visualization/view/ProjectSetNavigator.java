package com.sg.business.visualization.view;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.ProjectTypeProvider;
import com.sg.business.model.User;
import com.sg.business.model.UserProjectPerf;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.view.TreeNavigator;
import com.sg.widgets.viewer.CTreeViewer;

public class ProjectSetNavigator extends TreeNavigator {

	public ProjectSetNavigator() {
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		final CTreeViewer viewer = (CTreeViewer) getNavigator().getViewer();
		Tree tree = (Tree) viewer.getControl();
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {

					String parameter = event.text.substring(event.text
							.lastIndexOf("/") + 1);

					String[] paras = parameter.split("@");
					try {
						if ("Organization".equals(paras[0])) {
							Organization org = ModelService.createModelObject(
									Organization.class, new ObjectId(paras[1]));
							viewer.setSelection(new StructuredSelection(org));
							IStructuredSelection is = (IStructuredSelection) viewer
									.getSelection();
							if(!is.isEmpty()){
								open((Organization) is.getFirstElement());
							}
						} else if ("User".equals(paras[0])) {
							User user = ModelService.createModelObject(
									User.class, new ObjectId(paras[1]));
							viewer.setSelection(new StructuredSelection(user));
							open(user);
						} else if ("ProductTypeProvider".equals(paras[0])) {
							ProjectProvider pp = new ProjectTypeProvider(
									paras[1], paras[2]);
							open(pp);
						} else if ("UserProjectPerf".equals(paras[0])) {
							UserProjectPerf pperf = ModelService
									.createModelObject(UserProjectPerf.class,
											new ObjectId(paras[1]));
							open(pperf);

						}
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				}
			}
		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection isel = (IStructuredSelection) event
						.getSelection();
				Object element = isel.getFirstElement();
				try {
					if (element instanceof Organization) {
						open((Organization) element);
					} else if (element instanceof User) {
						open((User) element);
					} else if (element instanceof UserProjectPerf) {
						open((UserProjectPerf) element);
					} else if (element instanceof ProjectProvider) {
						open((ProjectProvider) element);
					}
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		});
	}

	private void open(Organization org) throws Exception {
		open(org.getAdapter(ProjectProvider.class));
	}

	private void open(User user) throws Exception {
		open(user.getAdapter(ProjectProvider.class));
	}

	private void open(ProjectProvider pp) throws Exception {
		DataObjectEditor
				.open(pp, "editor.visualization.projectset", true, null);
	}

}
