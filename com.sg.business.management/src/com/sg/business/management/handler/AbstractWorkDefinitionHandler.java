package com.sg.business.management.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.editor.page.NavigatorPage;
import com.sg.widgets.viewer.ViewerControl;

public abstract class AbstractWorkDefinitionHandler extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		if(selection == null || selection.isEmpty()){
			boolean b = nullSelectionContinue(event);
			if(!b){
				return null;
			}
		}
		
		WorkDefinition selected = (WorkDefinition) selection.getFirstElement();
		execute(selected,event);
		return null;
	}
	
	protected abstract void execute(WorkDefinition selected,ExecutionEvent event) ;

	/**
	 * û��ѡ��ʱ�Ƿ����������������Ը�����ѡ���Ƿ����������������ʾ����
	 * @param event
	 * @return
	 */
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return false;
	}

	protected ViewerControl getCurrentViewerControl(ExecutionEvent event){
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if(part instanceof DataObjectEditor){
			DataObjectEditor dataObjectEditor = (DataObjectEditor) part;
			Object page = dataObjectEditor.getSelectedPage();
			if(page instanceof NavigatorPage){
				NavigatorControl navi = ((NavigatorPage) page).getNavigator();
				return navi.getViewerControl();
			}
		}
		return null;
	}

}
