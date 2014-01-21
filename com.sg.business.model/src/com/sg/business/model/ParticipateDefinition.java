package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.nls.Messages;

/**
 * 参与者定义
 * @author jinxitao
 *
 */
public class ParticipateDefinition extends PrimaryObject {
	
	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().ParticipateDefinition_0;
	}
}
