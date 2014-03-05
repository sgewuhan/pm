package com.sg.sales.label;


import com.sg.sales.model.IDataStatusControl;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class DataStatusLabel extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof IDataStatusControl){
			IDataStatusControl control = (IDataStatusControl) element;
			return control.getStatusText();
		}
		return super.getText(element);
	}

}
