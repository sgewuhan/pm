package com.sg.business.commons.labelprovider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Work;

public class WorkActualFinish extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		Date value = ((Work)element).getActualFinish();
		if(value!=null){
			return sdf.format(value);
		}else{
			return "";
		}
	}

}
