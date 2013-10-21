package com.sg.business.model.bpmservice;

import java.util.Map;

public class MessageServiceTest001 extends MessageService {

	@Override
	public Map<String, Object> run(Object parameter) {
		sendMessage("aaaaa",null,"act_prj_admin","reviewer_list");
		return null;
	}

}
