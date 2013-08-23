package com.sg.business.commons.labelprovider;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Work;

public class WorkPlanWorks extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		DecimalFormat nf = new DecimalFormat(Utils.NF_NUMBER_P2);
		Double value = ((Work)element).getPlanWorks();
		if(value!=null){
			return nf.format(value.doubleValue());
		}else{
			return "";
		}
	}

}
