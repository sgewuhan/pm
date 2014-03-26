package com.sg.business.commons.ui.home.perf;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.sg.business.model.Organization;
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
				sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:8pt;color:#333333;" //$NON-NLS-1$
						+ "margin-left:0; display:block; width=1000px'>"); //$NON-NLS-1$
				String imageURL = null;

				List<RemoteFile> headpics = user
						.getGridFSFileValue(User.F_HEADPIC);
				if (headpics != null && headpics.size() > 0) {
					try {
						imageURL = FileUtil.getImageURL(headpics.get(0)
								.getNamespace(), new ObjectId(headpics.get(0)
								.getObjectId()), headpics.get(0).getDbName());
					} catch (Exception e) {
					}
				}

				// 显示用户照片
				if (imageURL != null) {
					sb.append("<img src='" //$NON-NLS-1$
							+ imageURL
							+ "' style='float:left; left:0; top:0; display:block;' width='28' height='28' />"); //$NON-NLS-1$
				}

				// 显示用户姓名
				String userHtmlLabel = user.getUsername();
				sb.append(userHtmlLabel);
				Organization org = user.getOrganization();
				if (org != null) {
					String simpleName = org.getSimpleName();
					sb.append("|" + simpleName);
				}

				sb.append("</span>"); //$NON-NLS-1$
				return sb.toString();
			}
		}
		return "?";
	}

}
