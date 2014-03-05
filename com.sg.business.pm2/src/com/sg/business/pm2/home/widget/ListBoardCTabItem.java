package com.sg.business.pm2.home.widget;

import java.math.BigDecimal;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.toolkit.UserToolkit;

public class ListBoardCTabItem extends CTabItem {

	private TableViewer leftViewer;
	private TableViewer rightViewer;

	public ListBoardCTabItem(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE);
		Composite composite = new Composite(tabFolder, SWT.NONE);
		FormLayout layout = new FormLayout();
		composite.setLayout(layout);

		FormData fd = new FormData();
		leftViewer = createTableViewer(composite, fd);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(50, -1);
		fd.top = new FormAttachment(0, 0);
		fd.bottom = new FormAttachment(100, -1);
		createTableViewerColumn(leftViewer, "006633");

		fd = new FormData();
		rightViewer = createTableViewer(composite, fd);
		fd.left = new FormAttachment(50, 1);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(0, 0);
		fd.bottom = new FormAttachment(100, -1);
		createTableViewerColumn(rightViewer, "006633");

		this.setControl(composite);
	}

	private TableViewer createTableViewer(Composite composite, FormData fd) {
		TableViewer tv = new TableViewer(composite, SWT.NONE);
		Control table = tv.getControl();
		table.setLayoutData(fd);
		table.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		table.setData(RWT.CUSTOM_ITEM_HEIGHT, 40);
		tv.setContentProvider(ArrayContentProvider.getInstance());
		return tv;
	}

	private void createTableViewerColumn(TableViewer tv, final String color) {
		TableViewerColumn col = new TableViewerColumn(tv, SWT.CENTER);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Object[]) {
					Object[] objects = (Object[]) element;
					int number = (int) objects[2];
					StringBuffer sb = new StringBuffer();
					sb.append("<span style='position:absolute; right:10;bottom:10;FONT-FAMILY:微软雅黑;"
							+ "font-size:15pt;color:#"
							+ color
							+ ";text-align:center;'>");
					sb.append("<b>");
					sb.append(number);
					sb.append("</b>");
					sb.append("</span>");
					return sb.toString();
				}
				return "";
			}

		});
		col.getColumn().setWidth(40);

		col = new TableViewerColumn(tv, SWT.LEFT);
		if (tv.equals(leftViewer)) {
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof Object[]) {
						Object[] objects = (Object[]) element;
						Object _id = (Object) objects[0];
						if (_id != null) {
							String userid = (String) _id;
							User user = UserToolkit.getUserById(userid);
							return getUserLabel(user);
						}
					}
					return "";
				}
			});
		} else {
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof Object[]) {
						Object[] objects = (Object[]) element;
						Object _id = (Object) objects[0];
						if (_id != null) {
							PrimaryObject po = ModelService.createModelObject(
									Project.class, (ObjectId) _id);
							Project project = (Project) po;
							return getProjectLabel(project);
						}
					}
					return "";
				}
			});
		}
		col.getColumn().setWidth(150);

		col = new TableViewerColumn(tv, SWT.CANCEL);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Object[]) {
					Object[] objects = (Object[]) element;
					Double profit = (Double) objects[1];
					if (profit != null) {
						BigDecimal d = new BigDecimal(profit / 10000d);
						StringBuffer sb = new StringBuffer();
						sb.append("<span style='position:absolute; right:0;bottom:10;FONT-FAMILY:微软雅黑;"
								+ "font-size:10pt;color:#"
								+ color
								+ ";text-align:right;'>");
						sb.append("<b>");
						sb.append(d.setScale(2, BigDecimal.ROUND_HALF_UP));
						sb.append("</b>");
						sb.append("</span>");
						return sb.toString();
					}
				}
				return "";
			}
		});
		col.getColumn().setWidth(70);

	}

	public void setInput(List<Object[]> list) {
		leftViewer.setInput(list.get(0));
		rightViewer.setInput(list.get(1));
	}

	private String getUserLabel(User user) {
		if (user != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;"
					+ "margin-left:0; display:block; width=1000px'>");
			String imageURL = null;

			List<RemoteFile> headpics = user.getGridFSFileValue(User.F_HEADPIC);
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
				sb.append("<img src='"
						+ imageURL
						+ "' style='float:left; left:0; top:0; display:block;' width='36' height='36' />");
			}

			// 显示用户名称
			String userHtmlLabel = user.getUsername();
			sb.append("<b>");
			sb.append(userHtmlLabel);
			sb.append("</b>");

			sb.append("<br/>");
			sb.append("<small>");

			// 显示用户所属组织
			Organization org = user.getOrganization();
			if (org != null) {
				String path = org.getPath(2);
				sb.append(path);
			}
			sb.append("</small>");

			sb.append("</span>");
			return sb.toString();
		} else {
			return "?";
		}
	}

	private String getProjectLabel(Project project) {
		ProjectPresentation pres = project.getPresentation();

		String desc = pres.getDescriptionText();

		String coverImageURL = pres.getCoverImageURL();

		String launchOrganization = pres.getLaunchOrganizationText();

		String charger = pres.getChargerText();

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;margin-left:0; display:block; width=1000px'>"); //$NON-NLS-1$
		// 显示项目封面
		if (coverImageURL != null) {
			sb.append("<img src='" //$NON-NLS-1$
					+ coverImageURL
					+ "' style='float:left; left:0; top:0; display:block;' width='36' height='36' />"); //$NON-NLS-1$
		}
		// 显示项目名称
		sb.append("<b>"); //$NON-NLS-1$
		sb.append(desc);
		sb.append("</b>"); //$NON-NLS-1$
		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		// 显示承担组织
		sb.append(launchOrganization);
		// 显示负责人
		sb.append(" "); //$NON-NLS-1$
		sb.append(charger);
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$

		return sb.toString();
	}

}
