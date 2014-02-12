package com.sg.business.work.home;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Message;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class WorkDetail extends PrimaryObjectDetailFormView {

	private CurrentAccountContext context;

	@Override
	protected void initContent() {
		context = new CurrentAccountContext();
		initContent("请在左边导航栏中选择您要处理的工作");
	}

	public void initContent(String text) {
		content.setLayout(new GridLayout());
		Label label = new Label(content, SWT.NONE);
		text = "<span style='font-size:19pt;font-family:微软雅黑;color:#A6A6A6'>" //$NON-NLS-1$
				+ text + "</span>";
		label.setText(text);
		label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		content.layout();
	}

	@Override
	protected PrimaryObjectEditorInput getInput(PrimaryObject primary) {
		PrimaryObjectEditorInput editorInput = null;
		if (primary instanceof Work) {
			editorInput = getInputFromWork((Work) primary);
		}else if(primary instanceof WorkDefinition){
			editorInput = getInputFromWorkDefinition((WorkDefinition) primary);
		}else if(primary instanceof Message){
			editorInput = getInputFromMessage((Message)primary);
		}
		return editorInput;
	}

	private PrimaryObjectEditorInput getInputFromMessage(Message message) {
		try {
			message.doMarkRead(context, Boolean.TRUE);
		} catch (Exception e) {
			return null;
		}
		String editorId = "message.editor.view";
		
		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(message, conf, null);
		editorInput.setEditable(true);
		editorInput.setNeedHostPartListenSaveEvent(false);
		editorInput.setContext(context);
		return editorInput;
	}

	private PrimaryObjectEditorInput getInputFromWorkDefinition(WorkDefinition workd) {
		Work work = ModelService.createModelObject(Work.class);
		work.setValue(Work.F_CHARGER, context.getConsignerId());// 设置负责人为当前用户
		try {
			workd.makeStandloneWork(work, context);
		} catch (Exception e) {
			return null;
		}
		String editorId;
		if(work.isExecuteWorkflowActivateAndAvailable()){
			editorId = "navigator.work.launch";
		}else{
			editorId = "navigator.work.launch.1";
		}
		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(work, conf, null);
		editorInput.setEditable(true);
		editorInput.setNeedHostPartListenSaveEvent(false);
		editorInput.setContext(context);
		return editorInput;
	}

	private PrimaryObjectEditorInput getInputFromWork(Work work) {
		String editorId;
		if (work.isSummaryWork()) {
			if (Work.STATUS_ONREADY_VALUE.equals(work.getLifecycleStatus())) {
				editorId = "navigator.view.work.4";
			} else if (Work.STATUS_WIP_VALUE.equals(work
					.getLifecycleStatus())) {
				editorId = "navigator.view.work.5";
			} else {
				editorId = "navigator.view.work.2";
			}
		} else {
			if (Work.STATUS_ONREADY_VALUE.equals(work.getLifecycleStatus())) {
				editorId = "navigator.view.work.1";
			} else {
				if (work.isExecuteWorkflowActivateAndAvailable()) {
					editorId = "navigator.view.work.2";
				} else {
					editorId = "navigator.view.work.3";
				}
			}
		}

		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator(editorId);
		PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(
				work, conf, null);
		editorInput.setEditable(false);
		return editorInput;
	}

	@Override
	protected boolean responseSelectionChanged(IWorkbenchPart part,
			ISelection selection) {
		if (!part.getSite().getId().equals("homenavigator")) {
			return false;
		}
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
			return false;
		}
		Object element = ((IStructuredSelection) selection).getFirstElement();
		return element instanceof Work || element instanceof WorkDefinition||element instanceof Message;
	}

	public void setInputWork(Work work) {
		PrimaryObjectEditorInput newInput = getInput(work);
		setInput(newInput);
	}
	
	@Override
	protected void loadTitle(PrimaryObject data) {
		if(!data.isPersistent()){
			String desc = data.getText(Work.F_WORK_DEFINITION_NAME);
			setPartName("发起工作:"+desc);
		}else{
			super.loadTitle(data);
		}
	}

}
