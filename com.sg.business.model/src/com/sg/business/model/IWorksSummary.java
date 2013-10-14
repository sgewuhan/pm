package com.sg.business.model;

import java.util.Date;

public interface IWorksSummary {

	/**
	 * 获得某个时间段的实际工时
	 * @param start
	 * @param end
	 * @return
	 */
	double getWorksSummary(Date start, Date end);

	/**
	 * 获得某个日期的实际工时
	 * @param date
	 * @param cache 
	 * @return
	 */
	double getWorksSummaryOfDay(Date date);

}
