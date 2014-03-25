package com.sg.business.commons.ui.home.perf;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectMonthData;
import com.sg.business.model.etl.IProjectETL;
import com.sg.business.model.etl.ProjectETL;
import com.sg.widgets.Widgets;

public class DelayTop10Page extends AbstractProjectTop10Page {

	public DelayTop10Page(Composite parent) {
		super(parent);
	}

	@Override
	protected Comparator<PrimaryObject> createComparator() {
		return new Comparator<PrimaryObject>() {
			@Override
			public int compare(PrimaryObject arg0, PrimaryObject arg1) {
				long value0 = 0;
				long value1 = 0;
				if (arg0 instanceof Project) {
					value0 = getDataFromProject((Project) arg0);
				} else if (arg0 instanceof ProjectMonthData) {
					value0 = getDataFromProjectMonthData((ProjectMonthData)arg0);
				}

				if (arg1 instanceof Project) {
					value1 = getDataFromProject((Project) arg1);
				} else if (arg1 instanceof ProjectMonthData) {
					value1 = getDataFromProjectMonthData((ProjectMonthData)arg1);
				}
				return -1 * new Long(value0).compareTo(new Long(value1));
			}

			private long getDataFromProjectMonthData(ProjectMonthData project) {
				Date pf = (Date) project.getValue(Project.F_PLAN_FINISH);
				if(pf==null){
					return 0l;
				}
				Integer year = (Integer) project.getValue(IProjectETL.F_YEAR);
				Integer month = (Integer) project.getValue(IProjectETL.F_MONTH);
				if(year == null|| month == null){
					return 0l;
				}
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, year.intValue());
				cal.set(Calendar.MONTH, month.intValue());
				cal.set(Calendar.DATE, 1);
				cal.add(Calendar.DATE, -1);
				return cal.getTimeInMillis()-pf.getTime();
			}

			private long getDataFromProject(Project project) {
				Date pf = project.getPlanFinish();
				if(pf==null){
					return 0l;
				}
				Date now = new Date();
				return now.getTime()-pf.getTime();
			}
		};
	}

	@Override
	protected String getLabelProviderKey() {
		return "performence.schedure";
	}
	
	@Override
	protected Color getHeadBackground() {
		return Widgets.getColor(getDisplay(), 0xff, 0xd9, 0x80);
	}
	
	@Override
	protected Color getIndicatorPanelBackgound() {
		return Widgets.getColor(getDisplay(), 0xff, 0xbd, 0x21);
	}

	@Override
	protected void setMonthDataCondition(DBObject query) {
		query.put(ProjectETL.F_IS_DELAY_DEFINITED, Boolean.TRUE);

	}

	@Override
	protected void setProjectDataCondition(DBObject query) {
		query.put(ProjectETL.F_ETL + "." + ProjectETL.F_IS_DELAY_DEFINITED,
				Boolean.TRUE);
	}

	@Override
	protected String getIndenticatorLabel() {
		return "超期项目数占比";
	}
}
