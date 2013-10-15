package com.sg.business.model;

import java.util.Date;
import java.util.List;

import com.mobnut.db.model.PrimaryObject;

public interface IWorksSummary {

	/**
	 * ���ĳ��ʱ��ε�ʵ�ʹ�ʱ
	 * @param start
	 * @param end
	 * @return
	 */
	double getWorksSummary(Date start, Date end);

	/**
	 * ���ĳ�����ڵ�ʵ�ʹ�ʱ
	 * @param date
	 * @param cache 
	 * @return
	 */
	double getWorksSummaryOfDay(Date date);

	/**
	 * ���ĳ�����ڵĹ�����¼�Ĺ���
	 * @param dateCode
	 * @return
	 */
	List<PrimaryObject[]> getWorkOfWorksSummaryOfDateCode(String userid,Date date);

}
