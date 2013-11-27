package com.sg.business.project.handler;

import java.util.Calendar;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.viewer.ViewerControl;

public class QueryDelayProcess extends AbstractNavigatorHandler {

	private class InputDialogExtension extends InputDialog {
		int year = -1;
		int month = -1;

		private InputDialogExtension(Shell parentShell, String dialogTitle,
				String dialogMessage, String initialValue,
				IInputValidator validator) {
			super(parentShell, dialogTitle, dialogMessage, initialValue,
					validator);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Calendar calendar = Calendar.getInstance();
			Composite composite = new Composite(parent, SWT.NONE);

			composite.setLayout(new GridLayout(2,true));

			final Combo comboYear = new Combo(composite, SWT.READ_ONLY);
			comboYear.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
					| GridData.HORIZONTAL_ALIGN_FILL));
			comboYear.add("" + calendar.get(Calendar.YEAR));
			comboYear.add("" + (calendar.get(Calendar.YEAR) - 1));
			comboYear.add("" + (calendar.get(Calendar.YEAR) - 2));
			final Combo comboMonth = new Combo(composite, SWT.READ_ONLY);
			for (int i = 1; i < 13; i++) {
				comboMonth.add("" + i);
			}

			comboMonth.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
					| GridData.HORIZONTAL_ALIGN_FILL));
			comboYear.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = comboYear.getSelectionIndex();
					if (index != -1) {
						year = Calendar.getInstance().get(Calendar.YEAR)
								- index;
					} else {
						year = -1;
					}
				}
			});

			comboMonth.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = comboMonth.getSelectionIndex();
					if (index != -1) {
						month = index;
					} else {
						month = -1;
					}
				}
			});

			applyDialogFont(composite);
			composite.pack();
			return composite;
		}
           @Override
		   protected void buttonPressed(int buttonId) {
				if (IDialogConstants.OK_ID == buttonId) {
					if(year!=-1&&month!=-1){
						okPressed();
					}
				} else if (IDialogConstants.CANCEL_ID == buttonId) {
					cancelPressed();
				}
		    }
		
		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			
			
			createButton(parent, IDialogConstants.OK_ID,
					IDialogConstants.get().OK_LABEL, true);
			createButton(parent, IDialogConstants.CANCEL_ID,
					IDialogConstants.get().CANCEL_LABEL, false);
		}

	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {

		Shell parentShell = part.getSite().getShell();
		InputDialogExtension input = new InputDialogExtension(parentShell,
				"Ñ¡ÔñÔÂ·Ý", null, null, null);
		if (input.open() == InputDialog.OK) {

			vc.getDataSetFactory().setQueryCondition(
					new BasicDBObject().append("year", input.year).append(
							"month", input.month));
			vc.doReloadData(true);
		}
	}

}
