package com.sg.business.visualization.labelprovider.project;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;

public class SchedualLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		List<Work> works = project.getMileStoneWorks();
		// 显示图形化的进度栏
		StringBuffer sb = new StringBuffer();
		sb.append("<span>");
		sb.append(getSchedualGraphic(works, project));
		sb.append("</span>");

		// 显示计划和实际的进度日期
		sb.append("<br>");
		sb.append("<small>");
		sb.append(getSchedualText(works, project));
		sb.append("</small>");
		return sb.toString();
	}

	private String getSchedualText(List<Work> milestones, Project project) {
		Date ps = project.getPlanStart();
		Date pf = project.getPlanFinish();
		Date as = project.getActualStart();
		Date af = project.getActualFinish();

		String start = as == null ? (ps == null ? "" : String.format("%tF%n",
				ps)) : String.format("%tF%n", as);
		String finish = af == null ? (pf == null ? "" : String.format("%tF%n",
				pf)) : String.format("%tF%n", af);

		StringBuffer sb = new StringBuffer();
		sb.append(start);
		sb.append("~");
		sb.append(finish);

		// 如果项目已经完成，显示完成旗标
		String projectLc = project.getLifecycleStatus();
//		String state = null;
		if (!ILifecycle.STATUS_FINIHED_VALUE.equals(projectLc)) {
			boolean isDelayed = project.isDelay();
			if (isDelayed) {
				sb.append("<span style='color="+Utils.COLOR_RED[8]+"'>");
				sb.append("超期");
				sb.append("</span>");
//				state = FileUtil.getImageURL(
//						BusinessResource.IMAGE_BALL_RED_1_16,
//						BusinessResource.PLUGIN_ID);
			} else {
				boolean maybeDelay = project.maybeDelay();
				if (maybeDelay) {
					sb.append("<span style='color="+Utils.COLOR_YELLOW[8]+"'>");
					sb.append("可能超期");
					sb.append("</span>");
//					state = FileUtil.getImageURL(
//							BusinessResource.IMAGE_BALL_YELLOW_1_16,
//							BusinessResource.PLUGIN_ID);
				}
			}
		}
//		if (state != null) {
//			sb.append("<img src='"
//					+ state
//					+ "' style='margin-top:0;margin-left:2' width='12' height='12' />");
//		}

		return sb.toString();
	}

	private String getSchedualGraphic(List<Work> milestones, Project project) {
		StringBuffer sb = new StringBuffer();
		// 如果项目已经完成
		String projectLc = project.getLifecycleStatus();
		String processIcon = getProcessIcon(projectLc);

		if (ILifecycle.STATUS_FINIHED_VALUE.equals(projectLc)) {
			String bar = getColorBar(8, null, "96%", "#ececec", processIcon,
					"right");
			sb.append(bar);

		} else {
			// 显示进度摘要的图形信息
			int count = milestones.size();
			int barCount = count > 1 ? count : 1;
			double percent = 1d / barCount;

			// 项目是否已经延期
			boolean isDelayed = project.isDelay();
			boolean maybeDelay = project.maybeDelay();

			// 获得当前的阶段
			Work latest = null;
			String location;
			for (int i = 0; i < milestones.size(); i++) {
				Work work = milestones.get(i);
				String lc = work.getLifecycleStatus();
				if (ILifecycle.STATUS_FINIHED_VALUE.equals(lc)) {
					continue;
				}
				latest = work;
			}

			if (latest != null
					&& ILifecycle.STATUS_WIP_VALUE.equals(latest
							.getLifecycleStatus())) {
				location = "right";
			} else {
				location = "left";
			}

			for (int i = 0; i < barCount; i++) {
				String colorCode;
				int colorIndex;
				String icon;
				if (count != 0) {
					Work work = milestones.get(i);
					String lc = work.getLifecycleStatus();
					// 如果工作完成
					if (ILifecycle.STATUS_FINIHED_VALUE.equals(lc)) {
						if (isDelayed) {
							colorCode = "red";
						} else if (maybeDelay) {
							colorCode = "yellow";
						} else {
							colorCode = "green";
						}

						colorIndex = i;
					} else {
						// 如果工作没有开始，显示蓝色
						colorCode = "blue";
						colorIndex = i;
					}
					if (latest == work) {
						icon = processIcon;
					} else {
						icon = null;
					}
				} else {
					// 没有里程碑
					if (isDelayed) {
						colorCode = "red";
					} else if (maybeDelay) {
						colorCode = "yellow";
					} else {
						colorCode = "green";
					}
					colorIndex = i;
					icon = processIcon;
				}

				if (i == barCount - 1) {
					percent = 1 - percent * (i) - 0.04;
				}
				String bar = getColorBar(colorIndex + 3, colorCode,
						new DecimalFormat("#.00").format(100 * percent) + "%",
						null, icon, location);
				sb.append(bar);
			}
		}

		return sb.toString();
	}

	private String getColorBar(int i, String style, String percent,
			String color, String currentStageIcon, String location) {
		if (color == null) {
			if (style.equals("red")) {
				if (i < Utils.COLOR_RED.length) {
					color = Utils.COLOR_RED[i];
				} else {
					color = Utils.COLOR_RED[i - 1];
				}
			} else if (style.equals("yellow")) {
				if (i < Utils.COLOR_YELLOW.length) {
					color = Utils.COLOR_YELLOW[i];
				} else {
					color = Utils.COLOR_YELLOW[i - 1];
				}
			} else if (style.equals("green")) {
				if (i < Utils.COLOR_GREEN.length) {
					color = Utils.COLOR_GREEN[i];
				} else {
					color = Utils.COLOR_GREEN[i - 1];
				}
			} else {
				if (i < Utils.COLOR_BLUE.length) {
					color = Utils.COLOR_BLUE[i];
				} else {
					color = Utils.COLOR_BLUE[i - 1];
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='display:block;width:" + percent
				+ "; height:20px;  float:left;background:" + color + ";'>");

		if (currentStageIcon != null) {
			// 显示状态图标<div align="center"><span>abc</span></div>
			String imageUrl = "<div align='"
					+ location
					+ "'><img src='"
					+ currentStageIcon
					+ "' style='margin-top:2;margin-right:2' width='16' height='16' /></div>";
			sb.append(imageUrl);
		}
		sb.append("</span>");
		return sb.toString();
	}

	private String getProcessIcon(String lc) {
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
	}

}
