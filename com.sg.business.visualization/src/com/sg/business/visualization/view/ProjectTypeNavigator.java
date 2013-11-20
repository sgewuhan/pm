package com.sg.business.visualization.view;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.sg.business.model.ProjectProvider;
import com.sg.business.model.ProjectTypeProjectProvider;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.view.TableNavigator;

public class ProjectTypeNavigator extends TableNavigator {

	public ProjectTypeNavigator() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Table table = (Table) getNavigator().getViewer().getControl();
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try{
						String info = event.text.substring(event.text.lastIndexOf("/")+1);
						String[] split = info.split(",");
						ProjectProvider pp = new ProjectTypeProjectProvider(split[0],split[1]);
						DataObjectEditor.open(pp, "editor.visualization.projectset", true, null);
					}catch(Exception e){
						MessageUtil.showToast(e);
					}
				}
			}
		});
	}

}
