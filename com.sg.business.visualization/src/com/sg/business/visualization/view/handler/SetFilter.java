package com.sg.business.visualization.view.handler;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import com.sg.business.model.ProjectProvider;
import com.sg.business.resource.nls.Messages;
import com.sg.business.visualization.ui.DurationSetting;
import com.sg.business.visualization.ui.ProjectProviderHolder;
import com.sg.widgets.MessageUtil;

public class SetFilter extends AbstractHandler implements IElementUpdater {

	private ProjectProviderHolder holder;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveShell(event);
		holder = ProjectProviderHolder.getInstance();
		ProjectProvider projectProvider = holder.getProjectProvider();
		if (projectProvider == null) {
			MessageUtil.showToast(Messages.get().SetFilter_0, SWT.ICON_WARNING);
			return null;
		}

		DurationSetting shell = new DurationSetting(parent, projectProvider) {
			@Override
			protected void setFilter(int yearIndex, int quarterIndex,
					int monthIndex, boolean clearFilter) {
				super.setFilter(yearIndex, quarterIndex, monthIndex,
						clearFilter);
				refreshCommand();
			}
		};

		shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point location = new Point(0, 100);
		shell.open(location);
		return null;
	}

	private void refreshCommand() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		ICommandService commandService = (ICommandService) window
				.getService(ICommandService.class);
		if (commandService != null) {
			commandService.refreshElements("visualization.command.setfilter", //$NON-NLS-1$
					null);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updateElement(UIElement element, Map parameters) {
		if (holder == null) {
			element.setText(Messages.get().SetFilter_2);
		} else {
			ProjectProvider projectProvider = holder.getProjectProvider();
			String text = DurationSetting.getHeadParameterText(projectProvider);
			if (text.isEmpty()) {
				element.setText(Messages.get().SetFilter_3);
			} else {
				element.setText(Messages.get().SetFilter_4 + text+"]"); //$NON-NLS-2$ //$NON-NLS-1$
			}
		}
	}

}
