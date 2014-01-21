package com.sg.business.project.action;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.NavigatorAction;

public class ViewDocument extends NavigatorAction {

	public ViewDocument() {
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_CHECK_24));
		setText(Messages.get().ViewDocument_0);
	}

	@Override
	public void execute() throws Exception {
		IStructuredSelection selection = getNavigator().getViewerControl().getSelection();
		if(selection==null||selection.isEmpty()){
			MessageUtil.showToast(Messages.get().ViewDocument_1, SWT.ICON_INFORMATION);
			return;
		}
		
		getNavigator().getViewerControl().doEditSelection(new CurrentAccountContext());
		
	}

}
