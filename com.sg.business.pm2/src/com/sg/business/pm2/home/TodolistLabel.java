package com.sg.business.pm2.home;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.pm2.nls.Messages;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;

public class TodolistLabel extends LabelProvider {

	private static final int COMPLETE_TASK = 1;
	private static final int START_WORK = 2;
	private static final int FINISH_WORK = 3;
	private static final int ASSIGN_WORK = 4;
	private static final int NONE = 0;
	private String userId;
	private Locale locale;

	public TodolistLabel() {
		init();
	}

	private void init() {
		CurrentAccountContext context = new CurrentAccountContext();
		userId = context.getAccountInfo().getConsignerId();
		locale = RWT.getLocale();
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Work) {
			Work work = (Work) element;
			return getRuntimeWorkHTMLLabel(work);
		}
		return ""; //$NON-NLS-1$
	}

	private String getRuntimeWorkHTMLLabel(Work work) {

		UserTask currentTask = null;
		if (work.isExecuteWorkflowActivateAndAvailable()) {// 如果有流程
			List<UserTask> result = work.getReservedUserTasks(userId);
			result.addAll(work.getInprogressUserTasks(userId));
			if (result.size() > 0) {
				currentTask = result.get(0);
			}
		}
		
		int code = getOperationCode(work, currentTask);

		// 标记
		String selectbarUrl = null;
		Date _planStart = work.getPlanStart();
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

		sb.append("<span style='font-family:微软雅黑;font-size:9pt;float:right;'>"); //$NON-NLS-1$
		if (currentTask != null) {
			sb.append(currentTask.getTaskName());
		}else if(code == ASSIGN_WORK){
			sb.append(Messages.get(locale).AssignWork);
		}
		sb.append("</span>");

		sb.append("<span style='font-family:微软雅黑;font-size:9pt;padding-left:6px;'>"); //$NON-NLS-1$

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

		sb.append("<small style='color:#909090;padding-left:6px;'>"); //$NON-NLS-1$
		String planStart = "?"; //$NON-NLS-1$
		if (_planStart != null) {
			planStart = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
					_planStart);
		}

		sb.append(""); //$NON-NLS-1$
		sb.append(planStart);
		sb.append("~"); //$NON-NLS-1$

		String planFinish = "?"; //$NON-NLS-1$
		if (_planFinish != null) {
			planFinish = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
					_planFinish);
		}
		sb.append(planFinish);
		sb.append("  "); //$NON-NLS-1$

		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}


	private int getOperationCode(Work work, UserTask currentTask) {
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


}
