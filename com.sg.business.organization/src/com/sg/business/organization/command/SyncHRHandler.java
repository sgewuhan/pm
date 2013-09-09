package com.sg.business.organization.command;

import java.util.HashSet;
import java.util.Set;

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
//		// ��HR����֯����ͬ��
//		SyscHR syscHR = new SyscHR();
//		syscHR.doSyscHROrganization();
//
//		IWorkbenchPart part = HandlerUtil.getActivePart(event);
//		if (part instanceof IRefreshablePart) {
//			((IRefreshablePart) part).doRefresh();
//		}
//		System.out.println(System.currentTimeMillis() - start);
		
		Shell shell = HandlerUtil.getActiveShell(event);
//
//		AbstractWork po = ((AbstractWork) selected).makeChildWork();
//		ViewerControl vc = getCurrentViewerControl(event);
//		Assert.isNotNull(vc);
//
//		// �����������Ҫ��ʹ��currentViewerControl�������������¼��� �������ϵĽڵ�
//		// 1. ���ø������漰�����ݿ⣬��Ҫ��ά��ģ�͵ĸ��ӹ�ϵ��ͬʱViewerControlҲ��Ҫ���������¼���Ӧ
//		po.setParentPrimaryObject(selected);
//		// 2. ����po���¼��������ʵ���Ӧ
//		po.addEventListener(vc);
//
//		// ʹ�ñ༭���򿪱༭��������
//		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
//				po.getDefaultEditorId());
//		try {
//			DataObjectDialog.openDialog(po, (DataEditorConfigurator) conf,
//					true, null,"����"+po.getTypeName());
//		} catch (Exception e) {
//			MessageUtil.showToast(shell, "����"+po.getTypeName(), e.getMessage(),
//					SWT.ICON_ERROR);
//		}
//
//		// 3. ������ɺ��ͷ�������
//		po.removeEventListener(vc);
		
		
//		SyscHR syscHR = new SyscHR();
//		// ���������PMϵͳ��HRϵͳ�ٵ���֯��������ΪPMϵͳ��Ҫ�������֯ʹ��
//		Set<OrgExchange> insertSet = new HashSet<OrgExchange>();
//		// ���������PMϵͳ��HRϵͳ�����֯��������ΪPMϵͳ��Ҫɾ������֯ʹ��
//		Set<OrgExchange> removeSet = new HashSet<OrgExchange>();
//		// ���������PMϵͳ��HRϵͳ���Ʋ�һ�µ���֯��������ΪPMϵͳ��Ҫ�޸�ȫ�Ƶ���֯ʹ��
//		Set<OrgExchange> renameSet = new HashSet<OrgExchange>();
//		syscHR.doSyscHROrganization(insertSet, removeSet, renameSet);
		
		SyncHROrganizationDialog dialog = new SyncHROrganizationDialog(shell);
		int ret = dialog.open();
		System.out.println(ret);
		return null;
	}

}
