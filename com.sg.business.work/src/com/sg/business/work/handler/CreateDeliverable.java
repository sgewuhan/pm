package com.sg.business.work.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.IWorkRelative;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateDeliverable extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Shell shell = part.getSite().getShell();
		PrimaryObject master = currentViewerControl.getMaster();
		if (master == null) {
			MessageUtil.showToast(shell, "创建交付物", "请选择流程", SWT.ICON_ERROR);
			return;
		}

		AbstractWork work;
		if (master instanceof AbstractWork) {
			work = (AbstractWork) master;
		} else if (master instanceof IWorkRelative) {
			IWorkRelative iWorkRelative = (IWorkRelative) master;
			work = iWorkRelative.getWork();
		} else {
			MessageUtil.showToast(shell, "创建交付物", "请选择流程", SWT.ICON_ERROR);
			return;
		}

		PrimaryObject po = work.makeDeliverableDefinition();
		Assert.isNotNull(currentViewerControl);

		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				po.getDefaultEditorId());
		try {
			DataObjectDialog.openDialog(po, (DataEditorConfigurator) conf,
					true, null, "添加" + po.getTypeName());
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.showToast(shell, "添加" + po.getTypeName(),
					e.getMessage(), SWT.ICON_ERROR);
		}
		currentViewerControl.doReloadData();
	}

}
