package com.sg.business.commons.ui.flow;

import org.bson.types.BasicBSONList;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.flow.model.DroolsProcessDiagram;
import com.sg.business.commons.flow.model.NodeActivity;
import com.sg.business.commons.flow.part.ActivityPartFactory;
import com.sg.business.commons.flow.part.SimpleActivityPart;
import com.sg.business.model.IProcessControl;

public class ProcessViewerDialog implements ISelectionChangedListener {

	private DroolsProcessDefinition procDefinition;
	private BasicBSONList procHistory;
	private String title;
	private Shell parentShell;
	private Shell shell;

	public ProcessViewerDialog(Shell parentShell, IProcessControl ipc,
			String processKey, String title) {
		procDefinition = ipc.getProcessDefinition(processKey);
		procHistory = ipc.getWorkflowHistroyData(processKey, true);
		this.title = title;
		this.parentShell = parentShell;
	}

	public void open() {
		shell = new Shell(parentShell, SWT.MAX | SWT.BORDER | SWT.CLOSE
				| SWT.RESIZE | SWT.APPLICATION_MODAL);
		shell.setLayout(new FillLayout());
		shell.setText(title);
		createContent(shell);

		Display display = parentShell.getDisplay();
		Rectangle displayBounds = display.getBounds();

		Point size = shell.computeSize(displayBounds.width / 2,
				displayBounds.height * 2 / 3);

		shell.setLocation((displayBounds.width - size.x) / 2,
				(displayBounds.height - size.y) / 2);

		shell.open();
	}

	protected Control createContent(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);

		ScrollingGraphicalViewer pv = new ScrollingGraphicalViewer();
		pv.setRootEditPart(new ScalableRootEditPart());
		pv.setEditPartFactory(new ActivityPartFactory());
		DroolsProcessDiagram diagram = new DroolsProcessDiagram(procDefinition,
				procHistory);
		pv.setContents(diagram);
		pv.createControl(sashForm);

		final ProcessHistoryTable pt = new ProcessHistoryTable(sashForm);
		pt.setInput(procDefinition, procHistory);

		pv.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection is = (StructuredSelection) event
						.getSelection();
				if (is == null || is.isEmpty()) {
					pt.resetFilters();
					return;
				}
				Object element = is.getFirstElement();

				if (element instanceof SimpleActivityPart) {
					Object model = ((SimpleActivityPart) element).getModel();
					if (model instanceof NodeActivity
							&& !((NodeActivity) model).isStartNode()
							&& !((NodeActivity) model).isEndNode()) {
						final String name = ((NodeActivity) model).getName();
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
						
						return;
					}
				}
				pt.resetFilters();
			}
		});
		sashForm.setWeights(new int[] { 2, 1 });
		return pv.getControl();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {

	}

}
