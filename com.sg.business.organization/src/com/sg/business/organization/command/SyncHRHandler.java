package com.sg.business.organization.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.model.AbstractWork;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IEditablePart;
import com.sg.widgets.part.IRefreshablePart;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class SyncHRHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		long start = System.currentTimeMillis();
//
//		// 与HR的组织进行同步
//		SyscHR syscHR = new SyscHR();
//		syscHR.doSyscHROrganization();
//
//		IWorkbenchPart part = HandlerUtil.getActivePart(event);
//		if (part instanceof IRefreshablePart) {
//			((IRefreshablePart) part).doRefresh();
//		}
//		System.out.println(System.currentTimeMillis() - start);
		
//		Shell shell = HandlerUtil.getActiveShell(event);
//
//		AbstractWork po = ((AbstractWork) selected).makeChildWork();
//		ViewerControl vc = getCurrentViewerControl(event);
//		Assert.isNotNull(vc);
//
//		// 以下两句很重要，使树currentViewerControl够侦听到保存事件， 更新树上的节点
//		// 1. 设置父并不涉及到数据库，主要是维护模型的父子关系，同时ViewerControl也需要父来处理事件响应
//		po.setParentPrimaryObject(selected);
//		// 2. 侦听po的事件作出合适的响应
//		po.addEventListener(vc);
//
//		// 使用编辑器打开编辑工作定义
//		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
//				po.getDefaultEditorId());
//		try {
//			DataObjectDialog.openDialog(po, (DataEditorConfigurator) conf,
//					true, null,"创建"+po.getTypeName());
//		} catch (Exception e) {
//			MessageUtil.showToast(shell, "创建"+po.getTypeName(), e.getMessage(),
//					SWT.ICON_ERROR);
//		}
//
//		// 3. 处理完成后，释放侦听器
//		po.removeEventListener(vc);
		
		return null;
	}

}
