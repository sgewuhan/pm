package com.sg.business.visualization.view;

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
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.visualization.VisualizationActivator;
import com.sg.widgets.Widgets;
import com.sg.widgets.viewer.ColumnAutoResizer;

public class ListCTabItem extends CTabItem {

	private TableViewer topViewer;
	private TableViewer bottomViewer;
	@SuppressWarnings("rawtypes")
	private Class selected;

	public ListCTabItem(CTabFolder tabFolder, String title) {
		super(tabFolder, SWT.NONE);
		this.setText(title);
		Composite composite = new Composite(tabFolder, SWT.NONE);
		FormLayout layout = new FormLayout();
		composite.setLayout(layout);

		FormData fd = new FormData();
		topViewer = createTableViewer(composite, fd);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(0, 0);
		fd.bottom = new FormAttachment(50, -1);
		createTableViewerColumn(topViewer, "006633"); //$NON-NLS-1$

		Label l = new Label(composite, SWT.NONE);
		l.setBackground(Widgets.getColor(composite.getDisplay(), 192, 192, 192));
		fd = new FormData();
		l.setLayoutData(fd);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(50, 0);
		fd.height = 1;

		fd = new FormData();
		bottomViewer = createTableViewer(composite, fd);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(50, 1);
		fd.bottom = new FormAttachment(100, 0);
		createTableViewerColumn(bottomViewer, "990033"); //$NON-NLS-1$

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
					sb.append("<span style='position:absolute; right:10;bottom:10;FONT-FAMILY:微软雅黑;" //$NON-NLS-1$
							+ "font-size:15pt;color:#" //$NON-NLS-1$
							+ color
							+ ";text-align:center;'>"); //$NON-NLS-1$
					sb.append("<b>"); //$NON-NLS-1$
					sb.append(number);
					sb.append("</b>"); //$NON-NLS-1$
					sb.append("</span>"); //$NON-NLS-1$
					return sb.toString();
				}
				return ""; //$NON-NLS-1$
			}

		});
		col.getColumn().setWidth(40);

		col = new TableViewerColumn(tv, SWT.LEFT);
		col.setLabelProvider(new ColumnLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(Object element) {
				if (element instanceof Object[]) {
					Object[] objects = (Object[]) element;
					Object _id = (Object) objects[0];
					if (selected.equals(Project.class)) {
						PrimaryObject po = ModelService.createModelObject(
								selected, (ObjectId) _id);
						Project project = (Project) po;
						return getProjectLabel(project);
					} else if (selected.equals(User.class)) {
						String userid = (String) _id;
						User user = UserToolkit.getUserById(userid);
						return getUserLabel(user);
					} else if (selected.equals(Organization.class)) {
						PrimaryObject po = ModelService.createModelObject(
								selected, (ObjectId) _id);
						Organization org = (Organization) po;
						return getOrganizationLabel(org);
					}
					return ""; //$NON-NLS-1$
				}
				return ""; //$NON-NLS-1$
			}
		});
