package com.sg.bussiness.work.labelprovider;

import java.text.SimpleDateFormat;

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
		String planStart = sdf.format(work.getPlanStart());
		String PlanFinish = sdf.format(work.getPlanFinish());
		String actualStart =sdf.format(work.getActualStart());
		String actualFinish =sdf.format(work.getActualFinish());
		int planDuration = work.getPlanDuration();
		int actualDuration=work.getActualDuration();
		
	
        sb.append(lifecycleState+","+rootDesc+","+workDesc);
        sb.append("<br/>");
        sb.append(planStart+","+PlanFinish+","+actualStart+","+actualFinish+","+planDuration+","+actualDuration);
        sb.append("<br/>");
		return sb.toString();
	}
	

}
