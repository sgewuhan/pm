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
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		MessageUtil.showToast("����Ҫѡ��һ���ϼ�", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {

		Shell shell = HandlerUtil.getActiveShell(event);

		Work po = ((Work) selected).makeChildWork();
		ViewerControl vc = getCurrentViewerControl(event);
		Assert.isNotNull(vc);

		// �����������Ҫ��ʹ��currentViewerControl�������������¼��� �������ϵĽڵ�
		// 1. ���ø������漰�����ݿ⣬��Ҫ��ά��ģ�͵ĸ��ӹ�ϵ��ͬʱViewerControlҲ��Ҫ���������¼���Ӧ
		po.setParentPrimaryObject(selected);
		// 2. ����po���¼��������ʵ���Ӧ
		po.addEventListener(vc);

		// ʹ�ñ༭���򿪱༭��������
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				"edit.work.plan.0");//ʹ�ò������̵Ĺ����༭�����д���

		try {
			DataObjectDialog.openDialog(po, (DataEditorConfigurator) conf,
					true, null, "����" + po.getTypeName());
			
			//ˢ���ϼ�����
			List<PrimaryObject> tobeRefresh = new ArrayList<PrimaryObject>();
			tobeRefresh.add((Work) selected);
			AbstractWork parent = ((Work) selected).getParent();
			while(parent!=null){
				tobeRefresh.add(parent);
				parent = ((Work) parent).getParent();
			}
			vc.getViewer().update(tobeRefresh.toArray(), null);
			

			// 4. ��������Ϣ���ݵ��༭��
			sendNavigatorActionEvent(event, INavigatorActionListener.CUSTOMER,
					new Integer(INavigatorActionListener.REFRESH));

		} catch (Exception e) {
			MessageUtil.showToast(shell, "����" + po.getTypeName(),
					e.getMessage(), SWT.ICON_ERROR);
		}

		// 3. ������ɺ��ͷ�������
		po.removeEventListener(vc);

	}

}
