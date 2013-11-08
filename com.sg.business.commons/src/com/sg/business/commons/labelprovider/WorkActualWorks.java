package com.sg.business.commons.labelprovider;

import java.text.DecimalFormat;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Work;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class WorkActualWorks extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Work) {

			DecimalFormat nf = new DecimalFormat(Utils.NF_NUMBER_P2);
			Double value = ((Work) element).getActualWorks();
			if (value != null) {
				return nf.format(value.doubleValue());
			}
		}
		return "";
	}

}
