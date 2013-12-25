package com.sg.business.work.labelprovider;

import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.jbpm.task.Status;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.business.work.nls.Messages;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class UserTaskLabelProvider extends ConfiguratorColumnLabelProvider {

	private long min;

	public UserTaskLabelProvider() {
		super();
		initAlertDelay();
	}

	public UserTaskLabelProvider(ColumnConfigurator configurator) {
		super(configurator);
		initAlertDelay();
	}

	private void initAlertDelay() {
		String _min = (String) Setting
				.getSystemSetting(IModelConstants.S_S_TASK_DELAY);
		try {
			min = Long.parseLong(_min);
		} catch (Exception e) {
			min = 3000;
		}
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$
		Work work = ((UserTask) element).getWork();

		// 显示流程任务的内容
		appendUserTaskInfo(work,(UserTask) element, sb);

		sb.append("<br/>"); //$NON-NLS-1$

		// 显示工作的内容
		sb.append("<small>"); //$NON-NLS-1$
		appendWorkInfo(work, sb);
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}

	private void appendUserTaskInfo(Work work,UserTask userTask, StringBuffer sb) {
		Date createOn = userTask.getCreatedOn();
		sb.append("<span style='float:right;padding-right:14px'>"); //$NON-NLS-1$
		sb.append(String.format("%1$tm/%1$te %1$tH:%1$tM", createOn)); //$NON-NLS-1$
		sb.append("</span>"); //$NON-NLS-1$

		appendMark(work, userTask, sb);

		String processName = userTask.getProcessName();
		sb.append(processName);
		sb.append(" "); //$NON-NLS-1$
		String desc = userTask.getTaskName();
		sb.append(desc);
		sb.append(" "); //$NON-NLS-1$

	}

	private void appendWorkInfo(Work  work, StringBuffer sb) {
		User charger = work.getCharger();
		if (charger != null) {
			sb.append("<span style='float:right;padding-right:14px'>"); //$NON-NLS-1$
			sb.append(charger);
			sb.append("</span>"); //$NON-NLS-1$
		}

		if (work.isProjectWork()) {
			Project project = work.getProject();
			sb.append(Messages.get().UserTaskLabelProvider_0);
			String projectDesc = Utils.getPlainText(project.getDesc());
			sb.append(projectDesc);
			sb.append(" - "); //$NON-NLS-1$
		}
		String workDesc = Utils.getPlainText(work.getDesc());
		sb.append(workDesc);
		sb.append(" "); //$NON-NLS-1$
		Date _planStart = work.getPlanStart();
		Date _planFinish = work.getPlanFinish();

		sb.append(String.format("%1$tm/%1$te", _planStart)); //$NON-NLS-1$
		sb.append(" ~ "); //$NON-NLS-1$
		sb.append(String.format("%1$tm/%1$te", _planFinish)); //$NON-NLS-1$
	}

	private void appendMark(Work work,UserTask userTask, StringBuffer sb) {
		String status = userTask.getStatus();
		String lc = work.getLifecycleStatus();
		if(!Work.STATUS_WIP_VALUE.equals(lc)){
			sb.append("<span style='border-style:none;float:right'>"); //$NON-NLS-1$
			sb.append(work.getLifecycleStatusText());
			sb.append(" </span>"); //$NON-NLS-1$
		}else{
			if(status.equals(Status.Reserved.name())){
				Date createOn = userTask.get_cdate();
				Date now = new Date();
				sb.append("<a href=\"" //$NON-NLS-1$
						+ userTask.get_id().toString()+"@"+"start" //$NON-NLS-1$ //$NON-NLS-2$
						+ "\" target=\"_rwt\">"); //$NON-NLS-1$
				sb.append("<img src='"); //$NON-NLS-1$
				if ((now.getTime() - createOn.getTime()) > min*60*1000) {
					// 显示延迟标记
					sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_ALERT_24,
							BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
				}else{
					sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_START_24,
							BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));				
				}
				sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='16' height='16' />"); //$NON-NLS-1$
				sb.append("</a>"); //$NON-NLS-1$
				
			}else{
				sb.append("<a href=\"" //$NON-NLS-1$
						+ userTask.get_id().toString()+"@"+"complete" //$NON-NLS-1$ //$NON-NLS-2$
						+ "\" target=\"_rwt\">"); //$NON-NLS-1$
				sb.append("<img src='"); //$NON-NLS-1$
				sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_FINISHED_16,
						BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));				
				sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='16' height='16' />"); //$NON-NLS-1$
				sb.append("</a>"); //$NON-NLS-1$
				
			}
		}
		
		
	}
}
