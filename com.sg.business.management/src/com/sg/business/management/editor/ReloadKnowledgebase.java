package com.sg.business.management.editor;

import java.util.List;

import org.eclipse.ui.forms.IFormPart;

import com.sg.bpm.service.BPM;
import com.sg.business.model.Organization;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.value.IFieldActionHandler;

public class ReloadKnowledgebase implements IFieldActionHandler {

	public ReloadKnowledgebase() {
	}

	@Override
	public Object run(IFormPart abstractFieldPart, PrimaryObjectEditorInput input) {
		Object kbaselist = input.getData().getValue(Organization.F_KBASE);
		if(kbaselist instanceof List<?>){
			for (int i = 0; i < ((List<?>)kbaselist).size(); i++) {
				String kbname = (String) ((List<?>)kbaselist).get(i);
				BPM.getBPMService().loadKnowledgeBase(kbname);
			}
		}else if(kbaselist instanceof String[]){
			for (int i = 0; i < ((String[])kbaselist).length; i++) {
				String kbname = ((String[])kbaselist)[i];
				BPM.getBPMService().loadKnowledgeBase(kbname);
			}
		}
		return null;
	}

}
