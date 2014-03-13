package com.sg.business.commons.operation.handler;

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
import com.sg.business.resource.nls.Messages;
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
		InputDialog input = new InputDialog(parentShell, Messages.get().CarlendarTest2_0,
				Messages.get().CarlendarTest2_1,
				new SimpleDateFormat("yyyyMMdd").format(new Date()), null); //$NON-NLS-1$
		if (input.open() == InputDialog.OK) {
			String value = input.getValue();
			String[] values = value.split(","); //$NON-NLS-1$
			try {
				Date value1 = new SimpleDateFormat("yyyyMMdd").parse(values[0]); //$NON-NLS-1$
				Date value2 = new SimpleDateFormat("yyyyMMdd").parse(values[1]); //$NON-NLS-1$
				int time = ds.getWorkingDays(value1, value2);
				MessageUtil.showToast(parentShell, value, Messages.get().CarlendarTest2_6 + time,
						SWT.ICON_INFORMATION);

			} catch (ParseException e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
