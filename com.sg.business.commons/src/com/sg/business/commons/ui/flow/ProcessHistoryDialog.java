package com.sg.business.commons.ui.flow;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.Widgets;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.TableConfigurator;
import com.sg.widgets.viewer.CTableViewer;
import com.sg.widgets.viewer.ViewerControl;

public class ProcessHistoryDialog implements ISelectionChangedListener {

	//private DroolsProcessDefinition procDefinition;
	//private BasicBSONList procHistory;
	private PrimaryObject po;
	private String title;
	private Shell parentShell;
	private Shell shell;

	public ProcessHistoryDialog(Shell parentShell, PrimaryObject po,
			String processKey, String title) {
		//procDefinition = ipc.getProcessDefinition(processKey);
		//procHistory = ipc.getWorkflowHistroyData(processKey, true);
		this.po = po;
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

	private void createContent(Shell parent) {
		Configurator configurator = Widgets.getTableRegistry().getConfigurator("workflow.taskhistory"); //$NON-NLS-1$
		CTableViewer tv = new CTableViewer(parent, (TableConfigurator) configurator);
		ProcessHistoryUIToolkit.handleProcessHistoryTable(tv.getTable());
		ViewerControl vc = tv.getViewerControl();
		vc.masterChanged(po, null, null);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {

	}

}
