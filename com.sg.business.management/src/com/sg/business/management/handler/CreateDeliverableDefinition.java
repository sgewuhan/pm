package com.sg.business.management.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.business.model.Deliverable;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.view.NavigatorPart;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateDeliverableDefinition extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("����Ҫѡ����ӵ��ϼ�����", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command, Map<String, Object> parameters, IStructuredSelection selection) {

		Shell shell = part.getSite().getShell();

		PrimaryObject po = ((AbstractWork) selected)
				.makeDeliverableDefinition(Deliverable.TYPE_OUTPUT);
		Assert.isNotNull(currentViewerControl);

		// �����������Ҫ��ʹ��currentViewerControl�������������¼��� �������ϵĽڵ�
		// 1. ���ø������漰�����ݿ⣬��Ҫ��ά��ģ�͵ĸ��ӹ�ϵ��ͬʱViewerControlҲ��Ҫ���������¼���Ӧ
		po.setParentPrimaryObject(selected);
		// 2. ����po���¼��������ʵ���Ӧ
		po.addEventListener(currentViewerControl);

		// ʹ�ñ༭���򿪱༭��������
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				po.getDefaultEditorId());
		try {
			DataObjectDialog.openDialog(po, (DataEditorConfigurator) conf,
					true, null, "���" + po.getTypeName());
		} catch (Exception e) {
			MessageUtil.showToast(shell, "���" + po.getTypeName(),
					e.getMessage(), SWT.ICON_ERROR);
		}

		// 3. ������ɺ��ͷ�������
		po.removeEventListener(currentViewerControl);

		// 4. ˢ�µ�ǰҳ����ĵ�ģ����ͼ
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		NavigatorPart np = (NavigatorPart) page
				.findView("management.documentdefinition");
		if (np != null) {
			np.reloadMaster();
		}
	}

}
