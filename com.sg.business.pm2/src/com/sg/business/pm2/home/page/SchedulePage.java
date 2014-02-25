package com.sg.business.pm2.home.page;

import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.model.dataset.work.ProcessingNavigatorItemSet;
import com.sg.widgets.block.TabBlockPage;
import com.sg.widgets.part.CurrentAccountContext;

@SuppressWarnings("restriction")
public class SchedulePage extends TabBlockPage {

	private ProcessingNavigatorItemSet itemSet;

	private CurrentAccountContext context;

	private Label title;

	private Label content;
	
	private static final String BLUE = "#33b5e5";
	
	private static final String ORANGE = "#ffbb33";
	
	private static final String RED = "#ff4444";
	
	private static final int ITEMCOUNT = 7;
	
	private static final int MARGIN = 8;
	
	public static final int BLOCKSIZE = 300;


	public SchedulePage(Composite parent) {
		super(parent, SWT.NONE);
		init();
		
		setLayout(new FormLayout());
		Control title = createTitle(this);
		FormData fd = new FormData();
		title.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = 60;
		
		Control grsphic = createGraphicBlock(this);
		fd = new FormData();
		grsphic.setLayoutData(fd);
		fd.top = new FormAttachment(title);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(50);
		fd.bottom = new FormAttachment(100);
		
		Control text = createTextBlock(this);
		fd = new FormData();
		text.setLayoutData(fd);
		fd.top = new FormAttachment(title);
		fd.left = new FormAttachment(grsphic);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
		
		doRefresh();
	}

	
	private void init() {
		itemSet = new ProcessingNavigatorItemSet();
		context = new CurrentAccountContext();
	}
	
	private Control createTitle(Composite parent) {
		title = new Label(parent, SWT.NONE);
		title.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		title.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);

		return title;
	}

	private Control createTextBlock(Composite parent) {
		content = new Label(parent, SWT.NONE);
		content.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		content.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);

		return content;
	}

	private Control createGraphicBlock(Composite parent) {
		Composite control = new Composite(parent,SWT.NONE);
		control.setBackground(new Color(getDisplay(),255,255,0));
		return control;
		
	}
	
	@Override
	public boolean canRefresh() {
		return true;
	}
	
	@Override
	public void doRefresh() {
		List<PrimaryObject> items = itemSet.getDataSet().getDataItems();
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
		sb.append("<span style='");
		sb.append("width:500px;");
		sb.append("height:36px;" + "margin:" + MARGIN + "px;");
		sb.append("'>");
		sb.append("<div style='display:block;width:4px; height:36px;  "
				+ "float:left;background:" + color + ";'>");
		sb.append("</div>");
		sb.append("<div style='"
				+ "display:-moz-inline-box; "
				+ "display:inline-block;"
				+ "height:36px;"
				+ "color:#909090;"
				+ "font-family:微软雅黑;font-size:15pt;  "
				+ "margin:4px;"+ "'>");
		if (reserved > 0) {
			sb.append("您需要处理的工作" + reserved + "件");
			if (overSchedule + overScheduleEst > 0) {
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
		sb.append("</div>");
		sb.append("</span>");
		return sb.toString();
	}

}
