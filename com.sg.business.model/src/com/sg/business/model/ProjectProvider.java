package com.sg.business.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public abstract class ProjectProvider extends PrimaryObject {

	
	public ProjectSetSummaryData summaryData;
	
	
	/**
	 * 成本正常
	 */
	public static String F_SUMMARY_NORMAL_COST = "s11";

	/**
	 * 成本超支
	 */
	public static String F_SUMMARY_OVER_COST = "s12";
	
	

	/**
	 * 参数名称：按年计算
	 */
	public static final String PARAMETER_SUMMARY_BY_YEAR = "y";

	/**
	 * 参数名称:按季度计算
	 */
	public static final String PARAMETER_SUMMARY_BY_QUARTER = "q";

	/**
	 * 参数:按月计算
	 */
	public static final String PARAMETER_SUMMARY_BY_MONTH = "m";

	private Object[] parametes;

	private HashMap<String, Object> summaryInfor;

	public ProjectProvider() {
		super();
		summaryInfor = new HashMap<String, Object>();
	}

	public abstract List<PrimaryObject> getProjectSet();

	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
	}

	@Override
	public void doInsert(IContext context) throws Exception {
	}

	/**
	 * 获得项目集合的名称
	 * 
	 * @return
	 */
	public abstract String getProjectSetName();

	/**
	 * 获得项目集封面图片
	 * 
	 * @return
	 */
	public abstract String getProjectSetCoverImage();

	/**
	 * 获得项目集 摘要数据
	 * 
	 * @param key
	 *            摘要数据的字段名
	 * @param year
	 * @return
	 */
	public final Object getSummaryValue(String key) {
		return summaryInfor.get(key);
	}

	/**
	 * 设置合计值
	 * 
	 * @param data
	 */
	public final void setSummaryDate(Map<String, Object> data) {
		summaryInfor.clear();
		summaryInfor.putAll(data);
	}

	/**
	 * 设置查询参数
	 * 
	 * @param parameters
	 */
	public void setParameters(Object[] parameters) {
		this.parametes = parameters;
	}

	/**
	 * 获取查询参数
	 * 
	 * @return
	 */
	public Object[] getParameters() {
		return this.parametes;
	}

	public Date getStartDate() throws Exception {
		// TODO 根据条件获得起始时间
		// parametes[0] 为传入时间
		// parameters[1]为条件，参考PARA开头的常量，
		// 如果parameters[1] 为PARAMETER_SUMMARY_BY_YEAR,
		// 应该将parameters[0]强转为Calender并返回该年的第一天
		// 如果参数空或错误，抛出异常
		Date start;
		Calendar calendar = (Calendar) parametes[0];
		switch ((String) parametes[1]) {
		case PARAMETER_SUMMARY_BY_YEAR:
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();
			return start;
		case PARAMETER_SUMMARY_BY_QUARTER:
			int i = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, i / 3 * 3);
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();
			return start;
		case PARAMETER_SUMMARY_BY_MONTH:
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();
			return start;
		default:
			throw new Exception("时间参数异常");
		}
	}

	public Date getEndDate() throws Exception {
		Date end;
		Calendar calendar = Calendar.getInstance();
		Date start = getStartDate();
		calendar.setTime(start);
		switch ((String) parametes[1]) {
		case PARAMETER_SUMMARY_BY_YEAR:
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
			calendar.set(Calendar.MILLISECOND,
					calendar.get(Calendar.MILLISECOND) - 1);
			end = calendar.getTime();
			return end;
		case PARAMETER_SUMMARY_BY_QUARTER:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 3);
			calendar.set(Calendar.MILLISECOND,
					calendar.get(Calendar.MILLISECOND) - 1);
			end = calendar.getTime();
			return end;
		case PARAMETER_SUMMARY_BY_MONTH:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			calendar.set(Calendar.MILLISECOND,
					calendar.get(Calendar.MILLISECOND) - 1);
			end = calendar.getTime();
			return end;
		default:
			throw new Exception("时间参数异常");
		}
	}

//	public abstract void setF_SUMMARY_TOTAL(int count);
//	public abstract void setF_SUMMARY_FINISHED(int count);
//	public abstract void setF_SUMMARY_PROCESSING(int count);
//	
//	public abstract void setF_SUMMARY_NORMAL_PROCESS(int count);
//
//	public abstract void setF_SUMMARY_DELAY(int count);
//
//	public abstract void setF_SUMMARY_ADVANCE(int count);
//
//	public abstract void setF_SUMMARY_NORMAL_COST(int count);
//
//	public abstract void setF_SUMMARY_OVER_COST(int count);
	
	

}
