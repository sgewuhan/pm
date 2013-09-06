package com.sg.business.model.event;

import java.util.Map;

import com.sg.business.model.Work;

public interface IEventAction {

	void run(Work work, Map<String, Object> params) throws Exception;

}
