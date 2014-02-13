package com.sg.business.work.home;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IEditorInputFactory;
import com.sg.business.model.Message;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;

public class WorkDetail extends PrimaryObjectDetailFormView {


	@Override
	protected void initContent() {
		goHome();
	}

	@Override
	public void goHome() {
		cleanUI();
		content.setLayout(new GridLayout());
		new GenericHomePanel(content);
		// Label label = new Label(content, SWT.NONE);
		//		String text = "<span style='font-size:19pt;font-family:微软雅黑;color:#A6A6A6'>" //$NON-NLS-1$
		// + "请在左边导航栏中选择您要处理的工作" + "</span>";
		// label.setText(text);
		// label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		// label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
		// false));
		content.layout();
	}

	@Override
	protected PrimaryObjectEditorInput getInput(PrimaryObject primary) {
		IEditorInputFactory f =  primary.getAdapter(IEditorInputFactory.class);
		return f.getInput("navigate");
	}


	@Override
	protected boolean responseSelectionChanged(IWorkbenchPart part,
			ISelection selection) {
		if (!part.getSite().getId().equals("homenavigator")) {
			return false;
		}
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
			return false;
		}
		Object element = ((IStructuredSelection) selection).getFirstElement();
		return element instanceof Work || element instanceof WorkDefinition
				|| element instanceof Message;
	}

}
