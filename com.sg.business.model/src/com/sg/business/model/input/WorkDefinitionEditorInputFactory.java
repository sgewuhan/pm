package com.sg.business.model.input;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class WorkDefinitionEditorInputFactory implements IEditorInputFactory {

	private WorkDefinition workd;

	public WorkDefinitionEditorInputFactory(WorkDefinition workd) {
		this.workd = workd;
	}

	@Override
	public PrimaryObjectEditorInput getInput(Object data) {
		if ("navigate".equals(data)) {
			CurrentAccountContext context = new CurrentAccountContext();
			Work work = ModelService.createModelObject(Work.class);
			work.setValue(Work.F_CHARGER, context.getConsignerId());// 设置负责人为当前用户
			try {
				workd.makeStandloneWork(work, context);
				work.setValue(Work.F_DESC, workd.getDesc());
			} catch (Exception e) {
				return null;
			}

			DataEditorConfigurator conf = getEditorConfig(work);
			PrimaryObjectEditorInput editorInput = new PrimaryObjectEditorInput(
					work, conf, null);
			editorInput.setEditable(true);
			editorInput.setNeedHostPartListenSaveEvent(false);
			editorInput.setContext(context);
			return editorInput;
		}
		return null;
	}

	@Override
	public DataEditorConfigurator getEditorConfig(Object data) {
		if (data instanceof Work) {
			Work work = (Work) data;
			String editorId;
			if (work.isExecuteWorkflowActivateAndAvailable()) {
				editorId = "navigator.work.launch";
			} else {
				editorId = "navigator.work.launch.1";
			}
			DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
					.getEditorRegistry().getConfigurator(editorId);
			return conf;
		}
		return null;
	}

}
