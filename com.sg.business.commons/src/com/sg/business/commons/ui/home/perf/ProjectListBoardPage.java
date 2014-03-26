package com.sg.business.commons.ui.home.perf;

import org.bson.types.ObjectId;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.etl.ProjectPresentation;

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
		return "各项目销售利润排名";
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
			ProjectPresentation pres = project.getPresentation();

			String desc = pres.getDescriptionText();

			String coverImageURL = pres.getCoverImageURL();

			StringBuffer sb = new StringBuffer();
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;margin-left:0; display:block; width=1000px'>"); //$NON-NLS-1$
			// 显示项目封面
			if (coverImageURL != null) {
				sb.append("<img src='" //$NON-NLS-1$
						+ coverImageURL
						+ "' style='float:left; left:0; top:0; display:block;' width='28' height='28' />"); //$NON-NLS-1$
			}
			// 显示项目名称
			sb.append(desc);
			sb.append("</span>"); //$NON-NLS-1$

			return sb.toString();
		}
		return "?";
	}
}
