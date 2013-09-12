package com.sg.business.work.labelprovider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.BasicBSONList;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
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

		boolean isOwner = isOwner(work,userId);
		if(!isOwner){
			sb.append("<span style='color:#bbbbbb'>");
		}

		User charger = work.getCharger();
		if (charger != null) {
			sb.append("<span style='float:right;padding-right:4px'>");
			sb.append(charger);
			sb.append("</span>");
		}

		String imageUrl = "<img src='" + getHeaderImageURL(work)
				+ "' style='float:left;padding:6px' width='32' height='32' />";
		sb.append(imageUrl);

		// 工作desc
		String workDesc = work.getDesc();
		workDesc = Utils.getPlainText(workDesc);
		sb.append("<b>");
		sb.append(workDesc);
		sb.append("</b> ");

		sb.append(" ");
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

		sb.append("<br/>");

		DBObject wfinfo = work.getCurrentWorkflowTaskData(Work.F_WF_EXECUTE,userId);
		if (wfinfo != null) {
			sb.append("流程:");
			Object taskname = wfinfo.get(Work.F_WF_TASK_NAME);
			sb.append(taskname);
			Object taskstatus = wfinfo.get(Work.F_WF_TASK_STATUS);
			sb.append(" ");
			sb.append(taskstatus);
			sb.append(" ");
			Object owner = wfinfo.get(Work.F_WF_TASK_ACTUALOWNER);
			if (owner instanceof String) {
				User ownerUser = UserToolkit.getUserById((String) owner);
				sb.append(ownerUser);
			}
		}

		sb.append("</small>");
		
		if(!isOwner){
			sb.append("</span>");
		}
		return sb.toString();
	}

	private boolean isOwner(Work work, String userId) {

		String chargerId = work.getChargerId();
		if (userId.equals(chargerId)) {
			return true;
		}

		String assignerId = work.getAssignerId();
		if(userId.equals(assignerId)){
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
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_CANCEL_32,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_FINISH_32,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_ONREADY_VALUE.equals(lc)) {
			if(work.isWorkflowActivate(Work.F_WF_EXECUTE)){
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK2_READY_32,
						BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
			}else{
				return FileUtil.getImageURL(
						BusinessResource.IMAGE_WORK_READY_32,
						BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
			}
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(lc)) {
			if(work.isWorkflowActivate(Work.F_WF_EXECUTE)){
				return FileUtil.getImageURL(BusinessResource.IMAGE_WORK2_WIP_32,
						BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
			}else{
				return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_WIP_32,
						BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
			}
		} else if (ILifecycle.STATUS_PAUSED_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_PAUSE_32,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		} else if (ILifecycle.STATUS_NONE_VALUE.equals(lc)) {
			return FileUtil.getImageURL(BusinessResource.IMAGE_WORK_32,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
		}
		return null;
	}

}
