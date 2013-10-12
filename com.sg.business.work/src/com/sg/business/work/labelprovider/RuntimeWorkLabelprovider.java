package com.sg.business.work.labelprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.bson.types.BasicBSONList;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.jbpm.task.Status;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;

public class RuntimeWorkLabelprovider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Work) {
			Work work = (Work) element;
			if (work.isProjectWBSRoot()) {
				return getProjectHTMLLabel(work);
			} else {
				return getRuntimeWorkHTMLLabel(work);
			}
		}
		return "";
	}

	private String getProjectHTMLLabel(Work work) {
		Project project = work.getProject();
		if (project == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");

		// **********************************************************************************
		// 右侧第一行，标记
		CurrentAccountContext context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getConsignerId();
		boolean userMarked = work.isMarked(userId);
		if (userMarked) {
			String selectbar = "<img src='"
					+ getSelectorURL(work, ImageResource.BLUE_BULLETIN)
					+ "' style='float:right;padding:0px;margin:0px' width='6' height='40' />";
			sb.append(selectbar);
		} else {
			String selectbar = "<img src='"
					+ getSelectorURL(work, ImageResource.WHITE_BULLETIN)
					+ "' style='float:right;padding:0px;margin:0px' width='6' height='40' />";
			sb.append(selectbar);

		}

		// **********************************************************************************
		// 右侧第一行，需要指派以及负责人图标
		// 如果需要再指派，添加再指派图标
		String assignerId = work.getAssignerId();
		if (userId.equals(assignerId)) {
			String imageUrl = "<img src='"
					+ FileUtil.getImageURL(
							BusinessResource.IMAGE_REASSIGNMENT_24,
							BusinessResource.PLUGIN_ID,
							BusinessResource.IMAGE_FOLDER)
					+ "' style='float:right;padding-right:4px' width='24' height='24' />";
			sb.append(imageUrl);
		}

		User charger = project.getCharger();
		if (charger != null) {
			sb.append("<span style='float:right;padding-right:4px'>");
			sb.append(charger);
			sb.append("</span>");
		}

		// **********************************************************************************
		// 左侧第一行，工作图标
		String imageUrl = "<img src='"
				+ FileUtil.getImageURL(BusinessResource.IMAGE_PROJECT_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER)
				+ "' style='float:left;padding:6px' width='24' height='24' />";
		sb.append(imageUrl);

		String desc = project.getDesc();
		desc = Utils.getPlainText(desc);
		sb.append("<b>项目: " + desc + "</b>");

		String projectNumber = project.getProjectNumber();
		sb.append(" [" + projectNumber + "]");

		Date _planStart = project.getPlanStart();
		String planStart = "";
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_MONTH_DATE_COMPACT_SASH);

		if (_planStart != null) {
			planStart = sdf.format(_planStart);
		}

		// String selectbar = "<img src='"
		// + getSelectorURL(work,ImageResource.WHITE_BULLETIN)
		// +
		// "' style='float:left;padding:px;margin:0px' width='16' height='8' />";
		// sb.append(selectbar);

		sb.append(" ");
		sb.append(planStart);
		sb.append("~");

		Date _planFinish = project.getPlanFinish();
		String planFinish = "";
		if (_planFinish != null) {
			planFinish = sdf.format(_planFinish);
		}
		sb.append(planFinish);
		sb.append("  ");

		sb.append("<br/><small>");
		String[] workOrders = project.getWorkOrders();
		for (int i = 0; i < workOrders.length; i++) {
			if (i == 0) {
				sb.append("<b>WON</b>:");
			} else {
				sb.append(" ");
			}
			sb.append(workOrders[i]);
		}

		sb.append("</small></span>");
		return sb.toString();
	}

	private String getRuntimeWorkHTMLLabel(Work work) {
		CurrentAccountContext context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getConsignerId();

		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_MONTH_DATE_COMPACT_SASH);

		User charger = work.getCharger();

		// style="position: absolute; overflow: hidden; z-index: 3;
		// vertical-align: middle; white-space: nowrap;

		boolean isOwner = isOwner(work, userId);
		if (!isOwner) {
			sb.append("<span style='color:#bbbbbb;FONT-FAMILY:微软雅黑;font-size:9pt'>");
		} else {
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");
		}

		// ---------------------------------------------------------------------------
		// 标记
		Date _planStart = work.getPlanStart();
		Date _planFinish = work.getPlanFinish();
		Date _actualStart = work.getActualStart();
		Date _actualFinish = work.getActualFinish();
		int remindBefore = work.getRemindBefore();
		// 首先判断当前时间是否晚于计划完成时间，如果是，显示为超期标签
		Date now = new Date();
		String selectbarUrl = null;
		if (isOwner && _planFinish != null) {
			if (now.after(_planFinish)) {
				selectbarUrl = getSelectorURL(work, ImageResource.RED_BULLETIN);
			} else if (remindBefore > 0
					&& (_planFinish.getTime() - now.getTime()) < remindBefore * 3600000) {
				// 然后判断当前时间是否达到提醒时间
				selectbarUrl = getSelectorURL(work,
						ImageResource.YELLOW_BULLETIN);
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
		String selectbar = "<img src='"
				+ selectbarUrl
				+ "' style='float:right;padding:0px;margin:0px' width='6' height='40' />";
		sb.append(selectbar);
		// -----------------------------------------------------------------------------------------

		
		if (charger != null) {
			sb.append("<span style='float:right;padding-right:4px'>");
			sb.append(charger);
			sb.append("</span>");
		}

		String assignerId = work.getAssignerId();
		if (userId.equals(assignerId)) {
			String imageUrl = "<img src='"
					+ FileUtil.getImageURL(
							BusinessResource.IMAGE_REASSIGNMENT_16,
							BusinessResource.PLUGIN_ID,
							BusinessResource.IMAGE_FOLDER)
							+ "' style='float:right' width='16' height='16' />";
			sb.append(imageUrl);
		}

		IProcessControl pc = (IProcessControl) work
				.getAdapter(IProcessControl.class);

		String imageUrl = "<img src='" + getHeaderImageURL(work, pc)
				+ "' style='float:left;padding:6px' width='16' height='16' />";
		sb.append(imageUrl);

		// 工作desc
		String workDesc = work.getDesc();
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);

		// 有关时间
		sb.append("<br/>");
		sb.append("<small>");

		sb.append(getWorkflowSummaryInformation(work, pc));

		String planStart = "";
		if (_planStart != null) {
			planStart = sdf.format(_planStart);
		}

		// String selectbar = "<img src='"
		// + getSelectorURL(work,ImageResource.WHITE_BULLETIN)
		// +
		// "' style='float:left;padding:px;margin:0px' width='16' height='8' />";
		// sb.append(selectbar);

		sb.append("");
		sb.append("<b>P</b>:");
		sb.append(planStart);
		sb.append("~");

		String planFinish = "";
		if (_planFinish != null) {
			planFinish = sdf.format(_planFinish);
		}
		sb.append(planFinish);
		sb.append("  ");

		String actualStart = "";
		if (_actualStart != null) {
			actualStart = sdf.format(_actualStart);
		}
		sb.append("<b>A</b>:");
		sb.append(actualStart);
		sb.append("~");

		String actualFinish = "";
		if (_actualFinish != null) {
			actualFinish = sdf.format(_actualFinish);
		}
		sb.append(actualFinish);
		sb.append("  ");

		sb.append("<b>D</b>:");

		Integer actualDuration = work.getActualDuration();
		if (actualDuration == null) {
			actualDuration = 0;
		}
		sb.append(actualDuration);
		sb.append("/");

		Integer planDuration = work.getPlanDuration();
		if (planDuration == null) {
			planDuration = 0;
		}
		sb.append(planDuration);

		sb.append("</small>");

		sb.append("</span>");
		return sb.toString();
	}

	private String getWorkflowSummaryInformation(Work work, IProcessControl pc) {
		DBObject data = pc.getWorkflowTaskData(Work.F_WF_EXECUTE);
		if (data == null) {
			return "";
		}

		DBObject latestTask = null;
		Iterator<String> iter = data.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Object value = data.get(key);
			if (value instanceof DBObject) {
				DBObject task = (DBObject) value;
				if (latestTask == null) {
					latestTask = task;
				} else {
					Object createdon = task
							.get(IProcessControl.F_WF_TASK_CREATEDON);
					if (createdon instanceof Date) {
						Date latestDate = (Date) latestTask
								.get(IProcessControl.F_WF_TASK_CREATEDON);
						if (((Date) createdon).after(latestDate)) {
							latestTask = task;
						}
					}
				}
			}
		}

		if (latestTask == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='float:right;padding-right:4px'>");
		// 根据状态取流程图标
		Object taskstatus = latestTask.get(IProcessControl.F_WF_TASK_STATUS);
		sb.append("<img src='" + getTaskStatusImageURL(taskstatus)
				+ "' style='float:left;padding:6px' width='10' height='10' />");

		Object taskname = latestTask.get(IProcessControl.F_WF_TASK_NAME);
		sb.append(" ");
		sb.append(taskname);
		sb.append(" ");
		Object owner = latestTask.get(IProcessControl.F_WF_TASK_ACTUALOWNER);
		if (owner instanceof String) {
			User ownerUser = UserToolkit.getUserById((String) owner);
			sb.append(ownerUser);
		}
		sb.append("</span>");
		return sb.toString();
	}

	private String getSelectorURL(Work work, String style) {
		return FileUtil.getImageURL(style, Widgets.PLUGIN_ID,
				BusinessResource.IMAGE_FOLDER);
	}

	private String getTaskStatusImageURL(Object taskstatus) {
		if (Status.Created.name().equals(taskstatus)) {
			return FileUtil.getImageURL(
					BusinessResource.IMAGE_WF_WORK_READY_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.Ready.name().equals(taskstatus)) {
			return FileUtil.getImageURL(
					BusinessResource.IMAGE_WF_WORK_READY_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.Completed.name().equals(taskstatus)) {
			return FileUtil.getImageURL(
					BusinessResource.IMAGE_WF_WORK_CLOSE_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.Error.name().equals(taskstatus)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WF_WORK_STOP_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.Failed.name().equals(taskstatus)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WF_WORK_STOP_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.Exited.name().equals(taskstatus)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WF_WORK_STOP_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.InProgress.name().equals(taskstatus)) {
			return FileUtil.getImageURL(
					BusinessResource.IMAGE_WF_WORK_PROCESS_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.Obsolete.name().equals(taskstatus)) {
			return FileUtil.getImageURL(
					BusinessResource.IMAGE_WF_WORK_CANCEL_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.Reserved.name().equals(taskstatus)) {
			return FileUtil.getImageURL(
					BusinessResource.IMAGE_WF_WORK_READY_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (Status.Suspended.name().equals(taskstatus)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WF_WORK_STOP_10,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		}
		return FileUtil.getImageURL(BusinessResource.IMAGE_WF_WORK_READY_10,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

	private boolean isOwner(Work work, String userId) {

		String chargerId = work.getChargerId();
		if (userId.equals(chargerId)) {
			return true;
		}

		String assignerId = work.getAssignerId();
		if (userId.equals(assignerId)) {
			return true;
		}

		BasicBSONList pidlist = work.getParticipatesIdList();
		if (pidlist != null && pidlist.contains(userId)) {
			return true;
		}

		return false;
	}

	private String getHeaderImageURL(Work work, IProcessControl pc) {
		if (work.isProjectWBSRoot()) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_PROJECT_32,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		}

		String lc = work.getLifecycleStatus();
		if (ILifecycle.STATUS_CANCELED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_CANCEL_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_FINISH_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_ONREADY_VALUE.equals(lc)) {
			if (pc.isWorkflowActivate(Work.F_WF_EXECUTE)) {
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK2_READY_16,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER);
			} else {
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK2_READY_16,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER);
			}
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(lc)) {
			if (pc.isWorkflowActivate(Work.F_WF_EXECUTE)) {
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK2_WIP_16,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER);
			} else {
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK2_WIP_16,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER);
			}
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

}
