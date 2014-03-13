package com.sg.business.commons.column.labelprovider;

import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Work;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class WorkActualFinish extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Work) {
			Date value = ((Work) element).getActualFinish();
			if (value != null) {
				return String.format(Utils.FORMATE_DATE_COMPACT_SASH, value);
			}
		}
		return ""; //$NON-NLS-1$
	}

}
