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
		MessageUtil.showToast("����Ҫѡ��һ���ϼ�", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {

		Work childWork = ((Work) selected).makeChildWork();
		ViewerControl vc = getCurrentViewerControl(event);
		Assert.isNotNull(vc);

		// �����������Ҫ��ʹ��currentViewerControl�������������¼��� �������ϵĽڵ�
		// 1. ���ø������漰�����ݿ⣬��Ҫ��ά��ģ�͵ĸ��ӹ�ϵ��ͬʱViewerControlҲ��Ҫ���������¼���Ӧ
		childWork.setParentPrimaryObject(selected);
		// 2. ����po���¼��������ʵ���Ӧ
		childWork.addEventListener(vc);

		// ʹ�ñ༭���򿪱༭��������
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				Work.EDITOR_CREATE_RUNTIME_WORK);
		try {
			DataObjectDialog.openDialog(childWork,
					(DataEditorConfigurator) conf, true, null, "��������");

		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		// 3. ������ɺ��ͷ�������
		childWork.removeEventListener(vc);

	}

}
