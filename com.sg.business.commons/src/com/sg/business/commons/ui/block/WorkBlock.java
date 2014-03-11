package com.sg.business.commons.ui.block;

import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.model.dataset.work.ProcessingNavigatorItemSet;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.Block;

public class WorkBlock extends Block {

	private static final String BLUE = "#33b5e5";
	private static final String ORANGE = "#ffbb33";
	private static final String RED = "#ff4444";
	private static final int ITEMCOUNT = 7;
	private static final int MARGIN = 4;
	public static final int BLOCKSIZE = 300;
	private static final String PERSPECTIVE = "perspective.work";
	private Label title;
	private Label content;
	private ProcessingNavigatorItemSet itemSet;

	public WorkBlock(Composite parent) {
		super(parent);
	}
	
	@Override
	protected void go() {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			workbench.showPerspective(PERSPECTIVE, window);
		} catch (WorkbenchException e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	protected void createContent(Composite parent) {
		itemSet = new ProcessingNavigatorItemSet();

		
		parent.setLayout(new FormLayout());

		title = new Label(parent, SWT.NONE);
		title.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		content = new Label(parent, SWT.NONE);
		content.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		FormData fd = new FormData();
		title.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = 60;

		fd = new FormData();
		content.setLayoutData(fd);
		fd.top = new FormAttachment(title, MARGIN);
		fd.left = new FormAttachment(0, MARGIN * 2);
		fd.right = new FormAttachment(100, -MARGIN);
		fd.bottom = new FormAttachment(100, -MARGIN);

		doRefresh();
	}

	private String getWorkContent(Work work, boolean delayFinish,
			boolean delayFinishEst) {
		int width = 290;
		Project project = work.getProject();
		String projectName = project == null ? "" : Utils.getPlainText(project
				.getDesc());

		String taskName = "";
		String userId = context.getConsignerId();
		List<UserTask> tasksReserved = work.getReservedUserTasks(userId);
		List<UserTask> tasksInProgress = work.getInprogressUserTasks(userId);
		tasksReserved.addAll(tasksInProgress);
		if (tasksReserved.size() > 0) {
			taskName = tasksReserved.get(0).getTaskName();
			taskName = Utils.getPlainText(taskName);
		}

		String workName = work.getDesc();
		workName = Utils.getPlainText(workName);

		if (delayFinish || delayFinishEst) {
			width -= 50;
		}

		int count = 1;
		if (taskName.length() != 0) {
			count++;
		}
		if (projectName.length() != 0) {
			count++;
		}

		taskName = Utils.shortenText(taskName, content, width / count);
		projectName = Utils.shortenText(projectName, content, width / count);
		workName = Utils.shortenText(workName, content, width / count);

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='color:#4a4a4a'>");
		sb.append(workName);
		sb.append(" </span>");
		sb.append("<span style='color:#909090'>");
		sb.append(taskName + " ");
		sb.append(projectName + " ");
		sb.append("</span>");
		if (delayFinish) {
			sb.append("<span style='color:" + RED + "'>已超期</span>");
		} else if (delayFinishEst) {
			sb.append("<span style='color:" + ORANGE + "'>超期预警</span>");
		}
		return sb.toString();
	}

	private String getTitle(int reserved, int overSchedule, int overScheduleEst) {
		String color;
		if (overSchedule > 0) {
			color = RED;
		} else if (overScheduleEst > 0) {
			color = ORANGE;
		} else {
			color = BLUE;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='float:left;color:#909090;");
		sb.append("font-family:微软雅黑;font-size:14pt;font-style:italic;");
		sb.append("display:-moz-inline-box; display:inline-block; ");
		sb.append("width:" + BLOCKSIZE + "px;");
		sb.append("height:60px;" + "margin:" + MARGIN + "px;");
		sb.append("'>");
		sb.append("<span style='display:block;width:5px; height:44px;  "
				+ "float:left;background:" + color + ";" + "margin:" + MARGIN
				+ "px;'>");
		sb.append("</span>");
		if (reserved > 0) {
			sb.append("您需要处理的工作" + reserved + "件");
			if (overSchedule + overScheduleEst > 0) {
				sb.append("<br/>");
			}
			if (overSchedule > 0) {
				sb.append(" 超期" + overSchedule + "件");
			}
			if (overScheduleEst > 0) {
				sb.append(" 超期预警" + overScheduleEst + "件");
			}
		} else {
			sb.append("您没有需要处理的工作");
		}
		sb.append("</span>");
		return sb.toString();
	}

	@Override
	protected Object doGetData() {
		DataSet dataSet = itemSet.getDataSet();
		return dataSet.getDataItems();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doDisplayData(Object data) {
		List<PrimaryObject> items = (List<PrimaryObject>) data;
		int overSchedule = 0;
		int overScheduleEst = 0;
		StringBuffer contentBuffer = new StringBuffer();

		for (int i = 0; i < items.size(); i++) {
			Work work = (Work) items.get(i);
			boolean delayFinish = work.isDelayFinish();
			boolean delayFinishEst = work.isDelayFinishEst();
			if (delayFinish) {
				overSchedule++;
			} else if (delayFinishEst) {
				overScheduleEst++;
			}
			if (i < ITEMCOUNT) {
				if (i != 0) {
					contentBuffer.append("<br/>");
				}
				contentBuffer.append(getWorkContent(work, delayFinish,
						delayFinishEst));
			} else if (i == ITEMCOUNT) {
				contentBuffer
						.append("<br/><span style='color:#4a4a4a'>...</span>");
			}
		}
		int reserved = items.size();
		title.setText(getTitle(reserved, overSchedule, overScheduleEst));
		content.setText(contentBuffer.toString());
	}
	
}
