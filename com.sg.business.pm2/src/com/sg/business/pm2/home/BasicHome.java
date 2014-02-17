package com.sg.business.pm2.home;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.IWorkbenchPart;

import com.sg.business.model.Message;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;

public class BasicHome extends PrimaryObjectDetailFormView {

	private GenericHomePanel genericHomePanel;

	@Override
	protected void initContent() {
		goHome();
	}

	@Override
	public void goHome() {
		cleanUI();
		content.setLayout(new GridLayout());
		genericHomePanel = new GenericHomePanel(content);
		// Label label = new Label(content, SWT.NONE);
		//		String text = "<span style='font-size:19pt;font-family:΢���ź�;color:#A6A6A6'>" //$NON-NLS-1$
		// + "������ߵ�������ѡ����Ҫ����Ĺ���" + "</span>";
		// label.setText(text);
		// label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		// label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
		// false));
		content.layout();
		super.goHome();
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

	@Override
	public void doRefresh() {
		if (isHome) {
			genericHomePanel.doRefresh();
		} else {
			loadMaster();
		}
	}
}
