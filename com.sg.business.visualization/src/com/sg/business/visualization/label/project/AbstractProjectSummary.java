package com.sg.business.visualization.label.project;

import java.text.DecimalFormat;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectMonthData;
import com.sg.business.visualization.label.projectset.ISummaryLabelProvider;
import com.sg.widgets.commons.labelprovider.CurrencyLabelProvider;
import com.sg.widgets.part.NavigatorControl;

public abstract class AbstractProjectSummary extends CurrencyLabelProvider implements
		ISummaryLabelProvider {


	@Override
	public String getSummary(Object data) {
		NavigatorControl nc  =(NavigatorControl) data;
		DataSet ds = nc.getViewerControl().getDataSet();
		List<PrimaryObject> dis = ds.getDataItems();
		double total = 0d;
		for (int i = 0; i < dis.size(); i++) {
			ProjectMonthData di = (ProjectMonthData) dis.get(i);
			Object value = getItemValue(di);
			if(value instanceof Number){
				total += ((Number) value).doubleValue();
			}
		}
		DecimalFormat nf = new DecimalFormat(Utils.NF_NUMBER_P2);

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='"//$NON-NLS-1$
				+ "color:#6f6f6f;"//$NON-NLS-1$
				+ "font-family:Î¢ÈíÑÅºÚ;"//$NON-NLS-1$
				+ "font-style:italic;"//$NON-NLS-1$
				+ "font-weight:bold;"//$NON-NLS-1$
				+ "font-size:11pt;"//$NON-NLS-1$
				+ "margin-left:20;"//$NON-NLS-1$
				+ "margin-top:8;"//$NON-NLS-1$
//				+ "float:right;"//$NON-NLS-1$
//				+ "word-break:break-all; "//$NON-NLS-1$
//				+ "white-space:normal; "//$NON-NLS-1$
				+ "display:block;"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append(nf.format(total/10000));
		sb.append("</span>");//$NON-NLS-1$
		return sb.toString();
		
	}

	protected Object getItemValue(ProjectMonthData di) {
		return di.getValue(getSummaryField());
	}

	protected abstract String getSummaryField() ;

}