//		col.getColumn().setWidth(280);
		new ColumnAutoResizer(tv.getTable(), col.getColumn());

		col = new TableViewerColumn(tv, SWT.CANCEL);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Object[]) {
					Object[] objects = (Object[]) element;
					Double profit = (Double) objects[1];
					BigDecimal d = new BigDecimal(profit / 10000d);
					StringBuffer sb = new StringBuffer();
					sb.append("<span style='position:absolute; right:0;bottom:10;FONT-FAMILY:微软雅黑;" //$NON-NLS-1$
							+ "font-size:10pt;color:#" //$NON-NLS-1$
							+ color
							+ ";text-align:right;'>"); //$NON-NLS-1$
					sb.append("<b>"); //$NON-NLS-1$
					sb.append(d.setScale(2, BigDecimal.ROUND_HALF_UP));
					sb.append("</b>"); //$NON-NLS-1$
					sb.append("</span>"); //$NON-NLS-1$
					return sb.toString();

				}
				return ""; //$NON-NLS-1$
			}
		});
		col.getColumn().setWidth(70);

		col = new TableViewerColumn(tv, SWT.CANCEL);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Object[]) {
					Object[] objects = (Object[]) element;
					Integer lastNumber = (Integer) objects[3];
					int number = (int) objects[2];
					Boolean type = (Boolean) objects[4];

					String ImageURL1;
					String ImageURL2;
					if (type) {
						ImageURL1 = FileUtil.getImageURL("up_16.png", //$NON-NLS-1$
								VisualizationActivator.PLUGIN_ID);
						ImageURL2 = FileUtil.getImageURL("down_16.png", //$NON-NLS-1$
								VisualizationActivator.PLUGIN_ID);
					} else {
						ImageURL1 = FileUtil.getImageURL("down_16.png", //$NON-NLS-1$
								VisualizationActivator.PLUGIN_ID);
						ImageURL2 = FileUtil.getImageURL("up_16.png", //$NON-NLS-1$
								VisualizationActivator.PLUGIN_ID);
					}

					StringBuffer sb = new StringBuffer();
					sb.append("<span style='position:absolute;FONT-FAMILY:微软雅黑;font-size:7pt;" //$NON-NLS-1$
							+ "text-align:center;'>"); //$NON-NLS-1$
					if (lastNumber == null) {

						sb.append("<img src='" //$NON-NLS-1$
								+ ImageURL1
								+ "' style='float:left; left:0; top:0; display:block;' width='10' height='10' />"); //$NON-NLS-1$
					} else {
						int intLastValue = lastNumber.intValue();
						if (intLastValue > number) {
							sb.append("<img src='" //$NON-NLS-1$
									+ ImageURL1
									+ "' style='float:left; left:0; top:0; display:block;' width='10' height='10' />"); //$NON-NLS-1$
							sb.append("<br/>"); //$NON-NLS-1$
							sb.append(intLastValue - number);
						} else if (intLastValue < number) {
							sb.append("<img src='" //$NON-NLS-1$
									+ ImageURL2
									+ "' style='float:left; left:0; top:0; display:block;' width='10' height='10' />"); //$NON-NLS-1$
							sb.append("<br/>"); //$NON-NLS-1$
							sb.append(number - intLastValue);
						} else {
							sb.append("-"); //$NON-NLS-1$
						}
					}
					sb.append("</span>"); //$NON-NLS-1$
					return sb.toString();

				}
				return ""; //$NON-NLS-1$
			}
		});
		col.getColumn().setWidth(20);
	}

//	private void setBackground(Composite composite) {
//		Object adapter = composite.getAdapter(IWidgetGraphicsAdapter.class);
//		IWidgetGraphicsAdapter gfxAdapter = (IWidgetGraphicsAdapter) adapter;
//		int[] percents = new int[] { 0, 50, 100 };
//		Color[] style = new Color[] {
//
//		Widgets.getColor(null, 0xd0, 0xf8, 0xdb),
//
//		Widgets.getColor(null, 0xff, 0xff, 0xff),
//
//		Widgets.getColor(null, 0xff, 0xe4, 0xe4),
//
//		};
//		gfxAdapter.setBackgroundGradient(style, percents, true);
//	}

	@SuppressWarnings("rawtypes")
	public void setInput(Object[] topData, Object[] bottomData, Class selected) {
		this.selected = selected;
		topViewer.setInput(topData);
		bottomViewer.setInput(bottomData);
		bottomViewer.getTable().setTopIndex(bottomData.length - 1);
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

	private String getUserLabel(User user) {
		if (user != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;" //$NON-NLS-1$
					+ "margin-left:0; display:block; width=1000px'>"); //$NON-NLS-1$
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
				sb.append("<img src='" //$NON-NLS-1$
						+ imageURL
						+ "' style='float:left; left:0; top:0; display:block;' width='36' height='36' />"); //$NON-NLS-1$
			}

			// 显示用户名称
			String userHtmlLabel = user.getUsername();
			sb.append("<b>"); //$NON-NLS-1$
			sb.append(userHtmlLabel);
			sb.append("</b>"); //$NON-NLS-1$

			sb.append("<br/>"); //$NON-NLS-1$
			sb.append("<small>"); //$NON-NLS-1$

			// 显示用户所属组织
			Organization org = user.getOrganization();
			if(org!=null){
				String path = org.getPath(2);
				sb.append(path);
			}
			sb.append("</small>"); //$NON-NLS-1$

			sb.append("</span>"); //$NON-NLS-1$
			return sb.toString();
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	private String getOrganizationLabel(Organization org) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;" //$NON-NLS-1$
				+ "margin-left:0; display:block; width=1000px'>"); //$NON-NLS-1$

		String imageUrl = "<img src='" + org.getImageURL() //$NON-NLS-1$
				+ "' style='float:left;padding:2px' width='24' height='24' />"; //$NON-NLS-1$
		String label = org.getLabel();
		String path = org.getFullName();

		sb.append(imageUrl);
		sb.append("<b>"); //$NON-NLS-1$
		sb.append(label);
		sb.append("</b>"); //$NON-NLS-1$
		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		sb.append(path);
		sb.append("</small></span>"); //$NON-NLS-1$

		return sb.toString();
	}
}
