package com.sg.business.work.home;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class WorkDetail extends PrimaryObjectDetailFormView {

	@Override
	public void initContent() {
		content.setLayout(new GridLayout());
		Label label = new Label(content,SWT.NONE);
		String text = "<span style='font-size:18pt;font-family:微软雅黑;color:#A6A6A6'>" //$NON-NLS-1$
					+ "请在左边导航栏中选择待处理的工作" + "</span>";
		label.setText(text);
		label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,false,false));
		content.layout();
		setPartName("工作");
	}
	
	@Override
	protected PrimaryObjectEditorInput getInput(PrimaryObject primary) {
		if (!(primary instanceof Work)) {
			return null;
		}
		Work work = (Work) primary;
		String editorId = "navigator.view.work2";
		if(Work.STATUS_ONREADY_VALUE.equals(work.getLifecycleStatus())){
			editorId =  "navigator.view.work.1";
		}else if(Work.STATUS_WIP_VALUE.equals(work.getLifecycleStatus())){
			if(work.isExecuteWorkflowActivateAndAvailable()){
				editorId = "navigator.view.work.2";
			}else{
				editorId = "navigator.view.work.3";
			}
		}
		
		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(
				primary, conf, null);
		editorInput.setEditable(false);
		return editorInput;
	}
	
	@Override
	protected boolean responseSelectionChanged(IWorkbenchPart part,
			ISelection selection) {
		return part.getSite().getId().equals("homenavigator");
	}
	

}
