package com.sg.business.visualization.view.handler;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import com.mobnut.db.model.DataSet;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.dataset.organization.OrgOfOwnerManager;
import com.sg.business.visualization.editor.DurationSetting;
import com.sg.widgets.MessageUtil;

public class SetFilter extends AbstractHandler implements IElementUpdater{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveShell(event);
		
		Object value = RWT.getApplicationContext().getAttribute(
				"projectProvider");
		ProjectProvider projectProvider;
		if (value instanceof ProjectProvider) {
			projectProvider = (ProjectProvider) value;
		} else {
			final OrgOfOwnerManager oom = new OrgOfOwnerManager();
			DataSet ds = oom.getDataSet();
			if (!ds.isEmpty()) {
				Organization org = (Organization) ds.getDataItems().get(0);
				projectProvider = org.getAdapter(ProjectProvider.class);
				RWT.getApplicationContext().setAttribute("projectProvider",
						projectProvider);
				projectProvider.getData();
			}else{
				projectProvider = null;
				MessageUtil.showToast("您没有获取组织的绩效数据权限", SWT.ICON_WARNING);
				return null;
			}
		}
		
		DurationSetting shell = new DurationSetting(parent,projectProvider){
			@Override
			protected void setFilter(int yearIndex, int quarterIndex,
					int monthIndex, boolean clearFilter) {
				super.setFilter(yearIndex, quarterIndex, monthIndex, clearFilter);
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ICommandService commandService = (ICommandService) window.getService(ICommandService.class);
				if (commandService != null) {
					commandService.refreshElements("de.rowlo.rcp.cce.app.command.Bar", null);
				}
			}
		};
		shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Display display = shell.getDisplay();
		Point location = new Point(display.getBounds().width/2-shell.getSize().x/2,display.getBounds().height/2-shell.getSize().y/2);
		shell.open(location);
		return null;
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {
		System.out.println();
	}
	
	

}
