package com.sg.sales.label;

import java.text.DecimalFormat;

import com.mobnut.commons.util.Utils;
import com.sg.sales.model.POItem;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class POItemLabel extends ConfiguratorColumnLabelProvider {

	public POItemLabel(ColumnConfigurator configurator) {
		super(configurator);
	}

	public POItemLabel() {
	}
	
	@Override
	public String getText(Object element) {
		if(element instanceof POItem){
			POItem poItem = (POItem) element;
			DecimalFormat df = new DecimalFormat(Utils.NF_RMB_MONEY);
			return df.format(poItem.getSummary());
		}
		return super.getText(element);
	}

}
