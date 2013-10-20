package com.sg.business.commons.ui.flow;

import org.bson.types.BasicBSONList;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.flow.model.DroolsProcessDiagram;
import com.sg.business.commons.flow.parts.ActivityPartFactory;
import com.sg.business.model.IProcessControl;

public class CopyOfProcessViewerDialog extends Dialog {

	private DroolsProcessDefinition procDefinition;
	private BasicBSONList procHistory;
	private String title;

	public CopyOfProcessViewerDialog(Shell parentShell, IProcessControl ipc,
			String processKey, String title) {
		super(parentShell);
		procDefinition = ipc.getProcessDefinition(processKey);
		procHistory = ipc.getWorkflowHistroyData(processKey, true);
		this.title = title;
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(title);
		super.configureShell(newShell);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.get().OK_LABEL, true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite content = (Composite) super.createDialogArea(parent);
		
		ScrollingGraphicalViewer pv = new ScrollingGraphicalViewer();
		pv.setRootEditPart(new ScalableRootEditPart());
		pv.setEditPartFactory(new ActivityPartFactory());
		DroolsProcessDiagram diagram = new DroolsProcessDiagram(procDefinition,procHistory);
		pv.setContents(diagram);
		
		pv.createControl(content);
		return content;
	}

}
