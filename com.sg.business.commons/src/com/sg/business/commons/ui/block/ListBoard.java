package com.sg.business.commons.ui.block;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.toolkit.UserToolkit;

public class ListBoard {

	private TableViewer leftViewer;
	private TableViewer rightViewer;
	private Label leftLabel;
	private Label rightLabel;
//	private int year;
	private int month;
	private Organization org;
	private Label topLabel;

	public ListBoard(Composite composite) {
		FormData fd = new FormData();
		topLabel = new Label(composite, SWT.NONE);
		topLabel.setLayoutData(fd);
		HtmlUtil.enableMarkup(topLabel);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = 32;
		
		fd = new FormData();
		leftLabel = new Label(composite, SWT.NONE);
		leftLabel.setLayoutData(fd);
		HtmlUtil.enableMarkup(leftLabel);

		fd.top = new FormAttachment(topLabel);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(50);
//		fd.bottom = new FormAttachment(25);
		fd.height = 36;
		
		
		rightLabel = new Label(composite,SWT.NONE);
		fd = new FormData();
		rightLabel.setLayoutData(fd);
		HtmlUtil.enableMarkup(rightLabel);
		fd.top = new FormAttachment(topLabel);
		fd.left = new FormAttachment(50);
		fd.right = new FormAttachment(100);
//		fd.bottom = new FormAttachment(25);
		fd.height = 36;

		fd = new FormData();
		leftViewer = createTableViewer(composite, fd);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(50);
		fd.top = new FormAttachment(leftLabel);
		fd.bottom = new FormAttachment(100);
		createTableViewerColumn(leftViewer, "006633");

		fd = new FormData();
		rightViewer = createTableViewer(composite, fd);
		fd.left = new FormAttachment(50);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(rightLabel);
		fd.bottom = new FormAttachment(100);
		createTableViewerColumn(rightViewer, "006633");

	}

	private TableViewer createTableViewer(Composite composite, FormData fd) {
		TableViewer tv = new TableViewer(composite, SWT.NONE);
		Control table = tv.getControl();
		table.setLayoutData(fd);
		HtmlUtil.enableMarkup(table);

		table.setData(RWT.CUSTOM_ITEM_HEIGHT, 28);
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
					sb.append("<span style='position:absolute;FONT-FAMILY:微软雅黑;"
							+ "font-size:11pt;color:#"
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
		col.getColumn().setWidth(200);

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
						sb.append("<span style='position:absolute; FONT-FAMILY:微软雅黑;"
								+ "font-size:9pt;color:#"
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
		col.getColumn().setWidth(40);

	}

	public void setInput(List<Object[]> list) {
		leftViewer.setInput(list.get(1));
		rightViewer.setInput(list.get(2));
	}

	private String getUserLabel(User user) {
		if (user != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;"
					+ "margin-left:0; display:block; width=1000px'>");

			// 显示用户名称
			String userHtmlLabel = user.getUsername();
//			sb.append("<b>");
			sb.append(userHtmlLabel);
//			sb.append("</b>");

			sb.append("</span>");
			return sb.toString();
		} else {
			return "?";
		}
	}

	private String getProjectLabel(Project project) {
		ProjectPresentation pres = project.getPresentation();

		String desc = pres.getDescriptionText();

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;color:#333333;margin-left:0; display:block; width=1000px'>"); //$NON-NLS-1$
		// 显示项目名称
//		sb.append("<b>"); //$NON-NLS-1$
		sb.append(desc);
//		sb.append("</b>"); //$NON-NLS-1$
		sb.append("</span>"); //$NON-NLS-1$

		return sb.toString();
	}

//	public void setYear(int year) {
//		this.year = year;
//	}

	protected void updateLabel(Locale locale) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='");
		sb.append("width:500px;");
		sb.append("height:36px;" + "margin:1px;");
		sb.append("'>");
		sb.append("<div style='display:block;width:4px; height:28px;  "
				+ "float:left;background:" + "#33b5e5" + ";'>");
		sb.append("</div>");
		sb.append("<div style='" + "display:-moz-inline-box; "
				+ "display:inline-block;" + "height:28px;" + "color:#909090;"
				+ "font-family:微软雅黑;font-size:14pt; '>");
//		sb.append(year);
//		sb.append("年");
		sb.append(org.getLabel());
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month-1);
		sb.append(cal
				.getDisplayName(Calendar.MONTH, Calendar.LONG, locale));
		sb.append("销售利润排名");
		sb.append("</div>");
		topLabel.setText(sb.toString());
		sb = new StringBuffer();
		sb.append("<div style='" + "font-family:微软雅黑;" + "margin:8;"
				+ "width:100%;" + "'>");
		sb.append("<div style='" + "font-size:11pt;" + "color:#6a6a6a;"
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "height:100%;" + "width:280;" + "'>");
		sb.append("<span style='margin:0 0 0 8;'>");
		sb.append("排名前十的项目负责人");
		sb.append("</span>");
		sb.append("</div>");
		leftLabel.setText(sb.toString());
		sb = new StringBuffer();
		sb.append("<div style='" + "font-family:微软雅黑;" + "margin:8;"
				+ "width:100%;" + "'>");
		sb.append("<div style='" + "font-size:11pt;" + "color:#6a6a6a;"
				+ "border-bottom:1px dotted #cdcdcd;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "height:100%;" + "width:280;" + "'>");
		sb.append("<span style='margin:0 0 0 8;'>");
		sb.append("排名前十的项目");
		sb.append("</span>");
		sb.append("</div>");
		rightLabel.setText(sb.toString());

		
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setOrganization(Organization org) {
		this.org = org;
	}

}
