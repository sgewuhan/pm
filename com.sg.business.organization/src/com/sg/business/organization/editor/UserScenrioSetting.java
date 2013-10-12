package com.sg.business.organization.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.Portal;
import com.sg.business.model.User;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class UserScenrioSetting extends AbstractFormPageDelegator {
	
	private Button[] buttons;

	public UserScenrioSetting() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		final User user;
		try {
			user = (User) input.getData();
			List<?> scenarios = (List<?>) user.getValue(User.F_SCENARIO);
			parent.setLayout(new RowLayout(SWT.VERTICAL));
			List<String[]> defs = Portal.getDefault()
					.getScenariosDefinitionList();
			buttons = new Button[defs.size()];
			for (int i = 0; i < defs.size(); i++) {
				Button button = new Button(parent, SWT.CHECK);
				button.setData(defs.get(i)[0]);
				button.setText(defs.get(i)[1]);
				if (scenarios != null && scenarios.contains(defs.get(i)[0])) {
					button.setSelection(true);
				}
				button.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						setUserScenario(user);
						setDirty(true);
					}

				});
				buttons[i] = button;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return parent;
	}

	protected void setUserScenario(PrimaryObject userData) {
		List<String> scenarios = new ArrayList<String>();
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].getSelection()) {
				String fid = (String) buttons[i].getData();
				scenarios.add(fid);
			}
		}

		try {
			userData.setValue(User.F_SCENARIO, scenarios);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void commit(boolean onSave) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	


}
