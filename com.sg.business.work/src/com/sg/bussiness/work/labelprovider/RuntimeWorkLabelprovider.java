package com.sg.bussiness.work.labelprovider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Work;

public class RuntimeWorkLabelprovider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof Work){
			Work work = (Work) element;
			return getRuntimeWorkHTMLLabel(work);
		}
		return "";
	}

	private String getRuntimeWorkHTMLLabel(Work work) {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		//生命周期状态
		String lifecycleState = work.getLifecycleStatusText();
		//根工作desc
		String rootDesc = work.getRoot().toString();
		//工作desc
		String workDesc = work.getDesc();
		
		Date date=work.getPlanStart();
		String planStart="";
		if(date!=null){
			planStart = sdf.format(date);
		}
		
		date=work.getPlanFinish();
		String planFinish="";
		if(date!=null){
			planFinish = sdf.format(date);
		}
		
		date=work.getActualStart();
		String actualStart="";
		if(date!=null){
			actualStart = sdf.format(date);
		}
		
		date=work.getActualFinish();
		String actualFinish="";
		if(date!=null){
			actualFinish = sdf.format(date);
		}
		
		int planDuration = work.getPlanDuration();
		Integer actualDuration=work.getActualDuration();
		
	
        sb.append(lifecycleState+","+rootDesc+","+workDesc);
        sb.append("<br/>");
        sb.append(planStart+","+planFinish+","+actualStart+","+actualFinish+","+planDuration+","+actualDuration);
        sb.append("<br/>");
		return sb.toString();
	}
	

}
