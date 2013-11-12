package com.sg.business.taskforms.validation;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IValidationHandler;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Work;

public class DocumentMondartory implements IValidationHandler {

	private String message;

	public DocumentMondartory() {
	}

	@Override
	public boolean validate(PrimaryObject work) {
		if(work instanceof Work){
			Work work2 = (Work) work;
			List<PrimaryObject> delis = work2.getDeliverable();
			if (delis != null) {
				for (int i = 0; i < delis.size(); i++) {
					Deliverable deli = (Deliverable) delis.get(i);
					if (deli.isMandatory()) {
						Document doc = deli.getDocument();
						try {
							doc.checkMandatory();
						} catch (Exception e) {
							message = e.getMessage();
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
