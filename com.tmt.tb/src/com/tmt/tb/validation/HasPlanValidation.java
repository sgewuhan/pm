package com.tmt.tb.validation;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;
import com.tmt.tb.nls.Messages;

public class HasPlanValidation extends AbstractValidator {

	public HasPlanValidation() {
	}

	@Override
	protected String getValidMessage(PrimaryObject data) {
		if(Utils.isDenied((String) data.getValue("choice"))){ //$NON-NLS-1$
			return null;
		}
		if("ÊÇ".equals(data.getValue("hasplan"))){ //$NON-NLS-1$ //$NON-NLS-2$
			if(data.getValue("plan")==null){ //$NON-NLS-1$
				return Messages.get().HasPlanValidation_4;
			}
		}
		return null;
		
	}

}
