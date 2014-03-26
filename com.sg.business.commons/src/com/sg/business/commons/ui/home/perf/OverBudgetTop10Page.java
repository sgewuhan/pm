package com.sg.business.commons.ui.home.perf;

import java.util.Comparator;

import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectMonthData;
import com.sg.business.model.etl.IProjectETL;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.etl.ProjectPresentation;

public class OverBudgetTop10Page extends AbstractProjectTop10Page {

	public OverBudgetTop10Page(Composite parent) {
		super(parent);
	}

	@Override
	protected Comparator<PrimaryObject> createComparator() {
		return new Comparator<PrimaryObject>() {
			@Override
			public int compare(PrimaryObject arg0, PrimaryObject arg1) {
				double value0 = 0;
				double value1 = 0;
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
				return -1 * new Double(value0).compareTo(new Double(value1));
			}

			private double getDataFromProjectMonthData(ProjectMonthData project) {
				return project
						.getDoubleValue(IProjectETL.F_INVESTMENT_DESIGNATED)
						- project.getDoubleValue(IProjectETL.F_BUDGET);
			}

			private double getDataFromProject(Project project) {
				ProjectPresentation pj0 = project.getPresentation();
				return pj0.getDesignatedInvestment() - pj0.getBudgetValue();
			}
		};
	}

	@Override
	protected String getLabelProviderKey() {
		return "performence.budget";
	}

	@Override
	protected void setMonthDataCondition(DBObject query) {
		query.put(ProjectETL.F_IS_OVERCOST_DEFINITED, Boolean.TRUE);
	}

	@Override
	protected void setProjectDataCondition(DBObject query) {
		query.put(ProjectETL.F_ETL + "." + ProjectETL.F_IS_OVERCOST_DEFINITED,
				Boolean.TRUE);
	}

	@Override
	protected String getIndenticatorLabel() {
		return "进行中项目超支占比";
	}

}
