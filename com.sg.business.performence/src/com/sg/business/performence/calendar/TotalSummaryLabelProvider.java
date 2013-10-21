package com.sg.business.performence.calendar;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IWorksSummary;

public class TotalSummaryLabelProvider extends ColumnLabelProvider {


	public TotalSummaryLabelProvider() {
	}
	
	@Override
	public String getText(Object element) {
		IWorksSummary ws = ((PrimaryObject)element).getAdapter(IWorksSummary.class);
		if(ws == null){
			return "";
		}
		
		double performenceWorks = ws.getWorksPerformenceTotalSummary();
		double allocateWorks = ws.getWorksAllocateTotalSummary();

		DecimalFormat df = new DecimalFormat("#########");
		String _performenceWorks = performenceWorks==0?"":df.format(performenceWorks);
		String _allocateWorks = allocateWorks==0?"":df.format(allocateWorks);
		
		return _performenceWorks+"/"+_allocateWorks;
	}


}
