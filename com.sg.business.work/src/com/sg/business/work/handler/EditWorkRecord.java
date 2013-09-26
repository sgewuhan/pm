package com.sg.business.work.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class EditWorkRecord extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Work) {
			Work work = (Work) selected;
			
			Configurator conf = Widgets.getEditorRegistry().getConfigurator(
					"editor.create.workrecord");
			
			try {
				
				
				 DataObjectDialog.openDialog(null, (DataEditorConfigurator) conf,
						true, null, "¹¤×÷¼ÇÂ¼");
				
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
		
	}
	
	

}
