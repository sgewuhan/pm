package com.sg.business.commons.labelprovider;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.portal.Portal;
import com.sg.business.model.User;

public class UserScenarioLabelprovider extends ColumnLabelProvider {

	@SuppressWarnings("unchecked")
	@Override
	public String getText(Object element) {
		String info = ""; //$NON-NLS-1$
		User user = (User) element;
		List<String> scenarios = (List<String>) user.getValue(User.F_SCENARIO);
		List<String[]> defs = Portal.getDefault().getScenariosDefinitionList();
		for (String scenario : scenarios) {
			for (String[] def : defs) {
				if (scenario.equals(def[0])) {
					info += def[1]+" "; //$NON-NLS-1$
				}
			}
		}
		return info;
	}

}
