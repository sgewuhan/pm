package com.sg.business.visualization.view;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.sg.business.model.ProjectProvider;
import com.sg.business.model.ProjectTypeProvider;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.view.TableNavigator;

public class ProductTypeNavigator extends TableNavigator {

	public ProductTypeNavigator() {
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		StructuredViewer viewer = getNavigator().getViewer();
		Table table = (Table) viewer.getControl();

		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try {
						String info = event.text.substring(event.text
								.lastIndexOf("/") + 1);
						String[] split = info.split(",");
						ProjectProvider pp = new ProjectTypeProvider(split[0],
								split[1]);
						open(pp);
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
				if (element instanceof ProjectProvider) {
					try {
						open((ProjectProvider) element);

					} catch (Exception e) {
						MessageUtil.showToast(e);
					}
				}
			}
		});
	}

	private void open(ProjectProvider pp) throws Exception {

		DataObjectEditor
				.open(pp, "editor.visualization.projectset", true, null);
	}

}
