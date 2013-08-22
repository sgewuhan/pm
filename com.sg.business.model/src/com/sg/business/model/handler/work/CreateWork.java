package com.sg.business.model.handler.work;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateWork extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		MessageUtil.showToast("您需要选择一个上级", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);

		AbstractWork po = ((AbstractWork) selected).makeChildWork();
		ViewerControl currentViewerControl = getCurrentViewerControl(event);
		Assert.isNotNull(currentViewerControl);

		// 以下两句很重要，使树currentViewerControl够侦听到保存事件， 更新树上的节点
		// 1. 设置父并不涉及到数据库，主要是维护模型的父子关系，同时ViewerControl也需要父来处理事件响应
		po.setParentPrimaryObject(selected);
		// 2. 侦听po的事件作出合适的响应
		po.addEventListener(currentViewerControl);

		// 使用编辑器打开编辑工作定义
		Configurator conf;
		if (po instanceof WorkDefinition) {
			conf = Widgets.getEditorRegistry().getConfigurator(
					WorkDefinition.EDITOR_PROJECT_WORK);
		} else {
			conf = Widgets.getEditorRegistry().getConfigurator(
					Work.EDITOR_PROJECT_WORK);
		}
		try {
			DataObjectDialog.openDialog(po, (DataEditorConfigurator) conf,
					true, null, getTitle(po));
		} catch (Exception e) {
			MessageUtil.showToast(shell, getTitle(po), e.getMessage(),
					SWT.ICON_ERROR);
		}

		// 3. 处理完成后，释放侦听器
		po.removeEventListener(currentViewerControl);
	}

	private String getTitle(AbstractWork po) {
		return (po instanceof WorkDefinition) ? "添加工作定义" : "添加工作";
	}

}
