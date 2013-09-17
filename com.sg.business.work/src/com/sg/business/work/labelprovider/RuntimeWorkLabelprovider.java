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
			return getRuntimeWorkHTMLLabel(work);
		}
		return "";
	}

	private String getRuntimeWorkHTMLLabel(Work work) {
		CurrentAccountContext context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getConsignerId();

		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);

		User charger = work.getCharger();

		// style="position: absolute; overflow: hidden; z-index: 3;
		// vertical-align: middle; white-space: nowrap;

		boolean isOwner = isOwner(work, userId);
		if (!isOwner) {
			sb.append("<span style='color:#bbbbbb;FONT-FAMILY:微软雅黑;font-size:9pt'>");
		} else {
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");
		}
		
		// 所有者标记
		String lc = work.getLifecycleStatus();
		if (charger != null && userId.equals(charger.getUserid())
				&& (Work.STATUS_ONREADY_VALUE.equals(lc)||Work.STATUS_WIP_VALUE.equals(lc))) {
			String selectbar = "<img src='"
					+ getSelectorURL(work,ImageResource.BLUE_BULLETIN)
					+ "' style='float:right;padding:0px;margin:0px' width='8' height='40' />";
			sb.append(selectbar);
		}else{
			String selectbar = "<img src='"
					+ getSelectorURL(work,ImageResource.WHITE_BULLETIN)
					+ "' style='float:right;padding:0px;margin:0px' width='8' height='40' />";
			sb.append(selectbar);

		}
		
		if (charger != null) {
			sb.append("<span style='float:right;padding-right:4px'>");
			sb.append(charger);
			sb.append("</span>");
		}
		String imageUrl = "<img src='" + getHeaderImageURL(work)
				+ "' style='float:left;padding:6px' width='32' height='24' />";
		sb.append(imageUrl);

		// 工作desc
		String workDesc = work.getDesc();
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);

		// BasicBSONList participateIds = work.getParticipatesIdList();
		// if (participateIds != null && participateIds.size() > 0) {
		// String pid = (String) participateIds.get(0);
		// User user = UserToolkit.getUserById(pid);
		// sb.append(user);
		// if (participateIds.size() > 1) {
		// sb.append("...");
		// }
		// }

		// 有关时间
		sb.append("<br/>");
		sb.append("<small>");

		sb.append(getWorkflowSummaryInformation(work));

		Date date = work.getPlanStart();
		String planStart = "";
		if (date != null) {
			planStart = sdf.format(date);
		}
		sb.append("计划:");
		sb.append(planStart);
		sb.append("~");

		date = work.getPlanFinish();
		String planFinish = "";
		if (date != null) {
			planFinish = sdf.format(date);
		}
		sb.append(planFinish);
		sb.append("  ");

		date = work.getActualStart();
		String actualStart = "";
		if (date != null) {
			actualStart = sdf.format(date);
		}
		sb.append("实际:");
		sb.append(actualStart);
		sb.append("~");

		date = work.getActualFinish();
		String actualFinish = "";
		if (date != null) {
			actualFinish = sdf.format(date);
		}
		sb.append(actualFinish);
		sb.append("  ");

		sb.append("工期:");

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

	private String getWorkflowSummaryInformation(Work work) {
		DBObject data = work.getWorkflowTaskData(Work.F_WF_EXECUTE);
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
					Object createdon = task.get(Work.F_WF_TASK_CREATEDON);
					if (createdon instanceof Date) {
						Date latestDate = (Date) latestTask
								.get(Work.F_WF_TASK_CREATEDON);
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
		Object taskstatus = latestTask.get(Work.F_WF_TASK_STATUS);
		sb.append("<img src='" + getTaskStatusImageURL(taskstatus)
				+ "' style='float:left;padding:6px' width='10' height='10' />");

		Object taskname = latestTask.get(Work.F_WF_TASK_NAME);
		sb.append(" ");
		sb.append(taskname);
		sb.append(" ");
		Object owner = latestTask.get(Work.F_WF_TASK_ACTUALOWNER);
		if (owner instanceof String) {
			User ownerUser = UserToolkit.getUserById((String) owner);
			sb.append(ownerUser);
		}
		sb.append("</span>");
		return sb.toString();
	}

	private String getSelectorURL(Work work, String style) {
		return FileUtil.getImageURL(style,
				Widgets.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
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

	private String getHeaderImageURL(Work work) {
		if (work.isProjectWBSRoot()) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_PROJECT_32,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		}

		String lc = work.getLifecycleStatus();
		if (ILifecycle.STATUS_CANCELED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_CANCEL_32x24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_FINISH_32x24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_ONREADY_VALUE.equals(lc)) {
			if (work.isWorkflowActivate(Work.F_WF_EXECUTE)) {
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK2_READY_32x24,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER);
			} else {
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK_READY_32x24,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER);
			}
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(lc)) {
			if (work.isWorkflowActivate(Work.F_WF_EXECUTE)) {
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK2_WIP_32x24,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER);
			} else {
				return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_WIP_32x24,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER);
			}
		} else if (ILifecycle.STATUS_PAUSED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_PAUSE_32x24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_NONE_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_32x24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		}
		return null;
		// return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_32x24,
		// BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	}

}
