package com.sg.business.commons.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class EditWorkDefinition extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("您需要选择一项", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, final IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		AbstractWork workdefinition = (AbstractWork) selected;
		Shell shell = part.getSite().getShell();

		Assert.isNotNull(currentViewerControl);

		workdefinition.addEventListener(currentViewerControl);

		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				selected.getDefaultEditorId());

		if (conf != null) {
			try {
				DataObjectDialog.openDialog(workdefinition,
						(DataEditorConfigurator) conf, true, null, "编辑"
								+ selected.getTypeName());

				// 4. 将更改消息传递到编辑器
				sendNavigatorActionEvent(part, INavigatorActionListener.CUSTOMER,
						new Integer(INavigatorActionListener.REFRESH));

			} catch (Exception e) {
				e.printStackTrace();
				MessageUtil.showToast(shell, "编辑" + selected.getTypeName(),
						e.getMessage(), SWT.ICON_ERROR);
			}
		}
		
		workdefinition.removeEventListener(currentViewerControl);

	}

}
