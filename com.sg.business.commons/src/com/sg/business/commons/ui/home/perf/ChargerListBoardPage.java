package com.sg.business.commons.ui.home.perf;

import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.toolkit.UserToolkit;

public class ChargerListBoardPage extends AbstractListBoardPage {

	public ChargerListBoardPage(Composite parent) {
		super(parent);
	}

	@Override
	protected String getGroupByField() {
		return Project.F_CHARGER;
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
		if (_id instanceof String) {
			String userid = (String) _id;
			User user = UserToolkit.getUserById(userid);
			if (user != null) {
				StringBuffer sb = new StringBuffer();
				sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;"
						+ "margin-left:0; display:block; width=1000px'>");

				// 显示用户名称
				String userHtmlLabel = user.getUsername();
				// sb.append("<b>");
				sb.append(userHtmlLabel);
				// sb.append("</b>");

				sb.append("</span>");
				return sb.toString();
			}
		}
		return "?";
	}

}
