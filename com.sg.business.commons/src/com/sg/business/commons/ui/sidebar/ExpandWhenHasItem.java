package com.sg.business.commons.ui.sidebar;

import com.sg.widgets.part.IRefreshablePart;
import com.sg.widgets.part.ISidebarItemExpandedWhen;
import com.sg.widgets.registry.config.ExpandItemConfigurator;

public class ExpandWhenHasItem implements ISidebarItemExpandedWhen {

	public ExpandWhenHasItem() {
	}

	@Override
	public Boolean isExpanded(ExpandItemConfigurator expandItemConfigurator,
			IRefreshablePart control) {
		if(control instanceof AbstractListViewSideItem){
			AbstractListViewSideItem item = (AbstractListViewSideItem) control;
			return item.isExpand();
		}
		return null;
	}


}
