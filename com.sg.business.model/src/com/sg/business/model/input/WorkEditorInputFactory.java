package com.sg.business.model.input;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.commons.model.EditorInputFactory;

public class WorkEditorInputFactory extends EditorInputFactory {

	public WorkEditorInputFactory(PrimaryObject primaryObject) {
		super(primaryObject);
	}
	
	@Override
	protected String getEditorId(PrimaryObject primaryObject, Object data) {
		Work work = (Work) primaryObject;
		if(work.isProjectWBSRoot()){
			return "navigator.view.project";
		}
		if (work.isSummaryWork()) {
			if (Work.STATUS_ONREADY_VALUE.equals(work.getLifecycleStatus())) {
				return  "navigator.view.work.4";
			} else if (Work.STATUS_WIP_VALUE.equals(work.getLifecycleStatus())) {
				if (work.isExecuteWorkflowActivateAndAvailable()) {
					return  "navigator.view.work.6";
				} else {
					return  "navigator.view.work.5";
				}
//				return  "navigator.view.work.5";
			} else {
				return  "navigator.view.work.2";
			}
		} else {
			if (Work.STATUS_ONREADY_VALUE.equals(work.getLifecycleStatus())) {
				return  "navigator.view.work.1";
			} else {
				if (work.isExecuteWorkflowActivateAndAvailable()) {
					return  "navigator.view.work.2";
				} else {
					return  "navigator.view.work.3";
				}
			}
		}
	}
	
}


//package com.sg.business.model.input;
//
//import com.mobnut.commons.util.Utils;
//import com.sg.business.model.Work;
//import com.sg.widgets.Widgets;
//import com.sg.widgets.commons.model.IEditorInputFactory;
//import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
//import com.sg.widgets.registry.config.Configurator;
//import com.sg.widgets.registry.config.DataEditorConfigurator;
//
//public class WorkEditorInputFactory implements IEditorInputFactory {
//
//	private Work work;
//
//	public WorkEditorInputFactory(Work work) {
//		this.work = work;
//	}
//
//	@Override
//	public PrimaryObjectEditorInput getInput(Object data) {
//		DataEditorConfigurator conf = getEditorConfig(data);
//		PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(
//				work, conf, null);
//		editorInput.setEditable(false);
//		return editorInput;
//	}
//
//	@Override
//	public DataEditorConfigurator getEditorConfig(Object data) {
//		
//		String editorId = Widgets.getEditorIdUsedBy(work,data);
//		if(!Utils.isNullOrEmpty(editorId)){
//			Configurator conf = Widgets.getEditorRegistry().getConfigurator(editorId);
//			if(conf!=null){
//				return (DataEditorConfigurator) conf;
//			}
//		}
//		
//		if (work.isSummaryWork()) {
//			if (Work.STATUS_ONREADY_VALUE.equals(work.getLifecycleStatus())) {
//				editorId = "navigator.view.work.4";
//			} else if (Work.STATUS_WIP_VALUE.equals(work.getLifecycleStatus())) {
//				editorId = "navigator.view.work.5";
//			} else {
//				editorId = "navigator.view.work.2";
//			}
//		} else {
//			if (Work.STATUS_ONREADY_VALUE.equals(work.getLifecycleStatus())) {
//				editorId = "navigator.view.work.1";
//			} else {
//				if (work.isExecuteWorkflowActivateAndAvailable()) {
//					editorId = "navigator.view.work.2";
//				} else {
//					editorId = "navigator.view.work.3";
//				}
//			}
//		}
//
//		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
//				.getEditorRegistry().getConfigurator(editorId);
//		return conf;
//	}
//}
