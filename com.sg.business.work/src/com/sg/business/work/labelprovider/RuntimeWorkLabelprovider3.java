package com.sg.business.work.labelprovider;

import java.util.Date;

import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.part.CurrentAccountContext;

public class RuntimeWorkLabelprovider3 extends ConfiguratorColumnLabelProvider {

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
		CurrentAccountContext context = new CurrentAccountContext();
		String userId = context.getAccountInfo().getConsignerId();

		StringBuffer sb = new StringBuffer();

		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$

		// ---------------------------------------------------------------------------

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
			Date now = new Date();
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
				+ "' style='float:left;padding:0px;margin:0px' width='4' height='40' />"; //$NON-NLS-1$
		sb.append(selectbar);
		// -----------------------------------------------------------------------------------------

		// String assignerId = work.getAssignerId();
		// if (userId.equals(assignerId)) {
		//			String imageUrl = "<img src='" //$NON-NLS-1$
		// + FileUtil.getImageURL(
		// BusinessResource.IMAGE_REASSIGNMENT_16,
		// BusinessResource.PLUGIN_ID,
		// BusinessResource.IMAGE_FOLDER)
		//					+ "' style='float:right' width='16' height='16' />"; //$NON-NLS-1$
		// sb.append(imageUrl);
		// }

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

		// 显示可用操作
		sb.append("<small>"); //$NON-NLS-1$
		
		sb.append("<span style='float:right;color:#1d4183'>");
		sb.append("开始");
		sb.append("</span>");

		String planStart = ""; //$NON-NLS-1$
		if (_planStart != null) {
			planStart = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
					_planStart);
		}

		sb.append(""); //$NON-NLS-1$
		sb.append(planStart);
		sb.append("~"); //$NON-NLS-1$

		String planFinish = ""; //$NON-NLS-1$
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

	private String getSelectorURL(Work work, String style) {
		return FileUtil.getImageURL(style, Widgets.PLUGIN_ID,
				BusinessResource.IMAGE_FOLDER);
	}

	// private String getHeaderImageURL(Work work, IProcessControl pc) {
	// if (work.isProjectWBSRoot()) {
	// return FileUtil.getImageURL(BusinessResource.IMAGE_PROJECT_32,
	// BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	// }
	//
	// String lc = work.getLifecycleStatus();
	// if (ILifecycle.STATUS_CANCELED_VALUE.equals(lc)) {
	// return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_CANCEL_16,
	// BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	// } else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lc)) {
	// return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_FINISH_16,
	// BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	// } else if (ILifecycle.STATUS_ONREADY_VALUE.equals(lc)) {
	// if (pc.isWorkflowActivate(Work.F_WF_EXECUTE)) {
	// return FileUtil.getImageURL(
	// BusinessResource.IMAGE_WORK2_READY_16,
	// BusinessResource.PLUGIN_ID,
	// BusinessResource.IMAGE_FOLDER);
	// } else {
	// return FileUtil.getImageURL(
	// BusinessResource.IMAGE_WORK2_READY_16,
	// BusinessResource.PLUGIN_ID,
	// BusinessResource.IMAGE_FOLDER);
	// }
	// } else if (ILifecycle.STATUS_WIP_VALUE.equals(lc)) {
	// if (pc.isWorkflowActivate(Work.F_WF_EXECUTE)) {
	// return FileUtil.getImageURL(
	// BusinessResource.IMAGE_WORK2_WIP_16,
	// BusinessResource.PLUGIN_ID,
	// BusinessResource.IMAGE_FOLDER);
	// } else {
	// return FileUtil.getImageURL(
	// BusinessResource.IMAGE_WORK2_WIP_16,
	// BusinessResource.PLUGIN_ID,
	// BusinessResource.IMAGE_FOLDER);
	// }
	// } else if (ILifecycle.STATUS_PAUSED_VALUE.equals(lc)) {
	// return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_PAUSE_16,
	// BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	// } else if (ILifecycle.STATUS_NONE_VALUE.equals(lc)) {
	// return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_READY_16,
	// BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	// }
	// return null;
	// // return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_16,
	// // BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
	// }

	@Override
	public String getToolTipText(Object element) {
		return element.toString();
	}

}
