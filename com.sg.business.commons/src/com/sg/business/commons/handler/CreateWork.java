package com.sg.business.commons.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.nls.Messages;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateWork extends AbstractNavigatorHandler {

	
	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast(Messages.get().CreateWork_0, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {

		Shell shell = part.getSite().getShell();

		Work po = ((Work) selected).makeChildWork();
		Assert.isNotNull(vc);

		// 以下两句很重要，使树currentViewerControl够侦听到保存事件， 更新树上的节点
		// 1. 设置父并不涉及到数据库，主要是维护模型的父子关系，同时ViewerControl也需要父来处理事件响应
		po.setParentPrimaryObject(selected);
		// 2. 侦听po的事件作出合适的响应
		po.addEventListener(vc);

		// 使用编辑器打开编辑工作定义
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				"edit.work.plan.0");//使用不带流程的工作编辑器进行创建 //$NON-NLS-1$

		try {
			DataObjectDialog.openDialog(po, (DataEditorConfigurator) conf,
					true, null, Messages.get().CreateWork_2 + po.getTypeName());
			
			//刷新上级数据
			List<PrimaryObject> tobeRefresh = new ArrayList<PrimaryObject>();
			tobeRefresh.add((Work) selected);
			AbstractWork parent = ((Work) selected).getParent();
			while(parent!=null){
				tobeRefresh.add(parent);
				parent = ((Work) parent).getParent();
			}
			vc.getViewer().update(tobeRefresh.toArray(), null);
			

			// 4. 将更改消息传递到编辑器
			sendNavigatorActionEvent( part, INavigatorActionListener.CUSTOMER,
					new Integer(INavigatorActionListener.REFRESH));

		} catch (Exception e) {
			MessageUtil.showToast(shell, Messages.get().CreateWork_3 + po.getTypeName(),
					e.getMessage(), SWT.ICON_ERROR);
		}

		// 3. 处理完成后，释放侦听器
		po.removeEventListener(vc);

	}

}
