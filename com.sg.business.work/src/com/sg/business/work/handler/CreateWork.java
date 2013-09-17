package com.sg.business.work.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
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

		Work childWork = ((Work) selected).makeChildWork();
		ViewerControl vc = getCurrentViewerControl(event);
		Assert.isNotNull(vc);

		// 以下两句很重要，使树currentViewerControl够侦听到保存事件， 更新树上的节点
		// 1. 设置父并不涉及到数据库，主要是维护模型的父子关系，同时ViewerControl也需要父来处理事件响应
		childWork.setParentPrimaryObject(selected);
		// 2. 侦听po的事件作出合适的响应
		childWork.addEventListener(vc);

		// 使用编辑器打开编辑工作定义
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				Work.EDITOR_CREATE_RUNTIME_WORK);
		try {
			DataObjectDialog.openDialog(childWork,
					(DataEditorConfigurator) conf, true, null, "创建工作");

		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		// 3. 处理完成后，释放侦听器
		childWork.removeEventListener(vc);

	}

}
