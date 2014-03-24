package com.sg.business.model;

import java.util.HashMap;
import java.util.Map;

public class RoleParameter implements IRoleParameter {

	@Override
	public Map<String, Object> getParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IRoleParameter.TYPE, IRoleParameter.TYPE_NONE);
		return parameters;
	}

}
