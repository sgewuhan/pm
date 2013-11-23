package com.sg.business.visualization.view;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.UserProjectPerf;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.view.TableNavigator;

public class MyProjectNavigator extends TableNavigator
{


	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		StructuredViewer viewer = getNavigator().getViewer();
		Table table = (Table) viewer.getControl();
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try{
						String _id = event.text.substring(event.text.lastIndexOf("/")+1);
						UserProjectPerf pperf = ModelService.createModelObject(UserProjectPerf.class,new ObjectId(_id));
						open(pperf);
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
				if(element instanceof UserProjectPerf){
					try {
						open((UserProjectPerf) element);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}
				}
			}
		});
	}

	private void open(UserProjectPerf pperf) throws Exception {
		DataObjectEditor.open(pperf, "editor.visualization.projectset", true, null);
	}

}
