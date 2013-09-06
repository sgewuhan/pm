package com.sg.business.model.event;

import com.sg.business.model.Work;

public interface IEventAction {

	void run(Work work) throws Exception;

}
