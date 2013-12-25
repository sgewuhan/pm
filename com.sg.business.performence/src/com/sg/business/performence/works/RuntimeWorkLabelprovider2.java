package com.sg.business.performence.works;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.BasicBSONList;
import org.eclipse.swt.graphics.Image;
import org.jbpm.task.Status;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.performence.nls.Messages;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.part.CurrentAccountContext;

public class RuntimeWorkLabelprovider2 extends ConfiguratorColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}
	
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
		return ""; //$NON-NLS-1$
	}

	private String getProjectHTMLLabel(Work work) {
		Project project = work.getProject();
		if (project == null) {
			return ""; //$NON-NLS-1$
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$

		// **********************************************************************************
		// 右侧第一行，标记
		CurrentAccountContext context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getConsignerId();
		boolean userMarked = work.isMarked(userId);
		if (userMarked) {
			String selectbar = "<img src='" //$NON-NLS-1$
					+ getSelectorURL(work, ImageResource.BLUE_BULLETIN)
					+ "' style='float:right;padding:0px;margin:0px' width='6' height='40' />"; //$NON-NLS-1$
			sb.append(selectbar);
		} else {
			String selectbar = "<img src='" //$NON-NLS-1$
					+ getSelectorURL(work, ImageResource.WHITE_BULLETIN)
					+ "' style='float:right;padding:0px;margin:0px' width='6' height='40' />"; //$NON-NLS-1$
			sb.append(selectbar);

		}

		// **********************************************************************************
		// 右侧第一行，需要指派以及负责人图标
		// 如果需要再指派，添加再指派图标
		String assignerId = work.getAssignerId();
		if (userId.equals(assignerId)) {
			String imageUrl = "<img src='" //$NON-NLS-1$
					+ FileUtil.getImageURL(
							BusinessResource.IMAGE_REASSIGNMENT_24,
							BusinessResource.PLUGIN_ID,
							BusinessResource.IMAGE_FOLDER)
					+ "' style='float:right;padding-right:4px' width='24' height='24' />"; //$NON-NLS-1$
			sb.append(imageUrl);
		}

		User charger = project.getCharger();
		if (charger != null) {
			sb.append("<span style='float:right;padding-right:4px'>"); //$NON-NLS-1$
			sb.append(charger);
			sb.append("</span>"); //$NON-NLS-1$
		}

		// **********************************************************************************
		// 左侧第一行，工作图标
		String imageUrl = "<img src='" //$NON-NLS-1$
				+ FileUtil.getImageURL(BusinessResource.IMAGE_PROJECT_32,
						BusinessResource.PLUGIN_ID,
						BusinessResource.IMAGE_FOLDER)
				+ "' style='float:left;padding:6px' width='24' height='24' />"; //$NON-NLS-1$
		sb.append(imageUrl);

		String desc = project.getDesc();
		desc = Utils.getPlainText(desc);
		sb.append(Messages.get().RuntimeWorkLabelprovider2_1 + desc + "</b>"); //$NON-NLS-2$

		String projectNumber = project.getProjectNumber();
		sb.append(" [" + projectNumber + "]"); //$NON-NLS-1$ //$NON-NLS-2$

		Date _planStart = project.getPlanStart();
		String planStart = ""; //$NON-NLS-1$
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_MONTH_DATE_COMPACT_SASH);

		if (_planStart != null) {
			planStart = sdf.format(_planStart);
		}

		// String selectbar = "<img src='"
		// + getSelectorURL(work,ImageResource.WHITE_BULLETIN)
		// +
		// "' style='float:left;padding:px;margin:0px' width='16' height='8' />";
		// sb.append(selectbar);

		sb.append(" "); //$NON-NLS-1$
		sb.append(planStart);
		sb.append("~"); //$NON-NLS-1$

		Date _planFinish = project.getPlanFinish();
		String planFinish = ""; //$NON-NLS-1$
		if (_planFinish != null) {
			planFinish = sdf.format(_planFinish);
		}
		sb.append(planFinish);
		sb.append("  "); //$NON-NLS-1$

		sb.append("<br/><small>"); //$NON-NLS-1$
		String[] workOrders = project.getWorkOrders();
		for (int i = 0; i < workOrders.length; i++) {
			if (i == 0) {
				sb.append("<b>WON</b>:"); //$NON-NLS-1$
			} else {
				sb.append(" "); //$NON-NLS-1$
			}
			sb.append(workOrders[i]);
		}

		sb.append("</small></span>"); //$NON-NLS-1$
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
			sb.append("<span style='color:#bbbbbb;FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$
		} else {
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$
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
		String selectbar = "<img src='" //$NON-NLS-1$
				+ selectbarUrl
				+ "' style='float:right;padding:0px;margin:0px' width='6' height='40' />"; //$NON-NLS-1$
		sb.append(selectbar);
		// -----------------------------------------------------------------------------------------

		
		if (charger != null) {
			sb.append("<span style='float:right;padding-right:4px'>"); //$NON-NLS-1$
			sb.append(charger);
			sb.append("</span>"); //$NON-NLS-1$
		}

		String assignerId = work.getAssignerId();
		if (userId.equals(assignerId)) {
			String imageUrl = "<img src='" //$NON-NLS-1$
					+ FileUtil.getImageURL(
							BusinessResource.IMAGE_REASSIGNMENT_16,
							BusinessResource.PLUGIN_ID,
							BusinessResource.IMAGE_FOLDER)
							+ "' style='float:right' width='16' height='16' />"; //$NON-NLS-1$
			sb.append(imageUrl);
		}

		IProcessControl pc = (IProcessControl) work
				.getAdapter(IProcessControl.class);

		String imageUrl = "<img src='" + getHeaderImageURL(work, pc) //$NON-NLS-1$
				+ "' style='float:left;padding:6px' width='16' height='16' />"; //$NON-NLS-1$
		sb.append(imageUrl);

		// 工作desc
		String workDesc = work.getDesc();
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);
		
		Project project = work.getProject();
		if(project!=null){
			sb.append(" "); //$NON-NLS-1$
			sb.append(Messages.get().RuntimeWorkLabelprovider2_0);
			sb.append(project);
		}
		
		// 有关时间
		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$

		sb.append(getWorkflowSummaryInformation(work,userId));

		String planStart = ""; //$NON-NLS-1$
		if (_planStart != null) {
			planStart = sdf.format(_planStart);
		}

		// String selectbar = "<img src='"
		// + getSelectorURL(work,ImageResource.WHITE_BULLETIN)
		// +
		// "' style='float:left;padding:px;margin:0px' width='16' height='8' />";
		// sb.append(selectbar);

		sb.append(""); //$NON-NLS-1$
		sb.append("<b>P</b>:"); //$NON-NLS-1$
		sb.append(planStart);
		sb.append("~"); //$NON-NLS-1$

		String planFinish = ""; //$NON-NLS-1$
		if (_planFinish != null) {
			planFinish = sdf.format(_planFinish);
		}
		sb.append(planFinish);
		sb.append("  "); //$NON-NLS-1$

		String actualStart = ""; //$NON-NLS-1$
		if (_actualStart != null) {
			actualStart = sdf.format(_actualStart);
		}
		sb.append("<b>A</b>:"); //$NON-NLS-1$
		sb.append(actualStart);
		sb.append("~"); //$NON-NLS-1$

		String actualFinish = ""; //$NON-NLS-1$
		if (_actualFinish != null) {
			actualFinish = sdf.format(_actualFinish);
		}
		sb.append(actualFinish);
		sb.append("  "); //$NON-NLS-1$

		sb.append("<b>D</b>:"); //$NON-NLS-1$

		Integer actualDuration = work.getActualDuration();
		if (actualDuration == null) {
			actualDuration = 0;
		}
		sb.append(actualDuration);
		sb.append("/"); //$NON-NLS-1$

		Integer planDuration = work.getPlanDuration();
		if (planDuration == null) {
			planDuration = 0;
		}
		sb.append(planDuration);

		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}

	private String getWorkflowSummaryInformation(Work work,String userId) {
		UserTask userTask = work.getLastDisplayTask(userId);
		if(userTask == null){
			return ""; //$NON-NLS-1$
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='float:right;padding-right:4px'>"); //$NON-NLS-1$
		// 根据状态取流程图标
		Object taskstatus = userTask.getValue(UserTask.F_STATUS);
		sb.append("<img src='" + getTaskStatusImageURL(taskstatus) //$NON-NLS-1$
				+ "' style='float:left;padding:6px' width='10' height='10' />"); //$NON-NLS-1$

		Object taskname = userTask.getValue(UserTask.F_DESC);
		sb.append(" "); //$NON-NLS-1$
		sb.append(taskname);
		sb.append(" "); //$NON-NLS-1$
		Object owner = userTask.getValue(UserTask.F_ACTUALOWNER);
		if (owner instanceof String) {
			User ownerUser = UserToolkit.getUserById((String) owner);
			sb.append(ownerUser);
		}
		sb.append("</span>"); //$NON-NLS-1$
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
