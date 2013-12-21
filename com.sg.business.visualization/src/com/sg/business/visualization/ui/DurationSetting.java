package com.sg.business.visualization.ui;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;

import com.sg.business.model.ProjectProvider;
import com.sg.business.visualization.nls.Messages;

public class DurationSetting extends Shell {

	private ProjectProvider projectProvider;

	public DurationSetting(Shell parent, ProjectProvider data) {
		super(parent, SWT.BORDER);
		this.projectProvider = data;
		createContent();
	}

	private void createContent() {
		setLayout(new FormLayout());
		final Combo yearCombo = new Combo(this, SWT.READ_ONLY);
		FormData fd = new FormData();
		yearCombo.setLayoutData(fd);
		fd.left = new FormAttachment(0, 4);
		fd.top = new FormAttachment(0, 4);
		fd.width = 100;

		final int startYear = Calendar.getInstance().get(Calendar.YEAR) - 5;
		for (int i = startYear; i < (startYear + 6); i++) {
			yearCombo.add("" + i); //$NON-NLS-1$
		}
		yearCombo.select(5);

		final Combo quarterCombo = new Combo(this, SWT.READ_ONLY);
		fd = new FormData();
		quarterCombo.setLayoutData(fd);
		fd.left = new FormAttachment(yearCombo, 4);
		fd.top = new FormAttachment(0, 4);
		fd.width = 80;
		quarterCombo.add(Messages.get().DurationSetting_0);
		for (int i = 1; i < 5; i++) {
			quarterCombo.add("" + i + Messages.get().DurationSetting_1); //$NON-NLS-1$
		}

		final Combo monthCombo = new Combo(this, SWT.READ_ONLY);
		fd = new FormData();
		monthCombo.setLayoutData(fd);
		fd.left = new FormAttachment(quarterCombo, 4);
		fd.top = new FormAttachment(0, 4);
		fd.right = new FormAttachment(100, -4);

		fd.width = 80;
		monthCombo.add(Messages.get().DurationSetting_2);
		for (int i = 1; i < 13; i++) {
			monthCombo.add("" + i + Messages.get().DurationSetting_3); //$NON-NLS-1$
		}

		final Button toToday = new Button(this, SWT.CHECK);
		toToday.setText(Messages.get().DurationSetting_4);

		Button ok = new Button(this, SWT.PUSH);
		ok.setText(Messages.get().DurationSetting_5);
		fd = new FormData();
		ok.setLayoutData(fd);
		fd.left = new FormAttachment(0, 4);
		fd.top = new FormAttachment(monthCombo, 16);
		fd.bottom = new FormAttachment(100, -4);
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int yearIndex = yearCombo.getSelectionIndex();
				setFilter(startYear + yearIndex,
						quarterCombo.getSelectionIndex(),
						monthCombo.getSelectionIndex(), toToday.getSelection());
				dispose();
			}
		});

		Button cancel = new Button(this, SWT.PUSH);
		cancel.setText(Messages.get().DurationSetting_6);
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
			}
		});

		fd = new FormData();
		cancel.setLayoutData(fd);
		fd.left = new FormAttachment(ok, 4);
		fd.top = new FormAttachment(monthCombo, 16);
		fd.bottom = new FormAttachment(100, -4);

		fd = new FormData();
		toToday.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(monthCombo, 16);
		fd.bottom = new FormAttachment(100, -4);

		addShellListener(new ShellListener() {

			@Override
			public void shellDeactivated(ShellEvent e) {
				close();
			}

			@Override
			public void shellClosed(ShellEvent e) {
			}

			@Override
			public void shellActivated(ShellEvent e) {
			}
		});
	}
	

	public void open(Point location){
		pack();
		setLocation(location);
		super.open();
	}

	protected void setFilter(int yearIndex, int quarterIndex, int monthIndex,
			boolean clearFilter) {
		if (clearFilter) {
			projectProvider.setParameters(null);
		} else {
			if (yearIndex < 0) {
				return;
			}
			Object[] parameters = new Object[2];

			parameters[0] = Calendar.getInstance();
			((Calendar) parameters[0]).set(Calendar.YEAR, yearIndex);
			
			if (monthIndex > 0) {
				parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_MONTH;
				((Calendar) parameters[0]).set(Calendar.MONTH, monthIndex - 1);

			} else if (quarterIndex > 0) {
				parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_QUARTER;
				((Calendar) parameters[0]).set(Calendar.MONTH,
						3 * (quarterIndex) - 1);
			} else{
				parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_YEAR;
			}

			projectProvider.setParameters(parameters);
		}

	}
	
	public static String getHeadParameterText(ProjectProvider data) {
		StringBuffer sb = new StringBuffer();
		if(data == null){
		}else if (data.parameters != null) {
			if (ProjectProvider.PARAMETER_SUMMARY_BY_YEAR
					.equals(data.parameters[1])) {
				sb.append(((Calendar) data.parameters[0]).get(Calendar.YEAR)
						+ Messages.get().DurationSetting_7);
			} else if (ProjectProvider.PARAMETER_SUMMARY_BY_QUARTER
					.equals(data.parameters[1])) {
				Calendar calendar = (Calendar) data.parameters[0];
				int month = calendar.get(Calendar.MONTH);
				sb.append(calendar.get(Calendar.YEAR) + "Äê" //$NON-NLS-1$
						+ (1 + (1 + month) / 4) + Messages.get().DurationSetting_8);
			} else if (ProjectProvider.PARAMETER_SUMMARY_BY_MONTH
					.equals(data.parameters[1])) {
				Calendar calendar = (Calendar) data.parameters[0];
				int month = calendar.get(Calendar.MONTH);
				sb.append(calendar.get(Calendar.YEAR) + Messages.get().DurationSetting_9 + (1 + month) + Messages.get().DurationSetting_10);
			}
		}else{
			sb.append(Messages.get().DurationSetting_11);
		}

		return sb.toString();
	}
}
