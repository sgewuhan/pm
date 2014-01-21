package com.sg.business.model.commonlabel;

import java.util.Date;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class WorkCommonHTMLLable extends CommonHTMLLabel {

	private static final int COMPLETE_TASK = 1;
	private static final int START_WORK = 2;
	private static final int FINISH_WORK = 3;
	private static final int ASSIGN_WORK = 4;
	private static final int NONE = 0;
	
	private Work work;
	
	public WorkCommonHTMLLable(Work work){
		this.work = work;
	}
	
	@Override
	public String getHTML() {
		String userId = getContext().getAccountInfo().getConsignerId();
		UserTask currentTask = null;
		if (work.isExecuteWorkflowActivateAndAvailable()) {// 如果有流程
			List<UserTask> result = work.getReservedUserTasks(userId);
			result.addAll(work.getInprogressUserTasks(userId));
			if (result.size() > 0) {
				currentTask = result.get(0);
			}
		}

		int code = getOperationCode(work, currentTask,userId);

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
			sb.append("<span style='color:#909090;'>");
			sb.append(projectDesc);
			sb.append("</span>");
		}

		// 有关时间
		sb.append("<br/>"); //$NON-NLS-1$
		
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
		sb.append("</span>");
		

		String start = "?"; //$NON-NLS-1$
		String color = "";
		if(_actualStart!=null){
			start = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
					_actualStart);
		}else if (_planStart != null) {
			start = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
					_planStart);
			color = "color:#909090;";
		}
		sb.append("<small style='padding-left:6px;"+color+"'>"); //$NON-NLS-1$
		sb.append(start);
		sb.append("</small>");
		sb.append("<small style='color:#909090;'>"); //$NON-NLS-1$

		String finish = "?"; //$NON-NLS-1$
		if (_planFinish != null) {
			finish = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
					_planFinish);
		}
		sb.append("~"); //$NON-NLS-1$
		sb.append(finish);

		sb.append("</small>"); //$NON-NLS-1$
		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}

	private int getOperationCode(Work work, UserTask currentTask,String userId) {
		
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

}
