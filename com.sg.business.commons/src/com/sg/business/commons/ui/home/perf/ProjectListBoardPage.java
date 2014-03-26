package com.sg.business.commons.ui.home.perf;

import org.bson.types.ObjectId;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectETL;

public class ProjectListBoardPage extends AbstractListBoardPage {

	public ProjectListBoardPage(Composite parent) {
		super(parent);
	}

	@Override
	protected String getGroupByField() {
		return ProjectETL.F_PROJECTID;
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
		return null;
	}

	@Override
	protected boolean hasUnWindField() {
		return false;
	}

	@Override
	protected String getListItemLabel(Object _id) {
		if (_id instanceof ObjectId) {
			Project project = ModelService.createModelObject(
					Project.class, (ObjectId) _id);
			StringBuffer sb = new StringBuffer();
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;"
					+ "margin-left:0; display:block; width=1000px'>");
			// 显示项目名称
			String label = project.getLabel();
			sb.append(label);
			sb.append("</span>");
			return sb.toString();
		}
		return "?";
	}
}
