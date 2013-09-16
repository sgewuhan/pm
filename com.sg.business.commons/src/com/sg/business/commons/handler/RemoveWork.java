package com.sg.business.commons.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveWork extends AbstractNavigatorHandler {


	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		MessageUtil.showToast("您需要选择一项执行删除", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		if(selected.getParentPrimaryObject()==null){
			MessageUtil.showToast(shell, "删除"+selected.getTypeName(), "顶级"+selected.getTypeName()+"不可删除", SWT.ICON_WARNING);
			return;
		}
		int yes = MessageUtil.showMessage(shell, "删除"+selected.getTypeName(),
				"您确定要删除这个"+selected.getTypeName()+"吗？\n该操作将不可恢复，选择YES确认删除。", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if(yes!=SWT.YES){
			return;
		}
		
		ViewerControl currentViewerControl = getCurrentViewerControl(event);
		Assert.isNotNull(currentViewerControl);

		//如果是工作，需要刷新开始和完成时间
		List<Work> toUpdate = null;
		if(selected instanceof Work){
			Work work = (Work) selected;
			toUpdate = work.getAllParents();
		}

		selected.addEventListener(currentViewerControl);
		try {
			selected.doRemove(new CurrentAccountContext());
			
			// 4. 将更改消息传递到编辑器
			sendNavigatorActionEvent(event, INavigatorActionListener.CUSTOMER,
					new Integer(INavigatorActionListener.REFRESH));
			
		} catch (Exception e) {
			MessageUtil.showMessage(shell, "删除"+selected.getTypeName(), e.getMessage(),
					SWT.ICON_WARNING);
		}
		selected.removeEventListener(currentViewerControl);
		
		if(toUpdate!=null){
			currentViewerControl.getViewer().update(toUpdate.toArray(), null);
		}
	}

}
