package com.sg.business.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public abstract class ProjectProvider extends PrimaryObject {

	/**
	 * 项目总数
	 */
	public static final String F_SUMMARY_TOTAL = "s1";
	
	/**
	 * 完成项目数
	 */
	public static final String F_SUMMARY_FINISHED = "s2";

	/**
	 * 进展中项目数
	 */
	public static final String F_SUMMARY_PROCESSING = "s3";

	/**
	 * 正常进行中
	 */
	public static final String F_SUMMARY_NORMAL_PROCESS = "s4";

	/**
	 * 延期
	 */
	public static final String F_SUMMARY_DELAY = "s5";

	/**
	 * 提前
	 */
	public static final String F_SUMMARY_ADVANCE = "s8";

	/**
	 * 成本正常
	 */
	public static final String F_SUMMARY_NORMAL_COST = "s6";
	
	/**
	 * 成本超支
	 */
	public static final String F_SUMMARY_OVER_COST = "s7";
	

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
	
	
	public ProjectProvider(){
		super();
		summaryInfor = new HashMap<String,Object>();
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
	 * @return
	 */
	public abstract String getProjectSetName() ;

	/**
	 * 获得项目集封面图片
	 * @return
	 */
	public abstract String getProjectSetCoverImage();

	/**
	 * 获得项目集 摘要数据
	 * @param key 摘要数据的字段名
	 * @param year 
	 * @return
	 */
	public final Object getSummaryValue(String key){
		return summaryInfor.get(key);
	}
	
	/**
	 * 设置合计值
	 * @param data
	 */
	public final void setSummaryDate(Map<String,Object>data){
		summaryInfor.clear();
		summaryInfor.putAll(data);
	}

	/**
	 * 设置查询参数
	 * @param parameters
	 */
	public void setParameters(Object[] parameters) {
		this.parametes = parameters;
	}
	
	/**
	 * 获取查询参数
	 * @return
	 */
	public Object[] getParameters(){
		return this.parametes;
	}
	
	public Date getStartDate(){
		//TODO 根据条件获得起始时间
		//parametes[0] 为传入时间
		//parameters[1]为条件，参考PARA开头的常量，
		//如果parameters[1] 为PARAMETER_SUMMARY_BY_YEAR, 
		//应该将parameters[0]强转为Calender并返回该年的第一天
		//如果参数空或错误，抛出异常
		return null;
	}
	
	public Date getEndDate(){
		//TODO 根据条件获得结束时间
		//参考获得开始时间的方法
		return null;
	}

}
