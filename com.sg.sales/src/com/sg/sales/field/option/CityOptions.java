package com.sg.sales.field.option;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.options.IFieldOptionProvider;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.FieldConfigurator;
import com.sg.widgets.registry.config.Option;

public class CityOptions implements IFieldOptionProvider {

	public CityOptions() {
	}

	@Override
	public Option getOption(Object input, Object data, String key,
			Object value) {
		PrimaryObjectEditorInput dataInput = (PrimaryObjectEditorInput)input;
		Object prov = ((PrimaryObject)data).getValue("prov");
		FieldConfigurator field = dataInput.getEditorConfigurator().getField("prov");
		String optionId = field.getOption();
		Option root = (Option) Widgets.getOptionRegistry().getConfigurator(optionId);
		Option[] children = root.getChildren();
		for (int i = 0; i < children.length; i++) {
			if(children[i].getValue().equals(prov)){
				return children[i];
			}
		}
		return null;
	}

}
