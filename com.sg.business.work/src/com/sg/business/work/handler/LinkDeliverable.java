package com.sg.business.work.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.IDeliverable;
import com.sg.business.model.IWorkRelative;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class LinkDeliverable extends AbstractNavigatorHandler {

	
	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}
	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			final ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {

		final Shell shell = part.getSite().getShell();
		final PrimaryObject master = vc.getMaster();
		if (master == null) {
			MessageUtil.showToast(shell, "创建交付物", "请选择流程", SWT.ICON_ERROR);
			return;
		}
		
		Work work = getWork(master);
		if(work == null){
			MessageUtil.showToast(shell, "创建交付物", "请选择流程", SWT.ICON_ERROR);
			return;
		}

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
						if (o instanceof Deliverable) {
							Deliverable deliverable = (Deliverable) o;
							Document document = deliverable.getDocument();
							if (document != null) {
								Work work = getWork(master);
								if(work == null){
									MessageUtil.showToast(shell, "创建交付物",
											"请选择流程", SWT.ICON_ERROR);
									return;
								}

								work.doAddDeliverable(document,IDeliverable.TYPE_REFERENCE,
										new CurrentAccountContext());
								vc.doReloadData();
							} else {
								MessageUtil.showToast("选择的交付物不包含文档",
										SWT.ICON_WARNING);
							}
						}
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				} else {
					MessageUtil.showToast("请选择文档", SWT.ICON_WARNING);
				}
			}
		};

		
		Project project = work.getProject();
		ns.setMaster(project);
		ns.show();
	}

	protected Work getWork(PrimaryObject master) {
		if (master instanceof Work) {
			return (Work) master;
		} else if (master instanceof IWorkRelative) {
			IWorkRelative iWorkRelative = (IWorkRelative) master;
			return (Work) iWorkRelative.getWork();
		} else {
			return null;
		}
	}

}
