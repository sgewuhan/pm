package com.sg.business.model.dataset.calendarsetting;

import java.util.Date;

public interface IWorkingTimeCaculator {

	double getWorkingTime(Date date);

	double getWorkingTime(String date);

	int getWorkingDays(Date startDate, Date endDate);

}
