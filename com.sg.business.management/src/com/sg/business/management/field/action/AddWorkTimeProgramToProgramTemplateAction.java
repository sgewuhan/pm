package com.sg.business.management.field.action;

import java.util.Iterator;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.selector.DropdownNavigatorSelector;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.part.editor.fields.value.IAddTableItemHandler;

public class AddWorkTimeProgramToProgramTemplateAction implements
		IAddTableItemHandler {

	public AddWorkTimeProgramToProgramTemplateAction() {

	}

	@Override
	public boolean addItem(final BasicDBList inputData,
			final AbstractFieldPart part) {
		PrimaryObject data = part.getInput().getData();
		Organization master;
		if (data instanceof ProjectTemplate) {
			master = ((ProjectTemplate) data).getOrganization();
			// 判断data是工作定义，且是独立工作定义
		} else if (data instanceof WorkDefinition
				&& ((WorkDefinition) data).isStandloneWork()) {
			master = ((WorkDefinition) data).getOrganization();
		} else if(data instanceof WorkDefinition
				&& ((WorkDefinition) data).isGenericWork()){
			master = ((WorkDefinition) data).getOrganization();
		}else{
			return false;
		}

		DropdownNavigatorSelector ns = new DropdownNavigatorSelector(
				"management.selectworktimeprogram") {

			@Override
			protected void doOK(IStructuredSelection is) {
				if (is == null || is.isEmpty()) {
				} else {
					Iterator<?> iterator = is.iterator();
					while (iterator.hasNext()) {
						Object value = iterator.next();
						if (value instanceof WorkTimeProgram) {
							ObjectId programId = ((WorkTimeProgram) value)
									.get_id();
							if (!inputData.contains(programId)) {
								inputData.add(programId);
							}
						}
					}
					part.setDirty(true);
					part.updateDataValueAndPresent();
					super.doOK(is);
				}
			}
		};
		ns.setMaster(master);
		ns.show(part.getShell(), part.getControl(), true);
		return true;
	}

}
