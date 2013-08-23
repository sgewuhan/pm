package com.sg.business.commons.handler.work;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.viewer.ViewerControl;

public class CarlendarTest1 extends AbstractNavigatorHandler {
	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		ViewerControl v = getCurrentViewerControl(event);
		DataSetFactory dsf = v.getDataSetFactory();
		CalendarCaculater ds = new CalendarCaculater(dsf.getDataSet()
				.getDataItems());
		Shell parentShell = HandlerUtil.getActiveShell(event);
		InputDialog input = new InputDialog(parentShell, "����ĳ���Ƿ�Ϊ������",
				"������yyyyMMdd��ʽ������",
				new SimpleDateFormat("yyyyMMdd").format(new Date()), null);
		if (input.open() == InputDialog.OK) {
			String value = input.getValue();
			double time = ds.getWorkingTime(value);
			if (time == 0d) {
				MessageUtil.showToast(parentShell, value, "����Ϣ��",
						SWT.ICON_INFORMATION);
			} else {
				MessageUtil.showToast(parentShell, value, "�ǹ����գ����칤ʱΪ" + time,
						SWT.ICON_INFORMATION);
			}
		}
	}

}
