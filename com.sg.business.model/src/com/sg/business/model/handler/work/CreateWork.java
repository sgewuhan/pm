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
		MessageUtil.showToast("����Ҫѡ��һ���ϼ�", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);

		AbstractWork po = ((AbstractWork) selected).makeChildWork();
		ViewerControl currentViewerControl = getCurrentViewerControl(event);
		Assert.isNotNull(currentViewerControl);

		// �����������Ҫ��ʹ��currentViewerControl�������������¼��� �������ϵĽڵ�
		// 1. ���ø������漰�����ݿ⣬��Ҫ��ά��ģ�͵ĸ��ӹ�ϵ��ͬʱViewerControlҲ��Ҫ���������¼���Ӧ
		po.setParentPrimaryObject(selected);
		// 2. ����po���¼��������ʵ���Ӧ
		po.addEventListener(currentViewerControl);

		// ʹ�ñ༭���򿪱༭��������
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

		// 3. ������ɺ��ͷ�������
		po.removeEventListener(currentViewerControl);
	}

	private String getTitle(AbstractWork po) {
		return (po instanceof WorkDefinition) ? "��ӹ�������" : "��ӹ���";
	}

}
