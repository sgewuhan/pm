package com.sg.business.model.etl;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.internal.widgets.MarkupValidator;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sg.business.model.CostAccount;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;

@SuppressWarnings("restriction")
public class ProjectETL implements IProjectETL{

	private Project project;

	private Date now;

	private boolean isDelayDefinited;

	private boolean isAdvanceDefinited;

	private boolean isDelayEstimated;

	private boolean isAdvanceEstimated;

	private double finishedDurationRatio;

	private double budget;

	private double allocatedInvestment;

	private double designatedInvestment;

	private double investment;

	private boolean isOverCostEstimated;

	private boolean isOverCostDefinited;

	private double salesRevenue;

	private double salesCost;

	private String projectDesc;

	private Date planFinish;

	private Date actualFinish;

	private Date planStart;

	private Date actualStart;

	private List<Work> milestones;

	private List<PrimaryObject> products;

	private String lifecycle;

	public ProjectETL(Project project) {
		this.project = project;
	}

	/**
	 * 获得分摊到工作令号的成本
	 * 
	 * @param colname
	 * @return
	 */
	private double extractInvestmentInternal(String colname) {
		// 获得工作令号
		String[] workOrders = project.getWorkOrders();
		double summary = 0d;
		if (workOrders.length != 0) {
			DBCollection col = DBActivator.getCollection(IModelConstants.DB,
					colname);
			DBObject matchCondition = new BasicDBObject();
			matchCondition.put(Project.F_WORK_ORDER,
					new BasicDBObject().append("$in", workOrders));
			DBObject match = new BasicDBObject().append("$match",
					matchCondition);
			DBObject groupCondition = new BasicDBObject();
			groupCondition.put("_id", "$" + Project.F_WORK_ORDER);// 按工作令号分组
			String[] costElements = CostAccount.getCostElemenArray();
			for (int i = 0; i < costElements.length; i++) {
				groupCondition.put(
						costElements[i],
						new BasicDBObject().append("$sum", "$"
								+ costElements[i]));
			}
			DBObject group = new BasicDBObject().append("$group",
					groupCondition);
			AggregationOutput agg = col.aggregate(match, group);
			Iterator<DBObject> iter = agg.results().iterator();
			while (iter.hasNext()) {
				DBObject data = iter.next();
				for (int i = 0; i < costElements.length; i++) {
					Number value = (Number) data.get(costElements[i]);
					summary += value.doubleValue();
				}
			}
		}
		return summary;
	}

	private String extractSchedualText() {
		String start = actualStart == null ? (planStart == null ? "" : String
				.format("%tF%n", planStart)) : String.format("%tF%n",
				actualStart);
		String finish = actualFinish == null ? (planFinish == null ? ""
				: String.format("%tF%n", planFinish)) : String.format("%tF%n",
				actualFinish);

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:8pt;margin-left:0;margin-top:2px; white-space:normal; display:block; width=1000px'>");

		sb.append(start);
		sb.append("~");
		sb.append(finish);

		// 如果项目已经完成，显示完成旗标
		// String state = null;
		if (!ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycle)) {
			if (isAdvanceDefinited) {
				sb.append("<span style='color:" + Utils.COLOR_GREEN[10] + "'>");
				sb.append("提前");
				sb.append("</span>");
			} else {
				if (isDelayDefinited) {
					sb.append("<span style='color:" + Utils.COLOR_RED[10]
							+ "'>");
					sb.append("超期");
					sb.append("</span>");
					// state = FileUtil.getImageURL(
					// BusinessResource.IMAGE_BALL_RED_1_16,
					// BusinessResource.PLUGIN_ID);
				} else {
					if (isDelayEstimated) {
						sb.append("<span style='color:"
								+ Utils.COLOR_YELLOW[10] + "'>");
						sb.append("超期风险");
						sb.append("</span>");
						// state = FileUtil.getImageURL(
						// BusinessResource.IMAGE_BALL_YELLOW_1_16,
						// BusinessResource.PLUGIN_ID);
					}
				}
			}
		}
		// if (state != null) {
		// sb.append("<img src='"
		// + state
		// + "' style='margin-top:0;margin-left:2' width='12' height='12' />");
		// }
		sb.append("</span>");

