package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.nls.Messages;

/**
 * �����߶���
 * @author jinxitao
 *
 */
public class ParticipateDefinition extends PrimaryObject {
	
	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().ParticipateDefinition_0;
	}
}
