package com.sg.business.commons.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.IProcessControlable;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class EditWork extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		MessageUtil.showToast("您需要选择一项", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Work work = (Work) selected;
		Shell shell = HandlerUtil.getActiveShell(event);

		ViewerControl vc = getCurrentViewerControl(event);
		Assert.isNotNull(vc);

		work.addEventListener(vc);

		String editorId;
		IProcessControlable ipc = (IProcessControlable) work
				.getAdapter(IProcessControlable.class);
		if (work.isSummaryWork()) {
			editorId = "edit.work.plan.2";
		} else {
			if(ipc.isWorkflowActivateAndAvailable(Work.F_WF_EXECUTE)){
				editorId = "edit.work.plan.1";
			}else{
				editorId = "edit.work.plan.0";
			}
		}
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				editorId);

		if (conf != null) {
			try {
				DataObjectDialog.openDialog(work,
						(DataEditorConfigurator) conf, true, null, "编辑"
								+ selected.getTypeName());
				// 刷新上级数据
				List<PrimaryObject> tobeRefresh = new ArrayList<PrimaryObject>();
				tobeRefresh.add((Work) selected);
				AbstractWork parent = ((Work) selected).getParent();
				while (parent != null) {
					tobeRefresh.add(parent);
					parent = ((Work) parent).getParent();
				}
				vc.getViewer().update(tobeRefresh.toArray(), null);

				// 4. 将更改消息传递到编辑器
				sendNavigatorActionEvent(event,
						INavigatorActionListener.CUSTOMER, new Integer(
								INavigatorActionListener.REFRESH));

			} catch (Exception e) {
				e.printStackTrace();
				MessageUtil.showToast(shell, "编辑" + selected.getTypeName(),
						e.getMessage(), SWT.ICON_ERROR);
			}
		}

		work.removeEventListener(vc);

	}

}
