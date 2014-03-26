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
		return "各部门销售利润排名";
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
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:8pt;color:#333333;" //$NON-NLS-1$
					+ "margin-left:0; display:block; width=1000px;vertical-align:middle;'>"); //$NON-NLS-1$

//			String imageUrl = "<img src='" + org.getImageURL() //$NON-NLS-1$
//					+ "' style='float:left;padding:2px' width='24' height='24' />"; //$NON-NLS-1$
			String label = org.getPath(2);

//			sb.append(imageUrl);
			sb.append(label);
			sb.append("</span>"); //$NON-NLS-1$

			return sb.toString();
		}
		return "?";
	}

}
