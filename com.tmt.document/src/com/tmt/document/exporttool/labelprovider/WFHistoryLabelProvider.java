package com.tmt.document.exporttool.labelprovider;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.DummyModel;
import com.sg.business.model.IDocumentProcess;

public class WFHistoryLabelProvider extends ColumnLabelProvider {

	public WFHistoryLabelProvider() {
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof DummyModel) {
			return getProcessInstanceLabel((DummyModel) element);
		}
		return ""; //$NON-NLS-1$
	}
	
	protected String getProcessInstanceLabel(DummyModel dummyModel) {
		DBObject dbObject =  dummyModel.get_data();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$
		// 工作desc
		String workDesc = (String) dbObject.get(PrimaryObject.F_DESC);
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);

		sb.append("  "); //$NON-NLS-1$

		// 时间
		Date date = (Date) dbObject.get(PrimaryObject.F__CDATE);
		sb.append(String.format(Utils.FORMATE_DATE_SIMPLE, date));

		sb.append("<br/>"); //$NON-NLS-1$

		// 流程名称
		sb.append("<small>"); //$NON-NLS-1$
		String processName = (String) dbObject
				.get(IDocumentProcess.F_PROCESSNAME);
		workDesc = Utils.getPlainText(processName);
		sb.append(processName);
		sb.append("</small>"); //$NON-NLS-1$
		sb.append("</span>"); //$NON-NLS-1$

		return sb.toString();
	}

}
