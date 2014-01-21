package com.sg.business.commons.handler;

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

public class CarlendarTest1 extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		DataSetFactory dsf = vc.getDataSetFactory();
		CalendarCaculater ds = new CalendarCaculater(dsf.getDataSet()
				.getDataItems());
		Shell parentShell = part.getSite().getShell();
		InputDialog input = new InputDialog(parentShell,Messages.get().CarlendarTest1_0,
				Messages.get().CarlendarTest1_1,
				new SimpleDateFormat("yyyyMMdd").format(new Date()), null); //$NON-NLS-1$
		if (input.open() == InputDialog.OK) {
			String value = input.getValue();
			double time = ds.getWorkingTime(value);
			if (time == 0d) {
				MessageUtil.showToast(parentShell, value,Messages.get().CarlendarTest1_3,
						SWT.ICON_INFORMATION);
			} else {
				MessageUtil.showToast(parentShell, value,Messages.get().CarlendarTest1_4 + time,
						SWT.ICON_INFORMATION);
			}
		}
	}

}
