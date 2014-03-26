package com.sg.business.commons.operation.action;

import org.eclipse.swt.widgets.Control;

import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class CreateChildWork extends AbstractWorkDetailPageAction {

	@Override
	public void run(Work data, Control control) {

		Work po = data.makeChildWork();

		// ʹ�ñ༭���򿪱༭��������
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				"edit.work.plan.0");//ʹ�ò������̵Ĺ����༭�����д��� //$NON-NLS-1$

		// �����㴴���ӹ���
		if ((data.getValue(Work.F_INTERNAL_PARA_CHARGERID)) != null) {
			po.setValue(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
			conf = Widgets.getEditorRegistry().getConfigurator(
					"edit.work.plan.change");
		}

		try {
			DataObjectDialog d = DataObjectDialog.openDialog(po,
					(DataEditorConfigurator) conf, true, null,
					Messages.get().CreateWork_2 + po.getTypeName());
			if (DataObjectDialog.OK == d.getReturnCode()) {
				pageReload(true);
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

	}

	@Override
	protected boolean visiableWhen(Work work) {
		return work.canCreateChildWork(getContext());
	}

}
