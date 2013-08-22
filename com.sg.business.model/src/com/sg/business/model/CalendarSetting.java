package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

public class CalendarSetting extends PrimaryObject {

	
	/**
	 * 开始日期
	 */
    public static final String F_START_DATE = "startdate";
    /**
     * 结束日期
     */
    public static final String F_END_DATE = "enddate";
    /**
     * 工作日
     */
    public static final String F_WORKINGDAY = "workingday";
    /**
     * 条件
     */
    public static final String F_CONDITION = "condition";
    /**
     * 计算
     */
    public static final String F_OPERATOR = "operator";
    /**
     * 值
     */
    public static final String F_VALUE = "value";
    /**
     * 优先级
     */
    public static final String F_SEQ = "seq";
    /**
     * 每天工时（小时）
     */
    public static final String F_WORKING_TIME = "worktime";
	public static final String F_PROJECT_ID = "project_id";

    
}
