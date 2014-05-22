package com.sg.business.management.field.action;

import java.util.Iterator;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mongodb.BasicDBList;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.selector.DropdownNavigatorSelector;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.part.editor.fields.value.IAddTableItemHandler;

public class AddWorkTimeProgramToProgramTemplateAction implements IAddTableItemHandler{

	public AddWorkTimeProgramToProgramTemplateAction() {
		
	}

	@Override
	public boolean addItem(final BasicDBList inputData, final AbstractFieldPart part) {
		ProjectTemplate projectTemplate = (ProjectTemplate) part.getInput().getData();
		Organization master = projectTemplate.getOrganization();
		
		
		DropdownNavigatorSelector ns=new DropdownNavigatorSelector("management.selectworktimeprogram"){
			
			@Override
			protected void doOK(IStructuredSelection is) {
				if(is==null || is.isEmpty()){
				}else{
					Iterator<?> iterator = is.iterator();
					while(iterator.hasNext()){
						Object value = iterator.next();
						if(value instanceof WorkTimeProgram){
							ObjectId programId = ((WorkTimeProgram) value).get_id();
							if(!inputData.contains(programId)){
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
