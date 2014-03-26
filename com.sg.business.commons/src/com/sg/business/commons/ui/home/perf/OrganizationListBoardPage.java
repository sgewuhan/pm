package com.sg.business.commons.ui.home.perf;

import org.bson.types.ObjectId;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectETL;

public class OrganizationListBoardPage extends AbstractListBoardPage {

	public OrganizationListBoardPage(Composite parent) {
		super(parent);
	}

	@Override
	protected String getGroupByField() {
		return Project.F_LAUNCH_ORGANIZATION;
	}

	@Override
	protected String getGroupField() {
		return ProjectETL.F_MONTH_SALES_PROFIT;
	}

	@Override
	protected String getTitle() {
		return "销售利润排名";
	}

	@Override
	protected Object getUnWindField() {
		return Project.F_LAUNCH_ORGANIZATION;
	}

	@Override
	protected boolean hasUnWindField() {
		return true;
	}

	@Override
	protected String getListItemLabel(Object _id) {
		if (_id instanceof ObjectId) {
			Organization org = ModelService.createModelObject(
					Organization.class, (ObjectId) _id);
			StringBuffer sb = new StringBuffer();
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;"
					+ "margin-left:0; display:block; width=1000px'>");
			// 显示组织名称
			String label = org.getLabel();
			sb.append(label);
			sb.append("</span>");
			return sb.toString();
		}
		return "?";
	}

}
