package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkDeliverable extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final Work work = (Work) selected;
		final ViewerControl vc = getCurrentViewerControl(event);

		NavigatorSelector ns = new NavigatorSelector(
				"project.deliverable.selector") {

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					Object o = is.getFirstElement();
					return o instanceof Deliverable;
				}
				return false;
			}

			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Object o = is.getFirstElement();
						if(o instanceof Deliverable){
							Deliverable deliverable = (Deliverable) o;
							Document document = deliverable.getDocument();
							if(document!=null){
								work.doAddDeliverable(document,new CurrentAccountContext());
								vc.getViewer().refresh(work, true);
							}else{
								MessageUtil.showToast("ѡ��Ľ����ﲻ�����ĵ�", SWT.ICON_WARNING);
							}
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				} else {
					MessageUtil.showToast("��ѡ���ĵ�", SWT.ICON_WARNING);
				}
			}
		};

		Project project = work.getProject();
		ns.setMaster(project);
		ns.show();
	}

}
