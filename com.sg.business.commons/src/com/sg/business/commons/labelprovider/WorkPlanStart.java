package com.sg.business.commons.labelprovider;

import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Work;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class WorkPlanStart extends ConfiguratorColumnLabelProvider{

	@Override
	public String getText(Object element) {
		if (element instanceof Work) {
			Date value = ((Work) element).getPlanStart();
			if (value != null) {
				return String.format(Utils.FORMATE_DATE_COMPACT_SASH, value);
			}
		}
		return "";
	}
}
