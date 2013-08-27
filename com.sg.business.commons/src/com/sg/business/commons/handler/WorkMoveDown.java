package com.sg.business.commons.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class WorkMoveDown extends AbstractNavigatorHandler {

	private static final String TITLE = "���ƹ�������";
	
	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "����Ҫѡ��һ����������", SWT.ICON_WARNING);
		return false;
	}
	
	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		try {
			PrimaryObject[] relativeObjects = ((AbstractWork)selected).doMoveDown(new CurrentAccountContext());
			
			ViewerControl vc = getCurrentViewerControl(event);
			ColumnViewer viewer = vc.getViewer();
			for(int i=0;i<relativeObjects.length;i++){
				viewer.refresh(relativeObjects[i]);
			}
			viewer.setSelection(new StructuredSelection(selected), true);
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(), SWT.ICON_WARNING);
		}
		
	}


}
