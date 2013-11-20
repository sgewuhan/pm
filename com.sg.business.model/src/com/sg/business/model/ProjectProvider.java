package com.sg.business.model;

import java.util.List;

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
	public static final String F_SUMMARY_ADVANCE = null;

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
	public abstract Object getSummaryValue(String key, Object...objects);
	
	

}