		return sb.toString();
	}

	private String extractSchedualGraphic() {
		StringBuffer sb = new StringBuffer();
		// 如果项目已经完成
		String processIcon = extractProcessIcon();
		if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycle)) {
			String bar = TinyVisualizationUtil.getColorBar(8, null, "96%",
					"#ececec", processIcon, "right", null);
			sb.append(bar);

		} else {
			// 显示进度摘要的图形信息
			int count = milestones.size();
			int barCount = count > 1 ? count : 1;
			double percent = 1d / barCount;

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
						if (isDelayDefinited) {
							colorCode = "red";
						} else if (isDelayEstimated) {
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
					if (isDelayDefinited) {
						colorCode = "red";
					} else if (isDelayEstimated) {
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
				String bar = TinyVisualizationUtil.getColorBar(colorIndex + 3,
						colorCode,
						new DecimalFormat("#.00").format(100 * percent) + "%",
						null, icon, location, null);
				sb.append(bar);
			}
		}

		return sb.toString();
	}

	private String extractProcessIcon() {
		if (ILifecycle.STATUS_CANCELED_VALUE.equals(lifecycle)) {
			return BusinessResource.getImageURL(BusinessResource.IMAGE_WORK2_CANCEL_16);
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycle)) {
			return BusinessResource.getImageURL(BusinessResource.IMAGE_WORK2_FINISH_16);
		} else if (ILifecycle.STATUS_ONREADY_VALUE.equals(lifecycle)) {
			return BusinessResource.getImageURL(BusinessResource.IMAGE_WORK2_READY_16);
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(lifecycle)) {
			return BusinessResource.getImageURL(BusinessResource.IMAGE_WORK2_WIP_16);
		} else if (ILifecycle.STATUS_PAUSED_VALUE.equals(lifecycle)) {
			return BusinessResource.getImageURL(BusinessResource.IMAGE_WORK2_PAUSE_16);
		} else if (ILifecycle.STATUS_NONE_VALUE.equals(lifecycle)) {
			return BusinessResource.getImageURL(BusinessResource.IMAGE_WORK2_READY_16);
		}
		return null;
	}
	
	private String extractSchedualDetailHtml() {
		if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycle)) {
			return "";
		}

		// 取正在进行的为中心，前后各一行
		int index = -1;
		for (int i = 0; i < milestones.size(); i++) {
			Work work = milestones.get(i);
			if (ILifecycle.STATUS_WIP_VALUE.equals(work.getLifecycleStatus())) {
				index = i;
				break;
			}
		}

		String line1;
		String line2;
		String line3;

		if (index == -1) {
			return "";
		} else if (index == 0) {
			line1 = extractWorkLabel(milestones.get(index), true);
			line2 = milestones.size() > 1 ? extractWorkLabel(milestones.get(1),
					false) : "";
			line3 = milestones.size() > 2 ? extractWorkLabel(milestones.get(2),
					false) : "";
		} else {
			line1 = extractWorkLabel(milestones.get(index - 1), false);
			line2 = extractWorkLabel(milestones.get(index), true);
			line3 = milestones.size() > (index + 1) ? extractWorkLabel(
					milestones.get(index + 1), false) : "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(line1);
		sb.append("<br/>");
		sb.append(line2);
		sb.append("<br/>");
		sb.append(line3);
		return sb.toString();
	}

	private String extractWorkLabel(Work work, boolean em) {
		// Date ps = work.getPlanStart();
		Date pf = work.getPlanFinish();
		// Date as = work.getActualStart();
		Date af = work.getActualFinish();

		// String start = as == null ? (ps == null ? "" : String.format("%tF%n",
		// ps)) : String.format("%tF%n", as);
		String finish = af == null ? (pf == null ? "" : String.format("%tF%n",
				pf)) : String.format("%tF%n", af);

		StringBuffer sb = new StringBuffer();
		if (em) {
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:8pt;'>");
			sb.append(work.getLabel());
			// sb.append(" ");
			// sb.append(start);
			// sb.append("~");
			sb.append(finish);
			sb.append(" 进行");
			sb.append("</span>");
		} else {
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:8pt;color:#a0a0a0'>");
			sb.append(work.getLabel());
			// sb.append(" ");
			// sb.append(start);
			// sb.append("~");
			sb.append(finish);
			sb.append("</span>");
		}

		return sb.toString();
	}

	public void doETL() throws Exception {
		now = new Date();
		planFinish = project.getPlanFinish();
		actualFinish = project.getActualFinish();
		planStart = project.getPlanStart();
		actualStart = project.getActualStart();
		ProjectBudget projectBudget = project.getBudget();
		milestones = project.getMileStoneWorks();
		products = project.getProduct();
		lifecycle = project.getLifecycleStatus();
		String desc = project.getDesc();
		List<RemoteFile> coverImageFileList = project.getCoverImages();
		List<PrimaryObject> launchOrgList = project.getLaunchOrganization();
		User charger = project.getCharger();

		Object overCostRatioSetting = Setting
				.getSystemSetting(IModelConstants.S_S_BI_OVER_COST_ESTIMATE);
		double overCostRatio = overCostRatioSetting == null ? .3d : Double
				.valueOf((String) overCostRatioSetting);

		DBObject transfered = new BasicDBObject();

		/**
		 * 
		 * 开始进行数据抽取和转换
		 * 
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		transfered.put(F_TRANSFERDATE, cal.getTimeInMillis());
		transfered.put(F_PROJECTID, project.get_id());

		isDelayDefinited = planFinish != null
				&& ((actualFinish == null && now.after(planFinish)) || (actualFinish != null && actualFinish
						.after(planFinish)));
		transfered.put(F_IS_DELAY_DEFINITED, isDelayDefinited);

		isAdvanceDefinited = planFinish != null && actualFinish != null
				&& actualFinish.before(planFinish);
		transfered.put(F_IS_ADVANCE_DEFINITED, isAdvanceDefinited);

		isDelayEstimated = false;
		if (!isDelayDefinited) {
			for (int i = 0; i < milestones.size() && !isDelayEstimated; i++) {
				isDelayEstimated = milestones.get(i).isDelayFinish();
			}
		} else {
			isDelayEstimated = true;
		}
		transfered.put(F_IS_DELAY_ESTIMATED, isDelayEstimated);

		isAdvanceEstimated = false;
		if (!isAdvanceDefinited) {
			for (int i = 0; i < milestones.size() && !isAdvanceEstimated; i++) {
				isAdvanceEstimated = milestones.get(i).isDelayFinish();
			}
		} else {
			isAdvanceEstimated = true;
		}
		transfered.put(F_IS_ADVANCE_ESTIMATED, isAdvanceEstimated);

		if (planFinish == null || planStart == null || actualStart == null) {
			finishedDurationRatio = 0;
		} else {
			long pd = (planFinish.getTime() - planStart.getTime());
			long ad = now.getTime() - actualStart.getTime();
			finishedDurationRatio = 1d * ad / pd;
		}
		transfered.put(F_FINISHED_DURATION_RATIO, finishedDurationRatio);

		if (projectBudget == null) {
			budget = 0d;
		} else {
			Double value = projectBudget.getBudgetValue();
			budget = value==null?0d:value.doubleValue();
		}
		transfered.put(F_BUDGET, budget);

		/**
		 * 取研发成本
		 */
		// 分摊到工作令号的研发成本
		allocatedInvestment = extractInvestmentInternal(IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		transfered.put(F_INVESTMENT_ALLOCATED, allocatedInvestment);

		// 指定到工作令号研发成本
		designatedInvestment = extractInvestmentInternal(IModelConstants.C_WORKORDER_COST);
		transfered.put(F_INVESTMENT_DESIGNATED, designatedInvestment);

		// 合计研发成本
		investment = allocatedInvestment + designatedInvestment;
		transfered.put(F_INVESTMENT, investment);

		/**
		 * 估计是否超支
		 * 
		 */
		if (ILifecycle.STATUS_WIP_VALUE.equals(lifecycle)) {
			if (budget == 0) {
				isOverCostEstimated = false;
			} else {
				double cr = 1d * investment / budget;
				double dr = finishedDurationRatio;
				isOverCostEstimated = cr - dr >= overCostRatio;
			}
		} else if (ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycle)) {
			isOverCostEstimated = investment > budget;
		} else {
			isOverCostEstimated = false;
		}
		transfered.put(F_IS_OVERCOST_ESTIMATED, isOverCostEstimated);

		/**
		 * 是否确定超支
		 */
		if (budget == 0) {
			isOverCostDefinited = false;
		} else {
			isOverCostDefinited = investment > budget;
		}
		transfered.put(F_IS_OVERCOST_DEFINITED, isOverCostDefinited);

		/**
		 * 取销售收入,成本
		 */

		salesRevenue = 0d;
		salesCost = 0d;
		if (products != null) {
			for (int i = 0; i < products.size(); i++) {
				ProductItem pd = (ProductItem) products.get(i);
				salesRevenue += pd.getSalesRevenue();
				salesCost += pd.getSalesCost();
			}
		}
		transfered.put(F_SALES_COST, salesCost);
		transfered.put(F_SALES_REVENUE, salesRevenue);

		/**************************************************************************
		 * 处理呈现层的优化显示
		 */
		/**
		 * 项目名称
		 */
		try {
			MarkupValidator.getInstance().validate(desc);
			projectDesc = desc;
		} catch (Exception e) {
			projectDesc = Utils.getPlainText(desc);
		}
		transfered.put(F_DESC_TEXT, projectDesc);

		/**
		 * 项目的封面图片地址
		 */
		String coverImageURL;
		if (coverImageFileList.size() > 0) {
			RemoteFile rf = coverImageFileList.get(0);
			coverImageURL = FileUtil.getImageURL(rf.getOutputRefData());
		} else {
			coverImageURL = null;
		}
		transfered.put(F_COVERIMAGE_URL, coverImageURL);

		/**
		 * 项目的发起组织名称
		 */
		String launchOrganizationText = "";
		for (int i = 0; i < launchOrgList.size(); i++) {
			Organization primaryObject = (Organization) launchOrgList.get(i);
			String path = primaryObject.getPath(2);
			if (i == 0) {
				launchOrganizationText += path;
			} else if (i <= 2) {
				launchOrganizationText += ", " + path;
			} else {
				launchOrganizationText += " ..";
				break;
			}
		}
		transfered.put(F_LAUNCH_ORGANIZATION_TEXT, launchOrganizationText);

		/**
		 * 项目负责人
		 */
		String chargerText = charger == null ? "?" : charger.getUsername();
		transfered.put(F_CHARGER_TEXT, chargerText);

		/**
		 * 项目进度HTML
		 */
		// 显示图形化的进度栏
		StringBuffer sb = new StringBuffer();
		sb.append("<span>");
		sb.append(extractSchedualGraphic());
		sb.append("</span>");

		// 显示计划和实际的进度日期
		sb.append("<small>");
		sb.append("<br/>");
		sb.append(extractSchedualText());
		sb.append("</small>");
		transfered.put(F_SCHEDUAL_HTML, sb.toString());

		/**
		 * 项目进度详细信息HTML
		 */
		String schedual_detail = extractSchedualDetailHtml();
		transfered.put(F_SCHEDUAL_DETAIL_HTML, schedual_detail);

		/**
		 * 
		 * 进行数据装载，保存至Project
		 * 
		 */
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		WriteResult ws = col.update(project.queryThis(),
				new BasicDBObject().append("$set", new BasicDBObject().append(F_ETL, transfered)));
		String error = ws.getError();
		if (error != null) {
			throw new Exception(error);
		}
	}



}
