package com.tmt.gs.validation;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;
import com.tmt.gs.nls.Messages;

public class GSRelatedDeptValidation extends AbstractValidator {

	public GSRelatedDeptValidation() {
	}

	@Override
	protected String getValidMessage(PrimaryObject data) {
		if(Utils.isDenied((String) data.getValue("choice"))){ //$NON-NLS-1$
			return null;
		}
		if("ÊÇ".equals(data.getValue("hasother"))){ //$NON-NLS-1$ //$NON-NLS-2$
			if(data.getValue("other")==null){ //$NON-NLS-1$
				return Messages.get().RelatedDeptValidation_4;
			}
		}
		return null;
		
	}

}
