package com.sg.business.commons.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.dataset.calendarsetting.CalendarCaculater;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.viewer.ViewerControl;

public class CarlendarTest2 extends AbstractNavigatorHandler {
	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		DataSetFactory dsf = vc.getDataSetFactory();
		Shell parentShell = part.getSite().getShell();

		CalendarCaculater ds = new CalendarCaculater(dsf.getDataSet()
				.getDataItems());
		InputDialog input = new InputDialog(parentShell, "测试某天是否为工作日",
				"请输入yyyyMMdd格式的日期，两个日期使用,分割",
				new SimpleDateFormat("yyyyMMdd").format(new Date()), null);
		if (input.open() == InputDialog.OK) {
			String value = input.getValue();
			String[] values = value.split(",");
			try {
				Date value1 = new SimpleDateFormat("yyyyMMdd").parse(values[0]);
				Date value2 = new SimpleDateFormat("yyyyMMdd").parse(values[1]);
				int time = ds.getWorkingDays(value1, value2);
				MessageUtil.showToast(parentShell, value, "工作天数" + time,
						SWT.ICON_INFORMATION);

			} catch (ParseException e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
