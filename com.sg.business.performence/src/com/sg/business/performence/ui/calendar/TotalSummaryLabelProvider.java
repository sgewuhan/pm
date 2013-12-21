package com.sg.business.performence.ui.calendar;

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
			return ""; //$NON-NLS-1$
		}
		
		double performenceWorks = ws.getWorksPerformenceTotalSummary();
		double allocateWorks = ws.getWorksAllocateTotalSummary();

		DecimalFormat df = new DecimalFormat("#########"); //$NON-NLS-1$
		String _performenceWorks = performenceWorks==0?"":df.format(performenceWorks); //$NON-NLS-1$
		String _allocateWorks = allocateWorks==0?"":df.format(allocateWorks); //$NON-NLS-1$
		
		return _performenceWorks+"/"+_allocateWorks; //$NON-NLS-1$
	}


}
