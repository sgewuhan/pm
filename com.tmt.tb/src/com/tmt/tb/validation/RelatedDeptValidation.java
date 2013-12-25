package com.tmt.tb.validation;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;
import com.tmt.tb.nls.Messages;

public class RelatedDeptValidation extends AbstractValidator {

	public RelatedDeptValidation() {
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
