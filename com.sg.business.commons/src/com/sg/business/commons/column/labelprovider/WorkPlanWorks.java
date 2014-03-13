package com.sg.business.commons.column.labelprovider;

import java.text.DecimalFormat;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Work;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class WorkPlanWorks extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Work) {
			DecimalFormat nf = new DecimalFormat(Utils.NF_NUMBER_P2);
			Double value = ((Work) element).getPlanWorks();
			if (value != null) {
				return nf.format(value.doubleValue());
			}

		}
		return ""; //$NON-NLS-1$
	}
}
