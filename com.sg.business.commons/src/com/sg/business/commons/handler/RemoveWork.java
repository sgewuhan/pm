package com.sg.business.commons.handler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveWork extends AbstractNavigatorHandler {


	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("����Ҫѡ��һ��ִ��ɾ��", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		Shell shell = part.getSite().getShell();
		
		int size = selection.size();
		int yes = MessageUtil.showMessage(shell, "ɾ��"+selected.getDesc()+(size>1?" �ȹ�����":""),
				"��ȷ��Ҫɾ�� "+selected.getDesc()+(size>1?" �ȹ���":" ����")+"��\n�ò��������ɻָ���ѡ��YESȷ��ɾ����", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if(yes!=SWT.YES){
			return;
		}
		
		Iterator<?> iter = selection.iterator();
		while(iter.hasNext()){
			Work work = (Work) iter.next();
			boolean b = deleteSingleWork(work, part, currentViewerControl, shell);
			if(!b){
				break;
			}
		}
	}

	private boolean deleteSingleWork(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Shell shell) {
		Assert.isNotNull(currentViewerControl);

		if(selected.getParentPrimaryObjectCache()==null){
			MessageUtil.showToast(shell, "ɾ��"+selected.getTypeName(), "����"+selected.getTypeName()+"����ɾ��", SWT.ICON_WARNING);
			return false;
		}

		//����ǹ�������Ҫˢ�¿�ʼ�����ʱ��
		List<Work> toUpdate = null;
		if(selected instanceof Work){
			Work work = (Work) selected;
			toUpdate = work.getAllParents();
		}

		selected.addEventListener(currentViewerControl);
		try {
			//
			selected.doRemove(new CurrentAccountContext());
			
			// 4. ��������Ϣ���ݵ��༭��
			sendNavigatorActionEvent(part, INavigatorActionListener.CUSTOMER,
					new Integer(INavigatorActionListener.REFRESH));
			
		} catch (Exception e) {
			MessageUtil.showMessage(shell, "ɾ��"+selected.getTypeName(), e.getMessage(),
					SWT.ICON_WARNING);
			return false;
		}
		selected.removeEventListener(currentViewerControl);
		
		if(toUpdate!=null){
			currentViewerControl.getViewer().update(toUpdate.toArray(), null);
		}
		
		return true;
	}

}
