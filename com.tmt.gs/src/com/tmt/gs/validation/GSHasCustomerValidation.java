package com.tmt.gs.validation;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;
import com.tmt.gs.nls.Messages;

public class GSHasCustomerValidation extends AbstractValidator {

	public GSHasCustomerValidation() {
	}

	@Override
	protected String getValidMessage(PrimaryObject data) {
		if(Utils.isDenied((String) data.getValue("choice"))){ //$NON-NLS-1$
			return null;
		}
		if("ÊÇ".equals(data.getValue("hascustomer"))){ //$NON-NLS-1$ //$NON-NLS-2$
			if(data.getValue("customer")==null){ //$NON-NLS-1$
				return Messages.get().HasCustomerValidation_4;
			}
		}
		return null;
		
	}

}
