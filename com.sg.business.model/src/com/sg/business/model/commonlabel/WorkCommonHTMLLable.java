package com.sg.business.model.commonlabel;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.StructuredViewer;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class WorkCommonHTMLLable extends CommonHTMLLabel {

	private static final int COMPLETE_TASK = 1;
	private static final int START_WORK = 2;
	private static final int FINISH_WORK = 3;
	private static final int ASSIGN_WORK = 4;
	private static final int NONE = 0;

	private Work work;

	public WorkCommonHTMLLable(Work work) {
		this.work = work;
	}

	@Override
	public String getHTML() {
		if ("record".equals(key)) {
			return getHTMLForRecord();
		} else if ("singleline".equals(key)) {
			return getHTMLForSingleLine();
		} else if ("inlist".equals(key)) {
			return getHTMLForList();
		}

		Object configurator = getData();
		boolean control = (configurator instanceof ColumnConfigurator)
				&& "wbsndelivery".equals(((ColumnConfigurator) configurator)
						.getName());

		String userId = getContext().getAccountInfo().getConsignerId();
		UserTask currentTask = null;
		if (work.isExecuteWorkflowActivateAndAvailable()) {// 如果有流程
			List<UserTask> result = work.getReservedUserTasks(userId);
			result.addAll(work.getInprogressUserTasks(userId));
			if (result.size() > 0) {
				currentTask = result.get(0);
			}
		}

		int code = getOperationCode(work, currentTask, userId);

		// 标记
		String selectbarUrl = null;
		Date _planStart = work.getPlanStart();
		Date _actualStart = work.getActualStart();
		Date _planFinish = work.getPlanFinish();
		int remindBefore = work.getRemindBefore();

		// 首先判断当前时间是否晚于计划完成时间，如果是，显示为超期标签
		String lc = work.getLifecycleStatus();
		if (Work.STATUS_CANCELED_VALUE.equals(lc)
				|| Work.STATUS_FINIHED_VALUE.equals(lc)) {
		} else {
			if (_planFinish != null) {
				Date now = new Date();
				if (now.after(_planFinish)) {
					selectbarUrl = getSelectorURL(work,
							ImageResource.RED_BULLETIN);
				} else if (remindBefore > 0
						&& (_planFinish.getTime() - now.getTime()) < remindBefore * 3600000) {
					// 然后判断当前时间是否达到提醒时间
					selectbarUrl = getSelectorURL(work,
							ImageResource.YELLOW_BULLETIN);
				}
			}
		}

		if (selectbarUrl == null) {
			boolean userMarked = work.isMarked(userId);
			if (userMarked) {
				selectbarUrl = getSelectorURL(work, ImageResource.BLUE_BULLETIN);
			} else {
				selectbarUrl = getSelectorURL(work,
						ImageResource.WHITE_BULLETIN);

			}
		}

		// ---------------------------------------------------------------------------
		StringBuffer sb = new StringBuffer();

		String selectbar = "<img src='" //$NON-NLS-1$
				+ selectbarUrl
				+ "' style='border-style:none;position:absolute; left:0; top:0; display:block;' width='4' height='36' />"; //$NON-NLS-1$
		sb.append(selectbar);

		String imageUrl = "<img src='" + getHeaderImageURL(work) //$NON-NLS-1$
				+ "' style='position:absolute; left:6px; top:1px;' width='16' height='16' />"; //$NON-NLS-1$
		sb.append(imageUrl);

		sb.append("<span style='font-family:微软雅黑;font-size:9pt;padding-left:24px;'>"); //$NON-NLS-1$

		// 工作desc
		String workDesc = work.getDesc();
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);

		// 获得有关的项目信息
		Project project = work.getProject();
		if (project != null) {
			String projectDesc = project.getDesc();
			projectDesc = Utils.getPlainText(projectDesc);
			sb.append(" ");
			sb.append("<span style='color:#909090;'>");//$NON-NLS-1$
			sb.append(projectDesc);
			sb.append("</span>");//$NON-NLS-1$
		}

		// 有关时间
		sb.append("<br/>"); //$NON-NLS-1$

		if (!control) {
			sb.append("<span style='font-family:微软雅黑;font-size:9pt;float:right;'>"); //$NON-NLS-1$
			if (currentTask != null) {
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_FLOW_16X12,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' width='16' height='12' /> "); //$NON-NLS-1$
				sb.append(currentTask.getTaskName());
			} else if (code == ASSIGN_WORK) {
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(
						BusinessResource.IMAGE_REASSIGNMENT_16X12,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER));
				sb.append("' width='16' height='12' /> "); //$NON-NLS-1$
			}
			sb.append("</span>");//$NON-NLS-1$
		}

		String start = "?"; //$NON-NLS-1$
		String color = "";//$NON-NLS-1$
		if (_actualStart != null) {
			start = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _actualStart);
		} else if (_planStart != null) {
			start = String.format(Utils.FORMATE_DATE_COMPACT_SASH, _planStart);
			color = "color:#909090;";//$NON-NLS-1$
		}
		sb.append("<small style='padding-left:6px;" + color + "'>"); //$NON-NLS-1$
		sb.append(start);
		sb.append("</small>");//$NON-NLS-1$
		sb.append("<small style='color:#909090;'>"); //$NON-NLS-1$

		String finish = "?"; //$NON-NLS-1$
		if (_planFinish != null) {
			finish = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _planFinish);
		}
		sb.append("~"); //$NON-NLS-1$
		sb.append(finish);

		if (control) {
			// 显示负责人和指派者
			sb.append(" ");
			sb.append(Messages.get(getLocale()).Work_187);
			sb.append(": ");
			User user = work.getCharger();
			if (user == null) {
				sb.append("?");
			} else {
				sb.append(user.getUsername());
			}

			sb.append(" ");
			sb.append(Messages.get(getLocale()).Role_20);
			sb.append(": ");
			user = work.getAssigner();
			if (user == null) {
				sb.append("?");
			} else {
				sb.append(user.getUsername());
			}
		}

		sb.append("</small>"); //$NON-NLS-1$
		sb.append("</span>"); //$NON-NLS-1$

		if (control) {
			sb.append("<a href=\"gowork@" + work.get_id().toString() //$NON-NLS-1$ 
					+ "\" target=\"_rwt\">"); //$NON-NLS-1$
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_NAVIGATE_24,
					BusinessResource.PLUGIN_ID));
			sb.append("' style='border-style:none;position:absolute; right:40; bottom:8; display:block;' width='24' height='24' />"); //$NON-NLS-1$
			sb.append("</a>");//$NON-NLS-1$
		}

		return sb.toString();
	}

	private String getHTMLForList() {

		String userId = getContext().getAccountInfo().getConsignerId();
		UserTask currentTask = null;
		if (work.isExecuteWorkflowActivateAndAvailable()) {// 如果有流程
			List<UserTask> result = work.getReservedUserTasks(userId);
			result.addAll(work.getInprogressUserTasks(userId));
			if (result.size() > 0) {
				currentTask = result.get(0);
			}
		}

		int code = getOperationCode(work, currentTask, userId);

		// 标记
		String selectbarUrl = null;
		Date _planStart = work.getPlanStart();
		Date _actualStart = work.getActualStart();
		Date _planFinish = work.getPlanFinish();
		int remindBefore = work.getRemindBefore();

		// 首先判断当前时间是否晚于计划完成时间，如果是，显示为超期标签
		String lc = work.getLifecycleStatus();
		if (Work.STATUS_CANCELED_VALUE.equals(lc)
				|| Work.STATUS_FINIHED_VALUE.equals(lc)) {
		} else {
			if (_planFinish != null) {
				Date now = new Date();
				if (now.after(_planFinish)) {
					selectbarUrl = getSelectorURL(work,
							ImageResource.RED_BULLETIN);
				} else if (remindBefore > 0
						&& (_planFinish.getTime() - now.getTime()) < remindBefore * 3600000) {
					// 然后判断当前时间是否达到提醒时间
					selectbarUrl = getSelectorURL(work,
							ImageResource.YELLOW_BULLETIN);
				}
			}
		}

		if (selectbarUrl == null) {
			boolean userMarked = work.isMarked(userId);
			if (userMarked) {
				selectbarUrl = getSelectorURL(work, ImageResource.BLUE_BULLETIN);
			} else {
				selectbarUrl = getSelectorURL(work,
						ImageResource.WHITE_BULLETIN);

			}
		}

		// ---------------------------------------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer;'>");

		String selectbar = "<img src='" //$NON-NLS-1$
				+ selectbarUrl
				+ "' style='border-style:none;position:absolute; left:0; top:0; display:block;' width='4' height='36' />"; //$NON-NLS-1$
		sb.append(selectbar);

		String imageUrl = "<img src='" + getHeaderImageURL(work) //$NON-NLS-1$
				+ "' style='position:absolute; left:6px; top:1px;' width='16' height='16' />"; //$NON-NLS-1$
		sb.append(imageUrl);

		sb.append("<span style='font-family:微软雅黑;font-size:9pt;padding-left:24px;'>"); //$NON-NLS-1$

		// 获得有关的项目信息
		Project project = work.getProject();
		if (project != null) {
			String projectDesc = project.getDesc();
			projectDesc = Utils.getPlainText(projectDesc);
			sb.append(" ");
			sb.append("<span style='font-weight:bold;'>");//$NON-NLS-1$
			sb.append(projectDesc);
			sb.append("</span>");//$NON-NLS-1$
			sb.append(" ");//$NON-NLS-1$
		}

		// 工作desc
		String workDesc = work.getDesc();
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);


		// 有关时间
		sb.append("<br/>"); //$NON-NLS-1$

		sb.append("<span style='font-family:微软雅黑;font-size:9pt;float:right;'>"); //$NON-NLS-1$
		if (currentTask != null) {
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_FLOW_16X12,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
			sb.append("' width='16' height='12' /> "); //$NON-NLS-1$
			sb.append(currentTask.getTaskName());
		} else if (code == ASSIGN_WORK) {
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(
					BusinessResource.IMAGE_REASSIGNMENT_16X12,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
			sb.append("' width='16' height='12' /> "); //$NON-NLS-1$
		}
		sb.append("</span>");//$NON-NLS-1$

		String start = "?"; //$NON-NLS-1$
		String color = "";//$NON-NLS-1$
		if (_actualStart != null) {
			start = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _actualStart);
		} else if (_planStart != null) {
			start = String.format(Utils.FORMATE_DATE_COMPACT_SASH, _planStart);
			color = "color:#909090;";//$NON-NLS-1$
		}
		sb.append("<small style='padding-left:6px;" + color + "'>"); //$NON-NLS-1$
		sb.append(start);
		sb.append("</small>");//$NON-NLS-1$
		sb.append("<small style='color:#909090;'>"); //$NON-NLS-1$

		String finish = "?"; //$NON-NLS-1$
		if (_planFinish != null) {
			finish = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _planFinish);
		}
		sb.append("~"); //$NON-NLS-1$
		sb.append(finish);


		sb.append("</small>"); //$NON-NLS-1$
		sb.append("</span>"); //$NON-NLS-1$
		sb.append("</div>"); //$NON-NLS-1$
		
		sb.append(HtmlUtil.createBottomLine(4)); //$NON-NLS-1$

		return sb.toString();
	}

	private String getHTMLForSingleLine() {
		// 工作desc
		String desc = work.getDesc();
		desc = Utils.getPlainText(desc);
		Date _planFinish = work.getPlanFinish();
		Project project = work.getProject();

		// ---------------------------------------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer; border-bottom:1px dotted #cdcdcd;height=100%'>");

		sb.append("<span style='"//$NON-NLS-1$
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:10pt;"//$NON-NLS-1$
				+ "margin:0 2;"//$NON-NLS-1$
				+ "color:#4d4d4d;"//$NON-NLS-1$
				+ "width:" + 160
				+ "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append(desc);
		sb.append("</span>");

		sb.append("<span style='" + "color:#909090;" + "font-size:8pt;"
				+ "margin:0 2;" + "width:" + 140 + "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		// 如果是项目文档，显示项目名称
		if (project != null) {
			desc = project.getDesc();
			desc = Utils.getPlainText(desc);
			sb.append("项目:");
			sb.append(desc);
		}
		sb.append("</span>");

		// 有关时间
		sb.append("<span style='" + "color:#909090;" + "font-size:8pt;"
				+ "margin:0 2;" + "width:60px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		String finish = "?"; //$NON-NLS-1$
		if (_planFinish != null) {
			finish = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _planFinish);
		}
		sb.append(finish);
		sb.append("</span>");

		//				sb.append("<hr style='" //$NON-NLS-1$
		//						+ "color:#ededed;" //$NON-NLS-1$
		//						+ "position:absolute; " //$NON-NLS-1$
		//						+ "left:0; " //$NON-NLS-1$
		//						+ "bottom:0; " //$NON-NLS-1$
		//						+ "background-color:#ededed;" //$NON-NLS-1$
		//						+ "height:1px;" //$NON-NLS-1$
		//						+ "width:100%;" //$NON-NLS-1$
		//						+ "line-height:1px;" //$NON-NLS-1$
		//						+ "font-size:0;" //$NON-NLS-1$
		//						+ "border:none;" //$NON-NLS-1$
		//						+ "'>"); //$NON-NLS-1$

		sb.append("</div>");

		return sb.toString();
	}

	private int getOperationCode(Work work, UserTask currentTask, String userId) {

		// 如果是准备中的工作，显示为开始工作
		// 如果是进行中的工作，判断是否在当前的流程任务中
		if (currentTask != null) {
			return COMPLETE_TASK;
		} else {
			String status = work.getLifecycleStatus();
			String assignerId = work.getAssignerId();
			String chargerId = work.getChargerId();
			if (userId.equals(assignerId)) {
				return ASSIGN_WORK;
			} else if (userId.equals(chargerId)) {
				if (ILifecycle.STATUS_ONREADY_VALUE.equals(status)) {
					return START_WORK;
				} else if (ILifecycle.STATUS_WIP_VALUE.equals(status)) {
					return FINISH_WORK;
				}
			}
		}
		return NONE;

	}

	private String getSelectorURL(Work work, String style) {
		return FileUtil.getImageURL(style, Widgets.PLUGIN_ID,
				BusinessResource.IMAGE_FOLDER);
	}

	private String getHeaderImageURL(Work work) {
		String lc = work.getLifecycleStatus();
		if (ILifecycle.STATUS_CANCELED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_CANCEL_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_FINISH_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_ONREADY_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_READY_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_WIP_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_PAUSED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_PAUSE_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_NONE_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_READY_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		}
		return null;
		// return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_16,
		// BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	@SuppressWarnings("unchecked")
	private String getHTMLForRecord() {
		StructuredViewer viewer = getViewer();
		int widthHint = getWidthHint();

		// 工作desc
		String desc = work.getDesc();
		desc = Utils.getPlainText(desc);
		Date _planStart = work.getPlanStart();
		Date _actualStart = work.getActualStart();
		Date _planFinish = work.getPlanFinish();
		Project project = work.getProject();

		// ---------------------------------------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer;'>");

		sb.append("<div style='"//$NON-NLS-1$
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:10pt;"//$NON-NLS-1$
				+ "margin:0 2;"//$NON-NLS-1$
				+ "color:#4d4d4d;"//$NON-NLS-1$
				+ "width:" + 250
				+ "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append(desc);
		sb.append("</div>");

		sb.append("<div style='" + "color:#909090;" + "font-size:8pt;"
				+ "margin:0 2;" + "width:" + 250 + "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		// 如果是项目文档，显示项目名称
		if (project != null) {
			desc = project.getDesc();
			desc = Utils.getPlainText(desc);
			sb.append("项目:");
			sb.append(desc);
		}
		sb.append("</div>");

		// 有关时间
		sb.append("<div style='" + "color:#909090;" + "font-size:8pt;"
				+ "margin:0 2;" + "width:" + widthHint + "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		String start = "?"; //$NON-NLS-1$
		if (_actualStart != null) {
			start = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _actualStart);
		} else if (_planStart != null) {
			start = String.format(Utils.FORMATE_DATE_COMPACT_SASH, _planStart);
		}
		sb.append(start);
		String finish = "?"; //$NON-NLS-1$
		if (_planFinish != null) {
			finish = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _planFinish);
		}
		sb.append("~"); //$NON-NLS-1$
		sb.append(finish);
		sb.append("</div>");

		sb.append("<a href=\"gowork@" + work.get_id().toString() //$NON-NLS-1$ 
				+ "\" target=\"_rwt\">"); //$NON-NLS-1$
		sb.append("<img src='"); //$NON-NLS-1$
		String imageURL = FileUtil.getImageURL(BusinessResource.IMAGE_S_LEFT_49,
				BusinessResource.PLUGIN_ID);
		String imageURL_Hover = FileUtil.getImageURL(BusinessResource.IMAGE_S_LEFT_49_HOVER,
				BusinessResource.PLUGIN_ID);
		String imageURL_Pressed = FileUtil.getImageURL(BusinessResource.IMAGE_S_LEFT_49_PRESSED,
				BusinessResource.PLUGIN_ID);
		sb.append(imageURL);
		sb.append("' style='border-style:none;position:absolute; right:0; bottom:1; display:block;' width='50' height='49' "
				+ " onmouseover=\"this.src='"+ imageURL_Hover + "'\";"
				+ " onmouseout=\"this.src='"+imageURL+ "'\";"
				+ " onmousedown=\"this.src='"+imageURL_Pressed+ "'\";"
				+ "/>"); //$NON-NLS-1$
		sb.append("</a>");//$NON-NLS-1$

		List<PrimaryObject> input = (List<PrimaryObject>) viewer.getInput();
		ObjectId thisId = work.get_id();
		if (input != null && !input.isEmpty()) {
			// 判断如果当前记录是最后一个，不显示分割线
			ObjectId lastId = input.get(input.size() - 1).get_id();
			if (!thisId.equals(lastId)) {
				sb.append(HtmlUtil.createBottomLine(0)); //$NON-NLS-1$
			}

		}
		sb.append("</div>");

		return sb.toString();
	}

}
