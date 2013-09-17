package com.sg.business.management.handler;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.CTreeViewer;
import com.sg.widgets.viewer.ViewerControl;

public class CopyGenericWorkDefinition extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final WorkDefinition workd = (WorkDefinition) selected;
		final ViewerControl vc = getCurrentViewerControl(event);
		workd.addEventListener(vc);
		NavigatorSelector ns = new NavigatorSelector(
				"management.genericwork.definitions") {

			//沒有啟用的通用工作定義不能引用
			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				Iterator<?> iterator = is.iterator();
				while (iterator.hasNext()) {
					WorkDefinition nworkd = (WorkDefinition) iterator.next();
					if (Boolean.FALSE.equals(nworkd.isActivated())) {
						return false;
					}
				}
				return super.isSelectEnabled(is);
			}
			
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is != null && !is.isEmpty()) {
					try {
						Iterator<?> iter = is.iterator();
						while (iter.hasNext()) {
							Object next = iter.next();
							workd.doImportGenericWorkDefinition(
									(WorkDefinition) next,
									new CurrentAccountContext());
						}
						CTreeViewer viewer = (CTreeViewer) vc.getViewer();
						viewer.refresh(workd);
						viewer.expandToLevel(workd, CTreeViewer.ALL_LEVELS);
						super.doOK(is);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}

				} else {
					MessageUtil.showToast("请选择角色定义", SWT.ICON_WARNING);
				}
			}
		};
		ProjectTemplate projectTemplate = workd.getProjectTemplate();
		ns.setMaster(projectTemplate.getOrganization());
		ns.show();
	}

}
