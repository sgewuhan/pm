package com.sg.business.project.action;

import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.NavigatorAction;

public class RecalculateSEQ extends NavigatorAction {

	public RecalculateSEQ() {
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_WBS_SORT_24));
		setText("÷ÿ–¬≈≈¡––Ú∫≈");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}

}
