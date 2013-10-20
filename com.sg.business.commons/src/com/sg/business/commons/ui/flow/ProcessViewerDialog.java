package com.sg.business.commons.ui.flow;

import org.bson.types.BasicBSONList;
import org.drools.definition.process.Node;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.ui.flow.part.SimpleNodeLabel;
import com.sg.business.model.IProcessControl;

public class ProcessViewerDialog extends Dialog {

	private DroolsProcessDefinition procDefinition;
	private BasicBSONList procHistory;
	private String title;

	public ProcessViewerDialog(Shell parentShell, IProcessControl ipc,
			String processKey,String title) {
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
		SashForm sashForm = new SashForm(content, SWT.HORIZONTAL);
		
		ProcessViewer pv = new ProcessViewer();
		
		// 创建流程图画板
		ProcessCanvas pc = new ProcessCanvas(sashForm);
		pc.setInput(procDefinition, procHistory);

		// 创建历史记录
		final ProcessHistoryTable pt = new ProcessHistoryTable(sashForm);
		pt.setInput(procDefinition, procHistory);

		pc.addNodeSelectListener(new INodeSelectListener() {

			@Override
			public void select(Object source) {
				if (source instanceof Label) {
					SimpleNodeLabel label = (SimpleNodeLabel) source;
					Node node = label.getNode();
					final String name = node.getName();
					pt.setFilters(new ViewerFilter[] { new ViewerFilter() {

						@Override
						public boolean select(Viewer viewer,
								Object parentElement, Object element) {
							if (element instanceof DBObject) {
								DBObject dbObject = (DBObject) element;
								Object taskname = dbObject
										.get(IProcessControl.F_WF_TASK_NAME);
								return name.equals(taskname);
							}
							return false;
						}
					} });
				}
			}
		});

		sashForm.setWeights(new int[] { 1, 2 });
		return content;
	}

}
