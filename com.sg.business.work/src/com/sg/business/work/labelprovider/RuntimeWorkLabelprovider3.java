package com.sg.business.work.labelprovider;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.business.work.nls.Messages;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class RuntimeWorkLabelprovider3 extends ConfiguratorColumnLabelProvider {

	private static final int COMPLETE_TASK = 1;
	private static final int START_WORK = 2;
	private static final int FINISH_WORK = 3;
	private static final int ASSIGN_WORK = 4;
	private static final int NONE = 0;
	private String userId;
	private Locale locale;

	public RuntimeWorkLabelprovider3() {
		super();
		init();
	}

	public RuntimeWorkLabelprovider3(ColumnConfigurator configurator) {
		super(configurator);
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
		if (work.isExecuteWorkflowActivateAndAvailable()) {// ���������
			List<UserTask> result = work.getReservedUserTasks(userId);
			result.addAll(work.getInprogressUserTasks(userId));
			if (result.size() > 0) {
				currentTask = result.get(0);
			}
		}
		
		int code = getOperationCode(work, currentTask);

		// ���
		String selectbarUrl = null;
		Date _planStart = work.getPlanStart();
		Date _planFinish = work.getPlanFinish();
		int remindBefore = work.getRemindBefore();

		// �����жϵ�ǰʱ���Ƿ����ڼƻ����ʱ�䣬����ǣ���ʾΪ���ڱ�ǩ
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
					// Ȼ���жϵ�ǰʱ���Ƿ�ﵽ����ʱ��
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

		sb.append("<span style='font-family:΢���ź�;font-size:9pt;float:right;'>"); //$NON-NLS-1$
		if (currentTask != null) {
			sb.append(currentTask.getTaskName());
		}else if(code == ASSIGN_WORK){
			sb.append(Messages.get(locale).AssignWork);
		}
		sb.append("</span>");

		sb.append("<span style='font-family:΢���ź�;font-size:9pt'>"); //$NON-NLS-1$
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

		// ����desc
		String workDesc = work.getDesc();
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);

		// ����йص���Ŀ��Ϣ
		Project project = work.getProject();
		if (project != null) {
			String projectDesc = project.getDesc();
			projectDesc = Utils.getPlainText(projectDesc);
			sb.append(" ");
			sb.append("<span style='color:#909090;'>");
			sb.append(projectDesc);
			sb.append("</span>");
		}

		// �й�ʱ��
		sb.append("<br/>"); //$NON-NLS-1$

//		// ��ʾ���ò���
//		sb.append("<span style='float:right;color:#1d4183;'>");
//		sb.append(getOperation(work,currentTask,code));
//		sb.append("</span>");

		sb.append("<small style='color:#909090;'>"); //$NON-NLS-1$
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

//	private Object getOperation(Work work, UserTask currentTask, int code) {
//		StringBuffer sb = new StringBuffer();
//		
//		switch (code) {
//		case ASSIGN_WORK:
//			sb.append("<img src='"); //$NON-NLS-1$
//			sb.append(FileUtil.getImageURL(
//					BusinessResource.IMAGE_REASSIGNMENT_16X12,
//					BusinessResource.PLUGIN_ID,
//					BusinessResource.IMAGE_FOLDER));
//			sb.append("' width='16' height='12'  />"); //$NON-NLS-1$
//			sb.append(Messages.get(locale).AssignWork);
//			break;
//		case START_WORK:
//			sb.append("<img src='"); //$NON-NLS-1$
//			sb.append(FileUtil.getImageURL(
//					BusinessResource.IMAGE_START_16X12,
//					BusinessResource.PLUGIN_ID,
//					BusinessResource.IMAGE_FOLDER));
//			sb.append("' width='16' height='12'  />"); //$NON-NLS-1$
//			sb.append(Messages.get(locale).StartWork);
//			break;
//		case FINISH_WORK:
//			sb.append("<img src='"); //$NON-NLS-1$
//			sb.append(FileUtil.getImageURL(
//					BusinessResource.IMAGE_FINISH_16X12,
//					BusinessResource.PLUGIN_ID,
//					BusinessResource.IMAGE_FOLDER));
//			sb.append("' width='16' height='12'  />"); //$NON-NLS-1$
//			sb.append(Messages.get(locale).FinishWork);
//			break;
//		case COMPLETE_TASK:
//			sb.append("<img src='"); //$NON-NLS-1$
//			sb.append(FileUtil.getImageURL(
//					BusinessResource.IMAGE_FLOW_16X12,
//					BusinessResource.PLUGIN_ID,
//					BusinessResource.IMAGE_FOLDER));
//			sb.append("' width='16' height='12' />"); //$NON-NLS-1$
//			sb.append(Messages.get(locale).FinishTask);
//			break;
//		default:
//			break;
//		}
//		return sb.toString();
//	}

	private int getOperationCode(Work work, UserTask currentTask) {
		// �����׼���еĹ�������ʾΪ��ʼ����
		// ����ǽ����еĹ������ж��Ƿ��ڵ�ǰ������������
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
