package com.sg.business.visualization.view.handler;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectProvider;
import com.sg.business.resource.nls.Messages;
import com.sg.business.visualization.ui.ProjectProviderHolder;
import com.sg.widgets.commons.selector.DropdownNavigatorSelector;

public class SetDepartmentFilter extends AbstractHandler implements
		IElementUpdater {

	private ProjectProviderHolder holder;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveShell(event);
		holder = ProjectProviderHolder.getInstance();
		DropdownNavigatorSelector nv = new DropdownNavigatorSelector(
				"vis.projectset.navigator.org"){ //$NON-NLS-1$
			@Override
			protected void doOK(IStructuredSelection is) {
				if(is!=null&&!is.isEmpty()){
					PrimaryObject sel = (PrimaryObject) is.getFirstElement();
					ProjectProvider pp;
					if(sel instanceof ProjectProvider){
						pp = (ProjectProvider) sel;
					}else{
						pp = sel.getAdapter(ProjectProvider.class);
					}
					if(pp!=null){
						holder.setCurrentProjectProvider(pp);
						refreshCommand();
					}
				}
				super.doOK(is);
			}
		};
		Rectangle bounds = new Rectangle(200, 100, 300, 400);
		nv.show(parent, bounds);

		return null;
	}

	private void refreshCommand() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		ICommandService commandService = (ICommandService) window
				.getService(ICommandService.class);
		if (commandService != null) {
			commandService.refreshElements("visualization.command.setdepartment", //$NON-NLS-1$
					null);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updateElement(UIElement element, Map parameters) {
		if(holder!=null){
			ProjectProvider pp = holder.getProjectProvider();
			if(pp !=null){
				element.setText(pp.getProjectSetName());
			}else{
				element.setText(Messages.get().SetDepartmentFilter_2);
			}
		}else{
			element.setText(Messages.get().SetDepartmentFilter_3);
		}
	}

}
